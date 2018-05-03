package bc.juhao.com.ui.activity;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.multidex.MultiDex;

import com.baidu.mapapi.SDKInitializer;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
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

import bc.juhao.com.R;
import bc.juhao.com.bean.CommentBean;
import bc.juhao.com.chat.DemoHelper;
import bc.juhao.com.chat.cache.ACache;
import bc.juhao.com.utils.ImageLoadProxy;
import bocang.json.JSONArray;
import bocang.json.JSONObject;
import bocang.view.BaseApplication;
import cn.jiguang.analytics.android.api.JAnalyticsInterface;
import cn.jpush.android.api.JPushInterface;

/**
 * @author Jun
 * @time 2017/1/6  16:06
 * @desc 全局
 */
public class IssueApplication extends BaseApplication {
    protected static Context mContext = null;

    ACache mACache = null;

    public static JSONObject UserInfo;
    public static int unreadMsgCount;
    private static ArrayList<CommentBean> commentList;
    private static DisplayImageOptions options;

    public static void setCommentList(ArrayList<CommentBean> commentList) {
        IssueApplication.commentList = commentList;
    }

    public static ArrayList<CommentBean> getCommentList() {
        return commentList;
    }

    @Override
    public void onCreate() {
        MultiDex.install(this);
//        Thread.currentThread().setUncaughtExceptionHandler(new MyExceptionHander());
        super.onCreate();
        mACache = ACache.get(this);
        mContext= getApplicationContext();
        super.mInstance = this;
        SDKInitializer.initialize(mContext);
        initImageLoader();
        ImageLoadProxy.initImageLoader(mContext);

        JPushInterface.setDebugMode(false); 	// 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);     		// 初始化 JPush

        JAnalyticsInterface.init(mContext);
//        JAnalyticsInterface.initCrashHandler(mContext);

        DemoHelper.getInstance().init(mContext);
        PgyCrashManager.register(this);
//        //SDK 初次注册成功后，开发者通过在自定义的 Receiver 里监听 Action - cn.jpush.android.intent.REGISTRATION 来获取对应的 RegistrationID。注册成功后，也可以通过此函数获取
//        public static String getRegistrationID(Context context)



    }

    public  static   DisplayImageOptions getImageLoaderOption() {
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder().bitmapConfig(Bitmap.Config.RGB_565)
                .cacheInMemory(true).imageScaleType(ImageScaleType.EXACTLY)
                .cacheOnDisk(true).build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                mContext)
                .threadPoolSize(3)
// default
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .denyCacheImageMultipleSizesInMemory()
// .memoryCache(new LruMemoryCache((int) (6 * 1024 * 1024)))
                .memoryCache(new WeakMemoryCache())
                .memoryCacheSize((int) (2 * 1024 * 1024))
                .memoryCacheSizePercentage(13)
                .diskCacheSize(50 * 1024 * 1024).diskCacheFileCount(100)
                .diskCacheFileNameGenerator(new HashCodeFileNameGenerator())
                .defaultDisplayImageOptions(defaultOptions).writeDebugLogs() // Remove
                .build();
// Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);

        options = new DisplayImageOptions.Builder()
                             .showImageOnLoading(R.drawable.bg_default) // resource or
                                                                     // drawable
                             .showImageForEmptyUri(R.drawable.bg_default) // resource or
                                                                         // drawable
                             .showImageOnFail(R.drawable.bg_default) // resource or
                                .cacheInMemory(true)// drawable
                             .resetViewBeforeLoading(false) // default
                             .delayBeforeLoading(1000).cacheInMemory(true) // default
                             .cacheOnDisk(true) // default
                             .considerExifParams(false) // default
                             .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2) // default
                             .bitmapConfig(Bitmap.Config.RGB_565) // default
                             .displayer(new SimpleBitmapDisplayer()) // default
                             .handler(new Handler()) // default
                             .build();
        return options;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }


    private class MyExceptionHander implements Thread.UncaughtExceptionHandler {
        @Override
        public void uncaughtException(Thread thread, Throwable ex) {
            // Logger.i("MobileSafeApplication", "发生了异常,但是被哥捕获了..");
            //            LogUtils.d("MobileSafeApplication","发生了异常,但是被哥捕获了..");
            //并不能把异常消灭掉,只是在应用程序关掉前,来一个留遗嘱的事件
            //获取手机硬件信息
            try {
                Field[] fields = Build.class.getDeclaredFields();
                StringBuffer sb = new StringBuffer();
                for(Field field:fields){
                    String value = field.get(null).toString();
                    String name  = field.getName();
                    sb.append(name);
                    sb.append(":");
                    sb.append(value);
                    sb.append("\n");
                }
                File file=new File(getFilesDir(),"error.log");
                FileOutputStream out = new FileOutputStream(file);
                StringWriter wr = new StringWriter();
                PrintWriter err = new PrintWriter(wr);
                //获取错误信息
                ex.printStackTrace(err);
                String errorlog = wr.toString();
                sb.append(errorlog);
                out.write(sb.toString().getBytes());
                out.flush();
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            //杀死页面进程
            restartApp();

        }
    }

    public void restartApp(){
        Intent intent = new Intent(mContext,MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
        android.os.Process.killProcess(android.os.Process.myPid());  //结束进程之前可以把你程序的注销或者退出代码放在这段代码之前
    }


    /**
     * 获得全局上下文
     * @return
     */
    public static Context getcontext() {
        return mContext;
    }

    //初始化网络图片缓存库
    private void initImageLoader(){
        //网络图片例子,结合常用的图片缓存库UIL,你可以根据自己需求自己换其他网络图片库
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true).cacheOnDisk(true).build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                getApplicationContext()).defaultDisplayImageOptions(defaultOptions)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO).build();
        ImageLoader.getInstance().init(config);
    }




    /**
     * 返回桌面
     */
    public void toDesktop(){
        Intent home = new Intent(Intent.ACTION_MAIN);
        home.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //        home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        home.addCategory(Intent.CATEGORY_HOME);
        startActivity(home);
    }

    public  static String mCId="";

    public static JSONObject mUserObject;

    public  static String imagePath="";

    public  static File cameraPath;

    public static  boolean isClassify=false;

    public static int mCartCount=0;

    public static int mLightIndex = 0;//点出来的灯的序号

    public  static boolean isGoProgramme=false;

    public static JSONArray mSelectProducts=new JSONArray();

    public static JSONArray mSelectScreens=new JSONArray();
    public static final String ACTION_REMOVE_SHORTCUT = "com.android.launcher.action.UNINSTALL_SHORTCUT";
//    private void removeShortcut(String name) {
//        // remove shortcut的方法在小米系统上不管用，在三星上可以移除
//        Intent intent = new Intent(ACTION_REMOVE_SHORTCUT);
//
//        // 名字
//        intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, name);
//
//        // 设置关联程序
//        Intent launcherIntent = new Intent(this,
//                IssueApplication.class).setAction(Intent.ACTION_MAIN);
//
//        intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, launcherIntent);
//
//        // 发送广播
//        sendBroadcast(intent);
//    }

}
