package bc.juhao.com.controller.blance;

import android.os.Message;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;

import bc.juhao.com.R;
import bc.juhao.com.cons.Constance;
import bc.juhao.com.controller.BaseController;
import bc.juhao.com.listener.INetworkCallBack02;
import bc.juhao.com.ui.activity.IssueApplication;
import bc.juhao.com.ui.activity.blance.UserFinanceActivity;
import bocang.utils.AppUtils;

/**
 * @author: Jun
 * @date : 2017/7/10 17:56
 * @description :
 */
public class UserFinanceController extends BaseController {
    private UserFinanceActivity mView;
    private TextView user_fnc_txtBalance,user_fnc_txtBonusSales,user_fnc_txtBonusPlatform;

    public  UserFinanceController(UserFinanceActivity v){
        mView=v;
        initView();
//        initViewData();
    }

    public void initViewData() {
        if(AppUtils.isEmpty(IssueApplication.mUserObject))return;
        String money= IssueApplication.mUserObject.getString(Constance.money);
        user_fnc_txtBalance.setText("￥" + money);
        sendSalesMoney();

    }

    /**
     * 获取销售数据
     */
    private void sendSalesMoney(){
        mNetWork.sendSalesMoney(new INetworkCallBack02() {
            @Override
            public void onSuccessListener(String requestCode, JSONObject ans) {
                String comission=ans.getString(Constance.commission);
                user_fnc_txtBonusSales.setText(comission);
                user_fnc_txtBonusPlatform.setText(ans.getString(Constance.amount));
            }

            @Override
            public void onFailureListener(String requestCode, JSONObject ans) {

            }
        });
    }


    private void initView() {
        user_fnc_txtBalance = (TextView) mView.findViewById(R.id.user_fnc_txtBalance);
        user_fnc_txtBonusSales = (TextView) mView.findViewById(R.id.user_fnc_txtBonusSales);
        user_fnc_txtBonusPlatform = (TextView) mView.findViewById(R.id.user_fnc_txtBonusPlatform);
    }

    @Override
    protected void handleMessage(int action, Object[] values) {

    }

    @Override
    protected void myHandleMessage(Message msg) {

    }
}
