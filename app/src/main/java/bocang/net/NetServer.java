package bocang.net;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import bc.juhao.com.cons.Constance;
import bc.juhao.com.utils.MyShare;
import bc.juhao.com.utils.UIUtils;
import bocang.utils.AppConfig;
import bocang.utils.AppLog;
import bocang.utils.AppUtils;

public class NetServer {

    private final static String BOUNDARY = "-------AaB03x";

    private Map<String, String> params = null;

    private String sem = null;

    public NetServer() {
        params = new LinkedHashMap<String, String>(3);
    }

    public void addParameter(String name, String value) {
        params.put(name, value);
    }

    /**
     * http post 方式上传
     *
     * @return
     */
    public String getString(String urlPath) {
        // 变量.定义
        sem = null;

        String ret = null;

        HttpURLConnection conn = null;
        OutputStream out = null;
        InputStream inStream = null;

        int i, j;
        // params
        byte post_data[] = null;
        try {
            post_data = getRequestParamToString(true).getBytes("utf-8");
        } catch (java.io.UnsupportedEncodingException e) {
            AppLog.error(e);
            sem = e.getMessage();
        }
        if (post_data == null)
            return null;

        String currentUserToken = "";
        //
        try {
            URL url = new URL(urlPath);

            conn = (HttpURLConnection) url.openConnection();

            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");

            conn.setRequestProperty("App-Version", AppConfig.APP_VERSION);
            conn.setRequestProperty("App-Width", String.valueOf(AppConfig.getScreenWidth()));
            conn.setRequestProperty("App-Height", String.valueOf(AppConfig.getScreenHeight()));
            conn.setRequestProperty("App-User-Token", currentUserToken);

            conn.setRequestProperty("Charset", "UTF-8");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Content-Length", String.valueOf(post_data.length));
            conn.setRequestProperty("Content-Type",
                    "multipart/form-data;boundary=" + BOUNDARY);

            conn.setConnectTimeout(30000);
            conn.setReadTimeout(30000);

            out = conn.getOutputStream();
//            out.write(post_data);
            out.flush();
            out.close();
            out = null;

            if (conn.getResponseCode() == 200) {
                if (AppConfig.API_SESSIONID == null) {
                    String cookie = conn.getHeaderField("Set-Cookie");
                    if (cookie != null) {
                        i = cookie.indexOf("PHPSESSID=");
                        if (i != -1) {
                            i += "PHPSESSID=".length();
                            j = cookie.indexOf(';', i);
                            if (j != -1) {
                                AppConfig.API_SESSIONID = cookie.substring(i, j);
                            }
                        }
                    }
                }

                // 更新用户令牌
                String newAppUserToken = conn.getHeaderField("App-User-Token");
                if (!AppUtils.isEmpty(newAppUserToken) && !newAppUserToken.equals(currentUserToken)) {
                    AppConfig.setUserToken(newAppUserToken);
                }

                // get response content
                inStream = conn.getInputStream();
                ret = getAns(inStream);
                inStream.close();
                inStream = null;
            }

            conn.disconnect();
            conn = null;

        } catch (MalformedURLException e) {
            AppLog.error(e);
            sem = e.getMessage();

        } catch (java.net.SocketTimeoutException e) {
            AppLog.error(e);
            sem = e.getMessage();

        } catch (IOException e) {
            AppLog.error(e);
            sem = e.getMessage();
        } finally {

        }

        return ret;
    }

    /**
     * http get方式 获取图片
     */
    public Bitmap getBitmap() {

        sem = null;

        Bitmap ret = null;

        // url
        String path = AppConfig.SOURCE_URL;

        // params
        StringBuilder sb = new StringBuilder();

        Iterator<Map.Entry<String, String>> itr = params.entrySet().iterator();

        while (itr.hasNext()) {
            Map.Entry<String, String> entry = (Map.Entry<String, String>) itr.next();
            sb.append(entry.getKey());
            sb.append("=");
            sb.append(entry.getValue());
            sb.append("&");
        }
        path += "?" + sb.toString();

        // open
        try {
            URL url = new URL(path);
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();

            conn.setConnectTimeout(10000);
            conn.setReadTimeout(10000);
            conn.setInstanceFollowRedirects(true);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            int code = conn.getResponseCode();
            if (code == 200) {
                InputStream in = conn.getInputStream();
                ret = BitmapFactory.decodeStream(in);
                in.close();
            } else {
                sem = "HttpURLConnection getResponseCode != 200 in [" + path + ']';
            }
        } catch (MalformedURLException e) {
            AppLog.error(e);
            sem = e.getMessage();
        } catch (IOException e) {
            AppLog.error(e);
            sem = e.getMessage();
        } catch (Exception e) {
            AppLog.error(e);
            sem = e.getMessage();
        }

        return ret;

    }

    /**
     * http post 方式上传图片
     *
     * @param elementName
     * @param fileName
     * @return
     */
    public String getString(String elementName, String fileName,String urlPath) {

        // 变量.定义
        sem = null;

        String ret = null;

        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        InputStream inStream = null;

        int bytesRead, bytesAvailable, bufferSize;

        byte[] buffer;

        int maxBufferSize = 1048576; // 1024 * 1024 : 1M

        //
        try {

            FileInputStream fileInputStream = new FileInputStream(new File(
                    fileName));
            URL url = new URL(urlPath);

            conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");

            if (AppConfig.API_SESSIONID != null) {
                conn.setRequestProperty("cookie", AppConfig.API_SESSIONID);
            }

            conn.setRequestProperty("App-Version", AppConfig.APP_VERSION);
            conn.setRequestProperty("App-Width", String.valueOf(AppConfig.getScreenWidth()));
            conn.setRequestProperty("App-Height", String.valueOf(AppConfig.getScreenHeight()));
            conn.setRequestProperty("App-User-Token", "");

            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + BOUNDARY);

            dos = new DataOutputStream(conn.getOutputStream());
            dos.writeBytes(getRequestParamToString(false));

            dos.writeBytes("--");
            dos.writeBytes(BOUNDARY);
            dos.writeBytes("\r\n");

            dos.writeBytes("Content-Disposition: form-data; name=\"");
            dos.writeBytes(elementName);
            dos.writeBytes("\";filename=\"");
            dos.writeBytes(fileName);
            dos.writeBytes("\"\r\n\r\n");

            bytesAvailable = fileInputStream.available();
            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            buffer = new byte[bufferSize];

            bytesRead = fileInputStream.read(buffer, 0, bufferSize);

            while (bytesRead > 0) {
                dos.write(buffer, 0, bufferSize);
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            }

            dos.writeBytes("\r\n");
            dos.writeBytes("--");
            dos.writeBytes(BOUNDARY);
            dos.writeBytes("--");
            dos.writeBytes("\r\n");

            // close streams

            fileInputStream.close();
            dos.flush();
            dos.close();

        } catch (MalformedURLException e) {
            AppLog.error(e);
            sem = e.getMessage();
        } catch (IOException e) {
            AppLog.error(e);
            sem = e.getMessage();
        }

        try {
            if (conn.getResponseCode() == 200) {
                inStream = conn.getInputStream();
                ret = getAns(inStream);
                inStream.close();
            }


        } catch (IOException e) {
            AppLog.error(e);
            sem = e.getMessage();
        }

        return ret;

    }

    public String getSEM() {
        return sem;
    }

    private String getAns(InputStream in) {

        String ret = null;

        int socketInputStreamReadNumber = 1024;

        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        try {
            while (true) {
                byte[] part = new byte[socketInputStreamReadNumber];

                int len = in.read(part, 0, socketInputStreamReadNumber);
                if (len == -1)
                    break;

                buffer.write(part, 0, len);
            }

            buffer.flush();
            ret = new String(buffer.toByteArray()).trim();

        } catch (IOException e) {
            ret = null;
            AppLog.error(e);

        } finally {
            try {
                buffer.close();
            } catch (IOException e) {

            }
        }

        //
        return ret;
    }

    /**
     * 将参数转换成http(Post)参数格式
     */
    private String getRequestParamToString(boolean endFlag) {
        StringBuilder sb = new StringBuilder();

        Iterator<Map.Entry<String, String>> itr = params.entrySet().iterator();

        while (itr.hasNext()) {

            Map.Entry<String, String> entry = (Map.Entry<String, String>) itr
                    .next();
            String key = entry.getKey();
            Object value = entry.getValue();

            sb.append("--");
            sb.append(BOUNDARY);
            sb.append("\r\n");

            sb.append("Content-Disposition: form-data; name=\"");
            sb.append(key);
            sb.append("\"\r\n\r\n");
            sb.append(value);

            sb.append("\r\n");
            sb.append("--");
            sb.append(BOUNDARY);
            if (endFlag && (!itr.hasNext()))
                sb.append("--");
            sb.append("\r\n");
        }

        return sb.toString();
    }

    /**
     * POST的网络请求
     *
     * @param urlPath
     * @return
     */
    public String doPost(String urlPath) {
        String result = "";
        BufferedReader reader = null;
        HttpURLConnection conn = null;
        InputStream inStream = null;
        String token= MyShare.get(UIUtils.getContext()).getString(Constance.TOKEN);

        try {
            URL url = new URL(urlPath);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("X-bocang-Authorization", token);
            conn.setDoOutput(true);
            String parames = "";
            for (Map.Entry<String, String> entry : params.entrySet()) {
                parames += ("&" + entry.getKey() + "=" + entry.getValue());
            }
            if(parames.length()>0){
                conn.getOutputStream().write(parames.substring(1).getBytes());
            }
            if( conn.getResponseCode()==200){
                reader=new BufferedReader(new
                        InputStreamReader(conn.getInputStream()));
                String line="";
                while((line=reader.readLine())!=null) {
                    result+=line;
                }
            }

        } catch (IOException e) {
            AppLog.error(e);
            sem = e.getMessage();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    AppLog.error(e);
                    sem = e.getMessage();
                }
            }
            if (conn != null) {
                conn.disconnect();
            }
        }
        return result;
    }

    /**
     * GET的网络请求
     *
     * @param urlPath
     * @return
     */
    public String doGet(String urlPath) {
        String result = "";
        BufferedReader reader = null;
        HttpURLConnection conn = null;
        InputStream inStream = null;
        try {
            URL url = new URL(urlPath);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            int code = conn.getResponseCode();
            if (code == 200) {
                inStream = conn.getInputStream();
                result = getAns(inStream);
                inStream.close();
            }

        } catch (Exception e) {
            AppLog.error(e);
            sem = e.getMessage();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (conn != null) {
                conn.disconnect();
            }
        }

        return result;
    }

}

