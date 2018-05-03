package bc.juhao.com.controller;

import android.os.Message;
import android.util.Log;
import android.view.View;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import bc.juhao.com.R;
import bc.juhao.com.bean.ExtractBean;
import bc.juhao.com.cons.Constance;
import bc.juhao.com.cons.NetWorkConst;
import bc.juhao.com.cons.ProfitBean;
import bc.juhao.com.listener.INetworkCallBack02;
import bc.juhao.com.ui.activity.blance.UserExtractProfitActivity;
import bocang.utils.AppUtils;
import bocang.utils.LogUtils;
import bocang.utils.MyToast;

/**
 * Created by gamekonglee on 2018/3/31.
 */

public class UserExtractProfitController extends BaseController{

    private final UserExtractProfitActivity mView;
    public int[] page={0,0};
    public List<ExtractBean> extractBeans;
    public List<ProfitBean> profitBeans;
    public View mNullView;

    public UserExtractProfitController(UserExtractProfitActivity userExtractProfitActivity) {
        mView = userExtractProfitActivity;
        initView();
        initData();
    }

    private void initView() {
        mNullView = mView.findViewById(R.id.mNullView);
    }

    private void initData() {
        extractBeans = new ArrayList<>();
        profitBeans = new ArrayList<>();
        page[0]=0;
        page[1]=0;
//     sendExtract();
//     sendProfitRecordList();
    }

    public void sendExtract() {
//        if(!mView.pullToRefresh.isRefreshing())mView.pullToRefresh.setRefreshing(true);
        mNetWork.sendAlipayList(new INetworkCallBack02() {
            @Override
            public void onSuccessListener(String requestCode, JSONObject ans) {
                switch (requestCode){
                    case NetWorkConst.ALIPAY_LIST_URL:
                        if (null == mView || mView.isFinishing())
                            return;
                        dismissRefesh();
                        JSONArray dataList=ans.getJSONArray(Constance.data);
//                        LogUtils.logE("dataList",dataList.getJSONObject(0).toString());
                        if (AppUtils.isEmpty(dataList) || dataList.size()==0) {
                            if (page[mView.currnetPage] == 1) {
                                mNullView.setVisibility(View.VISIBLE);
                            }

//                            dismissRefesh();
                            return;
                        }

                        mNullView.setVisibility(View.GONE);
//                        mNullNet.setVisibility(View.GONE);
                        getExtractDataSuccess(dataList);
                        break;
                }
            }

            @Override
            public void onFailureListener(String requestCode, JSONObject ans) {
//                mDialog.dismiss();
//                if (AppUtils.isEmpty(ans)) {
//                    mNullNet.setVisibility(View.VISIBLE);
//                    mRefeshBtn.setOnClickListener(mRefeshBtnListener);
//                    return;
//                }
//
//                if (null != mPullToRefreshLayout) {
                    dismissRefesh();
//                }
            }
        });
    }



    /**
     */
    public void sendProfitRecordList() {
//        if(!mView.pullToRefresh.isRefreshing())mView.pullToRefresh.setRefreshing(true);
        mNetWork.sendProfitRecordList
                (page[mView.currnetPage], 12, new INetworkCallBack02() {
                    @Override
                    public void onSuccessListener(String requestCode, JSONObject ans) {
                        switch (requestCode) {
                            case NetWorkConst.SALESACCOUNT_URL:
                                if (null == mView || mView.isFinishing())
                                    return;
                                dismissRefesh();
                                JSONArray dataList = ans.getJSONArray(Constance.account);
                                LogUtils.logE("profit","page:"+page[mView.currnetPage]+",size:"+dataList.size());
//                                LogUtils.logE("Profit",dataList.getJSONObject(0).toString());
                                if (AppUtils.isEmpty(dataList) || dataList.size() == 0) {
                                    if (page[mView.currnetPage] == 1) {
                                        mNullView.setVisibility(View.VISIBLE);
                                    }

                                    dismissRefesh();
                                    return;
                                }

                                mNullView.setVisibility(View.GONE);
                                int currentPage=ans.getJSONObject(Constance.paged).getInteger(Constance.page);
                                getDataSuccess(dataList,currentPage);
                                break;
                        }
                    }

                    @Override
                    public void onFailureListener(String requestCode, JSONObject ans) {
//                        mView.hideLoading();
                        dismissRefesh();
                        if (AppUtils.isEmpty(ans)) {
                            mNullView.setVisibility(View.VISIBLE);
//                            mRefeshBtn.setOnClickListener(mRefeshBtnListener);
                            return;
                        }

//                        if (null != mPullToRefreshLayout) {

//                        }
                    }
                });
    }

    public void dismissRefesh() {
        if(mView.pmSwipeRefreshLayouts[mView.currnetPage]!=null){
            mView.pmSwipeRefreshLayouts[mView.currnetPage].setRefreshing(false);
            mView.pmSwipeRefreshLayouts[mView.currnetPage].post(new Runnable() {
                @Override
                public void run() {
                    mView.pmSwipeRefreshLayouts[mView.currnetPage].setRefreshing(false);
                }
            });
        }
    }

        private void getDataSuccess(JSONArray array, int currentPage) {
        List<ProfitBean> temp=new ArrayList<>();
        for(int i=0;i<array.size();i++){
            temp.add(new Gson().fromJson(array.getJSONObject(i).toString(),ProfitBean.class));
        }
        if (1 == currentPage)
        { LogUtils.logE("page","=temp");
            profitBeans=temp;}
        else if (null != temp) {
                LogUtils.logE("page","Add.temp");
           profitBeans.addAll(temp);

            if (AppUtils.isEmpty(array))
                mView.isBottom[mView.currnetPage]=true;
//                MyToast.show(mView, "没有更多内容了");
        }
        mView.profitAdapter.replaceAll(profitBeans);
    }
    private void getExtractDataSuccess(JSONArray array) {
        List<ExtractBean> temp=new ArrayList<>();
        for(int i=0;i<array.size();i++){
            temp.add(new Gson().fromJson(array.getJSONObject(i).toString(),ExtractBean.class));
        }
            extractBeans=temp;
        mView.isBottom[mView.currnetPage]=true;
//        if (1 == page[mView.currnetPage]){
//
//        }
//        else if (null != temp) {
//            extractBeans.addAll(temp);
//
//            if (AppUtils.isEmpty(array))
//                mView.isBottom[mView.currnetPage]=true;
//                MyToast.show(mView, "没有更多内容了");
//        }
        mView.extractAdapter.replaceAll(extractBeans);
    }
    @Override
    protected void handleMessage(int action, Object[] values) {

    }

    @Override
    protected void myHandleMessage(Message msg) {

    }

    public void onRefresh() {
        page[mView.currnetPage]=1;
        if(mView.currnetPage==0){
            sendProfitRecordList();
        }else {
            sendExtract();
        }
    }
}
