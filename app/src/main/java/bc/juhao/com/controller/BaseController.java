package bc.juhao.com.controller;


import android.annotation.TargetApi;
import android.os.Build;
import android.os.Handler;
import android.os.Message;

import bc.juhao.com.factory.ThreadPoolFactory;
import bc.juhao.com.utils.Network;


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



}
