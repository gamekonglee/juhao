package bc.juhao.com.ui.activity.buy;

import android.view.View;

import bc.juhao.com.R;
import bc.juhao.com.controller.buy.SelectOrderController;
import bocang.view.BaseActivity;

/**
 * @author Jun
 * @time 2017/12/11  23:19
 * @desc ${TODD}
 */

public class SelectOrderActivity extends BaseActivity {
    private SelectOrderController mController;

    @Override
    protected void InitDataView() {

    }

    @Override
    protected void initController() {
        mController=new SelectOrderController(this);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_select_order);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onViewClick(View v) {

    }
}
