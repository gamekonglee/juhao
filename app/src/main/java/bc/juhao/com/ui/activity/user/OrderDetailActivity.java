package bc.juhao.com.ui.activity.user;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import bc.juhao.com.R;
import bc.juhao.com.cons.Constance;
import bc.juhao.com.controller.user.OrderDetailController;
import bocang.utils.AppUtils;
import bocang.view.BaseActivity;

/**
 * @author: Jun
 * @date : 2017/3/20 15:30
 * @description :
 */
public class OrderDetailActivity extends BaseActivity {
    private OrderDetailController mController;
    public JSONObject mOrderObject;
    private Intent mIntent;
    private TextView do_tv, do02_tv, do03_tv, copy_tv, update_money_tv,chat_buy_tv,pdf_tv,consigment_tv;
    public int mStatus=-1;
    public String  mOrderSn="";

    @Override
    protected void InitDataView() {

    }

    @Override
    protected void initController() {
        mController = new OrderDetailController(this);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_order_detail);
        setColor(this, Color.WHITE);
        do_tv = getViewAndClick(R.id.do_tv);
        do02_tv = getViewAndClick(R.id.do02_tv);
        do03_tv = getViewAndClick(R.id.do03_tv);
        copy_tv = getViewAndClick(R.id.copy_tv);
        pdf_tv = getViewAndClick(R.id.pdf_tv);
        consigment_tv = getViewAndClick(R.id.consigment_tv);
        chat_buy_tv = getViewAndClick(R.id.chat_buy_tv);
        update_money_tv = getViewAndClick(R.id.update_money_tv);


    }

    @Override
    protected void initData() {
        mIntent = getIntent();
        mOrderSn=mIntent.getStringExtra(Constance.id);
        if(!AppUtils.isEmpty(mOrderSn)) return;
        String jsonString = mIntent.getStringExtra(Constance.order);
        boolean isBuy=mIntent.getBooleanExtra(Constance.isbuy, false);
        mStatus=mIntent.getIntExtra(Constance.state,-1);
        mOrderObject = JSON.parseObject(jsonString);
    }

    @Override
    protected void onViewClick(View v) {
        switch (v.getId()) {
            case R.id.do_tv:
                mController.doOrder();
                break;
            case R.id.do02_tv:
                mController.do02Order();
                break;
            case R.id.do03_tv:
                mController.do03Order();
                break;
            case R.id.copy_tv:
                mController.getCopyOrder();
                break;
            case R.id.update_money_tv:
                mController.setUpdateMoney();
                break;
            case R.id.chat_buy_tv:
                mController.chatBuy();
                break;

            case R.id.pdf_tv:
                mController.getPDF();
                break;
            case R.id.consigment_tv:
                Intent intent=new Intent(this,ConsignmentOrderActivity.class);
                intent.putExtra(Constance.order, mOrderObject.toJSONString());
                startActivity(intent);
                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mController.ActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mController.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
