package bc.juhao.com.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import bc.juhao.com.R;
import bc.juhao.com.bean.AttrList;
import bc.juhao.com.common.BaseFragment;
import bc.juhao.com.controller.classify.FilterGoodsController;
import bocang.utils.AppUtils;

/**
 * @author: Jun
 * @date : 2017/1/20 9:21
 * @description :分类页面-筛选页面
 */
public class FilterGoodsFragment extends BaseFragment implements View.OnClickListener {
    private FilterGoodsController mController;
    public static AttrList mAttrList;
    private LinearLayout clear_ll;
    private Button btn_ok;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_screen, null);
    }

    @Override
    protected void initController() {
        mController=new FilterGoodsController(this);
    }

    @Override
    protected void initViewData() {

    }

    @Override
    protected void initView() {
        btn_ok = (Button) getActivity().findViewById(R.id.btn_ok);
        btn_ok.setOnClickListener(this);
        clear_ll = (LinearLayout) getActivity().findViewById(R.id.clear_ll);
        clear_ll.setOnClickListener(this);

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_ok:
                mController.selectGoods();
                break;
            case R.id.clear_ll:
                mController.clearData();
                break;
        }
    }


    @Override
    protected void initData() {

    }

    @Override
    public void onStart() {
        super.onStart();
        if(!AppUtils.isEmpty(mController.mFilterList)){
            mController.mAdapter.setData(mController.mFilterList);
        }else{
            this.showLoadingPage("",R.drawable.ic_loading);
            mController.sendAttrList();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(AppUtils.isEmpty(mAttrList)) return;
        mController.onResume();

    }
}
