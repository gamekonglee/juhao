package bc.juhao.com.net;

import android.os.Environment;
import android.os.SystemClock;
import android.widget.Toast;


import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;
import com.zhy.http.okhttp.callback.FileCallBack;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import bc.juhao.com.cons.Constance;
import bc.juhao.com.cons.NetWorkConst;
import bc.juhao.com.ui.activity.IssueApplication;
import bc.juhao.com.utils.MyShare;
import bc.juhao.com.utils.NetworkStateManager;
import bc.juhao.com.utils.UIUtils;
import okhttp3.Call;
import okhttp3.Response;


/**
 * Created by bocang on 18-2-2.
 */

public class ApiClient {
    /**
     * 检查网络是否联网
     */
    public static boolean hashkNewwork() {

        boolean hasNetwork = NetworkStateManager.instance().isNetworkConnected();
        if (!hasNetwork) {
            Toast.makeText(IssueApplication.getInstance(), "您的网络连接已中断", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    public static void sendPayment(String order, final Callback callback) {
        if(!hashkNewwork()){
            return;
        }

//        responseListener.onStarted();
        Map<String ,String> map=new HashMap<>();
        map.put("order",order);
        String url= NetWorkConst.APK_URL;
        String token= MyShare.get(UIUtils.getContext()).getString(Constance.TOKEN);

        OkHttpUtils.post()
                .url(url)
                .addHeader("X-bocang-Authorization",token)
                .addParams("order", order)
                .build()
                .execute(new Callback() {
                    @Override
                    public Object parseNetworkResponse(Response response, int i1) throws Exception {
                            String jsonRes=response.body().string();
//                        callback.onResponse(advertBeanList,i1);
                        return jsonRes;
                    }

                    @Override
                    public void onError(Call call, Exception e, int i) {
                        callback.onError(call,e,i);
                    }

                    @Override
                    public String onResponse(Object o, int i) {
                        callback.onResponse(o,i);

                        return null;
                    }
                });
        //        requestQueue.add(simplePostRequest);

    }
    public static void sendWxPayment(String order, final Callback<String> callback) {
        if(!hashkNewwork()){
            return;
        }

//        responseListener.onStarted();
        Map<String ,String> map=new HashMap<>();
        map.put("order",order);
        String url= NetWorkConst.ALIPAY_URL;
        String token= MyShare.get(UIUtils.getContext()).getString(Constance.TOKEN);

        OkHttpUtils.post()
                .url(url)
                .addHeader("X-bocang-Authorization",token)
                .addParams("order", order)
                .addParams("code","wxpay.dmf")
                .build()
                .execute(new Callback() {
                    @Override
                    public Object parseNetworkResponse(Response response, int i1) throws Exception {
                        String jsonRes=response.body().string();
//                        callback.onResponse(advertBeanList,i1);

                        return jsonRes;
                    }

                    @Override
                    public void onError(Call call, Exception e, int i) {
                        callback.onError(call,e,i);
                    }

                    @Override
                    public String onResponse(Object o, int i) {
                        callback.onResponse(String.valueOf(o),i);

                        return null;
                    }
                });
        //        requestQueue.add(simplePostRequest);

    }

    public static void sendUser(String profile, final Callback<String> callback) {
        if(!hashkNewwork()){
            return;
        }

//        responseListener.onStarted();
//        Map<String ,String> map=new HashMap<>();
        String url= profile;
        String token= MyShare.get(UIUtils.getContext()).getString(Constance.TOKEN);
        OkHttpUtils.post()
                .url(url)
                .addHeader("X-bocang-Authorization",token)
//                .addParams("order", order)
                .build()
                .execute(new Callback() {
                    @Override
                    public Object parseNetworkResponse(Response response, int i1) throws Exception {
                        String jsonRes=response.body().string();
//                        LogUtils.logE("okhttp3:",jsonRes);

//                        callback.onResponse(advertBeanList,i1);
                        return jsonRes;
                    }

                    @Override
                    public void onError(Call call, Exception e, int i) {
                        callback.onError(call,e,i);
                    }

                    @Override
                    public String onResponse(Object o, int i) {
                        callback.onResponse((String) o,i);

                        return null;
                    }
                });
    }
    public static void downloadMp4(String url, final FileCallBack callBack){
        if(!hashkNewwork()){
            return;
        }
        OkHttpUtils.get()
                .url(url)
                .build()
                .execute(new FileCallBack(Environment.getExternalStorageDirectory().getAbsolutePath(),System.currentTimeMillis()+"juhao.mp4") {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        callBack.onError(call,e,id);
                    }

                    @Override
                    public String onResponse(File response, int id) {
                        callBack.onResponse(response,id);
                        return null;
                    }
                });
    }
//    public static void SendRequest(String url, final Callback callback){
//        if(!hashkNewwork()){
//            return;
//        }
//
////        responseListener.onStarted();
////        Map<String ,String> map=new HashMap<>();
//        String token= MyShare.get(UIUtils.getContext()).getString(Constance.TOKEN);
//        OkHttpUtils.post()
//                .url(url)
//                .addHeader("X-bocang-Authorization",token)
//                .build()
//                .execute(new Callback() {
//                    @Override
//                    public Object parseNetworkResponse(okhttp3.Response response, int i1) throws Exception {
//                        String jsonRes=response.body().string();
//                        LogUtils.logE("okhttp3:",jsonRes);
//
////                        callback.onResponse(advertBeanList,i1);
//                        return jsonRes;
//                    }
//
//                    @Override
//                    public void onError(okhttp3.Call call, Exception e, int i) {
//                        callback.onError(call,e,i);
//                    }
//
//                    @Override
//                    public void onResponse(Object o, int i) {
//                        callback.onResponse((String) o,i);
//
//                    }
//                });
//    }
}
