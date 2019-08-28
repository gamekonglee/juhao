package bc.juhao.com.ui.activity.intelligence;

import android.widget.TextView;

import bc.juhao.com.R;
import bc.juhao.com.common.BaseActivity;
import bc.juhao.com.utils.UIUtils;


public class AboutActivity extends BaseActivity {
    @Override
    protected void InitDataView() {

    }

    @Override
    protected void initController() {

    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_about);
        TextView tv_version=findViewById(R.id.tv_version);
        tv_version.setText("钜豪智慧家庭v"+ UIUtils.getVerName(this));
    }

    @Override
    protected void initData() {

    }
}
