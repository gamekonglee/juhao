package bc.juhao.com.ui.activity.programme;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.ImageView;

import bc.juhao.com.R;
import bc.juhao.com.controller.programme.SelectSchemeController;
import bocang.view.BaseActivity;

/**
 * @author: Jun
 * @date : 2017/3/14 11:01
 * @description :选择方案类型
 */
public class SelectSchemeActivity extends BaseActivity {
    private SelectSchemeController mController;
    private ImageView iv_save;

    @Override
    protected void InitDataView() {

    }

    @Override
    protected void initController() {
        mController=new SelectSchemeController(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void initView() {
        setContentView(R.layout.activity_scheme_type);
        setColor(this,getResources().getColor(R.color.green));
        iv_save=getViewAndClick(R.id.iv_save);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onViewClick(View v) {
        switch (v.getId()){
            case R.id.iv_save:
            mController.saveScheme();
            break;
        }
    }
}
