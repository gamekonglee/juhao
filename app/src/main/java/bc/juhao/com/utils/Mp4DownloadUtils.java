package bc.juhao.com.utils;

import android.os.Handler;
import android.os.Message;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by gamekonglee on 2018/4/20.
 */

public class Mp4DownloadUtils { /** 播放MP4消息 */
public static final int PLAYER_MP4_MSG = 0x1001;
    /** 下载MP4完成 */
    public static final int DOWNLOAD_MP4_COMPLETION = 0x1002;
    /** 下载MP4失败 */
    public static final int DOWNLOAD_MP4_FAIL = 0x1003;

    /**
     * 下载MP4文件
     * @param url
     * @param fileName
     * @param handler
     * @return
     */
    public static File downloadMp4File(final String url, final String fileName,
                                       final Handler handler) {
        final File mp4File = new File(fileName);
        downloadVideoToFile(url, mp4File, handler);
        return mp4File;
    }

    /**
     * 下载视频数据到文件
     * @param url
     * @param dstFile
     * @param moovSize
     */
    private static void downloadVideoToFile(final String url, final File dstFile, final Handler handler) {
        Thread thread = new Thread() {

            @Override
            public void run() {
                super.run();
                try {
                    URL request = new URL(url);
                    HttpURLConnection httpConn = (HttpURLConnection) request.openConnection();
                    httpConn.setConnectTimeout(3000);
                    httpConn.setDoInput(true);
                    httpConn.setDoOutput(true);
                    httpConn.setDefaultUseCaches(false);

                    httpConn.setRequestMethod("GET");
                    httpConn.setRequestProperty("Charset", "UTF-8");
                    httpConn.setRequestProperty("Accept-Encoding", "identity");

                    int responseCode = httpConn.getResponseCode();
                    if ((responseCode == HttpURLConnection.HTTP_OK)) {
                        // 获取文件总长度
                        int totalLength = httpConn.getContentLength();
                        InputStream is = httpConn.getInputStream();

                        if (dstFile.exists()) {
                            dstFile.delete();
                        }
                        dstFile.createNewFile();
                        RandomAccessFile raf = new RandomAccessFile(dstFile, "rw");
                        BufferedInputStream bis = new BufferedInputStream(is);
                        int readSize = 0;

                        int mdatSize = 0;// mp4的mdat长度
                        int headSize = 0;// mp4头长度
                        byte[] boxSizeBuf = new byte[4];
                        byte[] boxTypeBuf = new byte[4];
                        // 由MP4的文件格式读取
                        int boxSize = readBoxSize(bis, boxSizeBuf);
                        String boxType = readBoxType(bis, boxTypeBuf);
                        raf.write(boxSizeBuf);
                        raf.write(boxTypeBuf);

                        while (!boxType.equalsIgnoreCase("moov")) {
                            int count = boxSize - 8;
                            if (boxType.equalsIgnoreCase("ftyp")) {
                                headSize += boxSize;
                                byte[] ftyps = new byte[count];
                                bis.read(ftyps, 0, count);
                                raf.write(ftyps, 0, count);
                            } else if (boxType.equalsIgnoreCase("mdat")) {
                                // 标记mdat数据流位置，在后面reset时读取
                                bis.mark(totalLength - headSize);
                                // 跳过mdat数据
                                skip(bis, count);
                                mdatSize = count;
                                byte[] mdatBuf = new byte[mdatSize];
                                raf.write(mdatBuf);
                            } else if (boxType.equalsIgnoreCase("free")) {
                                headSize += boxSize;
                            }

                            boxSize = readBoxSize(bis, boxSizeBuf);
                            boxType = readBoxType(bis, boxTypeBuf);
                            raf.write(boxSizeBuf);
                            raf.write(boxTypeBuf);
                        }

                        // 读取moov数据
                        byte[] buffer = new byte[4096];
                        int moovSize = 0;
                        while ((readSize = bis.read(buffer)) != -1) {
                            moovSize += readSize;
                            raf.write(buffer, 0, readSize);
                        }

                        // 返回到mdat数据开始
                        bis.reset();
                        // 设置文件指针偏移到mdat位置
                        long offset = raf.getFilePointer() - moovSize - mdatSize - 8;
                        raf.seek(offset);

                        // 读取mdat数据，设置mp4初始mdat的缓存大小
                        int buf_size = 56 * 1024;// 56kb
                        int downloadCount = 0;
                        boolean viable = false;
                        while (mdatSize > 0) {
                            readSize = bis.read(buffer);
                            raf.write(buffer, 0, readSize);
                            mdatSize -= readSize;
                            downloadCount += readSize;
                            if (handler != null && !viable && downloadCount >= buf_size) {
                                viable = true;
                                // 发送开始播放视频消息
                                sendMessage(handler, PLAYER_MP4_MSG, null);
                            }
                        }
                        // 发送下载消息
                        if (handler != null) {
                            sendMessage(handler, DOWNLOAD_MP4_COMPLETION, null);
                        }

                        bis.close();
                        is.close();
                        raf.close();
                        httpConn.disconnect();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    sendMessage(handler, DOWNLOAD_MP4_FAIL, null);
                }
            }

        };
        thread.start();
        thread = null;
    }

    /**
     * 发送下载消息
     * @param handler
     * @param what
     * @param obj
     */
    private static void sendMessage(Handler handler, int what, Object obj) {
        if (handler != null) {
            Message msg = new Message();
            msg.what = what;
            msg.obj = obj;
            handler.sendMessage(msg);
        }
    }

    /**
     * 跳转
     * @param is
     * @param count 跳转长度
     * @throws IOException
     */
    private static void skip(BufferedInputStream is, long count) throws IOException {
        while (count > 0) {
            long amt = is.skip(count);
            if (amt == -1) {
                throw new RuntimeException("inputStream skip exception");
            }
            count -= amt;
        }
    }

    /**
     * 读取mp4文件box大小
     * @param is
     * @param buffer
     * @return
     */
    private  static int readBoxSize(InputStream is, byte[] buffer) {
        int sz = fill(is, buffer);
        if (sz == -1) {
            return 0;
        }

        return bytesToInt(buffer, 0, 4);
    }

    /**
     * 读取MP4文件box类型
     * @param is
     * @param buffer
     * @return
     */
    private static String readBoxType(InputStream is, byte[] buffer) {
        fill(is, buffer);

        return byteToString(buffer);
    }

    /**
     * byte转换int
     * @param buffer
     * @param pos
     * @param bytes
     * @return
     */
    private static int bytesToInt(byte[] buffer, int pos, int bytes) {
        /*
         * int intvalue = (buffer[pos + 0] & 0xFF) << 24 | (buffer[pos + 1] &
         * 0xFF) << 16 | (buffer[pos + 2] & 0xFF) << 8 | buffer[pos + 3] & 0xFF;
         */
        int retval = 0;
        for (int i = 0; i < bytes; ++i) {
            retval |= (buffer[pos + i] & 0xFF) << (8 * (bytes - i - 1));
        }
        return retval;
    }

    /**
     * byte数据转换String
     * @param buffer
     * @return
     */
    private static String byteToString(byte[] buffer) {
        assert buffer.length == 4;
        String retval = new String();
        try {
            retval = new String(buffer, 0, buffer.length, "ascii");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return retval;
    }

    private static int fill(InputStream stream, byte[] buffer) {
        return fill(stream, 0, buffer.length, buffer);
    }

    /**
     * 读取流数据
     * @param stream
     * @param pos
     * @param len
     * @param buffer
     * @return
     */
    private static int fill(InputStream stream, int pos, int len, byte[] buffer) {
        int readSize = 0;
        try {
            readSize = stream.read(buffer, pos, len);
            if (readSize == -1) {
                return -1;
            }
            assert readSize == len : String.format("len %d readSize %d", len,
                    readSize);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return readSize;
    }
}
