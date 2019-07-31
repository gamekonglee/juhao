package bc.juhao.com.controller.programme;

import android.content.Intent;
import android.os.Message;
import android.util.Log;
import android.widget.EditText;

import com.lib.common.hxp.view.ListViewForScrollView;
import com.zhy.http.okhttp.callback.Callback;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import bc.juhao.com.R;
import bc.juhao.com.bean.Programme;
import bc.juhao.com.cons.Constance;
import bc.juhao.com.controller.BaseController;
import bc.juhao.com.listener.INetworkCallBack;
import bc.juhao.com.listener.ISchemeChooseListener;
import bc.juhao.com.net.ApiClient;
import bc.juhao.com.ui.activity.programme.SelectSchemeActivity;
import bc.juhao.com.ui.adapter.SchemeTypeAdapter;
import bc.juhao.com.utils.UIUtils;
import bocang.json.JSONArray;
import bocang.json.JSONObject;
import bocang.utils.AppUtils;
import bocang.utils.LogUtils;
import bocang.utils.MyToast;
import okhttp3.Call;
import okhttp3.Response;

/**
 * @author: Jun
 * @date : 2017/3/14 11:10
 * @description :
 */
public class SelectSchemeController extends BaseController {
    private SelectSchemeActivity mView;
    private EditText title_tv,remark_tv;
    private ListViewForScrollView scheme_type_lv;
    private SchemeTypeAdapter mAdapter;
    private List<Programme> mProgrammes;
    private String mStyle="现代简约";
    private String mSplace="玄关";
    private Intent mIntent;

    public SelectSchemeController(SelectSchemeActivity v){
        mView=v;
        initView();
        initViewData();
    }

    private void initViewData() {
        setropownMenuData();

//        mAdapter.setData(mProgrammes);
    }

    private void initView() {
        title_tv = (EditText) mView.findViewById(R.id.title_tv);
        remark_tv = (EditText) mView.findViewById(R.id.remark_tv);
        scheme_type_lv = (ListViewForScrollView) mView.findViewById(R.id.scheme_type_lv);
        scheme_type_lv.setDivider(null);//去除listview的下划线
        mAdapter=new SchemeTypeAdapter(mView);
        scheme_type_lv.setAdapter(mAdapter);
        mAdapter.setListener(new ISchemeChooseListener() {
            @Override
            public void onSchemeChanged(String style, String splace) {
                if(!AppUtils.isEmpty(style)){
                    mStyle = style;
                }
                if(!AppUtils.isEmpty(splace)){
                    mSplace = splace;
                }
            }
        });

    }

    private void setropownMenuData(){
        mProgrammes=new ArrayList<>();
        mNetWork.sendScene(new INetworkCallBack() {
            @Override
            public void onSuccessListener(String requestCode, JSONObject ans) {
                LogUtils.logE("success",ans.toString());
                JSONArray categories=ans.getJSONArray(Constance.categories);
                JSONArray spaceArray=categories.getJSONObject(0).getJSONArray(Constance.attrVal);
                JSONArray styleArray=categories.getJSONObject(1).getJSONArray(Constance.attrVal);
                String[] styleArrs =new String[categories.getJSONObject(1).getJSONArray(Constance.attrVal).length()];
                String[] spaceArrs =new String[categories.getJSONObject(0).getJSONArray(Constance.attrVal).length()];
                for(int i=0;i<spaceArray.length();i++){
                    spaceArrs[i]=spaceArray.getString(i);
                }
                for(int j=0;j<styleArray.length();j++){
                    styleArrs[j]=styleArray.getString(j);
                }
                Programme programme=new Programme();
//                programme.setAttr_name(UIUtils.getString(R.string.style_name));
                programme.setAttr_name(categories.getJSONObject(1).getString(Constance.attr_name));
                List<String> attrVal= Arrays.asList(styleArrs);
                List<String> attrVal02=new ArrayList<>();
                for(int i=0;i<attrVal.size();i++){
                    attrVal02.add(attrVal.get(i));
                }

                programme.setAttrVal(attrVal02);
                mProgrammes.add(programme);
                Programme programme2=new Programme();
                programme2.setAttr_name(categories.getJSONObject(0).getString(Constance.attr_name));
                List<String> spaces= Arrays.asList(spaceArrs);
                List<String> spaces02=new ArrayList<>();
                for(int i=0;i<spaces.size();i++){
                    spaces02.add(spaces.get(i));
                }
                programme2.setAttrVal(spaces02);
                mProgrammes.add(programme2);
                mAdapter.setData(mProgrammes);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailureListener(String requestCode, JSONObject ans) {
            LogUtils.logE("failure",ans.toString());
            }
        });
//        ApiClient.getScensList(new  Callback<String>() {
//            @Override
//            public String parseNetworkResponse(Response response, int id) throws Exception {
//                return response.body().string();
//            }
//
//            @Override
//            public void onError(Call call, Exception e, int id) {
//
//            }
//
//            @Override
//            public String onResponse(String response, int id) {
//                LogUtils.logE("scene",response);
//                return null;
//            }
//        });
//        String[] styleArrs = UIUtils.getStringArr(R.array.style);
//        String[] spaceArrs = UIUtils.getStringArr(R.array.space);


    }


    @Override
    protected void handleMessage(int action, Object[] values) {

    }

    @Override
    protected void myHandleMessage(Message msg) {

    }


    public void saveScheme() {
        if(AppUtils.isEmpty(title_tv.getText().toString())){
            MyToast.show(mView,"请输入您的标题");
            return;
        }

        mIntent=new Intent();
        mIntent.putExtra(Constance.style, mStyle);
        mIntent.putExtra(Constance.space, mSplace);
        mIntent.putExtra(Constance.title, title_tv.getText().toString());
        mIntent.putExtra(Constance.content, remark_tv.getText().toString());
        mView.setResult(Constance.FROMSCHEME, mIntent);//告诉原来的Activity 将数据传递给它
        mView.finish();//一定要调用该方法 关闭新的AC 此时 老是AC才能获取到Itent里面的值
    }
}
