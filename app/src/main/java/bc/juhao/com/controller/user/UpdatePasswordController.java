package bc.juhao.com.controller.user;

import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;

import bc.juhao.com.R;
import bc.juhao.com.cons.Constance;
import bc.juhao.com.cons.NetWorkConst;
import bc.juhao.com.controller.BaseController;
import bc.juhao.com.listener.INetworkCallBack;
import bc.juhao.com.ui.activity.user.UpdatePasswordActivity;
import bc.juhao.com.utils.MyShare;
import bc.juhao.com.utils.UIUtils;
import bocang.json.JSONObject;
import bocang.utils.AppDialog;
import bocang.utils.AppUtils;
import bocang.utils.CommonUtil;
import bocang.utils.HyUtil;
import bocang.utils.MyToast;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

/**
 * @author Jun
 * @time 2017/1/23  22:14
 * @desc 重置密码
 */
public class UpdatePasswordController extends BaseController implements INetworkCallBack {
    private UpdatePasswordActivity mView;
    private EditText find_pwd_edtPhone,find_pwd_edtCode,find_pwd_edtNewPwd,find_pwd_edtAffirmPwd;
    private int mSmsCount=0;
    private EditText edPwd_old;

    public UpdatePasswordController(UpdatePasswordActivity v){
        mView=v;
        initView();
        initViewData();
    }

    private void initViewData() {
        SMSSDK.initSDK(mView, "1eba557757363", "29cd2e2ce4e9087bd43129580161b82c");
        EventHandler eh=new EventHandler(){

            @Override
            public void afterEvent(int event, int result, Object data) {

                if (result == SMSSDK.RESULT_COMPLETE) {
                    //回调完成
                    if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                        String code=find_pwd_edtCode.getText().toString()+"11";
                        String pwd=find_pwd_edtNewPwd.getText().toString();
                        mNetWork.sendUpdatePwd(mPhone, pwd, code, UpdatePasswordController.this);
                        //提交验证码成功
                    }else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE){
                        //获取验证码成功
                    }else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES){
                        //返回支持发送验证码的国家列表
                    }
                }else{
                    ((Throwable)data).printStackTrace();
                    try {
                        Throwable throwable = (Throwable) data;
                        throwable.printStackTrace();
                        JSONObject object = new JSONObject(throwable.getMessage());
                        final String des = object.getString("detail");//错误描述
                        int status = object.getInt("status");//错误代码
                        if (status > 0 && !TextUtils.isEmpty(des)) {
                            Log.e("asd", "des: " + des);
                            mView.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    MyToast.show(mView, des);
                                }
                            });

                            mView.hideLoading();
                            return;
                        }
                    } catch (Exception e) {
                        //do something
                    }
                }
            }
        };

        SMSSDK.registerEventHandler(eh); //注册短信回调
    }

    private void initView() {
        find_pwd_edtPhone = (EditText)mView.findViewById(R.id.edtPhone);
        find_pwd_edtCode = (EditText)mView.findViewById(R.id.edtCode);
        find_pwd_edtNewPwd = (EditText)mView.findViewById(R.id.edPwd);
        find_pwd_edtAffirmPwd = (EditText)mView.findViewById(R.id.edtAffirmPwd);
        edPwd_old = mView.findViewById(R.id.edPwd_old);

    }

    @Override
    protected void handleMessage(int action, Object[] values) {

    }

    @Override
    protected void myHandleMessage(Message msg) {

    }

    /**
     * 获取验证码
     */
    public void requestYZM() {
        mPhone=find_pwd_edtPhone.getText().toString();
        if (HyUtil.isEmpty(mPhone)){
            MyToast.show(mView,"请输入手机号码");
            return;
        }
        if(!CommonUtil.isMobileNO(mPhone)){
            MyToast.show(mView,"请输入正确的手机号码");
            return;
        }
        mView.find_pwd_btnGetCode.start(60);

//        mNetWork.sendRequestYZM(phone, this);
//        if(mSmsCount>2){
//            SMSSDK.getVoiceVerifyCode("86", mPhone);
//        }else{
//            SMSSDK.getVerificationCode("86", mPhone);
//        }
//        mSmsCount=mSmsCount+1;
        mNetWork.sendRequestYZM(mPhone, this);

    }

    private String mPhone;

    /**
     * 修改密码
     */
    public void sendUpdatePwd() {
        mPhone=find_pwd_edtPhone.getText().toString();
        String code=find_pwd_edtCode.getText().toString();
        String newPwd=find_pwd_edtNewPwd.getText().toString();
        String affirmPwd=find_pwd_edtAffirmPwd.getText().toString();
        String oldPwd=edPwd_old.getText().toString();

        if (HyUtil.isEmpty(mPhone)){
            AppDialog.messageBox(UIUtils.getString(R.string.isnull_phone));
            return;
        }
        if(!CommonUtil.isMobileNO(mPhone)){
            AppDialog.messageBox(UIUtils.getString(R.string.mobile_assert));
            return;
        }
//        if(HyUtil.isEmpty(code)){
//            AppDialog.messageBox(UIUtils.getString(R.string.isnull_verification_code));
//            return;
//        }
        if(TextUtils.isEmpty(oldPwd)){
            AppDialog.messageBox("旧密码不能为空");
        }
        if(HyUtil.isEmpty(newPwd)){
            AppDialog.messageBox(UIUtils.getString(R.string.isnull_pwd));
            return;
        }
        if(HyUtil.isEmpty(affirmPwd)){
            AppDialog.messageBox(UIUtils.getString(R.string.isnull_affirm_pwd));
            return;
        }

        if(!affirmPwd.equals(newPwd)){
            AppDialog.messageBox(UIUtils.getString(R.string.compare_pwd_affirm));
            return;
        }

        //TODO修改密码
        mView.setShowDialog(true);
        mView.setShowDialog("正在重置中..");
        mView.showLoading();

        String pwd=find_pwd_edtNewPwd.getText().toString();
        mNetWork.sendUpdatePwdByOld(mPhone, pwd, oldPwd, UpdatePasswordController.this);

    }

    @Override
    public void onSuccessListener(String requestCode, JSONObject ans) {
        mView.hideLoading();
        switch (requestCode) {
            case NetWorkConst.UPDATE:
                String token=ans.getString(Constance.TOKEN);
                MyShare.get(mView).putString(Constance.TOKEN, token);//保存TOKEN
                MyToast.show(mView,"密码修改成功");
                mView.finish();
                break;
        }
    }

    @Override
    public void onFailureListener(String requestCode, JSONObject ans) {
        mView.hideLoading();
        if (null == mView || mView.isFinishing())
            return;
        if(AppUtils.isEmpty(ans)){
            AppDialog.messageBox(UIUtils.getString(R.string.server_error));
            return;
        }
        AppDialog.messageBox(ans.getString(Constance.error_desc));
    }
}
