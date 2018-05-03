package bc.juhao.com.wxapi;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;


import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;

import bc.juhao.com.R;
import bc.juhao.com.bean.PayResult;
import bc.juhao.com.common.BaseActivity;
import bc.juhao.com.cons.Constance;


public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";

    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_result);
        api = WXAPIFactory.createWXAPI(this, Constance.APP_ID);
        api.handleIntent(getIntent(), this);
    }
    public void goBack(View v){
        onBackPressed();
    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onReq(BaseReq req) {

    }

    @Override
    public void onResp(BaseResp resp) {
//        Toast.makeText(this,"code"+resp.errCode,Toast.LENGTH_LONG).show();
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            Log.d("520it", "onPayFinish,errCode=" + resp.errCode);
            if (resp.errCode == 0) {
                Toast.makeText(this, "付款成功!", Toast.LENGTH_SHORT).show();
                PayResult payResult=new PayResult();
                payResult.result="0";
                EventBus.getDefault().post(payResult);

                finish();
            } else if (resp.errCode == -2) {
                Toast.makeText(this, "您已取消付款!", Toast.LENGTH_SHORT).show();
                PayResult payResult=new PayResult();
                payResult.result="-2";
                EventBus.getDefault().post(payResult);
                finish();
            } else {
                Toast.makeText(this, "参数错误", Toast.LENGTH_SHORT).show();
                PayResult payResult=new PayResult();
                payResult.result=""+resp.errCode;
                EventBus.getDefault().post(payResult);
                finish();
            }
        } else {
            finish();
        }
    }
}