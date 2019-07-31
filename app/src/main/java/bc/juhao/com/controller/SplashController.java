package bc.juhao.com.controller;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.zhy.http.okhttp.callback.Callback;

import java.util.Timer;

import bc.juhao.com.cons.Constance;
import bc.juhao.com.cons.NetWorkConst;
import bc.juhao.com.listener.INetworkCallBack;
import bc.juhao.com.net.ApiClient;
import bc.juhao.com.ui.activity.SplashActivity;
import bc.juhao.com.utils.MyShare;
import bocang.json.JSONArray;
import bocang.json.JSONObject;
import bocang.utils.AppUtils;
import bocang.utils.LogUtils;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by gamekonglee on 2018/6/26.
 */

public class SplashController  extends BaseController {

    private final SplashActivity mView;
    private int WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 1;
    @Override
    protected void handleMessage(int action, Object[] values) {

    }

    @Override
    protected void myHandleMessage(Message msg) {

    }
    public SplashController(SplashActivity activity){
        mView = activity;
        initUI();
    }

    private void initUI() {

        mNetWork.sendBannerIndex(new INetworkCallBack() {
            @Override
            public void onSuccessListener(String requestCode, JSONObject ans) {
//                LogUtils.logE("bannerIndex",ans.toString());
                if(ans==null){
                    return;
                }
                JSONArray banners=ans.getJSONArray(Constance.banners);
                if(banners==null||banners.length()==0||banners.getJSONObject(0)==null){
                    startAni();
                    return;
                }
                ImageLoader.getInstance().loadImage(NetWorkConst.SCENE_HOST + "/data/afficheimg/" + ans.getJSONArray(Constance.banners).getJSONObject(0).getString(Constance.ad_code), new ImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String s, View view) {

                    }

                    @Override
                    public void onLoadingFailed(String s, View view, FailReason failReason) {
                        startAni();
                    }

                    @Override
                    public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                        mView.mLogoIv.setImageBitmap(bitmap);
                        startAni();
                    }

                    @Override
                    public void onLoadingCancelled(String s, View view) {

                    }
                });

            }

            @Override
            public void onFailureListener(String requestCode, JSONObject ans) {
//            LogUtils.logE("requestCode",requestCode+ans);
            startAni();
            }
        });
    }

    private void startAni() {
        mView.mLogoIv.setVisibility(View.VISIBLE);
        mView.mAnimation = new AlphaAnimation(0.2f, 1.0f);
        mView.mAnimation.setDuration(2500);
        mView.mAnimation.setFillAfter(true);
        mView.mLogoIv.startAnimation(mView.mAnimation);
        mView.mAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                try {
                    if(mView.mTimerSc!=null)new Timer().schedule(mView.mTimerSc, 0,1000);
                }catch (Exception e){

                }
                String token = MyShare.get(mView).getString(Constance.TOKEN);
                if (AppUtils.checkNetwork() && !AppUtils.isEmpty(token)){
                    mView.getSuccessLogin();
                }
                mView.version = Build.VERSION.RELEASE;
//        LogUtils.logE("codename",Build.VERSION.CODENAME);
//        LogUtils.logE("realease",Build.VERSION.RELEASE);
                int osVersion = Integer.valueOf(Build.VERSION.SDK);
                if (osVersion>22){

                    if (ContextCompat.checkSelfPermission(mView, Manifest.permission.UNINSTALL_SHORTCUT)
                            != PackageManager.PERMISSION_GRANTED) {
                        //申请WRITE_EXTERNAL_STORAGE权限
                        ActivityCompat.requestPermissions(mView, new String[]{Manifest.permission_group.STORAGE,Manifest.permission_group.PHONE},
                                WRITE_EXTERNAL_STORAGE_REQUEST_CODE);
                    }else{
                        mView.getImei();
                    }
                }else{
                    //如果SDK小于6.0则不去动态申请权限
                    mView.getImei();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


    }
}
