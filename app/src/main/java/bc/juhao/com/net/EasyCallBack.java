package bc.juhao.com.net;

import com.zhy.http.okhttp.callback.Callback;

import okhttp3.Call;
import okhttp3.Response;

public abstract class EasyCallBack extends Callback<String> {
    @Override
    public String parseNetworkResponse(Response response, int id) throws Exception {
        return null;
    }

    @Override
    public void onError(Call call, Exception e, int id) {

    }

    @Override
    public String onResponse(String response, int id) {
        onRespon(response);
        return null;
    }
    public abstract   void onRespon(String res);
}
