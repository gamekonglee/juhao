package bc.juhao.com.controller.blance;

import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lib.common.hxp.view.ListViewForScrollView;
import com.lib.common.hxp.view.PullToRefreshLayout;

import bc.juhao.com.R;
import bc.juhao.com.cons.Constance;
import bc.juhao.com.cons.NetWorkConst;
import bc.juhao.com.controller.BaseController;
import bc.juhao.com.listener.INetworkCallBack02;
import bc.juhao.com.ui.activity.IssueApplication;
import bc.juhao.com.ui.activity.blance.ProfitRecordActivity;
import bc.juhao.com.utils.DateUtils;
import bocang.utils.AppUtils;
import bocang.utils.MyToast;

/**
 * @author: Jun
 * @date : 2017/7/27 17:47
 * @description :
 */
public class ProfitRecordController extends BaseController implements AdapterView.OnItemClickListener, PullToRefreshLayout.OnRefreshListener {
    private ProfitRecordActivity mView;
    private JSONArray mApliayList;
    private PullToRefreshLayout mPullToRefreshLayout;
    private ProAdapter mProAdapter;
    private ListViewForScrollView order_sv;
    private int page = 1;
    private int per_pag = 20;
    private View mNullView;
    private View mNullNet;
    private Button mRefeshBtn;
    private TextView mNullNetTv;
    private TextView mNullViewTv;
    private ProgressBar pd;

    public ProfitRecordController(ProfitRecordActivity v) {
        mView = v;
        initView();
        initViewData();
    }

    private void initViewData() {
        //        mView.showLoadingPage("", R.drawable.ic_loading);
        page = 1;
        sendProfitRecordList();
    }

    private void initView() {
        mPullToRefreshLayout = ((PullToRefreshLayout) mView.findViewById(R.id.contentView));
        mPullToRefreshLayout.setOnRefreshListener(this);

        order_sv = (ListViewForScrollView) mView.findViewById(R.id.order_sv);
        order_sv.setDivider(null);//去除listview的下划线
        order_sv.setOnItemClickListener(this);
        mProAdapter = new ProAdapter();
        order_sv.setAdapter(mProAdapter);

        mNullView = mView.findViewById(R.id.null_view);
        mNullNet = mView.findViewById(R.id.null_net);
        mRefeshBtn = (Button) mNullNet.findViewById(R.id.refesh_btn);
        mNullNetTv = (TextView) mNullNet.findViewById(R.id.tv);
        mNullViewTv = (TextView) mNullView.findViewById(R.id.tv);
        pd = (ProgressBar) mView.findViewById(R.id.pd);
    }

    /**
     * 获取提现记录列表
     */
    public void sendProfitRecordList() {
        mView.setShowDialog(true);
        mView.setShowDialog("载入中..");
        mView.showLoading();
        mNetWork.sendProfitRecordList
                (page, per_pag, new INetworkCallBack02() {
                    @Override
                    public void onSuccessListener(String requestCode, JSONObject ans) {
                        mView.hideLoading();
                        switch (requestCode) {
                            case NetWorkConst.SALESACCOUNT_URL:
                                if (null == mView || mView.isFinishing())
                                    return;
                                if (null != mPullToRefreshLayout) {
                                    dismissRefesh();
                                }
                                JSONArray dataList = ans.getJSONArray(Constance.account);
                                if (AppUtils.isEmpty(dataList) || dataList.size() == 0) {
                                    if (page == 1) {
                                        mNullView.setVisibility(View.VISIBLE);
                                    }

                                    dismissRefesh();
                                    return;
                                }

                                mNullView.setVisibility(View.GONE);
                                mNullNet.setVisibility(View.GONE);
                                getDataSuccess(dataList);
                                break;
                        }
                    }

                    @Override
                    public void onFailureListener(String requestCode, JSONObject ans) {
                        mView.hideLoading();
                        if (AppUtils.isEmpty(ans)) {
                            mNullNet.setVisibility(View.VISIBLE);
                            mRefeshBtn.setOnClickListener(mRefeshBtnListener);
                            return;
                        }

                        if (null != mPullToRefreshLayout) {
                            dismissRefesh();
                        }
                    }
                });
    }

    private void getDataSuccess(JSONArray array) {
        if (1 == page)
            mApliayList = array;
        else if (null != mApliayList) {
            for (int i = 0; i < array.size(); i++) {
                mApliayList.add(array.getJSONObject(i));
            }

            if (AppUtils.isEmpty(array))
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
        sendProfitRecordList();
    }

    private void dismissRefesh() {
        mPullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
        mPullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
        pd.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
        page = 1;
        sendProfitRecordList();
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
        page = page + 1;
        sendProfitRecordList();
    }

    @Override
    protected void handleMessage(int action, Object[] values) {

    }

    @Override
    protected void myHandleMessage(Message msg) {

    }


    private class ProAdapter extends BaseAdapter {
        public ProAdapter() {
        }

        @Override
        public int getCount() {
            if (null == mApliayList)
                return 0;
            return mApliayList.size();
        }

        @Override
        public JSONObject getItem(int position) {
            if (null == mApliayList)
                return null;
            return mApliayList.getJSONObject(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(mView, R.layout.item_exchange_detail, null);

                holder = new ViewHolder();
                holder.exchange_tv = (TextView) convertView.findViewById(R.id.exchange_tv);
                holder.num_tv = (TextView) convertView.findViewById(R.id.num_tv);
                holder.time_tv = (TextView) convertView.findViewById(R.id.time_tv);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            JSONObject jsonObject = mApliayList.getJSONObject(position);
            String add_time = jsonObject.getString(Constance.created_at);
            String amount = jsonObject.getString(Constance.money);
            String customer_user_name = jsonObject.getString(Constance.customer);
            String user_name = jsonObject.getString(Constance.user_name);
            int customerlevel = jsonObject.getInteger(Constance.customer_level);
            int level = jsonObject.getInteger(Constance.level);
            String time = DateUtils.getStrTime(add_time);
            String customerUserId=jsonObject.getString(Constance.user_id);
            String userId= IssueApplication.mUserObject.getString(Constance.id);
            if(customerUserId.equals(userId)){
                holder.num_tv.setText("+"+amount);
                holder.exchange_tv.setText(customer_user_name +getLevel(level)+ "购买了产品");
            }else{
                holder.num_tv.setText(amount+"");
                holder.exchange_tv.setText("【下级收益】"+customer_user_name +getLevel(customerlevel)+ "购买了产品 您的"+user_name+getLevel(level)+"获得了收益");
            }
//            holder.num_tv.setText(amount);
            holder.time_tv.setText(time);
//            holder.exchange_tv.setText(user_name +getLevel(level)+ "购买了产品");
            return convertView;
        }

        class ViewHolder {
            TextView exchange_tv;
            TextView num_tv;
            TextView time_tv;

        }
    }


    private String getLevel(int level) {
        String levelValue = "";
        switch (level) {
            case 0:
                levelValue = "(一级)";
                break;
            case 1:
                levelValue = "(二级)";
                break;
            case 2:
                levelValue = "(三级)";
                break;
            case 3:
                levelValue = "(消费者)";
                break;
            case 4:
                levelValue = "(消费者)";
                break;
        }
        return levelValue;
    }
}
