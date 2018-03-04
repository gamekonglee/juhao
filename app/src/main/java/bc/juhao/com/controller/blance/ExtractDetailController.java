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
import bc.juhao.com.ui.activity.blance.ExtractDetailActivity;
import bc.juhao.com.ui.view.dialog.SpotsDialog;
import bc.juhao.com.utils.DateUtils;
import bocang.utils.AppUtils;
import bocang.utils.MyToast;

/**
 * @author: Jun
 * @date : 2017/7/11 10:56
 * @description :
 */
public class ExtractDetailController extends BaseController implements AdapterView.OnItemClickListener, PullToRefreshLayout.OnRefreshListener {
    private ExtractDetailActivity mView;

    private JSONArray mApliayList;
    private PullToRefreshLayout mPullToRefreshLayout;
    private ProAdapter mProAdapter;
    private ListViewForScrollView order_sv;
    private int page = 1;

    private View mNullView;
    private View mNullNet;
    private Button mRefeshBtn;
    private TextView mNullNetTv;
    private TextView mNullViewTv;
    private ProgressBar pd;
    private SpotsDialog mDialog;

    public ExtractDetailController(ExtractDetailActivity v){
        mView=v;
        initView();
        initViewData();
    }

    private void initViewData() {
        sendApliayList();
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
        mDialog=new SpotsDialog(mView);
    }

    @Override
    protected void handleMessage(int action, Object[] values) {

    }

    @Override
    protected void myHandleMessage(Message msg) {

    }

    /**
     * 获取提现记录列表
     */
    public void sendApliayList(){
        mDialog.show();
        mNetWork.sendAlipayList(new INetworkCallBack02() {
            @Override
            public void onSuccessListener(String requestCode, JSONObject ans) {
                mDialog.dismiss();
                switch (requestCode){
                    case NetWorkConst.ALIPAY_LIST_URL:
                        if (null == mView || mView.isFinishing())
                            return;
                        if (null != mPullToRefreshLayout) {
                            dismissRefesh();
                        }
                        JSONArray dataList=ans.getJSONArray(Constance.data);
                        if (AppUtils.isEmpty(dataList) || dataList.size()==0) {
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
                mDialog.dismiss();
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

    private void getDataSuccess(JSONArray array){
        if (1 == page)
            mApliayList = array;
        else if (null != mApliayList){
            for (int i = 0; i < array.size(); i++) {
                mApliayList.add(array.getJSONObject(i));
            }

            if(AppUtils.isEmpty(array))
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
        sendApliayList();
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
        sendApliayList();
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
        sendApliayList();
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
            String add_time=jsonObject.getString(Constance.add_time);
            String amount=jsonObject.getString(Constance.amount);
            String time=DateUtils.getStrTime(add_time);
            holder.num_tv.setText(amount);
            holder.time_tv.setText(time);
            String admin_note=jsonObject.getString(Constance.admin_note);
            int is_paid=jsonObject.getInteger(Constance.is_paid);
            if(is_paid==1){
                holder.exchange_tv.setText("提现成功");
            }else if(is_paid==0){
                holder.exchange_tv.setText("提现中");
            }else{
                holder.exchange_tv.setText("提现失败");
            }
            return convertView;
        }

        class ViewHolder {
            TextView exchange_tv;
            TextView num_tv;
            TextView time_tv;

        }
    }
}
