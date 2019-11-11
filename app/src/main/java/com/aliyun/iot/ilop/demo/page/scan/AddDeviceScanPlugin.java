package com.aliyun.iot.ilop.demo.page.scan;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;

import com.aliyun.iot.aep.component.router.Router;
import com.aliyun.iot.aep.component.scan.IScanPlugin;

/**
 * Created by zgb on 18/1/16.
 */

public class AddDeviceScanPlugin implements IScanPlugin {
    public static final String NAME = "AddDeviceScanPlugin";

    private Activity activity;

    public AddDeviceScanPlugin(Activity activity){
        this.activity = activity;
    }

    @Override
    public boolean executePlugin(Context context, String s) {
        if(TextUtils.isEmpty(s)){
            return false;
        }
        Uri uri = Uri.parse(s);
        String sharecode = uri.getQueryParameter("pk");
        if(!TextUtils.isEmpty(sharecode)){
            return true;
        }
        return false;
    }

    @Override
    public void dealCode(Context context, String s) {
        Uri uri = Uri.parse(s);
        String productKey = uri.getQueryParameter("pk");
        if(TextUtils.isEmpty(productKey)){
            return ;
        }
        String code = "link://plugin/a123kfz2KdRdrfYc";
        Bundle bundle = new Bundle();
        bundle.putString("productKey", productKey);
        Router.getInstance().toUrlForResult(activity, code, 1, bundle);
    }
}
