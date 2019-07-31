package bc.juhao.com.ui.fragment;

import android.Manifest;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.view.View;

import com.aliyun.iot.ilop.demo.DemoApplication;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.ui.EaseChatFragment;
import com.hyphenate.easeui.widget.chatrow.EaseCustomChatRowProvider;

import bc.juhao.com.cons.Constance;
import bc.juhao.com.cons.NetWorkConst;
import bc.juhao.com.ui.activity.buy.SelectOrderActivity;
import bc.juhao.com.ui.activity.user.UserDataActivity;
import bocang.utils.MyToast;


/**
 * @author: Jun
 * @date : 2017/1/17 11:22
 * @description :
 */
public class ChatLFragment extends EaseChatFragment implements EaseChatFragment.EaseChatFragmentHelper {
    @Override
    protected void setUpView() {
        super.setUpView();
        setChatFragmentListener(this);
        ActivityCompat.requestPermissions(getActivity(),
                new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CALL_PHONE},
                1);


    }

    public void sendOrder(String code) {
        sendTextMessage(code);
    }

    @Override
    public void onSetMessageAttributes(EMMessage message) {
        message.setAttribute(Constance.User_ID, DemoApplication.mUserObject.getString(Constance.id));
        message.setAttribute(Constance.USER_ICON, NetWorkConst.SCENE_HOST + DemoApplication.mUserObject.getString(Constance.avatar));
        message.setAttribute(Constance.USER_NICE, DemoApplication.mUserObject.getString(Constance.username));
    }


    @Override
    public void onEnterToChatDetails() {

    }
    @Override
    public void onSendOrder() {
        Intent intent = new Intent(this.getActivity(), SelectOrderActivity.class);
        this.getActivity().startActivityForResult(intent, 101);
    }


    @Override
    public void onAvatarClick(String username) {
        Intent intent = new Intent(this.getActivity(), UserDataActivity.class);
        intent.putExtra(Constance.user_id, username);
        this.getActivity().startActivity(intent);
    }

    @Override
    public void onAvatarLongClick(String username) {

    }

    @Override
    public boolean onMessageBubbleClick(EMMessage message) {
        return false;
    }

    @Override
    public void onMessageBubbleLongClick(EMMessage message) {
        ClipboardManager cm = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        String msg = message.getBody().toString();
        if (msg.indexOf("txt") != -1) {
            String value = msg.substring(5, msg.length() - 1).trim();
            cm.setText(value);
            MyToast.show(getActivity(), "内容复制成功!");
        } else {
            MyToast.show(getActivity(), "只能复制文本!");
        }

    }

    @Override
    public boolean onExtendMenuItemClick(int itemId, View view) {
        return false;
    }

    @Override
    public EaseCustomChatRowProvider onSetCustomChatRowProvider() {
        return null;
    }
}
