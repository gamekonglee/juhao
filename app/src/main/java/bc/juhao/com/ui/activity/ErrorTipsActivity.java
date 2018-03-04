package bc.juhao.com.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import bc.juhao.com.R;
import bc.juhao.com.common.BaseActivity;

/**
 * Created by bocang on 18-2-8.
 */

public class ErrorTipsActivity extends BaseActivity {
    @Override
    protected void InitDataView() {

    }

    @Override
    protected void initController() {

    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_error);
    }

    @Override
    protected void initData() {

    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fullScreen(this);
    }
}
