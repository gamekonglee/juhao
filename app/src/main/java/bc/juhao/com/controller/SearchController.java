package bc.juhao.com.controller;

import android.os.Message;

import bc.juhao.com.cons.Constance;
import bc.juhao.com.listener.INetworkCallBack;
import bc.juhao.com.ui.activity.SearchActivity;
import bocang.json.JSONArray;
import bocang.json.JSONObject;
import bocang.utils.AppUtils;

/**
 * Created by gamekonglee on 2018/3/28.
 */

public class SearchController extends BaseController{

    private final SearchActivity mView;
    public JSONArray mClassifyGoodsLists;

    public SearchController(SearchActivity searchActivity) {
        mView = searchActivity;
        initData();
    }

    private void initData() {
        sendGoodsType();
    }

    @Override
    protected void handleMessage(int action, Object[] values) {

    }

    @Override
    protected void myHandleMessage(Message msg) {

    }

    /**
     * 产品类别
     */
    private void sendGoodsType() {
        if(!AppUtils.isEmpty(mClassifyGoodsLists)) return;
//        mView.setShowDialog(true);
//        mView.setShowDialog("正在搜索中!");
//        mView.showLoading();
        mNetWork.sendGoodsType(1, 20, null, null, new INetworkCallBack() {
            @Override
            public void onSuccessListener(String requestCode, JSONObject ans) {
                mClassifyGoodsLists = ans.getJSONArray(Constance.categories);
            }

            @Override
            public void onFailureListener(String requestCode, JSONObject ans) {

            }
        });
    }
}
