package bc.juhao.com.ui.activity.user;

import android.view.View;

import bc.juhao.com.R;
import bc.juhao.com.controller.user.MyDistributorController;
import bocang.view.BaseActivity;

/**
 * @author: Jun
 * @date : 2017/9/19 9:11
 * @description :我的分销商
 */
public class MyDistributorActivity extends BaseActivity {
    private MyDistributorController mController;

    @Override
    protected void InitDataView() {

    }

    @Override
    protected void initController() {
        mController=new MyDistributorController(this);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_my_distributor);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onViewClick(View v) {

    }
}
