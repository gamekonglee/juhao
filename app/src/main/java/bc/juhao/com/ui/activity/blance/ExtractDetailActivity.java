package bc.juhao.com.ui.activity.blance;

import android.view.View;

import bc.juhao.com.R;
import bc.juhao.com.controller.blance.ExtractDetailController;
import bocang.view.BaseActivity;

/**
 * @author: Jun
 * @date : 2017/7/6 16:34
 * @description :
 */
public class ExtractDetailActivity  extends BaseActivity {
    private ExtractDetailController mController;

    @Override
    protected void InitDataView() {

    }

    @Override
    protected void initController() {
        mController=new ExtractDetailController(this);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_exchange_detail);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onViewClick(View v) {

    }


}
