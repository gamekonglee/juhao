package bc.juhao.com.controller.buy;

import android.content.Intent;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.lib.common.hxp.view.ListViewForScrollView;
import com.lib.common.hxp.view.PullToRefreshLayout;

import bc.juhao.com.R;
import bc.juhao.com.cons.Constance;
import bc.juhao.com.cons.NetWorkConst;
import bc.juhao.com.controller.BaseController;
import bc.juhao.com.listener.INetworkCallBack;
import bc.juhao.com.listener.INetworkCallBack02;
import bc.juhao.com.listener.IUpdateProductPriceListener;
import bc.juhao.com.ui.activity.buy.SelectOrderActivity;
import bc.juhao.com.ui.adapter.OrderGvAdapter;
import bc.juhao.com.utils.DateUtils;
import bocang.json.JSONObject;
import bocang.utils.AppUtils;
import bocang.utils.MyToast;

import static bc.juhao.com.R.id.send_tv;

/**
 * @author Jun
 * @time 2017/12/11  23:19
 * @desc ${TODD}
 */

public class SelectOrderController extends BaseController implements PullToRefreshLayout.OnRefreshListener, INetworkCallBack {
    private SelectOrderActivity mView;
    private ListViewForScrollView order_sv;
    private View mNullView;
    private View mNullNet;
    public JSONArray goodses = new JSONArray();
    private PullToRefreshLayout mPullToRefreshLayout;
    private ProAdapter mProAdapter;
    private Button mRefeshBtn;
    private TextView mNullNetTv;
    private TextView mNullViewTv;
    private ProgressBar pd;
    private int page = 1;

    public SelectOrderController(SelectOrderActivity v) {
        mView = v;
        initView();
        initViewData();
    }

    private void initViewData() {
        sendOrderList();
    }

    private void initView() {
        mPullToRefreshLayout = ((PullToRefreshLayout) mView.findViewById(R.id.refresh_view));
        mPullToRefreshLayout.setOnRefreshListener(this);
        order_sv = (ListViewForScrollView) mView.findViewById(R.id.order_sv);
        mProAdapter = new ProAdapter();
        order_sv.setAdapter(mProAdapter);

        mNullView = mView.findViewById(R.id.null_view);
        mNullNet = mView.findViewById(R.id.null_net);
        mRefeshBtn = (Button) mNullNet.findViewById(R.id.refesh_btn);
        mNullNetTv = (TextView) mNullNet.findViewById(R.id.tv);
        mNullViewTv = (TextView) mNullView.findViewById(R.id.tv);
        pd = (ProgressBar) mView.findViewById(R.id.pd);
    }

    private void dismissRefesh() {
        mPullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
        mPullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
    }

    @Override
    protected void handleMessage(int action, Object[] values) {

    }

    @Override
    protected void myHandleMessage(Message msg) {

    }

    private void sendOrderList() {
        mNetWork.sendorderList(page, 20, "-1", new INetworkCallBack02() {
            @Override
            public void onSuccessListener(String requestCode, com.alibaba.fastjson.JSONObject ans) {
                mView.hideLoading();
                switch (requestCode) {
                    case NetWorkConst.ORDERLIST:
                        if (null == mView || mView.isFinishing())
                            return;
                        if (null != mPullToRefreshLayout) {
                            dismissRefesh();
                        }
                        break;
                }

                JSONArray goodsList = ans.getJSONArray(Constance.orders);
                if (AppUtils.isEmpty(goodsList) || goodsList.size() == 0) {
                    if (page == 1) {
                        mNullView.setVisibility(View.VISIBLE);
                    }

                    dismissRefesh();
                    return;
                }

                mNullView.setVisibility(View.GONE);
                mNullNet.setVisibility(View.GONE);
                getDataSuccess(goodsList);
            }

            @Override
            public void onFailureListener(String requestCode, com.alibaba.fastjson.JSONObject ans) {
                mView.hideLoading();
                pd.setVisibility(View.INVISIBLE);
                if (null == mView || mView.isFinishing())
                    return;
                page--;

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
        });
    }


    private void reviceProductList() {
        mNetWork.reviceProductList(1 + "", page, this);
    }

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
        page = 1;
        reviceProductList();
    }

    public void onRefresh() {
        page = 1;
        reviceProductList();
    }

    private View.OnClickListener mRefeshBtnListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onRefresh();
        }
    };

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
        page = page + 1;
        reviceProductList();
    }

    @Override
    public void onSuccessListener(String requestCode, JSONObject ans) {
    }

    @Override
    public void onFailureListener(String requestCode, JSONObject ans) {

    }

    private void getDataSuccess(JSONArray array) {
        if (1 == page)
            goodses = array;
        else if (null != goodses) {
            for (int i = 0; i < array.size(); i++) {
                goodses.add(array.getJSONObject(i));
            }

            if (AppUtils.isEmpty(array))
                MyToast.show(mView, "没有更多内容了");
        }
        mProAdapter.notifyDataSetChanged();
    }

    private class ProAdapter extends BaseAdapter {
        public ProAdapter() {
        }

        @Override
        public int getCount() {
            return goodses.size();
        }

        @Override
        public com.alibaba.fastjson.JSONObject getItem(int position) {
            if (null == goodses)
                return null;
            return goodses.getJSONObject(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(mView, R.layout.item_select_order, null);

                holder = new ViewHolder();
                holder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
                holder.state_tv = (TextView) convertView.findViewById(R.id.state_tv);
                holder.time_tv = (TextView) convertView.findViewById(R.id.time_tv);
                holder.send_tv = (TextView) convertView.findViewById(send_tv);
                holder.code_tv = (TextView) convertView.findViewById(R.id.code_tv);
                holder.lv = (ListView) convertView.findViewById(R.id.lv);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            final com.alibaba.fastjson.JSONObject orderobject = goodses.getJSONObject(position);
            final int state = orderobject.getInteger(Constance.status);
            getState(state, holder.state_tv);
            holder.code_tv.setText("订单号:" + orderobject.getString(Constance.sn));
            final JSONArray array = orderobject.getJSONArray(Constance.goods);
            holder.time_tv.setText(DateUtils.getStrTime(orderobject.getString(Constance.created_at)));
            OrderGvAdapter maGvAdapter = new OrderGvAdapter(mView, array, 1, state);
            holder.lv.setAdapter(maGvAdapter);
            maGvAdapter.setUpdateProductPriceListener(new IUpdateProductPriceListener() {
                @Override
                public void onUpdateProductPriceListener(int position, com.alibaba.fastjson.JSONObject object) {
                }
            });
            holder.send_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.putExtra(Constance.VALUE, orderobject.getString(Constance.sn));
                    mView.setResult(101, intent);
                    mView.finish();
                }
            });

            return convertView;
        }

        /**
         * 订单状态
         *
         * @param type
         * @param state_tv
         */
        private void getState(int type, TextView state_tv) {
            String stateValue = "";
            switch (type) {
                case 0:
                    stateValue = "【待付款】";
                    break;
                case 1:
                    stateValue = "【待发货】";
                    break;
                case 2:
                    stateValue = "【待收货】";
                    break;
                case 3:
                    stateValue = "【已完成】";
                    break;
                case 4:
                    stateValue = "【已完成】";
                    break;
                case 5:
                    stateValue = "【已取消】";
                    break;
            }
            state_tv.setText(stateValue);
        }

        class ViewHolder {
            ImageView imageView;
            TextView state_tv;
            TextView time_tv;
            TextView send_tv;
            TextView code_tv;
            ListView lv;

        }
    }
}
