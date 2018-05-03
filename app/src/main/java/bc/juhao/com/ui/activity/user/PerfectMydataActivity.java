package bc.juhao.com.ui.activity.user;

import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import bc.juhao.com.R;
import bc.juhao.com.controller.user.PerfectMydataController;
import bc.juhao.com.ui.view.ShowDialog;
import bocang.utils.IntentUtil;
import bocang.utils.PermissionUtils;
import bocang.view.BaseActivity;

/**
 * @author: Jun
 * @date : 2017/1/21 15:40
 * @description :完善个人资料
 */
public class PerfectMydataActivity extends BaseActivity implements View.OnClickListener {
    private RelativeLayout head_rl, name_rl, sex_rl, birthday_rl, phone_rl, telephone_rl, email_rl, update_password_rl,save_rl;
    private PerfectMydataController mController;
    private ImageView topLeftBtn;
    @Override
    protected void InitDataView() {

    }

    @Override
    protected void initController() {
        mController=new PerfectMydataController(this);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_perfect_mydata);
        setColor(this, Color.WHITE);
        head_rl = (RelativeLayout) findViewById(R.id.head_rl);
        name_rl = (RelativeLayout) findViewById(R.id.name_rl);
        sex_rl = (RelativeLayout) findViewById(R.id.sex_rl);
        birthday_rl = (RelativeLayout) findViewById(R.id.birthday_rl);
        phone_rl = (RelativeLayout) findViewById(R.id.phone_rl);
        telephone_rl = (RelativeLayout) findViewById(R.id.telephone_rl);
        email_rl = (RelativeLayout) findViewById(R.id.email_rl);
        update_password_rl = (RelativeLayout) findViewById(R.id.update_password_rl);
        save_rl = (RelativeLayout) findViewById(R.id.save_rl);
        ImageView head_iv=findViewById(R.id.head_iv);
        head_iv.setOnClickListener(this);
        head_rl.setOnClickListener(this);
        name_rl.setOnClickListener(this);
        birthday_rl.setOnClickListener(this);
        phone_rl.setOnClickListener(this);
        telephone_rl.setOnClickListener(this);
        email_rl.setOnClickListener(this);
        update_password_rl.setOnClickListener(this);
        sex_rl.setOnClickListener(this);
        save_rl.setOnClickListener(this);
//        topLeftBtn.setOnClickListener(this);

    }


    @Override
    protected void initData() {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_rl:
            case R.id.head_iv:
                mController.setHead();
                break;
            case R.id.name_rl:
                mController.setIntent("用户名", 001);
                break;
            case R.id.sex_rl:
                mController.selectSex();
                break;
            case R.id.birthday_rl:
                mController.selectBirthday();
                break;
            case R.id.phone_rl:
                mController.setIntent("手机号", 004);
                break;
            case R.id.telephone_rl:
                mController.setIntent("固定电话", 005);
                break;
            case R.id.email_rl:
                mController.setIntent("电子邮箱", 006);
                break;
            case R.id.update_password_rl:
                IntentUtil.startActivity(this, UpdatePasswordActivity.class, false);
                break;
            case R.id.save_rl:
                mController.sendUpdateUser();
                break;
        }
    }

    public void goBack(View v){
        ShowDialog mDialog=new ShowDialog();
        mDialog.show(this, "提示", "你是否放弃个人信息编辑?", new ShowDialog.OnBottomClickListener() {
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
    protected void onViewClick(View v) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mController.ActivityResult(requestCode,resultCode,data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionUtils.requestPermissionsResult(this, requestCode, permissions, grantResults, new PermissionUtils.PermissionGrant() {
            @Override
            public void onPermissionGranted(int requestCode) {
                mController.setHead();
            }
        });
    }

}
