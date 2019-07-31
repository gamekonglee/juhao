package bc.juhao.com.ui.activity.intelligence;

import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import bc.juhao.com.R;
import bc.juhao.com.adapter.BaseAdapterHelper;
import bc.juhao.com.adapter.QuickAdapter;
import bc.juhao.com.common.BaseActivity;
import bc.juhao.com.cons.Constance;
import bocang.json.JSONArray;
import bocang.json.JSONObject;

/**
 * Created by gamekonglee on 2018/8/7.
 */

public class RepeatSettingActivity extends BaseActivity {

    private ListView lv_day;
    private boolean[] isSelected;
    private String days;

    @Override
    protected void InitDataView() {

    }

    @Override
    protected void initController() {

    }

    @Override
    protected void initView() {
        setColor(this, Color.WHITE);
        setContentView(R.layout.activity_repeat_setting);
        lv_day = findViewById(R.id.lv_day);
        final QuickAdapter adapter=new QuickAdapter<String>(this,R.layout.item_day) {
            @Override
            protected void convert(BaseAdapterHelper helper, String item) {
            if(isSelected[helper.getPosition()]){
                helper.setImageResource(R.id.iv_selected,R.mipmap.yhq_qd_sel);
            }else {
                helper.setImageResource(R.id.iv_selected,R.mipmap.yhq_qd_nor);
            }
            helper.setText(R.id.tv_day,item);
            }
        };
        isSelected = new boolean[7];
        if(getIntent().hasExtra(Constance.days)){
            days = getIntent().getStringExtra(Constance.days);
            days=days.substring(1,days.length()-1);
            String[] daysArray=days.split(",");
            if(!days.contains("0")){
            for(int i=0;i<daysArray.length;i++){
                isSelected[Integer.parseInt(daysArray[i])-1]=true;
            }
            }
        }
        lv_day.setAdapter(adapter);
        List<String> days=new ArrayList<>();
        days.add("星期一");
        days.add("星期二");
        days.add("星期三");
        days.add("星期四");
        days.add("星期五");
        days.add("星期六");
        days.add("星期天");
        adapter.replaceAll(days);

        lv_day.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    isSelected[position]=!isSelected[position];
                    adapter.notifyDataSetChanged();
            }
        });

    }

    @Override
    public void goBack(View v) {
//        JSONArray jsonArray=new JSONArray();
        String days="";
        for(int i=0;i<7;i++){
            if(isSelected[i]){
                int y=i+1;
                days+=y;
                days+=",";
            }
        }
        if(TextUtils.isEmpty(days)){
            days="0,";
        }
        days=days.substring(0,days.length()-1);
        days="["+days+"]";
        Intent intent=new Intent();
        intent.putExtra(Constance.days,days);
        setResult(200,intent);
        super.goBack(v);
    }

    @Override
    protected void initData() {

    }
}
