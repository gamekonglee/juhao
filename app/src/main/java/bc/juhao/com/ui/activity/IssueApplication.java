package bc.juhao.com.ui.activity;

import android.app.Activity;
import android.app.Application;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.multidex.MultiDex;

import com.alibaba.sdk.android.openaccount.OpenAccountSDK;
import com.alibaba.sdk.android.openaccount.callback.LoginCallback;
import com.alibaba.sdk.android.openaccount.model.OpenAccountSession;
import com.alibaba.sdk.android.openaccount.ui.OpenAccountUIService;
import com.aliyun.alink.linksdk.tools.ALog;
import com.aliyun.alink.linksdk.tools.ThreadTools;
import com.aliyun.iot.aep.component.router.IUrlHandler;
import com.aliyun.iot.aep.component.scan.ScanManager;
import com.aliyun.iot.aep.routerexternal.RouterExternal;
import com.aliyun.iot.aep.sdk.EnvConfigure;
import com.aliyun.iot.aep.sdk.PushManager;
import com.aliyun.iot.aep.sdk.base.delegate.APIGatewaySDKDelegate;
import com.aliyun.iot.aep.sdk.delegate.DownstreamConnectorSDKDelegate;
import com.aliyun.iot.aep.sdk.delegate.RNContainerComponentDelegate;
import com.aliyun.iot.aep.sdk.framework.bundle.BundleManager;
import com.aliyun.iot.aep.sdk.framework.bundle.IBundleRegister;
import com.aliyun.iot.aep.sdk.framework.bundle.PageConfigure;
import com.aliyun.iot.ilop.ApplicationHelper;
import com.aliyun.iot.ilop.demo.base.OpenAccountSDKDelegate;
import com.aliyun.iot.ilop.demo.page.scan.BoneMobileScanPlugin;
import com.aliyun.iot.ilop.page.scan.ScanPageInitHelper;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.pgyersdk.crash.PgyCrashManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;

import bc.juhao.com.R;
import bc.juhao.com.bean.CommentBean;
import bc.juhao.com.chat.DemoHelper;
import bc.juhao.com.chat.cache.ACache;
import bc.juhao.com.utils.ImageLoadProxy;
import bocang.json.JSONArray;
import bocang.json.JSONObject;
import bocang.utils.MyToast;
import bocang.view.BaseApplication;
import cn.jiguang.analytics.android.api.JAnalyticsInterface;
import cn.jpush.android.api.JPushInterface;

/**
 * @author Jun
 * @time 2017/1/6  16:06
 * @desc 全局
 */
public class IssueApplication extends Application {
    public static boolean hasBitmap;
//    protected static Context mContext = null;
//    public static String app_key="24940076";
//    ACache mACache = null;
//
//    public static JSONObject UserInfo;
//    public static int unreadMsgCount;
//    private static ArrayList<CommentBean> commentList;
//    private static DisplayImageOptions options;
//    private static DisplayImageOptions defaultOptions;
//    private static ImageLoaderConfiguration config;
//
//    public static void setCommentList(ArrayList<CommentBean> commentList) {
//        IssueApplication.commentList = commentList;
//    }
//
//    public static ArrayList<CommentBean> getCommentList() {
//        return commentList;
//    }
//
//    @Override
//    public void onCreate() {
//        MultiDex.install(this);
//        super.onCreate();
//        mACache = ACache.get(this);
//        mContext= getApplicationContext();
//        super.mInstance = this;
//        SDKInitializer.initialize(mContext);
//        initImageLoader();
//        ImageLoadProxy.initImageLoader(mContext);
//
//        JPushInterface.setDebugMode(false); 	// 设置开启日志,发布时请关闭日志
//        JPushInterface.init(this);     		// 初始化 JPush
//
//        JAnalyticsInterface.init(mContext);
////        JAnalyticsInterface.initCrashHandler(mContext);
//
//        DemoHelper.getInstance().init(mContext);
//        PgyCrashManager.register(this);
//
//        // Push SDK 需要在主进程和子进程都初始化
////        PushManager.getInstance().init(this);
//
//        // Push SDK 需要在主进程和子进程都初始化
////        PushManager.getInstance().init(this);
//
//        // 其他 SDK, 仅在 主进程上初始化
//        String packageName = this.getPackageName();
//        if (!packageName.equals(ThreadTools.getProcessName(this, android.os.Process.myPid()))) {
//            return;
//        }
//
//        // set env for sdks .begin
//        // api gateway
//        EnvConfigure.putEnvArg(APIGatewaySDKDelegate.ENV_KEY_API_CLIENT_API_ENV, "RELEASE");
//        EnvConfigure.putEnvArg(APIGatewaySDKDelegate.ENV_KEY_API_CLIENT_DEFAULT_HOST, "api.link.aliyun.com");
//        // OA
//        EnvConfigure.putEnvArg(OpenAccountSDKDelegate.ENV_KEY_OPEN_ACCOUNT_HOST, null);
//        // MQTT
//        EnvConfigure.putEnvArg(DownstreamConnectorSDKDelegate.ENV_KEY_MQTT_HOST, null);
//        EnvConfigure.putEnvArg(DownstreamConnectorSDKDelegate.ENV_KEY_MQTT_AUTO_HOST, "false");
//        EnvConfigure.putEnvArg(DownstreamConnectorSDKDelegate.ENV_KEY_MQTT_CHECK_ROOT_CRT, "true");
//        // set env for sdks .end
//
//        EnvConfigure.putEnvArg(RNContainerComponentDelegate.KEY_RN_CONTAINER_PLUGIN_ENV, "release");
//        EnvConfigure.putEnvArg(EnvConfigure.KEY_LANGUAGE, "zh-CN");
//
//        // the key set from sp that need to be put into AConfigure.envArgs
//        HashSet spKeySet = new HashSet();
//        EnvConfigure.init(this, spKeySet);
////
//        new ApplicationHelper().onCreate(this);
////
////        /* 加载Native页面 */
//        BundleManager.init(this, new IBundleRegister() {
//            @Override
//            public void registerPage(Application application, PageConfigure configure) {
//                if (null == configure || null == configure.navigationConfigures)
//                    return;
//
//                ArrayList<String> nativeUrls = new ArrayList<>();
//                ArrayList<PageConfigure.NavigationConfigure> configures = new ArrayList<>();
//
//                PageConfigure.NavigationConfigure deepCopyItem = null;
//                for (PageConfigure.NavigationConfigure item : configure.navigationConfigures) {
//                    if (null == item.navigationCode || item.navigationCode.isEmpty() || null == item.navigationIntentUrl || item.navigationIntentUrl.isEmpty())
//                        continue;
//
//                    deepCopyItem = new PageConfigure.NavigationConfigure();
//                    deepCopyItem.navigationCode = item.navigationCode;
//                    deepCopyItem.navigationIntentUrl = item.navigationIntentUrl;
//                    deepCopyItem.navigationIntentAction = item.navigationIntentAction;
//                    deepCopyItem.navigationIntentCategory = item.navigationIntentCategory;
//                    configures.add(deepCopyItem);
//                    nativeUrls.add(deepCopyItem.navigationIntentUrl);
////                    com.aliyun.iot.aep.sdk.log.ALog.d("BundleManager", "register-native-page: " + item.navigationCode + ", " + item.navigationIntentUrl);
//                    RouterExternal.getInstance().registerNativeCodeUrl(deepCopyItem.navigationCode, deepCopyItem.navigationIntentUrl);
//                    RouterExternal.getInstance().registerNativePages(nativeUrls, new NativeUrlHandler(deepCopyItem));
//                }
//            }
//        });
//
//        // 支持扫码调试
//        ScanManager.getInstance().registerPlugin("boneMobile", new BoneMobileScanPlugin());
//        //初始化pagescan页面的router配置
//        ScanPageInitHelper.initPageScanRouterConfig();
//
//    }
//    public void IotDemoInit(){
//        // Push SDK 需要在主进程和子进程都初始化
//        PushManager.getInstance().init(this);
//
//        // 其他 SDK, 仅在 主进程上初始化
//        String packageName = this.getPackageName();
//        if (!packageName.equals(ThreadTools.getProcessName(this, android.os.Process.myPid()))) {
//            return;
//        }
//
//        // set env for sdks .begin
//        // api gateway
//        EnvConfigure.putEnvArg(APIGatewaySDKDelegate.ENV_KEY_API_CLIENT_API_ENV, "RELEASE");
//        EnvConfigure.putEnvArg(APIGatewaySDKDelegate.ENV_KEY_API_CLIENT_DEFAULT_HOST, "api.link.aliyun.com");
//        // OA
//        EnvConfigure.putEnvArg(OpenAccountSDKDelegate.ENV_KEY_OPEN_ACCOUNT_HOST, null);
//        // MQTT
//        EnvConfigure.putEnvArg(DownstreamConnectorSDKDelegate.ENV_KEY_MQTT_HOST, null);
//        EnvConfigure.putEnvArg(DownstreamConnectorSDKDelegate.ENV_KEY_MQTT_AUTO_HOST, "false");
//        EnvConfigure.putEnvArg(DownstreamConnectorSDKDelegate.ENV_KEY_MQTT_CHECK_ROOT_CRT, "true");
//        // set env for sdks .end
//
//        EnvConfigure.putEnvArg(RNContainerComponentDelegate.KEY_RN_CONTAINER_PLUGIN_ENV, "release");
//        EnvConfigure.putEnvArg(EnvConfigure.KEY_LANGUAGE, "zh-CN");
//
//        // the key set from sp that need to be put into AConfigure.envArgs
//        HashSet spKeySet = new HashSet();
//        EnvConfigure.init(this, spKeySet);
//
//        new ApplicationHelper().onCreate(IssueApplication.this);
//
//        /* 加载Native页面 */
//        BundleManager.init(this, new IBundleRegister() {
//            @Override
//            public void registerPage(Application application, PageConfigure configure) {
//                if (null == configure || null == configure.navigationConfigures)
//                    return;
//
//                ArrayList<String> nativeUrls = new ArrayList<>();
//                ArrayList<PageConfigure.NavigationConfigure> configures = new ArrayList<>();
//
//                PageConfigure.NavigationConfigure deepCopyItem = null;
//                for (PageConfigure.NavigationConfigure item : configure.navigationConfigures) {
//                    if (null == item.navigationCode || item.navigationCode.isEmpty() || null == item.navigationIntentUrl || item.navigationIntentUrl.isEmpty())
//                        continue;
//
//                    deepCopyItem = new PageConfigure.NavigationConfigure();
//                    deepCopyItem.navigationCode = item.navigationCode;
//                    deepCopyItem.navigationIntentUrl = item.navigationIntentUrl;
//                    deepCopyItem.navigationIntentAction = item.navigationIntentAction;
//                    deepCopyItem.navigationIntentCategory = item.navigationIntentCategory;
//
//                    configures.add(deepCopyItem);
//
//                    nativeUrls.add(deepCopyItem.navigationIntentUrl);
//
//                    ALog.d("BundleManager", "register-native-page: " + item.navigationCode + ", " + item.navigationIntentUrl);
//
//                    RouterExternal.getInstance().registerNativeCodeUrl(deepCopyItem.navigationCode, deepCopyItem.navigationIntentUrl);
//                    RouterExternal.getInstance().registerNativePages(nativeUrls, new NativeUrlHandler(deepCopyItem));
//                }
//            }
//        });
//
//        // 支持扫码调试
//        ScanManager.getInstance().registerPlugin("boneMobile", new BoneMobileScanPlugin());
//
//        //初始化pagescan页面的router配置
//        ScanPageInitHelper.initPageScanRouterConfig();
//    }
//    public void login(String authCode) {
//        OpenAccountUIService openAccountService = OpenAccountSDK.getService(OpenAccountUIService.class);
//        try {
//            openAccountService.showLogin(IssueApplication.this, new LoginCallback() {
//                @Override
//                public void onSuccess(OpenAccountSession openAccountSession) {
//                    MyToast.show(getApplicationContext(), "登录成功");
//                }
//
//                @Override
//                public void onFailure(int i, String s) {
//                    MyToast.show(getApplicationContext(), "登录失败");
//                }
//            });
//        } catch (Exception e) {
//            MyToast.show(getApplicationContext(), "登录异常 : " + e.toString());
//        }
////        OpenAccountService service = OpenAccountSDK.getService(OpenAccountService.class);
//
////        service.authCodeLogin(IssueApplication.this, authCode, new LoginCallback() {
////            @Override
////            public void onSuccess(OpenAccountSession openAccountSession) {
////                Toast.makeText(getApplicationContext(), "auth 授权登录 成功  ", Toast.LENGTH_SHORT).show();
////            }
////
////            @Override
////            public void onFailure(int code, String msg) {
////                Log.e("authCodeLogin", "auth授权登录 失败 code = " + code + " message = " + msg);
//////                Toast.makeText(getApplicationContext(), "auth授权登录 失败 code = " + code + " message = " + msg, Toast.LENGTH_SHORT).show();
////            }
////        });
//    }
//
//    public  static DisplayImageOptions getImageLoaderOption() {
//       if(defaultOptions==null) {
//           defaultOptions = new DisplayImageOptions.Builder().bitmapConfig(Bitmap.Config.RGB_565)
//                   .cacheInMemory(true).imageScaleType(ImageScaleType.EXACTLY)
//                   .cacheOnDisk(true).build();
//       }
//        // default
//// .memoryCache(new LruMemoryCache((int) (6 * 1024 * 1024)))
//// Remove
//        if(config==null){
//           config = new ImageLoaderConfiguration.Builder(
//                    mContext)
//                    .threadPoolSize(4)
//                    .threadPriority(Thread.NORM_PRIORITY - 2)
//                    .denyCacheImageMultipleSizesInMemory()
//                    .diskCacheFileNameGenerator(new Md5FileNameGenerator())
//                    .tasksProcessingOrder(QueueProcessingType.LIFO)
//                    .denyCacheImageMultipleSizesInMemory()
//// .memoryCache(new LruMemoryCache((int) (6 * 1024 * 1024)))
//                    .memoryCache(new LruMemoryCache(5 * 1024 * 1024))
//                    .memoryCacheSize((int) (2 * 1024 * 1024))
//                    .memoryCacheSizePercentage(25)
//                    .diskCacheSize(50 * 1024 * 1024).diskCacheFileCount(200)
//                    .diskCacheFileNameGenerator(new HashCodeFileNameGenerator())
//                    .defaultDisplayImageOptions(defaultOptions).writeDebugLogs() // Remove
//                    .build();
//        }
//// Initialize ImageLoader with configuration.
//        ImageLoader.getInstance().init(config);
//
//        if(options==null){
//            options = new DisplayImageOptions.Builder()
//                    .showImageOnLoading(R.drawable.bg_default) // resource or
//                    // drawable
//                    .showImageForEmptyUri(R.drawable.bg_default) // resource or
//                    // drawable
//                    .showImageOnFail(R.drawable.bg_default) // resource or
//                    .cacheInMemory(true)// drawable
//                    .resetViewBeforeLoading(false) // default
//                    .delayBeforeLoading(1000).cacheInMemory(true) // default
//                    .cacheOnDisk(true) // default
//                    .considerExifParams(false) // default
//                    .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2) // default
//                    .bitmapConfig(Bitmap.Config.RGB_565) // default
//                    .displayer(new SimpleBitmapDisplayer()) // default
//                    .handler(new Handler()) // default
//                    .build();
//        }
//        return options;
//    }
//
//    @Override
//    protected void attachBaseContext(Context base) {
//        super.attachBaseContext(base);
//        MultiDex.install(this);
//    }
//
//
//    private class MyExceptionHander implements Thread.UncaughtExceptionHandler {
//        @Override
//        public void uncaughtException(Thread thread, Throwable ex) {
//            // Logger.i("MobileSafeApplication", "发生了异常,但是被哥捕获了..");
//            //            LogUtils.d("MobileSafeApplication","发生了异常,但是被哥捕获了..");
//            //并不能把异常消灭掉,只是在应用程序关掉前,来一个留遗嘱的事件
//            //获取手机硬件信息
//            try {
//                Field[] fields = Build.class.getDeclaredFields();
//                StringBuffer sb = new StringBuffer();
//                for(Field field:fields){
//                    String value = field.get(null).toString();
//                    String name  = field.getName();
//                    sb.append(name);
//                    sb.append(":");
//                    sb.append(value);
//                    sb.append("\n");
//                }
//                File file=new File(getFilesDir(),"error.log");
//                FileOutputStream out = new FileOutputStream(file);
//                StringWriter wr = new StringWriter();
//                PrintWriter err = new PrintWriter(wr);
//                //获取错误信息
//                ex.printStackTrace(err);
//                String errorlog = wr.toString();
//                sb.append(errorlog);
//                out.write(sb.toString().getBytes());
//                out.flush();
//                out.close();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            //杀死页面进程
//            restartApp();
//
//        }
//    }
//
//    public void restartApp(){
//        Intent intent = new Intent(mContext,MainActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        mContext.startActivity(intent);
//        android.os.Process.killProcess(android.os.Process.myPid());  //结束进程之前可以把你程序的注销或者退出代码放在这段代码之前
//    }
//
//
//    /**
//     * 获得全局上下文
//     * @return
//     */
//    public static Context getcontext() {
//        return mContext;
//    }
//
//    //初始化网络图片缓存库
//    private void initImageLoader(){
//        //网络图片例子,结合常用的图片缓存库UIL,你可以根据自己需求自己换其他网络图片库
//        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
//                .cacheInMemory(true).cacheOnDisk(true).build();
//
//        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
//                getApplicationContext()).defaultDisplayImageOptions(defaultOptions)
//                .threadPriority(Thread.NORM_PRIORITY - 2)
//                .denyCacheImageMultipleSizesInMemory()
//                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
//                .tasksProcessingOrder(QueueProcessingType.LIFO).build();
//        ImageLoader.getInstance().init(config);
//    }
//
//
//
//
//    /**
//     * 返回桌面
//     */
//    public void toDesktop(){
//        Intent home = new Intent(Intent.ACTION_MAIN);
//        home.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        //        home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        home.addCategory(Intent.CATEGORY_HOME);
//        startActivity(home);
//    }
//
//    public  static String mCId="";
//
//    public static JSONObject mUserObject;
//
//    public  static String imagePath="";
//
//    public  static File cameraPath;
//
//    public static  boolean isClassify=false;
//
//    public static int mCartCount=0;
//
//    public static int mLightIndex = 0;//点出来的灯的序号
//
//    public  static boolean isGoProgramme=false;
//
//    public static JSONArray mSelectProducts=new JSONArray();
//
//    public static JSONArray mSelectScreens=new JSONArray();
//    public static final String ACTION_REMOVE_SHORTCUT = "com.android.launcher.action.UNINSTALL_SHORTCUT";
//
//    /**
//     * help class
//     */
//    static final private class NativeUrlHandler implements IUrlHandler {
//
//        private final String TAG = "ApplicationHelper$NativeUrlHandler";
//
//        private final PageConfigure.NavigationConfigure navigationConfigure;
//
//        NativeUrlHandler(PageConfigure.NavigationConfigure configures) {
//            this.navigationConfigure = configures;
//        }
//
//        @Override
//        public void onUrlHandle(Context context, String url, Bundle bundle, boolean startActForResult, int reqCode) {
//            ALog.d(TAG, "onUrlHandle: url: " + url);
//            if (null == context || null == url || url.isEmpty())
//                return;
//
//            /* prepare the intent */
//            Intent intent = new Intent();
//            intent.setData(Uri.parse(url));
//
//            if (null != this.navigationConfigure.navigationIntentAction)
//                intent.setAction(this.navigationConfigure.navigationIntentAction);
//            if (null != this.navigationConfigure.navigationIntentCategory)
//                intent.addCategory(this.navigationConfigure.navigationIntentCategory);
//
//            /* start the navigated activity */
//            ALog.d(TAG, "startActivity(): url: " + this.navigationConfigure.navigationIntentUrl + ", startActForResult: " + startActForResult + ", reqCode: " + reqCode);
//            this.startActivity(context, intent, bundle, startActForResult, reqCode);}
//
//        private void startActivity(Context context, Intent intent, Bundle bundle, boolean startActForResult, int reqCode) {
//            if (null == context || null == intent)
//                return;
//
//
//            if (null != bundle) {
//                intent.putExtras(bundle);
//            }
//            /* startActivityForResult() 场景，只能被 Activity 调用 */
//            if (startActForResult) {
//                if (false == (context instanceof Activity))
//                    return;
//
//                ((Activity) context).startActivityForResult(intent, reqCode);
//
//                return;
//            }
//
//            /* startActivity 被 Application 调用时的处理 */
//            if (context instanceof Application) {
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                context.startActivity(intent);
//            }
//            /* startActivity 被 Activity、Service 调用时的处理 */
//            else if (context instanceof Activity || context instanceof Service) {
//                context.startActivity(intent);
//            }
//            /* startActivity 被其他组件调用时的处理 */
//            else {
//                // 暂不支持
//            }
//        }
//        }

}
