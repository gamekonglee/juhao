package bc.juhao.com.controller.classify;

import android.content.Intent;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;

import com.aliyun.iot.ilop.demo.DemoApplication;

import java.util.ArrayList;

import bc.juhao.com.R;
import bc.juhao.com.cons.Constance;
import bc.juhao.com.cons.NetWorkConst;
import bc.juhao.com.controller.BaseController;
import bc.juhao.com.listener.INetworkCallBack;
import bc.juhao.com.ui.activity.product.ClassifyGoodsActivity;
import bc.juhao.com.ui.activity.product.SelectGoodsActivity;
import bc.juhao.com.ui.adapter.ClassifyGoodsAdapter;
import bc.juhao.com.ui.adapter.ItemClassifyAdapter;
import bc.juhao.com.ui.fragment.ClassifyGoodsFragment;
import bocang.json.JSONArray;
import bocang.json.JSONObject;
import bocang.utils.AppUtils;
import bocang.utils.LogUtils;
import bocang.utils.MyToast;

/**
 * @author: Jun
 * @date : 2017/1/21 9:45
 * @description :
 */
public class ClassifyGoodsController extends BaseController implements INetworkCallBack {

    private ClassifyGoodsFragment mView;
    private Intent mIntent;

    private ListView recyclerview_category;
    private GridView itemGridView;

    private ClassifyGoodsAdapter mAdapter;
    private ItemClassifyAdapter mItemAdapter;

    private ArrayList<Boolean> colorList = new ArrayList<>();
    private JSONArray mClassifyGoodsLists;//产品类别(风格\类别\空间...)
    private JSONObject goodsAllAttr;//产品类别(风格\类别\空间...)下的分类
    //    private JSONArray categoriesArrays;

    public ClassifyGoodsController(ClassifyGoodsFragment v) {
        mView = v;
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

        mAdapter = new ClassifyGoodsAdapter(colorList, mView.getActivity());
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
                String categoriesId = categoryObject.getString(Constance.id);//右侧商品分类id
                Log.e("cateId", categoriesId);
                mIntent.putExtra(Constance.categories, categoriesId);
                mView.getActivity().startActivity(mIntent);
                if (DemoApplication.isClassify == true) {
                    DemoApplication.isClassify = false;
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
        if (!AppUtils.isEmpty(mClassifyGoodsLists)) return;
//        mView.setShowDialog(true);
//        mView.setShowDialog("正在搜索中!");
//        mView.showLoading();
        mNetWork.sendGoodsType(1, 20, null, null, this);
    }

    @Override
    public void onSuccessListener(String requestCode, JSONObject ans) {
        mView.hideLoading();
        mView.showContentView();
        switch (requestCode) {
            case NetWorkConst.CATEGORY://产品类别(分格\类别\空间...)
//                LogUtils.logE("category:",ans.toString());
                mClassifyGoodsLists = ans.getJSONArray(Constance.categories);
                LogUtils.logE("category:","mClassifyGoodsLists:"+ mClassifyGoodsLists.toString());
                if (AppUtils.isEmpty(mClassifyGoodsLists)) return;
                mAdapter.setData(mClassifyGoodsLists);
                //模拟点击第一项
//                LogUtils.logE("cate",mClassifyGoodsLists.toString());
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
        if (goodsAllAttr == null || goodsAllAttr.getString(Constance.id) == null) {
            MyToast.show(mView.getActivity(), "数据加载中，稍等");
            return;
        }
        String categoriesId = goodsAllAttr.getString(Constance.id);
        mIntent.putExtra(Constance.categories, categoriesId);
        mView.getActivity().startActivity(mIntent);
        if (DemoApplication.isClassify) {
            DemoApplication.isClassify = false;
            ClassifyGoodsActivity.mActivity.finish();
        }
    }
}
