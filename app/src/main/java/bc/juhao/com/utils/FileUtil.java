package bc.juhao.com.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Picture;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.util.Base64;
import android.util.Log;
import android.webkit.WebView;

import com.aliyun.iot.ilop.demo.DemoApplication;
import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.donkingliang.imageselector.utils.ImageSelectorUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import bc.juhao.com.cons.Constance;
import bc.juhao.com.ui.activity.IssueApplication;
import bc.juhao.com.ui.activity.user.VideoShotActivity;
import bocang.utils.PermissionUtils;

import static android.os.Environment.MEDIA_MOUNTED;

/**
 * Created by xpHuang on 2016/9/5.
 */
public class FileUtil {

    private static final  int REQUEST_CODE = 400;

    /**
     * 获取自定义sd卡上的文件目录
     *
     * @param context
     * @param cacheDir
     * @return 文件夹创建成功返回File or 文件夹创建失败返回null
     */
    public static File getOwnFilesDir(Context context, String cacheDir) {
        File appCacheDir = null;
        if (MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                && context
                .checkCallingOrSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") == PackageManager.PERMISSION_GRANTED) {
            appCacheDir = new File(Environment.getExternalStorageDirectory(),
                    cacheDir);
        }
        if (appCacheDir == null
                || (!appCacheDir.exists() && !appCacheDir.mkdirs())) {
            appCacheDir = context
                    .getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        }
        return appCacheDir;
    }

    /**
     * 截取webView快照(webView加载的整个内容的大小)
     * @param webView
     * @return
     */
    public static Bitmap captureWebView(WebView webView){
        Picture snapShot = webView.capturePicture();

        Bitmap bmp = Bitmap.createBitmap(snapShot.getWidth(),snapShot.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmp);
        snapShot.draw(canvas);
        return bmp;
    }
    /**
     * 截取webView快照(webView加载的整个内容的大小)
     * @param webView
     * @return
     */
    public static Bitmap captureWebView(com.tencent.smtt.sdk.WebView webView){
        Picture snapShot = webView.capturePicture();

        Bitmap bmp = Bitmap.createBitmap(snapShot.getWidth(),snapShot.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmp);
        snapShot.draw(canvas);
        return bmp;
    }

    /**
     * 拍照获取相片
     **/
    public static void takePhoto(final Activity context) {
        ActivityCompat.requestPermissions(context,
                new String[]{Manifest.permission.CAMERA},
                1);
        PackageManager packageManager = context.getPackageManager();
        int permission = packageManager.checkPermission("android.permission.CAMERA", "bc.juhao.com");
        if (PackageManager.PERMISSION_GRANTED != permission) {
            return;
        } else {
            // 图片名称 时间命名
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
            Date date = new Date(System.currentTimeMillis());
            DemoApplication.imagePath = format.format(date);
            DemoApplication.cameraPath = FileUtil.getOwnFilesDir(context, Constance.CAMERA_PATH);
//            Uri imageUri = Uri.fromFile(new File(IssueApplication.cameraPath, IssueApplication.imagePath + ".jpg"));
            Uri apkUri =
                    FileProvider.getUriForFile(context, "com.bocang.juhao.fileprovider", new File(DemoApplication.cameraPath, DemoApplication.imagePath + ".jpg"));
            System.out.println("imageUri" + apkUri.toString());
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); // 调用系统相机
            // 指定照片保存路径（SD卡）
            intent.putExtra(MediaStore.EXTRA_OUTPUT, apkUri);
            context.startActivityForResult(intent, Constance.PHOTO_WITH_CAMERA); // 用户点击了从相机获取
        }


    }


    /**
     * 从相册获取图片
     **/
    public static void pickPhoto(Activity context) {
        Intent intent = new Intent();
        intent.setType("image/*"); // 开启Pictures画面Type设定为image
        intent.setAction(Intent.ACTION_GET_CONTENT); // 使用Intent.ACTION_PICK这个Action则是直接打开系统图库
        context.startActivityForResult(intent, Constance.PHOTO_WITH_DATA); // 取得相片后返回到本画面
    }

    /**
     * 选择图片
     *
     * @param context
     */
    public static void openImage(final Activity context) {
        new AlertView(null, null, "取消", null,
                new String[]{"拍照", "从相册中选择"},
                context, AlertView.Style.ActionSheet, new OnItemClickListener() {
            @Override
            public void onItemClick(Object o, int position) {
                switch (position) {
                    case 0:
                        PermissionUtils.requestPermission(context, PermissionUtils.CODE_CAMERA, new PermissionUtils.PermissionGrant() {
                            @Override
                            public void onPermissionGranted(int requestCode) {
                                takePhoto(context);
                            }
                        });
                        break;
                    case 1:
                        PermissionUtils.requestPermission(context, PermissionUtils.CODE_READ_EXTERNAL_STORAGE, new PermissionUtils.PermissionGrant() {
                            @Override
                            public void onPermissionGranted(int requestCode) {
                                pickPhoto(context);
                            }
                        });

                        break;
                }
            }
        }).show();
    }
    /**
     * 选择图片
     *
     * @param context
     */
    public static void openSunImage(final Activity context) {
        new AlertView(null, null, "取消", null,
                new String[]{"拍照", "从相册中选择","小视频"},
                context, AlertView.Style.ActionSheet, new OnItemClickListener() {
            @Override
            public void onItemClick(Object o, int position) {
                switch (position) {
                    case 0:
                        PermissionUtils.requestPermission(context, PermissionUtils.CODE_CAMERA, new PermissionUtils.PermissionGrant() {
                            @Override
                            public void onPermissionGranted(int requestCode) {
                                takePhoto(context);
                            }
                        });
                        break;
                    case 1:
                        PermissionUtils.requestPermission(context, PermissionUtils.CODE_READ_EXTERNAL_STORAGE, new PermissionUtils.PermissionGrant() {
                            @Override
                            public void onPermissionGranted(int requestCode) {
                                ImageSelectorUtils.openPhoto(context, REQUEST_CODE, false, 3);
                            }
                        });
                        break;
                    case 2:
                        PermissionUtils.requestPermission(context, PermissionUtils.CODE_CAMERA, new PermissionUtils.PermissionGrant() {
                            @Override
                            public void onPermissionGranted(int requestCode) {
                                context.startActivityForResult(new Intent(context, VideoShotActivity.class),300);
                            }
                        });
                }
            }
        }).show();
    }

    /**
     * 通过Base32将Bitmap转换成Base64字符串
     *
     * @param bit
     * @return
     */
    public static String Bitmap2StrByBase64(Bitmap bit) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bit.compress(Bitmap.CompressFormat.JPEG, 65, bos);//参数100表示不压缩
        byte[] bytes = bos.toByteArray();


        return Base64.encodeToString(bytes, Base64.DEFAULT);


    }

    /**
     * 通过Base32将Bitmap转换成Base64字符串
     *
     * @param bit
     * @return
     */
    public static String BitmapStrByBase64(Bitmap bit) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bit.compress(Bitmap.CompressFormat.JPEG, 65, bos);//参数100表示不压缩
        byte[] bytes = bos.toByteArray();


        return Base64.encodeToString(bytes, Base64.DEFAULT);


    }

    /**
     * 选择相册
     */
    public static void getPickPhoto(final Activity context) {
        PermissionUtils.requestPermission(context, PermissionUtils.CODE_READ_EXTERNAL_STORAGE, new PermissionUtils.PermissionGrant() {
            @Override
            public void onPermissionGranted(int requestCode) {
                pickPhoto(context);
            }
        });
    }
    /**
     * 拍照
     */
    public static void getTakePhoto(final Activity context) {
        PermissionUtils.requestPermission(context, PermissionUtils.CODE_CAMERA, new PermissionUtils.PermissionGrant() {
            @Override
            public void onPermissionGranted(int requestCode) {
                takePhoto(context);
            }
        });
    }


    /**
     * 生成文件夹路径
     */
    public static String SDPATH = Environment.getExternalStorageDirectory()
            + "/TEST_PY/";

    /**
     * 将图片压缩保存到文件夹
     *
     * @param bm
     * @param picName
     */
    public static void saveBitmap(Bitmap bm, String picName) {
        try {

            // 如果没有文件夹就创建一个程序文件夹
            if (!isFileExist("")) {
                File tempf = createSDDir("");
            }
            File f = new File(SDPATH, picName + ".JPEG");
            // 如果该文件夹中有同名的文件，就先删除掉原文件
            if (f.exists()) {
                f.delete();
            }
            FileOutputStream out = new FileOutputStream(f);
            bm.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 质量压缩 并返回Bitmap
     *
     * @param image 要压缩的图片
     * @return 压缩后的图片
     */
    public static Bitmap compressImage(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 100) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();// 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;// 每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
        return bitmap;
    }

    /**
     * 质量压缩
     *
     * @param bitmap
     * @param picName
     */
    public static void compressImageByQuality(final Bitmap bitmap,
                                              String picName) {
        // 如果没有文件夹就创建一个程序文件夹
        if (!isFileExist("")) {
            try {
                File tempf = createSDDir("");
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        File f = new File(SDPATH, picName + ".JPEG");
        // 如果该文件夹中有同名的文件，就先删除掉原文件
        if (f.exists()) {
            f.delete();
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int options = 100;
        // 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);
        // 循环判断如果压缩后图片是否大于200kb,大于继续压缩
        while (baos.toByteArray().length / 1024 > 500) {
            // 重置baos即让下一次的写入覆盖之前的内容
            baos.reset();
            // 图片质量每次减少5
            options -= 5;
            // 如果图片质量小于10，则将图片的质量压缩到最小值
            if (options < 0)
                options = 0;
            // 将压缩后的图片保存到baos中
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);
            // 如果图片的质量已降到最低则，不再进行压缩
            if (options == 0)
                break;
        }
        // 将压缩后的图片保存的本地上指定路径中
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(new File(SDPATH, picName + ".JPEG"));
            fos.write(baos.toByteArray());
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {

            e.printStackTrace();
        } catch (IOException e) {

            e.printStackTrace();
        }
    }

    /**
     * 创建文件夹
     *
     * @param dirName 文件夹名称
     * @return 文件夹路径
     * @throws IOException
     */
    public static File createSDDir(String dirName) throws IOException {
        File dir = new File(SDPATH + dirName);
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {

            System.out.println("createSDDir:" + dir.getAbsolutePath());
            System.out.println("createSDDir:" + dir.mkdir());
        }
        return dir;
    }

    /**
     * 判断改文件是否是一个标准文件
     *
     * @param fileName 判断的文件路径
     * @return 判断结果
     */
    public static boolean isFileExist(String fileName) {
        File file = new File(SDPATH + fileName);
        file.isFile();
        return file.exists();
    }

    /**
     * 删除指定文件
     *
     * @param fileName
     */
    public static void delFile(String fileName) {
        File file = new File(SDPATH + fileName);
        if (file.isFile()) {
            file.delete();
        }
        file.exists();
    }

    /**
     * 删除指定文件
     *
     * @param file
     */
    public static void deleteFile(File file) {
        if (file.exists()) { // 判断文件是否存在
            if (file.isFile()) { // 判断是否是文件
                file.delete(); // delete()方法 你应该知道 是删除的意思;
            } else if (file.isDirectory()) { // 否则如果它是一个目录
                File files[] = file.listFiles(); // 声明目录下所有的文件 files[];
                for (int i = 0; i < files.length; i++) { // 遍历目录下所有的文件
                    deleteFile(files[i]); // 把每个文件 用这个方法进行迭代
                }
            }
            file.delete();

        } else {
            Log.i("TAG", "文件不存在！");
        }
    }

    /**
     * 删除指定文件夹中的所有文件
     */
    public static void deleteDir() {
        File dir = new File(SDPATH);
        if (dir == null || !dir.exists() || !dir.isDirectory())
            return;

        for (File file : dir.listFiles()) {
            if (file.isFile())
                file.delete();
            else if (file.isDirectory())
                deleteDir();
        }
        dir.delete();
    }

    /**
     * 判断是否存在该文件
     *
     * @param path 文件路径
     * @return
     */
    public static boolean fileIsExists(String path) {
        try {
            File f = new File(path);
            if (!f.exists()) {
                return false;
            }
        } catch (Exception e) {

            return false;
        }
        return true;
    }

    public static Bitmap getBitmap(String u) {
        Bitmap mBitmap = null;
        try {
            URL url = new URL(u);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            InputStream is = conn.getInputStream();
            mBitmap = BitmapFactory.decodeStream(is);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mBitmap;
    }

    public static Bitmap getImage(String Url) throws Exception {

        try {

            URL url = new URL(Url);

            String responseCode = url.openConnection().getHeaderField(0);

            if (responseCode.indexOf("200") < 0)

                throw new Exception("图片文件不存在或路径错误，错误代码：" + responseCode);

            return BitmapFactory.decodeStream(url.openStream());

        } catch (IOException e) {

            // TODO Auto-generated catch block

            throw new Exception(e.getMessage());

        }

    }

    public static File getFile(Context paramContext)
    {
        Object localObject = (Activity)paramContext;
        localObject = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "juhao/file");
        if (!((File)localObject).exists()) {
            ((File)localObject).mkdirs();
        }
        return (File)localObject;
    }




}
