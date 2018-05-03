package bc.juhao.com.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import bc.juhao.com.R;
import bc.juhao.com.ui.activity.IssueApplication;
import bc.juhao.com.ui.activity.user.LoginActivity;
import bc.juhao.com.ui.activity.user.Regiest01Activity;
import bc.juhao.com.ui.activity.user.RegiestActivity;
import bocang.utils.IntentUtil;

/**
 * @author Jun
 * @time 2016/8/19  10:37
 * @desc ${TODD}
 */
public class
UIUtils {

    /**
     * 得到上下文
     * @return
     */
    public static Context getContext(){
        return IssueApplication.getcontext();
    }


    public static String getDeviceId(){
        return ((TelephonyManager) getContext().getSystemService(getContext().TELEPHONY_SERVICE))
                .getDeviceId();
    }


    /**
     *mac
     * @param context
     * @return String
     */
    public static String getLocalMac(Context context){
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        String mac=info.getMacAddress();
        if(mac.equals("02:00:00:00:00:00")){
            return getInterfaceLocalmac();
        }else {
            return mac;
        }
    }



    public static String  getInterfaceLocalmac(){
        String mac="";
        Enumeration<NetworkInterface> interfaces = null;
        try {
            interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface iF = interfaces.nextElement();
                byte[] addr = iF.getHardwareAddress();
                if (addr == null || addr.length == 0) {
                    continue;
                }
                StringBuilder buf = new StringBuilder();
                for (byte b : addr) {
                    buf.append(String.format("%02X:", b));
                }
                if (buf.length() > 0) {
                    buf.deleteCharAt(buf.length() - 1);
                }
                mac = buf.toString();
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return mac;
    }


    /**
     * 得到Resources对象
     * @return
     */
    public static Resources getResources(){
        return getContext().getResources();
    }

    /**
     * 得到包名
     * @return
     */
    public static String getpackageName(){
        return  getContext().getPackageName();
    }

    /**
     * 得到配置的String信息
     * @param resId
     * @return
     */
    public static String getString(int resId){
        return getResources().getString(resId);
    }

    /**
     * 得到配置的String信息
     * @param resId
     * @return
     */
    public static String getString(int resId,Object ...formatAgs){
        return getResources().getString(resId,formatAgs);
    }

    /**
     * 得到配置String数组
     * @param resId
     * @return
     */
    public static String[] getStringArr(int resId){
        return getResources().getStringArray(resId);
    }
    public static int dip2PX(int dip) {
        //拿到设备密度
        float density=getResources().getDisplayMetrics().density;
        int px= (int) (dip*density+.5f);
        return px;
    }
    public static int dip2PX(Context context,int dip) {
        //拿到设备密度
        float density=context.getResources().getDisplayMetrics().density;
        int px= (int) (dip*density+.5f);
        return px;
    }

    /**
     * 兼容状态栏透明（沉浸式）
     * @param activity
     */
    public static void setImmersionStateMode(Activity activity){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT != Build.VERSION_CODES.LOLLIPOP) {
            // 透明状态栏
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // 透明导航栏
            // getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS |
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    // | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            window.setNavigationBarColor(Color.TRANSPARENT);
        }
    }
    public static Dialog showBottomInDialog(Activity activity, int layout_res, int height) {
        Dialog dialog = new Dialog(activity, R.style.customDialog);
        dialog.setContentView(layout_res);
        dialog.setCanceledOnTouchOutside(true);
        Window win = dialog.getWindow();
        win.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = win.getAttributes();
        WindowManager manager = activity.getWindowManager();
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        int width = outMetrics.widthPixels;
        lp.width = width;
        lp.height = height;
        lp.x=0;
        win.setAttributes(lp);
        dialog.show();
        return  dialog;
    }
    public static void showSingleWordDialog(final Context activity, String tittle, final View.OnClickListener listener) {
        final Dialog dialog = new Dialog(activity, R.style.customDialog);
        dialog.setContentView(R.layout.dialog_layout);
        TextView tv_num= (TextView) dialog.findViewById(R.id.tv_num);
        tv_num.setText(tittle);

        TextView btn = (TextView) dialog.findViewById(R.id.tv_ensure);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClick(view);
                dialog.dismiss();
            }
        });
        TextView cancel= (TextView) dialog.findViewById(R.id.tv_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
           /*
         * 获取圣诞框的窗口对象及参数对象以修改对话框的布局设置, 可以直接调用getWindow(),表示获得这个Activity的Window
         * 对象,这样这可以以同样的方式改变这个Activity的属性.
         */
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();
    }
    /**
     * 获取版本号名称
     *
     * @param context 上下文
     * @return
     */
    public static String getVerName(Context context) {
        String verName = "";
        try {
            verName = context.getPackageManager().
                    getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return verName;
    }
    public static Dialog showLoginDialog(final Context acticity){
        final Dialog dialog=new Dialog(acticity,R.style.customDialog);
        dialog.setContentView(R.layout.dialog_login_toast);
        TextView tv_login=dialog.findViewById(R.id.tv_login);
        TextView tv_register=dialog.findViewById(R.id.tv_register);
        tv_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent logoutIntent = new Intent(acticity, LoginActivity.class);
//                logoutIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                acticity.startActivity(logoutIntent);
                dialog.dismiss();
            }
        });
        tv_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                IntentUtil.startActivity(acticity, Regiest01Activity.class, false);
                acticity.startActivity(new Intent(acticity, Regiest01Activity.class));
                dialog.dismiss();
            }
        });
        ImageView iv_dismiss=dialog.findViewById(R.id.iv_dismiss);
        iv_dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
        return dialog;
    }

    public static int  initListViewHeight(ListView listView) {
        if(listView==null){
            return 0;
        }
        Adapter adapter=listView.getAdapter();
        if(adapter==null){
            return 0;
        }
        int count=adapter.getCount();
        int total=0;
        for(int i=0;i<count;i++){
            View view=adapter.getView(i,null,listView);
            view.measure(0,0);
            total+=view.getMeasuredHeight();
        }
//        System.out.println("total:"+total);
        ViewGroup.LayoutParams layoutParams=listView.getLayoutParams();
        layoutParams.height=total;
        listView.setLayoutParams(layoutParams);
        listView.requestLayout();
        return total;
    }

    public static void diallPhone(Context context,String mShop_mobile) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + mShop_mobile);
        intent.setData(data);
        context.startActivity(intent);
    }
    public static int getScreenWidth(Activity activity){
        WindowManager manager = activity.getWindowManager();
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        int width = outMetrics.widthPixels;
        int height = outMetrics.heightPixels;
        return width;
    }
    public static int getScreenHeight(Activity activity){
        WindowManager manager = activity.getWindowManager();
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        int width = outMetrics.widthPixels;
        int height = outMetrics.heightPixels;
        return height;
    }
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static boolean isValidContext (Context c) {

        Activity a = (Activity) c;

        if (a.isDestroyed() || a.isFinishing()) {
//            Log.i("YXH", "Activity is invalid." + " isDestoryed-->" + a.isDestroyed() + " isFinishing-->" + a.isFinishing());
            return false;
        } else {
            return true;
        }
    }
}
