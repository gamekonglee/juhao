package bc.juhao.com.ui.activity.buy;

import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import bc.juhao.com.R;
import bc.juhao.com.bean.PayResult;
import bc.juhao.com.cons.Constance;
import bc.juhao.com.controller.buy.ConfirmOrderController;
import bc.juhao.com.ui.activity.EditValueActivity;
import bc.juhao.com.ui.activity.user.OrderDetailActivity;
import bc.juhao.com.ui.view.ShowDialog;
import bocang.json.JSONArray;
import bocang.json.JSONObject;
import bocang.utils.MyToast;
import bocang.view.BaseActivity;

/**
 * @author: Jun
 * @date : 2017/2/24 16:51
 * @description :
 */
public class ConfirmOrderActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener {
    private ConfirmOrderController mController;
    public JSONArray goodsList;
    public RelativeLayout address_rl;
    private TextView money_tv, settle_tv;
    private RadioGroup group;
    private RelativeLayout logistic_type_rl;
    public JSONObject mAddressObject;
    private ImageView add_remark_iv;
    private CheckBox appliay_cb;
    private CheckBox weixin_cb;

    @Override
    protected void InitDataView() {

    }

    @Override
    protected void initController() {
        mController = new ConfirmOrderController(this);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_confirm_order_new);
        setColor(this, Color.WHITE);
        address_rl = getViewAndClick(R.id.address_rl);
        settle_tv = getViewAndClick(R.id.settle_tv);
        money_tv = (TextView) findViewById(R.id.summoney_tv);
        money_tv.setText("￥" + money + "");
        group = (RadioGroup) this.findViewById(R.id.radioGroup);
        group.setOnCheckedChangeListener(this);
        logistic_type_rl = (RelativeLayout) findViewById(R.id.logistic_type_rl);
        logistic_type_rl = getViewAndClick(R.id.logistic_type_rl);
        add_remark_iv = getViewAndClick(R.id.add_remark_iv);
        appliay_cb = (CheckBox) findViewById(R.id.appliay_cb);
        weixin_cb = (CheckBox) findViewById(R.id.weixin_cb);
        appliay_cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    appliay_cb.setChecked(isChecked);
                    weixin_cb.setChecked(false);
                } else {
                    weixin_cb.setChecked(true);
                }
            }
        });
        weixin_cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    weixin_cb.setChecked(isChecked);
                    appliay_cb.setChecked(false);
                } else {
                    appliay_cb.setChecked(true);
                }
            }
        });
        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void resultPay(PayResult result){
        int state=2;
        Intent intent = new Intent(this, OrderDetailActivity.class);
        intent.putExtra(Constance.order, mController.mOrderObject.toJSONString());

        if(result.result.equals("0")){
        MyToast.show(this, "支付成功");
        mController.sendPaySuccess();
        }else if(result.result.equals("-2")){
            state=0;
            MyToast.show(this, "支付失败");
            intent.putExtra(Constance.state, state);
            startActivity(intent);
            ConfirmOrderActivity.this.finish();
        }else {
            state=0;
            intent.putExtra(Constance.state, state);
            startActivity(intent);
            ConfirmOrderActivity.this.finish();
        }


    }
    //在主线程执行
    float money = 0;

    @Override
    protected void initData() {
        Intent intent = getIntent();
        goodsList = (JSONArray) intent.getSerializableExtra(Constance.goods);
        mAddressObject = (JSONObject) intent.getSerializableExtra(Constance.address);
        money = intent.getFloatExtra(Constance.money, 0);

    }
    @Override
    protected void onViewClick(View v) {
        switch (v.getId()) {
            case R.id.address_rl:
                mController.selectAddress();
                break;
            case R.id.settle_tv:
                mController.settleOrder();
                break;
            case R.id.logistic_type_rl:
                mController.selectLogistic();
                break;
            case R.id.add_remark_iv:
                mIntent = new Intent(this, EditValueActivity.class);
                mIntent.putExtra(Constance.TITLE, "订单备注");
                mIntent.putExtra(Constance.CODE, 007);
                startActivityForResult(mIntent, 007);
                break;
        }
    }

    private Intent mIntent;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mController.ActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        mController.selectCheckType(group.getCheckedRadioButtonId());
    }

    public void goBack(View v) {
        ShowDialog mDialog = new ShowDialog();
        mDialog.show(this, "提示", "是否退出当前的确定订单?", new ShowDialog.OnBottomClickListener() {
            @Override
            public void positive() {
                finish();
            }

            @Override
            public void negtive() {

            }
        });
    }



}
