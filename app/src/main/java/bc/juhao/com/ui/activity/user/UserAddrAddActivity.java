package bc.juhao.com.ui.activity.user;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import bc.juhao.com.R;
import bc.juhao.com.cons.Constance;
import bc.juhao.com.controller.user.UserAddrAddController;
import bc.juhao.com.ui.view.ShowDialog;
import bocang.json.JSONObject;
import bocang.view.BaseActivity;

/**
 * @author Jun
 * @time 2017/1/23  23:36
 * @desc ${TODD}
 */
public class UserAddrAddActivity extends BaseActivity {
    private UserAddrAddController mController;
    private Button btnSave;
    private LinearLayout user_addr_llyCity;
    public boolean mUpdateModele=false;
    public JSONObject addressObject;
    private TextView tv_delete;

    @Override
    protected void InitDataView() {

    }

    @Override
    protected void initController() {
        mController = new UserAddrAddController(this);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_user_address_add);
        //沉浸式状态栏
        setColor(this, Color.WHITE);
        btnSave = getViewAndClick(R.id.btnSave);
        user_addr_llyCity = getViewAndClick(R.id.user_addr_llyCity);
        tv_delete = getViewAndClick(R.id.tv_delete);
        tv_delete.setVisibility(mUpdateModele==true?View.VISIBLE:View.GONE);
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        addressObject= (JSONObject) intent.getSerializableExtra(Constance.address);
        mUpdateModele=intent.getBooleanExtra(Constance.UpdateModele,false);
    }

    @Override
    protected void onViewClick(View v) {
        switch (v.getId()) {
            case R.id.btnSave:
                mController.sendAddAddress();
                break;
            case R.id.user_addr_llyCity:
                mController.selectAddress();
                break;
            case R.id.tv_delete:
                mController.deleteAddress();
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==200&&grantResults[0]== PackageManager.PERMISSION_GRANTED){
            mController.onRequestPermissionRsult();
        }else if(requestCode==mController.REQUEST_LOCATION){
            mController.startLocation();
        }

    }

    public void goBack(View v){
        ShowDialog mDialog=new ShowDialog();
        mDialog.show(this, "提示", "你的收货地址还未保存，是否放弃保存?", new ShowDialog.OnBottomClickListener() {
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
