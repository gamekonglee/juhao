package bocang.net;

import android.graphics.Bitmap;
import android.os.AsyncTask;

public class NetBitmap extends AsyncTask<String, Void, Bitmap> {
    private int style;
    private Callback callback;
    private NetServer server;
    private String sem;

    public NetBitmap(int style, Callback callback) {
        this.style = style;
        this.callback = callback;
        server = new NetServer();
        sem = null;
    }

    public void addParameter(String name, String value) {
        server.addParameter(name, value);
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        Bitmap ret = getBitmap();
        return ret;
    }

    @Override
    protected void onPostExecute(Bitmap result) {
        callback.onCallback(style, result, sem);
    }

    private Bitmap getBitmap() {

        return server.getBitmap();

    }

    public interface Callback {
        public void onCallback(int style, Bitmap ans, String sem);
    }

}
