package bc.juhao.com.controller.user;

import android.os.Message;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.Map;

import bc.juhao.com.R;
import bc.juhao.com.cons.Constance;
import bc.juhao.com.cons.NetWorkConst;
import bc.juhao.com.controller.BaseController;
import bc.juhao.com.listener.INetworkCallBack;
import bc.juhao.com.ui.activity.user.UserAddrAddActivity;
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
    private UserAddrAddActivity mView;
    private EditText user_addr_editName, user_addr_editPhone, user_detail_addr;
    private TextView user_addr_txtCity, et_search;
    private CheckBox select_cb;
    private String mRegion;



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


}
