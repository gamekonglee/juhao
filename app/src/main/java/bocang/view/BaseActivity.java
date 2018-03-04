package bocang.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.IdRes;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import bc.juhao.com.R;
import bc.juhao.com.cons.Constance;
import bc.juhao.com.ui.activity.user.LoginActivity;
import bc.juhao.com.ui.view.dialog.SpotsDialog;
import bc.juhao.com.utils.MyShare;
import bocang.utils.AppUtils;


public abstract class BaseActivity extends FragmentActivity implements View.OnClickListener {
//    public LoadingDialog mDialog;

    public SpotsDialog mDialog;
//    mDialog=new SpotsDialog(mView);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去掉title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        //设为竖屏
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        initDiaLog();
        initData();
        initView();
        initController();
        InitDataView();




    }

    public Handler handler = new Handler();


    protected abstract void InitDataView();

    protected abstract void initController();

    /**
     * 初始化控件
     */
    protected abstract void initView();

    /**
     * 初始化数据
     */
    protected abstract void initData();


    /**
     * 设置状态栏颜色
     * @param activity 需要设置的activity
     * @param color 状态栏颜色值
     */
    public static void setColor(Activity activity, int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 设置状态栏透明
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // 生成一个状态栏大小的矩形
            View statusView = createStatusView(activity, color);
            // 添加 statusView 到布局中
            ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
            decorView.addView(statusView);
            // 设置根布局的参数
            ViewGroup rootView = (ViewGroup) ((ViewGroup) activity.findViewById(android.R.id.content)).getChildAt(0);
            rootView.setFitsSystemWindows(true);
            rootView.setClipToPadding(true);
        }
    }


    /**
     * 生成一个和状态栏大小相同的矩形条
     * @param activity 需要设置的activity
     * @param color 状态栏颜色值
     * @return 状态栏矩形条
     */
    private static View createStatusView(Activity activity, int color) {
        // 获得状态栏高度
        int resourceId = activity.getResources().getIdentifier("status_bar_height", "dimen", "android");
        int statusBarHeight = activity.getResources().getDimensionPixelSize(resourceId);
        // 绘制一个和状态栏一样高的矩形
        View statusView = new View(activity);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                statusBarHeight);
        statusView.setLayoutParams(params);
        statusView.setBackgroundColor(color);
        return statusView;
    }

    public void goBack(View v) {
        finish();
    }

    private void initDiaLog() {
    }


    /**
     * 获取并绑定点击
     *
     * @param id id
     */
    @SuppressWarnings("unchecked")
    protected <T extends View> T getViewAndClick(@IdRes int id) {
        T v = getView(id);
        v.setOnClickListener(this);
        return v;
    }

    /**
     * 获取 控件
     *
     * @param id 行布局中某个组件的id
     */
    @SuppressWarnings("unchecked")
    public <T extends View> T getView(@IdRes int id) {
        return (T) findViewById(id);
    }

    @Override
    public void onClick(View v) {
        onViewClick(v);
    }

    protected abstract void onViewClick(View v);

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }

    private boolean showDialog;// 显示加载对话框
    private boolean isDestroy;

    /**
     * 显示加载对话框
     *
     * @param showDialog 是否显示加载对话框
     */
    public void setShowDialog(boolean showDialog) {
        this.showDialog = showDialog;
    }

    /**
     * 加载框文本
     */
    public void setShowDialog(String msg) {
        if (isDestroy) return;
        if (msg == null) {
            showDialog = false;
            return;
        }
        showDialog = true;
        if (mDialog == null) {
            setLoadingDialog(new SpotsDialog(this, msg));
        } else {
//            mDialog.setLoadMsg(msg);
        }
    }

    public void setLoadingDialog(SpotsDialog loadingDialog) {
        this.mDialog = loadingDialog;
    }

    /**
     * 隐藏加载框
     */
    public  void hideLoading() {
        if (isDestroy) return;
        if (mDialog != null)
            mDialog.dismiss();
    }

    /**
     * 显示加载框
     */
    public void showLoading() {
        if (isDestroy) return;
        if (showDialog) {
            if (mDialog == null) {
                setLoadingDialog(new SpotsDialog(this));
            }
            try{
                mDialog.show();
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }

    private View errorView ,contentView;
    private TextView error_tv ;
    private ImageView error_iv ;
    private RotateAnimation animation ;


    /**
     * 加载页面
     * @param resId
     */
    public void showLoadingPage(String tip,int resId){
        errorinit();
        if (errorView==null){
            return;
        }
        if (error_iv==null){
            return;
        }
        if (error_tv==null){
            return;
        }
        if (contentView==null){
            return;
        }

//        errorView.setVisibility(View.VISIBLE);
//        contentView.setVisibility(View.GONE);
////        if (loginView!=null){
////            loginView.setVisibility(View.GONE);
////        }
//        if (!TextUtils.isEmpty(tip)){
//            error_tv.setText(tip);
//        }else {
//            error_tv.setText("数据正在加载...");
//        }
//        error_iv.setImageResource(resId);
//        /** 设置旋转动画 */
//        if (animation==null){
//            animation =new RotateAnimation(0f,359f, Animation.RELATIVE_TO_SELF,
//                    0.5f,Animation.RELATIVE_TO_SELF,0.5f);
//            animation.setDuration(1000);//设置动画持续时间
//            /** 常用方法 */
//            animation.setRepeatCount(Integer.MAX_VALUE);//设置重复次数
//            animation.startNow();
//        }
//        error_iv.setAnimation(animation);
        if (mDialog == null) {
            setLoadingDialog(new SpotsDialog(this));
        }
        try {
            mDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 显示错误页面
     * @param message
     * @param resId
     */
    public void showErrorView(String message,int resId){
        errorinit();
        if (errorView==null){
            return;
        }
        if (error_iv==null){
            return;
        }
        if (error_tv==null){
            return;
        }
        if (contentView==null){
            return;
        }
//        if (loginView!=null){
//            loginView.setVisibility(View.GONE);
//        }
        error_iv.setImageResource(resId);
        if (!TextUtils.isEmpty(message)){
            error_tv.setText(message);
        }else {
            error_tv.setText("数据加载失败！");
        }
        error_iv.setAnimation(null);
        errorView.setVisibility(View.VISIBLE);
        contentView.setVisibility(View.GONE);
    }

    private void errorinit() {
        errorView = findViewById(R.id.errorView);
//        errorView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (onReloadDataListener!=null){
//                    onReloadDataListener.request(true);
//                }
//            }
//        });
        error_iv = (ImageView)findViewById(R.id.error_iv);
        error_tv = (TextView) findViewById(R.id.error_tv);
        contentView = findViewById(R.id.contentView);
    }


    /**
     * 显示内容区域
     */
    public void showContentView(){
        errorinit();
        if (errorView==null){
            return;
        }
        if (error_iv==null){
            return;
        }
        if (error_tv==null){
            return;
        }
        if (contentView==null){
            return;
        }
        contentView.setVisibility(View.VISIBLE);
        errorView.setVisibility(View.GONE);
        mDialog.dismiss();
    }

    /**
     * 判断是否有toKen
     */
    public Boolean isToken() {
        String token = MyShare.get(this).getString(Constance.TOKEN);
        if(AppUtils.isEmpty(token)){
            Intent logoutIntent = new Intent(this, LoginActivity.class);
            logoutIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(logoutIntent);
            return true;
        }
        return false;
    }
}
