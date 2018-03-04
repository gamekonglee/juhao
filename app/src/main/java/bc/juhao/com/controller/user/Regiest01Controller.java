package bc.juhao.com.controller.user;

import android.content.Intent;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import bc.juhao.com.R;
import bc.juhao.com.cons.Constance;
import bc.juhao.com.cons.NetWorkConst;
import bc.juhao.com.controller.BaseController;
import bc.juhao.com.listener.INetworkCallBack;
import bc.juhao.com.ui.activity.user.Regiest01Activity;
import bc.juhao.com.ui.activity.user.RegiestActivity;
import bc.juhao.com.utils.UIUtils;
import bocang.json.JSONArray;
import bocang.json.JSONObject;
import bocang.utils.AppDialog;
import bocang.utils.AppUtils;
import bocang.utils.MyToast;

/**
 * @author: Jun
 * @date : 2017/2/7 16:06
 * @description :注册第一步
 */
public class Regiest01Controller extends BaseController implements INetworkCallBack {
    private Regiest01Activity mView;
    private EditText invitation_code;
    private TextView business_tv, phone_tv, address_tv, invitation_code_tv;
    private LinearLayout business_ll;
    private ProgressBar pd;
    private TextView null_tv;

    public Regiest01Controller(Regiest01Activity v) {
        mView = v;
        initView();
        initViewData();
    }

    private void initViewData() {
//        PermissionUtils.requestPermission(mView, PermissionUtils.CODE_ACCESS_FINE_LOCATION, new PermissionUtils.PermissionGrant() {
//            @Override
//            public void onPermissionGranted(int requestCode) {
//                initBaiduLoc();
//            }
//        });
    }

    private void initView() {
        invitation_code = (EditText) mView.findViewById(R.id.invitation_code);
        business_tv = (TextView) mView.findViewById(R.id.business_tv);
        phone_tv = (TextView) mView.findViewById(R.id.phone_tv);
        address_tv = (TextView) mView.findViewById(R.id.address_tv);
        invitation_code_tv = (TextView) mView.findViewById(R.id.invitation_code_tv);
        business_ll = (LinearLayout) mView.findViewById(R.id.business_ll);
        pd = (ProgressBar) mView.findViewById(R.id.pd);
        pd.setVisibility(View.VISIBLE);
        null_tv = (TextView) mView.findViewById(R.id.null_tv);
    }

    @Override
    protected void handleMessage(int action, Object[] values) {

    }

    @Override
    protected void myHandleMessage(Message msg) {

    }

    /**
     * 验证邀请码
     */
    public void sendInvitationCode() {
        String code = invitation_code.getText().toString();
        if (AppUtils.isEmpty(code)) {
            AppDialog.messageBox(UIUtils.getString(R.string.isnull_invitation_code));
            return;
        }

        mView.setShowDialog(true);
        mView.setShowDialog("正在验证中..");
        mView.showLoading();
        sendUserCode(code);

    }


    /**
     * 判断邀请码
     */
    public void sendUserCode(final String code) {
        mNetWork.sendUserCode(code, new INetworkCallBack() {
            @Override
            public void onSuccessListener(String requestCode, JSONObject ans) {
                mView.hideLoading();
                switch (requestCode) {
                    case NetWorkConst.USERCODE:
                        if(AppUtils.isEmpty(ans.getJSONObject(Constance.user))){
                            MyToast.show(mView, "邀请码错误!");
                        }else{
                            Intent intent=new Intent(mView, RegiestActivity.class);
                            intent.putExtra(Constance.yaoqing, code);
                            mView.startActivity(intent);
                            mView.finish();
                        }

                        break;
                }
            }

            @Override
            public void onFailureListener(String requestCode, JSONObject ans) {
                mView.hideLoading();
                if (null == mView || mView.isFinishing())
                    return;
                if(AppUtils.isEmpty(ans)){
                    AppDialog.messageBox(UIUtils.getString(R.string.server_error));
                    return;
                }
                AppDialog.messageBox(ans.getString(Constance.error_desc));
            }
        });
    }


    /**
     * 获取最近的服务商
     */
    public void sendLatelyVerification(String lng,String lat) {
        mNetWork.sendNearbyList(lng,lat,this);
    }



    @Override
    public void onSuccessListener(String requestCode, JSONObject ans) {
        pd.setVisibility(View.INVISIBLE);
        if (null == mView || mView.isFinishing())
            return;

        JSONArray jsonArray = ans.getJSONArray(Constance.server);
        if(AppUtils.isEmpty(jsonArray)){
            null_tv.setVisibility(View.VISIBLE);
            business_ll.setVisibility(View.GONE);
            return;
        }
        JSONObject jsonObject = jsonArray.getJSONObject(0);
        String address=jsonObject.getString(Constance.address);
        String phone=jsonObject.getString(Constance.cell_phone);
        String name=jsonObject.getJSONObject(Constance.user).getString(Constance.username);
        String yaoqing=jsonObject.getString(Constance.yaoqing);
        null_tv.setVisibility(View.VISIBLE);
        if(AppUtils.isEmpty(address)){
            null_tv.setVisibility(View.VISIBLE);
            business_ll.setVisibility(View.GONE);
            return;
        }else{
            business_ll.setVisibility(View.VISIBLE);
        }
        null_tv.setVisibility(View.GONE);
        business_tv.setText(name);
        phone_tv.setText(phone);
        address_tv.setText(address);
        invitation_code_tv.setText(yaoqing);

    }

    @Override
    public void onFailureListener(String requestCode, JSONObject ans) {
        if (null == mView || mView.isFinishing())
            return;
        if (AppUtils.isEmpty(ans)) {
            AppDialog.messageBox(UIUtils.getString(R.string.server_error));
            return;
        }
        AppDialog.messageBox(ans.getString(Constance.error_desc));
    }


//    /**
//     * 初始化地图数据
//     */
//    public void initBaiduLoc() {
//        // 定位初始化
//        mView.mLocationClient = new LocationClient(mView.getApplicationContext());     //声明Location
//        mView.mOption = new LocationClientOption();
//        mView.mOption.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
//        mView.mOption.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系，如果配合百度地图使用，建议设置为bd09ll;
//        mView.mOption.setScanSpan(3000);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
//        mView.mOption.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
//        mView.mOption.setIsNeedLocationDescribe(true);//可选，设置是否需要地址描述
//        mView.mOption.setNeedDeviceDirect(false);//可选，设置是否需要设备方向结果
//        mView.mOption.setLocationNotify(false);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
//        mView.mOption.setIgnoreKillProcess(true);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
//        mView.mOption.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
//        mView.mOption.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
//        mView.mOption.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
//        mView.mLocationClient.setLocOption(mView.mOption);
//
//        mView.mLocationClient.registerLocationListener(new BDLocationListener() {
//            @Override
//            public void onReceiveLocation(BDLocation location) {
//                //开始定位后，定位信息回调
//                if (location == null) {
//                    return;
//                }
////                //Receive Location
////                StringBuffer sb = new StringBuffer(256);
////                sb.append("time : ");
////                sb.append(location.getTime());
////                sb.append("\nerror code : ");
////                sb.append(location.getLocType());
////                sb.append("\nlatitude : ");
////                sb.append(location.getLatitude());
////                sb.append("\nlontitude : ");
////                sb.append(location.getLongitude());
////                sb.append("\nradius : ");
////                sb.append(location.getRadius());
////                if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
////                    sb.append("\nspeed : ");
////                    sb.append(location.getSpeed());// 单位：公里每小时
////                    sb.append("\nsatellite : ");
////                    sb.append(location.getSatelliteNumber());
////                    sb.append("\nheight : ");
////                    sb.append(location.getAltitude());// 单位：米
////                    sb.append("\ndirection : ");
////                    sb.append(location.getDirection());// 单位度
////                    sb.append("\naddr : ");
////                    sb.append(location.getAddrStr());
////                    sb.append("\ndescribe : ");
////                    sb.append("gps定位成功");
////
////                } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
////                    sb.append("\naddr : ");
////                    sb.append(location.getAddrStr());
////                    //运营商信息
////                    sb.append("\noperationers : ");
////                    sb.append(location.getOperators());
////                    sb.append("\ndescribe : ");
////                    sb.append("网络定位成功");
////                } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
////                    sb.append("\ndescribe : ");
////                    sb.append("离线定位成功，离线定位结果也是有效的");
////                } else if (location.getLocType() == BDLocation.TypeServerError) {
////                    sb.append("\ndescribe : ");
////                    sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
////                } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
////                    sb.append("\ndescribe : ");
////                    sb.append("网络不同导致定位失败，请检查网络是否通畅");
////                } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
////                    sb.append("\ndescribe : ");
////                    sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
////                }
////                sb.append("\nlocationdescribe : ");
////                sb.append(location.getLocationDescribe());// 位置语义化信息
////                List<Poi> list = location.getPoiList();// POI数据
////                if (list != null) {
////                    sb.append("\npoilist size = : ");
////                    sb.append(list.size());
////                    for (Poi p : list) {
////                        sb.append("\npoi= : ");
////                        sb.append(p.getId() + " " + p.getName() + " " + p.getRank());
////                    }
////                }
////                Toast.makeText(mView, "BaiduLocationApiDem:" + sb.toString(), Toast.LENGTH_LONG).show();
////                sendLatelyVerification("113.11","23.05");
//
//
//                sendLatelyVerification(location.getLongitude()+"",location.getLatitude()+"");
//
//                mView.mLocationClient.stop();
//            }
//
//            @Override
//            public void onConnectHotSpotMessage(String s, int i) {
//
//            }
//        });
//        mView.mLocationClient.start();// 点击开始定位
//    }

}
