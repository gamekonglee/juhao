package bc.juhao.com.ui.activity.blance;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import bc.juhao.com.R;
import bc.juhao.com.cons.Constance;
import bc.juhao.com.controller.blance.ExtractMoneyController;
import bc.juhao.com.utils.MyShare;
import bocang.utils.IntentUtil;
import bocang.view.BaseActivity;

/**
 * @author: Jun
 * @date : 2017/7/6 16:03
 * @description :提现操作
 */
public class ExtractMoneyActivity extends BaseActivity {
    private ExtractMoneyController mController;
    private TextView add_tv,alipay_tv;
    private Button Withdrawals_bt;

    @Override
    protected void InitDataView() {

    }

    @Override
    protected void initController() {
        mController=new ExtractMoneyController(this);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_extract_money);
        //沉浸式状态栏
//        setColor(this, getResources().getColor(R.color.green));
        add_tv = getViewAndClick(R.id.add_tv);
        Withdrawals_bt = getViewAndClick(R.id.Withdrawals_bt);
        alipay_tv = (TextView)findViewById(R.id.alipay_tv);
    }

    @Override
    protected void onStart() {
        super.onStart();
       String alipay= MyShare.get(ExtractMoneyActivity.this).getString(Constance.ALIPAY);
        alipay_tv.setText(alipay);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onViewClick(View v) {
        switch (v.getId()) {
            case R.id.add_tv:
                IntentUtil.startActivity(this,ExtractAccountActivity.class,false);
                break;
            case R.id.Withdrawals_bt:
                mController.WithdrawalsMoney();
                break;
        }
    }
}
