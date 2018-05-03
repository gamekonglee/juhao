package bc.juhao.com.controller.user;

import android.Manifest;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.google.android.gms.common.GooglePlayServicesClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import bc.juhao.com.R;
import bc.juhao.com.adapter.BaseAdapterHelper;
import bc.juhao.com.adapter.QuickAdapter;
import bc.juhao.com.bean.ContactInfo;
import bc.juhao.com.cons.Constance;
import bc.juhao.com.cons.NetWorkConst;
import bc.juhao.com.controller.BaseController;
import bc.juhao.com.listener.INetworkCallBack;
import bc.juhao.com.ui.activity.IssueApplication;
import bc.juhao.com.ui.activity.user.UserAddrAddActivity;
import bc.juhao.com.utils.ContactUtils;
import bc.juhao.com.utils.UIUtils;
import bocang.json.JSONArray;
import bocang.json.JSONObject;
import bocang.utils.AppDialog;
import bocang.utils.AppUtils;
import bocang.utils.CommonUtil;
import bocang.utils.MyToast;
import cn.qqtheme.framework.entity.City;
import cn.qqtheme.framework.entity.County;
import cn.qqtheme.framework.entity.Province;
import cn.qqtheme.framework.picker.AddressPicker;
import cn.qqtheme.framework.util.ConvertUtils;
import cn.qqtheme.framework.util.LogUtils;

/**
 * @author: Jun
 * @date : 2017/2/22 14:43
 * @description :
 */
public class UserAddrAddController extends BaseController implements INetworkCallBack {
    private static final String[] PERMISSIONS_LOCATION = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_PHONE_STATE} ;
    public static final int REQUEST_LOCATION = 1000;
    private UserAddrAddActivity mView;
    private EditText user_addr_editName, user_addr_editPhone, user_detail_addr;
    private TextView user_addr_txtCity, et_search;
    private CheckBox select_cb;
    private String mRegion;
    private LinearLayout ll_choose_connect;
    private TextView tv_location;
    private List<ContactInfo> contacts;
    private LocationClient mLocationClient;


    public UserAddrAddController(UserAddrAddActivity v) {
        mView = v;
        initView();
        initViewData();
    }

    private void initView() {
        user_addr_editName = (EditText) mView.findViewById(R.id.user_addr_editName);
        user_addr_editPhone = (EditText) mView.findViewById(R.id.user_addr_editPhone);
        user_addr_txtCity = (TextView) mView.findViewById(R.id.user_addr_txtCity);
        user_detail_addr = (EditText) mView.findViewById(R.id.user_detail_addr);
        et_search = (TextView) mView.findViewById(R.id.et_search);
        select_cb = (CheckBox) mView.findViewById(R.id.select_cb);
        ll_choose_connect = mView.findViewById(R.id.ll_choose_connect);
        tv_location = mView.findViewById(R.id.tv_location);
        ll_choose_connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Build.VERSION.SDK_INT>=23){
                    if(ContextCompat.checkSelfPermission(mView, Manifest.permission.READ_CONTACTS)!= PackageManager.PERMISSION_GRANTED){
                        ActivityCompat.requestPermissions(mView,new String[]{Manifest.permission.READ_CONTACTS},200);
                    }else {
                        getContact();
                    }
                }else {
                    getContact();
                }
            }
        });
        tv_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Build.VERSION.SDK_INT>=23){
                    if (ActivityCompat.checkSelfPermission(mView, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                            || ActivityCompat.checkSelfPermission(mView, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                            || ActivityCompat.checkSelfPermission(mView, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                            || ActivityCompat.checkSelfPermission(mView, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                        if (ActivityCompat.shouldShowRequestPermissionRationale(mView, Manifest.permission.ACCESS_COARSE_LOCATION) || ActivityCompat.shouldShowRequestPermissionRationale(mView, Manifest.permission.ACCESS_FINE_LOCATION) || ActivityCompat.shouldShowRequestPermissionRationale(mView, Manifest.permission.WRITE_EXTERNAL_STORAGE) || ActivityCompat.shouldShowRequestPermissionRationale(mView, Manifest.permission.READ_PHONE_STATE))
                        {
                            ActivityCompat.requestPermissions(mView, PERMISSIONS_LOCATION, REQUEST_LOCATION);
                        }else
                        {
                            ActivityCompat.requestPermissions(mView, PERMISSIONS_LOCATION, REQUEST_LOCATION);
                        }
                    }else {
                        startLocation();
                    }
                    }else
                    {
                        startLocation();
                    }



            }
        });
    }

    public void startLocation() {
        LocationClientOption option = new LocationClientOption();

        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
//可选，设置定位模式，默认高精度
//LocationMode.Hight_Accuracy：高精度；
//LocationMode. Battery_Saving：低功耗；
//LocationMode. Device_Sensors：仅使用设备；

        option.setCoorType("bd09ll");
//可选，设置返回经纬度坐标类型，默认gcj02
//gcj02：国测局坐标；
//bd09ll：百度经纬度坐标；
//bd09：百度墨卡托坐标；
//海外地区定位，无需设置坐标类型，统一返回wgs84类型坐标

        option.setScanSpan(1000);
//可选，设置发起定位请求的间隔，int类型，单位ms
//如果设置为0，则代表单次定位，即仅定位一次，默认为0
//如果设置非0，需设置1000ms以上才有效

        option.setOpenGps(true);
//可选，设置是否使用gps，默认false
//使用高精度和仅用设备两种定位模式的，参数必须设置为true

        option.setLocationNotify(true);
//可选，设置是否当GPS有效时按照1S/1次频率输出GPS结果，默认false

        option.setIgnoreKillProcess(false);
//可选，定位SDK内部是一个service，并放到了独立进程。
//设置是否在stop的时候杀死这个进程，默认（建议）不杀死，即setIgnoreKillProcess(true)

        option.SetIgnoreCacheException(false);
//可选，设置是否收集Crash信息，默认收集，即参数为false

        option.setWifiCacheTimeOut(5*60*1000);
//可选，7.2版本新增能力
//如果设置了该接口，首次启动定位时，会先判断当前WiFi是否超出有效期，若超出有效期，会先重新扫描WiFi，然后定位

        option.setEnableSimulateGps(false);
//可选，设置是否需要过滤GPS仿真结果，默认需要，即参数为false
        option.setIsNeedAddress(true);
        option.setOpenGps(true); // 打开gps
        option.setScanSpan(1000);

        mLocationClient = new LocationClient(IssueApplication.getcontext());
        //声明LocationClient类
        mLocationClient.registerLocationListener(new BDAbstractLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation location) {
                String country = location.getCountry();    //获取国家
                String province = location.getProvince();    //获取省份
                String city = location.getCity();    //获取城市
                String district = location.getDistrict();    //获取区县
                String street = location.getStreet();    //获取街道信息
                province=province.substring(0,province.length()-1);
                city=city.substring(0,city.length()-1);
                district=district.substring(0,district.length()-1);
                Log.e("province",province);
                Log.e("city",city);
                Log.e("district",district);
                ArrayList<Province> data = new ArrayList<>();
                String json = null;
                try {
                    json = ConvertUtils.toString(mView.getAssets().open("city.json"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                data.addAll(JSON.parseArray(json, Province.class));
                for(Province provinceTemp :data){
                    if(provinceTemp.getAreaName().contains(province)){
                        for(City cityTemp:provinceTemp.getCities()){
                            if(cityTemp.getAreaName().contains(city)){
                                for(County countyTemp:cityTemp.getCounties()){
                                    if(countyTemp.getAreaName().contains(district)){
                                        if(TextUtils.isEmpty(countyTemp.getCityId())){
                                            mRegion=cityTemp.getAreaId();
                                        }else {
                                            mRegion=countyTemp.getAreaId();
                                        }
                                        user_addr_txtCity.setText(province+" "+city+" "+district+"区");
                                        Log.e("mRegion",""+mRegion);
                                        break;
                                    }
                                }
                            }
                        }
                    }

                }
            }
        });
        mLocationClient.setLocOption(option);
        mLocationClient.start();
        //注册监听函数
    }

    private void getContact() {
        contacts = ContactUtils.getContactsInfos(mView);
        final Dialog dialog=new Dialog(mView,R.style.customDialog);
        dialog.setContentView(R.layout.dialog_contact);
        ListView lv_contact=dialog.findViewById(R.id.lv_contact);
        QuickAdapter<ContactInfo > adapter=new QuickAdapter<ContactInfo>(mView,R.layout.item_contact) {
            @Override
            protected void convert(BaseAdapterHelper helper, ContactInfo item) {
                  helper.setText(R.id.tv_name,item.name);
                  helper.setText(R.id.tv_tel,item.phone);
            }
        };
        lv_contact.setAdapter(adapter);
        adapter.replaceAll(contacts);
        adapter.notifyDataSetChanged();
        lv_contact.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                user_addr_editName.setText(contacts.get(i).name);
                user_addr_editPhone.setText(""+contacts.get(i).phone);
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    private void initViewData() {
        if (mView.mUpdateModele == true) {
            et_search.setText(UIUtils.getString(R.string.update_address));
            String name = mView.addressObject.getString(Constance.name);
            String address = mView.addressObject.getString(Constance.address);
            String mobile = mView.addressObject.getString(Constance.mobile);
            Boolean is_default = mView.addressObject.getBoolean(Constance.is_default);
            JSONArray regionsArray = mView.addressObject.getJSONArray(Constance.regions);
            String regions = "";
            for (int i = 0; i < regionsArray.length(); i++) {
                if (i == 0)
                    continue;
                regions += regionsArray.getJSONObject(i).getString(Constance.name) + " ";
            }
            user_addr_editName.setText(name);
            user_addr_txtCity.setText(regions);
            user_detail_addr.setText(address);
            user_addr_editPhone.setText(mobile);
            select_cb.setChecked(is_default);
             JSONArray rregionsList= mView.addressObject.getJSONArray(Constance.regions);
            mRegion=rregionsList.getJSONObject(rregionsList.length()-1).getString(Constance.id);

        } else {
            et_search.setText(UIUtils.getString(R.string.add_address));
        }


//        mNetWork.sendAddressList1(this);
    }

    @Override
    protected void handleMessage(int action, Object[] values) {

    }

    @Override
    protected void myHandleMessage(Message msg) {

    }

    public void sendAddAddress() {
        String name = user_addr_editName.getText().toString();
        String mobile = user_addr_editPhone.getText().toString();
        String tel = "";
        String zip_code = "";
        String region = user_addr_txtCity.getText().toString();
        String address = user_detail_addr.getText().toString();
        String identity = "";
        if (AppUtils.isEmpty(name)) {
            MyToast.show(mView, "收货人不能为空!");
            return;
        }
        if (AppUtils.isEmpty(mobile)) {
            MyToast.show(mView, "电话不能为空!");
            return;
        }
        // 做个正则验证手机号
        if (!CommonUtil.isMobileNO(mobile)) {
            AppDialog.messageBox(UIUtils.getString(R.string.mobile_assert));
            return;
        }

        if (AppUtils.isEmpty(region)) {
            MyToast.show(mView, "所在地区不能为空!");
            return;
        }
        if (AppUtils.isEmpty(address)) {
            MyToast.show(mView, "详细地址不能为空!");
            return;
        }

        mView.setShowDialog(true);
        mView.setShowDialog("正在保存中..");
        mView.showLoading();
        if (mView.mUpdateModele) {
            String id = mView.addressObject.getString(Constance.id);
            mNetWork.sendUpdateAddress(id,name, mobile, tel, zip_code, mRegion, address, identity, this);
        } else {
            mNetWork.sendAddAddress(name, mobile, tel, zip_code, mRegion, address, identity, this);
        }
    }


    private void sendDefaultAddress(String id){

        if (AppUtils.isEmpty(id))
            return;
        mNetWork.sendDefaultAddress(id,this);
    }

    @Override
    public void onSuccessListener(String requestCode, JSONObject ans) {

        switch (requestCode) {
            case NetWorkConst.CONSIGNEEADD:
                if(select_cb.isChecked()){
                    String id = ans.getJSONObject(Constance.consignee).getString(Constance.id);
                    sendDefaultAddress(id);
                }else{
                    MyToast.show(mView, "保存成功!");
                    mView.finish();
                }
                break;
            case NetWorkConst.CONSIGNEEUPDATE:
                if(select_cb.isChecked()){
                    String id = ans.getJSONObject(Constance.consignee).getString(Constance.id);
                    sendDefaultAddress(id);
                }else{
                    MyToast.show(mView, "保存成功!");
                    mView.finish();
                }
                break;
            case NetWorkConst.CONSIGNEEDELETE:
                mView.hideLoading();
                MyToast.show(mView, "删除成功!");
                mView.finish();
                break;
            case NetWorkConst.CONSIGNEEDEFAULT:
                mView.hideLoading();
                MyToast.show(mView, "保存成功!");
                mView.finish();
                break;
            case NetWorkConst.ADDRESSlIST:
                JSONArray array = ans.getJSONArray(Constance.regions);
                JSONObject jsonObject = array.getJSONObject(0);
                Map<String, Object> all = jsonObject.getAll();
                break;
        }
    }

    @Override
    public void onFailureListener(String requestCode, JSONObject ans) {
        mView.hideLoading();
        if (null == mView || mView.isFinishing())
            return;
        if (AppUtils.isEmpty(ans)) {
            AppDialog.messageBox(UIUtils.getString(R.string.server_error));
            return;
        }
        AppDialog.messageBox(ans.getString(Constance.error_desc));
    }

    /**
     * 删除地址
     */
    public void deleteAddress() {
        String id = mView.addressObject.getString(Constance.id);
        if (AppUtils.isEmpty(id))
            return;
        mView.setShowDialog(true);
        mView.setShowDialog("正在删除中..");
        mView.showLoading();
        mNetWork.sendDeleteAddress(id, this);
    }

    /**
     * 选择城市
     */
    public void selectAddress() {
        try {
            ArrayList<Province> data = new ArrayList<>();
            String json = ConvertUtils.toString(mView.getAssets().open("city.json"));
            data.addAll(JSON.parseArray(json, Province.class));
            final AddressPicker picker = new AddressPicker(mView, data);
            picker.setHideProvince(false);
            picker.setSelectedItem("广东", "佛山", "禅城");
            picker.setOnAddressPickListener(new AddressPicker.OnAddressPickListener() {
                @Override
                public void onAddressPicked(Province province, City city, County county) {
                    String address = province.getAreaName() + " " + city.getAreaName() + " " + county.getAreaName();

                    if (AppUtils.isEmpty(county.getCityId())) {
                        mRegion = city.getAreaId();


                    } else {
                        mRegion = county.getAreaId();
                    }
                    user_addr_txtCity.setText(address);
                    picker.dismiss();
                }
            });
            picker.show();

        } catch (Exception e) {
            MyToast.show(mView, LogUtils.toStackTraceString(e));
        }
    }


    public void onRequestPermissionRsult() {
        getContact();
    }
}
