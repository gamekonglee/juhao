package bc.juhao.com.ui.view.popwindow;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import bc.juhao.com.R;
import bc.juhao.com.cons.Constance;
import bc.juhao.com.cons.NetWorkConst;
import bc.juhao.com.listener.ILogisticsChooseListener;
import bc.juhao.com.listener.INetworkCallBack;
import bc.juhao.com.ui.activity.user.UserLogActivity;
import bc.juhao.com.utils.UIUtils;
import bocang.json.JSONArray;
import bocang.json.JSONObject;
import bocang.utils.AppDialog;
import bocang.utils.AppUtils;


/**
 * @author: Jun
 * @date : 2017/2/16 15:12
 * @description :
 */
public class SelectLogisticsPopWindow extends BasePopwindown implements View.OnClickListener, INetworkCallBack {
    private ImageView close_iv;
    private LinearLayout bg_ll;

    private Button btn_logistic;
    private ListView properties_lv;
    private JSONArray mLogisticList;
    private ProAdapter mAdapter;
    private Activity mActivity;
    private ProgressBar pd;

    private ILogisticsChooseListener mListener;

    public void setListener(ILogisticsChooseListener listener) {
        mListener = listener;
    }

    public SelectLogisticsPopWindow(Context context,Activity activity) {
        super(context);
        mActivity=activity;
        initViewData();


    }

    @Override
    protected void initView(Context context) {
        View contentView = View.inflate(mContext, R.layout.pop_select_logistic, null);
        initUI(contentView);

    }

    private void initViewData() {
        sendlogistics();
    }

    private void initUI(View contentView) {
        close_iv = (ImageView) contentView.findViewById(R.id.close_iv);
        bg_ll = (LinearLayout) contentView.findViewById(R.id.bg_ll);
        close_iv.setOnClickListener(this);
        bg_ll.setOnClickListener(this);
        btn_logistic = (Button) contentView.findViewById(R.id.btn_logistic);
        btn_logistic.setOnClickListener(this);
        pd = (ProgressBar) contentView.findViewById(R.id.pd);
        pd.setVisibility(View.VISIBLE);
        properties_lv = (ListView) contentView.findViewById(R.id.properties_lv);
        mAdapter=new ProAdapter();
        properties_lv.setAdapter(mAdapter);
        properties_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

        mPopupWindow = new PopupWindow(contentView, -1, -1);
        // 1.让mPopupWindow内部的控件获取焦点
        mPopupWindow.setFocusable(true);
        // 2.mPopupWindow内部获取焦点后 外部的所有控件就失去了焦点
        mPopupWindow.setOutsideTouchable(true);
        //只有加载背景图还有效果
        // 3.如果不马上显示PopupWindow 一般建议刷新界面
        mPopupWindow.update();
        // 设置弹出窗体显示时的动画，从底部向上弹出
        mPopupWindow.setAnimationStyle(R.style.AnimBottom);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.close_iv:
                onDismiss();
                break;
            case R.id.bg_ll:
                onDismiss();
                break;
            case R.id.btn_logistic:
                Intent intent=new Intent(mContext, UserLogActivity.class);
                intent.putExtra(Constance.isSelectLogistice, true);
                mActivity.startActivityForResult(intent, Constance.FROMLOG);
                onDismiss();
                break;
        }
    }

    public void sendlogistics() {
        mNetWork.sendlogistics(this);
    }


    @Override
    public void onSuccessListener(String requestCode, JSONObject ans) {
        pd.setVisibility(View.INVISIBLE);
        switch (requestCode) {
            case NetWorkConst.LOGISTICS:
                mLogisticList=ans.getJSONArray(Constance.logistics);
                mAdapter.notifyDataSetChanged();
                break;
        }
    }

    @Override
    public void onFailureListener(String requestCode, JSONObject ans) {
        pd.setVisibility(View.INVISIBLE);
        if (AppUtils.isEmpty(ans)) {
            AppDialog.messageBox(UIUtils.getString(R.string.server_error));
            return;
        }
        AppDialog.messageBox(ans.getString(Constance.error_desc));
    }

    private class ProAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            if (null == mLogisticList)
                return 0;
            return mLogisticList.length();
        }


        @Override
        public JSONObject getItem(int position) {
            if (null == mLogisticList)
                return null;
            return mLogisticList.getJSONObject(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(mContext, R.layout.item_user_logistic, null);
                holder = new ViewHolder();
                holder.consignee_tv = (TextView) convertView.findViewById(R.id.consignee_tv);
                holder.address_tv = (TextView) convertView.findViewById(R.id.address_tv);
                holder.phone_tv = (TextView) convertView.findViewById(R.id.phone_tv);
                holder.default_addr_tv = (TextView) convertView.findViewById(R.id.default_addr_tv);
                holder.delete_bt = (Button) convertView.findViewById(R.id.delete_bt);
                holder.edit_tv = (Button) convertView.findViewById(R.id.edit_tv);
                holder.ll = (LinearLayout) convertView.findViewById(R.id.ll);
                holder.logistics_name_rl = (RelativeLayout) convertView.findViewById(R.id.logistics_name_rl);
                holder.edit_rl = (RelativeLayout) convertView.findViewById(R.id.edit_rl);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            final JSONObject jsonObject = mLogisticList.getJSONObject(position);
            holder.consignee_tv.setText(jsonObject.getString(Constance.name));
            holder.address_tv.setText(jsonObject.getString(Constance.address));
            holder.phone_tv.setText(jsonObject.getString(Constance.tel));
            holder.default_addr_tv.setVisibility(View.GONE);
            holder.delete_bt.setVisibility(View.GONE);
            holder.edit_rl.setVisibility(View.GONE);
            holder.logistics_name_rl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onLogisticsChanged(jsonObject);
                    onDismiss();
                }
            });
            return convertView;
        }

        class ViewHolder {
            TextView consignee_tv;
            TextView address_tv;
            TextView phone_tv;
            TextView default_addr_tv;
            Button delete_bt, edit_tv;
            LinearLayout ll;
            RelativeLayout logistics_name_rl;
            RelativeLayout edit_rl;
        }
    }

}
