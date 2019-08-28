package bc.juhao.com.ui.fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aliyun.alink.linksdk.tmp.TmpSdk;
import com.aliyun.alink.linksdk.tmp.api.OutputParams;
import com.aliyun.alink.linksdk.tmp.api.TmpInitConfig;
import com.aliyun.alink.linksdk.tmp.device.panel.PanelDevice;
import com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback;
import com.aliyun.alink.linksdk.tmp.listener.IDevListener;
import com.aliyun.alink.linksdk.tmp.utils.ErrorInfo;
import com.aliyun.iot.aep.sdk.apiclient.IoTAPIClient;
import com.aliyun.iot.aep.sdk.apiclient.IoTAPIClientFactory;
import com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback;
import com.aliyun.iot.aep.sdk.apiclient.callback.IoTResponse;
import com.aliyun.iot.aep.sdk.apiclient.request.IoTRequest;
import com.aliyun.iot.aep.sdk.apiclient.request.IoTRequestBuilder;
import com.aliyun.iot.aep.sdk.log.ALog;
import com.aliyun.iot.aep.sdk.login.LoginBusiness;
import com.aliyun.iot.ilop.demo.page.ilopmain.AddDeviceActivity;
import com.aliyun.iot.ilop.demo.view.DevicePanelView;
import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.google.gson.Gson;
import com.zhy.http.okhttp.callback.Callback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import astuetz.MyPagerSlidingTabStrip;
import bc.juhao.com.R;
import bc.juhao.com.adapter.BaseAdapterHelper;
import bc.juhao.com.adapter.QuickAdapter;
import bc.juhao.com.bean.AccountDevDTO;
import bc.juhao.com.bean.WeatherBean;
import bc.juhao.com.common.BaseFragment;
import bc.juhao.com.cons.Constance;
import bc.juhao.com.net.ApiClient;
import bc.juhao.com.ui.activity.intelligence.DevicesControlActivity;
import bc.juhao.com.ui.view.PMSwipeRefreshLayout;
import bocang.utils.LogUtils;
import bocang.utils.MyToast;
import bocang.utils.UIUtils;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by gamekonglee on 2018/7/7.
 */

public class ItHomeMainFragment extends BaseFragment implements View.OnClickListener {

    private static final String TAG = "ItHomeMainFragment";
    private MyPagerSlidingTabStrip tabs;
    private ViewPager vp_it;
    private String[] titles;
    private int virturlDeviceCount;
    private ArrayList<JSONObject> mDeviceList;
    int mRegisterCount = 0;
    private QuickAdapter<AccountDevDTO> accountDevDTOQuickAdapter;
    private List<AccountDevDTO> accountDevDTOS;
    private ListView lv_it;
    private View view;
    private List<QuickAdapter> adapters;
    private ImageView iv_add;
    private List<ListView> listViews;
    private int currentPosition;
    private LinearLayout ll_none_device;
    private List<View> views;
    private LinearLayout ll_cmd;
    private TextView tv_temp_outside;
    private TextView tv_pm_outside;
    private List<PMSwipeRefreshLayout> pms;
    private RelativeLayout rl_hj;
    private RelativeLayout rl_lj;
    private RelativeLayout rl_qc;
    private RelativeLayout rl_sj;
    private RelativeLayout rl_xx;
    private RelativeLayout rl_jc;
    private int status;
    private String iotId;

    @Override
    protected void initController() {

    }

    @Override
    protected void initViewData() {

    }

    @Override
    protected void initView() {
        tabs = getView().findViewById(R.id.tabs);
        vp_it = getView().findViewById(R.id.vp_it);
        iv_add = getView().findViewById(R.id.iv_add);
        ll_cmd = getView().findViewById(R.id.ll_cmd);
        tv_temp_outside = getView().findViewById(R.id.tv_temp_outside);
        tv_pm_outside = getView().findViewById(R.id.tv_pm_outside);
        rl_hj = getView().findViewById(R.id.rl_hj);
        rl_lj = getView().findViewById(R.id.rl_lj);
        rl_qc = getView().findViewById(R.id.rl_qc);
        rl_sj = getView().findViewById(R.id.rl_sj);
        rl_xx = getView().findViewById(R.id.rl_xx);
        rl_jc = getView().findViewById(R.id.rl_jc);
        rl_hj.setOnClickListener(this);
        rl_lj.setOnClickListener(this);
        rl_qc.setOnClickListener(this);
        rl_sj.setOnClickListener(this);
        rl_xx.setOnClickListener(this);
        rl_jc.setOnClickListener(this);

        iv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AddDeviceActivity.class));
            }
        });
        vp_it.setAdapter(new MyPagerAdapter());
        tabs.defaultColor=getActivity().getResources().getColor(R.color.txt_black);
        tabs.selectColor=getActivity().getResources().getColor(R.color.txt_black);
        tabs.setUnderlineColor(Color.TRANSPARENT);
        tabs.setIndicatorColor(getActivity().getResources().getColor(R.color.green));
        tabs.setDividerColor(Color.TRANSPARENT);
        tabs.setViewPager(vp_it);
        accountDevDTOS=new ArrayList<>();
        adapters = new ArrayList<>();
        listViews = new ArrayList<>();
        views = new ArrayList<>();
        pms = new ArrayList<>();
        tabs.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentPosition = position;
                if(position<adapters.size())adapters.get(position).replaceAll(accountDevDTOS);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        //打开Log 输出
        ALog.setLevel(ALog.LEVEL_DEBUG);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
         requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},100);
        }else {
        initWeather();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==100&&grantResults[0]== PackageManager.PERMISSION_GRANTED){
            initWeather();
        }
    }
    public LocationClient mLocationClient = null;
    private MyLocationListener myListener = new MyLocationListener();
    private void initWeather() {

//BDAbstractLocationListener为7.2版本新增的Abstract类型的监听接口
//原有BDLocationListener接口暂时同步保留。具体介绍请参考后文中的说明
        mLocationClient = new LocationClient(getActivity());
            //声明LocationClient类
        mLocationClient.registerLocationListener(myListener);
            //注册监听函数
        LocationClientOption option = new LocationClientOption();
        option.setIsNeedAddress(true);
//可选，是否需要地址信息，默认为不需要，即参数为false
//如果开发者需要获得当前点的地址信息，此处必须为true
        mLocationClient.setLocOption(option);
//mLocationClient为第二步初始化过的LocationClient对象
//需将配置好的LocationClientOption对象，通过setLocOption方法传递给LocationClient对象使用
//更多LocationClientOption的配置，请参照类参考中LocationClientOption类的详细说明
        mLocationClient.start();
    }

    @Override
    protected void initData() {
        titles = new String[]{"所有设备","客厅"};
    }
    private Bundle mBundle = new Bundle();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_it_main_home_new,null);
    }

    @Override
    public void onClick(View v) {
        if(status!=1){
            MyToast.show(getActivity(),"该设备处于离线状态");
            return;
        }
        switch (v.getId()){

            case R.id.rl_hj:
                changeNight(Constance.ColorTemperature,4000);
                break;
            case R.id.rl_lj:
                changeNight(Constance.LightSwitch,0);
                break;
            case R.id.rl_qc:
                changeNight(Constance.ColorTemperature,5000);
                break;
            case R.id.rl_sj:
                changeNight(Constance.LightSwitch,0);
                break;
            case R.id.rl_xx:
                changeNight(Constance.ColorTemperature,1000);
                break;
            case R.id.rl_jc:
                changeNight(Constance.ColorTemperature,7000);
                break;

        }
    }

    class MyPagerAdapter extends PagerAdapter {

        private View view;

        @Override
        public int getCount() {
            return titles.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
//            if(view==null)
            view = View.inflate(getActivity(), R.layout.view_it_home,null);
            lv_it = view.findViewById(R.id.lv_it);
            ll_none_device = view.findViewById(R.id.ll_none_device);
            PMSwipeRefreshLayout pullToRefresh=view.findViewById(R.id.pullToRefresh);
            pullToRefresh.setColorSchemeColors(Color.BLUE,Color.GREEN,Color.RED);
            pullToRefresh.setRefreshing(false);
            pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    listByAccount();
                }
            });
            accountDevDTOQuickAdapter = new QuickAdapter<AccountDevDTO>(getActivity(), R.layout.item_home_dev) {
                @Override
                protected void convert(BaseAdapterHelper helper, AccountDevDTO item) {
                    helper.setText(R.id.tv_name,item.getName());
                    helper.setText(R.id.tv_scene,"客厅");
                    helper.setText(R.id.tv_status,item.getStatus());
                    int resId=R.mipmap.home_kg;
                    if(item.getName().contains("插座")){
                        resId=R.mipmap.home_cz;
                    }else if(item.getName().contains("开关")){
                        resId=R.mipmap.home_kg;
                    }else if(item.getName().contains("灯")){
                        resId=R.mipmap.home_zm;
                    }
                    helper.setImageResource(R.id.iv_img,resId);
                }
            };
            if(position>=listViews.size()){
                listViews.add(lv_it);
            }else {
                listViews.set(position,lv_it);
            }
            if(position>=adapters.size()){
                adapters.add(accountDevDTOQuickAdapter);
            }else {
                adapters.set(position,accountDevDTOQuickAdapter);
            }
            if(position>=views.size()){
                views.add(ll_none_device);
            }else {
                views.set(position,ll_none_device);
            }
            if(position>=pms.size()){
                pms.add(pullToRefresh);
            }else {
                pms.set(position,pullToRefresh);
            }
            lv_it.setAdapter(accountDevDTOQuickAdapter);
            lv_it.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent=new Intent(getActivity(), DevicesControlActivity.class);
                    if(accountDevDTOS==null||accountDevDTOS.size()<=position){
                        return;
                    }
                    intent.putExtra(Constance.iotId,accountDevDTOS.get(position).getIotId());
                    String type=Constance.night;
                    if(accountDevDTOS.get(position).getName().contains("开关")){
                        type=Constance.nightswitch;
                    }else if(accountDevDTOS.get(position).getName().contains("插座")){
                        type=Constance.socket;
                    }else if(accountDevDTOS.get(position).getName().contains("锁")){
                        type=Constance.lock;}
                    Log.e("type",type);
                    intent.putExtra(Constance.type,type);
                    startActivity(intent);

                }
            });
            lv_it.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                    UIUtils.showSingleWordDialog(getActivity(), "确定要解绑此设备吗？", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Map<String, Object> maps = new HashMap<>();
                            maps.put("iotId",accountDevDTOS.get(position).getIotId());
                            IoTRequestBuilder builder = new IoTRequestBuilder()
                                    .setPath("/uc/unbindAccountAndDev")
                                    .setApiVersion("1.0.2")
                                    .setAuthType("iotAuth")
                                    .setParams(maps);

                            IoTRequest request = builder.build();

                            IoTAPIClient ioTAPIClient = new IoTAPIClientFactory().getClient();
                            ioTAPIClient.send(request, new IoTCallback() {
                                @Override
                                public void onFailure(IoTRequest ioTRequest, Exception e) {

                                }

                                @Override
                                public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
                                    Log.e("unbindAccountAndDev",ioTResponse.toString());
                                    listByAccount();
                                }
                            });
                        }
                    });

                    return true;
                }
            });
            Button btn_add_device= view.findViewById(R.id.btn_add_device);
            btn_add_device.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Router.getInstance().toUrl(getContext(), "page/ilopadddevice", mBundle);
                    Intent intent=new Intent(getActivity(),AddDeviceActivity.class);
                    intent.putExtra("bundle",mBundle);
                    startActivity(intent);
                }
            });
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container,position,object);
            container.removeView((View) object);
        }
    }

    private void listByAccount(){
        accountDevDTOS = new ArrayList<>();
        Map<String, Object> maps = new HashMap<>();
        IoTRequestBuilder builder = new IoTRequestBuilder()
                .setPath("/uc/listByAccount")
                .setApiVersion("1.0.0")
                .setAuthType("iotAuth")
                .setParams(maps);

        IoTRequest request = builder.build();

        IoTAPIClient ioTAPIClient = new IoTAPIClientFactory().getClient();
        if(pms==null||pms.size()==0){
            pms.add(null);
        }
        final PMSwipeRefreshLayout temp=pms.get(currentPosition);
        ioTAPIClient.send(request, new IoTCallback() {
            @Override
            public void onFailure(IoTRequest ioTRequest, Exception e) {
                ALog.d(TAG, "onFailure");
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if(temp!=null){
                            temp.post(new Runnable() {
                                @Override
                                public void run() {
                                    temp.setRefreshing(false);
                                }
                            });
                        }
                    }
                });

            }

            @Override
            public void onResponse(IoTRequest ioTRequest, IoTResponse response) {
                ALog.d(TAG, "onResponse listByAccount");
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if(temp!=null){
                            temp.post(new Runnable() {
                                @Override
                                public void run() {
                                    temp.setRefreshing(false);
                                }
                            });
                        }
                    }
                });
                final int code = response.getCode();
                final String msg = response.getMessage();
                if (code != 200){
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
//                            Toast.makeText(getActivity(), "code = " +code + " msg =" + msg, Toast.LENGTH_SHORT).show();
                        }
                    });
                    return;
                }

                Object data = response.getData();
                if (null != data) {
                    if(data instanceof JSONArray){
                        mDeviceList = parseDeviceListFromSever((JSONArray) data);
                        if(mDeviceList==null||mDeviceList.size()==0){
                        mHandler.sendEmptyMessage(1);
                        return;
                        }
                        LogUtils.logE("mDevices",mDeviceList.toString());
                        for(int i=0;i<mDeviceList.size();i++){
                            try {
                                if(!mDeviceList.get(i).getString(Constance.type).equals("虚拟")){
                                accountDevDTOS.add(new Gson().fromJson(mDeviceList.get(i).toString(),AccountDevDTO.class));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        for(int i=0;i<accountDevDTOS.size();i++){
                            for(int j=0;j<accountDevDTOS.size();j++){
                            if(i!=j&&accountDevDTOS.get(i).getIotId().equals(accountDevDTOS.get(j).getIotId())){
                                accountDevDTOS.remove(j);
                                if(j!=0)j--;
                            }
                            }
                        }

                        mHandler.sendEmptyMessage(0);
                        String[] pks = {"a1IjeL0MqPS", "a1AzoSi5TMc", "a1nZ7Kq7AG1", "a1XoFUJWkPr"};
                        if (mDeviceList.size() == 0 || virturlDeviceCount < pks.length - 1){
                            if (mRegisterCount > 0){
                                return;
                            }
                            //注册虚拟设备

                            for (String pk : pks) {
//                                registerVirtualDevice(pk);
                            }
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

    private void initDevicePanel() {

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
//                mVDevicePanel.addView(devicePanelView, lp);
            }else {
                lp.setMargins(width / 2 * (index_my % 2) + dip2px(getContext(), 5),
                        dip2px(getContext(), 70) * (index_my / 2) + dip2px(getContext(), 26),
                        0, 0);
                index_my++;
//                mMyDevicePanel.addView(devicePanelView, lp);
//                mMyDevicePanelAdd.setVisibility(View.GONE);
            }
            if (status.equalsIgnoreCase("离线")){
                float alpha = 255 * 0.8f;
                devicePanelView.setAlpha(alpha / 255);
            }

        }
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

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what==1){
             views.get(currentPosition).setVisibility(View.VISIBLE);
             listViews.get(currentPosition).setVisibility(View.GONE);
                LinearLayout.LayoutParams layoutParams= (LinearLayout.LayoutParams) vp_it.getLayoutParams();
                layoutParams.height= (UIUtils.dip2PX(400));
                vp_it.setLayoutParams(layoutParams);
                ll_cmd.setVisibility(View.GONE);
            }else if(msg.what==0){
                views.get(currentPosition).setVisibility(View.GONE);
                listViews.get(currentPosition).setVisibility(View.VISIBLE);
            adapters.get(currentPosition).replaceAll(accountDevDTOS);
            LinearLayout.LayoutParams layoutParams= (LinearLayout.LayoutParams) vp_it.getLayoutParams();
            layoutParams.height= UIUtils.initListViewHeight(listViews.get(currentPosition));
            vp_it.setLayoutParams(layoutParams);
                ll_cmd.setVisibility(View.GONE);
                iotId = "";
                if(accountDevDTOS==null||accountDevDTOS.size()==0)return;
                for(int i=0;i<accountDevDTOS.size();i++){
                    if(accountDevDTOS.get(i).getName().contains("灯")){
                        iotId =accountDevDTOS.get(i).getIotId();
                        break;
                    }
                }
                if(TextUtils.isEmpty(iotId))return;
                ll_cmd.setVisibility(View.VISIBLE);
                TmpSdk.init(getActivity(), new TmpInitConfig(TmpInitConfig.ONLINE));
                TmpSdk.getDeviceManager().discoverDevices(null, 5000, new IDevListener() {
                    @Override
                    public void onSuccess(Object o, OutputParams outputParams) {

                    }

                    @Override
                    public void onFail(Object o, ErrorInfo errorInfo) {

                    }
                });

                final PanelDevice panelDevice = new PanelDevice(iotId);
                panelDevice.init(getActivity(), new IPanelCallback() {
                    @Override
                    public void onComplete(boolean b, Object o) {
                        Log.e("panelDevice","onComplete:"+b+o);
                        panelDevice.getStatus(new IPanelCallback() {
                            @Override
                            public void onComplete(boolean bSuc, Object o) {
                                Log.e("getStatus","onComplete:"+bSuc);
                                try {
                                    JSONObject data = new JSONObject((String)o);
                                    status = data.getJSONObject(Constance.data).getInt(Constance.status);
                                    if(status ==1){
                                        return;
                                    }
//                                    Log.e("panelDevice_status", status +"");

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                });
            }else if(msg.what==3){
                MyToast.show(getActivity(),"设置成功");
            }

        }
    };
    @Override
    public void onResume() {
        super.onResume();

        if (LoginBusiness.isLogin()){
            listByAccount();
//            bindmqtt();
        }
    }

    public class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location){
            //此处的BDLocation为定位结果信息类，通过它的各种get方法可获取定位相关的全部结果
            //以下只列举部分获取地址相关的结果信息
            //更多结果信息获取说明，请参照类参考中BDLocation类中的说明

            String addr = location.getAddrStr();    //获取详细地址信息
            String country = location.getCountry();    //获取国家
            String province = location.getProvince();    //获取省份
            String city = location.getCity();    //获取城市
            String district = location.getDistrict();    //获取区县
            String street = location.getStreet();    //获取街道信息
            Log.e("district",district);
            mLocationClient.stop();
            ApiClient.getWeather(district,new Callback<String>() {
                @Override
                public String parseNetworkResponse(Response response, int id) throws Exception {
                    return null;
                }

                @Override
                public void onError(Call call, Exception e, int id) {

                }

                @Override
                public String onResponse(String response, int id) {
                    Log.e("onWeather_response",response);
                    try {
                        WeatherBean weatherBean=new Gson().fromJson(response,WeatherBean.class);
                        String temp=weatherBean.getResults().get(0).getWeather_data().get(0).getDate();
                        int index=temp.indexOf("实时")+3;
                        temp=temp.substring(index,temp.length()-1);
                        String pm25=weatherBean.getResults().get(0).getPm25();
                        tv_temp_outside.setText(temp+"");
                        tv_pm_outside.setText(""+pm25);
                    }catch (Exception e){
                    }
                    return null;
                }
            });

        }
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale+0.5f);
    }
    private void changeNight(String identify,Object value) {
        Map<String, Object> maps = new HashMap<>();
        if(TextUtils.isEmpty(iotId))return;
        maps.put("iotId", iotId);
        com.alibaba.fastjson.JSONObject jsonObject=new com.alibaba.fastjson.JSONObject();
        jsonObject.put(identify,value);
        maps.put("items",jsonObject);
        IoTRequestBuilder ioTRequestBuilder = new IoTRequestBuilder()
                .setPath("/thing/properties/set")
                .setApiVersion("1.0.2")
                .setAuthType("iotAuth")
                .setParams(maps);
        IoTRequest request = ioTRequestBuilder.build();
        IoTAPIClient client = new IoTAPIClientFactory().getClient();
        client.send(request, new IoTCallback() {
            @Override
            public void onFailure(IoTRequest ioTRequest, Exception e) {

            }

            @Override
            public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
                LogUtils.logE("onResponse", ioTResponse.getCode() + "," + ioTResponse.getMessage() + "," + ioTResponse.getData());
                if(ioTResponse.getCode()==200){

                    mHandler.sendEmptyMessage(3);
//                    isOpen=!isOpen;
//                    handler.sendEmptyMessage(1);
                }
//                final ProgressDialog progressDialog=ProgressDialog.show(DevicesControlActivity.this,"请求中","");
//                new Thread(){
//                    @Override
//                    public void run() {
//                        super.run();
//                        SystemClock.sleep(1000);
//                        initStatus();
//                        progressDialog.dismiss();
//
//                    }
//                }.start();

            }
        });
    }
}
