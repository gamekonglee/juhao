package bc.juhao.com.bean;
/**
 * 基本的订单信息bean
 * @author Administrator
 *
 */
public class OrderInfo {
	/*签约合作者身份ID*/
	private String partner;
	/*签约卖家支付宝账号*/
	private String seller_id;
	/*商户网站唯一订单号*/
	private String out_trade_no;
	/*服务器异步通知页面路径*/
	private String notify_url;
	
	
	/*商品名称*/
	private String subject;
	/*商品详情*/
	private String body;
	/*商品金额*/
	private String total_fee;

	
	
	
	public String getPartner() {
		return partner;
	}
	public void setPartner(String partner) {
		this.partner = partner;
	}
	public String getSeller_id() {
		return seller_id;
	}
	public void setSeller_id(String seller_id) {
		this.seller_id = seller_id;
	}
	public String getOut_trade_no() {
		return out_trade_no;
	}
	public void setOut_trade_no(String out_trade_no) {
		this.out_trade_no = out_trade_no;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public String getTotal_fee() {
		return total_fee;
	}
	public void setTotal_fee(String total_fee) {
		this.total_fee = total_fee;
	}
	public String getNotify_url() {
		return notify_url;
	}
	public void setNotify_url(String notify_url) {
		this.notify_url = notify_url;
	}
	
	
	
}
