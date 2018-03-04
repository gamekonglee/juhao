package bc.juhao.com.ui.activity.user;

import android.content.Intent;
import android.view.View;

import bc.juhao.com.R;
import bc.juhao.com.cons.Constance;
import bc.juhao.com.controller.user.UserDataController;
import bocang.view.BaseActivity;

/**
 * @author: Jun
 * @date : 2017/7/5 14:10
 * @description :
 */
public class UserDataActivity extends BaseActivity {

    private UserDataController mController;
    public String mUserId;

    @Override
    protected void InitDataView() {

    }

    @Override
    protected void initController() {
        mController=new UserDataController(this);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_user_data);
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        mUserId=intent.getStringExtra(Constance.user_id);


    }

    @Override
    protected void onViewClick(View v) {

    }
}
