package bc.juhao.com.controller.classify;

import android.content.Intent;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;

import java.util.ArrayList;

import bc.juhao.com.R;
import bc.juhao.com.cons.Constance;
import bc.juhao.com.cons.NetWorkConst;
import bc.juhao.com.controller.BaseController;
import bc.juhao.com.listener.INetworkCallBack;
import bc.juhao.com.ui.activity.IssueApplication;
import bc.juhao.com.ui.activity.product.ClassifyGoodsActivity;
import bc.juhao.com.ui.activity.product.SelectGoodsActivity;
import bc.juhao.com.ui.adapter.ClassifyGoodsAdapter;
import bc.juhao.com.ui.adapter.ItemClassifyAdapter;
import bc.juhao.com.ui.fragment.ClassifyGoodsFragment;
import bocang.json.JSONArray;
import bocang.json.JSONObject;
import bocang.utils.AppUtils;
import bocang.utils.LogUtils;

/**
 * @author: Jun
 * @date : 2017/1/21 9:45
 * @description :
 */
public class ClassifyGoodsController extends BaseController implements INetworkCallBack {
    private ClassifyGoodsFragment mView;
    private GridView itemGridView;
    private ListView recyclerview_category;
    private JSONArray mClassifyGoodsLists;
    private ClassifyGoodsAdapter mAdapter;
    private ArrayList<Boolean> colorList=new ArrayList<>();
    private ItemClassifyAdapter mItemAdapter;
//    private JSONArray categoriesArrays;
    private Intent mIntent;
    private JSONObject goodsAllAttr;


    public ClassifyGoodsController(ClassifyGoodsFragment v){
        mView=v;
        initData();
        initView();
        initViewData();
    }

    private void initData() {
    }


    private void initViewData() {
        mView.showLoadingPage("", R.drawable.ic_loading);
        sendGoodsType();
    }

    private void initView() {
        recyclerview_category = (ListView) mView.getActivity().findViewById(R.id.recyclerview_category);
        itemGridView = (GridView) mView.getActivity().findViewById(R.id.itemGridView02);
        mAdapter=new ClassifyGoodsAdapter(colorList,mView.getActivity());
        recyclerview_category.setAdapter(mAdapter);
        recyclerview_category.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mAdapter.setLineColor(position);
                goodsAllAttr = mClassifyGoodsLists.getJSONObject(position);
                mItemAdapter.setDatas(goodsAllAttr.getJSONArray(Constance.categories));
            }


        });

        mItemAdapter = new ItemClassifyAdapter(mView.getActivity());
        itemGridView.setAdapter(mItemAdapter);


        itemGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                JSONObject categoryObject = goodsAllAttr.getJSONArray(Constance.categories).getJSONObject(position);
                mIntent = new Intent(mView.getActivity(), SelectGoodsActivity.class);
                String categoriesId = categoryObject.getString(Constance.id);
                mIntent.putExtra(Constance.categories, categoriesId);
                mView.getActivity().startActivity(mIntent);
                if(IssueApplication.isClassify==true){
                    IssueApplication.isClassify=false;
                    ClassifyGoodsActivity.mActivity.finish();
                }
            }
        });

//        if (AppUtils.isEmpty(((MainActivity) mView.getActivity()).mCategories))return;
//        mClassifyGoodsLists=((MainActivity) mView.getActivity()).mCategories;
//        mAdapter.setData(((MainActivity) mView.getActivity()).mCategories);
//        //模拟点击第一项
//        recyclerview_category.performItemClick(null, 0, 0);
    }

    /**
     * 产品类别
     */
    private void sendGoodsType() {
        if(!AppUtils.isEmpty(mClassifyGoodsLists)) return;
//        mView.setShowDialog(true);
//        mView.setShowDialog("正在搜索中!");
//        mView.showLoading();
        mNetWork.sendGoodsType(1, 20, null, null, this);
    }

    @Override
    public void onSuccessListener(String requestCode, JSONObject ans) {
        mView.hideLoading();
        mView.showContentView();
        switch (requestCode){
            case NetWorkConst.CATEGORY:
//                LogUtils.logE("category:",ans.toString());
                mClassifyGoodsLists= ans.getJSONArray(Constance.categories);
                if (AppUtils.isEmpty(mClassifyGoodsLists))return;
                mAdapter.setData(mClassifyGoodsLists);
                //模拟点击第一项
                recyclerview_category.performItemClick(null, 0, 0);
            break;
        }
    }

    @Override
    public void onFailureListener(String requestCode, JSONObject ans) {
        mView.hideLoading();
    }


    @Override
    protected void handleMessage(int action, Object[] values) {

    }

    @Override
    protected void myHandleMessage(Message msg) {

    }

    public void getAllData() {
        mIntent = new Intent(mView.getActivity(), SelectGoodsActivity.class);
        String categoriesId = goodsAllAttr.getString(Constance.id);
        mIntent.putExtra(Constance.categories, categoriesId);
        mView.getActivity().startActivity(mIntent);
        if(IssueApplication.isClassify==true){
            IssueApplication.isClassify=false;
            ClassifyGoodsActivity.mActivity.finish();
        }
    }
}
