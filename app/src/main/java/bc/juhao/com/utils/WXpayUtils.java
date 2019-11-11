package bc.juhao.com.utils;

/**
 * Created by gamekonglee on 2018/4/3.
 */

import android.content.Context;
import android.widget.Toast;

import com.aliyun.iot.ilop.demo.DemoApplication;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import bc.juhao.com.bean.OrederSendInfo;
import bc.juhao.com.bean.PrepayIdInfo;
import bc.juhao.com.cons.Constance;
import bc.juhao.com.ui.activity.IssueApplication;
import bocang.utils.MD5;

/**
 * Created by xmg on 2016/12/5.
 */

public class WXpayUtils {

    private static IWXAPI iwxapi;
    private static PayReq req;
    public static Context mContext;

    public static IWXAPI getWXAPI(){
        if (iwxapi == null){
            //通过WXAPIFactory创建IWAPI实例
            iwxapi = WXAPIFactory.createWXAPI(mContext, null);
            req = new PayReq();
            //将应用的appid注册到微信
            iwxapi.registerApp(Constance.APP_ID);
        }
        return iwxapi;
    }

    //生成随机字符串
    public static String genNonceStr() {
        Random random = new Random();
        return MD5.getMessageDigest(String.valueOf(random.nextInt(10000)).getBytes());
    }

    //获得时间戳
    private static long genTimeStamp() {
        return System.currentTimeMillis() / 1000;
    }

    //生成预支付随机签名
    public static  String genSign(OrederSendInfo info) {
        StringBuffer sb = new StringBuffer(info.toString());
        if (Constance.API_KEY.equals("")){
            Toast.makeText(DemoApplication.getInstance(),"APP_ID为空",Toast.LENGTH_LONG).show();
        }
        //拼接密钥
        sb.append("key=");
        sb.append(Constance.API_KEY);

        String appSign = MD5.getMessageDigest(sb.toString().getBytes());

        return appSign;
    }

    //生成支付随机签名
    private static String genAppSign(List<Param> params){
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < params.size(); i++) {
            sb.append(params.get(i).key);
            sb.append('=');
            sb.append(params.get(i).value);
            sb.append('&');
        }
        //拼接密钥
        sb.append("key=");
        sb.append(Constance.API_KEY);

        String appSign = MD5.getMessageDigest(sb.toString().getBytes());
        return appSign.toUpperCase();
    }

    //生成支付参数
    private static void genPayReq(PrepayIdInfo bean, String prepayid) {
        req.appId = Constance.APP_ID;
        req.partnerId = Constance.MCH_ID;
        req.prepayId = prepayid;
        req.packageValue = "Sign=WXPay";
        req.nonceStr = bean.getNonce_str();
        req.timeStamp =bean.getTimestamp();

        List<Param> signParams = new LinkedList<Param>();
        signParams.add(new Param("appid", req.appId));
        signParams.add(new Param("noncestr", req.nonceStr));
        signParams.add(new Param("package", req.packageValue));
        signParams.add(new Param("partnerid", req.partnerId));
        signParams.add(new Param("prepayid", req.prepayId));
        signParams.add(new Param("timestamp", req.timeStamp));

//        req.sign = genAppSign(signParams);
        req.sign = bean.getSign();
    }

    public static void Pay(PrepayIdInfo bean, String prepayid){
        if (judgeCanGo()){
            genPayReq(bean,prepayid);
            iwxapi.registerApp(Constance.APP_ID);
            iwxapi.sendReq(req);
        }
    }

    private static boolean judgeCanGo(){
        getWXAPI();
        if (!iwxapi.isWXAppInstalled()) {
            Toast.makeText(mContext, "请先安装微信应用", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    /**
     * post请求参数类   这里可以根据项目抽取成泛型
     */
    public static class Param {

        String key;
        String value;

        public Param() {
        }

        public Param(String key, String value) {
            this.key = key;
            this.value = value;
        }

    }


}
