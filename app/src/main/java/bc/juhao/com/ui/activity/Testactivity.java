package bc.juhao.com.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import bc.juhao.com.R;


/**
 * @author: Jun
 * @date : 2017/3/24 11:37
 * @description :
 */
public class Testactivity extends AppCompatActivity {

    private ViewPager viewPager;
    private LinearLayout point_group;

    private String[] imageUrl={"http://www.aktsc.com/data/afficheimg/1494232754369708811.JPG",
            "http://www.aktsc.com/data/afficheimg/1494232674950577532.JPG",
            "http://www.aktsc.com/data/afficheimg/1494212332348977512.jpg"};
    // 图片标题集合
    private final String[] imageDescriptions = {"第一张图片",
            "第二张图片", "第三张图片"};

    private ArrayList<ImageView> imageList;
    // 上一个页面的位置
    protected int lastPosition = 0;

    // 判断是否自动滚动viewPager
    private boolean isRunning = true;
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            // 执行滑动到下一个页面
            viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
            if (isRunning) {
                // 在发一个handler延时
                handler.sendEmptyMessageDelayed(0, 5000);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test1);

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        point_group = (LinearLayout) findViewById(R.id.point_group);

        // 初始化图片资源
        imageList = new ArrayList<ImageView>();
        for (int i=0;i<imageUrl.length;i++) {
            // 初始化图片资源
            ImageView imageView = new ImageView(this);
            ImageLoader.getInstance().displayImage(imageUrl[i], imageView);
            //            imageView.setBackgroundResource(i);
            imageList.add(imageView);

            // 添加指示小点
            ImageView point = new ImageView(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(25,
                    25);
            params.rightMargin = 20;
            params.bottomMargin = 10;
            point.setLayoutParams(params);
            point.setBackgroundResource(R.drawable.guide_star);
            if (i == 0) {
                //默认聚焦在第一张
                point.setBackgroundResource(R.drawable.guide_yuan);
                point.setEnabled(true);
            } else {
                point.setEnabled(false);
            }

            point_group.addView(point);
        }

        viewPager.setAdapter(new MyPageAdapter());
        // 设置当前viewPager的位置
        viewPager.setCurrentItem(Integer.MAX_VALUE / 2
                - (Integer.MAX_VALUE / 2 % imageList.size()));
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                // 页面切换后调用， position是新的页面位置

                // 实现无限制循环播放
                position %= imageList.size();

                // 把当前点设置为true,将上一个点设为false；并设置point_group图标
                point_group.getChildAt(position).setEnabled(true);
                point_group.getChildAt(position).setBackgroundResource(R.drawable.guide_star);//设置聚焦时的图标样式
                point_group.getChildAt(lastPosition).setEnabled(false);
                point_group.getChildAt(lastPosition).setBackgroundResource(R.drawable.guide_yuan);//上一张恢复原有图标
                lastPosition = position;

            }

            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {
                // 页面正在滑动时间回调

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                // 当pageView 状态发生改变的时候，回调

            }
        });

        /**
         * 自动循环： 1.定时器：Timer 2.开子线程：while true循环 3.ClockManger
         * 4.用Handler发送延时信息，实现循环，最简单最方便
         *
         */

        handler.sendEmptyMessageDelayed(0, 3000);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        /*int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }*/

        switch (item.getItemId()) {
            case 0:
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT, "分享");
                intent.putExtra(Intent.EXTRA_TEXT, "I would like to share this with you...");
                startActivity(Intent.createChooser(intent, getTitle()));
                return true;
        }

        //        return super.onOptionsItemSelected(item);
        return false;
    }

    @Override
    protected void onDestroy() {
        // 停止滚动
        isRunning = false;
        super.onDestroy();
    }

    private class MyPageAdapter extends PagerAdapter {
        // 需要实现以下四个方法

        @Override
        public int getCount() {
            // 获得页面的总数
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            // 判断view和Object对应是否有关联关系
            return view==object;
        }

        // 1、返回要显示的条目内容，创建条目
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            //对Viewpager页号求模去除View列表中要显示的项
            position %= imageList.size();
            if (position<0) {
                position = imageList.size() + position;
            }
            ImageView view = imageList.get(position);
            //如果View已经在之前添加到了一个父组件，则必须先remove，否则会抛出IllegalStateException。

            ViewParent viewParent = view.getParent();
            if (viewParent!=null){
                ViewGroup parent = (ViewGroup)viewParent;
                parent.removeView(view);
            }
            container.addView(view);


            return view;
        }

        // 2、销毁条目
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            // object 要销毁的对象
//            container.removeView((View) object);
        }

    }
}
