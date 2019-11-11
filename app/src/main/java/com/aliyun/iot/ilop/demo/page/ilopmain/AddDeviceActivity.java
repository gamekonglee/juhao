package com.aliyun.iot.ilop.demo.page.ilopmain;

/*import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aliyun.alink.business.devicecenter.api.add.DeviceInfo;
import com.aliyun.alink.business.devicecenter.api.discovery.IDiscoveryListener;
import com.aliyun.alink.business.devicecenter.api.discovery.ILocalDeviceMgr;
import com.aliyun.alink.business.devicecenter.api.discovery.LocalDeviceMgr;
import com.aliyun.iot.aep.component.router.Router;
import com.aliyun.iot.aep.sdk.apiclient.IoTAPIClient;
import com.aliyun.iot.aep.sdk.apiclient.IoTAPIClientFactory;
import com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback;
import com.aliyun.iot.aep.sdk.apiclient.callback.IoTResponse;
import com.aliyun.iot.aep.sdk.apiclient.request.IoTRequest;
import com.aliyun.iot.aep.sdk.apiclient.request.IoTRequestBuilder;
import com.aliyun.iot.aep.sdk.framework.AActivity;
import com.aliyun.iot.aep.sdk.log.ALog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bc.juhao.com.R;
import bocang.utils.UIUtils;

public class AddDeviceActivity extends AActivity {
    private String TAG = AddDeviceActivity.class.getSimpleName();
    private Button mBackBtn;
    private LinearLayout mSupportDeviceLL;
    private LinearLayout mFoundDeviceLL;
    private FrameLayout mFailMsgPanel;
    private Handler mHandler = new Handler();
    private FrameLayout mLocalDevicePanelFl;
    ArrayList<String> mDeviceList;
//    private static String CODE = "link://plugin/a123kfz2KdRdrfYc";
    private static String CODE =    "link://router/connectConfig";

    private ArrayList<SupportDeviceListItem> mSupportDeviceListItems;
    private ArrayList<FoundDeviceListItem> mFoundDeviceListItems;
    private ArrayList<FoundDeviceListItem> mFoundDeviceNeedEnrolleeListItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UIUtils.initActivityScreen(this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            mDeviceList = bundle.getStringArrayList("deviceList");
        }

        setContentView(R.layout.add_device_activity);
        mBackBtn = (Button) findViewById(R.id.ilop_main_back_btn);
        mBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mLocalDevicePanelFl = (FrameLayout)findViewById(R.id.local_device_panel_fl);
        mFailMsgPanel = (FrameLayout)findViewById(R.id.fail_msg_panel);
        mFoundDeviceLL = (LinearLayout)findViewById(R.id.found_device_ll);
        mFoundDeviceListItems = new ArrayList<>();
        mFoundDeviceNeedEnrolleeListItems = new ArrayList<>();
        mSupportDeviceLL = (LinearLayout)findViewById(R.id.support_device_ll);

        mSupportDeviceListItems = new ArrayList<>();
        getSupportDeviceListFromSever();
    }

    private void fillSupportDeviceLL() {
        View view = null;
        LayoutInflater inflater = getLayoutInflater();
        if (mSupportDeviceListItems.size() == 0){
            // 没有支持的设备
            mFailMsgPanel.setVisibility(View.VISIBLE);
        }else {
            mFailMsgPanel.setVisibility(View.GONE);
        }
        for (int i = 0; i < mSupportDeviceListItems.size(); i++){
            view = inflater.inflate(R.layout.device_listview_item, null);
            ImageView iv_device_icon = (ImageView)view.findViewById(R.id.list_item_device_icon);
            TextView tv_device_name = (TextView)view.findViewById(R.id.list_item_device_name);
//            Button btn_device_connect = (Button)view.findViewById(R.id.list_item_device_action);
//            iv_device_icon.setImageResource(R.drawable.add_device);
            final String name=mSupportDeviceListItems.get(i).deviceName;
            if(name.contains("开关")){
                iv_device_icon.setImageResource(R.mipmap.home_kg);
            }else if(name.contains("插座")){
                iv_device_icon.setImageResource(R.mipmap.home_cz);
            }else {
                iv_device_icon.setImageResource(R.mipmap.home_zm_g);
            }
            tv_device_name.setText(mSupportDeviceListItems.get(i).deviceName);
            final String productKey = mSupportDeviceListItems.get(i).productKey;
//            String name=mSupportDeviceListItems.get(i).deviceName;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String code = CODE;
                    Bundle bundle = new Bundle();
                    bundle.putString("productKey", productKey);
                    bundle.putString("deviceName",name);
                    Router.getInstance().toUrlForResult(AddDeviceActivity.this, code, 1, bundle);
                }
            });

            Log.d("TAG", "fillSupportDeviceLL");
            mSupportDeviceLL.addView(view);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && null != data) {
            Log.d("TAG", "onActivityResult");
            if (data.getStringExtra("productKey") != null){
                final String productKey = data.getStringExtra("productKey");
                final String deviceName = data.getStringExtra("deviceName");

                Intent intent = new Intent(getApplicationContext(), BindAndUseActivity.class);
                final Bundle bundle = new Bundle();
                bundle.putString("productKey", productKey);
                bundle.putString("deviceName", deviceName);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        }
    }

    private void fillFoundDeviceLL() {
        mLocalDevicePanelFl.setVisibility(View.VISIBLE);
        mFoundDeviceLL.removeAllViews();
        View view = null;
        LayoutInflater inflater = getLayoutInflater();

        // 需要绑定的设备列表
        for (int i = 0; i < mFoundDeviceListItems.size(); i++){
            view = inflater.inflate(R.layout.device_listview_item, null);
            ImageView iv_device_icon = (ImageView)view.findViewById(R.id.list_item_device_icon);
            TextView tv_device_name = (TextView)view.findViewById(R.id.list_item_device_name);
            Button btn_device_connect = (Button)view.findViewById(R.id.list_item_device_action);
            btn_device_connect.setText("绑定");
            iv_device_icon.setImageResource(R.drawable.add_device);
            tv_device_name.setText(mFoundDeviceListItems.get(i).deviceName);
            final DeviceInfo deviceInfo = mFoundDeviceListItems.get(i).deviceInfo;
            final int index = i;
            btn_device_connect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

//                      {
//                     "awssVer": "xxx", //有就传
//                     "productKey": "xxx",
//                     "deviceName": "xxx",
//                     "regProductKey": "", // 一般待配网设备有 有就透传
//                     "regDeviceName": "", // 一般待配网设备有 有就透传
//                     "mac": "xxx", // 可能没有  设备端返回就会传   有就透传
//                     "token": "xxx", // 可能没有  设备端返回就会传  不需要传到setDevice
//                     "provisionStatus": 0, // 0待配 1已配   有就透传
//                     "devType": 0, // 0: wifi device, 1: ethernet device, ... 设备端返回就会传 有就透传
//                     "bssid": "xxx", // 设备端返回就会传 有就透传
//                     "addDeviceFrom": "ROUTER"， //  有就透传 可选值为ROUTER 或“”
//                     "linkType": "ForceAliLinkTypeNone"
//                     }


                    String code = CODE;
                    Bundle bundle = new Bundle();
                    bundle.putString("awssVer", deviceInfo.awssVer.toString());
                    bundle.putString("productKey", deviceInfo.productKey);
                    bundle.putString("deviceName", deviceInfo.deviceName);
                    bundle.putString("regProductKey", deviceInfo.regProductKey);
                    bundle.putString("regDeviceName", deviceInfo.regDeviceName);
                    bundle.putString("token", deviceInfo.token);
                    bundle.putString("devType", deviceInfo.devType);
                    bundle.putString("addDeviceFrom", deviceInfo.addDeviceFrom);
                    bundle.putString("linkType", deviceInfo.linkType);
                    Router.getInstance().toUrlForResult(AddDeviceActivity.this, code, 1, bundle);
                }
            });

            mFoundDeviceLL.addView(view);
        }

        // 需要配网的设备列表
        for (int i = 0; i < mFoundDeviceNeedEnrolleeListItems.size(); i++){
            view = inflater.inflate(R.layout.device_listview_item, null);
            ImageView iv_device_icon = (ImageView)view.findViewById(R.id.list_item_device_icon);
            TextView tv_device_name = (TextView)view.findViewById(R.id.list_item_device_name);
            Button btn_device_connect = (Button)view.findViewById(R.id.list_item_device_action);
            btn_device_connect.setText("连接");
            iv_device_icon.setImageResource(R.drawable.add_device);
            tv_device_name.setText(mFoundDeviceNeedEnrolleeListItems.get(i).deviceName);
            final DeviceInfo deviceInfo = mFoundDeviceNeedEnrolleeListItems.get(i).deviceInfo;
            final int index = i;
            btn_device_connect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

//                      {
//                     "awssVer": "xxx", //有就传
//                     "productKey": "xxx",
//                     "deviceName": "xxx",
//                     "regProductKey": "", // 一般待配网设备有 有就透传
//                     "regDeviceName": "", // 一般待配网设备有 有就透传
//                     "mac": "xxx", // 可能没有  设备端返回就会传   有就透传
//                     "token": "xxx", // 可能没有  设备端返回就会传  不需要传到setDevice
//                     "provisionStatus": 0, // 0待配 1已配   有就透传
//                     "devType": 0, // 0: wifi device, 1: ethernet device, ... 设备端返回就会传 有就透传
//                     "bssid": "xxx", // 设备端返回就会传 有就透传
//                     "addDeviceFrom": "ROUTER"， //  有就透传 可选值为ROUTER 或“”
//                     "linkType": "ForceAliLinkTypeNone"
//                     }
//

                    String code = CODE;
                    Bundle bundle = new Bundle();
                    if (deviceInfo.awssVer != null){
                        bundle.putString("awssVer", deviceInfo.awssVer.toString());
                    }
                    bundle.putString("productKey", deviceInfo.productKey);
                    bundle.putString("deviceName", deviceInfo.deviceName);
                    bundle.putString("regProductKey", deviceInfo.regProductKey);
                    bundle.putString("regDeviceName", deviceInfo.regDeviceName);
                    bundle.putString("token", deviceInfo.token);
                    bundle.putString("devType", deviceInfo.devType);
                    bundle.putString("addDeviceFrom", deviceInfo.addDeviceFrom);
                    bundle.putString("linkType", deviceInfo.linkType);

                    Router.getInstance().toUrlForResult(AddDeviceActivity.this, code, 1, bundle);
                }
            });

            mFoundDeviceLL.addView(view);
        }
    }

    private void getSupportDeviceListFromSever() {
        Map<String, Object> maps = new HashMap<>();
        IoTRequestBuilder builder = new IoTRequestBuilder()
                .setPath("/thing/productInfo/getByAppKey")
                .setApiVersion("1.1.1")
                .setAuthType("iotAuth")
                .setParams(maps);

        IoTRequest request = builder.build();

        IoTAPIClient ioTAPIClient = new IoTAPIClientFactory().getClient();
        ioTAPIClient.send(request, new IoTCallback() {
            @Override
            public void onFailure(IoTRequest ioTRequest, Exception e) {
                ALog.d("TAG", "onFailure");
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        fillSupportDeviceLL();
                    }
                });
            }

            @Override
            public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
                ALog.d("TAG", "onResponse");
                final int code = ioTResponse.getCode();
                final String msg = ioTResponse.getMessage();
                if (code != 200){
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "code = " +code + " msg =" + msg, Toast.LENGTH_SHORT).show();
                        }
                    });
                    return;
                }

                Object data = ioTResponse.getData();
                if (null != data) {
                    if(data instanceof JSONArray){
                        mSupportDeviceListItems = parseSupportDeviceListFromSever((JSONArray) data);

                    }
                }
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        fillSupportDeviceLL();
                    }
                });
            }
        });



    }

    private ArrayList<JSONObject> mSupportDeviceList;
    private ArrayList<SupportDeviceListItem> parseSupportDeviceListFromSever(JSONArray jsonArray) {
        ArrayList<SupportDeviceListItem> arrayList = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                SupportDeviceListItem device = new SupportDeviceListItem();
                device.deviceName = jsonObject.getString("name");
                device.productKey = jsonObject.getString("productKey");
                arrayList.add(device);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return arrayList;
    }

    private boolean isDeviceBound(String deviceId){
        if (mDeviceList == null){
            return false;
        }
        if (mDeviceList.contains(deviceId)){
            return true;
        }
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 获取之前发现的所有设备
        List<DeviceInfo> list = LocalDeviceMgr.getInstance().getLanDevices();

        mFoundDeviceListItems = new ArrayList<>();
        ILocalDeviceMgr localDeviceMgr=LocalDeviceMgr.getInstance();
        LocalDeviceMgr.getInstance().startDiscovery(this, new IDiscoveryListener() {
            @Override
            public void onLocalDeviceFound(DeviceInfo deviceInfo) {
                //要绑定 需要在一分钟之内，否则绑定失败，需要重新发现再绑定
                Log.e("onLocalDevice", deviceInfo.toString());
                FoundDeviceListItem deviceListItem = new FoundDeviceListItem();
                deviceListItem.deviceName = deviceInfo.deviceName;
                deviceListItem.productKey = deviceInfo.productKey;
                deviceListItem.deviceInfo = deviceInfo;
                deviceListItem.deviceStatus = FoundDeviceListItem.NEED_BIND;
                if (false == isDeviceBound(deviceInfo.productKey + deviceInfo.deviceName)){
                    mFoundDeviceListItems.add(deviceListItem);
                }
                fillFoundDeviceLL();
            }

            @Override
            public void onEnrolleeDeviceFound(List<DeviceInfo> list) {
                Log.e("onEnrolleeDeviceFound", list.toString());
                //要配网
                for (DeviceInfo deviceInfo : list) {
                    FoundDeviceListItem deviceListItem = new FoundDeviceListItem();
                    deviceListItem.deviceStatus = FoundDeviceListItem.NEED_CONNECT;
                    deviceListItem.deviceInfo = deviceInfo;
                    deviceListItem.deviceName = deviceInfo.deviceName;
                    deviceListItem.productKey = deviceInfo.productKey;
                    if (false == isDeviceBound(deviceInfo.productKey + deviceInfo.deviceName)){
                        mFoundDeviceNeedEnrolleeListItems.add(deviceListItem);
                    }
                }
                fillFoundDeviceLL();
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        mLocalDevicePanelFl.setVisibility(View.GONE);
        LocalDeviceMgr.getInstance().stopDiscovery();
    }
}*/


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.aliyun.alink.business.devicecenter.api.add.AddDeviceBiz;
import com.aliyun.alink.business.devicecenter.api.add.DeviceInfo;
import com.aliyun.alink.business.devicecenter.api.add.IAddDeviceListener;
import com.aliyun.alink.business.devicecenter.api.add.ProvisionStatus;
import com.aliyun.alink.business.devicecenter.api.discovery.IDiscoveryListener;
import com.aliyun.alink.business.devicecenter.api.discovery.ILocalDeviceMgr;
import com.aliyun.alink.business.devicecenter.api.discovery.LocalDeviceMgr;
import com.aliyun.alink.business.devicecenter.base.DCErrorCode;
import com.aliyun.alink.business.devicecenter.extbone.BoneAddDeviceBiz;
import com.aliyun.alink.business.devicecenter.extbone.BoneHotspotHelper;
import com.aliyun.alink.business.devicecenter.extbone.BoneLocalDeviceMgr;
import com.aliyun.alink.sdk.jsbridge.BonePluginRegistry;
import com.aliyun.iot.aep.component.router.Router;
import com.aliyun.iot.aep.sdk.apiclient.IoTAPIClient;
import com.aliyun.iot.aep.sdk.apiclient.IoTAPIClientFactory;
import com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback;
import com.aliyun.iot.aep.sdk.apiclient.callback.IoTResponse;
import com.aliyun.iot.aep.sdk.apiclient.request.IoTRequest;
import com.aliyun.iot.aep.sdk.apiclient.request.IoTRequestBuilder;
import com.aliyun.iot.aep.sdk.framework.AActivity;
import com.aliyun.iot.aep.sdk.log.ALog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zhy.http.okhttp.callback.Callback;

import bc.juhao.com.R;
import bc.juhao.com.adapter.BaseAdapterHelper;
import bc.juhao.com.adapter.QuickAdapter;
import bc.juhao.com.bean.DeviceCategoryBean;
import bc.juhao.com.bean.DevicesDataBean;
import bc.juhao.com.cons.Constance;
import bc.juhao.com.net.ApiClient;
import bocang.utils.LogUtils;
import bocang.utils.MyToast;
import bocang.utils.UIUtils;
import okhttp3.Call;
import okhttp3.Response;


public class AddDeviceActivity extends AActivity {
    private String TAG = AddDeviceActivity.class.getSimpleName();
    private View mBackBtn;
    private LinearLayout mSupportDeviceLL;
    private LinearLayout mFoundDeviceLL;
    private FrameLayout mFailMsgPanel;
    private Handler mHandler = new Handler();
    private FrameLayout mLocalDevicePanelFl;
    ArrayList<String> mDeviceList;
    //    private static String CODE = "link://plugin/a123kfz2KdRdrfYc";
    private static String CODE = "link://router/connectConfig";


    private List<SupportDeviceListItem> mSupportDeviceListItems;
    private ArrayList<FoundDeviceListItem> mFoundDeviceListItems;
    private ArrayList<FoundDeviceListItem> mFoundDeviceNeedEnrolleeListItems;
    private QuickAdapter<DeviceCategoryBean> adapterCategoryA;
    private QuickAdapter<DeviceCategoryBean> adapterCategoryB;
    private List<DeviceCategoryBean> deviceCategoryBeansA;
    private ListView lv_cate_a;
    private List<DeviceCategoryBean> deviceCategoryBeansB;
    private GridView gv_cate_b;
    private int current=-1;
    private List<DevicesDataBean> devicesDataBeans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UIUtils.initActivityScreen(this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            mDeviceList = bundle.getStringArrayList("deviceList");
        }

        setContentView(R.layout.add_device_activity_new);
        mBackBtn =  findViewById(R.id.ilop_main_back_btn);
        mBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mLocalDevicePanelFl = (FrameLayout)findViewById(R.id.local_device_panel_fl);
        mFailMsgPanel = (FrameLayout)findViewById(R.id.fail_msg_panel);
        mFoundDeviceLL = (LinearLayout)findViewById(R.id.found_device_ll);
        mFoundDeviceListItems = new ArrayList<>();
        mFoundDeviceNeedEnrolleeListItems = new ArrayList<>();
        mSupportDeviceLL = (LinearLayout)findViewById(R.id.support_device_ll);
        lv_cate_a = findViewById(R.id.lv_cate_a);
        gv_cate_b = findViewById(R.id.lv_cate_b);
//        current = 0;
        adapterCategoryA = new QuickAdapter<DeviceCategoryBean>(AddDeviceActivity.this,R.layout.item_device_cate_1) {
            @Override
            protected void convert(BaseAdapterHelper helper, DeviceCategoryBean item) {
                if(current==helper.getPosition()){
                    helper.setBackgroundColor(R.id.ll_bg,getResources().getColor(R.color.white));
                }else {
                    helper.setBackgroundColor(R.id.ll_bg,getResources().getColor(R.color.line));
                }
                helper.setText(R.id.tv_name,item.getName());
                ImageView imageView=helper.getView(R.id.iv_img);
                ImageLoader.getInstance().displayImage(item.getPic(),imageView);

            }
        };
        lv_cate_a.setAdapter(adapterCategoryA);

        adapterCategoryB = new QuickAdapter<DeviceCategoryBean>(AddDeviceActivity.this,R.layout.item_device_cate_2) {
            @Override
            protected void convert(BaseAdapterHelper helper, DeviceCategoryBean item) {
                helper.setText(R.id.tv_name,item.getName());
                ImageView imageView=helper.getView(R.id.iv_img);
                ImageLoader.getInstance().displayImage(item.getPic(),imageView);
            }
        };
        gv_cate_b.setAdapter(adapterCategoryB);

        mSupportDeviceListItems = new ArrayList<>();
//        getSupportDeviceListFromSever();

        lv_cate_a.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                current=i;
                adapterCategoryA.notifyDataSetChanged();
                getDevicesListB(deviceCategoryBeansA.get(i).getId());
            }
        });
        gv_cate_b.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(devicesDataBeans!=null&&devicesDataBeans.size()>0){
                    boolean hasDevice=false;
                    String productKey="";
                    String name="";
                    for(int x=0;x<devicesDataBeans.size();x++){
//                    if(devicesDataBeans.get(x).getCategoryId()==deviceCategoryBeansB.get(i).getCate_id()){
//                    LogUtils.logE("getCate_id,",""+devicesDataBeans.get(x).getName());
//                    }
//                        LogUtils.logE("deviceCate",deviceCategoryBeansB.get(i).getName());
                        if(deviceCategoryBeansB.get(i).getName().equals("照明")){
                            productKey="a12htigUyHy";
                            name="平板灯";
                        }else if(deviceCategoryBeansB.get(i).getName().equals("插座")){
                            productKey="a1GQAwDVapn";
                            name="插座新";
                        }else if(deviceCategoryBeansB.get(i).getName().equals("开关")){
                            productKey="a1DQTWYrTEF";
                            name="智能开关";
                        }else if(deviceCategoryBeansB.get(i).getName().equals("门锁")){
                            productKey="a18Sffk8IIy";
                            name="智能门锁";
                        }else {
                            break;
                        }
                        hasDevice=true;
                        String code = CODE;
                        Bundle bundle = new Bundle();
//                            LogUtils.logE("getName,",""+devicesDataBeans.get(x).getName());
//                            LogUtils.logE("getProductKey,",""+devicesDataBeans.get(x).getProductKey());
                        bundle.putString("productKey", productKey);
                        bundle.putString("deviceName",name);
                        Router.getInstance().toUrlForResult(AddDeviceActivity.this, code, 1, bundle);
                        break;
                    }
                    if(!hasDevice){
                        MyToast.show(AddDeviceActivity.this,"没有相关的可用设备");
                    }
                }
            }
        });
        BonePluginRegistry.register("BoneAddDeviceBiz", BoneAddDeviceBiz.class);
        BonePluginRegistry.register("BoneLocalDeviceMgr", BoneLocalDeviceMgr.class);
        BonePluginRegistry.register("BoneHotspotHelper", BoneHotspotHelper.class);
        getDevicesList();
        getDevicesDatas();
    }

    private void getDevicesDatas() {
        ApiClient.getDevicesDatas(new Callback<String>() {
            @Override
            public String parseNetworkResponse(Response response, int id) throws Exception {
                return null;
            }

            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public String onResponse(String response, int id) {
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    if(jsonObject!=null){
                        JSONArray datas=jsonObject.getJSONArray(Constance.data);
                        if(datas!=null&&datas.length()>0){
                            devicesDataBeans = new Gson().fromJson(datas.toString(),new TypeToken<List<DevicesDataBean>>(){}.getType());
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if(devicesDataBeans !=null&& devicesDataBeans.size()>0){
//                                    UIUtils.initListViewHeight(lv_cate_a);
                                    }
                                }
                            });
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return null;
            }
        });
    }

    private void getDevicesListB(int id) {
        ApiClient.getDevicesList(id, new Callback<String>() {
            @Override
            public String parseNetworkResponse(Response response, int id) throws Exception {
                return null;
            }

            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public String onResponse(String response, int id) {
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    if(jsonObject!=null){
                        JSONArray datas=jsonObject.getJSONArray(Constance.data);
                        if(datas!=null&&datas.length()>0){
                            deviceCategoryBeansB = new Gson().fromJson(datas.toString(),new TypeToken<List<DeviceCategoryBean>>(){}.getType());
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if(deviceCategoryBeansB!=null&&deviceCategoryBeansB.size()>0){
                                        adapterCategoryB.replaceAll(deviceCategoryBeansB);
//                                    UIUtils.initListViewHeight(lv_cate_a);
                                    }
                                }
                            });
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return null;
            }
        });
    }

    int pid=0;
    private void getDevicesList() {

        ApiClient.getDevicesList(pid, new Callback<String>() {
            @Override
            public String parseNetworkResponse(Response response, int id) throws Exception {
                return null;
            }

            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public String onResponse(String response, int id) {
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    if(jsonObject!=null){
                        JSONArray datas=jsonObject.getJSONArray(Constance.data);
                        if(datas!=null&&datas.length()>0){
                            deviceCategoryBeansA = new Gson().fromJson(datas.toString(),new TypeToken<List<DeviceCategoryBean>>(){}.getType());
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if(deviceCategoryBeansA!=null&&deviceCategoryBeansA.size()>0){
                                        adapterCategoryA.replaceAll(deviceCategoryBeansA);
                                        if(current==-1){
                                            current=0;
                                            lv_cate_a.performItemClick(null,current,0);
                                        }

//                                    UIUtils.initListViewHeight(lv_cate_a);
                                    }
                                }
                            });
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return null;
            }
        });
    }

    //权限请求码
    private static final int PERMISSION_REQUEST_CODE = 0;
    //两个危险权限需要动态申请
    private static final String[] NEEDED_PERMISSIONS = new String[]{
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
    };


    private boolean mHasPermission;

    private void fillSupportDeviceLL() {
        View view = null;
        LayoutInflater inflater = getLayoutInflater();
        if (mSupportDeviceListItems.size() == 0){
            // 没有支持的设备
            mFailMsgPanel.setVisibility(View.VISIBLE);
        }else {
            mFailMsgPanel.setVisibility(View.GONE);
        }
        for (int i = 0; i < mSupportDeviceListItems.size(); i++){
            view = inflater.inflate(R.layout.device_listview_item, null);
            ImageView iv_device_icon = (ImageView)view.findViewById(R.id.list_item_device_icon);
            TextView tv_device_name = (TextView)view.findViewById(R.id.list_item_device_name);
//            Button btn_device_connect = (Button)view.findViewById(R.id.list_item_device_action);
//            iv_device_icon.setImageResource(R.drawable.add_device);
            final String name=mSupportDeviceListItems.get(i).getName();
            ImageLoader.getInstance().displayImage(mSupportDeviceListItems.get(i).getImage(),iv_device_icon);
//            if(name.contains("开关")){
//                iv_device_icon.setImageResource(R.mipmap.home_kg);
//            }else if(name.contains("插座")){
//                iv_device_icon.setImageResource(R.mipmap.home_cz);
//            }else {
//                iv_device_icon.setImageResource(R.mipmap.home_zm_g);
//            }
            tv_device_name.setText(mSupportDeviceListItems.get(i).getName());
            final String productKey = mSupportDeviceListItems.get(i).productKey;
            final int finalI = i;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    mHasPermission = checkPermission();
//                    if (!mHasPermission) {
//                        requestPermission();
//                    }
//                    DeviceInfo deviceInfo = new DeviceInfo();
//                    deviceInfo.productKey = productKey; // 商家后台注册的 productKey，不可为空
//                    deviceInfo.deviceName = mSupportDeviceListItems.get(finalI).deviceName;// 设备名, 可为空
////                    deviceInfo.productId = mSupportDeviceListItems.get(finalI);// 产品 ID， 蓝牙辅助配网必须
//                    deviceInfo.id= "xxx";// 设备热点的id，在发现热点设备返回到APP的时候会带这个字段，设备热点必须
//// 设备热点配网 ForceAliLinkTypeSoftAP  蓝牙辅助配网 ForceAliLinkTypeBLE
//// 二维码配网 ForceAliLinkTypeQR   手机热点配网 ForceAliLinkTypePhoneAP
//                    deviceInfo.linkType = "ForceAliLinkTypeNone"; // 默认一键配网
//
////设置待添加设备的基本信息
//                    AddDeviceBiz.getInstance().setDevice( deviceInfo);
//// 开始添加设备
//                    AddDeviceBiz.getInstance().startAddDevice(AddDeviceActivity.this, new IAddDeviceListener(){
//                        @Override
//                        public void onPreCheck(boolean b, DCErrorCode dcErrorCode) {
//                            LogUtils.logE("start","onPreCheck");
//                            // 参数检测回调
//                        }
//
//                        @Override
//                        public void onProvisionPrepare(int prepareType) {
//                            LogUtils.logE("onProvisionPrepare","type"+prepareType);
//                            String ssid = getSSID();// 热点配网的时候注意 要先获取ssid，然后再开启热点，否则无法正确获取到SSID
//                            String password = "82780311bocang";
//                            int timeout = 60;//单位秒 目前最短只能设置60S
//                            AddDeviceBiz.getInstance().toggleProvision(ssid, password, timeout);
//
//                            // 手机热点配网、设备热点配网、一键配网、蓝牙辅助配网、二维码配网会走到该流程，
//                            // 零配和智能路由器配网不会走到该流程。
//                            // prepareType = 1提示用户输入账号密码
//                            // prepareType = 2提示用户手动开启指定热点 aha 12345678
//                            // 执行完上述操作之后，调用toggleProvision接口继续执行配网流程
//                        }
//
//                        @Override
//                        public void onProvisioning() {
//                            LogUtils.logE("onProvisioning","on");
//                            // 配网中
//                        }
//                        @Override
//                        public void onProvisionStatus(ProvisionStatus provisionStatus) {
//                            LogUtils.logE("onProvisionStatus",""+provisionStatus);
//                            // 二维码配网会走到这里  provisionStatus=ProvisionStatus.QR_PROVISION_READY表示二维码ready了
//                            // ProvisionStatus.QR_PROVISION_READY.message() 获取二维码内容
//                            // 注意：返回二维码时已开启监听设备是否已配网成功的通告，并开始计时，UI端应提示用户尽快扫码；
//                            // 如果在指定时间配网超时了，重新调用开始配网流程并刷新二维码；
//                        }
//
//                        @Override
//                        public void onProvisionedResult(boolean b, DeviceInfo deviceInfo, DCErrorCode errorCode) {
//                            LogUtils.logE("onProvisionedResult",""+b+errorCode);
//                            // 配网结果 如果配网成功之后包含token，请使用配网成功带的token做绑定
//                        }
//                    });


                    String code = CODE;
                    Bundle bundle = new Bundle();
                    bundle.putString("productKey", productKey);
                    bundle.putString("deviceName",name);
                    Router.getInstance().toUrlForResult(AddDeviceActivity.this, code, 1, bundle);
                }
            });

            Log.d("TAG", "fillSupportDeviceLL");
            mSupportDeviceLL.addView(view);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && null != data) {
            Log.d("TAG", "onActivityResult");
            if (data.getStringExtra("productKey") != null){
                final String productKey = data.getStringExtra("productKey");
                final String deviceName = data.getStringExtra("deviceName");

                Intent intent = new Intent(getApplicationContext(), BindAndUseActivity.class);
                final Bundle bundle = new Bundle();
                bundle.putString("productKey", productKey);
                bundle.putString("deviceName", deviceName);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        }
    }

    private void fillFoundDeviceLL() {
        mLocalDevicePanelFl.setVisibility(View.VISIBLE);
        mFoundDeviceLL.removeAllViews();
        View view = null;
        LayoutInflater inflater = getLayoutInflater();

        // 需要绑定的设备列表
        for (int i = 0; i < mFoundDeviceListItems.size(); i++){
            view = inflater.inflate(R.layout.device_listview_item, null);
            ImageView iv_device_icon = (ImageView)view.findViewById(R.id.list_item_device_icon);
            TextView tv_device_name = (TextView)view.findViewById(R.id.list_item_device_name);
            Button btn_device_connect = (Button)view.findViewById(R.id.list_item_device_action);
            btn_device_connect.setText("绑定");
            iv_device_icon.setImageResource(R.drawable.add_device);
            tv_device_name.setText(mFoundDeviceListItems.get(i).deviceName);
            final DeviceInfo deviceInfo = mFoundDeviceListItems.get(i).deviceInfo;
            final int index = i;
            btn_device_connect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String code = CODE;
                    Bundle bundle = new Bundle();
                    bundle.putString("awssVer", deviceInfo.awssVer.toString());
                    bundle.putString("productKey", deviceInfo.productKey);
                    bundle.putString("deviceName", deviceInfo.deviceName);
                    bundle.putString("regProductKey", deviceInfo.regProductKey);
                    bundle.putString("regDeviceName", deviceInfo.regDeviceName);
                    bundle.putString("token", deviceInfo.token);
                    bundle.putString("devType", deviceInfo.devType);
                    bundle.putString("addDeviceFrom", deviceInfo.addDeviceFrom);
                    bundle.putString("linkType", deviceInfo.linkType);
                    Router.getInstance().toUrlForResult(AddDeviceActivity.this, code, 1, bundle);
                }
            });

            mFoundDeviceLL.addView(view);
        }

        // 需要配网的设备列表
        for (int i = 0; i < mFoundDeviceNeedEnrolleeListItems.size(); i++){
            view = inflater.inflate(R.layout.device_listview_item, null);
            ImageView iv_device_icon = (ImageView)view.findViewById(R.id.list_item_device_icon);
            TextView tv_device_name = (TextView)view.findViewById(R.id.list_item_device_name);
            Button btn_device_connect = (Button)view.findViewById(R.id.list_item_device_action);
            btn_device_connect.setText("连接");
            iv_device_icon.setImageResource(R.drawable.add_device);
            tv_device_name.setText(mFoundDeviceNeedEnrolleeListItems.get(i).deviceName);
            final DeviceInfo deviceInfo = mFoundDeviceNeedEnrolleeListItems.get(i).deviceInfo;
            final int index = i;
            btn_device_connect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String code = CODE;
                    Bundle bundle = new Bundle();
                    if (deviceInfo.awssVer != null){
                        bundle.putString("awssVer", deviceInfo.awssVer.toString());
                    }
                    bundle.putString("productKey", deviceInfo.productKey);
                    bundle.putString("deviceName", deviceInfo.deviceName);
                    bundle.putString("regProductKey", deviceInfo.regProductKey);
                    bundle.putString("regDeviceName", deviceInfo.regDeviceName);
                    bundle.putString("token", deviceInfo.token);
                    bundle.putString("devType", deviceInfo.devType);
                    bundle.putString("addDeviceFrom", deviceInfo.addDeviceFrom);
                    bundle.putString("linkType", deviceInfo.linkType);

                    Router.getInstance().toUrlForResult(AddDeviceActivity.this, code, 1, bundle);
                }
            });

            mFoundDeviceLL.addView(view);
        }
    }

    private void getSupportDeviceListFromSever() {
        Map<String, Object> maps = new HashMap<>();
        IoTRequestBuilder builder = new IoTRequestBuilder()
                .setPath("/thing/productInfo/getByAppKey")
                .setApiVersion("1.1.1")
                .setAuthType("iotAuth")
                .setParams(maps);

        IoTRequest request = builder.build();

        IoTAPIClient ioTAPIClient = new IoTAPIClientFactory().getClient();
        ioTAPIClient.send(request, new IoTCallback() {
            @Override
            public void onFailure(IoTRequest ioTRequest, Exception e) {
                ALog.d("TAG", "onFailure");
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        fillSupportDeviceLL();
                    }
                });
            }

            @Override
            public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
                ALog.d("TAG", "onResponse");
                final int code = ioTResponse.getCode();
                final String msg = ioTResponse.getMessage();
                if (code != 200){
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "code = " +code + " msg =" + msg, Toast.LENGTH_SHORT).show();
                        }
                    });
                    return;
                }

                Object data = ioTResponse.getData();
                if (null != data) {
                    if(data instanceof JSONArray){
                        mSupportDeviceListItems = parseSupportDeviceListFromSever((JSONArray) data);
//                        LogUtils.logE("supportDevices",data.toString());

                    }
                }
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        fillSupportDeviceLL();
                    }
                });
            }
        });



    }

    private ArrayList<JSONObject> mSupportDeviceList;
    private ArrayList<SupportDeviceListItem> parseSupportDeviceListFromSever(JSONArray jsonArray) {
        return  new Gson().fromJson(jsonArray.toString(),new TypeToken<List<SupportDeviceListItem>>(){}.getType());
//        ArrayList<SupportDeviceListItem> arrayList = new ArrayList<>();
//        for (int i = 0; i < jsonArray.length(); i++) {
//            try {
//                JSONObject jsonObject = jsonArray.getJSONObject(i);
//                SupportDeviceListItem device = new SupportDeviceListItem();
//                device.name = jsonObject.getString("name");
//                device.productKey = jsonObject.getString("productKey");
//
//                arrayList.add(device);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//        return arrayList;
    }

    private boolean isDeviceBound(String deviceId){
        if (mDeviceList == null){
            return false;
        }
        if (mDeviceList.contains(deviceId)){
            return true;
        }
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 获取之前发现的所有设备
        List<DeviceInfo> list = LocalDeviceMgr.getInstance().getLanDevices();

        mFoundDeviceListItems = new ArrayList<>();
        ILocalDeviceMgr localDeviceMgr=LocalDeviceMgr.getInstance();
        LocalDeviceMgr.getInstance().startDiscovery(this, new IDiscoveryListener() {
            @Override
            public void onLocalDeviceFound(DeviceInfo deviceInfo) {
                //要绑定 需要在一分钟之内，否则绑定失败，需要重新发现再绑定
                Log.e("onLocalDevice", deviceInfo.toString());
                FoundDeviceListItem deviceListItem = new FoundDeviceListItem();
                deviceListItem.deviceName = deviceInfo.deviceName;
                deviceListItem.productKey = deviceInfo.productKey;
                deviceListItem.deviceInfo = deviceInfo;
                deviceListItem.deviceStatus = FoundDeviceListItem.NEED_BIND;
                if (false == isDeviceBound(deviceInfo.productKey + deviceInfo.deviceName)){
                    mFoundDeviceListItems.add(deviceListItem);
                }
                fillFoundDeviceLL();
            }

            @Override
            public void onEnrolleeDeviceFound(List<DeviceInfo> list) {
                Log.e("onEnrolleeDeviceFound", list.toString());
                //要配网
                for (DeviceInfo deviceInfo : list) {
                    FoundDeviceListItem deviceListItem = new FoundDeviceListItem();
                    deviceListItem.deviceStatus = FoundDeviceListItem.NEED_CONNECT;
                    deviceListItem.deviceInfo = deviceInfo;
                    deviceListItem.deviceName = deviceInfo.deviceName;
                    deviceListItem.productKey = deviceInfo.productKey;
                    if (false == isDeviceBound(deviceInfo.productKey + deviceInfo.deviceName)){
                        mFoundDeviceNeedEnrolleeListItems.add(deviceListItem);
                    }
                }
                fillFoundDeviceLL();
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        mLocalDevicePanelFl.setVisibility(View.GONE);
        LocalDeviceMgr.getInstance().stopDiscovery();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean hasAllPermission = true;
        if (requestCode == PERMISSION_REQUEST_CODE) {
            for (int i : grantResults) {
                if (i != PackageManager.PERMISSION_GRANTED) {
                    hasAllPermission = false;   //判断用户是否同意获取权限
                    break;
                }
            }

            //如果同意权限
            if (hasAllPermission) {
                mHasPermission = true;

            } else {  //用户不同意权限
                mHasPermission = false;
                Toast.makeText(AddDeviceActivity.this,"获取权限失败",Toast.LENGTH_SHORT).show();
            }
        }
    }
}

