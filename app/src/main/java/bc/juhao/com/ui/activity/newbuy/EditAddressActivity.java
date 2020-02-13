package bc.juhao.com.ui.activity.newbuy;

import android.annotation.TargetApi;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import bc.juhao.com.R;
import bc.juhao.com.controller.AddressController;
import bc.juhao.com.controller.EditAddressController;
import bc.juhao.com.controller.user.CollectController;
import bocang.view.BaseActivity;

/**
 * author: cjt
 * date:  2019-12-05$
 * ClassName: AddressActivity$
 * Description:
 */
public class EditAddressActivity extends BaseActivity {

    private EditAddressController editAddressController;
    private ImageView iv_radiobuttom;
    private TextView tv_commit;
    private boolean flag_default = true;


    @Override
    protected void InitDataView() {

    }

    @Override
    protected void initController() {
        editAddressController = new EditAddressController(this);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_editaddress);
        setColor(this, Color.WHITE);
        iv_radiobuttom = getViewAndClick(R.id.iv_radiobuttom);
        tv_commit = getViewAndClick(R.id.tv_commit);

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onViewClick(View v) {
        switch (v.getId()){
            case R.id.iv_radiobuttom:
                flag_default = !flag_default;
                if (flag_default){
                    iv_radiobuttom.setImageDrawable(getResources().getDrawable(R.mipmap.home_icon_k));
                }else {

                    iv_radiobuttom.setImageDrawable(getResources().getDrawable(R.mipmap.home_icon_g));
                }
                break;
            case R.id.tv_commit:
                editAddressController.commit("","","","",flag_default);
                break;
        }
    }
}
