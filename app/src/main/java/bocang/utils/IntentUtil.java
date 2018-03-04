package bocang.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

/**
 * @author Jun
 * @version 1.0
 * @time 2016/11/10 15:37
 * @des 跳转工具类
 */
public class IntentUtil {

    public static void startActivity(Context context, Class clzz, boolean flag) {
        Intent intent = new Intent(context, clzz);
        ((Activity) context).startActivity(intent);
        if (flag) {
            ((Activity) context).finish();
        }
    }
    public static void startActivity(Activity activity, Class clzz, boolean flag) {
        Intent intent = new Intent(activity, clzz);
        (activity).startActivity(intent);
        if (flag) {
            (activity).finish();
        }
    }
}
