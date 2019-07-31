package bc.juhao.com.controller;

import android.content.Context;
import android.content.Intent;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;

import java.io.IOException;
import java.util.ArrayList;

import bc.juhao.com.R;
import bc.juhao.com.listener.INetworkCallBack;
import bc.juhao.com.ui.activity.BussinessApplyActivity;
import bc.juhao.com.ui.activity.MainActivity;
import bocang.json.JSONObject;
import bocang.utils.AppUtils;
import bocang.utils.MyToast;
import cn.qqtheme.framework.entity.City;
import cn.qqtheme.framework.entity.County;
import cn.qqtheme.framework.entity.Province;
import cn.qqtheme.framework.picker.AddressPicker;
import cn.qqtheme.framework.util.ConvertUtils;
import cn.qqtheme.framework.util.LogUtils;

/**
 * Created by gamekonglee on 2018/4/18.
 */

public class BussinessApplyController extends BaseController {

    private final BussinessApplyActivity mView;
    private EditText et_name;
    private EditText et_phone;
    private TextView tv_region;
    private EditText et_address;
    private EditText et_remark;
    private Button btn_apply;
    private String mRegion;
    private String json;
    private ArrayList<Province> data;
    private AddressPicker picker;

    @Override
    protected void handleMessage(int action, Object[] values) {

    }

    @Override
    protected void myHandleMessage(Message msg) {

    }
    public  BussinessApplyController(BussinessApplyActivity activity){
        mView = activity;
        initView();
        data=new ArrayList<>();
        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    json = ConvertUtils.toString(mView.getAssets().open("city.json"));
                    data.addAll(JSON.parseArray(json, Province.class));

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private void initView() {
        et_name = mView.findViewById(R.id.et_name);
        et_phone = mView.findViewById(R.id.et_phone);
        tv_region = mView.findViewById(R.id.tv_region);
        et_address = mView.findViewById(R.id.et_address);
        et_remark = mView.findViewById(R.id.et_remark);
        btn_apply = mView.findViewById(R.id.btn_apply);
        btn_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name=et_name.getText().toString();
                String phone=et_phone.getText().toString();
                String region=tv_region.getText().toString();
                String address=et_address.getText().toString();
                String remark=et_remark.getText().toString();
                if(TextUtils.isEmpty(name)||TextUtils.isEmpty(phone)||TextUtils.isEmpty(region)){
                    MyToast.show(mView,"请填写完整必要的信息");
                    return;
                }
                mNetWork.sendDealer(name,phone,region,address,remark, new INetworkCallBack() {
                    @Override
                    public void onSuccessListener(String requestCode, JSONObject ans) {
                        mView.startActivity(new Intent(mView, MainActivity.class));
                        mView.finish();
                    }

                    @Override
                    public void onFailureListener(String requestCode, JSONObject ans) {
                        MyToast.show(mView,"网络异常，请稍后再试");
                    }
                });
            }
        });
        tv_region.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm= (InputMethodManager) mView.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(et_phone.getWindowToken(),0);
                selectAddress();
            }
        });
    }

    /**
     * 选择城市
     */
    public void selectAddress() {
        try {
            data = new ArrayList<>();
            if(json==null)json = ConvertUtils.toString(mView.getAssets().open("city.json"));
            if(data.size()==0){
                data.addAll(JSON.parseArray(json, Province.class));
            }
            picker = new AddressPicker(mView, data);
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
                    tv_region.setText(address);
                    picker.dismiss();
                }
            });
            picker.show();

        } catch (Exception e) {
            MyToast.show(mView, LogUtils.toStackTraceString(e));
        }
    }
}
