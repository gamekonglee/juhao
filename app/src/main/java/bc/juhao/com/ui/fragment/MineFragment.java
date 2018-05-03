package bc.juhao.com.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import bc.juhao.com.R;
import bc.juhao.com.common.BaseFragment;
import bc.juhao.com.cons.Constance;
import bc.juhao.com.controller.user.MineController;
import bc.juhao.com.ui.activity.IssueApplication;
import bc.juhao.com.ui.activity.MainActivity;
import bc.juhao.com.ui.activity.blance.UserFinanceActivity;
import bc.juhao.com.ui.activity.buy.ShoppingCartActivity;
import bc.juhao.com.ui.activity.user.ZujiActivity;
import bocang.utils.AppUtils;
import bocang.utils.IntentUtil;
import bocang.utils.MyToast;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * @author Jun
 * @time 2017/1/5  12:00
 * @desc 我的页面
 */
public class MineFragment extends BaseFragment implements View.OnClickListener {
    private CircleImageView head_cv;
    private RelativeLayout order_rl;
    public LinearLayout  collect_ll, address_ll, stream_ll, message_ll,share_ll,user_money_ll,call_kefu_ll,cart_ll
            ,two_code_ll,setting_ll,distributor_ll,service_ll,call_kefu02_ll,setting02_ll,mine03_lv,mine04_lv;
    private MineController mController;
    private RelativeLayout payment_rl,delivery_rl,Receiving_rl;
    private LinearLayout pingtuan_ll;
    private LinearLayout zuji_ll;
    private LinearLayout setting_ll_new;
    public View view_empty;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        ((MainActivity)getActivity()).setFullScreenColor(Color.TRANSPARENT,getActivity());

        return inflater.inflate(R.layout.fm_mine_new, null);
    }

    @Override
    protected void initController() {
        mController=new MineController(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        mController.sendUser();
        initViewData();
    }

    @Override
    protected void initViewData() {
       onRefresh();
    }

    public void onRefresh() {
        isToken();
        if(AppUtils.isEmpty( IssueApplication.mUserObject)) return;
        int level=IssueApplication.mUserObject.getInt(Constance.level);
        if(level>2){
            mine03_lv.setVisibility(View.GONE);
            distributor_ll.setVisibility(View.GONE);
            view_empty.setVisibility(View.VISIBLE);
//            mine04_lv.setVisibility(View.VISIBLE);
        }else{
            mine03_lv.setVisibility(View.VISIBLE);
            mine04_lv.setVisibility(View.GONE);
            distributor_ll.setVisibility(View.VISIBLE);
            view_empty.setVisibility(View.GONE);
        }

    }

    @Override
    protected void initView() {
        head_cv = (CircleImageView) getActivity().findViewById(R.id.head_user_cv);
        setting_ll = (LinearLayout) getActivity().findViewById(R.id.setting_ll);
        setting02_ll = (LinearLayout) getActivity().findViewById(R.id.setting02_ll);
        order_rl = (RelativeLayout) getActivity().findViewById(R.id.order_rl);
        collect_ll = (LinearLayout) getActivity().findViewById(R.id.collect_ll);
        address_ll = (LinearLayout) getActivity().findViewById(R.id.address_ll);
        stream_ll = (LinearLayout) getActivity().findViewById(R.id.stream_ll);
        message_ll = (LinearLayout) getActivity().findViewById(R.id.message_ll);
        share_ll = (LinearLayout) getActivity().findViewById(R.id.share_ll);
        user_money_ll = (LinearLayout) getActivity().findViewById(R.id.user_money_ll);
        payment_rl = (RelativeLayout) getActivity().findViewById(R.id.payment_rl);
        delivery_rl = (RelativeLayout) getActivity().findViewById(R.id.delivery_rl);
        Receiving_rl = (RelativeLayout) getActivity().findViewById(R.id.Receiving_rl);
        call_kefu_ll = (LinearLayout) getActivity().findViewById(R.id.call_kefu_ll);
        call_kefu02_ll = (LinearLayout) getActivity().findViewById(R.id.call_kefu02_ll);
        two_code_ll = (LinearLayout) getActivity().findViewById(R.id.two_code_ll);
        distributor_ll = (LinearLayout) getActivity().findViewById(R.id.distributor_ll);
        service_ll = (LinearLayout) getActivity().findViewById(R.id.service_ll);
        cart_ll = (LinearLayout) getActivity().findViewById(R.id.cart_ll);
        mine03_lv = (LinearLayout) getActivity().findViewById(R.id.mine03_lv);
        mine04_lv = (LinearLayout) getActivity().findViewById(R.id.mine04_lv);
        pingtuan_ll = getView().findViewById(R.id.pingtuan_ll);
        view_empty = getView().findViewById(R.id.view_empty);
        zuji_ll = getView().findViewById(R.id.zuji_ll);
        setting_ll_new = getView().findViewById(R.id.setting_ll_new);
        pingtuan_ll.setOnClickListener(this);
        zuji_ll.setOnClickListener(this);
        setting_ll_new.setOnClickListener(this);
        head_cv.setOnClickListener(this);
        setting_ll.setOnClickListener(this);
        setting02_ll.setOnClickListener(this);
        order_rl.setOnClickListener(this);
        collect_ll.setOnClickListener(this);
        address_ll.setOnClickListener(this);
        stream_ll.setOnClickListener(this);
        message_ll.setOnClickListener(this);
        payment_rl.setOnClickListener(this);
        delivery_rl.setOnClickListener(this);
        Receiving_rl.setOnClickListener(this);
        share_ll.setOnClickListener(this);
        user_money_ll.setOnClickListener(this);
        call_kefu_ll.setOnClickListener(this);
        call_kefu02_ll.setOnClickListener(this);
        cart_ll.setOnClickListener(this);
        two_code_ll.setOnClickListener(this);
        distributor_ll.setOnClickListener(this);
        service_ll.setOnClickListener(this);

    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_user_cv://头像
                mController.setHead();
                break;
            case R.id.setting_ll://设置
            case R.id.setting_ll_new:
                mController.setSetting();
                break;
            case R.id.setting02_ll://设置
                mController.setSetting();
                break;
            case R.id.two_code_ll://邀请码
                mController.getInvitationCode();
                break;
            case R.id.distributor_ll://我的分销商
                mController.getMyistributor();
                break;
            case R.id.service_ll://服务
                mController.getService();
                break;
            case R.id.order_rl://我的订单
                mController.setOrder();
                break;
            case R.id.collect_ll://我的收藏
                mController.setCollect();
                break;
            case R.id.address_ll://管理收货地址
                mController.setAddress();
                break;
            case R.id.stream_ll://管理物流地址
                mController.setStream();
                break;
            case R.id.message_ll://消息中心
                mController.SetMessage();
                break;
            case R.id.payment_rl://待付款
                mController.setPayMen();
                break;
            case R.id.delivery_rl://待发货
                mController.setDelivery();
                break;
            case R.id.Receiving_rl://待收货
                mController.setReceiving();
                break;
            case R.id.share_ll://分享给好友
                mController.getShareApp();
                break;
            case R.id.user_money_ll://余额
                IntentUtil.startActivity(getActivity(), UserFinanceActivity.class, false);
                break;
            case R.id.call_kefu_ll://客服中心
                mController.sendCall("尝试连接聊天服务..请连接?");
                break;
            case R.id.call_kefu02_ll://客服中心
                mController.sendCall("尝试连接聊天服务..请连接?");
                break;
            case R.id.cart_ll://购物车
                IntentUtil.startActivity(getActivity(), ShoppingCartActivity.class, false);
                break;
            case R.id.zuji_ll://我的足迹
//                MyToast.show(getContext(),"该功能尚未开放");
                IntentUtil.startActivity(getActivity(), ZujiActivity.class,false);
                break;
            case R.id.pingtuan_ll:
                MyToast.show(getContext(),"该功能尚未开放");
                break;
        }
    }
}
