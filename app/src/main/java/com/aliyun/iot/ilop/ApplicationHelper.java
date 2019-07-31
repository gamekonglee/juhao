package com.aliyun.iot.ilop;

import java.util.Iterator;
import java.util.List;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import com.aliyun.iot.aep.sdk.EnvConfigure;
import com.aliyun.iot.aep.sdk.base.delegate.APIGatewaySDKDelegate;
import com.aliyun.iot.aep.sdk.base.delegate.OpenAccountSDKDelegate;
import com.aliyun.iot.aep.sdk.framework.AApplication;
import com.aliyun.iot.aep.sdk.framework.sdk.SDKConfigure;
import com.aliyun.iot.aep.sdk.framework.sdk.SDKManager;
import com.aliyun.iot.aep.sdk.log.ALog;

/**
 * Created by wuwang on 2017/10/30.
 */
public class ApplicationHelper {

    public void onCreate(AApplication application) {
        String packageName = application.getPackageName();

        // SDK 仅在主进程初始化, 多进程初始化可能出现问题，例如 长连接 SDK
        if (!packageName.equals(getProcessName(application, android.os.Process.myPid()))) {
            return;
        }

        this.init(application);
    }

    /* methods: helper init */
    private void init(AApplication application) {

        initALog(application);

        /* 初始化 SDK */
        this._initBaseSdk(application);

        this._initOtherSdk(application);
    }

    private void initALog(Application application) {
//        ALog.configALog(application, "9rqKSi8gkL");
//        ALog.setLevel(ALog.LEVEL_ERROR);
////
//        com.aliyun.alink.linksdk.tools.ALog.setLevel(com.aliyun.alink.linksdk.tools.ALog.LEVEL_ERROR);
}

    private void _initBaseSdk(AApplication application) {
        SDKManager.Result result = null;

        // API网关
        {
            SDKConfigure configure = new SDKConfigure("API-Client", "0.0.1", APIGatewaySDKDelegate.class.getName());
            result = new SDKManager.Result();
            result.sdkName = "APIGateway";
            result.sdkVer = "*";
            result.bInitialized = true;
            result.resultCode = new APIGatewaySDKDelegate().init(application, configure, EnvConfigure.envArgs);
            SDKManager.InitResultHolder.updateResult(APIGatewaySDKDelegate.class.getName(), result);
        }

        // 帐号初始化
        {
            SDKConfigure configure = new SDKConfigure("OpenAccount", "0.0.1", APIGatewaySDKDelegate.class.getName());

            result = new SDKManager.Result();
            result.sdkName = "OpenAccount";
            result.sdkVer = "*";
            result.bInitialized = true;
            result.resultCode = new OpenAccountSDKDelegate().init(application, configure, EnvConfigure.envArgs);
            SDKManager.InitResultHolder.updateResult(OpenAccountSDKDelegate.class.getName(), result);
        }
    }

    private void _initOtherSdk(AApplication application) {
        SDKManager.prepareForInitSdk(application);
        SDKManager.init_outOfUiThread(application);
        SDKManager.init_underUiThread(application);
    }

    private static String getProcessName(Context context, int pid) {
        String ret = null;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (null != am) {
            List<ActivityManager.RunningAppProcessInfo> runningApps = am.getRunningAppProcesses();
            if (runningApps != null) {
                Iterator i$ = runningApps.iterator();

                while (i$.hasNext()) {
                    ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) i$.next();
                    if (info.pid == pid) {
                        ret = info.processName;
                        break;
                    }
                }
            }
        }

        return ret;
    }
}
