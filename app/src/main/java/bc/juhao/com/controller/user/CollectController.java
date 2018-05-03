package bc.juhao.com.controller.user;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lib.common.hxp.view.GridViewForScrollView;
import com.lib.common.hxp.view.PullToRefreshLayout;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import bc.juhao.com.R;
import bc.juhao.com.cons.Constance;
import bc.juhao.com.cons.NetWorkConst;
import bc.juhao.com.controller.BaseController;
import bc.juhao.com.listener.INetworkCallBack;
import bc.juhao.com.ui.activity.product.ProDetailActivity;
import bc.juhao.com.ui.activity.user.CollectActivity;
import bocang.json.JSONArray;
import bocang.json.JSONObject;
import bocang.utils.AppUtils;
import bocang.utils.MyToast;

/**
 * @author: Jun
 * @date : 2017/2/13 16:18
 * @description :
 */
public class CollectController extends BaseController implements INetworkCallBack, PullToRefreshLayout.OnRefreshListener {
    private CollectActivity mView;

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
    private Boolean isEdit = false;
    private Boolean isCheck = false;
    private ImageView iv_edit;
    private ArrayList<Boolean> isCheckShowList = new ArrayList<>();
    private ArrayList<Boolean> isCheckList = new ArrayList<>();
    private ProgressBar pd;
    private RelativeLayout rl;
    private JSONArray goodCheckList;
    private Intent mIntent;


    public CollectController(CollectActivity v) {
        mView = v;
        initView();
        initViewData();
    }

    private void initViewData() {
        page = 1;
        sendCollectProduct(page, 20);
        mView.showLoadingPage("", R.drawable.ic_loading);
    }

    /**
     * 获取收藏列表
     *
     * @param page
     * @param per_page
     */
    private void sendCollectProduct(int page, int per_page) {
        mNetWork.sendCollectProduct(page, per_page, this);
    }

    private void initView() {
        mPullToRefreshLayout = ((PullToRefreshLayout) mView.findViewById(R.id.contentView));
        mPullToRefreshLayout.setOnRefreshListener(this);
        order_sv = (GridViewForScrollView) mView.findViewById(R.id.gridView);
        mNullView = mView.findViewById(R.id.null_view);
        mNullNet = mView.findViewById(R.id.null_net);
        mRefeshBtn = (Button) mNullNet.findViewById(R.id.refesh_btn);
        mNullNetTv = (TextView) mNullNet.findViewById(R.id.tv);
        mNullViewTv = (TextView) mNullView.findViewById(R.id.tv);
        iv_edit = (ImageView) mView.findViewById(R.id.iv_edit);
        pd = (ProgressBar) mView.findViewById(R.id.pd);
        rl = (RelativeLayout) mView.findViewById(R.id.rl);

        mProAdapter = new ProAdapter();
        order_sv.setAdapter(mProAdapter);
        order_sv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mIntent = new Intent(mView, ProDetailActivity.class);
                int productId = goodses.getJSONObject(position).getJSONObject(Constance.goods).getInt(Constance.id);
                mIntent.putExtra(Constance.product, productId);
                mView.startActivity(mIntent);
            }
        });
    }


    @Override
    protected void handleMessage(int action, Object[] values) {

    }

    @Override
    protected void myHandleMessage(Message msg) {

    }

    public void setEdit() {
        if (AppUtils.isEmpty(goodses)) {
            MyToast.show(mView, "还没有数据!");
            return;
        }
        if (isEdit) {
            isEdit = false;
            iv_edit.setImageResource(R.drawable.edit);
            mProAdapter.getCheck(false, false);
            rl.setVisibility(View.GONE);
        } else {
            iv_edit.setImageResource(R.drawable.ic_ok);

            mProAdapter.getCheck(true, false);
            rl.setVisibility(View.VISIBLE);
            isEdit = true;
        }
    }

    @Override
    public void onSuccessListener(String requestCode, JSONObject ans) {
        mView.showContentView();
        pd.setVisibility(View.INVISIBLE);
        switch (requestCode) {
            case NetWorkConst.LIKEDPRODUCT:
                mView.hideLoading();
                if (null == mView || mView.isFinishing())
                    return;
                if (null != mPullToRefreshLayout) {
                    dismissRefesh();
                }

                JSONArray goodsList = ans.getJSONArray(Constance.goodsList);
                if (AppUtils.isEmpty(goodsList)) {
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
            case NetWorkConst.ULIKEDPRODUCT:
                if (isLastDelete = true) {
                    page = 1;
                    sendCollectProduct(page, 20);
                }
                break;
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
            dismissRefesh();
        }
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
        //        mProAdapter.notifyDataSetChanged();
        mProAdapter.getCheck(false, true);
        mProAdapter.getIsCheck(false, true);
    }

    private View.OnClickListener mRefeshBtnListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onRefresh();
        }
    };

    public void onRefresh() {
        page = 1;
        sendCollectProduct(page, 20);
    }

    private void dismissRefesh() {
        mPullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
        mPullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
    }

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
        page = 1;
        sendCollectProduct(page, 20);
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
        sendCollectProduct(page++, 20);
    }

    public void setCheckAll() {
        if (isCheck) {
            isCheck = false;
            mProAdapter.getIsCheck(false, false);
        } else {
            isCheck = true;
            mProAdapter.getIsCheck(true, false);
        }
    }

    private boolean isLastDelete = false;

    /**
     * 删除收藏
     */
    public void senDeleteCollect() {
        mView.setShowDialog(true);
        mView.setShowDialog("正在删除收藏中!");
        mView.showLoading();
        isLastDelete = false;
        mProAdapter.getGoodCheck();
        for (int i = 0; i < goodCheckList.length(); i++) {
            String id = goodCheckList.getJSONObject(i).getJSONObject(Constance.goods).getString(Constance.id);
            mNetWork.sendUnLikeCollect(id, this);
            if (i == goodCheckList.length() - 1) {
                isLastDelete = true;
            }
        }

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

        private void getCheck(boolean isCheck, boolean isStart) {
            if (AppUtils.isEmpty(goodses))
                return;
            if (isStart) {
                for (int i = 0; i < goodses.length(); i++) {
                    isCheckShowList.add(isCheck);
                }
            } else {
                for (int i = 0; i < goodses.length(); i++) {
                    isCheckShowList.set(i, isCheck);
                }
            }
            notifyDataSetChanged();
        }

        private void getIsCheck(boolean isCheck, boolean isStart) {
            if (isStart) {
                isCheckList = new ArrayList<>();
                for (int i = 0; i < goodses.length(); i++) {
                    isCheckList.add(isCheck);
                }
            } else {
                for (int i = 0; i < goodses.length(); i++) {
                    isCheckList.set(i, isCheck);
                }
            }
            notifyDataSetChanged();
        }


        private void getGoodCheck() {
            goodCheckList = new JSONArray();
            for (int i = 0; i < isCheckList.size(); i++) {
                if (isCheckList.get(i)) {
                    goodCheckList.add(goodses.get(i));
                }
            }
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(mView, R.layout.item_gv_collect, null);

                holder = new ViewHolder();
                holder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
                holder.priceTv = (TextView) convertView.findViewById(R.id.priceTv);
                holder.name_tv = (TextView) convertView.findViewById(R.id.name_tv);
                holder.checkbox = (CheckBox) convertView.findViewById(R.id.checkbox);
                holder.price_old=convertView.findViewById(R.id.price_old_Tv);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            JSONObject goodObject = goodses.getJSONObject(position).getJSONObject(Constance.goods);
            if(goodObject!=null) {
                holder.name_tv.setText("型号:" + goodObject.getString(Constance.name));

                try {
                    String path = goodObject.getJSONObject(Constance.default_photo).getString(Constance.thumb);
                    ImageLoader.getInstance().displayImage(path, holder.imageView);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                holder.checkbox.setVisibility(isCheckShowList.get(position) == true ? View.VISIBLE : View.INVISIBLE);
                holder.checkbox.setChecked(isCheckList.get(position));
                JSONArray propertieArray = goodses.getJSONObject(position).getJSONArray(Constance.properties);
                int current = 0;
                for (int i = 0; i < propertieArray.length(); i++) {
                    if (propertieArray.getJSONObject(i).getString(Constance.name).equals("规格")) {
                        current = i;
                        break;
                    }
                }
                if (!AppUtils.isEmpty(propertieArray) && propertieArray.length() > 0) {
                    JSONArray attrsArray = propertieArray.getJSONObject(current).getJSONArray(Constance.attrs);
                    int price = attrsArray.getJSONObject(0).getInt(Constance.attr_price);
                    double currentPrice = Double.parseDouble(goodObject.getString(Constance.current_price)) + price;
                    holder.priceTv.setText("￥" + currentPrice);

                } else {
                    holder.priceTv.setText("￥" + goodObject.getString(Constance.current_price));
                }
                holder.price_old.setText("￥" + goodObject.getString(Constance.price));
                holder.price_old.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                holder.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        isCheckList.set(position, isChecked);
                    }
                });
            }
            return convertView;
        }

        class ViewHolder {
            ImageView imageView;
            TextView name_tv;
            TextView priceTv;
            CheckBox checkbox;
            TextView price_old;

        }
    }
}
