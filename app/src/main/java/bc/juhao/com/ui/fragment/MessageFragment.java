package bc.juhao.com.ui.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Pair;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aliyun.iot.ilop.demo.DemoApplication;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMConversationListener;
import com.hyphenate.EMError;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.model.EaseAtMessageHelper;
import com.hyphenate.easeui.ui.EaseBaseFragment;
import com.hyphenate.easeui.widget.EaseConversationList;
import com.hyphenate.exceptions.HyphenateException;
import com.hyphenate.util.NetUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import bc.juhao.com.R;
import bc.juhao.com.chat.db.InviteMessgeDao;
import bc.juhao.com.cons.Constance;
import bc.juhao.com.controller.user.MessageController;
import bc.juhao.com.ui.activity.IssueApplication;
import bc.juhao.com.ui.view.ShowDialog;
import bc.juhao.com.utils.MyShare;
import bocang.utils.AppUtils;
import bocang.utils.MyLog;
import bocang.utils.MyToast;

/**
 * @author Administrator
 * @version $Rev$
 * @time 2016/11/2 16:13
 * @des ${TODO}
 * @updateAuthor $Author$
 * @updateDate $Date$
 * @updateDes ${TODO}
 */
public class MessageFragment extends EaseBaseFragment {
    private final static int MSG_REFRESH = 2;
    protected EditText query;
    protected ImageButton clearSearch;
    protected boolean hidden;
    protected List<EMConversation> conversationList = new ArrayList<EMConversation>();
    protected EaseConversationList conversationListView;
    protected FrameLayout errorItemContainer;
    private RelativeLayout detail_rl;
    private MessageController mController;
    private TextView errorText;
    private LinearLayout errorContainer;
    private boolean isConnecting = false;//是否正在连接1
    private RelativeLayout reflesh_rl;
    protected boolean isConflict;

    protected EMConversationListener convListener = new EMConversationListener(){

        @Override
        public void onCoversationUpdate() {
            refresh();
        }

    };
    private View null_view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fm_message, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        if(savedInstanceState != null && savedInstanceState.getBoolean("isConflict", false))
            return;
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    protected void initView() {
        inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        conversationListView = (EaseConversationList) getView().findViewById(R.id.list);
        null_view = getView().findViewById(R.id.null_view);
        query = (EditText) getView().findViewById(R.id.query);
        // button to clear content in search bar
        clearSearch = (ImageButton) getView().findViewById(R.id.search_clear);
        errorItemContainer = (FrameLayout) getView().findViewById(R.id.fl_error_item);
        View errorView = (LinearLayout) View.inflate(getActivity(), R.layout.em_chat_neterror_item, null);
        errorItemContainer.addView(errorView);
        errorText = (TextView) errorView.findViewById(R.id.tv_connect_errormsg);
        errorContainer = (LinearLayout) errorView.findViewById(R.id.ll_error_container);
        errorItemContainer.setClickable(true);
        reflesh_rl = (RelativeLayout) getActivity().findViewById(R.id.reflesh_rl);
        reflesh_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginHX();
            }
        });
    }


    //登录环信
    private void loginHX() {
        final Toast toast = Toast.makeText(MessageFragment.this.getActivity(),"服务器连接中...!", Toast.LENGTH_SHORT);
        toast.show();
        if (NetUtils.hasNetwork(getActivity())&&!isConnecting) {
            final String uid= MyShare.get(this.getActivity()).getString(Constance.USERID);
            if(AppUtils.isEmpty(uid)){
                return;
            }
            if (!TextUtils.isEmpty(uid)) {
                isConnecting = true;
                getSuccessLogin(uid, toast);
            }
        }
    }

    private void getSuccessLogin(final String uid,final Toast toast) {
        EMClient.getInstance().login(uid, uid, new EMCallBack() {
            @Override
            public void onSuccess() {
                EMClient.getInstance().groupManager().loadAllGroups();
                EMClient.getInstance().chatManager().loadAllConversations();
                MyLog.e("登录环信成功!");
//                MyToast.show(getActivity(), "连接成功!");
                toast.cancel();
                isConnecting = false;
                refresh();
            }

            @Override
            public void onError(int i, String s) {
                if(s.equals("User dosn't exist")){
                    new Thread(new Runnable() {
                        public void run() {
                            try {
                                EMClient.getInstance().createAccount(uid,uid);//同步方法
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        MyLog.e("注册成功!");
                                        getSuccessLogin(uid, toast);
                                    }
                                });

                            } catch (final HyphenateException e) {

                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        MyLog.e("注册失败!");
                                        getConnection();
                                    }
                                });

                            }
                        }
                    }).start();
                }


                MyLog.e("登录环信失败!"+s);
                isConnecting = false;
            }

            @Override
            public void onProgress(int i, String s) {
                isConnecting = false;
            }
        });
    }



    @Override
    public void onStart() {
        super.onStart();
//        mController.sendMessage();
        refreshUIWithMessage();
    }

    @Override
    protected void setUpView() {
//        conversationList = new ArrayList<EMConversation>();
        conversationList.addAll(loadConversationList());
        conversationListView.init(conversationList);
        if(conversationList.size()==0||listItemClickListener==null){
            null_view.setVisibility(View.VISIBLE);
            conversationListView.setVisibility(View.GONE);
            return;
        }
        if(listItemClickListener != null){
            conversationListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    EMConversation conversation = conversationListView.getItem(position);
                    listItemClickListener.onListItemClicked(conversation);
                }
            });
        }


        EMClient.getInstance().addConnectionListener(connectionListener);

        query.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                conversationListView.filter(s);
                if (s.length() > 0) {
                    clearSearch.setVisibility(View.VISIBLE);
                } else {
                    clearSearch.setVisibility(View.INVISIBLE);
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void afterTextChanged(Editable s) {
            }
        });
        clearSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                query.getText().clear();
                hideSoftKeyboard();
            }
        });

        conversationListView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideSoftKeyboard();
                return false;
            }
        });

        registerForContextMenu(conversationListView);

//        registerMessageListener();
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getActivity().getMenuInflater().inflate(R.menu.em_delete_message, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        boolean deleteMessage = false;
        if (item.getItemId() == R.id.delete_message) {
            deleteMessage = true;
        } else if (item.getItemId() == R.id.delete_conversation) {
            deleteMessage = false;
        }
        EMConversation tobeDeleteCons = conversationListView.getItem(((AdapterView.AdapterContextMenuInfo) item.getMenuInfo()).position);
        if (tobeDeleteCons == null) {
            return true;
        }
        if(tobeDeleteCons.getType() == EMConversation.EMConversationType.GroupChat){
            EaseAtMessageHelper.get().removeAtMeGroup(tobeDeleteCons.getUserName());
        }
        try {
            EMClient.getInstance().chatManager().deleteConversation(tobeDeleteCons.getUserName(), deleteMessage);
            InviteMessgeDao inviteMessgeDao = new InviteMessgeDao(getActivity());
            inviteMessgeDao.deleteMessage(tobeDeleteCons.getUserName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        refresh();

        return true;
    }


    protected EMConnectionListener connectionListener = new EMConnectionListener() {

        @Override
        public void onDisconnected(int error) {
            if (error == EMError.USER_REMOVED || error == EMError.USER_LOGIN_ANOTHER_DEVICE || error == EMError.SERVER_SERVICE_RESTRICTED) {
                isConflict = true;
            } else {
                handler.sendEmptyMessage(0);
            }
        }

        @Override
        public void onConnected() {
            handler.sendEmptyMessage(1);
        }
    };
    private EaseConversationListItemClickListener listItemClickListener;

    protected Handler handler = new Handler(){
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:
                    onConnectionDisconnected();
                    break;
                case 1:
                    onConnectionConnected();
                    break;

                case MSG_REFRESH:
                {
                    if(conversationListView!=null){
                    conversationList.clear();
                    conversationList.addAll(loadConversationList());
                        conversationListView.refresh();
                    }
                    break;
                }
                default:
                    break;
            }
        }
    };

    /**
     * connected to server
     */
    protected void onConnectionConnected(){
        errorItemContainer.setVisibility(View.GONE);
        reflesh_rl.setVisibility(View.GONE);
    }

    /**
     * disconnected with server
     */
    protected void onConnectionDisconnected(){
        errorItemContainer.setVisibility(View.VISIBLE);
        reflesh_rl.setVisibility(View.VISIBLE);
        loginHX();
    }

    private void getConnection(){
        ShowDialog mDialog=new ShowDialog();
        mDialog.show(getActivity(), "提示", "连接聊天失败，是否尝试连接?", new ShowDialog.OnBottomClickListener() {
            @Override
            public void positive() {
                loginHX();
            }

            @Override
            public void negtive() {

            }
        });
    }


    /**
     * refresh ui
     */
    public void refresh() {
        if(!handler.hasMessages(MSG_REFRESH)){
            handler.sendEmptyMessage(MSG_REFRESH);
        }
    }

    /**
     * load conversation list
     *
     * @return
    +    */
    protected List<EMConversation> loadConversationList(){
        // get all conversations
        Map<String, EMConversation> conversations = EMClient.getInstance().chatManager().getAllConversations();
        List<Pair<Long, EMConversation>> sortList = new ArrayList<Pair<Long, EMConversation>>();
        /**
         * lastMsgTime will change if there is new message during sorting
         * so use synchronized to make sure timestamp of last message won't change.
         */
        synchronized (conversations) {
            for (EMConversation conversation : conversations.values()) {
                if (conversation.getAllMessages().size() != 0) {
                    sortList.add(new Pair<Long, EMConversation>(conversation.getLastMessage().getMsgTime(), conversation));
                }
            }
        }
        try {
            // Internal is TimSort algorithm, has bug
            sortConversationByLastChatTime(sortList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<EMConversation> list = new ArrayList<EMConversation>();
        for (Pair<Long, EMConversation> sortItem : sortList) {
            list.add(sortItem.second);
        }
        return list;
    }

    /**
     * sort conversations according time stamp of last message
     *
     * @param conversationList
     */
    private void sortConversationByLastChatTime(List<Pair<Long, EMConversation>> conversationList) {
        Collections.sort(conversationList, new Comparator<Pair<Long, EMConversation>>() {
            @Override
            public int compare(final Pair<Long, EMConversation> con1, final Pair<Long, EMConversation> con2) {

                if (con1.first.equals(con2.first)) {
                    return 0;
                } else if (con2.first.longValue() > con1.first.longValue()) {
                    return 1;
                } else {
                    return -1;
                }
            }

        });
    }

    protected void hideSoftKeyboard() {
        if (getActivity().getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getActivity().getCurrentFocus() != null)
                inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        this.hidden = hidden;
        if (!hidden && !isConflict) {
            refresh();
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        if (!hidden) {
            refresh();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EMClient.getInstance().removeConnectionListener(connectionListener);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(isConflict){
            outState.putBoolean("isConflict", true);
        }
    }

    public interface EaseConversationListItemClickListener {
        /**
         * click event for conversation list
         * @param conversation -- clicked item
         */
        void onListItemClicked(EMConversation conversation);
    }

    /**
     * set conversation list item click listener
     * @param listItemClickListener
     */
    public void setConversationListItemClickListener(EaseConversationListItemClickListener listItemClickListener){
        this.listItemClickListener = listItemClickListener;
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
//                for (EMMessage message : messages) {
//                    //                    EMLog.d(TAG, "onMessageReceived id : " + message.getMsgId());
//                    EaseUI.getInstance().getNotifier().onNewMsg(message);
//
//                    String userNice = message.getStringAttribute(Constance.USER_NICE, "");
//                    String userPic = message.getStringAttribute(Constance.USER_ICON, "");
//                    String userId = message.getStringAttribute(Constance.User_ID, "");
//                    EaseUser user1 = new EaseUser(userId);
//                    user1.setNickname(userId);
//                    user1.setNick(userNice);
//                    user1.setAvatar(NetWorkConst.IMAGEBASE + userPic);
//                    DemoHelper.getInstance().saveContact(user1);
//                }
                conversationListView.refresh();
//                int unreadMsgCount=0;
//                for(EMConversation conversation:EMClient.getInstance().chatManager().getAllConversations().values()){
//                    if(conversation.getType() == EMConversation.EMConversationType.ChatRoom)
//                        unreadMsgCount=conversation.getUnreadMsgCount();
//                }
//                EMConversation conversation = EMClient.getInstance().chatManager().getConversation(Constance.User_ID);
//                //获取此会话在本地的所有的消息数量
//                //如果只是获取当前在内存的消息数量，调用
//                AppDialog.messageBox(conversation.getUnreadMsgCount());
            }

            @Override
            public void onCmdMessageReceived(List<EMMessage> messages) {
//                for (EMMessage message : messages) {
//                    //                    EMLog.d(TAG, "receive command message");
//                    //get message body
//                    EMCmdMessageBody cmdMsgBody = (EMCmdMessageBody) message.getBody();
//                    final String action = cmdMsgBody.action();//获取自定义action
//
//                    if (action.equals("__Call_ReqP2P_ConferencePattern")) {
//                        String title = message.getStringAttribute("em_apns_ext", "conference call");
//                        //                        Toast.makeText(appContext, title, Toast.LENGTH_LONG).show();
//                    }
//                    //end of red packet code
//                    //获取扩展属性 此处省略
//                    //maybe you need get extension of your message
//                    //message.getStringAttribute("");
//                    //                    EMLog.d(TAG, String.format("Command：action:%s,message:%s", action, message.toString()));
//                }
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


    private void refreshUIWithMessage() {
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                // refresh unread count
                int unreadMsgCount = 0;
                for (EMConversation conversation : EMClient.getInstance().chatManager().getAllConversations().values()) {
                    unreadMsgCount = conversation.getUnreadMsgCount();
                }
                DemoApplication.unreadMsgCount=unreadMsgCount;
                EventBus.getDefault().post(Constance.MESSAGE);
            }
        });
    }


}
