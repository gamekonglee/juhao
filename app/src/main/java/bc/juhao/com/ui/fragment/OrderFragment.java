package bc.juhao.com.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;

import bc.juhao.com.R;
import bc.juhao.com.controller.user.OrderController;
import bocang.utils.AppUtils;

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

    public static OrderFragment newInstance(List<String> contentList,int flag){
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("content", (ArrayList<String>) contentList);
        bundle.putInt("flag", flag);
        OrderFragment orderFm = new OrderFragment();
        orderFm.setArguments(bundle);
        return orderFm;

    }
}
