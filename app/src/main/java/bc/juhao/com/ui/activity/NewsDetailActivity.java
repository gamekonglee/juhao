package bc.juhao.com.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import bc.juhao.com.R;
import bc.juhao.com.cons.Constance;
import bc.juhao.com.ui.view.MyWebView;
import bc.juhao.com.utils.NetWorkUtils;
import bocang.utils.AppUtils;
import bocang.utils.LogUtils;
import bocang.view.BaseActivity;

/**
 * @author: Jun
 * @date : 2017/3/10 16:20
 * @description :消息内容
 */
public class NewsDetailActivity extends BaseActivity {
    String mUrl;
    MyWebView mWebView;
    int mFromType=0;
    TextView title_tv;
    private String mHtml;
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mWebView.loadData(mHtml,"text/html; charset=UTF-8",null);
        }
    };
    @Override
    protected void InitDataView() {
        if(AppUtils.isEmpty(mUrl)) return;
//        mWebView.loadUrl(mUrl);

    }

    @Override
    protected void initController() {

    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_sys_message_detail);
        mWebView = (MyWebView)findViewById(R.id.webview);
        mWebView.setActivity(this);
        title_tv = (TextView)findViewById(R.id.title_tv);
        if(mFromType==1){
            title_tv.setText("场景详情");
        }
        mWebView.setWebChromeClient(new WebChromeClient());
        mWebView.setWebViewClient(new WebViewClient());
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        mWebView.getSettings().setSupportZoom(true);
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setDefaultTextEncodingName("utf-8");
        mWebView.setWebViewClient(new WebViewClient (){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith("tel:")){
                    Intent intent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse(url));
                    startActivity(intent);
                } else if(url.startsWith("http:") || url.startsWith("https:")) {
                    view.loadUrl(url);
                }
                return true;
            }
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
//
//                view.loadUrl("javascript:(function() { " +
//                        "document.body.innerHTML = "+
//                        "document.body.innerHTML.replace('api','www');"+
//                        "})()");

//                view.loadUrl(url.replace("api","wwww"));
            }
        });
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        mUrl=intent.getStringExtra(Constance.url);
        mFromType=intent.getIntExtra(Constance.FROMTYPE,0);
        LogUtils.logE("murl",mUrl);
        try {
            new Thread(){
                @Override
                public void run() {
                    super.run();
                    try {
                        mHtml = NetWorkUtils.getHtml(mUrl);
                        mHtml=mHtml.replace("src=\"/","src=\"http://www.juhao.com/");
                        LogUtils.logE("html",mHtml);
                        handler.sendEmptyMessage(0);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }.start();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onViewClick(View v) {

    }
}
