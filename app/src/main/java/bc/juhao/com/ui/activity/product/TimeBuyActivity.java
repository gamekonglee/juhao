package bc.juhao.com.ui.activity.product;

import android.graphics.Color;

import bc.juhao.com.R;
import bc.juhao.com.controller.product.TimeBuyController;

/**
 * @author Jun
 * @time 2018/1/8  21:03
 * @desc ${TODD}
 */

public class TimeBuyActivity extends bc.juhao.com.common.BaseActivity {
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
        setColor(this, Color.WHITE);
    }

    @Override
    protected void initData() {

    }

}
