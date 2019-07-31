package bc.juhao.com.ui.activity.user;

import android.view.View;

import bc.juhao.com.R;
import bc.juhao.com.controller.user.MessageController;
import bocang.view.BaseActivity;

/**
 * @author: Jun
 * @date : 2017/3/10 15:44
 * @description :消息中心
 */
public class MessageActivity extends BaseActivity {
    private MessageController mController;
    @Override
    protected void InitDataView() {
        mController=new MessageController(this);
    }

    @Override
    protected void initController() {

    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_sys_message);
        setColor(this,getResources().getColor(R.color.green));
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onViewClick(View v) {

    }
}
