package bc.juhao.com.ui.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageFormat;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.Image;
import android.media.ImageReader;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;

import java.nio.ByteBuffer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import bc.juhao.com.R;
import bc.juhao.com.ui.view.ScannerUtils;
import bc.juhao.com.utils.ImageUtil;
import bc.juhao.com.utils.UIUtils;

public class WebViewTestActivity extends AppCompatActivity {

    private WebView webview;
    private MediaProjectionManager mMediaProjectionManager;
    private ImageReader mImageReader;
    private VirtualDisplay mVirtualDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view_test);
        webview = findViewById(R.id.webview);
        View rl_bg=findViewById(R.id.rl_bg);
        rl_bg.setBackgroundColor(0);
        WebSettings webSettings= webview.getSettings();
        webSettings.setDomStorageEnabled(true);
        webSettings.setGeolocationEnabled(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowFileAccessFromFileURLs(true);
        webSettings.setAllowUniversalAccessFromFileURLs(true);
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setBlockNetworkImage(false);
        webSettings.setBlockNetworkLoads(false);
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(true);
        webview.setDrawingCacheEnabled(true);

        webview.loadUrl("http://3d.08138.com/uploads/1564215198/index(1).html");
        webview.setBackgroundColor(0);

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        int mResultCode = resultCode;
        Intent mResultData = data;
        MediaProjection mMediaProjection = mMediaProjectionManager.getMediaProjection(mResultCode, mResultData);


        //ImageFormat.RGB_565
        mImageReader = ImageReader.newInstance(UIUtils.getScreenWidth(this),UIUtils.getScreenHeight(this), 0x1, 2);
        mVirtualDisplay = mMediaProjection.createVirtualDisplay("screen-mirror",
                UIUtils.getScreenWidth(this), UIUtils.getScreenHeight(this), (int) getResources().getDisplayMetrics().density, DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
                mImageReader.getSurface(), new VirtualDisplay.Callback() {
                    @Override
                    public void onPaused() {
                        super.onPaused();
                    }

                    @Override
                    public void onResumed() {
                        super.onResumed();
                    }

                    @Override
                    public void onStopped() {
                        super.onStopped();
                    }
                }, null);

        DateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String strDate = dateFormat.format(new java.util.Date());
        String nameImage = strDate+".png";
        mImageReader.setOnImageAvailableListener(new ImageReader.OnImageAvailableListener() {
            @Override
            public void onImageAvailable(ImageReader imageReader) {
                Image image = mImageReader.acquireLatestImage();
                int width = image.getWidth();
                int height = image.getHeight();
                final Image.Plane[] planes = image.getPlanes();
                final ByteBuffer buffer = planes[0].getBuffer();
                int pixelStride = planes[0].getPixelStride();
                int rowStride = planes[0].getRowStride();
                int rowPadding = rowStride - pixelStride * width;
                Bitmap bitmap = Bitmap.createBitmap(width+rowPadding/pixelStride, height, Bitmap.Config.ARGB_8888);
                bitmap.copyPixelsFromBuffer(buffer);
                bitmap = Bitmap.createBitmap(bitmap, 0, 0,width, height);

                Dialog dialog=new Dialog(WebViewTestActivity.this);
                dialog.setContentView(R.layout.dialog_image_test);
                ImageView iv_img=dialog.findViewById(R.id.iv_img);
                iv_img.setImageBitmap(bitmap);
                dialog.show();
                image.close();
                if (mVirtualDisplay == null) {
                    return;
                }
                mVirtualDisplay.release();
                mVirtualDisplay = null;
            }
        }, getBackgroundHandler());

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void take(View v){

        mMediaProjectionManager = (MediaProjectionManager)getApplication().getSystemService(Context.MEDIA_PROJECTION_SERVICE);
        startActivityForResult(mMediaProjectionManager.createScreenCaptureIntent(), 2000);


//        webview.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
//        Bitmap imageData= ImageUtil.getWebViewBitmap(webview);

//        }else {
//        imageData = ImageUtil.takeScreenShot(mView);
//        }
        //截图
//        final Bitmap imageData = ImageUtil.compressImage(ImageUtil.takeScreenShot(mView));
//        final String imgUri= ScannerUtils.saveImageToGallery(this, imageData, ScannerUtils.ScannerType.RECEIVER);
    }
    //在后台线程里保存文件
    Handler backgroundHandler;

    private Handler getBackgroundHandler() {
        if (backgroundHandler == null) {
            HandlerThread backgroundThread =
                    new HandlerThread("catwindow", android.os.Process
                            .THREAD_PRIORITY_BACKGROUND);
            backgroundThread.start();
            backgroundHandler = new Handler(backgroundThread.getLooper());
        }
        return backgroundHandler;
    }
}
