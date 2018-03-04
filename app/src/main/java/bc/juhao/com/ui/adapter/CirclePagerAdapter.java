package bc.juhao.com.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 描述
 *
 * @author Jun
 * @time 2016/7/28 13:28
 */
public class CirclePagerAdapter extends FragmentPagerAdapter {
    private ArrayList<Fragment> list;
    private List<String> refresh_title;
    public CirclePagerAdapter(FragmentManager fm, ArrayList<Fragment> list, List<String> refresh_title) {
        super(fm);
        this.list=list;
        this.refresh_title=refresh_title;
    }
    public CirclePagerAdapter(FragmentManager fm, ArrayList<Fragment> list, String[] refresh_title) {
        super(fm);
        this.list=list;
        this.refresh_title= Arrays.asList(refresh_title);
    }

    @Override
    public Fragment getItem(int position) {
        return list.get(position);

    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position>refresh_title.size()){
            return "暂未加载";
        }
        return refresh_title.get(position);
    }
}
