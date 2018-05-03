package bc.juhao.com.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import bc.juhao.com.R;
import bc.juhao.com.ui.activity.IssueApplication;

/**
 * Created by xpHuang on 2016/8/13.
 */
public class ImageUtil {
    /**
     * 以最省内存的方式读取本地资源的图片
     *
     * @param context 上下文
     * @param resId   本地资源图片id
     * @return Bitmap
     */
    public static Bitmap getBitmapById(Context context, int resId) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        InputStream is = context.getResources().openRawResource(resId);
        return BitmapFactory.decodeStream(is, null, opt);
    }

    public static void changeLight(ImageView imageView, int brightness) {
        ColorMatrix cMatrix = new ColorMatrix();
        cMatrix.set(new float[]{1, 0, 0, 0, brightness, 0, 1, 0, 0, brightness, // 改变亮度
                0, 0, 1, 0, brightness, 0, 0, 0, 1, 0});
        imageView.setColorFilter(new ColorMatrixColorFilter(cMatrix));
    }

    public static void changeLight02(ImageView imageView, Bitmap srcBitmap, int brightness) {
        int imgWidth = srcBitmap.getWidth();
        int imgHeight = srcBitmap.getHeight();
        Bitmap bmp = Bitmap.createBitmap(imgWidth, imgHeight,
                Bitmap.Config.ARGB_8888);
        // int brightness = progress - 127;
        float contrast = (float) ((brightness + 64) / 128.0);
        ColorMatrix cMatrix = new ColorMatrix();
        cMatrix.set(new float[]{contrast, 0, 0, 0, 0, 0,
                contrast, 0, 0, 0,// 改变对比度
                0, 0, contrast, 0, 0, 0, 0, 0, 1, 0});

        Paint paint = new Paint();
        paint.setColorFilter(new ColorMatrixColorFilter(cMatrix));

        Canvas canvas = new Canvas(bmp);
        // 在Canvas上绘制一个已经存在的Bitmap。这样，dstBitmap就和srcBitmap一摸一样了
        canvas.drawBitmap(srcBitmap, 0, 0, paint);
        imageView.setImageBitmap(bmp);
    }

    /**
     * 生成二维码 的位图
     *
     * @param text   二维码内容
     * @param width  生成二维码宽度bitmap
     * @param height 生成二维码的高度
     * @return bitmap
     */
    public static Bitmap createQRImage(String text, final int width, final int height) {
        try {
            // 判断text合法性
            if (text == null || "".equals(text) || text.length() < 1) {
                return null;
            }
            Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            // 图像数据转换，使用了矩阵转换
            BitMatrix bitMatrix = new QRCodeWriter().encode(text,
                    BarcodeFormat.QR_CODE, width, height, hints);
            int[] pixels = new int[width * height];
            // 下面这里按照二维码的算法，逐个生成二维码的图片，
            // 两个for循环是图片横列扫描的结果 添加色块
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    if (bitMatrix.get(x, y)) {
                        pixels[y * width + x] = 0xff000000;
                    } else {
                        pixels[y * width + x] = 0xffffffff;
                    }
                }
            }
            // 生成二维码图片的格式，使用ARGB_8888
            Bitmap bitmap = Bitmap.createBitmap(width, height,
                    Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
            return bitmap;
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static Bitmap appendTextToPicture(Bitmap bmp, final String msg) {
        final int TXT_SIZE = 24;
        final int y_offset = 5;
        int heigth = bmp.getHeight() + y_offset + TXT_SIZE;
        final int max_width = bmp.getWidth();
        List<String> buf = new ArrayList<String>();
        String lineStr = "";

        Paint p = new Paint();
        Typeface font = Typeface.create("宋体", Typeface.BOLD);
        p.setColor(Color.BLACK);
        p.setTypeface(font);
        p.setTextSize(TXT_SIZE);

        for (int i = 0; i < msg.length(); ) {

            if (Character.getType(msg.charAt(i)) == Character.OTHER_LETTER) {
                // 如果这个字符是一个汉字
                if ((i + 1) < msg.length()) {
                    lineStr += msg.substring(i, i + 2);
                }

                i = i + 2;
            } else {
                lineStr += msg.substring(i, i + 1);
                i++;
            }

            float[] ws = new float[lineStr.length()];
            int wid = p.getTextWidths(lineStr, ws);

            if (wid > max_width) {
                buf.add(lineStr);
                lineStr = "";
                heigth += (TXT_SIZE + y_offset);
            }

            if (i >= msg.length()) {
                heigth += (TXT_SIZE + y_offset);
                break;
            }
        }


        Bitmap canvasBmp = Bitmap.createBitmap(max_width, heigth + TXT_SIZE, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(canvasBmp);
        canvas.drawColor(Color.WHITE);

        float y = y_offset + TXT_SIZE;
        for (String str : buf) {
            canvas.drawText(str, 0, y, p);
            y += (TXT_SIZE + y_offset);
        }

        canvas.drawBitmap(bmp, 0, y, p);
        return canvasBmp;
    }


    /**
     * 在二维码中间添加Logo图案
     */
    private static Bitmap addLogo(Bitmap src, Bitmap logo) {
        if (src == null) {
            return null;
        }

        if (logo == null) {
            return src;
        }

        //获取图片的宽高
        int srcWidth = src.getWidth();
        int srcHeight = src.getHeight();
        int logoWidth = logo.getWidth();
        int logoHeight = logo.getHeight();

        if (srcWidth == 0 || srcHeight == 0) {
            return null;
        }

        if (logoWidth == 0 || logoHeight == 0) {
            return src;
        }

        //logo大小为二维码整体大小的1/5
        float scaleFactor = srcWidth * 1.0f / 5 / logoWidth;
        Bitmap bitmap = Bitmap.createBitmap(srcWidth, srcHeight, Bitmap.Config.ARGB_8888);
        try {
            Canvas canvas = new Canvas(bitmap);
            canvas.drawBitmap(src, 0, 0, null);
            canvas.scale(scaleFactor, scaleFactor, srcWidth / 2, srcHeight / 2);
            canvas.drawBitmap(logo, (srcWidth - logoWidth) / 2, (srcHeight - logoHeight) / 2, null);

            canvas.save(Canvas.ALL_SAVE_FLAG);
            canvas.restore();
        } catch (Exception e) {
            bitmap = null;
            e.getStackTrace();
        }

        return bitmap;
    }

    public static Bitmap textAsBitmap(String text) {

        //        //获取图片的宽高
        //        int srcWidth = src.getWidth();
        //        int srcHeight = src.getHeight();

        TextPaint textPaint = new TextPaint();

        //         textPaint.setARGB(238, 118, 0, 1);
        textPaint.setColor(Color.WHITE);

        textPaint.setTextSize(45);

        StaticLayout layout = new StaticLayout(text, textPaint, 450,
                Layout.Alignment.ALIGN_NORMAL, 1.3f, 0.0f, true);
        Bitmap bitmap = Bitmap.createBitmap(layout.getWidth() + 20,
                layout.getHeight() + 20, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.translate(10, 10);
        canvas.drawColor(IssueApplication.getcontext().getResources().getColor(R.color.trantracent_wode));

        layout.draw(canvas);
        Log.d("textAsBitmap",
                String.format("1:%d %d", layout.getWidth(), layout.getHeight()));
        return bitmap;
    }

    /**
     * 根据一个网络连接(String)获取bitmap图像
     *
     * @param imageUri
     * @return
     */
    public static Bitmap getbitmap(String imageUri) {
        // 显示网络上的图片
        Bitmap bitmap = null;
        try {
            URL myFileUrl = new URL(imageUri);
            HttpURLConnection conn = (HttpURLConnection) myFileUrl
                    .openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            is.close();

        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            bitmap = null;
        } catch (IOException e) {
            e.printStackTrace();
            bitmap = null;
        }
        return bitmap;
    }
    /**
     * 根据 路径 得到 file 得到 bitmap
     * @param filePath
     * @return
     * @throws IOException
     */
    public static Bitmap decodeFile(String filePath) throws IOException{
        Bitmap b = null;
        int IMAGE_MAX_SIZE = 60000;

        File f = new File(filePath);
        if (f == null){
            return null;
        }
        //Decode image size
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;

        FileInputStream fis = new FileInputStream(f);
        BitmapFactory.decodeStream(fis, null, o);
        fis.close();

        int scale = 1;
        if (o.outHeight > IMAGE_MAX_SIZE || o.outWidth > IMAGE_MAX_SIZE) {
            scale = (int) Math.pow(2, (int) Math.round(Math.log(IMAGE_MAX_SIZE / (double) Math.max(o.outHeight, o.outWidth)) / Math.log(0.5)));
        }

        //Decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        fis = new FileInputStream(f);
        b = BitmapFactory.decodeStream(fis, null, o2);
        fis.close();
        return b;
    }

    //    /**传递进来的源图片*/
    //    private Bitmap source;
    //    /**图片的配文*/
    //    private String text;
    //    /**图片加上配文后生成的新图片*/
    //    private Bitmap newBitmap;
    //    /**配文的颜色*/
    //    private int textColor = Color.BLACK;
    //    /**配文的字体大小*/
    //    private float textSize = 16;
    //    /**图片的宽度*/
    //    private int bitmapWidth;
    //    /**图片的高度*/
    //    private int bitmapHeight;
    //    /**画图片的画笔*/
    //    private Paint bitmapPaint;
    //    /**画文字的画笔*/
    //    private Paint textPaint;
    //    /**配文与图片间的距离*/
    //    private float padding = 20;
    //    /**配文行与行之间的距离*/
    //    private float linePadding = 5;

    public static Bitmap getTowCodeImage(Bitmap source, String text) {
        int bitmapWidth = source.getWidth();
        int bitmapHeight = source.getHeight();
        Bitmap newBitmap;
        TextPaint textPaint = new TextPaint();
        float linePadding = 5;
        float textSize = 14;
        int textColor = Color.BLACK;
        float padding = 5;
        Paint bitmapPaint = new Paint();

        //一行可以显示文字的个数
        int lineTextCount = 20;
        //        int lineTextCount = (int) ((source.getWidth() - 0) / textSize);
        //一共要把文字分为几行
        int line = (int) Math.ceil(Double.valueOf(text.length()) / Double.valueOf(lineTextCount));

        //新创建一个新图片比源图片多出一部分，后续用来与文字叠加用
        newBitmap = Bitmap.createBitmap(bitmapWidth,
                (int) (bitmapHeight + padding + textSize * line + linePadding * line), Bitmap.Config.ARGB_8888);
        //        newBitmap = Bitmap.createBitmap(bitmapWidth,
        //                (int) (bitmapHeight + padding + textSize * line + linePadding * line), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(newBitmap);
        //把图片画上来
        canvas.drawBitmap(source, 0, 0, bitmapPaint);

        //在图片下边画一个白色矩形块用来放文字，防止文字是透明背景，在有些情况下保存到本地后看不出来
        textPaint.setColor(Color.WHITE);
        //        canvas.drawRect(0, source.getHeight(), source.getWidth(),
        //                source.getHeight() + padding + textSize * line + linePadding * line, textPaint);
        canvas.drawRect(0, source.getHeight(), source.getWidth(),
                source.getHeight() + padding + textSize * line + linePadding * line, textPaint);

        //把文字画上来
        textPaint.setColor(textColor);
        textPaint.setTextSize(textSize);

        Rect bounds = new Rect();

        //开启循环直到画完所有行的文字
        for (int i = 0; i < line; i++) {
            String s;
            if (i == line - 1) {//如果是最后一行，则结束位置就是文字的长度，别下标越界哦
                s = text.substring(i * lineTextCount, text.length());
            } else {//不是最后一行
                s = text.substring(i * lineTextCount, (i + 1) * lineTextCount);
            }
            //获取文字的字宽高以便把文字与图片中心对齐
            textPaint.getTextBounds(s, 0, s.length(), bounds);
            //画文字的时候高度需要注意文字大小以及文字行间距
            canvas.drawText(s, source.getWidth() / 2 - bounds.width() / 2,
                    source.getHeight() + padding + i * textSize + i * linePadding + bounds.height() / 2, textPaint);
        }
        canvas.save(Canvas.ALL_SAVE_FLAG);
        canvas.restore();
        return newBitmap;
    }

    /**
     * Bitmap转换成byte[]并且进行压缩,压缩到不大于maxkb
     * @param bitmap
     * @param IMAGE_SIZE
     * @return
     */
    public static byte[] bitmap2Bytes(Bitmap bitmap, int maxkb) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, output);
        int options = 100;
        while (output.toByteArray().length/1024 > maxkb&& options != 10) {
            output.reset(); //清空output
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, output);//这里压缩options%，把压缩后的数据存放到output中
            options -= 10;
        }
        return output.toByteArray();
    }
    public static byte[] getBitmapByte(Bitmap bitmap) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);

        bitmap.recycle();//自由选择是否进行回收
        //        byte[] result = out.toByteArray();//转换成功了
        try {
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return out.toByteArray();
    }

    // 将Bitmap转换成InputStream
    public static InputStream Bitmap2InputStream(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG,65, baos);
        InputStream is = new ByteArrayInputStream(baos.toByteArray());
        return is;
    }

    // Drawable转换成InputStream
    public static InputStream Drawable2InputStream(Drawable d) {
        Bitmap bitmap = drawable2Bitmap(d);
        return Bitmap2InputStream(bitmap);
    }

    public static Bitmap convertBmp(Bitmap bmp) {
        int w = bmp.getWidth();
        int h = bmp.getHeight();

        Matrix matrix = new Matrix();
        matrix.postScale(-1, 1); // 镜像水平翻转
        Bitmap convertBmp = Bitmap.createBitmap(bmp, 0, 0, w, h, matrix, true);

        return convertBmp;
    }


    // Drawable转换成Bitmap
    public static Bitmap drawable2Bitmap(Drawable drawable) {
        Bitmap bitmap = Bitmap
                .createBitmap(
                        drawable.getIntrinsicWidth(),
                        drawable.getIntrinsicHeight(),
                        drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                                : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    public static synchronized byte[] drawableToByte(Drawable drawable) {

        if (drawable != null) {
            Bitmap bitmap = Bitmap
                    .createBitmap(
                            drawable.getIntrinsicWidth(),
                            drawable.getIntrinsicHeight(),
                            drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                                    : Bitmap.Config.RGB_565);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                    drawable.getIntrinsicHeight());
            drawable.draw(canvas);
            int size = bitmap.getWidth() * bitmap.getHeight() * 4;
            // 创建一个字节数组输出流,流的大小为size
            ByteArrayOutputStream baos = new ByteArrayOutputStream(size);
            // 设置位图的压缩格式，质量为100%，并放入字节数组输出流中
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            // 将字节数组输出流转化为字节数组byte[]
            byte[] imagedata = baos.toByteArray();
            return imagedata;
        }
        return null;
    }

    public static Bitmap takeScreenShot(Activity activity) {
        // View是你需要截图的View
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap b1 = view.getDrawingCache();

        // 获取状态栏高度
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;

        // 获取屏幕长和高
        int width = activity.getWindowManager().getDefaultDisplay().getWidth();
        int height = activity.getWindowManager().getDefaultDisplay()
                .getHeight();
        // 去掉标题栏
        Bitmap b = Bitmap.createBitmap(b1, 0, statusBarHeight, width, height
                - statusBarHeight);
        view.destroyDrawingCache();
        return b;
    }

    //压缩图片大小
    public static Bitmap compressImage(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 80, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 18000000) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;//每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        return bitmap;
    }


    /**
     * 将彩色图转换为灰度图
     *
     * @param img 位图
     * @return 返回转换好的位图
     */
    public Bitmap convertGreyImg(Bitmap img) {
        int width = img.getWidth();         //获取位图的宽
        int height = img.getHeight();       //获取位图的高

        int[] pixels = new int[width * height]; //通过位图的大小创建像素点数组

        img.getPixels(pixels, 0, width, 0, 0, width, height);
        int alpha = 0xFF << 24;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int grey = pixels[width * i + j];

                int red = ((grey & 0x00FF0000) >> 16);
                int green = ((grey & 0x0000FF00) >> 8);
                int blue = (grey & 0x000000FF);

                grey = (int) ((float) red * 0.3 + (float) green * 0.59 + (float) blue * 0.11);
                grey = alpha | (grey << 16) | (grey << 8) | grey;
                pixels[width * i + j] = grey;
            }
        }

        Bitmap result = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        result.setPixels(pixels, 0, width, 0, 0, width, height);
        return result;
    }

    public static Bitmap createViewBitmap(View v) {
        Bitmap bitmap = Bitmap.createBitmap(v.getWidth(), v.getHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        v.draw(canvas);
        return bitmap;
    }

    public static Bitmap getBitmapByView(ScrollView scrollView) {
        int h = 0;
        Bitmap bitmap = null;

        for (int i = 0; i < scrollView.getChildCount(); i++) {
            h += scrollView.getChildAt(i).getHeight();
            scrollView.getChildAt(i).setBackgroundColor(
                    Color.parseColor("#ffffff"));
        }

        bitmap = Bitmap.createBitmap(scrollView.getWidth(), h,
                Bitmap.Config.RGB_565);
        final Canvas canvas = new Canvas(bitmap);
        scrollView.draw(canvas);
        return bitmap;
    }

    /*****
     * 从本地加载,位图  返回位图
     */
    public static Bitmap loadFromAsset(Context context, String fileName) {
        Bitmap image = null;
        AssetManager am = context.getResources().getAssets();
        InputStream is = null;
        try {
            is = am.open(fileName);
            image = BitmapFactory.decodeStream(is);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return image;
    }

    //absolutePath是图片绝对路径
    public static Bitmap adjustImage(Activity activity,String absolutePath) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        // 这个isjustdecodebounds很重要
        opt.inJustDecodeBounds = true;
        Bitmap bm = BitmapFactory.decodeFile(absolutePath, opt);

        // 获取到这个图片的原始宽度和高度
        int picWidth = opt.outWidth;
        int picHeight = opt.outHeight;

        // 获取屏的宽度和高度
        WindowManager windowManager = activity.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        int screenWidth = display.getWidth();
        int screenHeight = display.getHeight();

        // isSampleSize是表示对图片的缩放程度，比如值为2图片的宽度和高度都变为以前的1/2
        opt.inSampleSize = 1;
        // 根据屏的大小和图片大小计算出缩放比例
        if (picWidth > picHeight) {
            if (picWidth > screenWidth)
                opt.inSampleSize = picWidth / screenWidth;
        } else {
            if (picHeight > screenHeight)

                opt.inSampleSize = picHeight / screenHeight;
        }

        // 这次再真正地生成一个有像素的，经过缩放了的bitmap
        opt.inJustDecodeBounds = false;
        bm= BitmapFactory.decodeFile(absolutePath, opt);
        return bm;
        // 用imageview显示出bitmap
    }

    public static Bitmap getBitmapFromView(View v) {
        if (v == null) {
            return null;
        }
        v.setDrawingCacheEnabled(true);
        v.buildDrawingCache();
        Bitmap bitmap = v.getDrawingCache();
        return bitmap;
    }
    public static Bitmap loadBitmapFromView(View v) {
        if (v == null) {
            return null;
        }
        Bitmap screenshot;
        screenshot = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_4444);
        Canvas canvas = new Canvas(screenshot);
        canvas.translate(-v.getScrollX(), -v.getScrollY());//我们在用滑动View获得它的Bitmap时候，获得的是整个View的区域（包括隐藏的），如果想得到当前区域，需要重新定位到当前可显示的区域
        v.draw(canvas);// 将 view 画到画布上
        return screenshot;
    }

    public static Bitmap createThumbBitmap(Bitmap bm,int newWidth,int newHeight) {

        // 获得图片的宽高
        int width = bm.getWidth();
        int height = bm.getHeight();
        // 计算缩放比例
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片
        Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
        return newbm;
    }

}
