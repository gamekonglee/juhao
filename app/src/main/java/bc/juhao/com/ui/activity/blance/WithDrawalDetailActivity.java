package bc.juhao.com.ui.activity.blance;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import bc.juhao.com.R;
import bc.juhao.com.cons.Constance;
import bocang.view.BaseActivity;

/**
 * @author: Jun
 * @date : 2017/7/29 11:00
 * @description :提现详情
 */
public class WithDrawalDetailActivity extends BaseActivity {
    private String mAlipay="";
    private String mMoney="";
    private TextView alipay_tv,money_tv;
    private Button complete_bt;

    @Override
    protected void InitDataView() {

    }

    @Override
    protected void initController() {

    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_withdrawal_detal);
        alipay_tv = (TextView)findViewById(R.id.alipay_tv);
        money_tv = (TextView)findViewById(R.id.money_tv);
        complete_bt=getViewAndClick(R.id.complete_bt);
        alipay_tv.setText(mAlipay);
        money_tv.setText("￥"+mMoney);
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        mAlipay=intent.getStringExtra(Constance.alipay);
        mMoney=intent.getStringExtra(Constance.money);
    }

    @Override
    protected void onViewClick(View v) {
        switch (v.getId()){
            case R.id.complete_bt:
                finish();
            break;
        }
    }
}
