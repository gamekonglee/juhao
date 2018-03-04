package bc.juhao.com.ui.adapter;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;

import java.util.ArrayList;

import bc.juhao.com.ui.fragment.OrderFragment;


/**
 * Created by _H_JY on 2015/11/23.
 */


public class FragmentVPAdapter extends FragmentPagerAdapter {
    private ArrayList<OrderFragment> fragments;
    private FragmentManager fm;
    private String[] mTitleArrs;
    public FragmentVPAdapter(FragmentManager fm, ArrayList<OrderFragment> fragments,String[] titleArrs) {
        super(fm);
        this.fm = fm;
        this.fragments = fragments;
        this.mTitleArrs=titleArrs;
    }

    public void setFragments(ArrayList<OrderFragment> fragments) {
        if(this.fragments != null){
            FragmentTransaction ft = fm.beginTransaction();
            for(Fragment f:this.fragments){
                ft.remove(f);
            }
            ft.commit();
            ft=null;
            fm.executePendingTransactions();
        }
        this.fragments = fragments;
        notifyDataSetChanged();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position>mTitleArrs.length){
            return "暂未加载";
        }
        return mTitleArrs[position];
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public Fragment getItem(int arg0) {
        return fragments.get(arg0);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}