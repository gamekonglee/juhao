package bc.juhao.com.ui.activity.intelligence;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;

import com.google.gson.Gson;
import com.zhy.http.okhttp.callback.Callback;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import bc.juhao.com.R;
import bc.juhao.com.adapter.BaseAdapterHelper;
import bc.juhao.com.adapter.QuickAdapter;
import bc.juhao.com.bean.CountDownBean;
import bc.juhao.com.common.BaseActivity;
import bc.juhao.com.cons.Constance;
import bc.juhao.com.net.ApiClient;
import bc.juhao.com.ui.view.EndOfListView;
import bc.juhao.com.ui.view.PMSwipeRefreshLayout;
import bocang.json.JSONArray;
import bocang.json.JSONObject;
import bocang.utils.LogUtils;
import bocang.utils.MyToast;
import bocang.utils.UIUtils;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by gamekonglee on 2018/8/7.
 */

public class CountDownListActivity extends BaseActivity implements  SwipeRefreshLayout.OnRefreshListener {

    private String iotid;
    private QuickAdapter<CountDownBean> adapter;
    private List<CountDownBean> countDownBeans;
    private PMSwipeRefreshLayout pullToRef;
    private String type;

    @Override
    protected void InitDataView() {

    }

    @Override
    protected void initController() {

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void initView() {
        setColor(this, Color.WHITE);
        setContentView(R.layout.activity_count_down_list);
        pullToRef = findViewById(R.id.pullToRefresh);
        EndOfListView listView=findViewById(R.id.lv_count_down);
        Button btn_add_count_down=findViewById(R.id.btn_add_count_down);
        pullToRef.setColorSchemeColors(Color.RED,Color.GREEN, Color.YELLOW);
        pullToRef.setRefreshing(false);
        adapter = new QuickAdapter<CountDownBean>(this, R.layout.item_count_down) {
            @Override
            protected void convert(BaseAdapterHelper helper, final CountDownBean item) {
                String hour=item.getHour()+"";
                if(hour.length()<2){
                    hour="0"+hour;
                }
                String minute=item.getMinute()+"";
                if(minute.length()<2){
                    minute="0"+minute;
                }
                helper.setText(R.id.tv_time,hour+":"+minute);
                String[] weeks=item.getWeeks().split(",");
                String str="";
                if(weeks.length>0){
                    for(int i=0;i<weeks.length;i++){
                        if(weeks[i].equals("0")){
                            str="仅限一次,";
                            break;
                        }
                        if(weeks.length==7){
                            str="每天,";
                            break;
                        }
                        if(weeks[i].equals("1")){
                            str+="星期一，";
                        }
                        if(weeks[i].equals("2")){
                            str+="星期二，";
                        }
                        if(weeks[i].equals("3")){
                            str+="星期三，";
                        }
                        if(weeks[i].equals("4")){
                            str+="星期四，";
                        }
                        if(weeks[i].equals("5")){
                            str+="星期五，";
                        }
                        if(weeks[i].equals("6")){
                            str+="星期六，";
                        }
                        if(weeks[i].equals("7")){
                            str+="星期日，";
                        }
                    }
                }else {
                    str="仅限一次,";
                }
                helper.setText(R.id.tv_weeks,str.substring(0,str.length()-1));
                if(item.getStatus()==1){
                    helper.setImageResource(R.id.iv_status,R.mipmap.home_icon_k);
                    helper.setText(R.id.tv_status,"开关：开启");
                }else {
                    helper.setImageResource(R.id.iv_status,R.mipmap.home_icon_g);
                    helper.setText(R.id.tv_status,"开关：关闭");
                }
                helper.setOnClickListener(R.id.rl_status, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int status=item.getStatus()==1?0:1;
                        ApiClient.IotTimerUpdate(item.getPid(),item.getIotid(),item.getItems(),"["+item.getWeeks()+"]",item.getHour()+"",item.getMinute()+"",status, new Callback<String>() {
                            @Override
                            public String parseNetworkResponse(Response response, int id) throws Exception {
                                return null;
                            }

                            @Override
                            public void onError(Call call, Exception e, int id) {

                            }

                            @Override
                            public String onResponse(String response, int id) {
                                Log.e("IotUpdateTimer",response);
                                try {
                                    org.json.JSONObject jsonObject1=new org.json.JSONObject(response);
                                    if(jsonObject1.getBoolean(Constance.success)){
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                           countDownBeans=new ArrayList<>();
                                           listCoundDown();
                                        }
                                    });
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                return null;
                            }
                        });
                    }
                });
            }
        };
        listView.setAdapter(adapter);
        countDownBeans = new ArrayList<>();
        type = getIntent().getStringExtra(Constance.type);
        adapter.replaceAll(countDownBeans);
//        listView.setOnEndOfListListener(this);
        pullToRef.setOnRefreshListener(this);

        btn_add_count_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(CountDownListActivity.this,CountDownAddActivity.class);
                intent.putExtra(Constance.iotId,iotid);
                intent.putExtra(Constance.type,type);
                startActivityForResult(intent,200);
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(CountDownListActivity.this,CountDownAddActivity.class);
                String countJson=new Gson().toJson(countDownBeans.get(position),CountDownBean.class);
                intent.putExtra(Constance.count_down_json,countJson);
                intent.putExtra(Constance.iotId,iotid);
                intent.putExtra(Constance.type,type);
                startActivityForResult(intent,200);
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                UIUtils.showSingleWordDialog(CountDownListActivity.this, "确定要删除此定时吗？", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ApiClient.IotTimerDelete(countDownBeans.get(position).getPid(),countDownBeans.get(position).getIotid(), new Callback<String>() {
                            @Override
                            public String parseNetworkResponse(Response response, int id) throws Exception {
                                return null;
                            }

                            @Override
                            public void onError(Call call, Exception e, int id) {

                            }

                            @Override
                            public String onResponse(String response, int id) {
                                LogUtils.logE("IotTimerDelete",response);
                                try {
                                    org.json.JSONObject jsonObject1=new org.json.JSONObject(response);
                                    if(jsonObject1.getBoolean(Constance.success)){
                                        MyToast.show(CountDownListActivity.this,"删除成功");
                                        countDownBeans=new ArrayList<>();
                                        listCoundDown();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                return null;
                            }
                        });
                    }
                });
                return true;
            }
        });

    }
    Handler handler=new Handler();
    private void listCoundDown() {

        ApiClient.IotTimerList(iotid, new Callback<String>() {
            @Override
            public String parseNetworkResponse(Response response, int id) throws Exception {
                return null;
            }

            @Override
            public void onError(Call call, Exception e, int id) {
            pullToRef.setRefreshing(false);
            }

            @Override
            public String onResponse(String response, int id) {
                pullToRef.setRefreshing(false);
                LogUtils.logE("TimerList",response);
                try{
                    org.json.JSONObject jsonObject=new org.json.JSONObject(response);
                    org.json.JSONArray array=jsonObject.getJSONArray(Constance.success);
                    if(array==null||array.length()==0){
                        //没有记录
                    }else {
                        for(int i=0;i<array.length();i++){
                            countDownBeans.add(new Gson().fromJson(array.getJSONObject(i).toString(),CountDownBean.class));
                        }
                        adapter.replaceAll(countDownBeans);
                    }
                }catch (Exception e){
                    //解析失败，或没有记录
                }

                return null;
            }
        });

    }

    @Override
    protected void initData() {
        iotid = getIntent().getStringExtra(Constance.iotId);

    }
//
//    @Override
//    public void onEndOfList(Object lastItem) {
//
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if((resultCode==200||resultCode==300)&&requestCode==200){
            setResult(resultCode,data);
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        countDownBeans=new ArrayList<>();
        listCoundDown();
    }

    @Override
    public void onRefresh() {
        countDownBeans=new ArrayList<>();
        listCoundDown();
    }
}
