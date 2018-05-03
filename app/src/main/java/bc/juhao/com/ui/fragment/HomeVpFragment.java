package bc.juhao.com.ui.fragment;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

import astuetz.MyPagerSlidingTabStrip;
import bc.juhao.com.R;
import bc.juhao.com.common.BaseFragment;
import bc.juhao.com.ui.activity.ChartListActivity;
import bc.juhao.com.ui.activity.product.SelectGoodsActivity;
import bc.juhao.com.ui.activity.user.SimpleScannerActivity;
import bc.juhao.com.ui.fragment.home.DesginerHomeFragment;
import bc.juhao.com.ui.fragment.home.HomeFragment;
import bc.juhao.com.ui.fragment.home.JuHaoMkFragment;
import bc.juhao.com.ui.fragment.home.NewsHomeFragment;
import bc.juhao.com.ui.fragment.home.TimeBuyFragment;
import bc.juhao.com.ui.fragment.home.VideoHomeFragment;
import bc.juhao.com.utils.UIUtils;
import bocang.utils.IntentUtil;

/**
 * Created by gamekonglee on 2018/4/16.
 */

public class HomeVpFragment extends BaseFragment implements View.OnClickListener {

    private MyPagerSlidingTabStrip pagerSlidingTabStrip;
    private ViewPager vp_home;
    private String[] titles;
    private ArrayList<android.support.v4.app.Fragment> fragmentArrayList;
    public TextView unMessageTv;
    private TextView topLeftBtn;
    private TextView topRightBtn;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        ((MainActivity)getActivity()).setStatuTextColor(getActivity(), Color.WHITE);
        return inflater.inflate(R.layout.fm_home_vp_new, null);
    }
    @Override
    protected void initController() {

    }

    @Override
    protected void initViewData() {

    }

    @Override
    protected void initView() {
        pagerSlidingTabStrip = getView().findViewById(R.id.tabs);
        vp_home = getView().findViewById(R.id.main_viewpager);
        unMessageTv = (TextView) getView().findViewById(R.id.unMessageTv);
        EditText et_search = (EditText) getView().findViewById(R.id.et_search);
        topLeftBtn = getView().findViewById(R.id.topLeftBtn);
        topRightBtn = getView().findViewById(R.id.topRightBtn);
        et_search.setOnClickListener(this);
        topLeftBtn.setOnClickListener(this);
        topRightBtn.setOnClickListener(this);
        titles = UIUtils.getStringArr(R.array.home_titles);
        pagerSlidingTabStrip.selectColor=(getActivity().getResources().getColor(R.color.green));
        pagerSlidingTabStrip.defaultColor=getActivity().getResources().getColor(R.color.txt_black);
        pagerSlidingTabStrip.setDividerColor(Color.TRANSPARENT);
        pagerSlidingTabStrip.setIndicatorColor(getActivity().getResources().getColor(R.color.green));
        pagerSlidingTabStrip.setUnderlineColor(getActivity().getResources().getColor(R.color.goods_details_sku));
        pagerSlidingTabStrip.setUnderlineHeight(1);
        fragmentArrayList = new ArrayList<>();
        HomeFragment homeFragment=new HomeFragment();
        NewsHomeFragment newsHomeFragment=new NewsHomeFragment();
        VideoHomeFragment videoHomeFragment=new VideoHomeFragment();
        TimeBuyFragment timeBuyFragment=new TimeBuyFragment();
        JuHaoMkFragment juHaoMkFragment=new JuHaoMkFragment();
        DesginerHomeFragment desginerHomeFragment=new DesginerHomeFragment();
        fragmentArrayList.add(homeFragment);
        fragmentArrayList.add(newsHomeFragment);
        fragmentArrayList.add(videoHomeFragment);
        fragmentArrayList.add(timeBuyFragment);
        fragmentArrayList.add(juHaoMkFragment);
        fragmentArrayList.add(desginerHomeFragment);
        vp_home.setAdapter(new PagerHomeAdapter(getActivity().getSupportFragmentManager(),fragmentArrayList));
        pagerSlidingTabStrip.setViewPager(vp_home);
        MyPageChangeListener listener=new MyPageChangeListener();
        pagerSlidingTabStrip.setOnPageChangeListener(listener);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.topLeftBtn://扫描二维码
                IntentUtil.startActivity(this.getActivity(), SimpleScannerActivity.class, false);
                break;
            case R.id.topRightBtn://消息
                if (!isToken()) {
                    IntentUtil.startActivity(this.getActivity(), ChartListActivity.class, false);
                }
                break;
            case R.id.et_search://搜索产品
                IntentUtil.startActivity(this.getActivity(), SelectGoodsActivity.class, false);
                break;}
    }

    class PagerHomeAdapter extends FragmentPagerAdapter{

        private final ArrayList<android.support.v4.app.Fragment> mList;

        public PagerHomeAdapter(FragmentManager fm, ArrayList<android.support.v4.app.Fragment> list) {
            super(fm);
            mList = list;
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            android.support.v4.app.Fragment page=null;
            if(position<mList.size()){
                page=mList.get(position);
                if(page!=null)return page;}
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
    @Override
    protected void initData() {

    }
    class MyPageChangeListener implements ViewPager.OnPageChangeListener{
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            //当选择后才进行获取数据，而不是预加载
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }
}
