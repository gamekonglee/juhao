package bc.juhao.com.ui.activity.product;

import android.view.View;

import bc.juhao.com.R;
import bc.juhao.com.controller.product.TimeBuyController;
import bocang.view.BaseActivity;

/**
 * @author Jun
 * @time 2018/1/8  21:03
 * @desc ${TODD}
 */

public class TimeBuyActivity extends BaseActivity {
    private TimeBuyController mController;

    @Override
    protected void InitDataView() {

    }

    @Override
    protected void initController() {
        mController=new TimeBuyController(this);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_time_buy);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onViewClick(View v) {

    }
}
