package bc.juhao.com.ui.activity.user;

import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;

import bc.juhao.com.R;
import bc.juhao.com.controller.user.CollectController;
import bocang.view.BaseActivity;

/**
 * @author: Jun
 * @date : 2017/2/13 16:12
 * @description :
 */
public class CollectActivity extends BaseActivity {
    private ImageView iv_edit;
    private CollectController mController;
    private CheckBox checkAll;
    private Button delete_bt;

    @Override
    protected void InitDataView() {

    }

    @Override
    protected void initController() {
        mController = new CollectController(this);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_collect_product);
        iv_edit = getViewAndClick(R.id.iv_edit);
        checkAll = getViewAndClick(R.id.checkAll);
        delete_bt = getViewAndClick(R.id.delete_bt);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onViewClick(View v) {
        switch (v.getId()) {
            case R.id.iv_edit:
                mController.setEdit();
                break;
            case R.id.checkAll:
                mController.setCheckAll();
                break;
            case R.id.delete_bt:
                mController.senDeleteCollect();
                break;
        }
    }
}
