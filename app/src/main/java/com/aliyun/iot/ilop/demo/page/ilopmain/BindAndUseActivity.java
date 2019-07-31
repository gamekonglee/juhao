package com.aliyun.iot.ilop.demo.page.ilopmain;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.aliyun.alink.business.devicecenter.api.discovery.IOnDeviceTokenGetListener;
import com.aliyun.alink.business.devicecenter.api.discovery.LocalDeviceMgr;
import com.aliyun.alink.linksdk.tmp.TmpSdk;
import com.aliyun.alink.linksdk.tmp.api.TmpInitConfig;
import com.aliyun.alink.linksdk.tmp.device.panel.PanelDevice;
import com.aliyun.iot.aep.component.router.Router;
import com.aliyun.iot.aep.sdk.apiclient.IoTAPIClient;
import com.aliyun.iot.aep.sdk.apiclient.IoTAPIClientFactory;
import com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback;
import com.aliyun.iot.aep.sdk.apiclient.callback.IoTResponse;
import com.aliyun.iot.aep.sdk.apiclient.request.IoTRequest;
import com.aliyun.iot.aep.sdk.apiclient.request.IoTRequestBuilder;
import com.aliyun.iot.aep.sdk.framework.AActivity;
import com.aliyun.iot.aep.sdk.log.ALog;

import java.util.HashMap;
import java.util.Map;

import bc.juhao.com.R;


public class BindAndUseActivity extends AActivity {
    private String TAG = BindAndUseActivity.class.getSimpleName();
    private Button bindAndUseBtn;
    private Button mBackBtn;
    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.bind_and_use_activity);
        mBackBtn = (Button) findViewById(R.id.ilop_bind_back_btn);
        mBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        String pk = "";
        String dn = "";
//        String token = "";
        Bundle data = getIntent().getExtras();

        if (null != data) {
            pk = data.getString("productKey");
            dn = data.getString("deviceName");
//            token = data.getString("token");
        }

        final String productKey = pk;
        final String deviceName = dn;
//        final String iotToken = token;
        bindAndUseBtn = (Button) findViewById(R.id.bind_and_use_btn);
        bindAndUseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ALog.d("TAG", "LocalDeviceMgr.getInstance().getDeviceToken");
                LocalDeviceMgr.getInstance().getDeviceToken(productKey, deviceName, 2, new IOnDeviceTokenGetListener() {
                    @Override
                    public void onSuccess(String token) {
                        ALog.d("TAG", "getDeviceToken onSuccess token = " + token);

                        enrolleeUserBind(productKey, deviceName, token);
                    }

                    @Override
                    public void onFail(String s) {
                        ALog.e("TAG", "getDeviceToken onFail s = " + s);
                        Toast.makeText(getApplicationContext(), "getTokenFailed", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void gprsUserBind(String pk, String dn){
        Map<String, Object> maps = new HashMap<>();
        maps.put("productKey", pk);
        maps.put("deviceName", dn);
        IoTRequestBuilder builder = new IoTRequestBuilder()
                .setPath("/awss/gprs/user/bind")
                .setApiVersion("1.0.2")
                .setAuthType("iotAuth")
                .setParams(maps);

        IoTRequest request = builder.build();

        IoTAPIClient ioTAPIClient = new IoTAPIClientFactory().getClient();
        ioTAPIClient.send(request, new IoTCallback() {
            @Override
            public void onFailure(IoTRequest ioTRequest, Exception e) {
                ALog.d("TAG", "onFailure");
            }

            @Override
            public void onResponse(IoTRequest ioTRequest, IoTResponse response) {
                ALog.d("TAG", "onResponse gprsUserBind ok, rout to ilopmain page");
                Router.getInstance().toUrl(BindAndUseActivity.this, "page/ilopmain");
            }
        });
    }

    private void enrolleeUserBind(String pk, String dn, String token){
        Map<String, Object> maps = new HashMap<>();
        maps.put("productKey", pk);
        maps.put("deviceName", dn);
        maps.put("token", token);
//        maps.put("groupIds","\"[\"123\"]");
        IoTRequestBuilder builder = new IoTRequestBuilder()
                .setPath("/awss/enrollee/user/bind")
                .setApiVersion("1.0.2")
                .setAuthType("iotAuth")
                .setParams(maps);

        IoTRequest request = builder.build();

        IoTAPIClient ioTAPIClient = new IoTAPIClientFactory().getClient();
        ioTAPIClient.send(request, new IoTCallback() {
            @Override
            public void onFailure(IoTRequest ioTRequest, Exception e) {
                ALog.d("TAG", "onFailure");
            }

            @Override
            public void onResponse(IoTRequest ioTRequest, IoTResponse response) {
                ALog.d("TAG", "onResponse enrolleeUserBind ok, rout to ilopmain page");
                final int code = response.getCode();
                final String msg = response.getMessage();
                if (code != 200){
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "code = " +code + " msg =" + msg, Toast.LENGTH_SHORT).show();
                        }
                    });
                    return;
                }
                Router.getInstance().toUrl(BindAndUseActivity.this, "page/ilopmain");
            }
        });
    }

}
