package bc.juhao.com.ui.activity.intelligence;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import bc.juhao.com.R;
import bc.juhao.com.common.BaseActivity;


public class NoticeActivity extends BaseActivity implements View.OnClickListener {

    private int position;
    private TextView tv_device;
    private TextView tv_share;
    private TextView tv_notice;

    @Override
    protected void InitDataView() {

    }

    @Override
    protected void initController() {

    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_notice);
        tv_device = findViewById(R.id.tv_device);
        tv_share = findViewById(R.id.tv_share);
        tv_notice = findViewById(R.id.tv_notice);
        tv_device.setOnClickListener(this);
        tv_share.setOnClickListener(this);
        tv_notice.setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_device:
                position=0;
                break;
            case R.id.tv_share:
                position=1;
                break;
            case R.id.tv_notice:
                position=2;
                break;
        }
        refreshUI();
    }

    private void refreshUI() {
        tv_device.setBackgroundResource(R.drawable.bg_corner_empty_orange_15_left);
        tv_share.setBackgroundResource(R.drawable.bg_theme_empty);
        tv_notice.setBackgroundResource(R.drawable.bg_corner_empty_orange_15_right);
        tv_device.setTextColor(getResources().getColor(R.color.theme));
        tv_share.setTextColor(getResources().getColor(R.color.theme));
        tv_notice.setTextColor(getResources().getColor(R.color.theme));

        if(position==0){
            tv_device.setBackgroundResource(R.drawable.bg_corner_full_orange_15_left);
            tv_device.setTextColor(Color.WHITE);
        }else if(position==1){
            tv_share.setBackgroundColor(getResources().getColor(R.color.theme));
            tv_share.setTextColor(Color.WHITE);
        }else {
            tv_notice.setBackgroundResource(R.drawable.bg_corner_full_orange_15_right);
            tv_notice.setTextColor(Color.WHITE);
        }
    }
}
