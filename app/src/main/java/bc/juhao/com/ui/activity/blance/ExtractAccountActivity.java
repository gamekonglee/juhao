package bc.juhao.com.ui.activity.blance;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import bc.juhao.com.R;
import bc.juhao.com.cons.Constance;
import bc.juhao.com.ui.view.ShowDialog;
import bc.juhao.com.utils.MyShare;
import bocang.utils.AppUtils;
import bocang.utils.MyToast;
import bocang.view.BaseActivity;

/**
 * @author: Jun
 * @date : 2017/7/6 17:05
 * @description :
 */
public class ExtractAccountActivity extends BaseActivity {
    private EditText alipay_et,alipay_name_tv;
    private Button sure_bt;
     String mAlipay;
     String mAlipayName;

    @Override
    protected void InitDataView() {
        mAlipay=MyShare.get(ExtractAccountActivity.this).getString(Constance.ALIPAY);
        mAlipayName=MyShare.get(ExtractAccountActivity.this).getString(Constance.ALIPAYNAME);
        alipay_et.setText(mAlipay);
        alipay_name_tv.setText(mAlipayName);
    }

    @Override
    protected void initController() {

    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_extract_account);
        alipay_et = (EditText)findViewById(R.id.alipay_et);
        alipay_name_tv = (EditText)findViewById(R.id.alipay_name_tv);
        sure_bt=getViewAndClick(R.id.sure_bt);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onViewClick(View v) {
        switch (v.getId()){
            case R.id.sure_bt:
                final String alipay=alipay_et.getText().toString().trim();
                final String alipayName=alipay_name_tv.getText().toString().trim();
                if(AppUtils.isEmpty(alipay)){
                    MyToast.show(this,"支付宝帐号不能为空!");
                    return;
                }
                if(AppUtils.isEmpty(alipay)){
                    MyToast.show(this,"支付宝名称不能为空!");
                    return;
                }
                ShowDialog mDialog=new ShowDialog();
                mDialog.show(this, "提示", "请检查输入的支付宝信息是否正确？", new ShowDialog.OnBottomClickListener() {
                    @Override
                    public void positive() {
                        MyShare.get(ExtractAccountActivity.this).putString(Constance.ALIPAY,alipay);
                        MyShare.get(ExtractAccountActivity.this).putString(Constance.ALIPAYNAME,alipayName);
                        finish();
                    }

                    @Override
                    public void negtive() {

                    }
                });
            break;
        }
    }
}
