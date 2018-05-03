package bc.juhao.com.ui.activity.user;

import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.Button;

import bc.juhao.com.R;
import bc.juhao.com.cons.Constance;
import bc.juhao.com.controller.user.UserAddrController;
import bocang.utils.IntentUtil;
import bocang.view.BaseActivity;

/**
 * @author Jun
 * @time 2017/1/23  23:14
 * @desc ${TODD}收货地址
 */
public class UserAddrActivity  extends BaseActivity{
    private Button btn_save;
    private UserAddrController mController;
    public Boolean isSelectAddress=false;

    @Override
    protected void InitDataView() {

    }

    @Override
    protected void initController() {
        mController=new UserAddrController(this);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_user_address);
        //沉浸式状态栏
        setColor(this, Color.WHITE);
        btn_save=getViewAndClick(R.id.btn_save);

    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        isSelectAddress=intent.getBooleanExtra(Constance.isSELECTADDRESS,false);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mController.sendAddressList();
    }
    public void onResfresh(){
        mController.sendAddressList();
    }

    @Override
    protected void onViewClick(View v) {
        switch (v.getId()){
            case R.id.btn_save:
                IntentUtil.startActivity(this,UserAddrAddActivity.class,false);
            break;
        }
    }
}
