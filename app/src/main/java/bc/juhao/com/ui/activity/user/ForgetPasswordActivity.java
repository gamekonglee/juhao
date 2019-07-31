package bc.juhao.com.ui.activity.user;

import android.view.View;
import android.widget.Button;

import bc.juhao.com.R;
import bc.juhao.com.common.BaseActivity;
import bc.juhao.com.controller.ForgetPasswordController;
import bc.juhao.com.controller.user.UpdatePasswordController;
import bc.juhao.com.ui.view.ShowDialog;
import bc.juhao.com.ui.view.TimerButton;
import cn.smssdk.SMSSDK;

/**
 * Created by gamekonglee on 2018/9/1.
 */

public class ForgetPasswordActivity extends BaseActivity implements View.OnClickListener {

    public TimerButton find_pwd_btnGetCode;
    private Button find_pwd_btnConfirm;
    private ForgetPasswordController mController;

    @Override
    protected void InitDataView() {

    }

    @Override
    protected void initController() {
        mController = new ForgetPasswordController(this);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_forget_password_new);

//        setColor(this, getResources().getColor(R.color.colorPrimary));
        find_pwd_btnGetCode = (TimerButton) findViewById(R.id.find_pwd_btnGetCode);
        find_pwd_btnConfirm = (Button) findViewById(R.id.find_pwd_btnConfirm);
        find_pwd_btnGetCode.setOnClickListener(this);
        find_pwd_btnConfirm.setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.find_pwd_btnGetCode:
                mController.requestYZM();
                break;
            case R.id.find_pwd_btnConfirm:
                mController.sendUpdatePwd();
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
        mDialog.show(this, "提示", "你是否放弃修改密码?", new ShowDialog.OnBottomClickListener() {
            @Override
            public void positive() {
                finish();
            }

            @Override
            public void negtive() {

            }
        });
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
