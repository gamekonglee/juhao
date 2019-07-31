package bc.juhao.com.controller;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.text.DecimalFormat;

import bc.juhao.com.R;
import bc.juhao.com.cons.Constance;
import bc.juhao.com.listener.INetworkCallBack;
import bc.juhao.com.ui.activity.TaoCanHomeListActivity;
import bc.juhao.com.ui.activity.product.ProDetailActivity;
import bc.juhao.com.ui.view.EndOfListView;
import bc.juhao.com.ui.view.PMSwipeRefreshLayout;
import bc.juhao.com.utils.UIUtils;
import bocang.json.JSONArray;
import bocang.json.JSONObject;
import bocang.utils.AppUtils;
import bocang.utils.MyToast;

/**
 * Created by gamekonglee on 2018/6/13.
 */

public class TaoCanHomeController extends BaseController implements SwipeRefreshLayout.OnRefreshListener, EndOfListView.OnEndOfListListener {

    private final TaoCanHomeListActivity mView;
    private PMSwipeRefreshLayout mPullToRefreshLayout;
    private EndOfListView listView;
    private int page;
    private JSONArray goodses;
    private ProAdapter mProAdapter;

    @Override
    protected void handleMessage(int action, Object[] values) {

    }

    @Override
    protected void myHandleMessage(Message msg) {

    }
    public  TaoCanHomeController(TaoCanHomeListActivity view){
        mView = view;
        intiUI();
        page = 1;
        goodses = new JSONArray();
        initData();
    }

    private void initData() {
        mPullToRefreshLayout.setRefreshing(true);
        mNetWork.sendGoodsList(page, "20", null, "224", null, null, null, null, null, new INetworkCallBack() {
            @Override
            public void onSuccessListener(String requestCode, JSONObject ans) {
                if (null == mView || mView.isFinishing())
                    return;
                if (null != mPullToRefreshLayout) {
                    dismissRefesh();
                }

                JSONArray goodsList = ans.getJSONArray(Constance.goodsList);
                if (AppUtils.isEmpty(goodsList) || goodsList.length() == 0) {
                    if (page == 1) {
                    } else {
                        MyToast.show(mView, "没有更多数据了!");
                    }

                    dismissRefesh();
                    return;
                }

                getDataSuccess(goodsList);
            }

            @Override
            public void onFailureListener(String requestCode, JSONObject ans) {

            }
        });
    }
    private void getDataSuccess(JSONArray array) {
        if (1 == page)
            goodses = array;
        else if (null != goodses) {
            for (int i = 0; i < array.length(); i++) {
                goodses.add(array.getJSONObject(i));
            }

            if (AppUtils.isEmpty(array))
                MyToast.show(mView, "没有更多内容了");
        }
        mProAdapter.notifyDataSetChanged();
    }
    private void dismissRefesh() {
        if (null != mPullToRefreshLayout) {
            mPullToRefreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    mPullToRefreshLayout.setRefreshing(false);
                }
            });
        }
    }

    private void intiUI() {
        mPullToRefreshLayout = mView.findViewById(R.id.pullToRefresh);
        mPullToRefreshLayout.setColorSchemeColors(Color.BLUE,Color.GREEN,Color.YELLOW,Color.RED);
        mPullToRefreshLayout.setRefreshing(false);
        mPullToRefreshLayout.setOnRefreshListener(this);
        listView = mView.findViewById(R.id.listview);
        listView.setOnEndOfListListener(this);
        mProAdapter = new ProAdapter();
        listView.setAdapter(mProAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(mView, ProDetailActivity.class);
                intent.putExtra(Constance.product,goodses.getJSONObject(position).getInt(Constance.id));
                mView.startActivity(intent);
            }
        });
    }

    @Override
    public void onRefresh() {
        page=1;
        initData();
    }

    @Override
    public void onEndOfList(Object lastItem) {
        if(page==1&&goodses.length()==0){
            return;
        }
        page++;
        initData();

    }

    private class ProAdapter extends BaseAdapter {
        public ProAdapter() {
        }

        @Override
        public int getCount() {
            if (null == goodses)
                return 0;
            return goodses.length();
        }

        @Override
        public JSONObject getItem(int position) {
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
                convertView = View.inflate(mView, R.layout.item_gridview_fm_product_taocan, null);
                holder = new ViewHolder();
                holder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
                holder.check_iv = (ImageView) convertView.findViewById(R.id.check_iv);
                holder.textView = (TextView) convertView.findViewById(R.id.name_tv);
                holder.groupbuy_tv = (TextView) convertView.findViewById(R.id.groupbuy_tv);
                holder.old_price_tv = (TextView) convertView.findViewById(R.id.old_price_tv);
                holder.price_tv = (TextView) convertView.findViewById(R.id.price_tv);
                RelativeLayout.LayoutParams lLp = (RelativeLayout.LayoutParams) holder.imageView.getLayoutParams();
                lLp.setMargins(10,10,10,10);
                float w = UIUtils.getScreenWidth(mView)-20;
                lLp.width = (int) w;
                lLp.height= (int) (w*(170)/375);
                holder.imageView.setLayoutParams(lLp);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            try {
                String name = goodses.getJSONObject(position).getString(Constance.name);
                holder.textView.setText(name);
                //                holder.imageView.setImageResource(R.drawable.bg_default);
                ImageLoader.getInstance().displayImage(goodses.getJSONObject(position).getJSONObject(Constance.default_photo).getString(Constance.thumb)
                        , holder.imageView);

                JSONObject groupBuyObject = goodses.getJSONObject(position).getJSONObject(Constance.group_buy);
                int isFinished=-1;
                if(!AppUtils.isEmpty(groupBuyObject))
                {
                    isFinished=groupBuyObject.getInt(Constance.is_finished);
                }

                holder.groupbuy_tv.setVisibility(isFinished==0? View.VISIBLE : View.GONE);
                double old_Price=0;
                JSONArray propertieArray = goodses.getJSONObject(position).getJSONArray(Constance.properties);
                if (!AppUtils.isEmpty(propertieArray)&&propertieArray.length()>0) {
                    JSONArray attrsArray = propertieArray.getJSONObject(0).getJSONArray(Constance.attrs);
                    int price = attrsArray.getJSONObject(0).getInt(Constance.attr_price);
                    double currentPrice = price;
                    old_Price=currentPrice;
                    holder.price_tv.setText("￥" + currentPrice);
                } else {
                    old_Price= Double.parseDouble(goodses.getJSONObject(position).getString(Constance.current_price));
                    holder.price_tv.setText("￥" + goodses.getJSONObject(position).getString(Constance.current_price));
                }
                old_Price=old_Price*1.6;
                DecimalFormat df=new DecimalFormat("###.00");
                holder.old_price_tv.setText("￥" + df.format(old_Price));
                holder.old_price_tv.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                holder.check_iv.setVisibility(View.GONE);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return convertView;
        }

        class ViewHolder {
            ImageView imageView;
            ImageView check_iv;
            TextView textView;
            TextView groupbuy_tv;
            TextView old_price_tv;
            TextView price_tv;

        }
    }
}
