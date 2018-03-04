package bc.juhao.com.ui.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import bc.juhao.com.ui.view.dialog.SpotsDialog;
import bocang.utils.AppUtils;

/**
 * Created by XY on 2017/6/8.
 */

@SuppressLint("SetJavaScriptEnabled")
public class MyWebView extends WebView {
    private SpotsDialog mDialog;
    private Activity mActivity;

    public MyWebView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        init();
    }

    /**
     * @param context
     * @param attrs
     */
    public MyWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        init();
    }

    private void init() {


        WebSettings wSettings=getSettings();
        //String ua = wSettings.getUserAgentString();
        //LogUtil.e("ua::"+ua);
        wSettings.setJavaScriptEnabled(true);
        wSettings.setAppCacheEnabled(true);
        wSettings.setDatabaseEnabled(true);
        wSettings.setDomStorageEnabled(true);
        wSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        wSettings.setAllowFileAccess(true);
        this.setDrawingCacheEnabled(true);
        wSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        wSettings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
        setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                // TODO Auto-generated method stub
                setVisibility(View.INVISIBLE);
                if (mActivity!=null) {
                    if(AppUtils.isEmpty(mDialog)){
                        mDialog=new SpotsDialog(mActivity);
                    }
                    try{
                        mDialog.show();
                    }catch (Exception e){

                    }
//                    MyProgressDialog.show(mActivity, true);
                }
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                setVisibility(View.VISIBLE);
//                MyProgressDialog.close();
                mDialog.dismiss();
                super.onPageFinished(view, url);
            }
            @Override
            public void onReceivedError(WebView view, int errorCode,
                                        final String description, final String failingUrl) {
                // TODO Auto-generated method stub
                super.onReceivedError(view, errorCode, description, failingUrl);
                Log.e("ErrorCode", "errorCode::" + errorCode + "--description::"
                        + description + "--failingUrl::" + failingUrl);
                loadUrl("file:///android_asset/error.html");
                postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        loadUrl("javascript:setcontent('" + "网络不给力啊" + "')");
                        loadUrl("javascript:seturl('" + failingUrl + "')");
                    }
                }, 1000);
//                MyProgressDialog.close();
                mDialog.dismiss();
            }
        });

        setWebChromeClient(new WebChromeClient());
    }

    public void setActivity(Activity myActivity) {
        // TODO Auto-generated method stub
        mActivity = myActivity;
        init();
        setDownloadListener(new CusWebViewDownLoadListener(mActivity));
//        addJavascriptInterface(new JSInterface(mActivity, this), "JSInterface");

    }

}
