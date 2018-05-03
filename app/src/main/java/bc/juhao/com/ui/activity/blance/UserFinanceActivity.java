package bc.juhao.com.ui.activity.blance;

import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import bc.juhao.com.R;
import bc.juhao.com.controller.blance.UserFinanceController;
import bocang.utils.IntentUtil;
import bocang.view.BaseActivity;

/**
 * @author: Jun
 * @date : 2017/7/6 15:15
 * @description :
 */
public class UserFinanceActivity extends BaseActivity {
    private Button user_fnc_btnWithdraw;
    private TextView topRighttv;
    private UserFinanceController mController;
    private RelativeLayout profit_record_rl;
    private RelativeLayout tixian_rl;

    @Override
    protected void InitDataView() {

    }

    @Override
    protected void onStart() {
        super.onStart();
        mController.initViewData();
    }

    @Override
    protected void initController() {
        mController=new UserFinanceController(this);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_user_finance);
        //沉浸式状态栏
        setColor(this, Color.WHITE);
//        setColor(this, getResources().getColor(R.color.green));
        user_fnc_btnWithdraw = getViewAndClick(R.id.user_fnc_btnWithdraw);
        topRighttv = getViewAndClick(R.id.topRighttv);
        profit_record_rl = getViewAndClick(R.id.profit_record_rl);
        tixian_rl = getViewAndClick(R.id.tixian_rl);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onViewClick(View v) {
        switch (v.getId()) {
            case R.id.user_fnc_btnWithdraw:
                IntentUtil.startActivity(this, ExtractMoneyActivity.class, false);
                break;
            case R.id.topRighttv:
            case R.id.tixian_rl:
                Intent intent=new Intent(this,UserExtractProfitActivity.class);
                intent.putExtra("current",1);
                startActivity(intent);
//                IntentUtil.startActivity(this, ExtractDetailActivity.class, false);
                break;
            case R.id.profit_record_rl:
                Intent intent2=new Intent(this,UserExtractProfitActivity.class);
                intent2.putExtra("current",0);
                startActivity(intent2);
//                IntentUtil.startActivity(this, UserExtractProfitActivity.class, false);
                break;
        }
    }
}
