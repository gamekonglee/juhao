package bc.juhao.com.ui.activity;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.acker.simplezxing.activity.CaptureActivity;
import com.alibaba.sdk.android.openaccount.OpenAccountSDK;
import com.alibaba.sdk.android.openaccount.OpenAccountService;
import com.alibaba.sdk.android.openaccount.callback.LogoutCallback;
import com.aliyun.iot.aep.component.router.Router;
import com.aliyun.iot.aep.sdk.credential.IotCredentialManager.IoTCredentialManageImpl;
import com.aliyun.iot.ilop.demo.DemoApplication;
import com.aliyun.iot.ilop.demo.page.main.IndexActivity;
//import com.example.qrcode.Constant;
import com.example.qrcode.Constant;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCmdMessageBody;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.controller.EaseUI;
import com.hyphenate.easeui.domain.EaseUser;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import bc.juhao.com.R;
import bc.juhao.com.bean.LoginResult;
import bc.juhao.com.bean.UserLogin;
import bc.juhao.com.chat.DemoHelper;
import bc.juhao.com.common.BaseActivity;
import bc.juhao.com.cons.Constance;
import bc.juhao.com.controller.MainController;
import bc.juhao.com.controller.SimpleScannerLoginController;
import bc.juhao.com.ui.activity.product.ProDetailActivity;
import bc.juhao.com.ui.activity.user.LoginActivity;
import bc.juhao.com.ui.fragment.CartFragment;
import bc.juhao.com.ui.fragment.ClassifyFragment;
import bc.juhao.com.ui.fragment.HomeVpFragment;
import bc.juhao.com.ui.fragment.MineFragment;
import bc.juhao.com.ui.fragment.ProgrammeFragment;
import bc.juhao.com.ui.view.BottomBar;
import bc.juhao.com.ui.view.ShowDialog;
import bc.juhao.com.ui.view.WarnDialog;
import bc.juhao.com.utils.MyShare;
import bc.juhao.com.utils.UIUtils;
import bocang.json.JSONArray;
import bocang.utils.MyToast;
import cn.jiguang.analytics.android.api.JAnalyticsInterface;
import cn.jpush.android.api.JPushInterface;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    public HomeVpFragment mHomeFragment;
    private ClassifyFragment mProductFragment;
    private CartFragment mCartFragment;
    private ProgrammeFragment mMatchFragment;
    private MineFragment mMineFragment;
    //    public BottomBar mBottomBar;
    private Fragment currentFragmen;
    private int pager = 2;
    private long exitTime;
    public static JSONArray mCategories;
    public static boolean toFilter;
    private MainController mController;
    public String download = DOWNLOAD_SERVICE;
    public int unreadMsgCount = 0;


    private TextView frag_top_tv;
    private TextView frag_product_tv;
    private TextView frag_match_tv;
    private TextView frag_cart_tv;
    private TextView frag_mine_tv;
    private ImageView frag_top_iv;
    private ImageView frag_product_iv;
    private ImageView frag_match_iv;
    private ImageView frag_cart_iv;
    private ImageView frag_mine_iv;

    public static boolean isForeground = false;
    public static int mFragmentPosition;
    private int navigationBarHegiht;
    private Dialog dialog;
    public int allMsgCount;
    public boolean isError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
//        fullScreen(this);
        EventBus.getDefault().register(this);
//        ActivityCompat.requestPermissions(this,
//                new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CALL_PHONE},
//                1);
        JAnalyticsInterface.onPageStart(this, this.getClass().getCanonicalName());
    }


    // 初始化 JPush。如果已经初始化，但没有登录成功，则执行重新登录。
    private void init() {
        //        JPushInterface.init(getApplicationContext());
        JPushInterface.setDebugMode(true);//如果时正式版就改成false
        JPushInterface.init(this);

//        Log.e("520it", JPushInterface.getRegistrationID(this));
    }

    @Override
    protected void InitDataView() {
        selectItem(R.id.frag_top_ll);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (DemoApplication.isGoProgramme) {
            DemoApplication.isGoProgramme = false;
//            selectItem(R.id.frag_match_ll);
//            clickTab3Layout();
        }
        checkUI();


    }


    private void checkUI() {
        switch (mFragmentPosition){
            case 0:
                selectItem(R.id.frag_top_ll);
                break;
            case 1:
                selectItem(R.id.frag_product_ll);
                break;
            case 2:
                break;
            case 3:
                if(isToken()){
                    return;
                }
                selectItem(R.id.frag_cart_ll);
                break;
            case 4:
                if(isToken()){
                    return;
                }
                selectItem(R.id.frag_mine_ll);
                break;
        }
    }

    public void onRefresh(){
        onStart();
    }
    @Override
    protected void initController() {
        mController = new MainController(this);
    }

    @Override
    public void onBackPressed() {

        if(isError){
            finish();
        }else {
            super.onBackPressed();
        }
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_main_jh);
        //沉浸式状态栏
        setStatuTextColor(this, Color.WHITE);
        setFullScreenColor(Color.TRANSPARENT,this);
        navigationBarHegiht = getNavigationBarHeight2();
//        View ll_main=findViewById(R.id.ll_main);
        RelativeLayout.LayoutParams layoutParams=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        layoutParams.setMargins(0,0,0,navigationBarHegiht);
//        ll_main.setLayoutParams(layoutParams);
//            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        frag_top_tv = (TextView) findViewById(R.id.frag_top_tv);
        frag_product_tv = (TextView) findViewById(R.id.frag_product_tv);
        frag_match_tv = (TextView) findViewById(R.id.frag_match_tv);
        frag_cart_tv = (TextView) findViewById(R.id.frag_cart_tv);
        frag_mine_tv = (TextView) findViewById(R.id.frag_mine_tv);
        frag_top_iv = (ImageView) findViewById(R.id.frag_top_iv);
        frag_product_iv = (ImageView) findViewById(R.id.frag_product_iv);
        frag_match_iv = (ImageView) findViewById(R.id.frag_match_iv);
        frag_cart_iv = (ImageView) findViewById(R.id.frag_cart_iv);
        frag_mine_iv = (ImageView) findViewById(R.id.frag_mine_iv);

        findViewById(R.id.frag_top_tv).setOnClickListener(this);
        findViewById(R.id.frag_product_tv).setOnClickListener(this);
        findViewById(R.id.frag_match_tv).setOnClickListener(this);
        findViewById(R.id.frag_cart_tv).setOnClickListener(this);
        findViewById(R.id.frag_mine_tv).setOnClickListener(this);
        findViewById(R.id.frag_top_ll).setOnClickListener(this);
        findViewById(R.id.frag_product_ll).setOnClickListener(this);
        findViewById(R.id.frag_match_ll).setOnClickListener(this);
        findViewById(R.id.frag_cart_ll).setOnClickListener(this);
        findViewById(R.id.frag_mine_ll).setOnClickListener(this);

        initTab();
        registerMessageListener();
        mFragmentPosition = 0;
        isError = false;

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
//
//            local LayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
//            if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP){
//                //将侧边栏顶部延伸至status bar
//                mDrawerLayout.setFitsSystemWindows(true);
//                //将主页面顶部延伸至status bar;虽默认为false,但经测试,DrawerLayout需显示设置
//                mDrawerLayout.setClipToPadding(false);
//            }
//        }
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//        }



//        if (Build.VERSION.SDK_INT >= 21) {
//            View decorView = getWindow().getDecorView();
//            int option = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
//            decorView.setSystemUiVisibility(option);
//            getWindow().setNavigationBarColor(Color.TRANSPARENT);
//            getWindow().setStatusBarColor(Color.TRANSPARENT);
//        }
        Log.e("520it", "initView: " + getHasVirtualKey() + ":" + getNoHasVirtualKey() );

    }
    private int getNavigationBarHeight() {
        Resources resources = getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height","dimen", "android");
        int height = resources.getDimensionPixelSize(resourceId);
        Log.v("dbw", "Navi height:" + height);
        return height;
    }
    private int gethdHeight(Context context){
        int rid=context.getResources().getIdentifier("config_showNavigationBar","bool","android");
        if(rid!=0){
            int resourceId=context.getResources().getIdentifier("navigation_bar_height","dimen","android");
            return context.getResources().getDimensionPixelSize(resourceId);
        }else {
            return 0;
        }
    }
    public int getNavigationBarHeight2() {

        Resources resources = getResources();

        int resourceId=resources.getIdentifier("navigation_bar_height","dimen","android");

        int height = resources.getDimensionPixelSize(resourceId);

        Log.v("navigation bar>>>", "height:" + height);

        return height;

    }
//    private int getDaoHangHeight(Context context) {
//         int result = 0; 
//        int rid = context.getResources().getIdentifier("config_showNavigationBar", "bool", "android"); 
//        if (rid != 0) { 
//            int resourceId = context.getResources().getIdentifier("navigation_bar_height", "dimen", "android"); 
//            return context.getResources().getDimensionPixelSize(resourceId); 
//        } else {
//            return 0;
//        } 
//    }

    // 通过反射机制获取手机状态栏高度
    public int getStatusBarHeight() {
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, statusBarHeight = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return statusBarHeight;
    }


    /**
     * 获取屏幕尺寸，但是不包括虚拟功能高度
     *
     * @return
     */
    public int getNoHasVirtualKey() {
        int height = getWindowManager().getDefaultDisplay().getHeight();
        return height;
    }

    /**
     * 通过反射，获取包含虚拟键的整体屏幕高度
     *
     * @return
     */
    private int getHasVirtualKey() {
        int dpi = 0;
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        @SuppressWarnings("rawtypes")
        Class c;
        try {
            c = Class.forName("android.view.Display");
            @SuppressWarnings("unchecked")
            Method method = c.getMethod("getRealMetrics", DisplayMetrics.class);
            method.invoke(display, dm);
            dpi = dm.heightPixels;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dpi;
    }



    @Override
    protected void initData() {
//        Intent intent = getIntent();
//        if (intent.getBooleanExtra(Constance.ACCOUNT_CONFLICT, false)) {
//            showConflictDialog();
//        }

    }

    /**
     * show the dialog when user logged into another device
     */
    public void showConflictDialog() {
        EMClient.getInstance().logout(true, new EMCallBack() {
            @Override
            public void onSuccess() {
                // TODO Auto-generated method stub
                Log.e("520it", "Main注销登录");
            }

            @Override
            public void onProgress(int progress, String status) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onError(int code, String message) {
                // TODO Auto-generated method stub
                Log.e("520it", "Main注销失败:" + message);
            }
        });

        MyShare.get(this).putString(Constance.TOKEN, "");
        MyShare.get(MainActivity.this).putString(Constance.TOKEN, "");
        MyShare.get(MainActivity.this).putString(Constance.USERNAME, "");
        MyShare.get(MainActivity.this).putString(Constance.USERCODE, "");
        MyShare.get(MainActivity.this).putString(Constance.USERCODEID, "");
        MyShare.get(MainActivity.this).putString(Constance.invite_code,"");
        ShowDialog mDialog = new ShowDialog();
        mDialog.show(this, "提示", UIUtils.getString(R.string.connect_conflict), new ShowDialog.OnBottomClickListener() {
            @Override
            public void positive() {
                EMClient.getInstance().logout(true, new EMCallBack() {

                    @Override
                    public void onSuccess() {
                        // TODO Auto-generated method stub
                        Log.e("520it", "Main注销登录");
                    }

                    @Override
                    public void onProgress(int progress, String status) {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void onError(int code, String message) {
                        // TODO Auto-generated method stub
                        Log.e("520it", "Main注销失败:" + message);
                    }
                });
                Intent logoutIntent = new Intent(MainActivity.this, LoginActivity.class);
//                logoutIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(logoutIntent);
//                finish();


            }

            @Override
            public void negtive() {
                EMClient.getInstance().logout(true, new EMCallBack() {

                    @Override
                    public void onSuccess() {
                        // TODO Auto-generated method stub
                        Log.e("520it", "Main注销登录");
                    }

                    @Override
                    public void onProgress(int progress, String status) {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void onError(int code, String message) {
                        // TODO Auto-generated method stub
                        Log.e("520it", "Main注销失败:" + message);
                    }
                });
                Intent logoutIntent = new Intent(MainActivity.this, LoginActivity.class);
                logoutIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(logoutIntent);
                finish();
            }
        });


    }


    /**
     * EMEventListener
     */
    protected EMMessageListener messageListener = null;

    protected void registerMessageListener() {
        messageListener = new EMMessageListener() {
            private BroadcastReceiver broadCastReceiver = null;

            @Override
            public void onMessageReceived(List<EMMessage> messages) {
                for (EMMessage message : messages) {
                    //                    EMLog.d(TAG, "onMessageReceived id : " + message.getMsgId());
                    EaseUI.getInstance().getNotifier().onNewMsg(message);

                    String userNice = message.getStringAttribute(Constance.USER_NICE, "");
                    String userPic = message.getStringAttribute(Constance.USER_ICON, "");
                    String userId = message.getStringAttribute(Constance.User_ID, "");
                    EaseUser user1 = new EaseUser(userId);
                    user1.setNickname(userId);
                    user1.setNick(userNice);
                    user1.setAvatar(userPic);
                    DemoHelper.getInstance().saveContact(user1);

                }
                mController.refreshUIWithMessage();
            }

            @Override
            public void onCmdMessageReceived(List<EMMessage> messages) {
                for (EMMessage message : messages) {
                    //                    EMLog.d(TAG, "receive command message");
                    //get message body
                    EMCmdMessageBody cmdMsgBody = (EMCmdMessageBody) message.getBody();
                    final String action = cmdMsgBody.action();//获取自定义action

                    if (action.equals("__Call_ReqP2P_ConferencePattern")) {
                        String title = message.getStringAttribute("em_apns_ext", "conference call");
                        //                        Toast.makeText(appContext, title, Toast.LENGTH_LONG).show();
                    }
                    //end of red packet code
                    //获取扩展属性 此处省略
                    //maybe you need get extension of your message
                    //message.getStringAttribute("");
                    //                    EMLog.d(TAG, String.format("Command：action:%s,message:%s", action, message.toString()));
                }
            }

            @Override
            public void onMessageReadAckReceived(List<EMMessage> messages) {
            }

            @Override
            public void onMessageDeliveryAckReceived(List<EMMessage> message) {
            }

            @Override
            public void onMessageChanged(EMMessage message, Object change) {

            }
        };

        EMClient.getInstance().chatManager().addMessageListener(messageListener);
    }


    private BottomBar.IBottomBarItemClickListener mBottomBarClickListener = new BottomBar.IBottomBarItemClickListener() {
        @Override
        public void OnItemClickListener(int resId) {
            switch (resId) {
                case R.id.frag_top_ll:
                    //                    full(true);
                    //                    MainActivity.this.textView.setBackgroundColor(Color.parseColor("#FF0000"));
                    //                    rootView.setBackgroundColor(Color.parseColor("#FF0000"));
                    mFragmentPosition=0;
                    clickTab1Layout();
                    break;
                case R.id.frag_product_ll:
                    //                    MainActivity.this.textView.setBackgroundColor(Color.parseColor("#00000000"));
                    //                    rootView.setBackgroundColor(Color.parseColor("#00000000"));
                    mFragmentPosition=1;
                    clickTab2Layout();


                    break;
                case R.id.frag_match_ll:
                    clickTab3Layout();
                    break;
                case R.id.frag_cart_ll:
                    if(isToken()){
                        checkUI();
                        return;
                    }
                    mFragmentPosition=3;
                    clickTab4Layout();
                    break;
                case R.id.frag_mine_ll:
                    if(isToken()){
                        checkUI();
                        return;
                    }
                    mFragmentPosition=4;
                    clickTab5Layout();
                    break;
                case R.id.frag_top_tv:
                    //
                    //    full(true);
                    mFragmentPosition=0;
                    clickTab1Layout();
                    break;
            }
        }
    };

    /**
     * 初始化底部标签
     */
    private void initTab() {
        if (mHomeFragment == null) {
            mHomeFragment = new HomeVpFragment();
        }
        if (!mHomeFragment.isAdded()) {
            // 提交事务
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.top_bar, mHomeFragment).commit();

            // 记录当前Fragment
            currentFragmen = mHomeFragment;
        }
    }


    /**
     * 点击第1个tab
     */
    public void clickTab1Layout() {
        if (mHomeFragment == null) {
            mHomeFragment = new HomeVpFragment();
        }
        addOrShowFragment(getSupportFragmentManager().beginTransaction(), mHomeFragment);

    }

    /**
     * 点击第2个tab
     */
    public void clickTab2Layout() {
        if (mProductFragment == null) {
            mProductFragment = new ClassifyFragment();
        }
        addOrShowFragment(getSupportFragmentManager().beginTransaction(), mProductFragment);

    }

    /**
     * 点击第3个tab
     */
    public void clickTab3Layout() {
//        if (mMatchFragment == null) {
//            mMatchFragment = new ProgrammeFragment();
//        }
//        addOrShowFragment(getSupportFragmentManager().beginTransaction(), mMatchFragment);
        if(isToken()){
            return;
        }
        startActivity(new Intent(this, com.aliyun.iot.ilop.demo.page.ilopmain.MainActivity.class));
    }

    /**
     * 点击第4个tab
     */
    private void clickTab4Layout() {
        if (mCartFragment == null) {
            mCartFragment = new CartFragment();
        }
        addOrShowFragment(getSupportFragmentManager().beginTransaction(), mCartFragment);

    }

    /**
     * 点击第5个tab
     */
    public void clickTab5Layout() {
        if (mMineFragment == null) {
            mMineFragment = new MineFragment();
        }
        addOrShowFragment(getSupportFragmentManager().beginTransaction(), mMineFragment);

    }

    /**
     * 添加或者显示碎片
     *
     * @param transaction
     * @param fragment
     */
    private void addOrShowFragment(FragmentTransaction transaction,
                                   Fragment fragment) {
        if (currentFragmen == fragment)
            return;
        if (!fragment.isAdded()) { // 如果当前fragment未被添加，则添加到Fragment管理器中
            transaction.hide(currentFragmen)
                    .add(R.id.top_bar, fragment).commit();
        } else {
            transaction.hide(currentFragmen).show(fragment).commit();
        }

        currentFragmen = fragment;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if (pager == 2) {
            if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
                if ((System.currentTimeMillis() - exitTime) > 2000) {
                    exitTime = System.currentTimeMillis();

                    MyToast.show(this, R.string.back_desktop);
                } else {
                    finish();
                }
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * show 激活 dialog
     */
    public void showActivateDialog() {
        WarnDialog activateDialog = new WarnDialog(this, "请激活该设备", "确定", true, false, false);
        activateDialog.setListener(new bc.juhao.com.ui.view.BaseDialog.IConfirmListener() {
            @Override
            public void onDlgConfirm(bc.juhao.com.ui.view.BaseDialog dlg, int flag) {
                if (flag == 0) {
                    MyToast.show(MainActivity.this, "激活成功!!");
                }
            }
        });
        activateDialog.show();
    }


    @Override
    protected void onResume() {
        isForeground = true;
        super.onResume();
//        boolean confict=getIntent().getBooleanExtra(Constance.ACCOUNT_CONFLICT,false);
//        if (getIntent()!=null&&getIntent().getBooleanExtra(Constance.ACCOUNT_CONFLICT, false)) {
//            showConflictDialog();
//        }
    }


    @Override
    protected void onPause() {
        isForeground = false;
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUserLogin(UserLogin action){
//        int confict=getIntent().getIntExtra(Constance.ACCOUNT_CONFLICT,-1);
        if (action!=null&&action.getValue().equals("1")) {
            showConflictDialog();
        }
    }
    //在主线程执行
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUserEvent(Integer action) {
        if (action == Constance.CARTCOUNT) {
            mController.setIsShowCartCount();
        }
//        if (action == Constance.MESSAGE) {
//            unreadMsgCount = DemoApplication.unreadMsgCount;
//            if (unreadMsgCount == 0) {
//                mHomeFragment.unMessageTv.setVisibility(View.GONE);
//                ShortcutBadger.applyCount(this, 0);
//            } else {
//                ShortcutBadger.applyCount(this, this.unreadMsgCount); //for 1.1.4+
//                mHomeFragment.unMessageTv.setVisibility(View.VISIBLE);
//                mHomeFragment.unMessageTv.setText(unreadMsgCount + "");
//            }
//        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoginResult(LoginResult result){
        if(result.result==1){
            if(!TextUtils.isEmpty(MyShare.get(this).getString(Constance.TOKEN))){
                mController.sendUser();
            }
        }
    }
    @Override
    public void onClick(View v) {
        //	设置 如果电机的是当前的的按钮 再次点击无效
        if (mCurrenTabId != 0 && mCurrenTabId == v.getId()) {
            return;
        }

        selectItem(v.getId());
    }

    /**
     * 默认全部不被选中
     */
    private void defaultTabStyle() {
        frag_top_tv.setSelected(false);
        frag_top_iv.setSelected(false);
        frag_product_tv.setSelected(false);
        frag_product_iv.setSelected(false);
        frag_match_tv.setSelected(false);
        frag_match_iv.setSelected(false);
        frag_cart_tv.setSelected(false);
        frag_cart_iv.setSelected(false);
        frag_mine_tv.setSelected(false);
        frag_mine_iv.setSelected(false);
    }

    private int mCurrenTabId;

    /**
     * 选择指定的item
     *
     * @param currenTabId
     */
    public void selectItem(int currenTabId) {
        //	设置 如果电机的是当前的的按钮 再次点击无效
        if (mCurrenTabId != 0 && mCurrenTabId == currenTabId) {
            return;
        }
        //点击前先默认全部不被选中
        defaultTabStyle();

        mCurrenTabId = currenTabId;
        switch (currenTabId) {
            case R.id.frag_top_ll:
                frag_top_tv.setSelected(true);
                frag_top_iv.setSelected(true);
                mFragmentPosition=0;
                clickTab1Layout();
                break;
            case R.id.frag_product_ll:
                frag_product_tv.setSelected(true);
                frag_product_iv.setSelected(true);
                mFragmentPosition=1;
                clickTab2Layout();
                break;
            case R.id.frag_match_ll:
                if(isToken()){
                    checkUI();
                    return;
                }
                mFragmentPosition=0;
//                frag_match_tv.setSelected(true);
//                frag_match_iv.setSelected(true);
                clickTab3Layout();
                break;
            case R.id.frag_cart_ll:
            case R.id.frag_cart_tv:
                if(isToken()){
                    checkUI();
                    return;
                }
                mFragmentPosition=3;
                frag_cart_tv.setSelected(true);
                frag_cart_iv.setSelected(true);
                clickTab4Layout();
                break;
            case R.id.frag_mine_ll:
            case R.id.frag_mine_tv:
                if(isToken()){
                    checkUI();
                    return;
                }
                mFragmentPosition=4;
                frag_mine_tv.setSelected(true);
                frag_mine_iv.setSelected(true);
                clickTab5Layout();
                break;
            case R.id.frag_top_tv:
                mFragmentPosition=0;
                frag_top_tv.setSelected(true);
                frag_top_iv.setSelected(true);
                clickTab1Layout();
                break;
            case R.id.frag_product_tv:
                mFragmentPosition=1;
                frag_product_tv.setSelected(true);
                frag_product_iv.setSelected(true);
                clickTab2Layout();
                break;
            case R.id.frag_match_tv:
                frag_match_tv.setSelected(true);
                frag_match_iv.setSelected(true);
                clickTab3Layout();
                break;

//                mFragmentPosition=3;
//                frag_cart_tv.setSelected(true);
//                frag_cart_iv.setSelected(true);
//                clickTab4Layout();
//                break;

//                mFragmentPosition=4;
//                frag_mine_tv.setSelected(true);
//                frag_mine_iv.setSelected(true);
//                clickTab5Layout();
//                break;
        }
    }
//    /**
//     * 通过设置全屏，设置状态栏透明
//     *
//     * @param activity
//     */
//    private void fullScreen(Activity activity) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                //5.x开始需要把颜色设置透明，否则导航栏会呈现系统默认的浅灰色
//                Window window = activity.getWindow();
//                View decorView = window.getDecorView();
//                //两个 flag 要结合使用，表示让应用的主体内容占用系统状态栏的空间
//                int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
//                decorView.setSystemUiVisibility(option);
//                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//                window.setStatusBarColor(Color.TRANSPARENT);
//                //导航栏颜色也可以正常设置
////                window.setNavigationBarColor(Color.TRANSPARENT);
//            } else {
//                Window window = activity.getWindow();
//                WindowManager.LayoutParams attributes = window.getAttributes();
//                int flagTranslucentStatus = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
//                int flagTranslucentNavigation = WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION;
//                attributes.flags |= flagTranslucentStatus;
////                attributes.flags |= flagTranslucentNavigation;
//                window.setAttributes(attributes);
//            }
//        }
//    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (data == null) return;
//            Log.e("data",data.getStringExtra(Constant.EXTRA_RESULT_CONTENT));
            final String content=data.getStringExtra(CaptureActivity.EXTRA_SCAN_RESULT);
            if(content!=null&&content.contains("scale")){
                dialog = new Dialog(this, R.style.customDialog);
                dialog.setContentView(R.layout.dialog_login);
                dialog.setCancelable(true);
                dialog.setCanceledOnTouchOutside(true);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                Button btn_login= dialog.findViewById(R.id.btn_login);
                Button btn_cancel= dialog.findViewById(R.id.btn_cancel);
//            MyToast.show(this,result.getText());
                btn_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                btn_login.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        showLoading();
                        dialog.dismiss();
                        mController.login(content) ;
                    }
                });
                dialog.show();
            }else {
                try{Intent mIntent = new Intent(this, ProDetailActivity.class);
                int productId = Integer.parseInt(content);
                mIntent.putExtra(Constance.product, productId);
                this.startActivity(mIntent);
                this.finish();}catch (Exception e){

                }
            }
            switch (requestCode) {
                case 400:
//                    String type = data.getStringExtra(Constant.EXTRA_RESULT_CODE_TYPE);
//                    String content = data.getStringExtra(Constant.EXTRA_RESULT_CONTENT);
//                    Toast.makeText(MainActivity.this,"codeType:" + type
//                            + "-----content:" + content, Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
