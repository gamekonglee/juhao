package bocang.net;

import android.os.AsyncTask;
import android.util.Log;

import bocang.json.JSONObject;
import bocang.utils.AppUtils;

public class NetJSONObject extends AsyncTask<String, Void, JSONObject> {
    private int style;
    private Callback callback;
    private String sem;
    private NetServer server;
    private String elementName, fileName;
    private String mUrlpath;

    public NetJSONObject(int style, Callback callback) {
        this.style = style;
        this.callback = callback;
        server = new NetServer();
        elementName = null;
        fileName = null;
        sem = null;
    }

    public void addParameter(String name, String value) {
        server.addParameter(name, value);
    }

    /**
     * 传递地址
     * @param url  地址参数
     */
    public void addURLPath(String url){
        mUrlpath=url;
    }

    public void setUploadFile(String elementName, String fileName) {
        this.elementName = elementName;
        this.fileName = fileName;
    }

    @Override
    protected JSONObject doInBackground(String... params) {
        JSONObject ret = getJSONObject();
        return ret;
    }

    @Override
    protected void onPostExecute(JSONObject result) {
        callback.onCallback(style, result, sem);
    }

    private JSONObject getJSONObject() {

        String str;
        if(style==0){
            if (elementName == null || fileName == null) {
                str = server.doPost(mUrlpath);
            } else {
                str = server.getString(elementName, fileName,mUrlpath);
            }
        }else{
            str = server.doGet(mUrlpath);
        }

        if (str == null || str.isEmpty()) {
            sem = server.getSEM();
            return null;
        } else {

            str = AppUtils.getDecodeStr(str);
            if (str == null) {
                sem = "md5校验失败";
                return null;
            }

            JSONObject ss=new JSONObject(str);
            Log.v("520it",ss.toString());
            return  ss;

        }

    }

    public interface Callback {
        public void onCallback(int style, JSONObject ans, String sem);
    }

}
