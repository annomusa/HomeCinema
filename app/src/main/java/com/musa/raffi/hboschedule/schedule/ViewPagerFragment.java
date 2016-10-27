package com.musa.raffi.hboschedule.schedule;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.musa.raffi.hboschedule.main.MainActivity;
import com.musa.raffi.hboschedule.R;
import com.musa.raffi.hboschedule.schedule.adapter.ScheduleFragmentPagerAdapter;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Asus on 10/15/2016.
 */

public class ViewPagerFragment extends Fragment {
    private static final int NUM_PAGES = 5;
    PagerAdapter mPagerAdapter;

    @Bind(R.id.pager) ViewPager mPager;
    @Bind(R.id.sliding_tabs) TabLayout mTabLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_pager_fragment, container, false);
        ButterKnife.bind(this, view);

        configViewPager();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).setActionBatTitle(getString(R.string.schedule));
    }

    private void configViewPager() {
        setHasOptionsMenu(true);
        mPagerAdapter = new ScheduleFragmentPagerAdapter(getFragmentManager(), getActivity().getApplicationContext(), NUM_PAGES);
        mPager.setOffscreenPageLimit(5);
        mPager.setAdapter(mPagerAdapter);
        mTabLayout.setupWithViewPager(mPager);
        mPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position) {
                getActivity().invalidateOptionsMenu();
            }
        });
    }
}
