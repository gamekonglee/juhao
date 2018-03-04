package bc.juhao.com.ui.activity;

import android.app.Activity;
import android.graphics.PixelFormat;
import android.os.Bundle;

import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;

import bc.juhao.com.R;

public class WebViewHomeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        setContentView(R.layout.activity_web_view_home);
        WebView myWebView=findViewById(R.id.webview);
//        myWebView.setLayerType();
        myWebView.getSettings().setJavaScriptEnabled(true);//启用js
        myWebView.getSettings().setBlockNetworkImage(false);//解决图片不显示
//        myWebView.getSettings().setMixedContentMode(android.webkit.WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        myWebView.setDrawingCacheEnabled(true);
        String url=getIntent().getStringExtra("url");
        if(url==null)finish();
        myWebView.loadUrl(url);
    }

}
