package com.aliyun.iot.ilop.demo.page.main;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.sdk.android.push.noonesdk.PushServiceFactory;
import com.aliyun.alink.alirn.dev.BoneDevHelper;
import com.aliyun.iot.aep.component.bundlemanager.BundleManager;
import com.aliyun.iot.aep.component.router.Router;
import com.aliyun.iot.ilop.demo.dialog.ASlideDialog;
import com.aliyun.iot.ilop.demo.view.IndexItemView;
import com.aliyun.iot.aep.routerexternal.PluginConfigManager;
import com.aliyun.iot.aep.sdk.EnvConfigure;
import com.aliyun.iot.aep.sdk.framework.AActivity;
import com.aliyun.iot.aep.sdk.log.ALog;
import com.aliyun.iot.aep.sdk.login.ILoginCallback;
import com.aliyun.iot.aep.sdk.login.ILogoutCallback;
import com.aliyun.iot.aep.sdk.login.LoginBusiness;
import com.aliyun.iot.aep.sdk.login.data.UserInfo;
import com.aliyun.iot.aep.sdk.threadpool.ThreadPool;

import java.util.regex.Pattern;

import bc.juhao.com.R;


public class MainActivity extends AActivity {
    static private final String TAG = "MainActivity";

    private ASlideDialog menuDialog;

    private ImageView backImageView;

    private LocalBroadcastManager localBroadcastManager;

    private IntentFilter intentFilter;

    private LoginChangeReceiver loginChangeReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.setContentView(R.layout.debug_layout_view);
        super.onCreate(savedInstanceState);

        ((TextView) this.findViewById(R.id.topbar_title_textview)).setText(R.string.main_title_text);

        backImageView = (ImageView) findViewById(R.id.topbar_back_imageview);

        initBroadcastReceiver();

        refreshUILogo();

        ImageView leftIcon = findViewById(R.id.topbar_back_imageview);
        leftIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 左上角头像
                if (LoginBusiness.isLogin()) {
                    showMenuDialog();
                } else {
                    login();
                }
            }
        });

        initSDKList();
        checkPermission();
        String deviceId = PushServiceFactory.getCloudPushService().getDeviceId();
        EnvConfigure.putEnvArg(EnvConfigure.KEY_DEVICE_ID, deviceId);
    }

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (!Settings.canDrawOverlays(this)) {
                toast("为了更好的使用DemoAPP，请开启悬浮窗权限");
            }
        }

    }

    @Override
    protected void onDestroy() {
        localBroadcastManager.unregisterReceiver(loginChangeReceiver);
        super.onDestroy();
    }

    void initSDKList() {
        LinearLayout sdkList = (LinearLayout) findViewById(R.id.sdk_list);
        IndexItemView boneMobileSDK = new IndexItemView(this);
        boneMobileSDK.setTitle("BoneMobile 插件");
        boneMobileSDK.addAction("API列表", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //更改id， android ios保持统一 待@归空 确认
//                Router.getInstance().toUrl(v.getContext(), "a123HJmSsLEp6S1Q");
                Router.getInstance().toUrl(v.getContext(), "a123QdaCHcGejsEy");
            }
        });
        boneMobileSDK.addAction("组件列表", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //更改id， android ios保持统一 待@归空 确认
//                Router.getInstance().toUrl(v.getContext(), "a123IWPslIKdGXZm");
                Router.getInstance().toUrl(v.getContext(), "a123MtycBf2XkCUW");
            }
        });
        boneMobileSDK.addAction("环境切换", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Context context = v.getContext();

                final Dialog dialog = ASlideDialog.newInstance(context, ASlideDialog.Gravity.Bottom, R.layout.rncontainer_env_dialog);
                dialog.findViewById(R.id.rncontainer_env_release_textview).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        handleEnv(context, "release");
                        dialog.dismiss();
                    }
                });
                dialog.findViewById(R.id.rncontainer_env_test_textview).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        handleEnv(context, "test");
                        dialog.dismiss();
                    }
                });
                dialog.findViewById(R.id.menu_cancel_textview).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.setCanceledOnTouchOutside(true);
                dialog.show();

            }
        });
        boneMobileSDK.addAction("本地调试", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Context context = v.getContext();
                final String ip = BoneDevHelper.readBoneDebugServer(context);

                final Dialog dialog = ASlideDialog.newInstance(context, ASlideDialog.Gravity.Center, R.layout.rncontainer_debug_ip_dialog);

                final EditText editText = dialog.findViewById(R.id.rncontainer_debug_ip_edittext);
                editText.setText(ip);

                dialog.findViewById(R.id.rncontainer_debug_ip_ok_textview).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        handleIP(editText.getText().toString());
                        dialog.dismiss();
                    }
                });
                dialog.findViewById(R.id.rncontainer_debug_ip_cancel_textview).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.setCanceledOnTouchOutside(true);
                dialog.show();
            }
        });
        String env = "生产环境";
        if ("test".equalsIgnoreCase(BoneDevHelper.readPluginEnv(MainActivity.this, "test"))) {
            env = "开发环境";
        }
        boneMobileSDK.setStatus(env);

        IndexItemView openAccount = new IndexItemView(this);
        openAccount.setTitle("账号和用户");
        openAccount.addAction("界面展示", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Router.getInstance().toUrl(MainActivity.this, "page/login");
            }
        });


        IndexItemView apiGate = new IndexItemView(this);
        apiGate.setTitle("API 通道");
        apiGate.addAction("调试", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Router.getInstance().toUrl(MainActivity.this, "page/apiClient");
            }
        });

        IndexItemView socketSDK = new IndexItemView(this);
        socketSDK.setTitle("长连接通道");
        socketSDK.addAction("调试", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Router.getInstance().toUrl(MainActivity.this, "page/channel");
            }
        });


        IndexItemView mobilePush = new IndexItemView(this);
        mobilePush.setTitle("移动推送SDK");
        mobilePush.addAction("查看DeviceID", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String deviceId = PushServiceFactory.getCloudPushService().getDeviceId();
                if (TextUtils.isEmpty(deviceId)) {
                    deviceId = "没有获取到";
                }
                EnvConfigure.putEnvArg(EnvConfigure.KEY_DEVICE_ID, deviceId);
                Log.d(TAG, deviceId);

                Router.getInstance().toUrl(MainActivity.this, "page/about");
//                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).setMessage(deviceId).create();
//                alertDialog.show();
            }
        });

        sdkList.addView(boneMobileSDK);
        sdkList.addView(apiGate);
        sdkList.addView(socketSDK);
        sdkList.addView(openAccount);
        sdkList.addView(mobilePush);

    }

    private void login() {
        LoginBusiness.login(new ILoginCallback() {
            @Override
            public void onLoginSuccess() {
                toast(getApplication().getString(R.string.account_login_success));
                refreshUILogo();
            }

            @Override
            public void onLoginFailed(int code, String error) {
                toast(getApplication().getString(R.string.account_login_failed) + error);
            }
        });
    }

    private void initBroadcastReceiver() {
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        intentFilter = new IntentFilter();
        intentFilter.addAction(LoginBusiness.LOGIN_CHANGE_ACTION);
        loginChangeReceiver = new LoginChangeReceiver();
        localBroadcastManager.registerReceiver(loginChangeReceiver, intentFilter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        ALog.d(TAG, "onActivityResult(): requestCode: " + requestCode);
        ALog.d(TAG, "onActivityResult(): resultCode: " + resultCode);
        ALog.d(TAG, "onActivityResult(): result: " + data.getStringExtra("barCode"));
    }

    private void handleEnv(Context context, String pluginEnv) {

        if (TextUtils.equals(pluginEnv, BoneDevHelper.readPluginEnv(context, "test"))) {
            String message = "test".equalsIgnoreCase(pluginEnv) ? "当前已经是开发环境" : "当前已经是生产环境";
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            return;
        }

        BoneDevHelper.savetPluginEnv(context, pluginEnv);
        // 清空文件缓存
        new BundleManager().destroy();
        // 清空路由信息
        PluginConfigManager.getInstance().cleanConfig();

        Toast.makeText(context, "环境设置成功，应用将在3秒钟后自动关闭，请重新启动应用", Toast.LENGTH_SHORT).show();

        ThreadPool.MainThreadHandler.getInstance().post(new Runnable() {
            @Override
            public void run() {
                restart();
            }
        }, 3000);
    }

    private void handleIP(String ip) {

        // check ip
        boolean match = Pattern.matches("((?:(?:25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))\\.){3}(?:25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d))))", ip);

        if (!match) {
            Toast.makeText(this, R.string.rncontainer_illeagel_ip_address, Toast.LENGTH_SHORT).show();
            return;
        }

        BoneDevHelper.saveBoneDebugServer(this, ip);

        new BoneDevHelper().getBundleInfoAsync(this, ip, new BoneDevHelper.OnBondBundleInfoGetListener() {
            @Override
            public void onSuccess(BoneDevHelper.BoneBundleInfo boneBundleInfo) {
                BoneDevHelper.RouterInfo info = new BoneDevHelper().handleBundleInfo(MainActivity.this, boneBundleInfo);

                if (null == info) {
                    return;
                }

                Router.getInstance().toUrl(MainActivity.this, info.url, info.bundle);
            }

            @Override
            public void onError(String message, Exception e) {
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();

                if (null != e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void refreshUILogo() {
        if (!LoginBusiness.isLogin()) {
            backImageView.setImageResource(R.drawable.avatar_default_svg);
        } else {
            backImageView.setImageResource(R.drawable.avatar_login_svg);
        }
    }

    private void showMenuDialog() {
        if (menuDialog == null) {
            menuDialog = ASlideDialog.newInstance(this, ASlideDialog.Gravity.Bottom, R.layout.menu_dialog);
            menuDialog.findViewById(R.id.menu_logout_textview).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LoginBusiness.logout(new ILogoutCallback() {
                        @Override
                        public void onLogoutSuccess() {
                            toast(getApplication().getString(R.string.account_logout_success));
                            refreshUILogo();
                        }

                        @Override
                        public void onLogoutFailed(int code, String error) {
                            toast(getApplication().getString(R.string.account_logout_failed) + error);
                        }
                    });
                    hideMenuDialog();
                }
            });
            menuDialog.findViewById(R.id.menu_cancel_textview).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    hideMenuDialog();
                }
            });
            menuDialog.setCanceledOnTouchOutside(true);
        }
        //设置当前登录用户名
        if (LoginBusiness.isLogin()) {
            UserInfo userInfo = LoginBusiness.getUserInfo();
            String userName = "";
            if (userInfo != null) {
                userName = userInfo.userNick;
                if (TextUtils.isEmpty(userName)) {
                    userName = userInfo.userPhone;
                    if (TextUtils.isEmpty(userName)) {
                        userName = "未获取到用户名";
                    }
                }
                ((TextView) menuDialog.findViewById(R.id.menu_name_textview)).setText(userName);
            }

        }

        menuDialog.show();
    }

    private void hideMenuDialog() {
        if (menuDialog != null) {
            menuDialog.hide();
        }
    }

    private void toast(final String toast) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), toast, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private class LoginChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            refreshUILogo();
        }
    }

    private void restart() {
        Intent mStartActivity = new Intent(this, MainActivity.class);
        int mPendingIntentId = 123456;
        PendingIntent mPendingIntent = PendingIntent.getActivity(this, mPendingIntentId, mStartActivity, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager mgr = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        mgr.setExact(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);
        System.exit(0);
    }
}
