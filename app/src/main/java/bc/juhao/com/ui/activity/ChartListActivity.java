package bc.juhao.com.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.View;

import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCmdMessageBody;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.controller.EaseUI;
import com.hyphenate.easeui.domain.EaseUser;

import java.util.List;

import bc.juhao.com.R;
import bc.juhao.com.chat.DemoHelper;
import bc.juhao.com.cons.Constance;
import bc.juhao.com.ui.activity.user.ChatActivity;
import bc.juhao.com.ui.fragment.MessageFragment;
import bocang.view.BaseActivity;

/**
 * @author: Jun
 * @date : 2017/6/23 14:53
 * @description :
 */
public class ChartListActivity extends BaseActivity
{
    private MessageFragment mFragment;
    @Override
    protected void InitDataView() {
        registerMessageListener();
    }

    @Override
    protected void initController() {

    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_message);
        setColor(this, Color.WHITE);
        //必需继承FragmentActivity,嵌套fragment只需要这行代码
        mFragment=new MessageFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.container,
                mFragment).commitAllowingStateLoss();

        mFragment.setConversationListItemClickListener(new MessageFragment.EaseConversationListItemClickListener() {
            @Override
            public void onListItemClicked(EMConversation conversation) {
                startActivity(new Intent(ChartListActivity.this, ChatActivity.class).putExtra(EaseConstant.EXTRA_USER_ID, conversation.getUserName()));
            }
        });
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onViewClick(View v) {

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
                Log.e("onMessageReceived",messages.size()+"");
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
                mFragment.refresh();
            }

            @Override
            public void onCmdMessageReceived(List<EMMessage> messages) {
                Log.e("onCmdMessageReceived",messages.size()+"");
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
}
