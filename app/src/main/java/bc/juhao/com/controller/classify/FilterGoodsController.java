package bc.juhao.com.controller.classify;

import android.content.Intent;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import bc.juhao.com.R;
import bc.juhao.com.bean.AttrList;
import bc.juhao.com.cons.Constance;
import bc.juhao.com.cons.NetWorkConst;
import bc.juhao.com.controller.BaseController;
import bc.juhao.com.listener.INetworkCallBack;
import bc.juhao.com.ui.activity.buy.FilterTypeActivity;
import bc.juhao.com.ui.activity.product.SelectGoodsActivity;
import bc.juhao.com.ui.adapter.FilterGoodsAdapter;
import bc.juhao.com.ui.fragment.FilterGoodsFragment;
import bocang.json.JSONArray;
import bocang.json.JSONObject;
import bocang.utils.AppUtils;

/**
 * @author: Jun
 * @date : 2017/3/7 17:39
 * @description :
 */
public class FilterGoodsController extends BaseController implements INetworkCallBack, AdapterView.OnItemClickListener {
    private FilterGoodsFragment mView;
    private ListView listView;
    public FilterGoodsAdapter mAdapter;
    public JSONArray mFilterList;
    private Intent mIntent;
    private ArrayList<AttrList> mAttrListList=new ArrayList<>();


    public FilterGoodsController(FilterGoodsFragment v) {
        mView = v;
        initView();
        initViewData();
    }

    private void initViewData() {


    }

    private void initView() {
        listView = (ListView) mView.getActivity().findViewById(R.id.listView);
        mAdapter=new FilterGoodsAdapter(mView.getActivity());
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(this);
    }

    public void sendAttrList() {
        mNetWork.sendAttrList(this) ;

    }


    @Override
    protected void handleMessage(int action, Object[] values) {

    }

    @Override
    protected void myHandleMessage(Message msg) {

    }

    @Override
    public void onSuccessListener(String requestCode, JSONObject ans) {
       mView.showContentView();
        switch (requestCode) {
            case NetWorkConst.ATTRLIST:
                mFilterList=ans.getJSONArray(Constance.goods_attr_list);

                mAdapter.setData(mFilterList);
                break;
        }
    }

    @Override
    public void onFailureListener(String requestCode, JSONObject ans) {

        mView.showContentView();
    }

    private int mPosition=0;

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        final JSONObject object = mFilterList.getJSONObject(position);
        final String name=object.getString(Constance.filter_attr_name);
        JSONArray attrArray = object.getJSONArray(Constance.attr_list);
        if(AppUtils.isEmpty(attrArray)) return;
        mPosition=position;
        mIntent =new Intent(mView.getActivity(),FilterTypeActivity.class);
        mIntent.putExtra(Constance.attr_list, attrArray);
        mIntent.putExtra(Constance.filter_attr_name, name);
        mView.getActivity().startActivity(mIntent);
    }

    public void onResume() {
        mAdapter.setAttrList(mView.mAttrList, mPosition);
        mView.mAttrList=null;
    }


    public void clearData() {
        mAdapter.setClearAttrList();
        mAdapter.notifyDataSetChanged();
    }

    public void selectGoods() {
        mIntent = new Intent(mView.getActivity(), SelectGoodsActivity.class);
        String filter="";
        for(int i=0;i<mFilterList.length();i++){
            if(i==mFilterList.length()-1){
                filter+=mAdapter.mAttrList.get(i).getId();
            }else{
                filter+=mAdapter.mAttrList.get(i).getId()+".";
            }
        }
        mIntent.putExtra(Constance.filter_attr, filter);
        mView.getActivity().startActivity(mIntent);
    }
}
