package bc.juhao.com.utils;


import bc.juhao.com.bean.OrderInfo;

/**
 * 模仿网络请求工具
 * 
 * @author Administrator
 *
 */
public class HttpUtils {

	// 商户PID
	public static  String PARTNER;
	// 商户收款账号
	public static  String SELLER;

	/*服务器异步通知页面路径*/
	public static  String NOTIFY_URL = "";
	/*商户网站唯一订单号*/
	public static  String OUT_TRADE_NO = "";

	//商户私钥，pkcs8格式
	public static String RSA_PRIVATE;

	/**
	 * 获取签名
	 * @param orderInfo  提交给服务器的参数
	 * @return 服务器返回的结果
	 */
	public  static String getSign(String orderInfo) {
		return SignUtils.sign(orderInfo, RSA_PRIVATE);
	}


	/**
	 * 商户与订单信息
	 */
	public static OrderInfo getOrderInfo(String body,String subject,String money,String notifyUrl,String orderID) {
		OrderInfo  mOrderInfo=new OrderInfo();
		mOrderInfo.setBody(body);
		mOrderInfo.setSubject(subject);
		mOrderInfo.setTotal_fee(money);
		
		mOrderInfo.setNotify_url(notifyUrl);
		//生成订单号
		mOrderInfo.setOut_trade_no(orderID);
		mOrderInfo.setPartner(PARTNER);
		mOrderInfo.setSeller_id(SELLER);
		return mOrderInfo;
	}

}
