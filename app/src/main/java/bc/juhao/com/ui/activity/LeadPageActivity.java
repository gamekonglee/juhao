package bc.juhao.com.ui.activity;

import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import java.util.ArrayList;

import bc.juhao.com.R;
import bc.juhao.com.cons.Constance;
import bc.juhao.com.utils.MyShare;
import bocang.utils.IntentUtil;
import bocang.view.BaseActivity;

/**
 * @author Jun
 * @time 2016/11/27  20:15
 * @desc ${TODD}
 */
public class LeadPageActivity extends BaseActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {
    private ViewPager mViewPager;
    private ImageView mPage0;
    private ImageView mPage1;
//    private ImageView mPage2;
//    private ImageView mPage2;
//    private ImageView mPage3;
    private Button mStartBtn;

    private int currIndex = 0;
    private ArrayList<ImageView> mViews;

    @Override
    protected void InitDataView() {

    }

    @Override
    protected void initController() {

    }

    @Override
    protected void initView() {
        //去除title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //去掉Activity上面的状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_lead_page);
        mViewPager = (ViewPager)findViewById(R.id.whatsnew_viewpager);
        mViewPager.setOnPageChangeListener(new MyOnPageChangeListener());

        mPage0 = (ImageView)findViewById(R.id.page0);
        mPage1 = (ImageView)findViewById(R.id.page1);
//        mPage2 = (ImageView)findViewById(R.id.page2);
        mStartBtn = (Button)findViewById(R.id.startBtn);
        mStartBtn.setOnClickListener(this);

        //将要分页显示的View装入数组中
        LayoutInflater mLi = LayoutInflater.from(this);

        //每个页面的view数据
        mViews = new ArrayList<ImageView>();
        ImageView iv=new ImageView(this);
        iv.setImageResource(R.drawable.guide_bg01);
        ImageView iv2=new ImageView(this);
        iv2.setImageResource(R.drawable.guide_bg02);
//        ImageView iv3=new ImageView(this);
//        iv3.setImageResource(R.drawable.guide_bg03);
        iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
        iv2.setScaleType(ImageView.ScaleType.CENTER_CROP);
//        iv3.setScaleType(ImageView.ScaleType.CENTER_CROP);
        mViews.add(iv);
        mViews.add(iv2);
//        mViews.add(iv3);
        //填充ViewPager的数据适配器
        PagerAdapter mPagerAdapter = new PagerAdapter() {

            @Override
            public boolean isViewFromObject(View arg0, Object arg1) {
                return arg0 == arg1;
            }

            @Override
            public int getCount() {
                return mViews.size();
            }

            @Override
            public void destroyItem(View container, int position, Object object) {
                ((ViewPager)container).removeView(mViews.get(position));
            }



            @Override
            public Object instantiateItem(View container, int position) {
                ((ViewPager)container).addView(mViews.get(position));
                return mViews.get(position);
            }
        };

        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.addOnPageChangeListener(this);
    }

    @Override
    public void onClick(View v) {
        MyShare.get(this).putBoolean(Constance.ISFIRSTISTART, true);
        IntentUtil.startActivity(this, MainActivity.class, true);
    }

    @Override
    protected void onViewClick(View v) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if(position==mViews.size()-1){
            mStartBtn.setVisibility(View.VISIBLE);
        }else{
            mStartBtn.setVisibility(View.GONE);
        }
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageSelected(int arg0) {
            switch (arg0) {
                case 0:
                    mPage0.setImageDrawable(getResources().getDrawable(R.drawable.guide_star));
                    mPage1.setImageDrawable(getResources().getDrawable(R.drawable.guide_yuan));
//                    mPage2.setImageDrawable(getResources().getDrawable(R.drawable.guide_yuan));
                    break;
                case 1:
                    mPage0.setImageDrawable(getResources().getDrawable(R.drawable.guide_yuan));
                    mPage1.setImageDrawable(getResources().getDrawable(R.drawable.guide_star));
//                    mPage2.setImageDrawable(getResources().getDrawable(R.drawable.guide_yuan));
                    break;
//                case 2:
//                    mPage0.setImageDrawable(getResources().getDrawable(R.drawable.guide_yuan));
//                    mPage1.setImageDrawable(getResources().getDrawable(R.drawable.guide_yuan));
////                    mPage2.setImageDrawable(getResources().getDrawable(R.drawable.guide_star));
//                    break;
            }
            currIndex = arg0;
        }
        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
    }
    //跳转页面
    public void startbutton(View v) {
        Intent intent = new Intent();
        intent.setClass(LeadPageActivity.this,MainActivity.class);
        startActivity(intent);
        this.finish();
    }

    @Override
    protected void initData() {

    }
}
