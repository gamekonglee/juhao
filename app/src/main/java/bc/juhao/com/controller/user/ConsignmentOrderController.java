package bc.juhao.com.controller.user;

import android.content.Intent;
import android.os.Message;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;

import bc.juhao.com.R;
import bc.juhao.com.cons.Constance;
import bc.juhao.com.controller.BaseController;
import bc.juhao.com.listener.INetworkCallBack02;
import bc.juhao.com.ui.activity.user.ConsignmentOrderActivity;
import bc.juhao.com.ui.activity.user.OrderDetailActivity;
import bc.juhao.com.utils.UIUtils;
import bocang.utils.AppDialog;
import bocang.utils.AppUtils;
import bocang.utils.MyToast;

/**
 * @author: Jun
 * @date : 2017/8/21 11:56
 * @description :
 */
public class ConsignmentOrderController extends BaseController implements OnItemClickListener, INetworkCallBack02 {
    private ConsignmentOrderActivity mView;
    private AlertView mLogView;
    private EditText shipping_et,courier_number_et;
    private TextView order_code_tv,order_name_tv;
    private String order_code;

    public ConsignmentOrderController(ConsignmentOrderActivity v){
        mView=v;
        initView();
        initViewData();
    }

    private void initViewData() {
        order_code = mView.mOrderObject.getString(Constance.sn);
        String user_name = mView.mOrderObject.getString(Constance.user_name);
        int orderLevel = mView.mOrderObject.getInteger(Constance.level);
        String levelValue = "";
        if (orderLevel == 0) {
            levelValue = "一级";
        } else if (orderLevel == 1) {
            levelValue = "二级";
        } else if (orderLevel == 2) {
            levelValue = "三级";
        } else {
            levelValue = "消费者";
        }
        order_code_tv.setText(order_code);
        order_name_tv.setText(user_name+"("+levelValue+")");
    }

    private void initView() {
        mLogView = new AlertView(null, null, "取消", null,
                Constance.LOGTYPE,
                mView, AlertView.Style.ActionSheet, this);
        shipping_et = (EditText) mView.findViewById(R.id.shipping_et);
        courier_number_et = (EditText) mView.findViewById(R.id.courier_number_et);
        order_code_tv = (TextView) mView.findViewById(R.id.order_code_tv);
        order_name_tv = (TextView) mView.findViewById(R.id.order_name_tv);
    }

    @Override
    protected void handleMessage(int action, Object[] values) {

    }

    @Override
    protected void myHandleMessage(Message msg) {

    }

    /**
     * 选择货运公司
     */
    public void selectLog() {
        mLogView.show();
    }

    @Override
    public void onItemClick(Object o, int position) {
        if(position>=0){
            shipping_et.setText(Constance.LOGTYPE[position]);
        }

    }

    /**
     * 发货
     */
    public void sendConsignmentOrder() {
        String invoice_no=courier_number_et.getText().toString();
        if (AppUtils.isEmpty(invoice_no)){
            MyToast.show(mView,"快递单号不能为空!");
            return;
        }
        String shipping_name=shipping_et.getText().toString();
        if(AppUtils.isEmpty(shipping_name))
            shipping_name="默认物流";
        mView.setShowDialog(true);
        mView.setShowDialog("提交发货中..");
        mView.showLoading();
        mNetWork.sendConsignmentOrder(invoice_no,order_code,shipping_name,this);
    }

    @Override
    public void onSuccessListener(String requestCode, JSONObject ans) {
        mView.hideLoading();
        AppDialog.messageBox("发货成功!");
        Intent intent = new Intent();
        mView.setResult(Constance.FROMCONSIGNMENT, intent);//告诉原来的Activity 将数据传递给它

        if(mView.mIsType){
            Intent intent1 = new Intent(mView, OrderDetailActivity.class);
            intent1.putExtra(Constance.order, mView.mOrderObject.toJSONString());
            intent1.putExtra(Constance.state, 2);
            mView.startActivity(intent1);
        }
        mView.finish();
    }

    @Override
    public void onFailureListener(String requestCode, JSONObject ans) {
        mView.hideLoading();
        if(AppUtils.isEmpty(ans)){
            AppDialog.messageBox(UIUtils.getString(R.string.server_error));
            return;
        }
        AppDialog.messageBox(ans.getString(Constance.error_desc));
    }
}
