package bc.juhao.com.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSONObject;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import bc.juhao.com.R;
import bc.juhao.com.bean.PayResult;
import bc.juhao.com.cons.Constance;
import bc.juhao.com.controller.user.OrderController;
import bc.juhao.com.ui.activity.user.OrderDetailActivity;
import bocang.utils.AppUtils;
import bocang.utils.MyToast;

/**
 * @author: Jun
 * @date : 2017/2/6 13:50
 * @description :
 */
public class OrderFragment extends OrderBaseFragment {
    public OrderController mController;
    public List<String> list = new ArrayList<String>();
    public int flag;
    public Boolean isSearch=false;
    public JSONObject mSearchOrder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        if(bundle != null){
            list = bundle.getStringArrayList("content");
            flag = bundle.getInt("flag");
        }
        EventBus.getDefault().register(this);
    }




    @Override
    public void onStart() {
        super.onStart();
        if(!AppUtils.isEmpty(mController)){
            mController.initViewData();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fm_order, null);
    }

    @Override
    protected void initController() {

    }

    @Override
    protected void initViewData() {

    }

    @Override
    protected void initView() {
        isPrepared = true;
        lazyLoad();
    }
    Boolean isPrepared=false;

    @Override
    protected void initData() {

    }

    @Override
    protected void lazyLoad() {
        if (!isPrepared || !isVisible) {
            return;
        }
        mController=new OrderController(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isPrepared=false;
    }

    public static OrderFragment newInstance(List<String> contentList, int flag){
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("content", (ArrayList<String>) contentList);
        bundle.putInt("flag", flag);
        OrderFragment orderFm = new OrderFragment();
        orderFm.setArguments(bundle);
        return orderFm;

    }
//    @Subscribe (threadMode = ThreadMode.MAIN)
//    public void resultPay(int resultCode){
//        mController.page = 1;
//        mController.sendOrderList(mController.page);
//        Intent intent = new Intent(getActivity(), OrderDetailActivity.class);
//        intent.putExtra(Constance.order, mController.goodses.getJSONObject(mController.mPosition).toJSONString());
//        intent.putExtra(Constance.state, 1);
//        getActivity().startActivity(intent);
//    }
    @Subscribe (threadMode = ThreadMode.MAIN)
    public void resultPay(PayResult result){
        int state=2;
        Intent intent = new Intent(getContext(), OrderDetailActivity.class);

        if(result.result.equals("0")){
            MyToast.show(getContext(), "支付成功");
            mController.page = 1;
            flag=0;
            mController.sendOrderList(mController.page);
            mController.sendPaySuccess();
        }else if(result.result.equals("-2")){
            state=0;
            MyToast.show(getContext(), "支付失败");
            intent.putExtra(Constance.order, mController.goodses.getJSONObject(mController.mPosition).toJSONString());
            intent.putExtra(Constance.state, state);
            startActivity(intent);
        }else {
            state=0;
            intent.putExtra(Constance.order, mController.goodses.getJSONObject(mController.mPosition).toJSONString());
            intent.putExtra(Constance.state, state);
            startActivity(intent);
        }
    }
}
