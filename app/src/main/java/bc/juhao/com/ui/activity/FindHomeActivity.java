package bc.juhao.com.ui.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.gklee.regionselector.BaseAdapterHelper;
import com.gklee.regionselector.OnRegionDataSetListener;
import com.gklee.regionselector.QuickAdapter;
import com.gklee.regionselector.RegionBean;
import com.gklee.regionselector.RegionLevel;
import com.gklee.regionselector.RegionSelectDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.List;

import bc.juhao.com.R;
import bc.juhao.com.bean.EstateBean;
import bc.juhao.com.common.BaseActivity;
import bc.juhao.com.cons.Constance;
import bc.juhao.com.net.ApiClient;
import bc.juhao.com.net.EasyCallBack;
import bc.juhao.com.utils.UIUtils;
import bocang.json.JSONArray;
import bocang.json.JSONObject;
import bocang.utils.LogUtils;
import bocang.utils.MyToast;
import okhttp3.Response;

public class FindHomeActivity extends BaseActivity {


    private List<RegionBean> provinceRegionBeans;
    private RegionSelectDialog regionSelectDialog;
    private List<RegionBean> cityRegionBeans;
    private List<RegionBean> zoneRegionBeans;
    private String mProvince;
    private String mCity;
    private String mZone;
    private TextView tv_region;
    private EditText et_xiaoguo;
    private TextView tv_search;
    private List<EstateBean> estateBeans;
    private String mProvinceId;
    private String mCityId;
    private String mZoneId;
    private String phone;
    private ProgressDialog progressDialog;

    @Override
    protected void InitDataView() {

    }

    @Override
    protected void initController() {

    }

    @Override
    protected void initView() {
    setContentView(R.layout.activity_find_home);
        tv_region=findViewById(R.id.tv_region);
        et_xiaoguo=findViewById(R.id.et_xiaoqu);
        tv_search = findViewById(R.id.tv_search);

        tv_region.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showRegionSelector();
            }
        });
        tv_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String estate=et_xiaoguo.getText().toString();
                if(estate!=null)estate=estate.trim();
                progressDialog = ProgressDialog.show(FindHomeActivity.this,"","搜索中");
                ApiClient.getEstate(mCity,mZone,estate, new EasyCallBack() {
                    @Override
                    public void onRespon(String res) {
                        LogUtils.logE("getestate",res);
                        progressDialog.dismiss();
                        estateBeans = new Gson().fromJson(res,new TypeToken<List<EstateBean>>(){}.getType());
                        if(estateBeans !=null&& estateBeans.size()>0){
                            showBottomInDialog();
                        }else {
                            MyToast.show(FindHomeActivity.this,"没有找到相关的数据");
                        }
                    }
                });
            }
        });
    }

    private void showRegionSelector() {
        try {
            showRegionSelectDialog();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void initData() {

    }

    private void showBottomInDialog() {
        Dialog dialog= UIUtils.showBottomInDialog(this,R.layout.dialog_estate,UIUtils.dip2PX(180));
        ListView lv_estate=dialog.findViewById(R.id.lv_estate);
        QuickAdapter<EstateBean> adapter=new QuickAdapter<EstateBean>(FindHomeActivity.this,R.layout.item_estate) {
            @Override
            protected void convert(BaseAdapterHelper helper, EstateBean item) {
                helper.setText(R.id.tv_name,item.getName());
                helper.setText(R.id.tv_count,item.getCount()+"个户型");
            }
        };
        lv_estate.setAdapter(adapter);
        adapter.replaceAll(estateBeans);
        lv_estate.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent(FindHomeActivity.this,FindHomeListActivity.class);
                intent.putExtra(Constance.xiaoqu,estateBeans.get(i).getName());
                intent.putExtra(Constance.city,mCity);
                intent.putExtra(Constance.area,mZone);
                startActivity(intent);
            }
        });

    }

    private void showRegionSelectDialog() throws IOException {
        regionSelectDialog = new RegionSelectDialog(this, RegionLevel.LEVEL_THREE);
        new Thread(){
            @Override
            public void run() {
                super.run();
                Response response= ApiClient.getRegionSync("0");
                String result= null;
                try {
                    result = response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                JSONObject jsonObject=new JSONObject(result);
                if(jsonObject!=null){
                    JSONArray array=jsonObject.getJSONArray(Constance.data);
                    if(array!=null&&array.length()>0){
                        provinceRegionBeans = new Gson().fromJson(array.toString(),new TypeToken<List<RegionBean>>(){}.getType());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                regionSelectDialog.setOnRegionDataSetListenr(new OnRegionDataSetListener() {

                                    @Override
                                    public List<RegionBean> setProvinceList() {
                                        return provinceRegionBeans;
                                    }

                                    @Override
                                    public List<RegionBean> setOnProvinceSelected(RegionBean regionBean) {
                                        mProvince = regionBean.getName();
                                        mProvinceId = regionBean.getId();
                                        String res= null;
                                        try {
                                            res = ApiClient.getRegionSync(regionBean.getId()).body().string();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                        JSONObject jsonObject=new JSONObject(res);
                                        if(jsonObject!=null){
                                            JSONArray array=jsonObject.getJSONArray(Constance.data);
                                            if(array!=null&&array.length()>0) {
                                                cityRegionBeans = new Gson().fromJson(array.toString(), new TypeToken<List<RegionBean>>() {}.getType());
                                            }
                                        }
                                        return cityRegionBeans;
                                    }

                                    @Override
                                    public List<RegionBean> setOnCitySelected(RegionBean regionBean) {
                                        mCity = regionBean.getName();
                                        mCityId = regionBean.getId();

                                        String res= null;
                                        try {
                                            res = ApiClient.getRegionSync(regionBean.getId()).body().string();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                        JSONObject jsonObject=new JSONObject(res);
                                        if(jsonObject!=null){
                                            JSONArray array=jsonObject.getJSONArray(Constance.data);
                                            if(array!=null&&array.length()>0) {
                                                zoneRegionBeans = new Gson().fromJson(array.toString(), new TypeToken<List<RegionBean>>() {}.getType());
                                            }
                                        }
                                        return zoneRegionBeans;
                                    }

                                    @Override
                                    public List<RegionBean> setOnZoneSelected(RegionBean regionBean) {
                                        mZone = regionBean.getName();
                                        mZoneId = regionBean.getId();
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                tv_region.setText(mProvince+" "+mCity+" "+mZone);
                                            }
                                        });
                                        return null;
                                    }

                                    @Override
                                    public void setOnAreaSelected(RegionBean regionBean) {

                                    }
                                });
                                regionSelectDialog.showDialog();
                            }
                        });
                    }
                }
            }
        }.start();

    }

}
