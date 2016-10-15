package com.musa.raffi.hboschedule;

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
import android.view.Menu;
import android.view.MenuItem;

import com.musa.raffi.hboschedule.reminder.ReminderActivity;
import com.musa.raffi.hboschedule.reminder.ReminderFragment;
import com.musa.raffi.hboschedule.schedule.PagesFragment;
import com.musa.raffi.hboschedule.settings.SettingsFragment;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    @Bind(R.id.nav_view) NavigationView navigationView;
    @Bind(R.id.drawer_layout) DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        configToolbarDrawer();
        getFragmentManager().beginTransaction().replace(R.id.fragmentHolder, new PagesFragment()).commit();
    }

    private void configToolbarDrawer() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }

    public void setActionBatTitle(String title){
        getSupportActionBar().setTitle(title);
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
            getFragmentManager().beginTransaction().replace(R.id.fragmentHolder, new PagesFragment()).commit();
        } else if (id == R.id.reminder) {
//            Intent intent = new Intent(this, ReminderActivity.class);
//            startActivity(intent);
            getFragmentManager().beginTransaction().replace(R.id.fragmentHolder, new ReminderFragment()).commit();
        } else if (id == R.id.settings) {
            getFragmentManager().beginTransaction().replace(R.id.fragmentHolder, new SettingsFragment()).commit();
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
