package bc.juhao.com.controller.product;

import android.os.Message;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import bc.juhao.com.R;
import bc.juhao.com.cons.Constance;
import bc.juhao.com.cons.NetWorkConst;
import bc.juhao.com.controller.BaseController;
import bc.juhao.com.ui.activity.product.ProDetailActivity;
import bc.juhao.com.ui.fragment.DetailGoodsFragmemt;
import bocang.utils.AppUtils;

/**
 * @author: Jun
 * @date : 2017/2/14 16:01
 * @description :
 */
public class DetailGoodsController extends BaseController {
    private DetailGoodsFragmemt mView;
    private WebView webView;
    private com.alibaba.fastjson.JSONObject mProductObject;

    public DetailGoodsController(DetailGoodsFragmemt v){
        mView=v;
        initView();
        initViewData();

    }

    private void initViewData() {
        sendProductDetail();
    }

    private void initView() {
        webView = (WebView) mView.getActivity().findViewById(R.id.webView1);
        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setDefaultTextEncodingName("utf-8");
    }

    private void getWebView(String htmlValue){
        String html = htmlValue;
        html = html.replace("src=\"", "src=\"" + NetWorkConst.SCENE_HOST);
        html = html.replace("</p>", "");
        html = html.replace("<p>", "");
        html = "<meta name=\"viewport\" content=\"width=device-width\">" + html;
        webView.loadData(html, "text/html; charset=UTF-8", null);//这种写法可以正确解析中文
    }

    /**
     * 产品详情
     */
    public void sendProductDetail() {

        mProductObject = ((ProDetailActivity) mView.getActivity()).mProductObject;
        if (AppUtils.isEmpty(mProductObject)){
            return;
        }


        final String value = mProductObject.getString(Constance.goods_desc);
        getWebView(value);

    }

    @Override
    protected void handleMessage(int action, Object[] values) {

    }

    @Override
    protected void myHandleMessage(Message msg) {

    }
}
