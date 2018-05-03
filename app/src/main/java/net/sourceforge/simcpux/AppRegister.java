package net.sourceforge.simcpux;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import bc.juhao.com.cons.Constance;

public class AppRegister extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		final IWXAPI msgApi = WXAPIFactory.createWXAPI(context, Constance.APP_ID,true);

		// ����appע�ᵽ΢��
		msgApi.registerApp(Constance.APP_ID);
	}
}
