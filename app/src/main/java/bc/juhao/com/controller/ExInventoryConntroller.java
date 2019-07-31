package bc.juhao.com.controller;

import android.os.Message;
import android.util.DisplayMetrics;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.RelativeLayout;

import com.aliyun.iot.ilop.demo.DemoApplication;
import com.pgyersdk.crash.PgyCrashManager;
import com.zhy.http.okhttp.callback.Callback;

import org.json.JSONException;

import bc.juhao.com.cons.Constance;
import bc.juhao.com.cons.NetWorkConst;
import bc.juhao.com.listener.INetworkCallBack;
import bc.juhao.com.net.ApiClient;
import bc.juhao.com.ui.activity.buy.ExInventoryActivity;
import bocang.json.JSONObject;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by gamekonglee on 2018/8/22.
 */

public class ExInventoryConntroller extends  BaseController{

    private final ExInventoryActivity mView;

    public ExInventoryConntroller(ExInventoryActivity exInventoryActivity) {
        mView = exInventoryActivity;
        getParent();
    }

    private void getParent() {
//        PgyCrashManager.reportCaughtException(mView,new Exception("getParent"));
        mNetWork.sendShopAddress(mView.usercodeid, new INetworkCallBack() {
            @Override
            public void onSuccessListener(String requestCode, JSONObject ans) {
                try {
                    org.json.JSONObject jsonObject=new org.json.JSONObject(ans.toString());
                    org.json.JSONObject shop=jsonObject.getJSONObject(Constance.shop);
//                    PgyCrashManager.reportCaughtException(mView,new Exception("mView.fillData(shop);"));
                    mView.fillData(shop);
                } catch (JSONException e) {
//                    e.printStackTrace();
                    mView.fillData(null);
//                    PgyCrashManager.reportCaughtException(mView,e);
                }
            }

            @Override
            public void onFailureListener(String requestCode, JSONObject ans) {
                PgyCrashManager.reportCaughtException(mView,new Exception("sendShopAddress，onFailureListener"));
            }
        });
//        ApiClient.sendShopAddress(""+mView.usercodeid, new Callback<String>() {
//            @Override
//            public String parseNetworkResponse(Response response, int id) throws Exception {
//                return response.body().string();
//            }
//
//            @Override
//            public void onError(Call call, Exception e, int id) {
//
//            }
//
//            @Override
//            public String onResponse(String response, int sid) {
//
//                return null;
//            }
//        });
    }

    @Override
    protected void handleMessage(int action, Object[] values) {

    }

    @Override
    protected void myHandleMessage(Message msg) {

    }

}
