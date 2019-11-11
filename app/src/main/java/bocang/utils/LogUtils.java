package bocang.utils;

import android.util.Log;

/**
 * Created by gklee on 2016/12/23.
 */

public class LogUtils {

    private static boolean isDebug = true;

    public static void logE(String tag, String content) {

        if (isDebug){
            int p = 2048;
            long length = content.length();
            if (length < p || length == p)
                Log.e(tag, content);
            else {
                while (content.length() > p) {
                    String logContent = content.substring(0, p);
                    content = content.replace(logContent, "");
                    Log.e(tag, logContent);
                }
                Log.e(tag, content);
            }
        }

    }
}
