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
import bc.juhao.com.common.BaseFragment;
import bc.juhao.com.controller.user.Order02Controller;

/**
 * @author: Jun
 * @date : 2017/2/6 13:50
 * @description :
 */
public class Order02Fragment extends BaseFragment {
    public Order02Controller mController;
    public List<String> list = new ArrayList<String>();
    public int flag;
    public Boolean isSearch=false;
    public JSONObject mSearchOrder;
    public String mOrdersn="";

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





    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fm_order02, null);
    }

    @Override
    protected void initController() {
        mController=new Order02Controller(this);
    }

    @Override
    protected void initViewData() {

    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }

    public static Order02Fragment newInstance(List<String> contentList, int flag){
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("content", (ArrayList<String>) contentList);
        bundle.putInt("flag", flag);
        Order02Fragment orderFm = new Order02Fragment();
        orderFm.setArguments(bundle);
        return orderFm;

    }
}
