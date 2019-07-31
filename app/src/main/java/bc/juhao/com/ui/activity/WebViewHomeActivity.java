package bc.juhao.com.ui.activity;

import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.tencent.smtt.sdk.WebView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Stack;

import bc.juhao.com.R;
import bc.juhao.com.common.BaseActivity;
import bocang.utils.LogUtils;

public class WebViewHomeActivity extends BaseActivity {

    private String content;
    private WebView myWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void InitDataView() {

    }

    @Override
    protected void initController() {

    }

    @Override
    protected void initView() {
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        setContentView(R.layout.activity_web_view_home);
        setColor(this,getColor(R.color.theme_orange));
        myWebView = findViewById(R.id.webview);
//        myWebView.setLayerType();
        myWebView.getSettings().setJavaScriptEnabled(true);//启用js
        myWebView.getSettings().setBlockNetworkImage(false);//解决图片不显示
//        myWebView.getSettings().setMixedContentMode(android.webkit.WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        myWebView.setDrawingCacheEnabled(true);
        myWebView.getSettings().setDefaultTextEncodingName("UTF-8");
        final String url=getIntent().getStringExtra("url");
        LogUtils.logE("url",url);
        if(url==null)finish();
//        myWebView.loadUrl(url);

        new Thread(){
            @Override
            public void run() {
                super.run();
//                Document doc = null;
//                try {
//                    doc = Jsoup.connect(url).get();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                String content = GetDocContent(doc);
//                System.out.println(doc.body().text());
                content = downLoad(url);
                if(content.contains("src=\"/ueditor")){
                    content.replace("src=\"/ueditor","src=\"http://www.juhao.com//ueditor");
                }
                handler.sendEmptyMessage(0);

                LogUtils.logE("网页正文如下" , content);

            }
        }.start();

    }
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            myWebView.loadData(content,"text/html; charset=UTF-8",null);
        }
    };

    @Override
    protected void initData() {

    }
    public static String downLoad(String url) {
        String response = "";
        //第一步：创建HttpClient对象
        HttpClient httpClient = new DefaultHttpClient();
        //第二步：创建代表请求的对象,参数是访问的服务器地址
        HttpGet httpGet = new HttpGet(url);
        //第三步：执行请求，获取服务器发还的相应对象
        HttpResponse httpResponse = null;
        try {
            httpResponse = httpClient.execute(httpGet);
            //第四步：检查相应的状态是否正常：检查状态码的值是200表示正常
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                //第五步：从相应对象当中取出数据，放到entity当中
                HttpEntity entity = httpResponse.getEntity();
                response = EntityUtils.toString(entity, "utf-8");//将entity当中的数据转换为字符串
            } else {
                response = "";
            }
        } catch (IOException e) {
            e.printStackTrace();
            response = "";
        }finally {
            httpClient.getConnectionManager().shutdown();
        }
        return response;
    }

    private static String GetDocContent(Document doc) {
        Elements divs = doc.body().getElementsByTag("div");
        int max = -1;
        String content = null;
        for (int i = 0; i < divs.size(); i++) {
            Element div = (Element) divs.get(i);
            String divContent = GetDivContent(div);
            if (divContent.length() > max) {
                max = divContent.length();
                content = divContent;
            }
        }
        return content;
    }

    private static String GetDivContent(Element div) {
        StringBuilder sb = new StringBuilder();

        // 考虑div里标签内容的顺序，对div子树进行深度优先搜索
        Stack<Element> sk = new Stack<Element>();
        sk.push(div);
        while (!sk.empty()) {
            Element e = sk.pop();

            // 对于div中的div过滤掉
            if (e != div && e.tagName().equals("div"))
                continue;

            // 考虑正文被包含在p标签中的情况，并且p标签里不能含有a标签
            if (e.tagName().equals("p") && e.getElementsByTag("a").size() == 0) {
                String className = e.className();
                if (className.length() != 0 && className.equals("pictext"))
                    continue;
                sb.append(e.text());
                sb.append("\n");
                continue;
            } else if (e.tagName().equals("td")) {

                // 考虑正文被包含在td标签中的情况
                if (e.getElementsByTag("div").size() != 0)
                    continue;
                sb.append(e.text());
                sb.append("\n");
                continue;
            }

            // 将孩子节点加入栈中
            Elements children = e.children();
            for (int i = children.size() - 1; i >= 0; i--) {
                sk.push((Element) children.get(i));
            }
        }
        return sb.toString();
    }
}
