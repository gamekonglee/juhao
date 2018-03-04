package bc.juhao.com.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import bc.juhao.com.R;
import bc.juhao.com.common.BaseActivity;

/**
 * Created by bocang on 18-2-9.
 */

public class ItDevicesActivity extends BaseActivity implements View.OnClickListener{
    @Override
    protected void InitDataView() {

    }

    @Override
    protected void initController() {

    }

    @Override
    protected void initView() {
    setContentView(R.layout.activity_it_devices);
    }

    @Override
    protected void initData() {

    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fullScreen(this);
    }

    @Override
    public void onClick(View view) {
        startActivity(new Intent(this,ErrorTipsActivity.class));
    }
}
