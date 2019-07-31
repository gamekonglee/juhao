package bc.juhao.com.controller.user;

import android.os.Message;
import android.widget.EditText;

import com.aliyun.iot.ilop.demo.DemoApplication;

import bc.juhao.com.R;
import bc.juhao.com.cons.Constance;
import bc.juhao.com.cons.NetWorkConst;
import bc.juhao.com.controller.BaseController;
import bc.juhao.com.listener.INetworkCallBack;
import bc.juhao.com.ui.activity.IssueApplication;
import bc.juhao.com.ui.activity.user.SetInviteCodeActivity;
import bc.juhao.com.utils.UIUtils;
import bocang.json.JSONObject;
import bocang.utils.AppDialog;
import bocang.utils.AppUtils;
import bocang.utils.MyToast;

/**
 * @author Jun
 * @time 2017/10/21  22:38
 * @desc 设置邀请码
 */

public class SetInviteCodeController extends BaseController implements INetworkCallBack {
    private SetInviteCodeActivity mView;
    private EditText et_value;

    public SetInviteCodeController(SetInviteCodeActivity v) {
        mView = v;
        initView();
        initViewData();
    }

    private void initViewData() {

    }

    private void initView() {
        et_value = (EditText) mView.findViewById(R.id.et_value);
    }

    @Override
    protected void handleMessage(int action, Object[] values) {

    }

    @Override
    protected void myHandleMessage(Message msg) {

    }

    public void editInviteCode() {
        String inviteCode = et_value.getText().toString();
        String userId = DemoApplication.mUserObject.getString(Constance.id);
        if (AppUtils.isEmpty(inviteCode)) {
            MyToast.show(mView, "邀请码不能为空!");
            return;
        }
        if (inviteCode.length() < 6) {
            MyToast.show(mView, "邀请码不能少于位数6!");
            return;
        }
        mView.setShowDialog(true);
        mView.setShowDialog("正在设置中..");
        mView.showLoading();
        mNetWork.editInviteCode(userId, inviteCode, this);
    }

    @Override
    public void onSuccessListener(String requestCode, JSONObject ans) {
        switch (requestCode) {
            case NetWorkConst.LEVEL_EDIT_CODE_URL:
                mView.hideLoading();
                int code = ans.getInt(Constance.error_code);
                if (code == 400) {
                    MyToast.show(mView, "邀请码已用过，请重新输入一个新的!");
                } else {
                    MyToast.show(mView, "邀请码设置成功!");
                    mView.finish();
                }
                break;

        }
    }

    @Override
    public void onFailureListener(String requestCode, JSONObject ans) {
        mView.hideLoading();
        if (null == mView || mView.isFinishing())
            return;
        if (AppUtils.isEmpty(ans)) {
            AppDialog.messageBox(UIUtils.getString(R.string.server_error));
            return;
        }
//        AppDialog.messageBox(ans.getString(Constance.error_desc));
    }
}
