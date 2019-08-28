package bc.juhao.com.ui.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.aliyun.iot.aep.component.router.Router;
import com.aliyun.iot.aep.sdk.apiclient.IoTAPIClient;
import com.aliyun.iot.aep.sdk.apiclient.IoTAPIClientFactory;
import com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback;
import com.aliyun.iot.aep.sdk.apiclient.callback.IoTResponse;
import com.aliyun.iot.aep.sdk.apiclient.request.IoTRequest;
import com.aliyun.iot.aep.sdk.apiclient.request.IoTRequestBuilder;
import com.aliyun.iot.aep.sdk.log.ALog;
import com.aliyun.iot.ilop.demo.page.ilopmain.AddDeviceActivity;
import com.google.gson.Gson;

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
import bc.juhao.com.bean.Scenes;
import bc.juhao.com.bean.ScenesBean;
import bc.juhao.com.common.BaseFragment;
import bc.juhao.com.cons.Constance;
import bc.juhao.com.ui.view.EndOfListView;
import bc.juhao.com.ui.view.PMSwipeRefreshLayout;
import bocang.utils.LogUtils;
import bocang.utils.MyToast;

import static com.alibaba.mtl.appmonitor.AppMonitor.TAG;

/**
 * Created by gamekonglee on 2018/7/7.
 */

public class ItApplicationFragment extends BaseFragment implements EndOfListView.OnEndOfListListener {
    private int currentP = 0;
    private TextView btn_add_device;
    private View iv_add;
    Bundle mBundle = new Bundle();
    private List<Scenes> scenes = new ArrayList<>();
    private QuickAdapter<Scenes> adapter;
    private int page = 1;
    private PMSwipeRefreshLayout pullToRefresh;
    private boolean isBottom;
    private QuickAdapter<Scenes> adapterB;
    private View rl_add;
    private View ll_none_device;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_it_main_app, null);
    }

    @Override
    protected void initController() {

    }

    @Override
    protected void initViewData() {

    }

    @Override
    protected void initView() {
        TextView tv_scene = getView().findViewById(R.id.tv_scene);
        final TextView tv_auto = getView().findViewById(R.id.tv_auto);
        final View view_scene = getView().findViewById(R.id.view_scene);
        final View view_auto = getView().findViewById(R.id.view_auto);
        btn_add_device = getView().findViewById(R.id.btn_add_device);
        iv_add = getView().findViewById(R.id.iv_add);
        pullToRefresh = getView().findViewById(R.id.pullToRefresh);
        rl_add = getView().findViewById(R.id.rl_add);
        ll_none_device = getView().findViewById(R.id.ll_none_device);
        pullToRefresh.setColorSchemeColors(Color.BLUE, Color.GREEN, Color.RED);
        pullToRefresh.setRefreshing(false);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                isBottom = false;
                page = 1;
                scenes = new ArrayList<>();
                getSceneList();
            }
        });
        final EndOfListView lv_scenes = getView().findViewById(R.id.lv_scenes);
        lv_scenes.setOnEndOfListListener(this);
        rl_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(getActivity(), IotSceneAddActivity.class);
//                startActivity(intent);
                String code = "link://router/scene";
                Bundle bundle = new Bundle();
                bundle.putString("sceneType", "ilop"); // 传入插件参数，没有参数则不需要这一行
                Router.getInstance().toUrlForResult(getActivity(), code, 1, bundle);

            }
        });
        adapter = new QuickAdapter<Scenes>(getActivity(), R.layout.item_scene) {
            @Override
            protected void convert(BaseAdapterHelper helper, final Scenes item) {
                helper.setText(R.id.tv_name, item.getName());
                helper.setOnClickListener(R.id.tv_doit, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Map<String, Object> maps = new HashMap<>();
                        maps.put("sceneId", item.getId());
                        IoTRequestBuilder builder = new IoTRequestBuilder()
                                .setPath("/scene/fire")
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

                                final int code = ioTResponse.getCode();

                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (code != 200) {
                                            Toast.makeText(getActivity(), "执行失败", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(getActivity(), "执行成功", Toast.LENGTH_SHORT).show();
                                            page = 1;
                                            scenes = new ArrayList<>();
                                            getSceneList();
                                        }
                                    }
                                });
                            }
                        });
                    }
                });
                helper.setOnClickListener(R.id.rl_setting, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String code = "link://router/scene";
                        Bundle bundle = new Bundle();
                        bundle.putString("sceneType", "ilop"); // 传入插件参数，没有参数则不需要这一行
                        bundle.putString("sceneId", item.getId());
                        Router.getInstance().toUrlForResult(getActivity(), code, 1, bundle);
                    }
                });
            }
        };
        adapterB = new QuickAdapter<Scenes>(getActivity(), R.layout.item_scene_auto) {
            @Override
            protected void convert(BaseAdapterHelper helper, final Scenes item) {
                helper.setText(R.id.tv_name, item.getName());
                if (item.getEnable()) {
                    helper.setBackgroundRes(R.id.tv_doit, R.mipmap.icon_opon);
                } else {
                    helper.setBackgroundRes(R.id.tv_doit, R.mipmap.icon_close);
                }
                helper.setOnClickListener(R.id.tv_doit, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Map<String, Object> maps = new HashMap<>();
                        maps.put("sceneId", item.getId());
                        IoTRequestBuilder builder = new IoTRequestBuilder()
                                .setPath("/scene/info/get")
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
                                if (ioTResponse.getData() != null) {
                                    LogUtils.logE("scenesDetail", ioTResponse.getData().toString());
                                    try {
                                        JSONObject jsonObject = new JSONObject(ioTResponse.getData().toString());
                                        JSONArray actions = jsonObject.getJSONArray(Constance.actions);
                                        Map<String, Object> maps = new HashMap<>();
                                        boolean ifOpen = !item.getEnable();
                                        maps.put("sceneId", item.getId());
                                        maps.put("enable", ifOpen);
                                        maps.put("name", item.getName());
                                        maps.put("icon", item.getIcon());
                                        maps.put("description", item.getDescription());
                                        maps.put("actions", actions.toString());

                                        IoTRequestBuilder builder2 = new IoTRequestBuilder()
                                                .setPath("/scene/update")
                                                .setApiVersion("1.0.2")
                                                .setAuthType("iotAuth")
                                                .setParams(maps);

                                        IoTRequest request2 = builder2.build();

                                        IoTAPIClient ioTAPIClient2 = new IoTAPIClientFactory().getClient();
                                        ioTAPIClient2.send(request2, new IoTCallback() {

                                            @Override
                                            public void onFailure(IoTRequest ioTRequest, Exception e) {

                                            }

                                            @Override
                                            public void onResponse(IoTRequest ioTRequest, final IoTResponse ioTResponse) {

                                                getActivity().runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        if (ioTResponse.getCode() != 200) {
                                                            MyToast.show(getActivity(), "设置失败");
                                                            return;
                                                        } else {
                                                            MyToast.show(getActivity(), "设置成功");
                                                        }
                                                        page = 1;
                                                        scenes = new ArrayList<>();
                                                        getSceneList();
                                                    }
                                                });
                                                return;

                                            }
                                        });
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                            }
                        });
//
//
//


                    }
                });

            }
        };
        lv_scenes.setAdapter(adapter);
        tv_scene.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (currentP == 0) {
                    return;
                }
                currentP = 0;
                view_scene.setVisibility(View.VISIBLE);
                view_auto.setVisibility(View.INVISIBLE);
                lv_scenes.setAdapter(adapter);
                adapter.replaceAll(scenes);

            }
        });
        tv_auto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentP == 1) {
                    return;
                }
                currentP = 1;
                view_scene.setVisibility(View.INVISIBLE);
                view_auto.setVisibility(View.VISIBLE);
                lv_scenes.setAdapter(adapterB);
                adapterB.replaceAll(scenes);

            }
        });
        btn_add_device.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent=new Intent(getActivity(), IotSceneAddActivity.class);
//                startActivity(intent);
                String code = "link://router/scene";
                Bundle bundle = new Bundle();
                bundle.putString("sceneType", "ilop"); // 传入插件参数，没有参数则不需要这一行
                Router.getInstance().toUrlForResult(getActivity(), code, 1, bundle);


            }
        });
//        iv_add.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////            addDevice();
//
////                String code = "link://router/scene";
////                Bundle bundle = new Bundle();
////                bundle.putString("sceneType","ilop"); // 传入插件参数，没有参数则不需要这一行
////                Router.getInstance().toUrlForResult(getActivity(), code, 1, bundle);
//
//                Intent intent=new Intent(getActivity(), IotSceneAddActivity.class);
//                startActivity(intent);
//
//            }
//        });

    }

    @Override
    public void onResume() {
        super.onResume();
        getSceneList();
    }

    private void getSceneList() {
        Map<String, Object> maps = new HashMap<>();
        maps.put("pageNo", page);
        maps.put("pageSize", "10");
        IoTRequestBuilder builder = new IoTRequestBuilder()
                .setPath("/scene/list/get")
                .setApiVersion("1.0.2")
                .setAuthType("iotAuth")
                .setParams(maps);

        IoTRequest request = builder.build();

        IoTAPIClient ioTAPIClient = new IoTAPIClientFactory().getClient();
        ioTAPIClient.send(request, new IoTCallback() {
            @Override
            public void onFailure(IoTRequest ioTRequest, Exception e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pullToRefresh.setRefreshing(false);
                    }
                });
            }

            @Override
            public void onResponse(IoTRequest ioTRequest, IoTResponse response) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pullToRefresh.setRefreshing(false);
                        ll_none_device.setVisibility(View.GONE);
                        pullToRefresh.setVisibility(View.VISIBLE);
                    }
                });
                final int code = response.getCode();
                final String msg = response.getMessage();
                if (code != 200) {
                    isBottom = true;
                    return;
                }

                Object data = response.getData();
                if (data != null) {
                    ScenesBean scenesBean = new Gson().fromJson(data.toString(), ScenesBean.class);
                    List<Scenes> temp = scenesBean.getScenes();
                    if (temp == null || temp.size() == 0) {
                        isBottom = true;
                        if(scenesBean.getPageNo()==1){
                          getActivity().runOnUiThread(new Runnable() {
                              @Override
                              public void run() {
                                  ll_none_device.setVisibility(View.VISIBLE);
                                  pullToRefresh.setVisibility(View.GONE);
                              }
                          });
                        }
                        return;
                    }
                    if (scenesBean.getPageNo() == 1) {
                        scenes = temp;
                    } else {
                        scenes.addAll(temp);
                    }
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.replaceAll(scenes);
                        }
                    });

//                scenesBean.getScenes().get(0)
                } else {
                    isBottom = true;
                }
//                LogUtils.logE("sceneList",""+data.toString());
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    private void addDevice() {
        final List<AccountDevDTO> accountDevDTOS = new ArrayList<>();
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
                ALog.d(TAG, "onResponse listByAccount");
//                mHandler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        if(temp!=null){
//                            temp.post(new Runnable() {
//                                @Override
//                                public void run() {
//                                    temp.setRefreshing(false);
//                                }
//                            });
//                        }
//                    }
//                });
                final int code = response.getCode();
                final String msg = response.getMessage();
                if (code != 200) {
                    return;
                }

                Object data = response.getData();
                if (null != data) {
                    if (data instanceof JSONArray) {
                        List<JSONObject> mDeviceList = parseDeviceListFromSever((JSONArray) data);
                        Intent intent = new Intent(getActivity(), AddDeviceActivity.class);
                        intent.putExtra("bundle", mBundle);
                        startActivity(intent);
//                        if(mDeviceList==null||mDeviceList.size()==0){
////                            mHandler.sendEmptyMessage(1);
//                            return;
//                        }
//                        LogUtils.logE("mDevices",mDeviceList.toString());
//                        for(int i=0;i<mDeviceList.size();i++){
//                            try {
//                                if(!mDeviceList.get(i).getString(Constance.type).equals("虚拟")){
//                                    accountDevDTOS.add(new Gson().fromJson(mDeviceList.get(i).toString(), AccountDevDTO.class));
//                                }
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                        for(int i=0;i<accountDevDTOS.size();i++){
//                            for(int j=0;j<accountDevDTOS.size();j++){
//                                if(i!=j&&accountDevDTOS.get(i).getIotId().equals(accountDevDTOS.get(j).getIotId())){
//                                    accountDevDTOS.remove(j);
//                                    if(j!=0)j--;
//                                }
//                            }
//                        }
//
//                        mHandler.sendEmptyMessage(0);
//                        String[] pks = {"a1IjeL0MqPS", "a1AzoSi5TMc", "a1nZ7Kq7AG1", "a1XoFUJWkPr"};
//                        if (mDeviceList.size() == 0 || virturlDeviceCount < pks.length - 1){
//                            if (mRegisterCount > 0){
//                                return;
//                            }
//                            //注册虚拟设备
//
////                            for (String pk : pks) {
//////                                registerVirtualDevice(pk);
////                            }
//                        }else {
//                            mHandler.post(new Runnable() {
//                                @Override
//                                public void run() {
//                                    initDevicePanel();
//                                }
//                            });
//                        }
                    }
                }

            }
        });
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
                if ("VIRTUAL".equalsIgnoreCase(jsonObject.getString("thingType"))) {
                    type = "虚拟";
                    virturlDeviceCount++;
                } else {
                    type = jsonObject.getString("netType");
                }
                device.put("type", type);
                String statusStr = "离线";
                if (1 == jsonObject.getInt("status")) {
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
    protected void initData() {

    }


    @Override
    public void onEndOfList(Object lastItem) {
        if (page == 1 && scenes.size() == 0 || isBottom) {
            return;
        }
        page++;
        getSceneList();
    }
}
