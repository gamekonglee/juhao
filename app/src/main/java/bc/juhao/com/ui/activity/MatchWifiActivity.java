package bc.juhao.com.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.aliyun.iot.ilop.demo.DemoApplication;

import java.net.InetAddress;

import bc.juhao.com.R;
import bc.juhao.com.common.BaseActivity;

/**
 * Created by gamekonglee on 2018/7/27.
 */

public class MatchWifiActivity extends BaseActivity {


    private TextView tv_wifi_name;
    private TextView tv_wifi_change;
    private EditText et_wifi_pwd;
    private ImageView iv_cansee;
    private Button btn_next;
    private boolean isCanSee;

    @Override
    protected void InitDataView() {

    }

    @Override
    protected void initController() {

    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_wifi_match);

        tv_wifi_name = findViewById(R.id.tv_wifi_name);
        tv_wifi_change = findViewById(R.id.tv_change_wifi);
        et_wifi_pwd = findViewById(R.id.et_wifi_pwd);
        iv_cansee = findViewById(R.id.iv_wifi_cansee);
        btn_next = findViewById(R.id.btn_next);
        isCanSee = false;
        iv_cansee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isCanSee){
                    isCanSee=false;
                    iv_cansee.setImageResource(R.mipmap.wifi_g);
                    et_wifi_pwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }else {
                    isCanSee=true;
                    iv_cansee.setImageResource(R.mipmap.wifi_k);
                    et_wifi_pwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
            }
        });

        WifiManager wifiMgr = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        int wifiState = wifiMgr.getWifiState();
        if(wifiState==WifiManager.WIFI_STATE_ENABLED){
            WifiInfo info = wifiMgr.getConnectionInfo();
        String wifiId = info != null ? info.getSSID() : null;
//        Log.e("wifiid,",wifiId);
        tv_wifi_name.setText(""+wifiId);
        }

//        InetAddress address = getWifiIp();
//        address.getHostAddress();
    }
//    public static InetAddress getWifiIp() {
//        Context myContext = Globals.getContext();
//        if (myContext == null) {
//            throw new NullPointerException("Global context is null");
//        }
//        WifiManager wifiMgr = (WifiManager) myContext.getSystemService(Context.WIFI_SERVICE);
//        if (isWifiEnabled()) {
//            int ipAsInt = wifiMgr.getConnectionInfo().getIpAddress();
//            if (ipAsInt == 0) {
//                return null;
//            } else {
//                return Util.intToInet(ipAsInt);
//            }
//        } else {
//            return null;
//        }
//    }



    public static boolean isWifiEnabled() {
        Context myContext = DemoApplication.getInstance();
        if (myContext == null) {
            throw new NullPointerException("Global context is null");
        }
        WifiManager wifiMgr = (WifiManager) myContext.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (wifiMgr.getWifiState() == WifiManager.WIFI_STATE_ENABLED) {
            ConnectivityManager connManager = (ConnectivityManager) myContext
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo wifiInfo = connManager
                    .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            return wifiInfo.isConnected();
        } else {
            return false;
        }
    }
    private String getConnectWifiSsid(){
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        Log.d("wifiInfo", wifiInfo.toString());
        Log.d("SSID",wifiInfo.getSSID());
        return wifiInfo.getSSID();
    }
    @Override
    protected void initData() {

    }
}
