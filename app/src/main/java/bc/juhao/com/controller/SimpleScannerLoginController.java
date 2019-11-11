package bc.juhao.com.controller;

import android.os.Message;

import com.alibaba.fastjson.JSONObject;

import bc.juhao.com.listener.INetworkCallBack02;
import bocang.view.BaseActivity;

/**
 * Created by gamekonglee on 2018/3/12.
 */

public class SimpleScannerLoginController extends BaseController {

    private final BaseActivity mView;

    public SimpleScannerLoginController(BaseActivity simpleScannerLoginActivity, String android_id) {
        mView = simpleScannerLoginActivity;
        mNetWork.sendTokenAdd(android_id, new INetworkCallBack02() {
            @Override
            public void onSuccessListener(String requestCode, JSONObject ans) {
                mView.hideLoading();
                mView.finish();
//                MyToast.show(mView,ans.toJSONString());
            }

            @Override
            public void onFailureListener(String requestCode, JSONObject ans) {
            mView.hideLoading();
            mView.onRefresh();
//            if(ans!=null){
//                MyToast.show(mView,"failure"+ans.toJSONString());
//            }else {
//                MyToast.show(mView,"failuter");
//            }
            }
        });
    }

    @Override
    protected void handleMessage(int action, Object[] values) {

    }

    @Override
    protected void myHandleMessage(Message msg) {

    }



}
