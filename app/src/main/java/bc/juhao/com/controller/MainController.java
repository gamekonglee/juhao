package bc.juhao.com.controller;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Message;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.aliyun.iot.ilop.demo.DemoApplication;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.exceptions.HyphenateException;

import bc.juhao.com.R;
import bc.juhao.com.cons.Constance;
import bc.juhao.com.cons.NetWorkConst;
import bc.juhao.com.listener.INetworkCallBack;
import bc.juhao.com.listener.INetworkCallBack02;
import bc.juhao.com.ui.activity.MainActivity;
import bc.juhao.com.ui.view.ShowDialog;
import bc.juhao.com.utils.MyShare;
import bc.juhao.com.utils.NetWorkUtils;
import bc.juhao.com.utils.UIUtils;
import bocang.json.JSONObject;
import bocang.utils.AppUtils;
import bocang.utils.CommonUtil;
import bocang.utils.LogUtils;
import bocang.utils.MyToast;
import me.leolin.shortcutbadger.ShortcutBadger;

/**
 * @author: Jun
 * @date : 2017/3/17 10:24
 * @description :
 */
public class MainController extends BaseController implements INetworkCallBack {

    private MainActivity mView;
    private final View rl_main;
    private TextView unMessageReadTv;


    private String mAppVersion;//App最新版本号
    private UpdateApkBroadcastReceiver broadcastReceiver;//App更新广播接收器
    private int isLoginCount = 0;

    public MainController(MainActivity v) {
        mView = v;
        rl_main = mView.findViewById(R.id.rl_main);
        initView();
        initViewData();
        String token= MyShare.get(UIUtils.getContext()).getString(Constance.TOKEN);
//        LogUtils.logE(TAG, token);
    }

    private void initView() {
        unMessageReadTv = (TextView) mView.findViewById(R.id.unMessageReadTv);
    }

    private void initViewData() {
        sendShoppingCart();
        sendVersion();
        checkSystem();
    }

    /**
     * 获取购物车数据
     */
    private void sendShoppingCart() {
        mNetWork.sendShoppingCart(this);
    }

    /**
     * 获取版本号
     */
    private void sendVersion() {
//        mNetWork.sendVersion(this);
        new Thread(new Runnable() {
            @Override
            public void run() {
                //ans 钜豪商城更新提示
                String ans = NetWorkUtils.doGet(NetWorkConst.VERSION_URL_CONTENT);
                final JSONObject jsonObject = new JSONObject(ans);
                mAppVersion = jsonObject.getString(Constance.version);
                if (AppUtils.isEmpty(mAppVersion)) {
                    return;
                }
                String localVersion = CommonUtil.localVersionName(mView);
                if ("-1".equals(mAppVersion)) {

                } else {
                    boolean isNeedUpdate = CommonUtil.isNeedUpdate(localVersion, mAppVersion);
                    if (isNeedUpdate) {
                        mView.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                final Dialog dialog = new Dialog(mView, R.style.customDialog);
                                dialog.setContentView(R.layout.dialog_update);
                                dialog.setCancelable(false);
                                TextView tv_info = (TextView) dialog.findViewById(R.id.tv_update_info);
                                Button btn_upgrate = (Button) dialog.findViewById(R.id.btn_upgrate);
                                ImageView iv_close = (ImageView) dialog.findViewById(R.id.iv_close);
                                String updateInfo = jsonObject.getString(Constance.text);
                                tv_info.setText("" + Html.fromHtml(updateInfo).toString());
                                dialog.show();
                                btn_upgrate.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        Intent intent = new Intent();
                                        intent.setAction("android.intent.action.VIEW");
                                        Uri content_url = Uri.parse("http://a.app.qq.com/o/simple.jsp?pkgname=bc.juhao.com");
                                        intent.setData(content_url);
                                        mView.startActivity(intent);

//                                        AppVersion appVersion = new AppVersion();
//                                        appVersion.setVersion(mAppVersion);
//                                        appVersion.setName(NetWorkConst.APK_NAME);
//                                        appVersion.setDes("");
//                                        appVersion.setForcedUpdate("0");
//                                        appVersion.setUrl(NetWorkConst.DOWN_APK_URL);
//                                        if (appVersion != null) {
//                                            dialog.dismiss();
//                                            new UpAppUtils(mView, appVersion);
//                                        }
                                    }
                                });
                                iv_close.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
//                                        dialog.dismiss();
                                    }
                                });

//                                ShowDialog mDialog = new ShowDialog();
//                                mDialog.show(mView, "升级提示", "最新升级包: V"+mAppVersion+"版，是否升级?", new ShowDialog.OnBottomClickListener() {
//                                    @Override
//                                    public void positive() {
////                                        broadcastReceiver = new UpdateApkBroadcastReceiver();
////                                        mView.registerReceiver(broadcastReceiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
////                                        Intent intent = new Intent(mView, UpdateApkService.class);
////                                        mView.startService(intent);
//                                        AppVersion appVersion = new AppVersion();
//                                        appVersion.setVersion(mAppVersion);
//                                        appVersion.setName("jhsc");
//                                        appVersion.setDes("");
//                                        appVersion.setForcedUpdate("0");
//                                        appVersion.setUrl(NetWorkConst.DOWN_APK_URL);
//                                        if (appVersion != null) {
//                                            new UpAppUtils(mView, appVersion);
//                                        }
//                                    }
//
//                                    @Override
//                                    public void negtive() {
//
//                                    }
//                                });
                            }
                        });


                    }
                }

            }
        }).start();

    }

    private void checkSystem() {
        mNetWork.checkSystem(new INetworkCallBack() {
            @Override
            public void onSuccessListener(String requestCode, JSONObject ans) {
                JSONObject data = ans.getJSONObject(Constance.data);
//                data=new JSONObject();
//                data.add("title","系统维护");
//                data.add("text","钜豪商城定于今天（2018年8月27日，星期一）晚上 20:00—22:00 对购物系统进行停机维护。受此影响，届时APP将暂停服务。感谢您的支持和谅解！");
                if (data != null && data.length() > 0) {
                    mView.isError = true;
                    UIUtils.showSystemStopDialog(mView, rl_main, data.getString(Constance.title), data.getString(Constance.text));
                } else {
                    mView.isError = false;
                }

                LogUtils.logE(TAG, "" + data);
            }

            @Override
            public void onFailureListener(String requestCode, JSONObject ans) {

            }
        });
    }

    @Override
    public void onSuccessListener(String requestCode, JSONObject ans) {
        switch (requestCode) {
            case NetWorkConst.GETCART://购物车
                if (ans.getJSONArray(Constance.goods_groups).length() > 0) {
                    DemoApplication.mCartCount = ans.getJSONArray(Constance.goods_groups)
                            .getJSONObject(0).getJSONArray(Constance.goods).length();
                    setIsShowCartCount();
                }
                LogUtils.logE(TAG, "cart data:"+ans);
                break;
            case NetWorkConst.VERSION_URL://版本号
                mAppVersion = ans.getString(Constance.JSON);

                break;
            default:
                break;
        }
    }

    @Override
    public void onFailureListener(String requestCode, JSONObject ans) {

    }

    public void refreshUIWithMessage() {
        mView.runOnUiThread(new Runnable() {
            public void run() {
                // refresh unread count
                for (EMConversation conversation : EMClient.getInstance().chatManager().getAllConversations().values()) {
                    mView.unreadMsgCount = conversation.getUnreadMsgCount();
                    mView.allMsgCount = conversation.getAllMsgCount();
                }
                //获取此会话在本地的所有的消息数量
                //如果只是获取当前在内存的消息数量，调用
                DemoApplication.unreadMsgCount = mView.unreadMsgCount;
                if (mView.unreadMsgCount == 0) {
//                    ShortcutBadger.applyCount(this, badgeCount); //for 1.1.4+
                    mView.mHomeFragment.unMessageTv.setVisibility(View.GONE);
                    ShortcutBadger.applyCount(mView, 0);
                } else {
                    ShortcutBadger.applyCount(mView, mView.unreadMsgCount); //for 1.1.4+
                    mView.mHomeFragment.unMessageTv.setVisibility(View.VISIBLE);
                    mView.mHomeFragment.unMessageTv.setText(mView.unreadMsgCount + "");
                }

//                if (mView.mMessageFragment != null) {
//                    mView.mMessageFragment.refresh();
//                }
            }
        });
    }

    /**
     * 登录成功处理事件
     */
    private void getSuccessLogin() {
        if (isLoginCount == 3) {
            ShowDialog mDialog = new ShowDialog();
            mDialog.show(mView, "提示", "聊天登录失败,是否重试?", new ShowDialog.OnBottomClickListener() {
                @Override
                public void positive() {
                    isLoginCount = 1;
                    getSuccessLogin();
                }

                @Override
                public void negtive() {

                }
            });
        }

        isLoginCount = isLoginCount + 1;
        final String uid = MyShare.get(mView).getString(Constance.USERID);
        if (AppUtils.isEmpty(uid)) {
            return;

        }


        EMClient.getInstance().login(uid, uid, new EMCallBack() {
            @Override
            public void onSuccess() {
                Log.e("520it", "登录成功");
                EMClient.getInstance().groupManager().loadAllGroups();
                EMClient.getInstance().chatManager().loadAllConversations();
                refreshUIWithMessage();
            }

            @Override
            public void onProgress(int progress, String status) {
            }

            @Override
            public void onError(final int code, final String message) {
                if (message.equals("Unknown server error")) {
                    Log.e("520it", "再次登录一:" + message);
                    getSuccessLogin();
                } else if (message.equals("User is already login")) {
                    Log.e("520it", "再次登录二:" + message);
                    EMClient.getInstance().logout(true, new EMCallBack() {

                        @Override
                        public void onSuccess() {
                            getSuccessLogin();
                        }

                        @Override
                        public void onProgress(int progress, String status) {
                            // TODO Auto-generated method stub
                        }

                        @Override
                        public void onError(int code, String message) {
                            // TODO Auto-generated method stub
                        }
                    });

                }
                Log.e("520it", "登录失败:" + message);
            }
        });
    }

    /**
     * 获取用户信息
     */
    public void sendUser() {
        mNetWork.sendUser(new INetworkCallBack() {
            @Override
            public void onSuccessListener(String requestCode, JSONObject ans) {
                JSONObject mUserObject = ans.getJSONObject(Constance.user);
                DemoApplication.mUserObject = mUserObject;
//                if (AppUtils.isEmpty(mUserObject))
//                    return;
//                String avatar = NetWorkConst.SCENE_HOST + mUserObject.getString(Constance.avatar);
////                if (!AppUtils.isEmpty(avatar))
////                    ImageLoadProxy.displayHeadIcon(avatar, head_cv);
//
//                String username = DemoApplication.mUserObject.getString(Constance.username);
//                String nickName = DemoApplication.mUserObject.getString(Constance.nickname);
//                int level = DemoApplication.mUserObject.getInt(Constance.level);
//                String levelValue = "";
////                mView.user_money_ll.setVisibility(View.VISIBLE);
//                if (level == 0) {
//                    levelValue = "一级";
//                } else if (level == 1) {
//                    levelValue = "二级";
//                } else if (level == 2) {
//                    levelValue = "三级";
//                } else {
////                    mView.user_money_ll.setVisibility(View.GONE);
//                    levelValue = "消费者";
//                }
//                level_tv.setText(levelValue);
//                Log.v("520it", DemoApplication.mUserObject.getString(Constance.money));
//                JSONArray countArray = ans.getJSONArray("count");
//                String count01 = countArray.get(0).toString();
//                String count02 = countArray.get(1).toString();
//                String count03 = countArray.get(2).toString();
//                unMessageReadTv.setText(countArray.get(0).toString());
//                unMessageRead02Tv.setText(countArray.get(1).toString());
//                unMessageRead03Tv.setText(countArray.get(2).toString());
//                unMessageReadTv.setVisibility(Integer.parseInt(count01) > 0 ? View.VISIBLE : View.GONE);
//                unMessageRead02Tv.setVisibility(Integer.parseInt(count02) > 0 ? View.VISIBLE : View.GONE);
//                unMessageRead03Tv.setVisibility(Integer.parseInt(count03) > 0 ? View.VISIBLE : View.GONE);
//                if (AppUtils.isEmpty(nickName)) {
////                    nickname_tv.setText(username);
////                    IntentUtil.startActivity(mView.getActivity(), PerfectMydataActivity.class, false);
//                    return;
//                } else {
////                    nickname_tv.setText(nickName);
//                }
            }

            @Override
            public void onFailureListener(String requestCode, JSONObject ans) {

            }
        });
    }

    public void login(String content) {
        mNetWork.sendTokenAdd(content, new INetworkCallBack02() {
            @Override
            public void onSuccessListener(String requestCode, com.alibaba.fastjson.JSONObject ans) {
//                mView.hideLoading();
//                mView.finish();
//                MyToast.show(mView,ans.toJSONString());
            }

            @Override
            public void onFailureListener(String requestCode, com.alibaba.fastjson.JSONObject ans) {
//                mView.hideLoading();
//                mView.onRefresh();
//            if(ans!=null){
//                MyToast.show(mView,"failure"+ans.toJSONString());
//            }else {
//                MyToast.show(mView,"failuter");
//            }
            }
        });
    }

    private class UpdateApkBroadcastReceiver extends BroadcastReceiver {

        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
        @Override
        public void onReceive(Context context, final Intent intent) {
            // 判断是否下载完成的广播
            if (intent.getAction().equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
                // 注销广播
                mView.unregisterReceiver(broadcastReceiver);
                broadcastReceiver = null;

                // 获取下载的文件id
                long downId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                DownloadManager down = (DownloadManager) mView.getSystemService(mView.download);
                final Uri uri = down.getUriForDownloadedFile(downId);
            }
        }
    }

    /**
     * 首页显示购物车商品数量
     */
    public void setIsShowCartCount() {
        unMessageReadTv.setVisibility(DemoApplication.mCartCount == 0 ? View.GONE : View.VISIBLE);
        unMessageReadTv.setText(DemoApplication.mCartCount + "");
    }

    @Override
    protected void handleMessage(int action, Object[] values) {

    }

    @Override
    protected void myHandleMessage(Message msg) {

    }

    /**
     * 环信注册成功
     */
    private void sendRegiestSuccess() {
        final String uid = MyShare.get(mView).getString(Constance.USERID);
        if (AppUtils.isEmpty(uid)) {
            return;

        }
        new Thread(new Runnable() {
            public void run() {
                try {
                    EMClient.getInstance().createAccount(uid, uid);//同步方法
                    mView.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            getSuccessLogin();
                            MyToast.show(mView, "注册成功!");
                        }
                    });

                } catch (final HyphenateException e) {
                    mView.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            getSuccessLogin();
                        }
                    });

                }
            }
        }).start();
    }

}
