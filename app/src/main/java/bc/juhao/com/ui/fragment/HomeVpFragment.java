package bc.juhao.com.ui.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.acker.simplezxing.activity.CaptureActivity;
import com.aliyun.iot.aep.component.router.Router;
import com.example.qrcode.ScannerActivity;

import java.util.ArrayList;

import astuetz.MyPagerSlidingTabStrip;
import bc.juhao.com.R;
import bc.juhao.com.common.BaseFragment;
import bc.juhao.com.listener.OnTabClickListener;
import bc.juhao.com.ui.activity.ChartListActivity;
import bc.juhao.com.ui.activity.product.SelectGoodsActivity;
import bc.juhao.com.ui.fragment.home.DesginerHomeFragment;
import bc.juhao.com.ui.fragment.home.GoodsFragment;
import bc.juhao.com.ui.fragment.home.HomeFragment;
import bc.juhao.com.ui.fragment.home.JuHaoMkFragment;
import bc.juhao.com.ui.fragment.home.NewsHomeFragment;
import bc.juhao.com.ui.fragment.home.TimeBuyFragment;
import bc.juhao.com.ui.fragment.home.VideoHomeFragment;
import bc.juhao.com.utils.UIUtils;
import bocang.utils.IntentUtil;
import bocang.utils.MyToast;
import bocang.utils.PermissionUtils;

/**
 * Created by gamekonglee on 2018/4/16.
 */

public class HomeVpFragment extends BaseFragment implements View.OnClickListener {

    private MyPagerSlidingTabStrip pagerSlidingTabStrip;
    private ViewPager vp_home;
    public TextView unMessageTv;
    private TextView topLeftBtn;
    private TextView topRightBtn;
    private EditText et_search;

    private String[] titles;//首页ViewPager的title
    private ArrayList<android.support.v4.app.Fragment> fragmentArrayList;//首页ViewPager中的fragment集合
    private static final int RESULT_REQUEST_CODE = 400;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fm_home_vp_new, null);
    }

    @Override
    protected void initController() {

    }

    @Override
    protected void initViewData() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        pagerSlidingTabStrip = getView().findViewById(R.id.tabs);
        vp_home = getView().findViewById(R.id.main_viewpager);
        unMessageTv = (TextView) getView().findViewById(R.id.unMessageTv);
        topLeftBtn = getView().findViewById(R.id.topLeftBtn);
        topRightBtn = getView().findViewById(R.id.topRightBtn);
        et_search = (EditText) getView().findViewById(R.id.et_search);

        topLeftBtn.setOnClickListener(this);
        topRightBtn.setOnClickListener(this);
        et_search.setOnClickListener(this);

        pagerSlidingTabStrip.selectColor = (getActivity().getResources().getColor(R.color.green));
        pagerSlidingTabStrip.defaultColor = getActivity().getResources().getColor(R.color.txt_black);
        pagerSlidingTabStrip.setDividerColor(Color.TRANSPARENT);
        pagerSlidingTabStrip.setIndicatorColor(getActivity().getResources().getColor(R.color.green));
        pagerSlidingTabStrip.setUnderlineColor(getActivity().getResources().getColor(R.color.goods_details_sku));
        pagerSlidingTabStrip.setUnderlineHeight(1);

        titles = UIUtils.getStringArr(R.array.home_titles);
        fragmentArrayList = new ArrayList<>();
        HomeFragment homeFragment = new HomeFragment();
        NewsHomeFragment newsHomeFragment = new NewsHomeFragment();
        final GoodsFragment goodsFragment=new GoodsFragment();
        VideoHomeFragment videoHomeFragment = new VideoHomeFragment();
        TimeBuyFragment timeBuyFragment = new TimeBuyFragment();
        JuHaoMkFragment juHaoMkFragment = new JuHaoMkFragment();
        DesginerHomeFragment desginerHomeFragment = new DesginerHomeFragment();
        fragmentArrayList.add(homeFragment);
        fragmentArrayList.add(newsHomeFragment);
        fragmentArrayList.add(goodsFragment);
        fragmentArrayList.add(videoHomeFragment);
        fragmentArrayList.add(timeBuyFragment);
        fragmentArrayList.add(juHaoMkFragment);
        fragmentArrayList.add(desginerHomeFragment);
        vp_home.setAdapter(new PagerHomeAdapter(getActivity().getSupportFragmentManager(), fragmentArrayList));

        pagerSlidingTabStrip.setViewPager(vp_home);
        MyPageChangeListener listener = new MyPageChangeListener();
        pagerSlidingTabStrip.setOnPageChangeListener(listener);
        pagerSlidingTabStrip.setOnTabClickLisener(new OnTabClickListener() {
            @Override
            public void onTabClick(int position) {
//                MyToast.show(getActivity(),"position:"+position);
                if(position==2){
                    if(goodsFragment!=null&&goodsFragment.mController!=null){
                        goodsFragment.topRightClick();
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.topLeftBtn://扫描二维码
//                IntentUtil.startActivity(this.getActivity(), SimpleScannerActivity.class, false);
//                Intent intent2 = new Intent(getActivity(), ScannerActivity.class);
//                startActivityForResult(intent2, 400);
                PermissionUtils.requestPermission(this.getActivity(), PermissionUtils.CODE_CAMERA, new PermissionUtils.PermissionGrant() {
                    @Override
                    public void onPermissionGranted(int requestCode) {
                        startActivityForResult(new Intent(getActivity(), CaptureActivity.class), CaptureActivity.REQ_CODE);
                    }
                });

//                Router.getInstance().toUrlForResult(getActivity(),"page/scan",RESULT_REQUEST_CODE);
                break;
            case R.id.topRightBtn://消息
                if (!isToken()) {
                    IntentUtil.startActivity(this.getActivity(), ChartListActivity.class, false);
                }
                break;
            case R.id.et_search://搜索产品
                IntentUtil.startActivity(this.getActivity(), SelectGoodsActivity.class, false);
                break;
        }
    }

    class PagerHomeAdapter extends FragmentPagerAdapter {

        private final ArrayList<android.support.v4.app.Fragment> mList;

        public PagerHomeAdapter(FragmentManager fm, ArrayList<android.support.v4.app.Fragment> list) {
            super(fm);
            mList = list;
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            android.support.v4.app.Fragment page = null;
            if (position < mList.size()) {
                page = mList.get(position);
                if (page != null) return page;
            }
            return null;
        }

//        @Override
//        public long getItemId(int position) {
//            return mList.get(position).getId();
//        }
        //
//        @Override
//        public Object instantiateItem(ViewGroup container, int position) {
//            return super.instantiateItem(container, position);
//        }
//
//        @Override
//        public void destroyItem(ViewGroup container, int position, Object object) {
//            container.removeView((android.support.v4.app.Fragment) object);
//        }

        @Override
        public int getCount() {
            return titles.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    }

    class MyPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
//            if(position==2){
//            MyToast.show(getActivity(),"筛选");
//            }
            //当选择后才进行获取数据，而不是预加载
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionUtils.requestPermissionsResult(this.getActivity(), PermissionUtils.CODE_CAMERA, permissions, grantResults, new PermissionUtils.PermissionGrant() {
            @Override
            public void onPermissionGranted(int requestCode) {

                startActivityForResult(new Intent(getActivity(), CaptureActivity.class), CaptureActivity.REQ_CODE);
            }
        });
    }
}
