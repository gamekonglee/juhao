package bc.juhao.com.ui.activity;

import bc.juhao.com.R;
import bc.juhao.com.common.BaseActivity;
import bc.juhao.com.controller.ArtiController;

/**
 * Created by bocang on 18-2-8.
 */

public class ArtiActivity extends BaseActivity {

    private ArtiController mController;

    @Override
    protected void InitDataView() {

    }

    @Override
    protected void initController() {
        mController = new ArtiController(this);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_artis);

//        QuickAdapter quickAdapter=new QuickAdapter<ArticlesBean>

    }

    @Override
    protected void initData() {

    }

}
