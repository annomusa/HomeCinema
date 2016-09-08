package com.musa.raffi.hboschedule.schedule;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.support.v13.app.FragmentPagerAdapter;

/**
 * Created by Asus on 9/8/2016.
 */

public class SampleFragmentPagerAdapter extends FragmentPagerAdapter {
    private String tabTitles[] = new String[] { "HBO HD", "HBO Sign", "HBO Fam", "HBO HITS", "Cinemax" };
    private Context context;
    int mTotalPage;

    public SampleFragmentPagerAdapter(FragmentManager fm, Context context, int totalPage) {
        super(fm);
        this.context = context;
        mTotalPage = totalPage;
    }

    @Override
    public int getCount() {
        return mTotalPage;
    }

    @Override
    public Fragment getItem(int position) {
        return PageFragment.newInstance(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }
}

