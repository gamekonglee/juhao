package bc.juhao.com.chat.service;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import java.util.List;

import bc.juhao.com.chat.cache.ACache;
import bc.juhao.com.chat.model.User;
import bc.juhao.com.cons.Constance;


public class UpdateIntentService extends IntentService {


    ACache mACache;
    String mUid;
    List<User> users;

//    protected Network mNetWork;

    public UpdateIntentService() {
        super("UpdateIntentService");
//        mNetWork = new Network();
    }


    @Override
    public void onCreate() {
        super.onCreate();
        mACache = ACache.get(getApplicationContext());
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.e("lxx", "onHandleIntent");
        if (intent != null) {

            mUid = intent.getStringExtra(Constance.User_ID);

//            mNetWork.sendMoreUserInfo(mUid, Constance.UserColumns, new INetworkCallBack() {
//                @Override
//                public void onSuccessListener(JSONObject ans) {
//                    JSONArray jsonArray = ans.getJSONArray(Constance.MSG);
//                    mACache.put(mUid + "Nick", jsonArray.getJSONObject(0).getString(Constance.USER_NICE), ACache.TIME_DAY);
//                    mACache.put(mUid + "Avatar", NetWorkConst.IMAGEBASE+jsonArray.getJSONObject(0).getString(Constance.USER_ICON), ACache.TIME_DAY);
//                }
//
//                @Override
//                public void onFailureListener(JSONObject ans) {
//
//                }
//            });
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }




}
