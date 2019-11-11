package bc.juhao.com.controller;

import android.content.Intent;
import android.graphics.Color;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import bc.juhao.com.R;
import bc.juhao.com.adapter.BaseAdapterHelper;
import bc.juhao.com.adapter.QuickAdapter;
import bc.juhao.com.bean.ArticlesBean;
import bc.juhao.com.cons.Constance;
import bc.juhao.com.cons.NetWorkConst;
import bc.juhao.com.listener.INetworkCallBack;
import bc.juhao.com.ui.activity.BrandPlayActivity;
import bc.juhao.com.ui.fragment.home.VideoHomeFragment;
import bc.juhao.com.ui.view.EndOfListView;
import bc.juhao.com.ui.view.PMSwipeRefreshLayout;
import bc.juhao.com.utils.ShareUtil;
import bocang.json.JSONArray;
import bocang.json.JSONObject;

/**
 * Created by gamekonglee on 2018/4/18.
 */
public class VideoController extends BaseController implements SwipeRefreshLayout.OnRefreshListener, EndOfListView.OnEndOfListListener, INetworkCallBack {

    private final VideoHomeFragment mView;
    private PMSwipeRefreshLayout pullToRefresh;

    private QuickAdapter adapter;
    public List<ArticlesBean> articlesBeans;
    private int page;
    private boolean noRecord;//没有内容

    public VideoController(VideoHomeFragment videoHomeFragment) {
        mView = videoHomeFragment;
        initView();
        initData();
    }

    private void initData() {
//        mNetWork.sendVideoA();
        page = 0;
        mView.currentType = 0;
        noRecord = false;
    }

    private void initView() {
        pullToRefresh = mView.getView().findViewById(R.id.pullToRefresh);
        pullToRefresh.setColorSchemeColors(Color.RED, Color.GREEN, Color.BLUE, Color.CYAN);
        pullToRefresh.setRefreshing(false);
        pullToRefresh.setOnRefreshListener(this);

        EndOfListView listView = mView.getView().findViewById(R.id.lv_video);
        listView.setOnEndOfListListener(this);

        adapter = new QuickAdapter<ArticlesBean>(mView.getContext(), R.layout.item_lv_video) {
            @Override
            protected void convert(BaseAdapterHelper helper, final ArticlesBean item) {

                helper.setText(R.id.tv_title, item.getTitle());
                ImageView imageView = helper.getView(R.id.iv_img);

                if (mView.currentType == 0) {
                    helper.setVisible(R.id.iv_share, true);
                } else {
                    helper.setVisible(R.id.iv_share, false);
                }
                helper.setOnClickListener(R.id.iv_share, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ShareUtil.shareWxVideo(mView.getActivity(), item.getTitle(), item.getLink());
                    }
                });

//            if(helper.getPosition()==0){
//                imageView.setImageResource(R.mipmap.video_a);
//            }else if(helper.getPosition()==1){
//                imageView.setImageResource(R.mipmap.video_b);
//            }else {
//                imageView.setImageResource(R.drawable.bg_default);
//            }
                ImageLoader.getInstance().displayImage(NetWorkConst.SCENE_HOST + item.getFile_url(), imageView);
                helper.setOnClickListener(R.id.iv_start, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mView.getContext(), BrandPlayActivity.class);
                        int type = 0;
                        if (mView.currentType == 0 && item.getDisplay() == 1 || mView.currentType == 1 && item.getDisplay() == 0) {
                            type = 0;
                        } else {
                            type = 1;
                        }
                        intent.putExtra(Constance.type, type);
                        intent.putExtra(Constance.url, item.getLink());
                        mView.startActivity(intent);
                    }
                });
            }
        };
        listView.setAdapter(adapter);
        articlesBeans = new ArrayList<>();
    }

    @Override
    public void onRefresh() {
        page = 1;
        articlesBeans = new ArrayList<>();
        if (mView.currentType == 0) {
            sendVideoA();
        } else {
            sendVideoB();
        }
    }

    @Override
    public void onEndOfList(Object lastItem) {
        if (page == 1 && articlesBeans.size() != 0 || noRecord) {
            return;
        }
        page++;
        pullToRefresh.setRefreshing(true);
        if (mView.currentType == 0) {
            sendVideoA();
        } else {
            sendVideoB();
        }
    }


    /**
     * 教学视频
     */
    public void sendVideoB() {
        noRecord = false;
        mNetWork.sendVideoB(page, 20, this);
    }

    /**
     * 宣传视频
     */
    public void sendVideoA() {
        noRecord = false;
        mNetWork.sendVideoA(page, 20, this);
    }

    @Override
    public void onSuccessListener(String requestCode, JSONObject ans) {
        dismissReresh();
        JSONArray mArticlesArray = ans.getJSONArray(Constance.articles);
        if (mArticlesArray.length() == 0 && page == 1) {
            noRecord = true;
        }
        for (int i = 0; i < mArticlesArray.length(); i++) {
            JSONObject jsonObject = mArticlesArray.getJSONObject(i);
            int id = jsonObject.getInt(Constance.id);
            String title = jsonObject.getString(Constance.title);
            String url = jsonObject.getString(Constance.url);
            String link = jsonObject.getString(Constance.link);//视频链接
            String file_url = jsonObject.getString(Constance.file_url);//图片链接
            articlesBeans.add(new ArticlesBean(id, title, url, link, file_url, jsonObject.getInt(Constance.display)));
        }
        adapter.replaceAll(articlesBeans);
    }

    private void dismissReresh() {
        pullToRefresh.post(new Runnable() {
            @Override
            public void run() {
                pullToRefresh.setRefreshing(false);
            }
        });
    }

    @Override
    public void onFailureListener(String requestCode, JSONObject ans) {
        dismissReresh();
    }

    @Override
    protected void handleMessage(int action, Object[] values) {

    }

    @Override
    protected void myHandleMessage(Message msg) {

    }
}
