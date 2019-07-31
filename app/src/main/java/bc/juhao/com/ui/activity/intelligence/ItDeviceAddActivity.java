package bc.juhao.com.ui.activity.intelligence;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.alibaba.sdk.android.oauth.OauthServiceImpl;
import com.aliyun.alink.business.devicecenter.api.add.DeviceInfo;
import com.aliyun.alink.business.devicecenter.api.discovery.IDiscoveryListener;
import com.aliyun.alink.business.devicecenter.api.discovery.LocalDeviceMgr;
import com.aliyun.iot.aep.sdk.apiclient.IoTAPIClient;
import com.aliyun.iot.aep.sdk.apiclient.IoTAPIClientFactory;
import com.aliyun.iot.aep.sdk.apiclient.IoTAPIClientImpl;
import com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback;
import com.aliyun.iot.aep.sdk.apiclient.callback.IoTResponse;
import com.aliyun.iot.aep.sdk.apiclient.hook.IoTAuthProvider;
import com.aliyun.iot.aep.sdk.apiclient.request.IoTRequest;
import com.aliyun.iot.aep.sdk.apiclient.request.IoTRequestBuilder;
import com.aliyun.iot.aep.sdk.base.delegate.adapter.OALoginAdapter;
import com.aliyun.iot.aep.sdk.credential.IoTCredentialProviderImpl;
import com.aliyun.iot.aep.sdk.credential.IotCredentialManager.IoTCredentialManageImpl;
import com.aliyun.iot.aep.sdk.login.LoginBusiness;
import com.aliyun.iot.ilop.demo.DemoApplication;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bc.juhao.com.R;
import bc.juhao.com.adapter.BaseAdapterHelper;
import bc.juhao.com.adapter.QuickAdapter;
import bc.juhao.com.bean.DevicesBean;
import bc.juhao.com.common.BaseActivity;
import bc.juhao.com.cons.Constance;
import bc.juhao.com.ui.activity.IssueApplication;
import bc.juhao.com.ui.view.EndOfListView;

/**
 * Created by gamekonglee on 2018/7/10.
 */

public class ItDeviceAddActivity extends BaseActivity {

    private QuickAdapter adapter;
    private EndOfListView lv_devices;
    private List<DevicesBean> devicesBeans;

    @Override
    protected void InitDataView() {

    }

    @Override
    protected void initController() {

    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_it_devices_add);
        lv_devices = findViewById(R.id.lv_devices);


        adapter = new QuickAdapter<DevicesBean>(this, R.layout.item_lv_devices) {
            @Override
            protected void convert(BaseAdapterHelper helper, DevicesBean item) {
            helper.setText(R.id.tv_name,item.getName());
            }
        };
        lv_devices.setAdapter(adapter);
        devicesBeans = new ArrayList<>();
        devicesBeans.add(new DevicesBean("开关","","1"));
        devicesBeans.add(new DevicesBean("照明","","2"));
        adapter.replaceAll(devicesBeans);
        OauthServiceImpl oauthService=new OauthServiceImpl();

        OALoginAdapter loginAdapter = new OALoginAdapter(DemoApplication.getInstance());
        loginAdapter.setDefaultOAHost(null);
        LoginBusiness.init(DemoApplication.getInstance(), loginAdapter, true, "RELEASE");
        String appkey= Constance.app_key;
        IoTCredentialManageImpl.init(loginAdapter, appkey);
        IoTAuthProvider provider = new IoTCredentialProviderImpl(IoTCredentialManageImpl.getInstance(DemoApplication.getInstance()));
        IoTAPIClientImpl.getInstance().registerIoTAuthProvider("iotAuth", provider);
//
//        IoTRequest request = new IoTRequestBuilder()
//                .setPath("path")
//                .setApiVersion("1.0.4")
//                .addParam("request", paramMap)
//                .setAuthType("iotAuth")
//                .build();
//

//        Map<String, Object> maps = new HashMap<>();
//        IoTRequestBuilder builder = new IoTRequestBuilder()
//                .setPath("/thing/productInfo/getByAppKey")
//                .setApiVersion("1.1.1")
//                .setAuthType("24938534")
//                .setParams(maps);
//
//        IoTRequest request = builder.build();
//
//        IoTAPIClient ioTAPIClient = new IoTAPIClientFactory().getClient();
//        ioTAPIClient.send(request, new IoTCallback() {
//            @Override
//            public void onFailure(IoTRequest ioTRequest, Exception e) {
//                ALog.d("TAG", "onFailure");
//            }
//
//            @Override
//            public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
//                final int code = ioTResponse.getCode();
//                final String msg = ioTResponse.getMessage();
//                if (code != 200){
//
//                }
//
//                Object data = ioTResponse.getData();
//                ALog.d("TAG", "onResponse："+msg+"------"+data);
//            }
//        });
        Map<String, Object> maps = new HashMap<>();
        IoTRequestBuilder builder = new IoTRequestBuilder()
                .setPath("/thing/productInfo/getByAppKey")
                .setApiVersion("1.1.1")
                .setAuthType("24938534")
                .setParams(maps);

        IoTRequest request = builder.build();

        IoTAPIClient ioTAPIClient = new IoTAPIClientFactory().getClient();
        ioTAPIClient.send(request, new IoTCallback() {
            @Override
            public void onFailure(IoTRequest ioTRequest, Exception e) {
                Log.e("TAG", "onFailure");
            }

            @Override
            public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
                final int code = ioTResponse.getCode();
                final String msg = ioTResponse.getMessage();
                Log.e("TAG", "onResponse"+msg);
                if (code != 200){
                    return;
                }

                Object data = ioTResponse.getData();
                if (null != data) {
                    if(data instanceof JSONArray){

                    }
                }
            }
        });

        lv_devices.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                TmpSdk.init(getBaseContext(), new TmpInitConfig(TmpInitConfig.ONLINE));
//                TmpSdk.getDeviceManager().discoverDevices(null,5000,null);
//                PanelDevice panelDevice = new PanelDevice(iotid);

                // 开始发现设备
                LocalDeviceMgr.getInstance().startDiscovery(ItDeviceAddActivity.this, new IDiscoveryListener() {
                    @Override
                    public void onLocalDeviceFound(DeviceInfo deviceInfo) {
// 停止设备发现
                        Log.e("deviceinfo",deviceInfo.toString());
                        LocalDeviceMgr.getInstance().stopDiscovery();
                    }

                    @Override
                    public void onEnrolleeDeviceFound(List<DeviceInfo> list) {
// 停止设备发现
                        Log.e("onEnrolleeDeviceFound",list.toString());
                        LocalDeviceMgr.getInstance().stopDiscovery();
                    }
                });
            }});
    }

    @Override
    protected void initData() {

    }
}
