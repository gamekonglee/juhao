package bc.juhao.com.controller;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Message;
import android.widget.TextView;

import bc.juhao.com.R;
import bc.juhao.com.ui.activity.SettingActivity;
import bocang.utils.AppDialog;
import bocang.utils.DataCleanUtil;

/**
 * @author: Jun
 * @date : 2017/2/6 9:51
 * @description : 设置
 */
public class SettingController extends BaseController{
    private SettingActivity mView;
    private TextView cache_tv;
    private String mTotalCacheSize;

    public SettingController(SettingActivity v){
        mView=v;
        initView();
        initViewData();
    }

    private void initViewData() {
        getTotalCacheSize();
    }

    private void initView() {
        cache_tv = (TextView) mView.findViewById(R.id.cache_tv);
    }

    @Override
    protected void handleMessage(int action, Object[] values) {

    }

    @Override
    protected void myHandleMessage(Message msg) {

    }

    /**
     * 清理缓存
     */
    public void clearCache() {
        new AlertDialog.Builder(mView).setTitle("清除缓存?").setMessage("确认清除您所有的缓存？")
                .setPositiveButton("清除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(mTotalCacheSize.equals("0K")){
                            AppDialog.messageBox("没有缓存可以清除!");
                            return;
                        }
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                DataCleanUtil.clearAllCache(mView);
                                mView.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        AppDialog.messageBox("清除缓存成功!");
                                        getTotalCacheSize();
                                    }
                                });
                            }
                        }).start();

                    }
                })
                .setNegativeButton("取消", null).show();
    }

    /**
     * 查看缓存大小
     */
    private void getTotalCacheSize(){
        try {
            mTotalCacheSize = DataCleanUtil.getTotalCacheSize(mView);
            cache_tv.setText(mTotalCacheSize);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
