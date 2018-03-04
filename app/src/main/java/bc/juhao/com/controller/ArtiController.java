package bc.juhao.com.controller;

import android.content.Intent;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;

import com.google.gson.Gson;
import com.lib.common.hxp.view.GridViewForScrollView;
import com.lib.common.hxp.view.PullToRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import bc.juhao.com.R;
import bc.juhao.com.adapter.BaseAdapterHelper;
import bc.juhao.com.adapter.QuickAdapter;
import bc.juhao.com.bean.ArticlesBean;
import bc.juhao.com.cons.Constance;
import bc.juhao.com.listener.INetworkCallBack;
import bc.juhao.com.ui.activity.ArtiActivity;
import bc.juhao.com.ui.activity.WebViewHomeActivity;
import bocang.json.JSONArray;
import bocang.json.JSONObject;
import bocang.utils.AppUtils;
import bocang.utils.LogUtils;
import bocang.utils.MyToast;

/**
 * Created by bocang on 18-2-8.
 */

public class ArtiController extends BaseController implements PullToRefreshLayout.OnRefreshListener {

    private ArtiActivity mView;
    private int page;
    private int pagePer;
    private GridViewForScrollView listview;
    private PullToRefreshLayout mPullToRefreshLayout;
    private List<ArticlesBean> mArticlesBeans;
    private QuickAdapter adapter;

    @Override
    protected void handleMessage(int action, Object[] values) {

    }

    @Override
    protected void myHandleMessage(Message msg) {

    }
    public  ArtiController(ArtiActivity v){
        mView = v;
        initView();
        initData();
    }

    private void initView() {
        listview = mView.findViewById(R.id.listview);
        mPullToRefreshLayout = ((PullToRefreshLayout) mView.findViewById(R.id.refresh_view));
        mPullToRefreshLayout.setOnRefreshListener(this);
        adapter = new QuickAdapter<ArticlesBean>(mView, R.layout.item_article){
            @Override
            protected void convert(BaseAdapterHelper helper, ArticlesBean item) {
            helper.setText(R.id.title,item.getTitle());
            }
        };
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent(mView, WebViewHomeActivity.class);
                intent.putExtra("url",mArticlesBeans.get(i).getUrl());
                mView.startActivity(intent);
            }
        });
    }

    private void initData() {
        page = 1;
        pagePer = 20;
        mArticlesBeans = new ArrayList<>();
        sendArticle();
    }
    public void sendArticle(){
        LogUtils.logE("art:",page+"");
        mNetWork.sendArticle(1, 20, new INetworkCallBack() {
            @Override
            public void onSuccessListener(String requestCode, JSONObject ans) {
                if (null != mPullToRefreshLayout) {
                    dismissRefesh();
                }
                JSONArray mArticlesArray = ans.getJSONArray(Constance.articles);

                if (1 == page){
                    mArticlesBeans=new ArrayList<>();
                    for(int i=0;i<mArticlesArray.length();i++){
                        JSONObject jsonObject = mArticlesArray.getJSONObject(i);
                        if (jsonObject.getInt(Constance.article_type) == 1) {
                            mArticlesBeans.add(new Gson().fromJson(String.valueOf(mArticlesArray.getJSONObject(i)), ArticlesBean.class));
                        }
                    }
                }
                else if (null != mArticlesBeans) {
                    for (int i = 0; i < mArticlesArray.length(); i++) {
                        JSONObject jsonObject = mArticlesArray.getJSONObject(i);
                        if (jsonObject.getInt(Constance.article_type) == 1) {
                            mArticlesBeans.add(new Gson().fromJson(String.valueOf(mArticlesArray.getJSONObject(i)), ArticlesBean.class));
                        }
                    }

                    if (AppUtils.isEmpty(mArticlesArray))
                        MyToast.show(mView, "没有更多内容了");
                }
                adapter.replaceAll(mArticlesBeans);
                adapter.notifyDataSetChanged();
                if (mArticlesBeans.size() == 0)
                    return;

            }

            @Override
            public void onFailureListener(String requestCode, JSONObject ans) {
                if (null != mPullToRefreshLayout) {
                    dismissRefesh();
                }
            }
        });
    }
    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
    page=1;
    sendArticle();
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
    page++;
    sendArticle();
    }
    private void dismissRefesh() {
        mPullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
        mPullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
    }

}
