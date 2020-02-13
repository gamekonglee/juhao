package bc.juhao.com.ui.activity;

import android.graphics.Color;
import android.view.View;

import com.tencent.smtt.sdk.WebView;

import bc.juhao.com.cons.Constance;
import bocang.view.BaseActivity;

/**
 * Created by gamekonglee on 2018/11/26.
 */

public class MyWebViewActivity extends BaseActivity {

    private String url;

    @Override
    protected void InitDataView() {

    }

    @Override
    protected void initController() {

    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        url = getIntent().getStringExtra(Constance.url);
        WebView webview=new WebView(this);
        setContentView(webview);
        webview.loadUrl(url);
        setColor(this, Color.WHITE);
    }

    @Override
    protected void onViewClick(View v) {

    }
}
