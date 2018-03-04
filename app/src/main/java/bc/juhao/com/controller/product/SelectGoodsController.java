package bc.juhao.com.controller.product;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lib.common.hxp.view.GridViewForScrollView;
import com.lib.common.hxp.view.PullToRefreshLayout;
import com.nostra13.universalimageloader.core.ImageLoader;

import bc.juhao.com.R;
import bc.juhao.com.cons.Constance;
import bc.juhao.com.cons.NetWorkConst;
import bc.juhao.com.controller.BaseController;
import bc.juhao.com.listener.INetworkCallBack;
import bc.juhao.com.ui.activity.IssueApplication;
import bc.juhao.com.ui.activity.product.ClassifyGoodsActivity;
import bc.juhao.com.ui.activity.product.ProDetailActivity;
import bc.juhao.com.ui.activity.product.SelectGoodsActivity;
import bc.juhao.com.utils.ConvertUtil;
import bc.juhao.com.utils.MyShare;
import bocang.json.JSONArray;
import bocang.json.JSONObject;
import bocang.utils.AppUtils;
import bocang.utils.MyToast;

import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * @author: Jun
 * @date : 2017/2/16 17:30
 * @description :产品列表
 */
public class SelectGoodsController extends BaseController implements INetworkCallBack, PullToRefreshLayout.OnRefreshListener, AdapterView.OnItemClickListener {
    private SelectGoodsActivity mView;
    private EditText et_search;
    private JSONArray goodses;
    private PullToRefreshLayout mPullToRefreshLayout;
    private ProAdapter mProAdapter;
    private GridViewForScrollView order_sv;
    private int page = 1;
    private View mNullView;
    private View mNullNet;
    private Button mRefeshBtn;
    private TextView mNullNetTv;
    private TextView mNullViewTv;
    private String per_pag = "20";
    public int mScreenWidth;
    public String mSortKey;
    public String mSortValue;
    private TextView popularityTv, priceTv, newTv, saleTv;
    private ImageView price_iv;
    private Intent mIntent;
    private ProgressBar pd;


    public SelectGoodsController(SelectGoodsActivity v) {
        mView = v;
        initView();
        initViewData();
    }

    private void initViewData() {
        if (!AppUtils.isEmpty(mView.mSort)) {
            if (mView.mSort == 4) {
                selectSortType(R.id.saleTv);
            } else if (mView.mSort == 5) {
                selectSortType(R.id.newTv);
            } else if (mView.mSort == 2) {
                selectSortType(R.id.popularityTv);
            }
        }
        if (mView.mIsYiJI) {
            selectYijiProduct(1, per_pag, null, null, null);
        } else {
            selectProduct(1, per_pag, null, null, null);
        }

    }

    private void initView() {
        et_search = (EditText) mView.findViewById(R.id.et_search);
        mPullToRefreshLayout = ((PullToRefreshLayout) mView.findViewById(R.id.refresh_view));
        mPullToRefreshLayout.setOnRefreshListener(this);
        order_sv = (GridViewForScrollView) mView.findViewById(R.id.priductGridView);
        mProAdapter = new ProAdapter();
        order_sv.setAdapter(mProAdapter);
        order_sv.setOnItemClickListener(this);

        mNullView = mView.findViewById(R.id.null_view);
        mNullNet = mView.findViewById(R.id.null_net);
        mRefeshBtn = (Button) mNullNet.findViewById(R.id.refesh_btn);
        mNullNetTv = (TextView) mNullNet.findViewById(R.id.tv);
        mNullViewTv = (TextView) mNullView.findViewById(R.id.tv);
        mScreenWidth = mView.getResources().getDisplayMetrics().widthPixels;
        popularityTv = (TextView) mView.findViewById(R.id.popularityTv);
        priceTv = (TextView) mView.findViewById(R.id.priceTv);
        newTv = (TextView) mView.findViewById(R.id.newTv);
        saleTv = (TextView) mView.findViewById(R.id.saleTv);
        price_iv = (ImageView) mView.findViewById(R.id.price_iv);
        pd = (ProgressBar) mView.findViewById(R.id.pd);
        et_search.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // TODO Auto-generated method stub
                // 修改回车键功能
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    // 先隐藏键盘
                    ((InputMethodManager) mView.getSystemService(INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(mView
                                            .getCurrentFocus().getWindowToken(),
                                    InputMethodManager.HIDE_NOT_ALWAYS);
                }
                if (mView.mIsYiJI) {
                    selectYijiProduct(1, per_pag, null, null, null);
                } else {
                    selectProduct(1, per_pag, null, null, null);
                }
                return false;
            }
        });
    }

    @Override
    protected void handleMessage(int action, Object[] values) {

    }

    @Override
    protected void myHandleMessage(Message msg) {

    }


    public void selectSortType(int type) {
        popularityTv.setTextColor(mView.getResources().getColor(R.color.fontColor60));
        priceTv.setTextColor(mView.getResources().getColor(R.color.fontColor60));
        newTv.setTextColor(mView.getResources().getColor(R.color.fontColor60));
        saleTv.setTextColor(mView.getResources().getColor(R.color.fontColor60));
        price_iv.setImageResource(R.drawable.arror);

        switch (type) {
            case R.id.popularityTv:
                popularityTv.setTextColor(mView.getResources().getColor(R.color.colorPrimaryRed));
                mSortKey = "2";//人气
                mSortValue = "1";//排序
                break;
            case R.id.stylell:
                priceTv.setTextColor(mView.getResources().getColor(R.color.colorPrimaryRed));
                price_iv.setImageResource(R.drawable.arror_top);
                mSortKey = "1";//价格
                mSortValue = "2";//排序
                break;
            case 2:
                priceTv.setTextColor(mView.getResources().getColor(R.color.colorPrimaryRed));
                price_iv.setImageResource(R.drawable.arror_button);
                mSortKey = "1";//价格
                mSortValue = "1";//排序
                break;
            case R.id.newTv:
                newTv.setTextColor(mView.getResources().getColor(R.color.colorPrimaryRed));
                mSortKey = "5";//新品
                mSortValue = "1";//排序
                break;
            case R.id.saleTv:
                saleTv.setTextColor(mView.getResources().getColor(R.color.colorPrimaryRed));
                mSortKey = "4";//销售
                mSortValue = "1";//排序
                break;
        }
        page = 1;
        if (mView.mIsYiJI) {
            selectYijiProduct(1, per_pag, null, null, null);
        } else {
            selectProduct(1, per_pag, null, null, null);
        }

    }


    public void selectProduct(int page, String per_page, String brand, String category, String shop) {
        String keyword = et_search.getText().toString();
        //        mView.setShowDialog(true);
        //        mView.setShowDialog("正在搜索中!");
        //        mView.showLoading();
        pd.setVisibility(View.VISIBLE);
        mNetWork.sendGoodsList(page, per_page, brand, mView.mCategoriesId, mView.mFilterAttr, shop, keyword, mSortKey, mSortValue, this);
    }

    public void selectYijiProduct(int page, String per_page, String brand, String category, String shop) {
        String keyword = et_search.getText().toString();
        //        mView.setShowDialog(true);
        //        mView.setShowDialog("正在搜索中!");
        //        mView.showLoading();
        pd.setVisibility(View.VISIBLE);
        String invite_code = MyShare.get(mView).getString(Constance.invite_code);
        mNetWork.selectYijiProduct(page, per_page, brand, mView.mCategoriesId, mView.mFilterAttr, shop, keyword, mSortKey, mSortValue, invite_code, this);
    }

    @Override
    public void onSuccessListener(String requestCode, JSONObject ans) {
        mView.hideLoading();
        pd.setVisibility(View.INVISIBLE);
        switch (requestCode) {
            case NetWorkConst.PRODUCT:
                if (null == mView || mView.isFinishing())
                    return;
                if (null != mPullToRefreshLayout) {
                    dismissRefesh();
                }

                JSONArray goodsList = ans.getJSONArray(Constance.goodsList);
                if (AppUtils.isEmpty(goodsList) || goodsList.length() == 0) {
                    if (page == 1) {
                        mNullView.setVisibility(View.VISIBLE);
                    } else {
                        MyToast.show(mView, "没有更多数据了!");
                    }

                    dismissRefesh();
                    return;
                }

                mNullView.setVisibility(View.GONE);
                mNullNet.setVisibility(View.GONE);
                getDataSuccess(goodsList);
                break;
            case NetWorkConst.PRODUCTYIJI:
                if (null == mView || mView.isFinishing())
                    return;
                if (null != mPullToRefreshLayout) {
                    dismissRefesh();
                }

                JSONArray goodsList1 = ans.getJSONArray(Constance.goodsList);
                if (AppUtils.isEmpty(goodsList1) || goodsList1.length() == 0) {
                    if (page == 1) {
                        mNullView.setVisibility(View.VISIBLE);
                    } else {
                        MyToast.show(mView, "没有更多数据了!");
                    }

                    dismissRefesh();
                    return;
                }

                mNullView.setVisibility(View.GONE);
                mNullNet.setVisibility(View.GONE);
                getDataSuccess(goodsList1);
                return;
        }
    }

    @Override
    public void onFailureListener(String requestCode, JSONObject ans) {
        mView.hideLoading();
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

    private View.OnClickListener mRefeshBtnListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onRefresh();
        }
    };

    public void onRefresh() {
        page = 1;
        if (mView.mIsYiJI) {
            selectYijiProduct(page, per_pag, null, null, null);
        } else {
            selectProduct(page, per_pag, null, null, null);
        }

    }

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
        page = 1;
        if (mView.mIsYiJI) {
            selectYijiProduct(page, per_pag, null, null, null);
        } else {
            selectProduct(page, per_pag, null, null, null);
        }

    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
        page = page + 1;
        if (mView.mIsYiJI) {
            selectYijiProduct(page, per_pag, null, null, null);
        } else {
            selectProduct(page, per_pag, null, null, null);
        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (mView.isSelectGoods == true) {
            for (int i = 0; i < IssueApplication.mSelectProducts.length(); i++) {
                String selectName = IssueApplication.mSelectProducts.getJSONObject(i).getString(Constance.name);
                String name = goodses.getJSONObject(position).getString(Constance.name);
                if (selectName.equals(name)) {
                    IssueApplication.mSelectProducts.delete(i);
                    mProAdapter.notifyDataSetChanged();
                    mView.select_num_tv.setText(IssueApplication.mSelectProducts.length() + "");
                    return;
                }
            }
            IssueApplication.mSelectProducts.add(goodses.getJSONObject(position));
            mProAdapter.notifyDataSetChanged();
            mView.select_num_tv.setText(IssueApplication.mSelectProducts.length() + "");


        } else {
            mIntent = new Intent(mView, ProDetailActivity.class);
            int productId = goodses.getJSONObject(position).getInt(Constance.id);
            mIntent.putExtra(Constance.product, productId);
//            mIntent.putExtra(Constance.is_xiangou,false);
            mView.startActivity(mIntent);
        }

    }

    /**
     * 筛选
     */
    public void openClassify() {
        //        onRefresh();
        //        IssueApplication.isClassify=true;
        //        IntentUtil.startActivity(mView, ClassifyGoodsActivity.class,false);
        mIntent = new Intent(mView, ClassifyGoodsActivity.class);
        mView.startActivityForResult(mIntent, 103);
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
                convertView = View.inflate(mView, R.layout.item_gridview_fm_product, null);
                holder = new ViewHolder();
                holder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
                holder.check_iv = (ImageView) convertView.findViewById(R.id.check_iv);
                holder.textView = (TextView) convertView.findViewById(R.id.name_tv);
                holder.groupbuy_tv = (TextView) convertView.findViewById(R.id.groupbuy_tv);
                holder.old_price_tv = (TextView) convertView.findViewById(R.id.old_price_tv);
                holder.price_tv = (TextView) convertView.findViewById(R.id.price_tv);
                RelativeLayout.LayoutParams lLp = (RelativeLayout.LayoutParams) holder.imageView.getLayoutParams();
                float h = (mScreenWidth - ConvertUtil.dp2px(mView, 45.8f)) / 2;
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
                JSONArray propertieArray = goodses.getJSONObject(position).getJSONArray(Constance.properties);
                if (!AppUtils.isEmpty(propertieArray)) {
                    JSONArray attrsArray = propertieArray.getJSONObject(0).getJSONArray(Constance.attrs);
                    int price = attrsArray.getJSONObject(0).getInt(Constance.attr_price);
                    double oldPrice = Double.parseDouble(goodses.getJSONObject(position).getString(Constance.price)) + price;
                    double currentPrice = Double.parseDouble(goodses.getJSONObject(position).getString(Constance.current_price)) + price;
                    holder.old_price_tv.setText("￥" + oldPrice);
                    holder.old_price_tv.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                    holder.price_tv.setText("￥" + currentPrice);
                } else {
                    holder.old_price_tv.setText("￥" + goodses.getJSONObject(position).getString(Constance.price));
                    holder.old_price_tv.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                    holder.price_tv.setText("￥" + goodses.getJSONObject(position).getString(Constance.current_price));
                }
                holder.check_iv.setVisibility(View.GONE);
                if (mView.isSelectGoods == true) {
                    for (int i = 0; i < IssueApplication.mSelectProducts.length(); i++) {
                        String goodName = IssueApplication.mSelectProducts.getJSONObject(i).getString(Constance.name);
                        if (name.equals(goodName)) {
                            holder.check_iv.setVisibility(View.VISIBLE);
                            break;
                        }

                    }
                }
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
