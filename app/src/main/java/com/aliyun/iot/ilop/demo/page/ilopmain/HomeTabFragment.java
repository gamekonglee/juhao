package com.aliyun.iot.ilop.demo.page.ilopmain;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.aliyun.alink.linksdk.tmp.TmpSdk;
import com.aliyun.alink.linksdk.tmp.api.TmpInitConfig;
import com.aliyun.alink.linksdk.tmp.device.panel.PanelDevice;
import com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback;
import com.aliyun.iot.aep.component.router.Router;
import com.aliyun.iot.ilop.demo.view.DevicePanelView;
import com.aliyun.iot.aep.sdk.apiclient.IoTAPIClient;
import com.aliyun.iot.aep.sdk.apiclient.IoTAPIClientFactory;
import com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback;
import com.aliyun.iot.aep.sdk.apiclient.callback.IoTResponse;
import com.aliyun.iot.aep.sdk.apiclient.request.IoTRequest;
import com.aliyun.iot.aep.sdk.apiclient.request.IoTRequestBuilder;
import com.aliyun.iot.aep.sdk.log.ALog;
import com.aliyun.iot.aep.sdk.login.LoginBusiness;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import bc.juhao.com.R;

public class HomeTabFragment extends android.support.v4.app.Fragment{
    private String TAG = HomeTabFragment.class.getSimpleName();
    private Button mIlopMainAddBtn;
    private Button mIlopMainAddBigBtn;
    private Button mIlopMainMenuAddDeviceBtn;
    private Button mIlopMainMenuScanBtn;
    private FrameLayout mIlopMainMenu;
    private FrameLayout mVDevicePanel;
    private FrameLayout mMyDevicePanel;
    private FrameLayout mMyDevicePanelAdd;
    private Handler mHandler = new Handler();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.hometab_fragment_layout, null);
        mIlopMainAddBtn = (Button) view.findViewById(R.id.ilop_main_add_btn);
        mIlopMainAddBigBtn = (Button) view.findViewById(R.id.ilop_main_add_big_btn);
        mIlopMainMenuScanBtn = (Button) view.findViewById(R.id.ilop_main_menu_scan_btn);
        mIlopMainMenuAddDeviceBtn = (Button) view.findViewById(R.id.ilop_main_menu_add_device_btn);
        mIlopMainMenu = (FrameLayout) view.findViewById(R.id.ilop_main_menu);
        mMyDevicePanel = (FrameLayout) view.findViewById(R.id.my_device_panel);
        mVDevicePanel = (FrameLayout) view.findViewById(R.id.my_vdevice_panel);
        mMyDevicePanelAdd = (FrameLayout) view.findViewById(R.id.my_device_panel_add);

        mIlopMainAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mIlopMainMenu.setVisibility(View.VISIBLE);
                mIlopMainMenu.bringToFront();
            }
        });
        mIlopMainAddBigBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Router.getInstance().toUrl(getContext(), "page/ilopadddevice", mBundle);
            }
        });
        mIlopMainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mIlopMainMenu.setVisibility(View.INVISIBLE);
            }
        });
        mIlopMainMenuAddDeviceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Router.getInstance().toUrl(getContext(), "page/ilopadddevice", mBundle);
            }
        });
        mIlopMainMenuScanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Router.getInstance().toUrl(getContext(), "page/scan");
            }
        });

        return view;
    }

    int mRegisterCount = 0;
    private void listByAccount(){
        Map<String, Object> maps = new HashMap<>();
        IoTRequestBuilder builder = new IoTRequestBuilder()
                .setPath("/uc/listByAccount")
                .setApiVersion("1.0.0")
                .setAuthType("iotAuth")
                .setParams(maps);

        IoTRequest request = builder.build();

        IoTAPIClient ioTAPIClient = new IoTAPIClientFactory().getClient();
        ioTAPIClient.send(request, new IoTCallback() {
            @Override
            public void onFailure(IoTRequest ioTRequest, Exception e) {
                ALog.d(TAG, "onFailure");
            }

            @Override
            public void onResponse(IoTRequest ioTRequest, IoTResponse response) {
                ALog.d(TAG, "onResponse listByAccount");
                final int code = response.getCode();
                final String msg = response.getMessage();
                if (code != 200){
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(), "code = " +code + " msg =" + msg, Toast.LENGTH_SHORT).show();
                        }
                    });
                    return;
                }

                Object data = response.getData();
                if (null != data) {
                    if(data instanceof JSONArray){
                        mDeviceList = parseDeviceListFromSever((JSONArray) data);
                        String[] pks = {"a1IjeL0MqPS", "a1AzoSi5TMc", "a1nZ7Kq7AG1", "a1XoFUJWkPr"};
                        if (mDeviceList.size() == 0 || virturlDeviceCount < pks.length - 1){
//                            if (mRegisterCount > 0){
//                                return;
//                            }
//                            //注册虚拟设备
//
//                            for (String pk : pks) {
//                                registerVirtualDevice(pk);
//                            }
                        }else {
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    initDevicePanel();
                                }
                            });
                        }
                    }
                }

            }
        });
    }

    private void bindVirturalToUser(String pk, String dn){
        Map<String, Object> maps = new HashMap<>();
        maps.put("productKey", pk);
        maps.put("deviceName", dn);
        IoTRequestBuilder builder = new IoTRequestBuilder()
                .setPath("/thing/virtual/binduser")
                .setApiVersion("1.0.0")
                .setAuthType("iotAuth")
                .setParams(maps);

        IoTRequest request = builder.build();

        IoTAPIClient ioTAPIClient = new IoTAPIClientFactory().getClient();
        ioTAPIClient.send(request, new IoTCallback() {
            @Override
            public void onFailure(IoTRequest ioTRequest, Exception e) {
                ALog.d(TAG, "onFailure");
            }

            @Override
            public void onResponse(IoTRequest ioTRequest, IoTResponse response) {
                final int code = response.getCode();
                final String msg = response.getMessage();
                mRegisterCount++;
                if (mRegisterCount == 4){
                    listByAccount();
                }
                if (code != 200){
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getContext(), "code = " +code + " msg =" + msg, Toast.LENGTH_SHORT).show();
                        }
                    });
                    return;
                }
            }
        });
    }

    private void registerVirtualDevice(String pk) {
        Map<String, Object> maps = new HashMap<>();
        maps.put("productKey", pk);
        IoTRequestBuilder builder = new IoTRequestBuilder()
                .setPath("/thing/virtual/register")
                .setApiVersion("1.0.0")
                .setAuthType("iotAuth")
                .setParams(maps);

        IoTRequest request = builder.build();

        IoTAPIClient ioTAPIClient = new IoTAPIClientFactory().getClient();
        ioTAPIClient.send(request, new IoTCallback() {
            @Override
            public void onFailure(IoTRequest ioTRequest, Exception e) {
                ALog.d(TAG, "onFailure");
            }

            @Override
            public void onResponse(IoTRequest ioTRequest, IoTResponse response) {
                ALog.d(TAG, "onResponse registerVirtualDevice");
                final int code = response.getCode();
                final String msg = response.getMessage();
                if (code != 200){
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getContext(), "code = " +code + " msg =" + msg, Toast.LENGTH_SHORT).show();
                        }
                    });
                    return;
                }

                Object data = response.getData();
                if (null != data) {
                    if (data instanceof JSONObject) {
                        try {
                            String dn = ((JSONObject) data).getString("deviceName");
                            String pk = ((JSONObject) data).getString("productKey");
                            bindVirturalToUser(pk, dn);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }

            }
        });
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale+0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    private void initDevicePanel() {
        if (mMyDevicePanel.getChildCount() > 1){
            mMyDevicePanel.removeViews(1, mMyDevicePanel.getChildCount() - 1);
        }
        if (mVDevicePanel.getChildCount() > 1){
            mVDevicePanel.removeViews(1, mVDevicePanel.getChildCount() - 1);
        }

        Resources resources = this.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        float density = dm.density;
        int width = dm.widthPixels;
        int height = dm.heightPixels;

        ArrayList<JSONObject> deviceList;
        deviceList = mDeviceList;
        int index_my = 0;
        int index_v = 0;
        for (int i = 0; i < deviceList.size(); i++) {
            String deviceType = "";
            String status = "";
            JSONObject device = deviceList.get(i);
            DevicePanelView devicePanelView = new DevicePanelView(getContext());
            try {
                devicePanelView.setName(device.getString("name"));
                deviceType = device.getString("type");
                devicePanelView.setType(deviceType);
                status = device.getString("status");
                devicePanelView.setStatus(status);
                final String code = device.getString("productKey");
                final String iotId = device.getString("iotId");

                devicePanelView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ALog.d(TAG, "devicePanelView onClick");
                        Toast.makeText(getActivity(), "本版本不支持插件打开", Toast.LENGTH_SHORT).show();
                        TmpSdk.init(getActivity(), new TmpInitConfig(TmpInitConfig.ONLINE));
                        TmpSdk.getDeviceManager().discoverDevices(null,5000,null);
                        PanelDevice panelDevice = new PanelDevice(iotId);
                        panelDevice.init(getActivity(), new IPanelCallback() {
                            @Override
                            public void onComplete(boolean b, Object o) {

                            }
                        });
                        panelDevice.getStatus(new IPanelCallback() {
                            @Override
                            public void onComplete(boolean bSuc, Object o) {
                                ALog.d(TAG,"getStatus(), request complete," + bSuc);
                                try {
                                    JSONObject data = new JSONObject((String)o);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        String paramsStr="";
                        panelDevice.invokeService(paramsStr, new IPanelCallback() {
                            @Override
                            public void onComplete(boolean bSuc, Object o) {
                                ALog.d(TAG,"callService(), request complete,"+bSuc);
                                try {
                                    JSONObject data = new JSONObject((String)o);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
//                        Bundle bundle = new Bundle();
//                        bundle.putString("iotId", iotId);
//                        Router.getInstance().toUrl(getActivity(), code, bundle);
                    }
                });
            } catch (JSONException e) {
                ALog.e(TAG, "initDevicePanel ", new Exception(e));
            }
            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams((width-30) / 2,
                    ViewGroup.LayoutParams.WRAP_CONTENT);

            if(deviceType.equalsIgnoreCase("虚拟")){
                lp.setMargins(width / 2 * (index_v % 2) + dip2px(getContext(), 5),
                        dip2px(getContext(), 70) * (index_v / 2) + dip2px(getContext(), 26),
                        0, 0);
                index_v++;
                mVDevicePanel.addView(devicePanelView, lp);
            }else {
                lp.setMargins(width / 2 * (index_my % 2) + dip2px(getContext(), 5),
                        dip2px(getContext(), 70) * (index_my / 2) + dip2px(getContext(), 26),
                        0, 0);
                index_my++;
                mMyDevicePanel.addView(devicePanelView, lp);
                mMyDevicePanelAdd.setVisibility(View.GONE);
            }
            if (status.equalsIgnoreCase("离线")){
                    float alpha = 255 * 0.8f;
                    devicePanelView.setAlpha(alpha / 255);
            }

        }
    }

    private Bundle mBundle = new Bundle();
    private ArrayList<JSONObject> mDeviceList;
    private int virturlDeviceCount = 0;
    private ArrayList<JSONObject> parseDeviceListFromSever(JSONArray jsonArray) {
        virturlDeviceCount = 0;
        ArrayList<JSONObject> arrayList = new ArrayList<>();
        ArrayList<String> deviceStrList = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                JSONObject device = new JSONObject();
                device.put("name", jsonObject.getString("productName"));

                String type = "虚拟";
                if ("VIRTUAL".equalsIgnoreCase(jsonObject.getString("thingType"))){
                    type = "虚拟";
                    virturlDeviceCount++;
                }else{
                    type = jsonObject.getString("netType");
                }
                device.put("type", type);
                String statusStr = "离线";
                if (1 == jsonObject.getInt("status")){
                    statusStr = "在线";
                }
                device.put("status", statusStr);
                device.put("productKey", jsonObject.getString("productKey"));
                device.put("iotId", jsonObject.getString("iotId"));
                device.put("deviceName", jsonObject.getString("deviceName"));
                deviceStrList.add(jsonObject.getString("productKey") + jsonObject.getString("deviceName"));
                arrayList.add(device);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        mBundle.putStringArrayList("deviceList", deviceStrList);
        return arrayList;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            Log.d(TAG, "onActivityResult");
            if (data.getStringExtra("productKey") != null){
                Bundle bundle = new Bundle();
                bundle.putString("productKey", data.getStringExtra("productKey"));
                bundle.putString("deviceName", data.getStringExtra("deviceName"));
                bundle.putString("token", data.getStringExtra("token"));
                Intent intent = new Intent(getActivity(), HomeTabFragment.class);
                intent.putExtras(bundle);
                this.startActivity(intent);
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mIlopMainMenu.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (LoginBusiness.isLogin()){
            listByAccount();
        }
    }
}
