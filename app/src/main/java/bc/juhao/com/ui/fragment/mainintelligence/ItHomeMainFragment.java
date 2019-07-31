package bc.juhao.com.ui.fragment.mainintelligence;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import astuetz.MyPagerSlidingTabStrip;
import bc.juhao.com.R;
import bc.juhao.com.common.BaseFragment;
import bc.juhao.com.ui.activity.intelligence.ItDeviceAddActivity;
import bc.juhao.com.ui.view.EndOfListView;

/**
 * Created by gamekonglee on 2018/7/7.
 */

public class ItHomeMainFragment extends BaseFragment {

    private MyPagerSlidingTabStrip tabs;
    private ViewPager vp_it;
    private String[] titles;

    @Override
    protected void initController() {

    }

    @Override
    protected void initViewData() {

    }

    @Override
    protected void initView() {
        tabs = getView().findViewById(R.id.tabs);
        vp_it = getView().findViewById(R.id.vp_it);
        vp_it.setAdapter(new MyPagerAdapter());
        tabs.defaultColor=getActivity().getResources().getColor(R.color.txt_black);
        tabs.selectColor=getActivity().getResources().getColor(R.color.txt_black);
        tabs.setUnderlineColor(Color.TRANSPARENT);
        tabs.setIndicatorColor(getActivity().getResources().getColor(R.color.green));
        tabs.setDividerColor(Color.TRANSPARENT);

        tabs.setViewPager(vp_it);

    }

    @Override
    protected void initData() {
        titles = new String[]{"所有设备","客厅"};
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_it_main_home,null);
    }
    class MyPagerAdapter extends PagerAdapter {

        private View view;

        @Override
        public int getCount() {
            return titles.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
//            if(view==null)
                view = View.inflate(getContext(), R.layout.view_it_home,null);
            ListView lv_it=view.findViewById(R.id.lv_it);
            Button btn_add_device=view.findViewById(R.id.btn_add_device);
            btn_add_device.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                startActivity(new Intent(getContext(), ItDeviceAddActivity.class));
                }
            });
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container,position,object);
            container.removeView((View) object);
        }
    }
}
