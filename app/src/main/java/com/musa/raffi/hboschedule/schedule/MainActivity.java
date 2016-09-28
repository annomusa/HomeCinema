package com.musa.raffi.hboschedule.schedule;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.musa.raffi.hboschedule.Application.App;
import com.musa.raffi.hboschedule.R;
import com.musa.raffi.hboschedule.models.channel.SingletonChannelList;
import com.musa.raffi.hboschedule.reminder.ReminderActivity;
import com.musa.raffi.hboschedule.schedule.adapter.ScheduleFragmentPagerAdapter;
import com.musa.raffi.hboschedule.service.RestApi;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final int NUM_PAGES = 5;
    PagerAdapter mPagerAdapter;

    @Bind(R.id.nav_view) NavigationView navigationView;
    @Bind(R.id.drawer_layout) DrawerLayout drawer;
    @Bind(R.id.pager) ViewPager mPager;
    @Bind(R.id.sliding_tabs) TabLayout mTabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        configViewAdapter();
    }

    private void configViewAdapter() {
        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new ScheduleFragmentPagerAdapter(getFragmentManager(), MainActivity.this, NUM_PAGES);
        mPager.setAdapter(mPagerAdapter);
        mTabLayout.setupWithViewPager(mPager);

        mPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position) {
                String title = SingletonChannelList.getInstance().getChannel(position).getName();
                getSupportActionBar().setTitle(title);
                invalidateOptionsMenu();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.schedule) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (id == R.id.reminder) {
            Intent intent = new Intent(this, ReminderActivity.class);
            startActivity(intent);
        } else if (id == R.id.settings) {

        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
