package bocang.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.view.Gravity;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.alertview.AlertView;

import bocang.view.BaseApplication;


/**
 * 简单对话框类
 * 
 */
public class AppDialog {

	private static AlertView mAlertView;//避免创建重复View，先创建View，然后需要的时候show出来，推荐这个做法
	private static AlertView mAlertViewExt;//窗口拓展例子
	private static EditText etName;//拓展View内容
	private static InputMethodManager imm;

	public AppDialog() {

	}

	/**
	 * 信息提示框
	 * 
	 * @param o
	 */
	public static void messageBox(Object o) {
		String text = o == null ? "" : o.toString();
		Toast toast = Toast.makeText(BaseApplication.getInstance(), text, Toast.LENGTH_SHORT);
		LinearLayout linearLayout = (LinearLayout) toast.getView();  
		TextView messageTextView = (TextView) linearLayout.getChildAt(0);  
		messageTextView.setTextSize(17);  
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}

	public static void alert(Context context, Object o) {
		String text = o == null ? "" : o.toString();
		final AlertDialog d = new AlertDialog.Builder(context).setMessage(text)
				.setTitle("温馨提示").setPositiveButton("确定", null).create();

		d.show();

	}



}
