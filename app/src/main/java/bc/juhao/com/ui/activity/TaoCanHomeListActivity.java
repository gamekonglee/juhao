package bc.juhao.com.ui.activity;

import android.graphics.Color;

import bc.juhao.com.R;
import bc.juhao.com.common.BaseActivity;
import bc.juhao.com.controller.TaoCanHomeController;

/**
 * Created by gamekonglee on 2018/6/13.
 */

public class TaoCanHomeListActivity extends BaseActivity {

    private TaoCanHomeController controller;

    @Override
    protected void InitDataView() {

    }

    @Override
    protected void initController() {
        controller = new TaoCanHomeController(this);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_taocan_home);
        setColor(this, Color.WHITE);
    }

    @Override
    protected void initData() {

    }
}
