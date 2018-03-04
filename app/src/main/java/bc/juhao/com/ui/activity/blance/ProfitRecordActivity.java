package bc.juhao.com.ui.activity.blance;

import android.view.View;

import bc.juhao.com.R;
import bc.juhao.com.controller.blance.ProfitRecordController;
import bocang.view.BaseActivity;

/**
 * @author: Jun
 * @date : 2017/7/27 17:46
 * @description :
 */
public class ProfitRecordActivity extends BaseActivity {
    private ProfitRecordController mController;



    @Override
    protected void InitDataView() {

    }

    @Override
    protected void initController() {
        mController=new ProfitRecordController(this);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_profit_record);
        //沉浸式状态栏
//        setColor(this, getResources().getColor(R.color.green));
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onViewClick(View v) {

    }
}
