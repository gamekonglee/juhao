package bc.juhao.com.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import bc.juhao.com.R;
import bc.juhao.com.cons.Constance;
import bc.juhao.com.ui.adapter.ImageDetailAdapter;
import bocang.view.BaseActivity;


/**
 * @author Jun
 * @time 2016/8/18  11:13
 * @desc ${TODD}
 */
public class DetailPhotoActivity extends BaseActivity {


    private ViewPager vp_photo;
    private RelativeLayout back_ll;
    private TextView indicator;
    List<String> DetailImgS;
    private int position = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //去掉Activity上面的状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_detail_photo);
        vp_photo = (ViewPager) findViewById(R.id.vp_photo);
        getData();
        vp_photo.setAdapter(new ImageDetailAdapter(this, DetailImgS));
        vp_photo.setCurrentItem(position);
        back_ll = (RelativeLayout) findViewById(R.id.back_ll);
        back_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        indicator = (TextView) findViewById(R.id.indicator);

        String values = position + 1 + "/" + DetailImgS.size();
        indicator.setText(values);
        vp_photo.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                String values = position + 1 + "/" + DetailImgS.size();
                indicator.setText(values);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    protected void InitDataView() {

    }

    @Override
    protected void initController() {

    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onViewClick(View v) {

    }

    /**
     * 获取数据
     */
    public void getData() {
        Intent intent = getIntent();
        DetailImgS = (List<String>) intent.getSerializableExtra(Constance.IMAGESHOW);
        position = intent.getIntExtra(Constance.IMAGEPOSITION, 0);
    }
}
