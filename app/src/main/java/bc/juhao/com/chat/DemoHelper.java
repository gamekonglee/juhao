package bc.juhao.com.chat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.aliyun.iot.ilop.demo.DemoApplication;
import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMError;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCmdMessageBody;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.easeui.controller.EaseUI;
import com.hyphenate.easeui.domain.EaseEmojicon;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.model.EaseAtMessageHelper;
import com.hyphenate.easeui.model.EaseNotifier;
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.hyphenate.util.EMLog;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import bc.juhao.com.R;
import bc.juhao.com.bean.UserLogin;
import bc.juhao.com.chat.db.InviteMessgeDao;
import bc.juhao.com.chat.db.UserDao;
import bc.juhao.com.cons.Constance;
import bc.juhao.com.cons.NetWorkConst;
import bc.juhao.com.ui.activity.IssueApplication;
import bc.juhao.com.ui.activity.MainActivity;
import bc.juhao.com.ui.activity.user.ChatActivity;

/**
 * @author Jun
 * @time 2016/12/21  16:20
 * @desc ${TODD}
 */
public class DemoHelper {

    /**
     * data sync listener
     */
    public interface DataSyncListener {
        /**
         * sync complete
         *
         * @param success true：data sync successful，false: failed to sync data
         */
        void onSyncComplete(boolean success);
    }

    protected static final String TAG = "DemoHelper";

    private EaseUI easeUI;

    /**
     * EMEventListener
     */
    protected EMMessageListener messageListener = null;

    private Map<String, EaseUser> contactList;


    private String username;

    private Context appContext;

    private static DemoHelper instance = null;

    private InviteMessgeDao inviteMessgeDao;
    private UserDao userDao;


    private boolean isGroupAndContactListenerRegisted;

    private DemoHelper() {
    }

    public synchronized static DemoHelper getInstance() {
        if (instance == null) {
            instance = new DemoHelper();
        }
        return instance;
    }

    /**
     * init helper
     *
     * @param context application context
     */
    public void init(Context context) {
        EMOptions options = initChatOptions();
        if (EaseUI.getInstance().init(context, options)) {
            appContext = context;

            EMClient.getInstance().setDebugMode(true);
            easeUI = EaseUI.getInstance();
            setEaseUIProviders();

        }

        registerMessageListener();

        initDbDao();
    }

    private void initDbDao() {
        inviteMessgeDao = new InviteMessgeDao(appContext);
        userDao = new UserDao(appContext);
    }

    EMConnectionListener connectionListener;

    private void setEaseUIProviders() {
        // set profile provider if you want easeUI to handle avatar and nickname
        easeUI.setUserProfileProvider(new EaseUI.EaseUserProfileProvider() {

            @Override
            public EaseUser getUser(String username) {
                return getUserInfo(username);
            }
        });

        //set options
        easeUI.setSettingsProvider(new EaseUI.EaseSettingsProvider() {

            @Override
            public boolean isSpeakerOpened() {
                return true;
            }

            @Override
            public boolean isMsgVibrateAllowed(EMMessage message) {

                //                boolean isVibrate = MyShare.get(BaseApplication.getContext()).getBoolean(Constance.MESSAGE_VIBRATE);
                return true;
            }

            @Override
            public boolean isMsgSoundAllowed(EMMessage message) {
                //                boolean isSound = MyShare.get(BaseApplication.getContext()).getBoolean(Constance.MESSAGE_SOUND);
                return true;
            }

            @Override
            public boolean isMsgNotifyAllowed(EMMessage message) {
                if (message == null) {
                    //                    boolean isNotify = MyShare.get(BaseApplication.getContext()).getBoolean(Constance.MESSAGE_NOTIFICATION);
                    return true;
                }
                return true;
            }

        });
        //set emoji icon provider
        easeUI.setEmojiconInfoProvider(new EaseUI.EaseEmojiconInfoProvider() {

            @Override
            public EaseEmojicon getEmojiconInfo(String emojiconIdentityCode) {
                return null;
            }

            @Override
            public Map<String, Object> getTextEmojiconMapping() {
                return null;
            }
        });

        //set notification options, will use default if you don't set it
        easeUI.getNotifier().setNotificationInfoProvider(new EaseNotifier.EaseNotificationInfoProvider() {

            @Override
            public String getTitle(EMMessage message) {
                //you can update title here
                return null;
            }

            @Override
            public int getSmallIcon(EMMessage message) {
                //you can update icon here
                return 0;
            }

            @Override
            public String getDisplayedText(EMMessage message) {
                // be used on notification bar, different text according the message type.
                String ticker = EaseCommonUtils.getMessageDigest(message, appContext);
                if (message.getType() == EMMessage.Type.TXT) {
                    ticker = ticker.replaceAll("\\[.{2,3}\\]", "[表情]");
                }
                EaseUser user = getUserInfo(message.getFrom());
                if (user != null) {
                    if (EaseAtMessageHelper.get().isAtMeMsg(message)) {
                        return String.format(appContext.getString(R.string.at_your_in_group), user.getNick());
                    }
                    return user.getNick() + ": " + ticker;
                } else {
                    if (EaseAtMessageHelper.get().isAtMeMsg(message)) {
                        return String.format(appContext.getString(R.string.at_your_in_group), message.getFrom());
                    }
                    return message.getFrom() + ": " + ticker;
                }
            }

            @Override
            public String getLatestText(EMMessage message, int fromUsersNum, int messageNum) {
                return null;
            }

            @Override
            public Intent getLaunchIntent(EMMessage message) {
                // you can set what activity you want display when user click the notification
                Intent intent = new Intent(appContext, ChatActivity.class);
                EMMessage.ChatType chatType = message.getChatType();
                if (chatType == EMMessage.ChatType.Chat) { // single chat message
                    intent.putExtra("userId", message.getFrom());
                    intent.putExtra("chatType", Constance.CHATTYPE_SINGLE);
                } else { // group chat message
                    // message.getTo() is the group id
                    intent.putExtra("userId", message.getTo());
                    if (chatType == EMMessage.ChatType.GroupChat) {
                        intent.putExtra("chatType", Constance.CHATTYPE_GROUP);
                    } else {
                        intent.putExtra("chatType", Constance.CHATTYPE_CHATROOM);
                    }

                }
                return intent;
            }
        });


        connectionListener = new EMConnectionListener() {
            @Override
            public void onDisconnected(int error) {
                EMLog.d("global listener", "onDisconnect" + error);
                if (error == EMError.USER_REMOVED) {//用户被移除
                    onUserException(Constance.ACCOUNT_REMOVED);
                } else if (error == EMError.USER_LOGIN_ANOTHER_DEVICE) {//显示帐号在其他设备登录
                    onUserException(Constance.ACCOUNT_CONFLICT);
                } else if (error == EMError.SERVER_SERVICE_RESTRICTED) {//IM功能限制
                    onUserException(Constance.ACCOUNT_FORBIDDEN);
                }
            }

            @Override
            public void onConnected() {
            }
        };

        EMClient.getInstance().addConnectionListener(new MyConnectionListener());

    }


    private void tipException(final String exception){
//        ShowDialog mDialog=new ShowDialog();
//        mDialog.show(appContext, "提示", "确定退出登录?", new ShowDialog.OnBottomClickListener() {
//            @Override
//            public void positive() {
//                EMClient.getInstance().logout(true, new EMCallBack() {
//
//                    @Override
//                    public void onSuccess() {
//                        // TODO Auto-generated method stub
//                        Log.e("520it", "Setting注销登录");
//                    }
//
//                    @Override
//                    public void onProgress(int progress, String status) {
//                        // TODO Auto-generated method stub
//
//                    }
//
//                    @Override
//                    public void onError(int code, String message) {
//                        // TODO Auto-generated method stub
//                        Log.e("520it", "Setting注销失败:" + message);
//                    }
//                });
//                MyShare.get(appContext).putString(Constance.TOKEN, "");
//                MyShare.get(appContext).putString(Constance.USERNAME, "");
//                MyShare.get(appContext).putString(Constance.USERCODE, "");
//                MyShare.get(appContext).putString(Constance.USERCODEID, "");
//                onUserException(exception);
//
//
//            }
//
//            @Override
//            public void negtive() {
//
//            }
//        });

//        MyToast.show(appContext,exception);
//        MyShare.get(appContext).putString(Constance.TOKEN, "");
//        MyShare.get(appContext).putString(Constance.USERNAME, "");
//        MyShare.get(appContext).putString(Constance.USERCODE, "");
//        MyShare.get(appContext).putString(Constance.USERCODEID, "");
        onUserException(exception);
    }

    //实现ConnectionListener接口
    private class MyConnectionListener implements EMConnectionListener {
        @Override
        public void onConnected() {

        }

        @Override
        public void onDisconnected(final int error) {
            if (error == EMError.USER_REMOVED) {
                onUserException(Constance.ACCOUNT_REMOVED);
            } else if (error == EMError.USER_LOGIN_ANOTHER_DEVICE) {
                onUserException(Constance.ACCOUNT_CONFLICT);
            } else if (error == EMError.SERVER_SERVICE_RESTRICTED) {
                onUserException(Constance.ACCOUNT_FORBIDDEN);

            }
        }
    }

    protected void onUserException(String exception) {
        EMLog.e(TAG, "onUserException: " + exception);
        Intent intent = new Intent(appContext, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(exception, true);
        intent.putExtra(Constance.ACCOUNT_CONFLICT, true);
        intent.putExtra(Constance.ACCOUNT_CONFLICT_INT, 1);
        appContext.startActivity(intent);
        EventBus.getDefault().post(new UserLogin("","1"));
    }

    /**
     * get instance of EaseNotifier
     *
     * @return
     */
    public EaseNotifier getNotifier() {
        return easeUI.getNotifier();
    }

    protected void registerMessageListener() {
        messageListener = new EMMessageListener() {
            private BroadcastReceiver broadCastReceiver = null;

            @Override
            public void onMessageReceived(List<EMMessage> messages) {
                for (EMMessage message : messages) {
                    EMLog.d(TAG, "onMessageReceived id : " + message.getMsgId());
                    if (!easeUI.hasForegroundActivies()) {
                        getNotifier().onNewMsg(message);
                    }

                    String userNice = message.getStringAttribute(Constance.USER_NICE, "");
                    String userPic = message.getStringAttribute(Constance.USER_ICON, "");
                    String userId = message.getStringAttribute(Constance.User_ID, "");
                    EaseUser user1 = new EaseUser(userId);
                    user1.setNickname(userId);
                    user1.setNick(userNice);
                    user1.setAvatar(userPic);
                    saveContact(user1);
                }
            }

            @Override
            public void onCmdMessageReceived(List<EMMessage> messages) {
                for (EMMessage message : messages) {
                    EMLog.d(TAG, "receive command message");
                    //get message body
                    EMCmdMessageBody cmdMsgBody = (EMCmdMessageBody) message.getBody();
                    final String action = cmdMsgBody.action();//获取自定义action

                    if (action.equals("__Call_ReqP2P_ConferencePattern")) {
                        String title = message.getStringAttribute("em_apns_ext", "conference call");
                        Toast.makeText(appContext, title, Toast.LENGTH_LONG).show();
                    }
                    //end of red packet code
                    //获取扩展属性 此处省略
                    //maybe you need get extension of your message
                    //message.getStringAttribute("");
                    EMLog.d(TAG, String.format("Command：action:%s,message:%s", action, message.toString()));
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

        //        EMClient.getInstance().chatManager().addMessageListener(messageListener);
    }


    private EaseUser getUserInfo(String username) {
        // To get instance of EaseUser, here we get it from the user list in memory
        // You'd better cache it if you get it from your server
        EaseUser user = null;
        if (username.equals(EMClient.getInstance().getCurrentUser())) {
            user = new EaseUser(username);
            user.setNick(DemoApplication.mUserObject.getString(Constance.username));
            user.setAvatar(NetWorkConst.SCENE_HOST + DemoApplication.mUserObject.getString(Constance.avatar));
//            if (!AppUtils.isEmpty((IssueApplication.mUserObject)) {
//
//
//            }

        } else {
            Map<String, EaseUser> uses = userDao.getContactList();
            user = uses.get(username);
            if (user == null) {
                user = new EaseUser(username);
                EaseCommonUtils.setUserInitialLetter(user);
            }

        }
        return user;
    }

    public Map<String, EaseUser> getContactList() {

        if (contactList == null) {

            contactList = userDao.getContactList();

        }
        return contactList;

    }


    private EMOptions initChatOptions() {
        Log.d(TAG, "init HuanXin Options");

        EMOptions options = new EMOptions();
        // set if accept the invitation automatically
        options.setAcceptInvitationAlways(false);
        // set if you need read ack
        options.setRequireAck(true);
        // set if you need delivery ack
        options.setRequireDeliveryAck(false);

        //you need apply & set your own id if you want to use google cloud messaging.
        options.setGCMNumber("324169311137");
        //you need apply & set your own id if you want to use Mi push notification
        options.setMipushConfig("2882303761517426801", "5381742660801");
        //you need apply & set your own id if you want to use Huawei push notification
        options.setHuaweiPushAppId("10492024");
        options.setAutoLogin(false);


        return options;
    }


    /**
     * update contact list
     *
     * @param aContactList
     */
    public void setContactList(Map<String, EaseUser> aContactList) {
        userDao.saveContactList(new ArrayList<EaseUser>(aContactList.values()));
    }

    /**
     * save single contact
     */
    public void saveContact(EaseUser user) {
        userDao.saveContact(user);
    }

}
