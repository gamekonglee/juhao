package bc.juhao.com.controller.user;

import android.content.Context;
import android.os.Message;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import org.greenrobot.eventbus.EventBus;

import bc.juhao.com.R;
import bc.juhao.com.bean.LoginResult;
import bc.juhao.com.cons.Constance;
import bc.juhao.com.cons.NetWorkConst;
import bc.juhao.com.controller.BaseController;
import bc.juhao.com.listener.INetworkCallBack;
import bc.juhao.com.ui.activity.MainActivity;
import bc.juhao.com.ui.activity.user.ForgetPasswordActivity;
import bc.juhao.com.ui.activity.user.LoginActivity;
import bc.juhao.com.ui.activity.user.Regiest01Activity;
import bc.juhao.com.ui.activity.user.UpdatePasswordActivity;
import bc.juhao.com.utils.MyShare;
import bc.juhao.com.utils.UIUtils;
import bocang.json.JSONObject;
import bocang.utils.AppDialog;
import bocang.utils.AppUtils;
import bocang.utils.IntentUtil;
import bocang.utils.MyToast;

/**
 * @author: Jun
 * @date : 2017/2/7 14:07
 * @description :登录
 */
public class LoginController extends BaseController implements INetworkCallBack {
    private LoginActivity mView;
    private ImageView phone_iv, pwd_iv;
    private TextView regiest_tv, find_pwd_tv;
    private RelativeLayout pwd_rl;
    private TextView typeTv, type02Tv;
    private EditText phone_et, pwd_et;
    private int mType = 0;
    private Button login_bt;
    private String mCode;


    public LoginController(LoginActivity v) {
        mView = v;
        initView();
        InitViewData();
    }

    private void InitViewData() {
        if (mType == 0) {
            phone_et.setText(MyShare.get(mView).getString(Constance.USERNAME));
        }
        selectType(R.id.type02Tv);
    }


    private void initView() {
        phone_iv = (ImageView) mView.findViewById(R.id.phone_iv);
        pwd_iv = (ImageView) mView.findViewById(R.id.pwd_iv);
        phone_et = (EditText) mView.findViewById(R.id.phone_et);
        pwd_et = (EditText) mView.findViewById(R.id.pwd_et);
        regiest_tv = (TextView) mView.findViewById(R.id.regiest_tv);
        find_pwd_tv = (TextView) mView.findViewById(R.id.find_pwd_tv);
        pwd_rl = (RelativeLayout) mView.findViewById(R.id.pwd_rl);
        type02Tv = (TextView) mView.findViewById(R.id.type02Tv);
        typeTv = (TextView) mView.findViewById(R.id.typeTv);
        login_bt = (Button) mView.findViewById(R.id.login_bt);
    }

    @Override
    protected void handleMessage(int action, Object[] values) {

    }

    @Override
    protected void myHandleMessage(Message msg) {

    }

    /**
     * 判断是通过什么方式登录
     *
     * @param type
     */
    public void selectType(int type) {
//        typeTv.setTextColor(mView.getResources().getColor(R.color.fontColor2));
        type02Tv.setTextColor(mView.getResources().getColor(R.color.fontColor2));
        phone_et.setText("");

        switch (mView.loginType) {
            case 1:
                mView.loginType=0;
//                typeTv.setTextColor(mView.getResources().getColor(R.color.theme_orange));
                type02Tv.setText("邀请码");
                pwd_rl.setVisibility(View.VISIBLE);
                phone_et.setHint(UIUtils.getString(R.string.him_phone));
                phone_iv.setImageResource(R.mipmap.login_phone);
                phone_et.setText(MyShare.get(mView).getString(Constance.USERNAME));
                mType = 0;
                break;
            case 0:
                mView.loginType=1;
//                type02Tv.setTextColor(mView.getResources().getColor(R.color.theme_orange));
                type02Tv.setText("已注册");
                pwd_rl.setVisibility(View.GONE);
                phone_et.setHint(UIUtils.getString(R.string.him_invite_code));
                phone_iv.setImageResource(R.mipmap.login_password);
                mType = 1;
                break;
        }
    }

    /**
     * 登录
     */
    public void sendLogin() {
        mCode = phone_et.getText().toString();
        String pwd = pwd_et.getText().toString();
        if (AppUtils.isEmpty(mCode) && mType == 0) {
            AppDialog.messageBox(UIUtils.getString(R.string.isnull_accounts));
            return;
        }

        if (AppUtils.isEmpty(mCode) && mType == 1) {
            AppDialog.messageBox(UIUtils.getString(R.string.isnull_invitation_code));
            return;
        }

        //判断密码是否为空
        if (AppUtils.isEmpty(pwd) && mType == 0) {
            AppDialog.messageBox(UIUtils.getString(R.string.isnull_pwd));
            return;
        }
        mView.setShowDialog(true);
        mView.setShowDialog("正在登录中..");
        mView.showLoading();
        InputMethodManager imm =  (InputMethodManager)mView.getSystemService(Context.INPUT_METHOD_SERVICE);
        if(imm != null) {

            imm.hideSoftInputFromWindow(mView.getWindow().getDecorView().getWindowToken(),
                    0);
        }
        if (mType == 0) {//手机号码登录
            mNetWork.sendLogin(mCode, pwd, this);
        } else {//邀请码登录
            //TODO

            sendUserCode(mCode);
        }
    }

    /**
     * 注册
     */
    public void sendRegiest() {
        IntentUtil.startActivity(mView, Regiest01Activity.class, false);
    }
    /**
     * 判断邀请码
     */
    public void sendUserCode(String code) {
       mNetWork.sendUserCode(code, this);
    }

    /**
     * 找回密码
     */
    public void sendFindPwd() {
        IntentUtil.startActivity(mView, ForgetPasswordActivity.class, false);
    }

    @Override
    public void onSuccessListener(String requestCode, JSONObject ans) {
        switch (requestCode) {
            case NetWorkConst.LOGIN:
                mView.hideLoading();
                if (mType == 0) {
                    MyShare.get(mView).putString(Constance.USERNAME, mCode);
                    MyShare.get(mView).putString(Constance.USERID, ans.getJSONObject(Constance.user).getString(Constance.id));

                }
                MyShare.get(mView).putString(Constance.username,phone_et.getText().toString().trim());
                MyShare.get(mView).putString(Constance.pwd,pwd_et.getText().toString().trim());

                String token = ans.getString(Constance.TOKEN);
                MyShare.get(mView).putString(Constance.TOKEN, token);
                EventBus.getDefault().postSticky(new LoginResult(1));
                IntentUtil.startActivity(mView, MainActivity.class, true);
                getSuccessLogin();

                break;
            case NetWorkConst.USERCODE:
                mView.hideLoading();
                if(AppUtils.isEmpty(ans.getJSONObject(Constance.user))){
                    MyToast.show(mView,"邀请码错误!");
                }else{
                    MyShare.get(mView).putString(Constance.invite_code,mCode);
                    MyShare.get(mView).putString(Constance.USERCODE, ans.getJSONObject(Constance.user).getString(Constance.user_name));
                    MyShare.get(mView).putInt(Constance.USERCODEID, ans.getJSONObject(Constance.user).getInt(Constance.user_id));
                    IntentUtil.startActivity(mView, MainActivity.class, true);
                }

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

    /**
     * 环信注册
     */
    private void sendRegiestSuccess() {
        final String uid= MyShare.get(mView).getString(Constance.USERID);
        if(AppUtils.isEmpty(uid)){
            return;
        }
        new Thread(new Runnable() {
            public void run() {
                try {
                    EMClient.getInstance().createAccount(uid,uid);//同步方法
                    mView.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            MyShare.get(mView).putBoolean(Constance.EMREGIEST, true);//保存TOKEN
                            Log.e("520it", "L注册成功!");
                            getSuccessLogin();

                        }
                    });

                } catch (final HyphenateException e) {
                    mView.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            MyToast.show(mView, "服务器连接失败，请重新登录!");
                            MyShare.get(mView).putString(Constance.TOKEN, "");
                        }
                    });

                }
            }
        }).start();
    }

    /**
     * 登录成功处理事件
     */
    private void getSuccessLogin() {
        final String uid= MyShare.get(mView).getString(Constance.USERID);        Log.d(TAG, "USERID: "+uid);
        if(AppUtils.isEmpty(uid)){
            return;
        }

        if(EMClient.getInstance().isLoggedInBefore()){
            EMClient.getInstance().logout(true, new EMCallBack() {

                @Override
                public void onSuccess() {
                    // TODO Auto-generated method stub
                    Log.e("520it", "Login注销成功,曾在登录");
                    getSuccessLogin();
                }

                @Override
                public void onProgress(int progress, String status) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void onError(int code, String message) {
                    // TODO Auto-generated method stub
                    Log.e("520it", "Login注销失败");
                }
            });

        }else{
            EMClient.getInstance().login(uid, uid, new EMCallBack() {
                @Override
                public void onSuccess() {

                    Log.e("520it", "Login直接登录");
                    EMClient.getInstance().groupManager().loadAllGroups();
                    EMClient.getInstance().chatManager().loadAllConversations();
//                    DemoHelper.getInstance().init(mView);
                }

                @Override
                public void onProgress(int progress, String status) {
                }

                @Override
                public void onError(final int code, final String message) {
                    Log.e("520it", "Login登录失败："+message);
                    if(message.equals("User dosn't exist")){
                        sendRegiestSuccess();
                    }
                }
            });
        }


    }



//    /**
//     * 分享操作
//     */
//    public void showShare(final String url, final String appName) {
////        ShareSDK.initSDK(mView);
//        HashMap<String, Object> wechat = new HashMap<String, Object>();
//        wechat.put("Id", "4");
//        wechat.put("SortId", "4");
//        wechat.put("AppId", "wx552a6247e06de962");
//        wechat.put("AppSecret", "fad84dbef5639004bd73c4bce0995c1d");
//        wechat.put("BypassApproval", "false");
//        wechat.put("Enable", "true");
//        ShareSDK.setPlatformDevInfo(Wechat.NAME, wechat);
//        ShareSDK.setPlatformDevInfo(WechatMoments.NAME, wechat);
//        OnekeyShare oks = new OnekeyShare();
//        //关闭sso授权
//        oks.disableSSOWhenAuthorize();
//
//        // 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
//        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
//        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
//        oks.setTitle(appName);
//        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
//        oks.setTitleUrl(url);
//        // text是分享文本，所有平台都需要这个字段
//        oks.setText("一款能约，能玩，还能赚钱的神器。");
//        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
//        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
//        // url仅在微信（包括好友和朋友圈）中使用
//        oks.setUrl(url);
//        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
//        oks.setComment(appName);
//        // site是分享此内容的网站名称，仅在QQ空间使用
//        oks.setSite(appName);
//        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
//        oks.setSiteUrl(url);
//
//        oks.setImageUrl("http://118.178.241.214/ic_launcher.png");
//        oks.setShareContentCustomizeCallback(new ShareContentCustomizeCallback() {
//            @Override
//            public void onShare(Platform platform, Platform.ShareParams paramsToShare) {
//                if ("QZone".equals(platform.getName())) {
//                    paramsToShare.setTitle(appName);
//                    paramsToShare.setTitleUrl(url);
//                }
//                if ("SinaWeibo".equals(platform.getName())) {
//                    paramsToShare.setUrl(null);
//                    paramsToShare.setText("分享文本 ");
//                }
//                if ("Wechat".equals(platform.getName())) {
//                    Bitmap imageData = BitmapFactory.decodeResource(mView.getResources(), R.mipmap.ic_launcher);
//                    paramsToShare.setImageData(imageData);
//                }
//                if ("WechatMoments".equals(platform.getName())) {
//                    Bitmap imageData = BitmapFactory.decodeResource(mView.getResources(), R.mipmap.ic_launcher);
//                    paramsToShare.setImageData(imageData);
//                }
//
//            }
//        });
//
//        // 启动分享GUI
//        oks.show(mView);
//    }

    public void setEtVisibility(boolean isVisi) {
        if(isVisi){
            pwd_et.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            pwd_et.setSelection(pwd_et.getText().toString().length());
            mView.iv_see.setImageDrawable(mView.getResources().getDrawable(R.mipmap.login_display_cipher));
        }else {
            pwd_et.setTransformationMethod(PasswordTransformationMethod.getInstance());
            pwd_et.setSelection(pwd_et.getText().toString().length());
            mView.iv_see.setImageDrawable(mView.getResources().getDrawable(R.mipmap.login_hidden_password));
        }
    }
}
