package bc.juhao.com.controller.user;

import android.app.Dialog;
import android.content.Intent;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.aliyun.iot.ilop.demo.DemoApplication;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.exceptions.HyphenateException;
import com.hyphenate.util.NetUtils;

import bc.juhao.com.R;
import bc.juhao.com.chat.DemoHelper;
import bc.juhao.com.cons.Constance;
import bc.juhao.com.cons.NetWorkConst;
import bc.juhao.com.controller.BaseController;
import bc.juhao.com.listener.INetworkCallBack;
import bc.juhao.com.ui.activity.ChartListActivity;
import bc.juhao.com.ui.activity.SettingActivity;
import bc.juhao.com.ui.activity.user.ChatActivity;
import bc.juhao.com.ui.activity.user.CollectActivity;
import bc.juhao.com.ui.activity.user.InvitationCodeActivity;
import bc.juhao.com.ui.activity.user.MerchantInfoActivity;
import bc.juhao.com.ui.activity.user.MessageActivity;
import bc.juhao.com.ui.activity.user.MyDistributorActivity;
import bc.juhao.com.ui.activity.user.MyDistrubutorNewActivity;
import bc.juhao.com.ui.activity.user.MyOrderActivity;
import bc.juhao.com.ui.activity.user.PerfectMydataActivity;
import bc.juhao.com.ui.activity.user.UserAddrActivity;
import bc.juhao.com.ui.activity.user.UserLogActivity;
import bc.juhao.com.ui.fragment.MineFragment;
import bc.juhao.com.ui.view.ShowDialog;
import bc.juhao.com.utils.ImageLoadProxy;
import bc.juhao.com.utils.MyShare;
import bc.juhao.com.utils.ShareUtil;
import bc.juhao.com.utils.UIUtils;
import bocang.json.JSONArray;
import bocang.json.JSONObject;
import bocang.utils.AppUtils;
import bocang.utils.IntentUtil;
import bocang.utils.MyLog;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * @author: Jun
 * @date : 2017/1/21 15:08
 * @description :
 */
public class MineController extends BaseController implements INetworkCallBack {
    private MineFragment mView;
    private CircleImageView head_cv;
    private TextView nickname_tv;
    public JSONObject mUserObject;
    public Intent mIntent;
    private ScrollView main_sv;
    private TextView unMessageReadTv, unMessageRead02Tv, unMessageRead03Tv, level_tv;


    public MineController(MineFragment v) {
        mView = v;
        initView();
        initViewData();
    }

    private void initViewData() {
    }

    private void initView() {
        head_cv = (CircleImageView) mView.getView().findViewById(R.id.head_user_cv);
        nickname_tv = (TextView) mView.getView().findViewById(R.id.nickname_tv);
        unMessageReadTv = (TextView) mView.getView().findViewById(R.id.unMessageReadTv);
        unMessageRead02Tv = (TextView) mView.getView().findViewById(R.id.unMessageRead02Tv);
        unMessageRead03Tv = (TextView) mView.getView().findViewById(R.id.unMessageRead03Tv);
        level_tv = (TextView) mView.getView().findViewById(R.id.level_tv);
        main_sv = (ScrollView) mView.getView().findViewById(R.id.main_sv);
    }

    @Override
    protected void handleMessage(int action, Object[] values) {

    }

    @Override
    protected void myHandleMessage(Message msg) {

    }

    /**
     * 头像
     */
    public void setHead() {
        IntentUtil.startActivity(mView.getActivity(), PerfectMydataActivity.class, false);
    }

    /**
     * 设置
     */
    public void setSetting() {
        IntentUtil.startActivity(mView.getActivity(), SettingActivity.class, false);
    }

    /**
     * 我的订单
     */
    public void setOrder() {
        IntentUtil.startActivity(mView.getActivity(), MyOrderActivity.class, false);
    }

    /**
     * 我的收藏
     */
    public void setCollect() {
        IntentUtil.startActivity(mView.getActivity(), CollectActivity.class, false);

    }

    /**
     * 管理收货地址
     */
    public void setAddress() {
        IntentUtil.startActivity(mView.getActivity(), UserAddrActivity.class, false);
    }


    /**
     * 代收货
     */
    public void setReceiving() {
        mIntent = new Intent(mView.getActivity(), MyOrderActivity.class);
        mIntent.putExtra(Constance.order_type, 3);
        mView.startActivity(mIntent);
    }

    /**
     * 管理物流地址
     */
    public void setStream() {
        IntentUtil.startActivity(mView.getActivity(), UserLogActivity.class, false);
    }

    /**
     * 消息中心
     */
    public void SetMessage() {
        IntentUtil.startActivity(mView.getActivity(), MessageActivity.class, false);
    }

    /**
     * 待付款
     */
    public void setPayMen() {
        mIntent = new Intent(mView.getActivity(), MyOrderActivity.class);
        mIntent.putExtra(Constance.order_type, 1);
        mView.startActivity(mIntent);
    }

    /**
     * 待发货
     */
    public void setDelivery() {
        mIntent = new Intent(mView.getActivity(), MyOrderActivity.class);
        mIntent.putExtra(Constance.order_type, 2);
        mView.startActivity(mIntent);
    }

    /**
     * 获取用户信息
     */
    public void sendUser() {
        if(!TextUtils.isEmpty(MyShare.get(mView.getActivity()).getString(Constance.TOKEN))){
        mNetWork.sendUser(this);
        }
    }

    /**
     * 分享给好友
     */
    public void getShareApp() {
        final String title = "来自 " + UIUtils.getString(R.string.app_name) + " App的分享";
        final Dialog dialog=UIUtils.showBottomInDialog(mView.getActivity(), R.layout.share_dialog,UIUtils.dip2PX(150));
        TextView tv_cancel=dialog.findViewById(R.id.tv_cancel);
        LinearLayout ll_wx=dialog.findViewById(R.id.ll_wx);
        LinearLayout ll_pyq=dialog.findViewById(R.id.ll_pyq);
        LinearLayout ll_qq=dialog.findViewById(R.id.ll_qq);
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        ll_wx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShareUtil.shareWx(mView.getActivity(), title, NetWorkConst.APK_URL, NetWorkConst.SHAREIMAGE);
                dialog.dismiss();
            }
        });
        ll_pyq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShareUtil.sharePyq(mView.getActivity(), title, NetWorkConst.APK_URL, NetWorkConst.SHAREIMAGE);
                dialog.dismiss();
            }
        });
        ll_qq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShareUtil.shareQQ(mView.getActivity(), title, NetWorkConst.APK_URL, NetWorkConst.SHAREIMAGE);
                dialog.dismiss();
            }
        });
    }

    @Override
    public void onSuccessListener(String requestCode, JSONObject ans) {
        switch (requestCode) {
            case NetWorkConst.PROFILE:
                mUserObject = ans.getJSONObject(Constance.user);
                DemoApplication.mUserObject = mUserObject;
                if (AppUtils.isEmpty(mUserObject))
                    return;
                String avatar = NetWorkConst.SCENE_HOST + mUserObject.getString(Constance.avatar);
                if (!AppUtils.isEmpty(avatar))
                    ImageLoadProxy.displayHeadIcon(avatar, head_cv);

                String username = DemoApplication.mUserObject.getString(Constance.username);
                String nickName = DemoApplication.mUserObject.getString(Constance.nickname);
                int level = DemoApplication.mUserObject.getInt(Constance.level);
                String levelValue = "";
                mView.user_money_ll.setVisibility(View.VISIBLE);
                mView.distributor_ll.setVisibility(View.VISIBLE);
                mView.view_empty.setVisibility(View.GONE);
                if (level == 0||level==5) {
                    levelValue = "一级";
                } else if (level == 1) {
                    levelValue = "二级";
                } else if (level == 2) {
                    levelValue = "三级";
                } else {
                    mView.user_money_ll.setVisibility(View.INVISIBLE);
                    mView.distributor_ll.setVisibility(View.GONE);
                    mView.view_empty.setVisibility(View.VISIBLE);
                    levelValue = "消费者";
                }
                mView.onRefresh();
                level_tv.setText(levelValue);
                Log.v("520it", DemoApplication.mUserObject.getString(Constance.money));
                JSONArray countArray = ans.getJSONArray("count");
                String count01 = countArray.get(0).toString();
                String count02 = countArray.get(1).toString();
                String count03 = countArray.get(2).toString();
                unMessageReadTv.setText(countArray.get(0).toString());
                unMessageRead02Tv.setText(countArray.get(1).toString());
                unMessageRead03Tv.setText(countArray.get(2).toString());
                unMessageReadTv.setVisibility(Integer.parseInt(count01) > 0 ? View.VISIBLE : View.GONE);
                unMessageRead02Tv.setVisibility(Integer.parseInt(count02) > 0 ? View.VISIBLE : View.GONE);
                unMessageRead03Tv.setVisibility(Integer.parseInt(count03) > 0 ? View.VISIBLE : View.GONE);
                if (AppUtils.isEmpty(nickName)) {
                    nickname_tv.setText(username);
                    IntentUtil.startActivity(mView.getActivity(), PerfectMydataActivity.class, false);
                    return;
                } else {
                    nickname_tv.setText(nickName);
                }

                break;
        }

    }

    @Override
    public void onFailureListener(String requestCode, JSONObject ans) {
        try {
            if (AppUtils.isEmpty(ans)) {
//                AppDialog.messageBox(UIUtils.getString(R.string.server_error));
                return;
            }
        } catch (Exception e) {

        }
    }

    /**
     * 联系客服
     */
    public void sendCall(String msg) {
        try {
            int level = DemoApplication.mUserObject.getInt(Constance.level);
            if (level == 0) {
                if (!mView.isToken()) {
                    IntentUtil.startActivity(mView.getActivity(), ChartListActivity.class, false);
                }
                return;
            }

            String parent_name = DemoApplication.mUserObject.getString("parent_name");
            String parent_id = DemoApplication.mUserObject.getString("parent_id");
            String userIcon = NetWorkConst.SCENE_HOST + DemoApplication.mUserObject.getString("parent_avatar");
            EaseUser user = new EaseUser(parent_id);
            user.setNickname(parent_name);
            user.setNick(parent_name);
            user.setAvatar(userIcon);
            DemoHelper.getInstance().saveContact(user);

            if (!EMClient.getInstance().isLoggedInBefore()) {
                ShowDialog mDialog = new ShowDialog();
                mDialog.show(mView.getActivity(), "提示", msg, new ShowDialog.OnBottomClickListener() {
                    @Override
                    public void positive() {
                        loginHX();
                    }

                    @Override
                    public void negtive() {

                    }
                });
            } else {
                EMClient.getInstance().contactManager().acceptInvitation(parent_id);
                mView.startActivity(new Intent(mView.getActivity(), ChatActivity.class).putExtra(EaseConstant.EXTRA_USER_ID, parent_id));
            }


        } catch (HyphenateException e) {
            e.printStackTrace();
        }
    }

    //登录环信
    private void loginHX() {
        final Toast toast = Toast.makeText(mView.getActivity(), "服务器连接中...!", Toast.LENGTH_SHORT);
        toast.show();
        if (NetUtils.hasNetwork(mView.getActivity())) {
            final String uid = MyShare.get(mView.getActivity()).getString(Constance.USERNAME);
            if (AppUtils.isEmpty(uid)) {
                return;
            }
            if (!TextUtils.isEmpty(uid)) {
                getSuccessLogin(uid, toast);

            }
        }
    }

    private void getSuccessLogin(final String uid, final Toast toast) {
        EMClient.getInstance().login(uid, uid, new EMCallBack() {
            @Override
            public void onSuccess() {
                EMClient.getInstance().groupManager().loadAllGroups();
                EMClient.getInstance().chatManager().loadAllConversations();
                MyLog.e("登录环信成功!");
                toast.cancel();
                String parent_id = DemoApplication.mUserObject.getString("parent_id");
                try {
                    EMClient.getInstance().contactManager().acceptInvitation(parent_id);
                    mView.startActivity(new Intent(mView.getActivity(), ChatActivity.class).putExtra(EaseConstant.EXTRA_USER_ID, parent_id));
                } catch (HyphenateException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(int i, String s) {
                if (s.equals("User dosn't exist")) {
                    new Thread(new Runnable() {
                        public void run() {
                            try {
                                EMClient.getInstance().createAccount(uid, uid);//同步方法
                                mView.getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        MyLog.e("注册成功!");
                                        getSuccessLogin(uid, toast);
                                    }
                                });

                            } catch (final HyphenateException e) {
                                mView.getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        MyLog.e("注册失败!");
                                        getSuccessLogin(uid, toast);
                                    }
                                });

                            }
                        }
                    }).start();
                } else {
                    Toast.makeText(mView.getActivity(), "连接失败,请重试!", Toast.LENGTH_SHORT).show();
                    MyLog.e("登录环信失败!");
                    sendCall("连接聊天失败,请重试?");
                }
            }

            @Override
            public void onProgress(int i, String s) {
            }
        });
    }

    /**
     * 我的邀请码
     */
    public void getInvitationCode() {
        IntentUtil.startActivity(mView.getActivity(), InvitationCodeActivity.class, false);
    }

    /**
     * 我的分销商
     */
    public void getMyistributor() {
        IntentUtil.startActivity(mView.getActivity(), MyDistrubutorNewActivity.class, false);
    }

    /**
     * 服务
     */
    public void getService() {
        IntentUtil.startActivity(mView.getActivity(), MerchantInfoActivity.class, false);
    }
}
