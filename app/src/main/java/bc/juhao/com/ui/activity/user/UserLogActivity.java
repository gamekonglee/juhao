package bc.juhao.com.ui.activity.user;

import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.Button;

import bc.juhao.com.R;
import bc.juhao.com.cons.Constance;
import bc.juhao.com.controller.user.UserLogController;
import bocang.view.BaseActivity;

/**
 * @author: Jun
 * @date : 2017/1/19 17:10
 * @description :物流列表
 */
public class UserLogActivity extends BaseActivity {
    private UserLogController mController;
    private Button btn_save;
    public boolean isSelectLogistics=false;
    private Intent mIntent;

    @Override
    protected void InitDataView() {
    }

    @Override
    protected void initController() {
        mController=new UserLogController(this);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_user_logistics);
        setColor(this, Color.WHITE);
        btn_save = getViewAndClick(R.id.btn_save);

    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        isSelectLogistics=intent.getBooleanExtra(Constance.isSelectLogistice, false);
    }

    @Override
    protected void onViewClick(View v) {
        switch (v.getId()){
            case R.id.btn_save:
                mIntent=new Intent(this,UserLogAddActivity.class);
                this.startActivityForResult(mIntent, Constance.FROMLOG);
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mController.ActivityResult(requestCode, resultCode, data);
    }
}
