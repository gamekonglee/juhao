package bc.juhao.com.ui.activity.user;

import android.content.Intent;
import android.view.View;
import android.widget.Button;

import bc.juhao.com.R;
import bc.juhao.com.cons.Constance;
import bc.juhao.com.controller.user.RegiestController;
import bc.juhao.com.ui.view.ShowDialog;
import bc.juhao.com.ui.view.TimerButton;
import bocang.view.BaseActivity;
import cn.smssdk.SMSSDK;

/**
 * @author: Jun
 * @date : 2017/2/7 15:35
 * @description :
 */
public class RegiestActivity extends BaseActivity {
    public RegiestController mController;
    private Button sure_bt;
    public TimerButton find_pwd_btnGetCode;
    public String yaoqing;

    @Override
    protected void InitDataView() {

    }

    @Override
    protected void initController() {
        mController=new RegiestController(this);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_regiest_new);
        sure_bt=getViewAndClick(R.id.sure_bt);
        find_pwd_btnGetCode=getViewAndClick(R.id.find_pwd_btnGetCode);
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        yaoqing=intent.getStringExtra(Constance.yaoqing);
    }

    @Override
    protected void onViewClick(View v) {
        switch (v.getId()){
            case R.id.sure_bt:
                mController.sendRegiest();
            break;
            case R.id.find_pwd_btnGetCode:
                mController.requestYZM();
            break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterAllEventHandler();
    }

    public void goBack(View v){
        ShowDialog mDialog=new ShowDialog();
        mDialog.show(this, "提示", "你是否放弃当前注册?", new ShowDialog.OnBottomClickListener() {
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
