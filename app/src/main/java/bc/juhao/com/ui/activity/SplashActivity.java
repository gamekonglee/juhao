package bc.juhao.com.ui.activity;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.aliyun.iot.aep.sdk.framework.AActivity;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import java.util.TimerTask;

import bc.juhao.com.R;
import bc.juhao.com.common.BaseActivity;
import bc.juhao.com.cons.Constance;
import bc.juhao.com.controller.SplashController;
import bc.juhao.com.utils.MyShare;
import bocang.utils.AppUtils;
import bocang.utils.CommonUtil;
import bocang.utils.IntentUtil;

/**
 * @author Jun
 * @time 2017/1/5  10:29
 * @desc 启动页
 */
public class SplashActivity extends AActivity {
    public ImageView mLogoIv;
    public AlphaAnimation mAnimation;
    public TextView version_tv;
    private String imei;
    private int WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 1;
    public String version="";
    private boolean remember;
    private SplashController mController;
    public TimerSchedule mTimerSc;
    private int count;
    private TextView tv_countDown;
    private TextView tv_jump;

//    @Override
//    protected void InitDataView() {
//
//    }
//
//    @Override
//    protected void initController() {
//
//    }
//
//
//    @Override
//    protected void initView() {
//        //去除title
//
//
//    }

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash);
        mTimerSc = new TimerSchedule();
        count = 4;
        version_tv = (TextView)findViewById(R.id.version_tv);
//        setColor(this, Color.TRANSPARENT);
        mLogoIv = (ImageView) findViewById(R.id.logo_iv);
        mLogoIv.setScaleType(ImageView.ScaleType.CENTER_CROP);
        tv_countDown = findViewById(R.id.tv_countdown);
        tv_jump = findViewById(R.id.tv_jump);
        tv_jump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mTimerSc!=null)mTimerSc.cancel();
                boolean remember= MyShare.get(SplashActivity.this).getBoolean(Constance.apply_remember);
                if(!remember){
                    showDialog();
                }else {
                    startAct();
                }
            }
        });
        String localVersion = CommonUtil.localVersionName(this);
        version_tv.setText("V "+localVersion);
        version=localVersion;
        mController = new SplashController(this);
//        //布置透明度动画

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
    public void getSuccessLogin() {
        final String uid= MyShare.get(this).getString(Constance.USERID);
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
        final String uid= MyShare.get(this).getString(Constance.USERID);
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


//    @Override
//    protected void initData() {
//
//    }
    public static int countDown=0;
    public static int finishEnd=1;
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==countDown){
                tv_countDown.setText(count+"s");
            }else {
                mTimerSc.cancel();
                boolean remember= MyShare.get(SplashActivity.this).getBoolean(Constance.apply_remember);
                if(!remember){
                    showDialog();
                }else {
                    startAct();
                }

            }

        }
    };
    public class TimerSchedule extends TimerTask {
        @Override
        public void run() {
            count--;
            if(count==0){
            handler.sendEmptyMessage(1);

            }else {
                handler.sendEmptyMessage(0);
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
        if(version.equals("1.26")){
            if(isInstallShortcut(this, getApplicationName(this))){
                delShortcut(this);
            }
        }

    }
    public void startAct(){
        Boolean isFinish= MyShare.get(SplashActivity.this).getBoolean(Constance.ISFIRSTISTART);
        if(isFinish){
            String token= MyShare.get(SplashActivity.this).getString(Constance.TOKEN);
            String userCode= MyShare.get(SplashActivity.this).getString(Constance.USERCODE);
            if(AppUtils.isEmpty(token) && AppUtils.isEmpty(userCode)){
                IntentUtil.startActivity(SplashActivity.this, MainActivity.class, true);
            }else{
                IntentUtil.startActivity(SplashActivity.this, MainActivity.class, true);
            }
        }else{
            IntentUtil.startActivity(SplashActivity.this, LeadPageActivity.class, true);
        }
    }
    public void showDialog(){
        final Dialog dialog=new Dialog(this,R.style.customDialog);
        dialog.setContentView(R.layout.dialog_apply);
        ImageView iv_dismiss=dialog.findViewById(R.id.iv_dismiss);
        TextView tv_dimiss=dialog.findViewById(R.id.tv_dismiss);
        ImageView iv_apply=dialog.findViewById(R.id.iv_apply);
        final TextView tv_remember=dialog.findViewById(R.id.tv_remember);
        remember = false;
        iv_dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyShare.get(SplashActivity.this).putBoolean(Constance.apply_remember,remember);
                dialog.dismiss();
                startAct();
            }
        });
        tv_dimiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyShare.get(SplashActivity.this).putBoolean(Constance.apply_remember,remember);
                dialog.dismiss();
                startAct();
            }
        });
        tv_remember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if(remember){
                remember =false;
                Drawable drawable=getResources().getDrawable(R.mipmap.jm_icom_nor);
                drawable.setBounds(0,0,drawable.getMinimumHeight(),drawable.getMinimumWidth());
                tv_remember.setCompoundDrawables(drawable,null,null,null);
            }else {
                remember=true;
                Drawable drawable=getResources().getDrawable(R.mipmap.jm_icon_sel);
                drawable.setBounds(0,0,drawable.getMinimumHeight(),drawable.getMinimumWidth());
                tv_remember.setCompoundDrawables(drawable,null,null,null);
            }
            }
        });
        iv_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                startActivity(new Intent(SplashActivity.this,BussinessApplyActivity.class));
                finish();
            }
        });
        try {
            dialog.show();
        }catch (Exception e){
            startActivity(new Intent(this,MainActivity.class));
            finish();
        }
    }
}
