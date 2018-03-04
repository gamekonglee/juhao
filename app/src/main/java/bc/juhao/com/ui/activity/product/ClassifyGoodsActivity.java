package bc.juhao.com.ui.activity.product;

import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import bc.juhao.com.R;
import bc.juhao.com.bean.AttrList;
import bc.juhao.com.controller.classify.ClassifyGoods02Controller;
import bocang.utils.AppUtils;
import bocang.view.BaseActivity;

/**
 * @author: Jun
 * @date : 2017/2/17 14:55
 * @description :
 */
public class ClassifyGoodsActivity extends BaseActivity {
    private ClassifyGoods02Controller mController;
    public static Activity mActivity;
    public static AttrList mAttrList;
    private LinearLayout clear_ll;
    private Button btn_ok;

    @Override
    protected void InitDataView() {

    }

    @Override
    protected void initController() {
        mController=new ClassifyGoods02Controller(this);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_classify);
        mActivity=this;
        btn_ok = (Button)findViewById(R.id.btn_ok);
        btn_ok.setOnClickListener(this);
        clear_ll = (LinearLayout) findViewById(R.id.clear_ll);
        clear_ll.setOnClickListener(this);
    }




    @Override
    protected void initData() {

    }

    @Override
    protected void onViewClick(View v) {
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
