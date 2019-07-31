package bc.juhao.com.controller.blance;

import android.content.Intent;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.iot.ilop.demo.DemoApplication;
import com.google.gson.Gson;

import bc.juhao.com.R;
import bc.juhao.com.bean.BankInfo;
import bc.juhao.com.cons.Constance;
import bc.juhao.com.cons.NetWorkConst;
import bc.juhao.com.controller.BaseController;
import bc.juhao.com.listener.INetworkCallBack;
import bc.juhao.com.listener.INetworkCallBack02;
import bc.juhao.com.ui.activity.IssueApplication;
import bc.juhao.com.ui.activity.blance.ExtractMoneyActivity;
import bc.juhao.com.ui.activity.blance.WithDrawalDetailActivity;
import bc.juhao.com.ui.activity.user.TixianGzActivity;
import bc.juhao.com.utils.MyShare;
import bocang.utils.AppUtils;
import bocang.utils.MyToast;

/**
 * @author: Jun
 * @date : 2017/7/10 18:00
 * @description :
 */
public class ExtractMoneyController extends BaseController {
    private ExtractMoneyActivity mView;
    private TextView money_tv,exchange_num_et,alipay_tv;
    private String money;
    private String exchange_num;
    private String alipay;

    public ExtractMoneyController(ExtractMoneyActivity v){
        mView=v;
        initView();
        initViewData();
    }

    private void initViewData() {
        if(AppUtils.isEmpty(DemoApplication.mUserObject))return;
        money= DemoApplication.mUserObject.getString(Constance.money);
        money_tv.setText("￥" + money);

    }

    private void initView() {
        money_tv = (TextView) mView.findViewById(R.id.money_tv);
        exchange_num_et = (TextView) mView.findViewById(R.id.exchange_num_et);
        alipay_tv = (TextView) mView.findViewById(R.id.alipay_tv);
    }

    @Override
    protected void handleMessage(int action, Object[] values) {

    }

    @Override
    protected void myHandleMessage(Message msg) {

    }

    /**
     * 提现余额
     */
    public void WithdrawalsMoney() {
        alipay=alipay_tv.getText().toString().trim();
        exchange_num=exchange_num_et.getText().toString().trim();
        String name= MyShare.get(mView).getString(Constance.ALIPAYNAME);
        if(AppUtils.isEmpty(alipay)){
            MyToast.show(mView,"提现帐号不能为空!");
            return;
        }
        if(AppUtils.isEmpty(exchange_num)){
            MyToast.show(mView,"提现金额不能为空!");
            return;
        }

        if(Float.parseFloat(money)<Float.parseFloat(exchange_num)){
            MyToast.show(mView,"提现金额不能大于余额!");
            return;
        }
//        if(exchange_num.contains(".")){
//            MyToast.show(mView,"提现金额必须为整数");
//            return;
//        }
        mView.setShowDialog(true);
        mView.setShowDialog("正在提现中...");
        mView.showLoading();
        mNetWork.sendAlipayMoney(exchange_num, alipay, name,new INetworkCallBack02() {
            @Override
            public void onSuccessListener(String requestCode, JSONObject ans) {
                mView.hideLoading();
                String state=ans.getString(Constance.state);
                if(state.equals("success")){
                    sendUser();
                }else{
                    MyToast.show(mView,"提现失败!,请重试!");
                }
            }

            @Override
            public void onFailureListener(String requestCode, JSONObject ans) {
                mView.hideLoading();
                if(ans!=null&&ans.getString(Constance.error_desc)!=null){
                    MyToast.show(mView, ""+ans.getString(Constance.error_desc));
                }else {
                MyToast.show(mView, "提现失败!,请重试!");
                }
            }
        });
    }

    /**
     * 获取用户信息
     */
    public void sendUser() {
        mNetWork.sendUser(new INetworkCallBack() {
            @Override
            public void onSuccessListener(String requestCode, bocang.json.JSONObject ans) {
                switch (requestCode) {
                    case NetWorkConst.PROFILE:
                        bocang.json.JSONObject mUserObject = ans.getJSONObject(Constance.user);
                        DemoApplication.mUserObject=mUserObject;
                        money_tv.setText("￥" + money);
                        Intent intent=new Intent(mView, WithDrawalDetailActivity.class);
                        intent.putExtra(Constance.alipay,alipay);
                        intent.putExtra(Constance.money,exchange_num);
                        mView.startActivity(intent);
                        break;
                }
            }

            @Override
            public void onFailureListener(String requestCode, bocang.json.JSONObject ans) {

            }
        });
    }

    public void getAllMoney() {
        exchange_num_et.setText(""+money);
    }

    public void sendAccountList() {
        mNetWork.sendAccountList(new INetworkCallBack() {
            @Override
            public void onSuccessListener(String requestCode, bocang.json.JSONObject ans) {
                if(ans!=null){
                    bocang.json.JSONObject data=ans.getJSONObject(Constance.data);
                    if(data!=null){
                        final BankInfo bankInfo=new Gson().fromJson(data.toString(),BankInfo.class);
                        if(bankInfo!=null){
                            mView.add_tv.setVisibility(View.GONE);
                            mView.alipay_tv.setText(bankInfo.getCompany());
                            mView.alipay_tv.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent=new Intent(mView,TixianGzActivity.class);
                                    intent.putExtra("NoEdit",true);
                                    intent.putExtra("bank",bankInfo);
                                    mView.startActivity(intent);
                                }
                            });
                        }else {
                            mView.add_tv.setVisibility(View.VISIBLE);
                        }
                    }else {
                        mView.add_tv.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailureListener(String requestCode, bocang.json.JSONObject ans) {

            }
        });
    }
}
