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

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.exceptions.HyphenateException;

import bc.juhao.com.R;
import bc.juhao.com.bean.AppVersion;
import bc.juhao.com.cons.Constance;
import bc.juhao.com.cons.NetWorkConst;
import bc.juhao.com.listener.INetworkCallBack;
import bc.juhao.com.ui.activity.IssueApplication;
import bc.juhao.com.ui.activity.MainActivity;
import bc.juhao.com.ui.activity.user.PerfectMydataActivity;
import bc.juhao.com.ui.view.ShowDialog;
import bc.juhao.com.utils.ImageLoadProxy;
import bc.juhao.com.utils.MyShare;
import bc.juhao.com.utils.NetWorkUtils;
import bc.juhao.com.utils.upload.UpAppUtils;
import bocang.json.JSONArray;
import bocang.json.JSONObject;
import bocang.utils.AppUtils;
import bocang.utils.CommonUtil;
import bocang.utils.IntentUtil;
import bocang.utils.MyToast;
import me.leolin.shortcutbadger.ShortcutBadger;

/**
 * @author: Jun
 * @date : 2017/3/17 10:24
 * @description :
 */
public class MainController extends BaseController implements INetworkCallBack {
    private TextView unMessageReadTv;
    private MainActivity mView;
    private String mAppVersion;

    public MainController(MainActivity v) {
        mView = v;
        initView();
        initViewData();


    }

    private void initViewData() {
        sendShoppingCart();
        sendVersion();

    }

    public void sendShoppingCart() {
        mNetWork.sendShoppingCart(this);
    }

    private void initView() {
        unMessageReadTv = (TextView) mView.findViewById(R.id.unMessageReadTv);
    }

    @Override
    protected void handleMessage(int action, Object[] values) {

    }

    @Override
    protected void myHandleMessage(Message msg) {

    }

    /**
     * 获取版本号
     */
    private void sendVersion(){
        mNetWork.sendVersion(this);
        new Thread(new Runnable() {
            @Override
            public void run() {
                String ans = NetWorkUtils.doGet(NetWorkConst.VERSION_URL_CONTENT);
                final JSONObject jsonObject=new JSONObject(ans);
                mAppVersion= jsonObject.getString(Constance.version);
                if(AppUtils.isEmpty(mAppVersion)) return;
                String localVersion = CommonUtil.localVersionName(mView);
                if ("-1".equals(mAppVersion)) {

                } else {
                    boolean isNeedUpdate = CommonUtil.isNeedUpdate(localVersion, mAppVersion);
                    if (isNeedUpdate){
                        mView.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                final Dialog dialog = new Dialog(mView, R.style.customDialog);
                                dialog.setContentView(R.layout.dialog_update);
                                TextView tv_info= (TextView) dialog.findViewById(R.id.tv_update_info);
                                Button btn_upgrate= (Button) dialog.findViewById(R.id.btn_upgrate);
                                ImageView iv_close= (ImageView) dialog.findViewById(R.id.iv_close);
                                String updateInfo=jsonObject.getString(Constance.text);
                                tv_info.setText(""+ Html.fromHtml(updateInfo).toString());
                                dialog.show();
                                btn_upgrate.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        Intent intent = new Intent();
                                        intent.setAction("android.intent.action.VIEW");
                                        Uri content_url = Uri.parse("http://app.08138.com/jhsc.apk");
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
                                        dialog.dismiss();
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

    @Override
    public void onSuccessListener(String requestCode, JSONObject ans) {
        switch (requestCode) {
            case NetWorkConst.GETCART:
                if (ans.getJSONArray(Constance.goods_groups).length() > 0) {
                    IssueApplication.mCartCount = ans.getJSONArray(Constance.goods_groups)
                            .getJSONObject(0).getJSONArray(Constance.goods).length();
                    setIsShowCartCount();
                }
                break;
            case NetWorkConst.VERSION_URL:
               mAppVersion= ans.getString(Constance.JSON);

                break;
        }
    }


    public void refreshUIWithMessage() {
        mView.runOnUiThread(new Runnable() {
            public void run() {
                // refresh unread count
                for (EMConversation conversation : EMClient.getInstance().chatManager().getAllConversations().values()) {
                    mView.unreadMsgCount = conversation.getUnreadMsgCount();
                }
                //获取此会话在本地的所有的消息数量
                //如果只是获取当前在内存的消息数量，调用
                IssueApplication.unreadMsgCount = mView.unreadMsgCount;
                if (mView.unreadMsgCount == 0) {
//                    ShortcutBadger.applyCount(this, badgeCount); //for 1.1.4+
                    mView.mHomeFragment.unMessageTv.setVisibility(View.GONE);
                    ShortcutBadger.applyCount(mView,0);
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
     * 环信注册成功
     */
    private void sendRegiestSuccess() {
        final String uid=MyShare.get(mView).getString(Constance.USERID);
        if(AppUtils.isEmpty(uid)){
            return;

        }
        new Thread(new Runnable() {
            public void run() {
                try {
                    EMClient.getInstance().createAccount(uid,uid);//同步方法
                    mView.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            getSuccessLogin();
                            MyToast.show(mView,"注册成功!");
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

    private int isLoginCount=0;

    /**
     * 登录成功处理事件
     */
    private void getSuccessLogin() {
        if(isLoginCount==3){
            ShowDialog mDialog=new ShowDialog();
            mDialog.show(mView, "提示", "聊天登录失败,是否重试?", new ShowDialog.OnBottomClickListener() {
                @Override
                public void positive() {
                    isLoginCount=1;
                    getSuccessLogin();
                }

                @Override
                public void negtive() {

                }
            });
        }

        isLoginCount=isLoginCount+1;
        final String uid=MyShare.get(mView).getString(Constance.USERID);
        if(AppUtils.isEmpty(uid)){
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

    private UpdateApkBroadcastReceiver broadcastReceiver;

    public void sendUser() {
        mNetWork.sendUser(new INetworkCallBack() {
            @Override
            public void onSuccessListener(String requestCode, JSONObject ans) {
                JSONObject mUserObject = ans.getJSONObject(Constance.user);
                IssueApplication.mUserObject = mUserObject;
//                if (AppUtils.isEmpty(mUserObject))
//                    return;
//                String avatar = NetWorkConst.SCENE_HOST + mUserObject.getString(Constance.avatar);
////                if (!AppUtils.isEmpty(avatar))
////                    ImageLoadProxy.displayHeadIcon(avatar, head_cv);
//
//                String username = IssueApplication.mUserObject.getString(Constance.username);
//                String nickName = IssueApplication.mUserObject.getString(Constance.nickname);
//                int level = IssueApplication.mUserObject.getInt(Constance.level);
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
//                Log.v("520it", IssueApplication.mUserObject.getString(Constance.money));
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



    public void setIsShowCartCount() {
        unMessageReadTv.setVisibility(IssueApplication.mCartCount==0?View.GONE:View.VISIBLE);
        unMessageReadTv.setText(IssueApplication.mCartCount+"");
    }


    @Override
    public void onFailureListener(String requestCode, JSONObject ans) {

    }
}
