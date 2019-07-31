package bc.juhao.com.controller.user;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aliyun.iot.ilop.demo.DemoApplication;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.exceptions.HyphenateException;
import com.hyphenate.util.NetUtils;
import com.lib.common.hxp.view.ListViewForScrollView;
import com.nostra13.universalimageloader.core.ImageLoader;

import bc.juhao.com.R;
import bc.juhao.com.chat.DemoHelper;
import bc.juhao.com.cons.Constance;
import bc.juhao.com.controller.BaseController;
import bc.juhao.com.listener.INetworkCallBack;
import bc.juhao.com.ui.activity.IssueApplication;
import bc.juhao.com.ui.activity.user.ChatActivity;
import bc.juhao.com.ui.activity.user.MerchantInfoActivity;
import bc.juhao.com.ui.view.ScannerUtils;
import bc.juhao.com.ui.view.ShowDialog;
import bc.juhao.com.utils.ImageLoadProxy;
import bc.juhao.com.utils.ImageUtil;
import bc.juhao.com.utils.MyShare;
import bocang.json.JSONArray;
import bocang.json.JSONObject;
import bocang.utils.AppUtils;
import bocang.utils.MyLog;
import bocang.utils.MyToast;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * @author: Jun
 * @date : 2017/5/20 16:01
 * @description :
 */
public class MerchantInfoController extends BaseController {
    private MerchantInfoActivity mView;
    private ListViewForScrollView main_lv;
    private JSONArray mDatas;
    private ProAdapter mProAdapter;
    private TextView nickname_tv,phone_tv,address_tv;
    private CircleImageView head_cv;
    public boolean misShowBaiduMap=true;


    public MerchantInfoController(MerchantInfoActivity v) {
        mView = v;
        initView();
        initViewData();
    }

    private void sendUserKefu(int id) {
        mNetWork.sendUserKefu(id, new INetworkCallBack() {
            @Override
            public void onSuccessListener(String requestCode, JSONObject ans) {
                mDatas = ans.getJSONArray("kefu");
                mProAdapter.notifyDataSetChanged();

            }

            @Override
            public void onFailureListener(String requestCode, JSONObject ans) {
                MyToast.show(mView, "网络异常，请重新加载!");
            }
        });
    }

    /**
     * 获取邀请码用户信息
     * @param id
     */
    private void sendShopAddress(int id){
        mNetWork.sendShopAddress(id, new INetworkCallBack() {
            @Override
            public void onSuccessListener(String requestCode, JSONObject ans) {
                JSONObject jsonObject = ans.getJSONObject(Constance.shop);
                if (AppUtils.isEmpty(jsonObject))
                    return;
                String user_name = MyShare.get(mView).getString(Constance.USERCODE);
                nickname_tv.setText(user_name);
                phone_tv.setText("联系方式:" + jsonObject.getString(Constance.phone));
                mView.mAddress=jsonObject.getString(Constance.address);
                address_tv.setText("商家地址:" + mView.mAddress);
                String imageUrl = jsonObject.getString(Constance.photo);


                if (!AppUtils.isEmpty(imageUrl)){
                    ImageLoadProxy.displayImage(imageUrl, head_cv);
                }


                if(AppUtils.isEmpty(jsonObject.getString(Constance.latval))) {
                    return;

                }
                mView. baiduMapContainer.setVisibility(View.VISIBLE);
                mView.latx=Float.parseFloat(jsonObject.getString(Constance.latval));
                mView.laty=Float.parseFloat(jsonObject.getString(Constance.longval));
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(500);
                            mView.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mView.initMyLocation();
                                }
                            });
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }

            @Override
            public void onFailureListener(String requestCode, JSONObject ans) {
                MyToast.show(mView, "数据异常，请重试!");
            }
        });
    }

    private void initViewData() {
        int id= MyShare.get(mView).getInt(Constance.USERCODEID);
        sendUserKefu(id);
        sendShopAddress(id);

    }

    private void initView() {
        main_lv = (ListViewForScrollView) mView.findViewById(R.id.main_lv);
        mProAdapter = new ProAdapter();
        main_lv.setAdapter(mProAdapter);
        nickname_tv = (TextView) mView.findViewById(R.id.nickname_tv);
        phone_tv = (TextView) mView.findViewById(R.id.phone_tv);
        address_tv = (TextView) mView.findViewById(R.id.address_tv);
        head_cv = (CircleImageView) mView.findViewById(R.id.head_cv);
    }

    @Override
    protected void handleMessage(int action, Object[] values) {

    }

    @Override
    protected void myHandleMessage(Message msg) {

    }


    private void setPhone(final String phoneNumber) {
        ActivityCompat.requestPermissions(mView,
                new String[]{"android.permission.CALL_PHONE"},
                1);
        ShowDialog mDialog = new ShowDialog();
        mDialog.show(mView, "提示", "是否打电话给" + phoneNumber + "?", new ShowDialog.OnBottomClickListener() {
            @Override
            public void positive() {
                PackageManager packageManager = mView.getPackageManager();
                int permission = packageManager.checkPermission("android.permission.CALL_PHONE", "bc.juhao.com");
                if (PackageManager.PERMISSION_GRANTED != permission) {
                    return;
                } else {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_CALL);
                    intent.setData(Uri.parse("tel:" + phoneNumber));
                    mView.startActivity(intent);
                }
            }

            @Override
            public void negtive() {

            }
        });

    }

    private class ProAdapter extends BaseAdapter {
        public ProAdapter() {
        }

        @Override
        public int getCount() {
            if (null == mDatas)
                return 0;
            return mDatas.length();
        }

        @Override
        public JSONObject getItem(int position) {
            if (null == mDatas)
                return null;
            return mDatas.getJSONObject(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(mView, R.layout.item_merchant_info, null);

                holder = new ViewHolder();
                holder.iv = (ImageView) convertView.findViewById(R.id.iv);
                holder.name_tv = (TextView) convertView.findViewById(R.id.name_tv);
                holder.phone_tv = (TextView) convertView.findViewById(R.id.phone_tv);
                holder.qq_tv = (TextView) convertView.findViewById(R.id.qq_tv);
                holder.weixin_tv = (TextView) convertView.findViewById(R.id.weixin_tv);
                holder.phone_ll = (LinearLayout) convertView.findViewById(R.id.phone_ll);
                holder.qq_ll = (LinearLayout) convertView.findViewById(R.id.qq_ll);
                holder.weixin_ll = (LinearLayout) convertView.findViewById(R.id.weixin_ll);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            try {
                final String qq = mDatas.getJSONObject(position).getString(Constance.qq);
                final String phone = mDatas.getJSONObject(position).getString(Constance.phone);
                holder.name_tv.setText(mDatas.getJSONObject(position).getString(Constance.name));
                holder.phone_tv.setText(phone);
                holder.qq_tv.setText(qq);
                holder.weixin_tv.setText(mDatas.getJSONObject(position).getString(Constance.weixin));
                ImageLoader.getInstance().displayImage(mDatas.getJSONObject(position).getString(Constance.photo), holder.iv);
                holder.phone_ll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setPhone(phone);
                    }
                });
                holder.qq_ll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String qqUrl="mqqwpa://im/chat?chat_type=wpa&uin="+qq+"&version=1";
                        mView.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(qqUrl)));
                    }
                });
                holder.iv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ActivityCompat.requestPermissions(mView,
                                new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"},
                                1);

                        ShowDialog mDialog = new ShowDialog();
                        mDialog.show(mView, "提示", "是否保存该二维码?", new ShowDialog.OnBottomClickListener() {
                            @Override
                            public void positive() {
                                PackageManager packageManager = mView.getPackageManager();
                                int permission = packageManager.checkPermission("android.permission.WRITE_EXTERNAL_STORAGE", "bc.juhao.com");
                                if (PackageManager.PERMISSION_GRANTED != permission) {
                                    return;
                                } else {
                                    ScannerUtils.saveImageToGallery(mView, ImageUtil.drawable2Bitmap(holder.iv.getDrawable()), ScannerUtils.ScannerType.RECEIVER);
                                }
                            }

                            @Override
                            public void negtive() {

                            }
                        });
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }

            return convertView;
        }

        class ViewHolder {
            ImageView iv;
            TextView name_tv;
            TextView phone_tv;
            TextView qq_tv;
            TextView weixin_tv;
            LinearLayout phone_ll;
            LinearLayout qq_ll;
            LinearLayout weixin_ll;

        }
    }
    /**
     * 联系客服
     */
    public void sendCall(String msg, final String parent_id, final String parent_name, final String userIcon) {
        try {
            //            if (AppUtils.isEmpty(IssueApplication.mUserObject.getString("parent_name"))) {
            //                MyToast.show(mView.getActivity(), "不能和自己聊天!");
            //                return;
            //            }

            EaseUser user = new EaseUser(parent_id);
            user.setNickname(parent_name);
            user.setNick(parent_name);
            user.setAvatar(userIcon);
            DemoHelper.getInstance().saveContact(user);


            if (!EMClient.getInstance().isLoggedInBefore()) {
                ShowDialog mDialog = new ShowDialog();
                mDialog.show(mView, "提示", msg, new ShowDialog.OnBottomClickListener() {
                    @Override
                    public void positive() {
                        loginHX(parent_id, parent_name, userIcon);
                    }

                    @Override
                    public void negtive() {

                    }
                });
            } else {
                EMClient.getInstance().contactManager().acceptInvitation(parent_id);
                mView.startActivity(new Intent(mView, ChatActivity.class).putExtra(EaseConstant.EXTRA_USER_ID, parent_id));
            }
        } catch (HyphenateException e) {
            e.printStackTrace();
        }
    }

    //登录环信
    private void loginHX(final String parent_id, final String parent_name, final String userIcon) {
        final Toast toast = Toast.makeText(mView, "服务器连接中...!", Toast.LENGTH_SHORT);
        toast.show();
        if (NetUtils.hasNetwork(mView)) {
            final String uid = MyShare.get(mView).getString(Constance.USERID);
            if (AppUtils.isEmpty(uid)) {
                return;
            }
            if (!TextUtils.isEmpty(uid)) {
                getSuccessLogin(uid, toast, parent_id, parent_name, userIcon);


            }
        }
    }

    private void getSuccessLogin(final String uid, final Toast toast, final String parent_id, final String parent_name, final String userIcon) {
        EMClient.getInstance().login(uid, uid, new EMCallBack() {
            @Override
            public void onSuccess() {
                EMClient.getInstance().groupManager().loadAllGroups();
                EMClient.getInstance().chatManager().loadAllConversations();
                MyLog.e("登录环信成功!");
                toast.cancel();
                String parent_name = DemoApplication.mUserObject.getString("parent_name");
                try {
                    EMClient.getInstance().contactManager().acceptInvitation(parent_id);
                    mView.startActivity(new Intent(mView, ChatActivity.class).putExtra(EaseConstant.EXTRA_USER_ID, parent_id));
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
                                mView.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        MyLog.e("注册成功!");
                                        getSuccessLogin(uid, toast, parent_id, parent_name, userIcon);
                                    }
                                });

                            } catch (final HyphenateException e) {
                                mView.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        MyLog.e("注册失败!");
                                        getSuccessLogin(uid, toast, parent_id, parent_name, userIcon);
                                    }
                                });

                            }
                        }
                    }).start();
                } else {
                    Toast.makeText(mView, "连接失败,请重试!", Toast.LENGTH_SHORT).show();
                    MyLog.e("登录环信失败!");
                    sendCall("连接聊天失败,请重试?", parent_id, parent_name, userIcon);
                }


            }

            @Override
            public void onProgress(int i, String s) {
            }
        });
    }

}
