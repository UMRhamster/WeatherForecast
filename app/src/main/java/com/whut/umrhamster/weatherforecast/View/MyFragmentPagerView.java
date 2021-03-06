package com.whut.umrhamster.weatherforecast.View;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * Created by 12421 on 2018/6/13.
 */

public class MyFragmentPagerView extends FragmentStatePagerAdapter {
    private List<FragmentWeather> fragmentList;
    public MyFragmentPagerView(FragmentManager fm,List<FragmentWeather> fragmentList) {
        super(fm);
        this.fragmentList = fragmentList;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}
