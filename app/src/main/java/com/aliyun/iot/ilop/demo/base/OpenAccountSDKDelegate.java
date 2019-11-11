package com.aliyun.iot.ilop.demo.base;

import android.app.Application;

import com.aliyun.iot.ilop.demo.DemoApplication;
import com.aliyun.iot.ilop.demo.base.adapter.OALoginAdapter;
import com.aliyun.iot.aep.sdk.EnvConfigure;
import com.aliyun.iot.aep.sdk.base.delegate.APIGatewaySDKDelegate;
import com.aliyun.iot.aep.sdk.framework.sdk.SDKConfigure;
import com.aliyun.iot.aep.sdk.framework.sdk.SimpleSDKDelegateImp;
import com.aliyun.iot.aep.sdk.log.ALog;
import com.aliyun.iot.aep.sdk.login.LoginBusiness;
import com.ut.mini.IUTApplication;
import com.ut.mini.UTAnalytics;
import com.ut.mini.core.sign.IUTRequestAuthentication;
import com.ut.mini.core.sign.UTSecuritySDKRequestAuthentication;
import com.ut.mini.crashhandler.IUTCrashCaughtListner;

import java.util.Map;

/**
 * Created by wuwang on 2017/10/30.
 */

public final class OpenAccountSDKDelegate extends SimpleSDKDelegateImp {
    static final private String TAG = "OpenAccountSDKDelegate";

    static final public String ENV_KEY_OPEN_ACCOUNT_HOST = "ENV_KEY_OPEN_ACCOUNT_HOST";


    /* API: ISDKDelegate */

    @Override
    public int init(Application app, SDKConfigure configure, Map<String, String> args) {
        boolean isDebug = "true".equals(args.get(EnvConfigure.KEY_IS_DEBUG));
        ALog.i(TAG, "init OpenAccount -- isDebug :" + isDebug + " env is:" + args.get(APIGatewaySDKDelegate.ENV_KEY_API_CLIENT_API_ENV));
        initUT(app, configure, args.get(EnvConfigure.KEY_APPKEY), isDebug);

        String env = args == null ? "" : args.get(APIGatewaySDKDelegate.ENV_KEY_API_CLIENT_API_ENV);
        String host = args == null ? "" : args.get(ENV_KEY_OPEN_ACCOUNT_HOST);

        //使用系统默认OA
        OALoginAdapter loginAdapter = new OALoginAdapter(app);
        loginAdapter.setDefaultOAHost(host);
        LoginBusiness.init(app, loginAdapter, true, env);

        return 0;
    }

    //// TODO: 17/11/7 暂时放在Account模块初始化，后面引入UTBusiness，再移出去
    private void initUT(Application application, final SDKConfigure sdkConfigure, final String key, final boolean isDebug) {
        UTAnalytics.getInstance().setAppApplicationInstance(application, new IUTApplication() {
            @Override
            public String getUTAppVersion() {
                return sdkConfigure.version;
            }

            @Override
            public String getUTChannel() {
                return null;
            }

            @Override
            public IUTRequestAuthentication getUTRequestAuthInstance() {
                return new UTSecuritySDKRequestAuthentication(key, null);
            }

            @Override
            public boolean isUTLogEnable() {
                return isDebug;
            }

            @Override
            public boolean isAliyunOsSystem() {
                return false;
            }

            @Override
            public IUTCrashCaughtListner getUTCrashCraughtListener() {
                return null;
            }

            @Override
            public boolean isUTCrashHandlerDisable() {
                return false;
            }
        });
    }

}
