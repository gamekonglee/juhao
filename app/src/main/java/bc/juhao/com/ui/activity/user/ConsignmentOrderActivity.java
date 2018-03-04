package bc.juhao.com.ui.activity.user;

import android.content.Intent;
import android.view.View;
import android.widget.Button;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import bc.juhao.com.R;
import bc.juhao.com.cons.Constance;
import bc.juhao.com.controller.user.ConsignmentOrderController;
import bocang.view.BaseActivity;

/**
 * @author: Jun
 * @date : 2017/8/21 11:18
 * @description :确认发货
 */
public class ConsignmentOrderActivity extends BaseActivity {
    private Button select_log_Btn;
    private ConsignmentOrderController mController;
    private Button consignment_bt;
    public JSONObject mOrderObject;
    private Intent mIntent;
    public Boolean mIsType=false;

    @Override
    protected void InitDataView() {

    }

    @Override
    protected void initController() {
        mController = new ConsignmentOrderController(this);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_confirm_consignment);
        select_log_Btn = getViewAndClick(R.id.select_log_Btn);
        consignment_bt = getViewAndClick(R.id.consignment_bt);
    }

    @Override
    protected void initData() {
        mIntent = getIntent();
         String jsonString = mIntent.getStringExtra(Constance.order);
        mOrderObject = JSON.parseObject(jsonString);
        mIsType =mIntent.getBooleanExtra(Constance.type, false);
    }

    @Override
    protected void onViewClick(View v) {
        switch (v.getId()) {
            case R.id.select_log_Btn:
                mController.selectLog();
                break;
            case R.id.consignment_bt:
                mController.sendConsignmentOrder();
                break;
        }
    }
}
