package com.musa.raffi.hboschedule.schedule.adapter;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.support.v13.app.FragmentPagerAdapter;

import com.musa.raffi.hboschedule.schedule.PageFragment;

/**
 * Created by Asus on 9/8/2016.
 */

public class ScheduleFragmentPagerAdapter extends FragmentPagerAdapter {
    private String tabTitles[] = new String[] { "HBO HD", "HBO Sign", "HBO Fam", "HBO HITS", "Cinemax" };
    private Context context;
    int mTotalPage;

    public ScheduleFragmentPagerAdapter(FragmentManager fm, Context context, int totalPage) {
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

