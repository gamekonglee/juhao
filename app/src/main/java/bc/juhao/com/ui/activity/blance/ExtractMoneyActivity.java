package bc.juhao.com.ui.activity.blance;

import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.aliyun.iot.ilop.demo.DemoApplication;

import bc.juhao.com.R;
import bc.juhao.com.cons.Constance;
import bc.juhao.com.controller.blance.ExtractMoneyController;
import bc.juhao.com.ui.activity.IssueApplication;
import bc.juhao.com.ui.activity.MainActivity;
import bc.juhao.com.ui.activity.user.LoginActivity;
import bc.juhao.com.ui.activity.user.TixianGzActivity;
import bc.juhao.com.utils.MyShare;
import bc.juhao.com.utils.UIUtils;
import bocang.utils.IntentUtil;
import bocang.view.BaseActivity;

/**
 * @author: Jun
 * @date : 2017/7/6 16:03
 * @description :提现操作
 */
public class ExtractMoneyActivity extends BaseActivity {
    private ExtractMoneyController mController;
    public TextView add_tv,alipay_tv;
    private Button Withdrawals_bt;
    public TextView tv_get_all;
    private int levelid;

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
        setColor(this, Color.WHITE);
        add_tv = getViewAndClick(R.id.add_tv);
        Withdrawals_bt = getViewAndClick(R.id.Withdrawals_bt);
        alipay_tv = (TextView)findViewById(R.id.alipay_tv);
        tv_get_all = getViewAndClick(R.id.tv_get_all);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(DemoApplication.mUserObject==null){
            MainActivity.mFragmentPosition=0;
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
        levelid = DemoApplication.mUserObject.getInt(Constance.level);
//        if(levelid==0){
//         mController.sendAccountList();
//        }else {
       String alipay= MyShare.get(ExtractMoneyActivity.this).getString(Constance.ALIPAY);
//       if(TextUtils.isEmpty(alipay)){
//           add_tv.setVisibility(View.VISIBLE);
//       }else {
//           add_tv.setVisibility(View.GONE);
//       }
        alipay_tv.setText(alipay);
//        }
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onViewClick(View v) {
        switch (v.getId()) {
            case R.id.add_tv:
//                Log.e("level",levelid+"");
//                if(levelid==0){
//                 startActivity(new Intent(this, TixianGzActivity.class));
//                }else {
                IntentUtil.startActivity(this,ExtractAccountActivity.class,false);
//                }
                break;
            case R.id.Withdrawals_bt:
                mController.WithdrawalsMoney();
                break;
            case R.id.tv_get_all:
                mController.getAllMoney();
                break;
        }
    }
}
