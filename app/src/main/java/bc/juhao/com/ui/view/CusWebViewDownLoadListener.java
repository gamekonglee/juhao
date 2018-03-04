package bc.juhao.com.ui.view;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.webkit.DownloadListener;

/**
 * Created by XY on 2017/6/8.
 */

public class CusWebViewDownLoadListener implements DownloadListener {

    private Context mContext;
    public CusWebViewDownLoadListener(Context context){
        mContext=context;
    }

    @Override
    public void onDownloadStart(String url, String userAgent,
                                String contentDisposition, String mimetype, long contentLength) {
        // TODO Auto-generated method stub
        /*
        Log.i("tag", "url="+url);
        Log.i("tag", "userAgent="+userAgent);
        Log.i("tag", "contentDisposition="+contentDisposition);
        Log.i("tag", "mimetype="+mimetype);
        Log.i("tag", "contentLength="+contentLength);
        */
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        mContext.startActivity(intent);
    }
}
