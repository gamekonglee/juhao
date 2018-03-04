package bc.juhao.com.ui.activity.user;

import android.view.View;
import android.widget.Button;

import bc.juhao.com.R;
import bc.juhao.com.controller.user.SetInviteCodeController;

/**
 * @author Jun
 * @time 2017/10/21  22:37
 * @desc 设置邀请码
 */

public class SetInviteCodeActivity extends bocang.view.BaseActivity {
    private SetInviteCodeController mController;
    private Button sure_bt;

    @Override
    protected void InitDataView() {

    }

    @Override
    protected void initController() {
        mController=new SetInviteCodeController(this);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_set_invitecode);
        sure_bt=getViewAndClick(R.id.sure_bt);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onViewClick(View v) {
        switch (v.getId()){
            case R.id.sure_bt://确定
                mController.editInviteCode();
            break;
        }
    }
}
