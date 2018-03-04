package bocang.utils;

import java.lang.Thread.UncaughtExceptionHandler;

public class AppUncaughtExceptionHandler implements UncaughtExceptionHandler {

	private UncaughtExceptionHandler defaultUncaughtExceptionHandler;

	public AppUncaughtExceptionHandler() {
		this.defaultUncaughtExceptionHandler = Thread
				.getDefaultUncaughtExceptionHandler();
	}

	@Override
	public void uncaughtException(Thread thread, Throwable ex) {

		// String msg = ex.getLocalizedMessage();
		// AppConfig.logd(this, msg);

		// 处理异常，保存异常log或向服务器发送异常报告
		// WebServiceJSONObject web = new WebServiceJSONObject(0, new
		// IWebServiceJSONObject() {
		// @Override
		// public void webServiceCallback(int style, JSONObject ans, String sem)
		// {
		// }
		// });
		// try {
		// String query = URLEncoder.encode(msg, "utf-8");
		// /////////////////// web.execute(AppConfig.SERVER_ROOT +
		// "/app1/log.php?log=" + query);
		// } catch (UnsupportedEncodingException e1) {
		// }

		// 暂停运行3秒
		// try {
		// Thread.sleep(3000);
		// } catch (InterruptedException e) {
		// }

		// 默认处理方式
		defaultUncaughtExceptionHandler.uncaughtException(thread, ex);
	}

}