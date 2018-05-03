package bc.juhao.com.ui.activity.user;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import bc.juhao.com.R;
import bc.juhao.com.bean.BankInfo;
import bc.juhao.com.common.BaseActivity;
import bc.juhao.com.controller.GongZhangController;

public class TixianGzActivity extends BaseActivity {

    private GongZhangController mController;
    private boolean noEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void InitDataView() {

    }

    @Override
    protected void initController() {
        mController = new GongZhangController(this);
        noEdit = getIntent().getBooleanExtra("NoEdit",false);
        if(noEdit){
            mController.btn_subimit.setEnabled(false);
            BankInfo bankInfo= (BankInfo) getIntent().getSerializableExtra("bank");
            mController.et_company.setText(bankInfo.getCompany());
            mController.et_bank_name.setText(bankInfo.getBank());
            mController.et_bank_account.setText(bankInfo.getAccount());
            mController.et_name.setText(bankInfo.getName());
            mController.et_tel.setText(bankInfo.getPhone());
        }else {
            mController.btn_subimit.setEnabled(true);
        }
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_tixian_gz);
        setColor(this, Color.WHITE);


    }

    @Override
    protected void initData() {

    }
}
