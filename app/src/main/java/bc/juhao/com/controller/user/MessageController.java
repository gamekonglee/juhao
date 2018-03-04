package bc.juhao.com.controller.user;

import android.content.Intent;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lib.common.hxp.view.PullToRefreshLayout;
import com.yjn.swipelistview.swipelistviewlibrary.widget.SwipeMenuListView;

import bc.juhao.com.R;
import bc.juhao.com.cons.Constance;
import bc.juhao.com.cons.NetWorkConst;
import bc.juhao.com.controller.BaseController;
import bc.juhao.com.listener.INetworkCallBack;
import bc.juhao.com.ui.activity.user.MessageActivity;
import bc.juhao.com.ui.activity.user.MessageDetailActivity;
import bocang.json.JSONArray;
import bocang.json.JSONObject;
import bocang.utils.AppUtils;
import bocang.utils.MyToast;

/**
 * @author: Jun
 * @date : 2017/3/10 15:50
 * @description : 消息中心
 */
public class MessageController extends BaseController implements PullToRefreshLayout.OnRefreshListener, AdapterView.OnItemClickListener, INetworkCallBack {
    private MessageActivity mView;
    private JSONArray mMessageLists;
    private PullToRefreshLayout mPullToRefreshLayout;
    private ProAdapter mProAdapter;
    private SwipeMenuListView message_lv;
    private int page = 1;
    private View mNullView;
    private View mNullNet;
    private Button mRefeshBtn;
    private TextView mNullNetTv;
    private TextView mNullViewTv;
    private ProgressBar pd;


    public MessageController(MessageActivity v) {
        mView = v;
        initView();
        initViewData();
    }

    private void initViewData() {
        int page=1;
        sendArticle();
    }

    /**
     * 获取文章列表
     */
    private void sendArticle() {
        mView.showLoadingPage("", R.drawable.ic_loading);
        int per_page = 20;
        mNetWork.sendArticle(page, per_page, this);
    }

    private void initView() {
        message_lv = (SwipeMenuListView) mView.findViewById(R.id.message_lv);
        mPullToRefreshLayout = ((PullToRefreshLayout) mView.findViewById(R.id.contentView));
        mPullToRefreshLayout.setOnRefreshListener(this);
        mProAdapter = new ProAdapter();
        message_lv.setAdapter(mProAdapter);
        message_lv.setOnItemClickListener(this);

        mNullView = mView.findViewById(R.id.null_view);
        mNullNet = mView.findViewById(R.id.null_net);
        mRefeshBtn = (Button) mNullNet.findViewById(R.id.refesh_btn);
        mNullNetTv = (TextView) mNullNet.findViewById(R.id.tv);
        mNullViewTv = (TextView) mNullView.findViewById(R.id.tv);
        pd = (ProgressBar) mView.findViewById(R.id.pd);

    }

    @Override
    protected void handleMessage(int action, Object[] values) {

    }

    @Override
    protected void myHandleMessage(Message msg) {

    }

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
        page = 1;
        sendArticle();
//        sendNotice(page);
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
        page=page+1;
        sendArticle();
//        sendNotice(page);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onSuccessListener(String requestCode, JSONObject ans) {
        mView.showContentView();
        pd.setVisibility(View.INVISIBLE);
        switch (requestCode) {
//            case NetWorkConst.NOTICELIST:
//                if (null == mView || mView.isFinishing())
//                    return;
//                if (null != mPullToRefreshLayout) {
//                    dismissRefesh();
//                }
//
//                JSONArray goodsList = ans.getJSONArray(Constance.notices);
//                if (AppUtils.isEmpty(goodsList) ||goodsList.length()==0) {
//                    if (page == 1) {
//                        mNullView.setVisibility(View.VISIBLE);
//                    }
//
//                    dismissRefesh();
//                    return;
//                }
//
//                mNullView.setVisibility(View.GONE);
//                mNullNet.setVisibility(View.GONE);
//                getDataSuccess(goodsList);
//                break;
            case NetWorkConst.ARTICLELIST:
                if (null == mView || mView.isFinishing())
                    return;
                if (null != mPullToRefreshLayout) {
                    dismissRefesh();
                }

                JSONArray goodsList = ans.getJSONArray(Constance.articles);
                if (AppUtils.isEmpty(goodsList) ||goodsList.length()==0) {
                    if (page == 1) {
                        mNullView.setVisibility(View.VISIBLE);
                    }

                    dismissRefesh();
                    return;
                }

                mNullView.setVisibility(View.GONE);
                mNullNet.setVisibility(View.GONE);
                getDataSuccess(goodsList);
                break;
        }
    }

    @Override
    public void onFailureListener(String requestCode, JSONObject ans) {
        pd.setVisibility(View.INVISIBLE);
        if (null == mView || mView.isFinishing())
            return;
        this.page--;

        if (AppUtils.isEmpty(ans)) {
            mNullNet.setVisibility(View.VISIBLE);
            mRefeshBtn.setOnClickListener(mRefeshBtnListener);
            return;
        }

        if (null != mPullToRefreshLayout) {
            mPullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
            mPullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
        }
    }

    private void dismissRefesh() {
        mPullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
        mPullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
    }

    private void getDataSuccess(JSONArray array) {
        if (1 == page)
            mMessageLists = array;
        else if (null != mMessageLists) {
            for (int i = 0; i < array.length(); i++) {
                mMessageLists.add(array.getJSONObject(i));
            }

            if (AppUtils.isEmpty(array) ||array.length()==0)
                MyToast.show(mView, "没有更多内容了");
        }
        mProAdapter.notifyDataSetChanged();
    }
    private View.OnClickListener mRefeshBtnListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onRefresh();
        }
    };

    public void onRefresh() {
        page = 1;
        sendArticle();
//        sendNotice(page);
    }

    private class ProAdapter extends BaseAdapter {
        public ProAdapter() {
        }

        @Override
        public int getCount() {
            if (null == mMessageLists)
                return 0;
            return mMessageLists.length();
        }

        @Override
        public JSONObject getItem(int position) {
            if (null == mMessageLists)
                return null;
            return mMessageLists.getJSONObject(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(mView, R.layout.item_gridview_fm_scene, null);

                holder = new ViewHolder();
                holder.name_tv = (TextView) convertView.findViewById(R.id.name_tv);
                holder.filter_name_tv = (TextView) convertView.findViewById(R.id.filter_name_tv);
                holder.filter_rl= (RelativeLayout) convertView.findViewById(R.id.filter_rl);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            final JSONObject object = mMessageLists.getJSONObject(position);
            String message=object.getString(Constance.title);
            if(!AppUtils.isEmpty(message)){
                holder.name_tv.setText(message);
            }
            holder.filter_rl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(mView,MessageDetailActivity.class);
                    intent.putExtra(Constance.url,object.getString(Constance.url));
                    mView.startActivity(intent);
                }
            });
            return convertView;
        }

        class ViewHolder {
            TextView name_tv;
            TextView filter_name_tv;
            RelativeLayout filter_rl;

        }
    }

}
