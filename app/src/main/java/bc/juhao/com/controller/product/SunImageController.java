package bc.juhao.com.controller.product;

import android.content.Intent;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.lib.common.hxp.view.GridViewForScrollView;
import com.lib.common.hxp.view.PullToRefreshLayout;
import com.lib.common.hxp.view.PullableGridView;

import java.util.ArrayList;

import bc.juhao.com.R;
import bc.juhao.com.cons.Constance;
import bc.juhao.com.cons.NetWorkConst;
import bc.juhao.com.controller.BaseController;
import bc.juhao.com.listener.INetworkCallBack;
import bc.juhao.com.ui.activity.DetailPhotoActivity;
import bc.juhao.com.ui.adapter.SunImageAdapter;
import bc.juhao.com.ui.fragment.SunImageFragment;
import bc.juhao.com.utils.DateUtils;
import bc.juhao.com.utils.ImageLoadProxy;
import bocang.json.JSONArray;
import bocang.json.JSONObject;
import bocang.utils.AppUtils;
import bocang.utils.MyToast;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * @author Jun
 * @time 2017/12/10  12:52
 * @desc ${TODD}
 */

public class SunImageController extends BaseController implements INetworkCallBack, PullToRefreshLayout.OnRefreshListener {
    private SunImageFragment mView;
    private PullableGridView gridView1;
    private View mNullView;
    private View mNullNet;
    private JSONArray goodses = new JSONArray();
    private PullToRefreshLayout mPullToRefreshLayout;
    private ProAdapter mProAdapter;
    private Button mRefeshBtn;
    private TextView mNullNetTv;
    private TextView mNullViewTv;
    private ProgressBar pd;
    private int page = 1;

    public SunImageController(SunImageFragment v) {
        mView = v;
        initView();
        initViewData();
    }

    private void initViewData() {
        page = 1;
        reviceProductList();
    }

    private void initView() {
        mPullToRefreshLayout = ((PullToRefreshLayout) mView.getView().findViewById(R.id.refresh_view));
        mPullToRefreshLayout.setOnRefreshListener(this);
        gridView1 = (PullableGridView) mView.getView().findViewById(R.id.gridView);
        mProAdapter = new ProAdapter();
        gridView1.setAdapter(mProAdapter);

        mNullView = mView.getView().findViewById(R.id.null_view);
        mNullNet = mView.getView().findViewById(R.id.null_net);
        mRefeshBtn = (Button) mNullNet.findViewById(R.id.refesh_btn);
        mNullNetTv = (TextView) mNullNet.findViewById(R.id.tv);
        mNullViewTv = (TextView) mNullView.findViewById(R.id.tv);
        pd = (ProgressBar) mView.getView().findViewById(R.id.pd);
    }

    @Override
    protected void handleMessage(int action, Object[] values) {

    }

    @Override
    protected void myHandleMessage(Message msg) {

    }

    private void reviceProductList() {
        mNetWork.reviceProductList(mView.productId, page, this);
    }

    private void sendOrderClick(String id, int state) {
        mNetWork.sendOrderClick(id, state, this);
    }


    private void dismissRefesh() {
        mPullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
        mPullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
    }

    @Override
    public void onSuccessListener(String requestCode, JSONObject ans) {
        mView.hideLoading();
        switch (requestCode) {
            case NetWorkConst.REVICE_PRODUCT_LIST_URL:
                if (null == mView)
                    return;
                if (null != mPullToRefreshLayout) {
                    dismissRefesh();
                }

                JSONArray dataList = ans.getJSONArray(Constance.reviews);
                if (AppUtils.isEmpty(dataList) || dataList.length() == 0) {
                    if (page == 1) {
                        mNullView.setVisibility(View.VISIBLE);
                    } else {
                        MyToast.show(mView.getActivity(), "没有更多数据了!");
                    }

                    dismissRefesh();
                    return;
                }

                mNullView.setVisibility(View.GONE);
                mNullNet.setVisibility(View.GONE);
                getDataSuccess(dataList);
                break;
            case NetWorkConst.ORDER_CLICK_URL:
                initViewData();
                //                AppDialog.messageBox(ans.getString(Constance.error_desc));
                break;
        }
    }

    @Override
    public void onFailureListener(String requestCode, JSONObject ans) {
        mView.hideLoading();
        pd.setVisibility(View.INVISIBLE);
        if (null == mView || mView.getActivity().isFinishing())
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


    private void getDataSuccess(JSONArray array) {
        if (1 == page)
            goodses = array;
        else if (null != goodses) {
            for (int i = 0; i < array.length(); i++) {
                goodses.add(array.getJSONObject(i));
            }

            if (AppUtils.isEmpty(array))
                MyToast.show(mView.getContext(), "没有更多内容了");
        }

        Log.e("520it", "goodses:" + goodses.length());
        mProAdapter.notifyDataSetChanged();
    }

    public void onRefresh() {
        page = 1;
        reviceProductList();
    }

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
        page = 1;
        reviceProductList();
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
        page = page + 1;
        reviceProductList();
    }

    private View.OnClickListener mRefeshBtnListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onRefresh();
        }
    };

    private class ProAdapter extends BaseAdapter {
        public ProAdapter() {
        }

        @Override
        public int getCount() {
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
                convertView = View.inflate(mView.getContext(), R.layout.item_sun_image, null);
                holder = new ViewHolder();
                holder.head_iv = (CircleImageView) convertView.findViewById(R.id.head_iv);
                holder.name_tv = (TextView) convertView.findViewById(R.id.name_tv);
                holder.remark_tv = (TextView) convertView.findViewById(R.id.remark_tv);
                holder.time_tv = (TextView) convertView.findViewById(R.id.time_tv);
                holder.num_tv = (TextView) convertView.findViewById(R.id.num_tv);
                holder.click_iv = (ImageView) convertView.findViewById(R.id.click_iv);
                holder.gridView = (GridViewForScrollView) convertView.findViewById(R.id.gridView);
                holder.click_ll = (LinearLayout) convertView.findViewById(R.id.click_ll);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            final JSONObject object = goodses.getJSONObject(position);
            JSONObject authorObject = object.getJSONObject(Constance.author);
            authorObject.getString(Constance.avatar);
            String avatar = NetWorkConst.SCENE_HOST + authorObject.getString(Constance.avatar);
            if (!AppUtils.isEmpty(avatar))
                ImageLoadProxy.displayHeadIcon(avatar, holder.head_iv);
            holder.name_tv.setText(authorObject.getString(Constance.username));
            holder.time_tv.setText(DateUtils.getStrTime02(object.getString(Constance.updated_at)));
            holder.remark_tv.setText(object.getString(Constance.content));
            holder.num_tv.setText(object.getString(Constance.clike));
            holder.click_ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mView.setShowDialog(true);
                    mView.setShowDialog("正在处理中..");
                    mView.showLoading();
                    String id = object.getString(Constance.id);
                    sendOrderClick(id, 1);
                }
            });

            int state = object.getInt(Constance.state);
            holder.click_iv.setImageResource(state == 1 ? R.drawable.
                    dianzang02 : R.drawable.dianzang01);

            final JSONArray jsonArray = object.getJSONArray(Constance.path);
            final ArrayList<String> mImageResList = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                mImageResList.add(jsonArray.getString(i));
            }
            SunImageAdapter mdapter = new SunImageAdapter(mView.getContext(), mImageResList);
            holder.gridView.setAdapter(mdapter);
            holder.gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(mView.getContext(), DetailPhotoActivity.class);
                    intent.putExtra(Constance.IMAGESHOW, mImageResList);
                    intent.putExtra(Constance.IMAGEPOSITION, 0);
                    mView.startActivity(intent);
                }
            });
            return convertView;
        }

        class ViewHolder {
            CircleImageView head_iv;
            TextView name_tv;
            TextView remark_tv;
            TextView time_tv;
            TextView num_tv;
            ImageView click_iv;
            LinearLayout click_ll;
            GridViewForScrollView gridView;


        }
    }
}
