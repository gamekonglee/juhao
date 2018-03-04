package bc.juhao.com.ui.activity;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import java.util.Timer;
import java.util.TimerTask;

import bc.juhao.com.R;
import bc.juhao.com.common.BaseActivity;
import bc.juhao.com.cons.Constance;
import bc.juhao.com.ui.activity.user.LoginActivity;
import bc.juhao.com.utils.MyShare;
import bc.juhao.com.utils.UIUtils;
import bocang.utils.AppUtils;
import bocang.utils.CommonUtil;
import bocang.utils.IntentUtil;

/**
 * @author Jun
 * @time 2017/1/5  10:29
 * @desc 启动页
 */
public class SplashActivity extends BaseActivity {
    private ImageView mLogoIv;
    private AlphaAnimation mAnimation;
    private TextView version_tv;
    private String imei;
    private int WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 1;
    @Override
    protected void InitDataView() {
        String localVersion = CommonUtil.localVersionName(this);
        version_tv.setText("V "+localVersion);
    }

    @Override
    protected void initController() {

    }

    @Override
    protected void initView() {
        //去除title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //去掉Activity上面的状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash);
        mLogoIv = (ImageView) findViewById(R.id.logo_iv);
        mLogoIv.setScaleType(ImageView.ScaleType.CENTER_CROP);
        //布置透明度动画
        mAnimation.setDuration(2500);
        mAnimation.setFillAfter(true);
        mLogoIv.startAnimation(mAnimation);
        String token = MyShare.get(this).getString(Constance.TOKEN);
        if (AppUtils.checkNetwork() && !AppUtils.isEmpty(token)){
            getSuccessLogin();
        }
        version_tv = (TextView)findViewById(R.id.version_tv);
        int osVersion = Integer.valueOf(android.os.Build.VERSION.SDK);
        if (osVersion>22){
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.UNINSTALL_SHORTCUT)
                    != PackageManager.PERMISSION_GRANTED) {
                //申请WRITE_EXTERNAL_STORAGE权限
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.UNINSTALL_SHORTCUT},
                        WRITE_EXTERNAL_STORAGE_REQUEST_CODE);
            }else{
                getImei();
            }
        }else{
            //如果SDK小于6.0则不去动态申请权限
            getImei();
        }


    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == WRITE_EXTERNAL_STORAGE_REQUEST_CODE) {
            getImei();
        }else{
        }
    }

    /**
     * 登录成功处理事件
     */
    private void getSuccessLogin() {
        final String uid=MyShare.get(this).getString(Constance.USERID);
        if(AppUtils.isEmpty(uid)){
            return;
        }

        if(EMClient.getInstance().isLoggedInBefore()){
            EMClient.getInstance().logout(true, new EMCallBack() {

                @Override
                public void onSuccess() {
                    // TODO Auto-generated method stub
                    Log.e("520it", "S注销成功");
                    getSuccessLogin();
                }

                @Override
                public void onProgress(int progress, String status) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void onError(int code, String message) {
                    // TODO Auto-generated method stub
                    Log.e("520it", "S注销失败");

                }
            });

        }else{
            EMClient.getInstance().login(uid, uid, new EMCallBack() {
                @Override
                public void onSuccess() {
                    Log.e("520it", "S登录成功");
                    EMClient.getInstance().groupManager().loadAllGroups();
                    EMClient.getInstance().chatManager().loadAllConversations();
                }

                @Override
                public void onProgress(int progress, String status) {
                }

                @Override
                public void onError(final int code, final String message) {
                    Log.e("520it", "S登录失败:"+message);
                    if(message.equals("User dosn't exist")){
                        sendRegiestSuccess();
                    }

                }
            });
        }


    }

    /**
     * 环信注册
     */
    private void sendRegiestSuccess() {
        final String uid=MyShare.get(this).getString(Constance.USERID);
        if(AppUtils.isEmpty(uid)){
            return;
        }
        new Thread(new Runnable() {
            public void run() {
                try {
                    EMClient.getInstance().createAccount(uid,uid);//同步方法
                    SplashActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            MyShare.get(SplashActivity.this).putBoolean(Constance.EMREGIEST, true);//保存TOKEN
                            Log.e("520it", "S注册成功!");
                            getSuccessLogin();

                        }
                    });

                } catch (final HyphenateException e) {
                    SplashActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.e("520it", "S注册失败!：" + e.getMessage());
                            getSuccessLogin();
                        }
                    });

                }
            }
        }).start();
    }


    @Override
    protected void initData() {
        mAnimation = new AlphaAnimation (0.2f, 1.0f);
        new Timer().schedule(new TimerSchedule(), 2600);
    }

    private class TimerSchedule extends TimerTask {
        @Override
        public void run() {
           Boolean isFinish= MyShare.get(SplashActivity.this).getBoolean(Constance.ISFIRSTISTART);
            if(isFinish){
               String token= MyShare.get(SplashActivity.this).getString(Constance.TOKEN);
               String userCode= MyShare.get(SplashActivity.this).getString(Constance.USERCODE);
                if(AppUtils.isEmpty(token) && AppUtils.isEmpty(userCode)){
                    IntentUtil.startActivity(SplashActivity.this, LoginActivity.class, true);
                }else{
                    IntentUtil.startActivity(SplashActivity.this, MainActivity.class, true);
                }

            }else{
                IntentUtil.startActivity(SplashActivity.this, LeadPageActivity.class, true);
            }
        }
    }
    public static boolean isInstallShortcut(Context context, String applicationName) {
        boolean isInstallShortcut = false;
        ContentResolver cr = context.getContentResolver();
        //sdk大于8的时候,launcher2的设置查找
        String AUTHORITY = "com.android.launcher2.settings";
        Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/favorites?notify=true");
        Cursor c = cr.query(CONTENT_URI, new String[] { "title", "iconResource" },
                "title=?", new String[] { applicationName }, null);
        if (c != null && c.getCount() > 0) {
            isInstallShortcut = true;
        }
        if (c != null) {
            c.close();
        }
        //如果存在先关闭cursor，再返回结果
        if (isInstallShortcut) {
            return isInstallShortcut;
        }
        //android.os.Build.VERSION.SDK_INT < 8时
        AUTHORITY = "com.android.launcher.settings";
        CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/favorites?notify=true");
        c = cr.query(CONTENT_URI, new String[] { "title", "iconResource" }, "title=?",
                new String[] {applicationName}, null);
        if (c != null && c.getCount() > 0) {
            isInstallShortcut = true;
        }
        if (c != null) {
            c.close();
        }
        return isInstallShortcut;
    }
    //获取当前app的应用程序名称
    public static String getApplicationName(Context  context) {
        PackageManager packageManager = null;
        ApplicationInfo applicationInfo = null;
        try {
            packageManager = context.getApplicationContext().getPackageManager();
            applicationInfo=packageManager.getApplicationInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            applicationInfo = null;
        }
        String applicationName =(String) packageManager.getApplicationLabel(applicationInfo);
        return applicationName;
    }
    //删除shortcut
    public static void delShortcut(Context cx) {
        Intent shortcut = new Intent("com.android.launcher.action.UNINSTALL_SHORTCUT");
        // 获取当前应用名称
        String title = null;
        try {
            final PackageManager pm=cx.getPackageManager();     title=pm.getApplicationLabel(pm.getApplicationInfo(cx.getPackageName(),PackageManager.GET_META_DATA)).toString();
        } catch (Exception e) {
        }
        // 快捷方式名称
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, title);
        Intent shortcutIntent = cx.getPackageManager().getLaunchIntentForPackage(cx.getPackageName());
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT,shortcutIntent);
        cx.sendBroadcast(shortcut);
    }
    public void getImei(){
        if(isInstallShortcut(this, getApplicationName(this))){
            delShortcut(this);
        }
    }
}
