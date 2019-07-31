package bc.juhao.com.ui.activity.intelligence;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.zhy.http.okhttp.callback.Callback;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import bc.juhao.com.R;
import bc.juhao.com.adapter.BaseAdapterHelper;
import bc.juhao.com.adapter.QuickAdapter;
import bc.juhao.com.bean.CountDownBean;
import bc.juhao.com.common.BaseActivity;
import bc.juhao.com.cons.Constance;
import bc.juhao.com.net.ApiClient;
import bc.juhao.com.ui.view.CustomDatePicker;
import bocang.utils.MyToast;
import bocang.utils.UIUtils;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by gamekonglee on 2018/8/6.
 */

public class CountDownAddActivity extends BaseActivity implements View.OnClickListener {

    private CustomDatePicker customDatePicker1;
    private TextView tv_current_time;
    private TextView tv_current_repeat;
    private TextView tv_current_switch;
    private String days;
    private int PowerSwitch=1;
    private int PowerSwitch_1=1;
    private int PowerSwitch_2=1;
    private int PowerSwitch_3=1;
    private int PowerSwitch_4=1;
    private int LightSwitch=1;
    private int ColorTemperature=5000;
    private TextView tv_save;
    private CountDownBean countDownBean;
    private TextView tv_count_down_add;
    private boolean isEdit;
    private LinearLayout ll_switch_all;
    private LinearLayout ll_switch_1;
    private LinearLayout ll_switch_2;
    private LinearLayout ll_switch_3;
    private LinearLayout ll_switch_4;
    private LinearLayout ll_seek;
    private LinearLayout ll_switch;
    private String type;
    private TextView tv_current_switch_1;
    private TextView tv_current_switch_2;
    private TextView tv_current_switch_3;
    private TextView tv_current_switch_4;
    private SeekBar seekBar;
    private String iotId;

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
        setContentView(R.layout.activity_count_down);
        LinearLayout ll_time=findViewById(R.id.ll_time);
        LinearLayout ll_repeat=findViewById(R.id.ll_repeat);
        ll_switch = findViewById(R.id.ll_switch);
        tv_current_time = findViewById(R.id.tv_current_time);
        tv_current_repeat = findViewById(R.id.tv_current_repeat);
        tv_current_switch = findViewById(R.id.tv_current_switch);
        tv_save = findViewById(R.id.tv_save);
        tv_count_down_add = findViewById(R.id.tv_countdown_add);
        ll_switch_all = findViewById(R.id.ll_switch_night);
        ll_switch_1 = findViewById(R.id.ll_switch_1);
        ll_switch_2 = findViewById(R.id.ll_switch_2);
        ll_switch_3 = findViewById(R.id.ll_switch_3);
        ll_switch_4 = findViewById(R.id.ll_switch_4);
        tv_current_switch_1 = findViewById(R.id.tv_current_switch_1);
        tv_current_switch_2 = findViewById(R.id.tv_current_switch_2);
        tv_current_switch_3 = findViewById(R.id.tv_current_switch_3);
        tv_current_switch_4 = findViewById(R.id.tv_current_switch_4);
        ll_seek = findViewById(R.id.ll_seek);
        seekBar = findViewById(R.id.seekb_night);
        seekBar.setMax(6000);
        seekBar.setProgress(4000);
        type = getIntent().getStringExtra(Constance.type);
                if(type.equals(Constance.socket)){
                    ll_switch_all.setVisibility(View.GONE);
                    ll_seek.setVisibility(View.GONE);
                }else if(type.equals(Constance.nightswitch)){
                    ll_switch_all.setVisibility(View.VISIBLE);
                    ll_seek.setVisibility(View.GONE);
                    ll_switch.setVisibility(View.GONE);
                }else if(type.equals(Constance.night)){
                    ll_switch.setVisibility(View.VISIBLE);
                    ll_seek.setVisibility(View.VISIBLE);
                    ll_switch_all.setVisibility(View.GONE);
                }
        ll_switch_1.setOnClickListener(this);
        ll_time.setOnClickListener(this);
        ll_repeat.setOnClickListener(this);
        ll_switch.setOnClickListener(this);
        ll_switch_1.setOnClickListener(this);
        ll_switch_2.setOnClickListener(this);
        ll_switch_3.setOnClickListener(this);
        ll_switch_4.setOnClickListener(this);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                ColorTemperature=progress+1000;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        days="[0]";
        tv_save.setOnClickListener(this);
        isEdit=false;
        iotId = getIntent().getStringExtra(Constance.iotId);
        if(getIntent().hasExtra(Constance.count_down_json)){
            countDownBean = new Gson().fromJson(getIntent().getStringExtra(Constance.count_down_json),CountDownBean.class);
            isEdit = true;
            days="["+countDownBean.getWeeks()+"]";
            tv_count_down_add.setText("编辑定时");
            String  hour=countDownBean.getHour()+"";
            String minute=countDownBean.getMinute()+"";
            if(hour.length()<2){
                hour="0"+hour;
            }
            if(minute.length()<2){
                minute="0"+minute;
            }
            tv_current_time.setText(hour+":"+minute);
            String[] weeks=countDownBean.getWeeks().split(",");
            String str="星期";
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
                        str+="一，";
                    }
                    if(weeks[i].equals("2")){
                        str+="二，";
                    }
                    if(weeks[i].equals("3")){
                        str+="三，";
                    }
                    if(weeks[i].equals("4")){
                        str+="四，";
                    }
                    if(weeks[i].equals("5")){
                        str+="五，";
                    }
                    if(weeks[i].equals("6")){
                        str+="六，";
                    }
                    if(weeks[i].equals("7")){
                        str+="日，";
                    }
                }
            }else {
                str="仅限一次,";
            }
            tv_current_repeat.setText(str.substring(0,str.length()-1));
            try {
                JSONObject items=new JSONObject(countDownBean.getItems());
                if(type.equals(Constance.socket)){
                    PowerSwitch=items.getInt(Constance.PowerSwitch);
                    tv_current_switch.setText(PowerSwitch==1?"开启":"关闭");
                }else if(type.equals(Constance.night)){
                    LightSwitch=items.getInt(Constance.LightSwitch);
                    ColorTemperature=items.getInt(Constance.ColorTemperature);
                    tv_current_switch.setText(LightSwitch==1?"开启":"关闭");
                    seekBar.setProgress(ColorTemperature-1000);
                }else if(type.equals(Constance.nightswitch)){
                    PowerSwitch_1=items.getInt(Constance.PowerSwitch_1);
                    PowerSwitch_2=items.getInt(Constance.PowerSwitch_2);
                    PowerSwitch_3=items.getInt(Constance.PowerSwitch_3);
                    PowerSwitch_4=items.getInt(Constance.PowerSwitch_4);
                    tv_current_switch_1.setText(PowerSwitch_1==1?"开启":"关闭");
                    tv_current_switch_2.setText(PowerSwitch_2==1?"开启":"关闭");
                    tv_current_switch_3.setText(PowerSwitch_3==1?"开启":"关闭");
                    tv_current_switch_4.setText(PowerSwitch_4==1?"开启":"关闭");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }

    private void showSwitchDialog(final int position) {
        final Dialog dialog=new Dialog(this,R.style.buttom_dialog);
        dialog.setContentView(R.layout.dialog_swich);
        TextView tv_open=dialog.findViewById(R.id.tv_open);
        TextView tv_close=dialog.findViewById(R.id.tv_close);
        tv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if(type.equals(Constance.socket)){
                    PowerSwitch=0;
                    tv_current_switch.setText("关闭");
                }else if(type.equals(Constance.night)){
                    LightSwitch=0;
                    tv_current_switch.setText("关闭");
                    ll_seek.setVisibility(View.GONE);
                }else if(type.equals(Constance.nightswitch)){
                    switch (position){
                        case 1:
                            PowerSwitch_1=0;
                            tv_current_switch_1.setText("关闭");
                            break;
                        case 2:
                            PowerSwitch_2=0;
                            tv_current_switch_2.setText("关闭");
                            break;
                        case 3:
                            PowerSwitch_3=0;
                            tv_current_switch_3.setText("关闭");
                            break;
                        case 4:
                            PowerSwitch_4=0;
                            tv_current_switch_4.setText("关闭");
                            break;
                    }
                }

            }
        });
        tv_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if(type.equals(Constance.socket)){
                    PowerSwitch=1;
                    tv_current_switch.setText("开启");
                }else if(type.equals(Constance.night)){
                    LightSwitch=1;
                    tv_current_switch.setText("开启");
                    ll_seek.setVisibility(View.GONE);
                }else if(type.equals(Constance.nightswitch)){
                    switch (position){
                        case 1:
                            PowerSwitch_1=1;
                            tv_current_switch_1.setText("开启");
                            break;
                        case 2:
                            PowerSwitch_2=1;
                            tv_current_switch_2.setText("开启");
                            break;
                        case 3:
                            PowerSwitch_3=1;
                            tv_current_switch_3.setText("开启");
                            break;
                        case 4:
                            PowerSwitch_4=1;
                            tv_current_switch_4.setText("开启");
                            break;
                    }
                }
            }
        });
        dialog.setCancelable(false);//点击外部不可dismiss
        dialog.setCanceledOnTouchOutside(false);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.setWindowAnimations(R.style.BottomDialog_Animation);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = UIUtils.dip2PX(101);
        window.setAttributes(params);
        dialog.show();

    }

    @Override
    protected void initData() {

    }

    private void initDatePicker() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
        String now = sdf.format(new Date());
//        currentDate.setText(now.split(" ")[0]);
//        currentTime.setText(now);
        // 回调接口，获得选中的时间
//                currentDate.setText(time.split(" ")[0]);
// 初始化日期格式请用：yyyy-MM-dd HH:mm，否则不能正常运行
        customDatePicker1 = new CustomDatePicker(this, new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) { // 回调接口，获得选中的时间
//                currentDate.setText(time.split(" ")[0]);
                tv_current_time.setText(""+time.substring(time.length()-5));

            }
        }, "2018-08-07 00:00", "2018-08-08 23:59");
        customDatePicker1.showSpecificTime(true); // 不显示时和分
        customDatePicker1.setIsLoop(true); // 不允许循环滚动

//        customDatePicker2 = new CustomDatePicker(this, new CustomDatePicker.ResultHandler() {
//            @Override
//            public void handle(String time) { // 回调接口，获得选中的时间
//                currentTime.setText(time);
//            }
//        }, "2010-01-01 00:00", now); // 初始化日期格式请用：yyyy-MM-dd HH:mm，否则不能正常运行
//        customDatePicker2.showSpecificTime(true); // 显示时和分
//        customDatePicker2.setIsLoop(true); // 允许循环滚动
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==200){
            if(data!=null){
                days = data.getStringExtra(Constance.days);
                if(days.contains("0")){
                    tv_current_repeat.setText("仅限一次");
                }else {
                    String str="";
                    if(days.contains("1")){
                        str+="一,";
                    }
                    if(days.contains("2")){
                        str+="二,";
                    }
                    if(days.contains("3")){
                        str+="三,";
                    }
                    if(days.contains("4")){
                        str+="四,";
                    }
                    if(days.contains("5")){
                        str+="五,";
                    }
                    if(days.contains("6")){
                        str+="六,";
                    }
                    if(days.contains("7")){
                        str+="日,";
                    }
                    str="每星期"+str;
                    str=str.substring(0,str.length()-1);
                    tv_current_repeat.setText(str);
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_switch_1:
                showSwitchDialog(1);
                break;
            case R.id.ll_switch_2:
                showSwitchDialog(2);
                break;
            case R.id.ll_switch_3:
                showSwitchDialog(3);
                break;
            case R.id.ll_switch_4:
                showSwitchDialog(4);
                break;
            case R.id.ll_time:
                initDatePicker();
                if(isEdit){
                    customDatePicker1.show("2018-08-07 "+countDownBean.getHour()+":"+countDownBean.getMinute());
                }else {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
                    String now = sdf.format(new Date());
                    customDatePicker1.show(now);
                }
                break;
            case R.id.ll_repeat:
                Intent intent=new Intent(CountDownAddActivity.this,RepeatSettingActivity.class);
                if(days!=null){
                    intent.putExtra(Constance.days,days);
                }
                startActivityForResult(intent,200);
                break;
            case R.id.ll_switch:
                showSwitchDialog(0);
                break;
            case R.id.tv_save:

//                Intent intentCountDownBean=new Intent();
//                intentCountDownBean.putExtra(Constance.days,days);
//                intentCountDownBean.putExtra(Constance.status,1);
//                intentCountDownBean.putExtra(Constance.hour,tv_current_time.getText().toString().substring(0,2));
//                intentCountDownBean.putExtra(Constance.minute,tv_current_time.getText().toString().substring(3));
//                if(isEdit){
//                    intentCountDownBean.putExtra(Constance.count_down_json,new Gson().toJson(countDownBean,CountDownBean.class));
//                    setResult(300,intentCountDownBean);
//                }else {
//                    setResult(200,intentCountDownBean);
//                }
                String hour=tv_current_time.getText().toString().substring(0,2);
                String minute=tv_current_time.getText().toString().substring(3);
                JSONObject jsonObject=new JSONObject();
                try {
                if(type.equals(Constance.socket)){
                    jsonObject.put(Constance.PowerSwitch,PowerSwitch);
                }else if(type.equals(Constance.nightswitch)){
                    jsonObject.put(Constance.PowerSwitch_1,PowerSwitch_1);
                    jsonObject.put(Constance.PowerSwitch_2,PowerSwitch_2);
                    jsonObject.put(Constance.PowerSwitch_3,PowerSwitch_3);
                    jsonObject.put(Constance.PowerSwitch_4,PowerSwitch_4);
                }else if(type.equals(Constance.night)){
                    jsonObject.put(Constance.LightSwitch,LightSwitch);
                    ColorTemperature=(int)(ColorTemperature/500)*500;
                    if(LightSwitch==1){
                    jsonObject.put(Constance.ColorTemperature,ColorTemperature);
                    }
                }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                int status=1;

                if(!isEdit){
                    Log.e("params:",iotId+","+days+","+hour+","+minute+","+status+","+jsonObject.toString());
                    ApiClient.IotCreatTimer(iotId,days,hour,minute,status,jsonObject.toString(), new Callback<String>() {
                        @Override
                        public String parseNetworkResponse(Response response, int id) throws Exception {
                            return null;
                        }

                        @Override
                        public void onError(Call call, Exception e, int id) {

                        }

                        @Override
                        public String onResponse(String response, int id) {
                            Log.e("IotCreatTimer",response);
                            try {
                                JSONObject jsonObject1=new JSONObject(response);
                                if(jsonObject1.getBoolean(Constance.success)){
                                    MyToast.show(CountDownAddActivity.this,"添加成功");
                                    finish();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            return null;
                        }
                    });
                }else{
                    status=countDownBean.getStatus();
                    ApiClient.IotTimerUpdate(countDownBean.getPid(),iotId,jsonObject.toString(),days,hour,minute,status, new Callback<String>() {
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
                                JSONObject jsonObject1=new JSONObject(response);
                                if(jsonObject1.getBoolean(Constance.success)){
                                    MyToast.show(CountDownAddActivity.this,"修改成功");
                                    finish();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            return null;
                        }
                    });
                }
                break;
        }
    }
}
