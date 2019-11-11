package bc.juhao.com.ui.activity;

import bc.juhao.com.R;
import bc.juhao.com.common.BaseActivity;
import bc.juhao.com.controller.TaoCanController;

/**
 * Created by gamekonglee on 2018/5/16.
 */

public class TaoCanListActivity  extends BaseActivity {

    private TaoCanController mController;

    @Override
    protected void InitDataView() {

    }

    @Override
    protected void initController() {
        mController = new TaoCanController(this);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_taocan);
    }

    @Override
    protected void initData() {

    }
}
