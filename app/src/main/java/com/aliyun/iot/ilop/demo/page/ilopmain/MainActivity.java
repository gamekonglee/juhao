package com.aliyun.iot.ilop.demo.page.ilopmain;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.sdk.android.openaccount.OpenAccountSDK;
import com.alibaba.sdk.android.openaccount.OpenAccountService;
import com.alibaba.sdk.android.openaccount.callback.LoginCallback;
import com.alibaba.sdk.android.openaccount.callback.LogoutCallback;
import com.alibaba.sdk.android.openaccount.model.OpenAccountSession;
import com.aliyun.alink.business.devicecenter.extbone.BoneAddDeviceBiz;
import com.aliyun.alink.business.devicecenter.extbone.BoneHotspotHelper;
import com.aliyun.alink.business.devicecenter.extbone.BoneLocalDeviceMgr;
import com.aliyun.alink.linksdk.channel.core.base.AError;
import com.aliyun.alink.linksdk.channel.mobile.api.IMobileConnectListener;
import com.aliyun.alink.linksdk.channel.mobile.api.IMobileRequestListener;
import com.aliyun.alink.linksdk.channel.mobile.api.MobileChannel;
import com.aliyun.alink.linksdk.channel.mobile.api.MobileConnectConfig;
import com.aliyun.alink.linksdk.channel.mobile.api.MobileConnectState;
import com.aliyun.alink.sdk.jsbridge.BonePluginRegistry;
import com.aliyun.iot.aep.component.router.Router;
import com.aliyun.iot.aep.component.scan.ScanManager;
import com.aliyun.iot.aep.sdk.credential.IotCredentialManager.IoTCredentialListener;
import com.aliyun.iot.aep.sdk.credential.IotCredentialManager.IoTCredentialManage;
import com.aliyun.iot.aep.sdk.credential.IotCredentialManager.IoTCredentialManageError;
import com.aliyun.iot.aep.sdk.credential.IotCredentialManager.IoTCredentialManageImpl;
import com.aliyun.iot.aep.sdk.credential.data.IoTCredentialData;
import com.aliyun.iot.ilop.demo.DemoApplication;
import com.aliyun.iot.ilop.demo.page.main.IndexActivity;
import com.aliyun.iot.ilop.demo.page.main.StartActivity;
import com.aliyun.iot.ilop.demo.page.scan.AddDeviceScanPlugin;
import com.aliyun.iot.ilop.demo.utils.FloatWindowHelper;
import com.aliyun.iot.aep.sdk.login.LoginBusiness;
import com.pgyersdk.crash.PgyCrashManager;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;

import bc.juhao.com.R;
import bc.juhao.com.cons.Constance;
import bc.juhao.com.ui.activity.user.LoginActivity;
import bc.juhao.com.ui.fragment.ItApplicationFragment;
import bc.juhao.com.ui.fragment.ItHomeMainFragment;
import bc.juhao.com.ui.fragment.ItMineMainFragment;
import bc.juhao.com.utils.MyShare;
import bocang.utils.MyToast;

public class MainActivity extends FragmentActivity {
    private String TAG = MainActivity.class.getSimpleName();

    private MyFragmentTabLayout fragmentTabHost;

    private Class fragmentClass[] = {ItHomeMainFragment.class, ItApplicationFragment.class, ItMineMainFragment.class};
    private String textViewArray[] = {"首页", "智能", "个人中心"};
    private Integer drawables[] = {R.drawable.tab_home_btn, R.drawable.tab_view_btn, R.drawable.tab_mine_btn};
    private LinearLayout ll_home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FloatWindowHelper helper = FloatWindowHelper.getInstance(getApplication());
        if (helper != null) {
            helper.setNeedShowFloatWindowFlag(false);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setStatuTextColor(this, Color.WHITE);
        setFullScreenColor(Color.TRANSPARENT,this);
        DemoApplication.activityList=new ArrayList<>();
        DemoApplication.activityList.add(this);
        ll_home = findViewById(R.id.ll_home);
        fragmentTabHost = (MyFragmentTabLayout) findViewById(R.id.tab_layout);
        fragmentTabHost.init(getSupportFragmentManager())
                .setFragmentTabLayoutAdapter(new DefaultFragmentTabAdapter(Arrays.asList(fragmentClass), Arrays.asList(textViewArray), Arrays.asList(drawables)) {
                    @Override
                    public View createView(int pos) {
                        View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.tab_item, null);
                        ImageView imageView = (ImageView) view.findViewById(R.id.img);
                        imageView.setImageResource(drawables[pos]);
                        TextView textView = (TextView) view.findViewById(R.id.tab_text);
                        textView.setText(textViewArray[pos]);
                        return view;
                    }
                    @Override
                    public void onClick(int pos) {

                    }
                }).creat();
        //扫码添加设备 注册
        ScanManager.getInstance().registerPlugin(AddDeviceScanPlugin.NAME, new AddDeviceScanPlugin(this));
        initMobileConnection();
//        refreshLogin();
        initLoginStatus();
        BonePluginRegistry.register("BoneAddDeviceBiz", BoneAddDeviceBiz.class);
        BonePluginRegistry.register("BoneLocalDeviceMgr", BoneLocalDeviceMgr.class);
        BonePluginRegistry.register("BoneHotspotHelper", BoneHotspotHelper.class);

//        MobileChannel.getInstance().unBindAccount(new IMobileRequestListener() {
//            @Override
//            public void onSuccess(String s) {
//                Log.e("unBindAccount",s+"");
//                logout();
//            }
//
//            @Override
//            public void onFailure(AError aError) {
//                Log.e("unBindAccount","error"+aError.getMsg());
//                logout();
//            }
//        });
    }

    private void initMobileConnection() {
        MobileConnectConfig config = new MobileConnectConfig();
        // 设置 appKey 和 authCode(必填)
        config.appkey = DemoApplication.app_key;
        config.securityGuardAuthcode = "114d";
        // 设置验证服务器（默认不填，SDK会自动使用“API通道SDK“的Host设定）
//        config.authServer = "";
        // 指定长连接服务器地址。 （默认不填，SDK会使用默认的地址及端口。默认为国内华东节点。）
//        config.channelHost = "{长连接服务器域名}";
        // 开启动态选择Host功能。 (默认false，海外环境建议设置为true。此功能前提为ChannelHost 不特殊指定。）
        config.autoSelectChannelHost = false;
        MobileChannel.getInstance().startConnect(this, config, new IMobileConnectListener() {
            @Override
            public void onConnectStateChange(MobileConnectState state) {
//                Log.e(TAG,"onConnectStateChange(), state = "+state.toString());
//                PgyCrashManager.reportCaughtException(MainActivity.this,new Exception("MobileChannel_state,"+state.toString()));
//                if(state.equals(MobileConnectState.CONNECTED)){

//                    initLoginStatus();
//                }
//                if(LoginBusiness.isLogin()&&(state.equals(MobileConnectState.CONNECTED)||state.equals(MobileConnectState.CONNECTING))){
//                    bindmqtt();
//                    initLoginStatus();
//                }
            }
        });
    }
    private void bindmqtt() {
        IoTCredentialManage ioTCredentialManage= IoTCredentialManageImpl.getInstance(DemoApplication.getInstance());

        MobileChannel.getInstance().bindAccount(ioTCredentialManage.getIoTToken(), new IMobileRequestListener() {
            @Override
            public void onSuccess(String jsonData) {
                Log.e(TAG,"bindAccount,onSuccess"+jsonData+"");
            }

            @Override
            public void onFailure(AError error) {
                Log.e(TAG,"bindAccount,onFailure,error");
            }
        });
    }
    private void refreshLogin() {
        WebView webview=new WebView(this);
        webview.setTag("wv");
        webview.setVisibility(View.GONE);
        for(int i=0;i<ll_home.getChildCount();i++){
            if(ll_home.getChildAt(i).getTag()!=null&&ll_home.getChildAt(i).getTag().equals("wv")){
                ll_home.removeViewAt(i);
            }
        }
        ll_home.addView(webview);
        WebSettings settings=webview.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setPluginState(WebSettings.PluginState.ON);
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        settings.setAllowFileAccess(true);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setDatabaseEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setGeolocationEnabled(true);
        settings.setAppCacheEnabled(true);
        webview.setWebViewClient(new WebViewClient(){
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                Log.e("shouldOverrideUrlurl", String.valueOf(request.getUrl()));
                return super.shouldOverrideUrlLoading(view, request);
            }

            @Override
            public void onLoadResource(WebView view, String url) {
                Log.e("onLoadResource url", url);
                super.onLoadResource(view, url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if(url.contains("callback&response_type=code&scope=")){

                }else {
                    Log.e("pagefinish",url);
                    final String code=url.substring(url.indexOf("code=")+5);
                    Log.e("code",code);
                    Message msg=new Message();
                    msg.obj=code;
                    handler.sendMessage(msg);
                }

//                if(isLogin){
//                }else {
//                login(code);
//                }
//                login("");
            }
        });

        String username= MyShare.get(this).getString(Constance.username);
        String pwd=MyShare.get(this).getString(Constance.pwd);
        if(TextUtils.isEmpty(username)||TextUtils.isEmpty(pwd)){
            MyToast.show(this,"登录状态失效");
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }
        webview.loadUrl("http://smart.juhao.com/login?username="+username+"&password="+pwd);
    }
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String code= (String) msg.obj;
            login(code);
        }
    };
    private void initLoginStatus() {
        IoTCredentialManageImpl ioTCredentialManage =  IoTCredentialManageImpl.getInstance(DemoApplication.getInstance());
        if(TextUtils.isEmpty(ioTCredentialManage.getIoTToken())){
            ioTCredentialManage.asyncRefreshIoTCredential(new IoTCredentialListener() {
                @Override
                public void onRefreshIoTCredentialSuccess(IoTCredentialData ioTCredentialData) {
                    MobileChannel.getInstance().bindAccount(ioTCredentialData.iotToken, new IMobileRequestListener() {
                        @Override
                        public void onSuccess(String s) {
//                            PgyCrashManager.reportCaughtException(MainActivity.this,new Exception("onRefreshIoTCredentialSuccess_bindAccount success"));
                            Log.e(TAG,"mqtt bindAccount onSuccess");
                        }

                        @Override
                        public void onFailure(AError aError) {
                            PgyCrashManager.reportCaughtException(MainActivity.this,new Exception("onRefreshIoTCredentialSuccess_bindAccount faliure"));
                            Log.e(TAG,"mqtt bindAccount onFailure aError = " + aError.getMsg());
//                            refreshLogin();
                            Intent intent=new Intent(MainActivity.this,IndexActivity.class);
                            intent.putExtra(Constance.islogin,true);
                            startActivity(intent);
                            finish();

                        }
                    });
                }

                @Override
                public void onRefreshIoTCredentialFailed(IoTCredentialManageError ioTCredentialManageError) {
                    Log.e(TAG,"mqtt bindAccount onFailure ");
//                    PgyCrashManager.reportCaughtException(MainActivity.this,new Exception("onRefreshIoTCredentialFailed"));
//                    refreshLogin();
                    Intent intent=new Intent(MainActivity.this,IndexActivity.class);
                    intent.putExtra(Constance.islogin,true);
                    startActivity(intent);
                    finish();
                }
            });
        } else {
            MobileChannel.getInstance().bindAccount(ioTCredentialManage.getIoTToken(), new IMobileRequestListener() {
                @Override
                public void onSuccess(String s) {
//                    PgyCrashManager.reportCaughtException(MainActivity.this,new Exception("Main_bindAccount_onsuccess"));
                    Log.e(TAG,"mqtt bindAccount onSuccess ");
                }

                @Override
                public void onFailure(AError aError) {
                    Log.e(TAG,"mqtt bindAccount onFailure aError = " + aError.getMsg());
//                    refreshLogin();
                    PgyCrashManager.reportCaughtException(MainActivity.this,new Exception("Main_bindAccount_onfailure"));
                    Intent intent=new Intent(MainActivity.this,IndexActivity.class);
                    intent.putExtra(Constance.islogin,true);
                    startActivity(intent);
                    finish();

                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        //退出首页不显示浮窗
//        FloatWindowHelper helper = FloatWindowHelper.getInstance(getApplication());
//        if (helper != null) {
//            helper.setNeedShowFloatWindowFlag(false);
//        }
        bc.juhao.com.ui.activity.MainActivity.mFragmentPosition=0;
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        if (!LoginBusiness.isLogin()) {
//            Intent intent = new Intent(getApplicationContext(), IndexActivity.class);
//            startActivity(intent);
//            finish();
//        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            Log.d(TAG, "onActivityResult");
            if (data!=null&&data.getStringExtra("productKey") != null){
                Bundle bundle = new Bundle();
                bundle.putString("productKey", data.getStringExtra("productKey"));
                bundle.putString("deviceName", data.getStringExtra("deviceName"));
                bundle.putString("token", data.getStringExtra("token"));
                Intent intent = new Intent(this, BindAndUseActivity.class);
                intent.putExtras(bundle);
                this.startActivity(intent);
            }
        }
    }
    public void goBack(View v){
        finish();
    }

    private void login(final String authCode) {
        OpenAccountService service = OpenAccountSDK.getService(OpenAccountService.class);
        service.authCodeLogin(MainActivity.this, authCode, new LoginCallback() {
            @Override
            public void onSuccess(OpenAccountSession openAccountSession) {

                IoTCredentialManageImpl ioTCredentialManage =  IoTCredentialManageImpl.getInstance(DemoApplication.getInstance());
                if(TextUtils.isEmpty(ioTCredentialManage.getIoTToken())){
                    ioTCredentialManage.asyncRefreshIoTCredential(new IoTCredentialListener() {
                        @Override
                        public void onRefreshIoTCredentialSuccess(IoTCredentialData ioTCredentialData) {
                            MobileChannel.getInstance().bindAccount(ioTCredentialData.iotToken, new IMobileRequestListener() {
                                @Override
                                public void onSuccess(String s) {
                                    Log.e(TAG,"mqtt bindAccount onSuccess");
                                }

                                @Override
                                public void onFailure(AError aError) {
                                    Log.e(TAG,"mqtt bindAccount onFailure aError = " + aError.getMsg());
                                }
                            });
                        }

                        @Override
                        public void onRefreshIoTCredentialFailed(IoTCredentialManageError ioTCredentialManageError) {
                            Log.e(TAG,"mqtt onRefreshIoTCredentialFailed ");

                        }
                    });
                } else {
                    MobileChannel.getInstance().bindAccount(ioTCredentialManage.getIoTToken(), new IMobileRequestListener() {
                        @Override
                        public void onSuccess(String s) {
                            Log.e(TAG,"mqtt bindAccount onSuccess ");
                        }

                        @Override
                        public void onFailure(AError aError) {
                            Log.e(TAG,"mqtt bindAccount onFailure aError = " + aError.getMsg());

                        }
                    });
                }
//                            //注册虚拟设备
//                            String[] pks = {"a1nbd4BjB3N", "a186j8fot9K", "a1XKEJTOkPL", "a1aJCduQG0p"};
//                            for (String pk : pks) {
//                                registerVirtualDevice(pk);
//                            }
                mH.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Router.getInstance().toUrl(MainActivity.this, "page/ilopmain");
                        finish();
                    }
                }, 0);
            }

            @Override
            public void onFailure(int code, String msg) {
                MyToast.show(MainActivity.this,"登录状态失效");
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
                return;
//                            Toast.makeText(getApplicationContext(), "auth授权登录 失败 code = " + code + " message = " + msg, Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void logout() {
        OpenAccountService openAccountService = OpenAccountSDK.getService(OpenAccountService.class);

        try {
            openAccountService.logout(this, new LogoutCallback() {
                @Override
                public void onSuccess() {
//                    ToastUtil.show(getApplicationContext(), "登出成功");
                    Log.e("logout","登出成功");
                    IoTCredentialManageImpl.getInstance(DemoApplication.getInstance()).clearIoTTokenInfo();
                    refreshLogin();
                }

                @Override
                public void onFailure(int i, String s) {
//                    ToastUtil.show(getApplicationContext(), "登出失败 : " + s);
                    Log.e("logout","登出失败");
                    refreshLogin();
                }
            });
        } catch (Exception e) {
            refreshLogin();
            Log.e("logout","登出异常");
//            ToastUtil.show(getApplicationContext(), "登出异常 : " + e.toString());
        }
    }
    Handler mH=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    public void setStatuTextColor(Activity activity, int color) {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            //取消状态栏透明
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //添加Flag把状态栏设为可绘制模式
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            //设置状态栏颜色
            window.setStatusBarColor(color);
            //设置系统状态栏处于可见状态
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M) {
                //设置状态栏文字颜色及图标为深色
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
        }
    }
    public static void setFullScreenColor(int color,Activity activity){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            // 设置状态栏透明
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            // 生成一个状态栏大小的矩形
//            View statusView = createStatusView(activity, color);
            // 添加 statusView 到布局中
//            ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
//            decorView.addView(statusView);
            // 设置根布局的参数
            ViewGroup rootView = (ViewGroup) ((ViewGroup) activity.findViewById(android.R.id.content)).getChildAt(0);
//            rootView.setFitsSystemWindows(true);
//            rootView.setClipToPadding(true);
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
                try {
                    Class decorViewClazz = Class.forName("com.android.internal.policy.DecorView");
                    Field field = decorViewClazz.getDeclaredField("mSemiTransparentStatusBarColor");
                    field.setAccessible(true);
                    field.setInt(activity.getWindow().getDecorView(), Color.TRANSPARENT);  //改为透明
                } catch (Exception e) {}
            }
        }
    }
}
