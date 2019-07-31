package bc.juhao.com.controller;


import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Message;

import bc.juhao.com.cons.Constance;
import bc.juhao.com.factory.ThreadPoolFactory;
import bc.juhao.com.ui.activity.user.LoginActivity;
import bc.juhao.com.ui.view.dialog.ShowSureDialog;
import bc.juhao.com.utils.MyShare;
import bc.juhao.com.utils.Network;
import bocang.json.JSONObject;
import bocang.utils.AppUtils;


/**
 * Copyright (C) 2016
 * filename :
 * action : Controller基类
 *
 * @author : Jun
 * @version : 1.0
 * @date : 2016-09-12
 * modify :
 */
public abstract class BaseController{
    protected Network mNetWork;

    public BaseController(){
        mNetWork = new Network();
    }
    /**
     * 发送异步数据，由子类来实现
     *
     * @param action
     * @param values
     */
    public void sendAsyncMessage(final int action, final Object... values) {
        ThreadPoolFactory.createNormalThreadPoolPoxy().execute(new Runnable() {
            @Override
            public void run() {
                handleMessage(action, values);
            }
        });
    }

    /**
     * handler机制
     */
    public Handler mHandler=new Handler(){
        @TargetApi(Build.VERSION_CODES.GINGERBREAD)
        @Override
        public void handleMessage(Message msg) {
            myHandleMessage(msg);
        }
    };

    /**
     * 处理子线程的数据
     * @param action
     * @param values
     */
    protected abstract void handleMessage(int action, Object[] values);

    /**
     * handler消息处理
     * @param msg 消息
     */
    protected abstract void myHandleMessage(Message msg);

    public void getOutLogin(final Activity activity, JSONObject ans){
//        String token = MyShare.get(activity).getString(Constance.TOKEN);
//        if(AppUtils.isEmpty(token))return;
//        if(AppUtils.isEmpty(ans))return;
//        String errorToken = "该账号已在其它设备登录，请重新登录!";
//        if ("Token 无效".equals(ans.getString("error_desc"))) {
//            try {
//                ShowSureDialog dialog = new ShowSureDialog();
//                dialog.show(activity, "提示", errorToken, new ShowSureDialog.OnBottomClickListener() {
//                    @Override
//                    public void positive() {
//                        Intent intentAct = new Intent();
//                        intentAct.setClass(activity, LoginActivity.class);
//                        intentAct.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                        activity.startActivity(intentAct);
//                    }
//                });
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            return;
//        }
    }

    public void getOutLogin02(final Activity activity, com.alibaba.fastjson.JSONObject ans){
        try{
//            if(AppUtils.isEmpty(ans))return;
//            String errorToken = "该账号已在其它设备登录，请重新登录!";
//            if ("Token 无效".equals(ans.getString("error_desc"))) {
//                try {
//                    ShowSureDialog dialog = new ShowSureDialog();
//                    dialog.show(activity, "提示", errorToken, new ShowSureDialog.OnBottomClickListener() {
//                        @Override
//                        public void positive() {
//                            Intent intentAct = new Intent();
//                            intentAct.setClass(activity, LoginActivity.class);
//                            intentAct.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                            activity.startActivity(intentAct);
//                        }
//                    });
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                return;
//            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }


}
