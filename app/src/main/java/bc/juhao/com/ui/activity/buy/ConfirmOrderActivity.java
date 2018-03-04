package bc.juhao.com.ui.activity.buy;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import bc.juhao.com.R;
import bc.juhao.com.cons.Constance;
import bc.juhao.com.controller.buy.ConfirmOrderController;
import bc.juhao.com.ui.activity.EditValueActivity;
import bc.juhao.com.ui.view.ShowDialog;
import bocang.json.JSONArray;
import bocang.json.JSONObject;
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

    @Override
    protected void InitDataView() {

    }

    @Override
    protected void initController() {
        mController = new ConfirmOrderController(this);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_confirm_order);
        address_rl = getViewAndClick(R.id.address_rl);
        settle_tv = getViewAndClick(R.id.settle_tv);
        money_tv = (TextView) findViewById(R.id.summoney_tv);
        money_tv.setText("￥" + money + "");
        group = (RadioGroup) this.findViewById(R.id.radioGroup);
        group.setOnCheckedChangeListener(this);
        logistic_type_rl = (RelativeLayout) findViewById(R.id.logistic_type_rl);
        logistic_type_rl = getViewAndClick(R.id.logistic_type_rl);
        add_remark_iv = getViewAndClick(R.id.add_remark_iv);
    }


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
    private  Intent mIntent;

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
