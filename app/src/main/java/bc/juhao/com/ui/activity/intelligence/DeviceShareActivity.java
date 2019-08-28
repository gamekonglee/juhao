package bc.juhao.com.ui.activity.intelligence;

import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.sdk.android.openaccount.ui.ui.LoginActivity;
import com.aliyun.iot.aep.sdk.apiclient.IoTAPIClient;
import com.aliyun.iot.aep.sdk.apiclient.IoTAPIClientFactory;
import com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback;
import com.aliyun.iot.aep.sdk.apiclient.callback.IoTResponse;
import com.aliyun.iot.aep.sdk.apiclient.request.IoTRequest;
import com.aliyun.iot.aep.sdk.apiclient.request.IoTRequestBuilder;
import com.aliyun.iot.aep.sdk.login.ILogoutCallback;
import com.aliyun.iot.aep.sdk.login.LoginBusiness;
import com.aliyun.iot.ilop.demo.page.ilopmain.MainActivity;
import com.aliyun.iot.ilop.demo.page.main.IndexActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bc.juhao.com.R;
import bc.juhao.com.adapter.BaseAdapterHelper;
import bc.juhao.com.adapter.QuickAdapter;
import bc.juhao.com.bean.AccountDevDTO;
import bc.juhao.com.common.BaseActivity;
import bc.juhao.com.cons.Constance;
import bc.juhao.com.ui.view.EndOfListView;
import bocang.utils.LogUtils;
import bocang.utils.MyToast;

public class DeviceShareActivity extends BaseActivity implements View.OnClickListener {

    private TextView tv_no_device;
    private boolean isEdit;
    private TextView tv_choose;
    private TextView tv_device_my;
    private TextView tv_device_share;
    int position=0;
    private EndOfListView lv_devices;
    private List<AccountDevDTO> accountDevDTOS;
    private QuickAdapter<AccountDevDTO> accountDevDTOQuickAdapter;
    private boolean[] isSelected;
    private int currentP;

    @Override
    protected void InitDataView() {

    }

    @Override
    protected void initController() {

    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_device_share);
        tv_choose = findViewById(R.id.tv_choose);
        tv_device_my = findViewById(R.id.tv_device_my);
        tv_device_share = findViewById(R.id.tv_device_share);
        tv_no_device = findViewById(R.id.tv_no_devices);
        lv_devices = findViewById(R.id.lv_devices);
        tv_device_my.setOnClickListener(this);
        tv_device_share.setOnClickListener(this);
        tv_choose.setOnClickListener(this);
        accountDevDTOQuickAdapter = new QuickAdapter<AccountDevDTO>(this,R.layout.item_device_share) {
            @Override
            protected void convert(final BaseAdapterHelper helper, AccountDevDTO item) {
                helper.setText(R.id.tv_name,item.getProductName());
//                helper.setImageUrl(R.id.iv_img,item.getProductImage(), new ImageLoader(new RequestQueue()),)
                ImageView iv_img=helper.getView(R.id.iv_img);
                ImageView iv_check=helper.getView(R.id.iv_check);
                ImageView iv_arrow=helper.getView(R.id.iv_arrow);
                if(isEdit){
                    iv_arrow.setVisibility(View.GONE);
                    iv_check.setVisibility(View.VISIBLE);
                    if(isSelected!=null&&isSelected.length>helper.getPosition()){
                        if(isSelected[helper.getPosition()]){
                            iv_check.setBackgroundResource(R.mipmap.icon_check_sel);
                        }else {
                            iv_check.setBackgroundResource(R.mipmap.icon_check_nor);
                        }

                    }


                }else {
                    iv_arrow.setVisibility(View.VISIBLE);
                    iv_check.setVisibility(View.GONE);
                }


                ImageLoader.getInstance().displayImage(item.getProductImage(),iv_img);

            }
        };
        lv_devices.setAdapter(accountDevDTOQuickAdapter);
        accountDevDTOS = new ArrayList<>();
        lv_devices.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(isEdit){
                    if(isSelected!=null&&isSelected.length>i){
                    isSelected[i]=!isSelected[i];
                    for(int x=0;x<isSelected.length;x++){
                        if(x!=i){
                            isSelected[x]=false;
                        }
                    }
                        currentP = i;
                    accountDevDTOQuickAdapter.notifyDataSetChanged();
                    }

                }
            }
        });
        listDevices();
    }

    private void listDevices() {

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

            }

            @Override
            public void onResponse(IoTRequest ioTRequest, IoTResponse response) {
                final int code = response.getCode();
                final String msg = response.getMessage();
                if (code != 200){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(DeviceShareActivity.this, "code = " +code + " msg =" + msg, Toast.LENGTH_SHORT).show();
                            if(code==401){
                                LoginBusiness.logout(new ILogoutCallback() {
                                    @Override
                                    public void onLogoutSuccess() {
                                        startActivity(new Intent(DeviceShareActivity.this, MainActivity.class));
                                    }

                                    @Override
                                    public void onLogoutFailed(int i, String s) {

                                    }
                                });

//                                getActivity().finish();
                            }
                        }
                    });
                    return;
                }

                Object data = response.getData();
                if (null != data) {
                    if(data instanceof JSONArray){
//                        final List<JSONObject> mDeviceList = parseDeviceListFromSever((JSONArray) data);
                        accountDevDTOS=new Gson().fromJson(((JSONArray) data).toString(),new TypeToken<List<AccountDevDTO>>(){}.getType());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(accountDevDTOS==null||accountDevDTOS.size()==0){
                                tv_no_device.setVisibility(View.VISIBLE);
                                lv_devices.setVisibility(View.GONE);
                                    return;
                                }else {
                                    lv_devices.setVisibility(View.VISIBLE);
                                    tv_no_device.setVisibility(View.GONE);
                                }
                            }
                        });


                        LogUtils.logE("mDevices",accountDevDTOS.toString());
//                        for(int i=0;i<accountDevDTOS.size();i++){
//                            if(!accountDevDTOS.get(i).getThingType().equals("虚拟")){
//                                accountDevDTOS.add(new Gson().fromJson(accountDevDTOS.get(i).toString(), AccountDevDTO.class));
//                            }
//                        }
                        for(int i=0;i<accountDevDTOS.size();i++){
                            for(int j=0;j<accountDevDTOS.size();j++){
                                if(i!=j&&accountDevDTOS.get(i).getIotId().equals(accountDevDTOS.get(j).getIotId())){
                                    accountDevDTOS.remove(j);
                                    if(j!=0)j--;
                                }
                            }
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                accountDevDTOQuickAdapter.replaceAll(accountDevDTOS);
                            }
                        });

                    }
                }

            }
        });
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_choose:
                isEdit = !isEdit;
                if(isEdit){
                    tv_choose.setText("下一步");
                    if(accountDevDTOS==null||accountDevDTOS.size()==0){
                        return;
                    }
                    isSelected = new boolean[accountDevDTOS.size()];
                    accountDevDTOQuickAdapter.notifyDataSetChanged();
                }else {
                    boolean atLeastOne=false;
                    for(int i=0;i<isSelected.length;i++){
                        if(isSelected[i]){
                            atLeastOne=true;
                            break;
                        }
                    }
                    if(!atLeastOne){
                        MyToast.show(this,"请选择一个要共享的设备");
                        return;
                    }
                    tv_choose.setText("选择");
                    Intent intent=new Intent(this,DeviceShareAddActivity.class);
                    intent.putExtra(Constance.data,accountDevDTOS.get(currentP));
                    startActivity(intent);

                }
                break;
            case R.id.tv_device_my:
                position=0;
                refreshUI();
                break;
            case R.id.tv_device_share:
                position=1;
                refreshUI();
                break;
        }
    }
    private void refreshUI() {
        tv_device_my.setBackgroundResource(R.drawable.bg_corner_empty_orange_15_left);
        tv_device_share.setBackgroundResource(R.drawable.bg_corner_empty_orange_15_right);
        tv_device_my.setTextColor(getResources().getColor(R.color.theme));
        tv_device_share.setTextColor(getResources().getColor(R.color.theme));

        if(position==0){
            tv_device_my.setBackgroundResource(R.drawable.bg_corner_full_orange_15_left);
            tv_device_my.setTextColor(Color.WHITE);
        }else if(position==1){
            tv_device_share.setBackgroundResource(R.drawable.bg_corner_full_orange_15_right);
            tv_device_share.setTextColor(Color.WHITE);
        }
    }

    private ArrayList<JSONObject> parseDeviceListFromSever(JSONArray jsonArray) {
        int virturlDeviceCount = 0;
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
//        mBundle.putStringArrayList("deviceList", deviceStrList);
        return arrayList;
    }
}
