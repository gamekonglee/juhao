package bc.juhao.com.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import bc.juhao.com.R;
import bc.juhao.com.common.BaseFragment;
import bc.juhao.com.controller.classify.ClassifyGoodsController;

/**
 * @author: Jun
 * @date : 2017/1/20 9:21
 * @description :分类页面-分类一
 */
public class ClassifyGoodsFragment extends BaseFragment implements View.OnClickListener {

    private ClassifyGoodsController mController;
    private LinearLayout all_ll;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fm_classify_goods, null);
    }

    @Override
    protected void initController() {
        mController=new ClassifyGoodsController(this);
    }

    @Override
    protected void initViewData() {

    }

    @Override
    protected void initView() {
        all_ll = (LinearLayout) getActivity().findViewById(R.id.all_ll);
        all_ll.setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.all_ll:
                mController.getAllData();
            break;
        }
    }
}
