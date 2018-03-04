package bocang.utils;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * 程序升级
 */
public class AppUpdate {

    private Context mContext;

    public AppUpdate(Context context) {
        mContext = context;
    }

    /**
     * 升级程序
     *
     * @param newFileName
     * @param newAppVersion
     */
    public void update(String newFileName, String newAppVersion) {
        if (AppConfig.APP_VERSION.equals(newAppVersion)) {
            return;
        }

        new DownloadUpdate(newFileName, newAppVersion).execute();
    }

    private class DownloadUpdate extends AsyncTask<Void, Integer, String> {

        ProgressDialog pd = null;
        String mFileName = null;

        DownloadUpdate(String fileName, String appVersion) {

            mFileName = fileName;

            pd = new ProgressDialog(mContext, 0);
            pd.setTitle("下载");
            pd.setMessage("下载新版本(v" + appVersion + ")中...");
            pd.setMax(100);
            pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            pd.show();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            // 更新进度
            pd.setProgress(values[0]);
        }

        @Override
        protected String doInBackground(Void... params) {

            String ret = null;

            String local_url = AppConfig.TEMP_DIRECTORY + "/" + mFileName;
            File file = new File(local_url);
            if (file.exists())
                file.delete();

            URL url;
            try {
                url = new URL(AppConfig.UPDATE_URL);
                HttpsURLConnection conn;
                conn = (HttpsURLConnection) url.openConnection();

                conn.setConnectTimeout(5000);
                // 获取到文件的大小
                int length = conn.getContentLength();
                InputStream is = conn.getInputStream();
                FileOutputStream fos = new FileOutputStream(file);
                BufferedInputStream bis = new BufferedInputStream(is);
                byte[] buffer = new byte[1024];
                int len;
                int total = 0;
                while ((len = bis.read(buffer)) != -1) {
                    fos.write(buffer, 0, len);
                    total += len;
                    publishProgress((int) ((total / (float) length) * 100));
                }
                fos.flush();
                fos.close();
                bis.close();
                is.close();

            } catch (MalformedURLException e) {
                ret = e.getMessage();
            } catch (IOException e) {
                ret = e.getMessage();
            }

            return ret;

        }

        @Override
        protected void onPostExecute(String result) {

            if (result == null) {
                requestInstall();
            } else {
                AppDialog.messageBox(result);
            }

            pd.dismiss();
        }

        /**
         * 询问安装
         */
        private void requestInstall() {

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
            alertDialogBuilder
                    .setTitle("程序升级")
                    .setIcon(android.R.drawable.ic_dialog_info)
                    .setMessage("是否安装新版本?")
                    .setPositiveButton("立即安装", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            installApk();
                        }
                    })
                    .setNegativeButton("暂不安装", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }

        /**
         * 安装APK
         */
        private void installApk() {

            String localUrl = AppConfig.TEMP_DIRECTORY + "/" + mFileName;
            File file = new File(localUrl);

            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
            mContext.startActivity(intent);
        }
    }
}
