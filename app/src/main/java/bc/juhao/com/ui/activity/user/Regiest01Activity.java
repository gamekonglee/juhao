package bc.juhao.com.ui.activity.user;

import android.view.View;
import android.widget.Button;

import bc.juhao.com.R;
import bc.juhao.com.controller.user.Regiest01Controller;
import bc.juhao.com.ui.view.ShowDialog;
import bocang.utils.PermissionUtils;
import bocang.view.BaseActivity;

/**
 * @author: Jun
 * @date : 2017/2/7 16:03
 * @description :
 */
public class Regiest01Activity extends BaseActivity {
     private Regiest01Controller mController;
    private Button find_pwd_btnConfirm;

    @Override
    protected void InitDataView() {

//        mController.initBaiduLoc();
    }

    @Override
    protected void initController() {
        mController=new Regiest01Controller(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionUtils.requestPermissionsResult(this, requestCode, permissions, grantResults, new PermissionUtils.PermissionGrant() {
            @Override
            public void onPermissionGranted(int requestCode) {
//                mController.initBaiduLoc();
            }
        });
    }


    @Override
    protected void initView() {
        setContentView(R.layout.activity_regiest01_new);
        //沉浸式状态栏
//        setColor(this, getResources().getColor(R.color.colorPrimary));
        find_pwd_btnConfirm=getViewAndClick(R.id.find_pwd_btnConfirm);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onViewClick(View v) {
        switch (v.getId()){
            case R.id.find_pwd_btnConfirm:
                mController.sendInvitationCode();
            break;
        }
    }

//    public LocationClient mLocationClient=null;
//    public LocationClientOption mOption;

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        if(AppUtils.isEmpty(mLocationClient)) return;
//        mLocationClient.stop();
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
