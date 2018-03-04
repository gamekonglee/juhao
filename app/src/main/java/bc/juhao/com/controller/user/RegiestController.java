package bc.juhao.com.controller.user;

import android.content.Intent;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import bc.juhao.com.R;
import bc.juhao.com.cons.Constance;
import bc.juhao.com.cons.NetWorkConst;
import bc.juhao.com.controller.BaseController;
import bc.juhao.com.listener.INetworkCallBack;
import bc.juhao.com.ui.activity.MainActivity;
import bc.juhao.com.ui.activity.user.RegiestActivity;
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
 * @author: Jun
 * @date : 2017/2/8 17:25
 * @description :注册第二步
 */
public class RegiestController extends BaseController implements INetworkCallBack {
    private RegiestActivity mView;
    private EditText edtPhone, edtCode, edPwd, edtAffirmPwd, nikname_et;
    private String mPhone;
    private int mSmsCount = 0;

    public RegiestController(RegiestActivity v) {
        mView = v;
        initView();
        initViewData();
    }

    private void initViewData() {
        SMSSDK.initSDK(mView, "1eba557757363", "29cd2e2ce4e9087bd43129580161b82c");
        EventHandler eh = new EventHandler() {

            @Override
            public void afterEvent(int event, int result, Object data) {

                if (result == SMSSDK.RESULT_COMPLETE) {
                    //回调完成
                    if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                        String deviceId = UIUtils.getLocalMac(mView);
                        String code = edtCode.getText().toString() + "11";
                        String pwd = edPwd.getText().toString();
                        String nickName = nikname_et.getText().toString();
                        mNetWork.sendRegiest(deviceId, mPhone, pwd, code, mView.yaoqing, nickName, RegiestController.this);
                        //提交验证码成功
                    } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                        //获取验证码成功
                    } else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {
                        //返回支持发送验证码的国家列表
                    }
                } else {
                    ((Throwable) data).printStackTrace();
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
        edtPhone = (EditText) mView.findViewById(R.id.edtPhone);
        edtCode = (EditText) mView.findViewById(R.id.edtCode);
        edPwd = (EditText) mView.findViewById(R.id.edPwd);
        nikname_et = (EditText) mView.findViewById(R.id.nikname_et);
        edtAffirmPwd = (EditText) mView.findViewById(R.id.edtAffirmPwd);
    }

    @Override
    protected void handleMessage(int action, Object[] values) {

    }

    @Override
    protected void myHandleMessage(Message msg) {

    }

    public void sendRegiest() {
        mPhone = edtPhone.getText().toString();
        String code = edtCode.getText().toString();
        String pwd = edPwd.getText().toString();
        String nikName = nikname_et.getText().toString();
        String affirmPwd = edtAffirmPwd.getText().toString();
        String deviceId = UIUtils.getLocalMac(mView);

        if (AppUtils.isEmpty(mPhone)) {
            AppDialog.messageBox(UIUtils.getString(R.string.isnull_phone));
            return;
        }
        if (AppUtils.isEmpty(code)) {
            AppDialog.messageBox(UIUtils.getString(R.string.isnull_verification_code));
            return;
        }
        if (AppUtils.isEmpty(pwd)) {
            AppDialog.messageBox(UIUtils.getString(R.string.isnull_pwd));
            return;
        }
        if (AppUtils.isEmpty(affirmPwd)) {
            AppDialog.messageBox(UIUtils.getString(R.string.isnull_affirm_pwd));
            return;
        }
        if (AppUtils.isEmpty(nikName)) {
            AppDialog.messageBox("昵称不能为空!");
            return;
        }

        // 做个正则验证手机号
        if (!CommonUtil.isMobileNO(mPhone)) {
            AppDialog.messageBox(UIUtils.getString(R.string.mobile_assert));
            return;
        }

        if (!affirmPwd.equals(pwd)) {
            AppDialog.messageBox(UIUtils.getString(R.string.compare_pwd_affirm));
            return;
        }
        mView.setShowDialog(true);
        mView.setShowDialog("正在注册中..");
        mView.showLoading();
        mNetWork.sendRegiest(deviceId, mPhone, pwd, code, mView.yaoqing, nikName, RegiestController.this);
    }

    @Override
    public void onSuccessListener(String requestCode, JSONObject ans) {
        mView.hideLoading();
        switch (requestCode) {
            case NetWorkConst.REGIEST:
                String token = ans.getString(Constance.TOKEN);
                String id = ans.getJSONObject(Constance.user).getString(Constance.id);
                MyShare.get(mView).putString(Constance.TOKEN, token);//保存TOKEN
                MyShare.get(mView).putString(Constance.USERNAME, mPhone);//保存帐号
                MyShare.get(mView).putString(Constance.USERID, id);//保存帐号
                AppDialog.messageBox(UIUtils.getString(R.string.regiest_ok));
                sendRegiestSuccess();
                Intent logoutIntent = new Intent(mView, MainActivity.class);
                logoutIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                mView.startActivity(logoutIntent);
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
        AppDialog.messageBox(ans.getString(Constance.error_desc));
    }


    /**
     * 环信注册成功
     */
    private void sendRegiestSuccess() {
        final String uid = MyShare.get(mView).getString(Constance.USERID);
        if (AppUtils.isEmpty(uid)) {
            return;

        }
        new Thread(new Runnable() {
            public void run() {
                try {
                    EMClient.getInstance().createAccount(uid, uid);//同步方法
                    mView.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.e("520it", "r注册成功!");
                            //                            getSuccessLogin();

                        }
                    });

                } catch (final HyphenateException e) {
                    Log.e("520it", "r注册失败!");
                }
            }
        }).start();
    }


    /**
     * 登录成功处理事件
     */
    private void getSuccessLogin() {
        final String uid = MyShare.get(mView).getString(Constance.USERID);
        if (AppUtils.isEmpty(uid)) {
            return;
        }

        if (EMClient.getInstance().isLoggedInBefore()) {
            EMClient.getInstance().logout(true, new EMCallBack() {

                @Override
                public void onSuccess() {
                    // TODO Auto-generated method stub
                    Log.e("520it", "r注销登录");
                    getSuccessLogin();
                }

                @Override
                public void onProgress(int progress, String status) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void onError(int code, String message) {
                    // TODO Auto-generated method stub
                    Log.e("520it", "r注销失败");

                }
            });

        } else {
            EMClient.getInstance().login(uid, uid, new EMCallBack() {
                @Override
                public void onSuccess() {
                    Log.e("520it", "r登录成功");
                    EMClient.getInstance().groupManager().loadAllGroups();
                    EMClient.getInstance().chatManager().loadAllConversations();
                }

                @Override
                public void onProgress(int progress, String status) {
                }

                @Override
                public void onError(final int code, final String message) {
                    Log.e("520it", "r登录失败");
                }
            });
        }


    }


    public void requestYZM() {
        mPhone = edtPhone.getText().toString();
        if (HyUtil.isEmpty(mPhone)) {
            MyToast.show(mView, "请输入手机号码");
            return;
        }
        if (!CommonUtil.isMobileNO(mPhone)) {
            MyToast.show(mView, "请输入正确的手机号码");
            return;
        }
        mView.find_pwd_btnGetCode.start(60);
        //

        //        //打开注册页面
        //        RegisterPage registerPage = new RegisterPage();
        //        registerPage.setRegisterCallback(new EventHandler() {
        //            public void afterEvent(int event, int result, Object data) {
        //                // 解析注册结果
        //                if (result == SMSSDK.RESULT_COMPLETE) {
        //                    @SuppressWarnings("unchecked")
        //                    HashMap<String,Object> phoneMap = (HashMap<String, Object>) data;
        //                    String country = (String) phoneMap.get("country");
        //                    String phone = (String) phoneMap.get("phone");
        //
        //                    // 提交用户信息（此方法可以不调用）
        ////                    registerUser(country, phone);
        //                }
        //            }
        //        });
        //        registerPage.show(mView);

        //        if(mSmsCount>2){
        //            SMSSDK.getVoiceVerifyCode("86", mPhone);
        //        }else{
        //            SMSSDK.getVerificationCode("86", mPhone);
        //        }
        //        mSmsCount=mSmsCount+1;
        mNetWork.sendRequestYZM(mPhone, this);
        //        mNetWork.sendRequestYZM(mPhone,this);


    }
}
