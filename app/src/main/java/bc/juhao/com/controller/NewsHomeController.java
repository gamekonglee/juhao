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
import bc.juhao.com.ui.activity.product.ProDetailActivity;
import bc.juhao.com.ui.fragment.home.NewsHomeFragment;
import bc.juhao.com.ui.view.EndOfGridView;
import bc.juhao.com.ui.view.EndOfListView;
import bc.juhao.com.ui.view.PMSwipeRefreshLayout;
import bc.juhao.com.utils.ConvertUtil;
import bc.juhao.com.utils.UIUtils;
import bocang.json.JSONArray;
import bocang.json.JSONObject;
import bocang.utils.AppUtils;
import bocang.utils.MyToast;

/**
 * Created by gamekonglee on 2018/4/16.
 */

public class NewsHomeController extends BaseController implements EndOfListView.OnEndOfListListener, SwipeRefreshLayout.OnRefreshListener {

    private final NewsHomeFragment mView;
    private PMSwipeRefreshLayout pullToRefresh;
    private EndOfGridView priductGridView;
    private int page;
    private JSONArray goodses;
    private int mScreenWidth;
    private ProAdapter mProAdapter;

    @Override
    protected void handleMessage(int action, Object[] values) {

    }

    @Override
    protected void myHandleMessage(Message msg) {

    }
    public NewsHomeController(NewsHomeFragment newsHomeFragment){
        mView =   newsHomeFragment;
        page = 0;
        goodses=new JSONArray();
        initView();
        initData();
    }

    private void initData() {

    }

    private void initView() {
        pullToRefresh = mView.getView().findViewById(R.id.pullToRefresh);
        pullToRefresh.setColorSchemeColors(Color.RED,Color.YELLOW,Color.GREEN,Color.BLUE);
        pullToRefresh.setRefreshing(false);
        priductGridView = mView.getView().findViewById(R.id.priductGridView);
        priductGridView.setOnEndOfListListener(this);
        pullToRefresh.setOnRefreshListener(this);
        mScreenWidth = UIUtils.getScreenWidth(mView.getActivity());
        mProAdapter = new ProAdapter();
        priductGridView.setAdapter(mProAdapter);
        priductGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent mIntent = new Intent(mView.getContext(), ProDetailActivity.class);
                int productId = goodses.getJSONObject(position).getInt(Constance.id);
                mIntent.putExtra(Constance.product, productId);
                mView.startActivity(mIntent);
            }
        });
    }

    @Override
    public void onEndOfList(Object lastItem) {
        ++page;
        pullToRefresh.setRefreshing(true);
        findProduct();
    }


    private void findProduct() {
        mNetWork.sendGoodsList(page, "20", "", "", "", "", "", "5", "2", new INetworkCallBack() {
            @Override
            public void onSuccessListener(String requestCode, JSONObject ans) {
                if (null == mView || mView.getActivity()==null||mView.getActivity().isFinishing())
                    return;
                if (null != pullToRefresh) {
                    dismissRefesh();
                }

                JSONArray goodsList = ans.getJSONArray(Constance.goodsList);
                JSONObject paged=ans.getJSONObject(Constance.paged);
                int pageTemp=page;
                if(paged!=null){
                    pageTemp=paged.getInt(Constance.page);
                }
//                if (AppUtils.isEmpty(goodsList) || goodsList.length() == 0) {
//                    if (page == 1) {
//                        mNullView.setVisibility(View.VISIBLE);
//                    } else {
//                        MyToast.show(mView, "没有更多数据了!");
//                    }
//
//                    dismissRefesh();
//                    return;
//                }

//                mNullView.setVisibility(View.GONE);
//                mNullNet.setVisibility(View.GONE);
                getDataSuccess(goodsList,pageTemp);
            }

            @Override
            public void onFailureListener(String requestCode, JSONObject ans) {

            }
        });
    }

    private void getDataSuccess(JSONArray array, int page) {
        if (1 == page)
            goodses = array;
        else if (null != goodses) {
            for (int i = 0; i < array.length(); i++) {
                goodses.add(array.getJSONObject(i));
            }

            if (AppUtils.isEmpty(array))
                MyToast.show(mView.getContext(), "没有更多内容了");
        }
        mProAdapter.notifyDataSetChanged();
    }

    private void dismissRefesh() {
        pullToRefresh.post(new Runnable() {
            @Override
            public void run() {
                pullToRefresh.setRefreshing(false);
            }
        });
    }

    @Override
    public void onRefresh() {
        page=1;
        findProduct();
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
                convertView = View.inflate(mView.getContext(), R.layout.item_gridview_fm_product, null);
                holder = new ViewHolder();
                holder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
                holder.check_iv = (ImageView) convertView.findViewById(R.id.check_iv);
                holder.textView = (TextView) convertView.findViewById(R.id.name_tv);
                holder.groupbuy_tv = (TextView) convertView.findViewById(R.id.groupbuy_tv);
                holder.old_price_tv = (TextView) convertView.findViewById(R.id.old_price_tv);
                holder.price_tv = (TextView) convertView.findViewById(R.id.price_tv);
                RelativeLayout.LayoutParams lLp = (RelativeLayout.LayoutParams) holder.imageView.getLayoutParams();
                float h = (mScreenWidth - ConvertUtil.dp2px(mView.getActivity(), 45.8f)) / 2;
                lLp.height = (int) h;
                holder.imageView.setLayoutParams(lLp);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            try {
                String name = goodses.getJSONObject(position).getString(Constance.name);
                holder.textView.setText(name);
                //                holder.imageView.setImageResource(R.drawable.bg_default);
                ImageLoader.getInstance().displayImage(goodses.getJSONObject(position).getJSONObject(Constance.default_photo).getString(Constance.large)
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
//                if (!AppUtils.isEmpty(propertieArray)&&propertieArray.length()>0) {
//                    JSONArray attrsArray = propertieArray.getJSONObject(0).getJSONArray(Constance.attrs);
//                    int price = attrsArray.getJSONObject(0).getInt(Constance.attr_price);
//                    double currentPrice = price;
//                    old_Price=currentPrice;
//                    holder.price_tv.setText("￥" + currentPrice);
//                } else {
//                }
                old_Price= Double.parseDouble(goodses.getJSONObject(position).getString(Constance.current_price));
                holder.price_tv.setText("￥" + goodses.getJSONObject(position).getString(Constance.current_price));
                old_Price=old_Price*1.6;
                DecimalFormat df=new DecimalFormat("###.00");
                holder.old_price_tv.setText("￥" + df.format(old_Price));
                holder.old_price_tv.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                holder.check_iv.setVisibility(View.GONE);
//                if (mView.isSelectGoods == true) {
//                    for (int i = 0; i < IssueApplication.mSelectProducts.length(); i++) {
//                        String goodName = IssueApplication.mSelectProducts.getJSONObject(i).getString(Constance.name);
//                        if (name.equals(goodName)) {
//                            holder.check_iv.setVisibility(View.VISIBLE);
//                            break;
//                        }
//
//                    }
//                }
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
