package bc.juhao.com.ui.activity.programme;

import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import bc.juhao.com.R;
import bc.juhao.com.controller.programme.ThreeDimensionalSceneController;
import bocang.view.BaseActivity;

public class ThreeDimensionalSceneActivity extends BaseActivity {

    private ThreeDimensionalSceneController mController;

    @Override
    protected void InitDataView() {

    }

    @Override
    protected void initController() {
        mController = new ThreeDimensionalSceneController(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void initView() {
        setContentView(R.layout.activity_three_dimensional_scene);
        setColor(this, Color.WHITE);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onViewClick(View v) {

    }
}
