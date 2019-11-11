package bc.juhao.com.controller;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import bc.juhao.com.R;
import bc.juhao.com.cons.Constance;
import bc.juhao.com.listener.INetworkCallBack;
import bc.juhao.com.ui.activity.user.TixianGzActivity;
import bc.juhao.com.utils.UIUtils;
import bocang.json.JSONObject;
import bocang.utils.MyToast;

/**
 * Created by gamekonglee on 2018/3/30.
 */

public class GongZhangController  extends BaseController{

    private final TixianGzActivity mView;
    public EditText et_company;
    public EditText et_bank_name;
    public EditText et_bank_account;
    public EditText et_name;
    public EditText et_tel;
    public Button btn_subimit;

    public GongZhangController(TixianGzActivity tixianGzActivity) {
        mView = tixianGzActivity;
        initView();
    }

    private void initView() {
        et_company = mView.findViewById(R.id.et_company);
        et_bank_name = mView.findViewById(R.id.et_bank_name);
        et_bank_account = mView.findViewById(R.id.et_bank_account);
        et_name = mView.findViewById(R.id.et_name);
        et_tel = mView.findViewById(R.id.et_tel);
        btn_subimit = mView.findViewById(R.id.btn_subimit);
        btn_subimit.setOnClickListener(new View.OnClickListener() {

            private ProgressDialog pd;

            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(et_company.getText())){
                    MyToast.show(mView,"公司名称必填");
                    return;
                }
                if(TextUtils.isEmpty(et_bank_name.getText())){
                    MyToast.show(mView,"开户银行必填");
                    return;
                }
                if(TextUtils.isEmpty(et_bank_account.getText())){
                    MyToast.show(mView,"账号必填");
                    return;
                }
                if(TextUtils.isEmpty(et_name.getText())){
                    MyToast.show(mView,"联系人必填");
                    return;
                }
                if(TextUtils.isEmpty(et_tel.getText())){
                    MyToast.show(mView,"手机号必填");
                    return;
                }
                String company=et_company.getText().toString().trim();
                String bank=et_bank_name.getText().toString().trim();
                String account=et_bank_account.getText().toString().trim();
                String name=et_name.getText().toString().trim();
                String tel=et_tel.getText().toString().trim();
                pd = ProgressDialog.show(mView,"","请稍等");
                mNetWork.sendAddAccount(company,bank,account,name,tel, new INetworkCallBack() {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
                    @Override
                    public void onSuccessListener(String requestCode, JSONObject ans) {
                        if(UIUtils.isValidContext(mView)&&pd!=null&&pd.isShowing()){
                            pd.dismiss();
                        }
                        if(ans!=null){
                            if(ans.getInt(Constance.error_code)==0){
                                MyToast.show(mView,"添加成功");
                                mView.finish();
                            }
                        }
                    }

                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
                    @Override
                    public void onFailureListener(String requestCode, JSONObject ans) {
                        if(UIUtils.isValidContext(mView)&&pd!=null&&pd.isShowing()){
                            pd.dismiss();
                        }
                    }
                });

            }
        });
    }

    @Override
    protected void handleMessage(int action, Object[] values) {

    }

    @Override
    protected void myHandleMessage(Message msg) {

    }

}
