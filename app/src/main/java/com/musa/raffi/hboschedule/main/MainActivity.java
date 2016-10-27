package com.musa.raffi.hboschedule.main;

import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.musa.raffi.hboschedule.R;
import com.musa.raffi.hboschedule.models.scheduledb.DataManager;
import com.musa.raffi.hboschedule.models.scheduledb.Schedule;
import com.musa.raffi.hboschedule.reminder.ReminderFragment;
import com.musa.raffi.hboschedule.schedule.ViewPagerFragment;
import com.musa.raffi.hboschedule.settings.SettingsFragment;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        GoogleApiClient.OnConnectionFailedListener, MainActivityInterface {

    @Bind(R.id.nav_view) NavigationView navigationView;
    @Bind(R.id.drawer_layout) DrawerLayout drawer;
    ImageView mPhoto;
    TextView mUsername;
    TextView mEmail;
    String username = "";
    String email = "";

    public static final String scheduleFirebase = "schedule";
    private MainActivityPresenter mPresenter;
    private static final int RC_SIGN_IN = 9001;
    private static final String TAG = "SignInActivity";
    private DatabaseReference mFirebaseDatabaseReference;
    private DataManager dataManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        dataManager = new DataManager(this);

        initFirebaseAuth();
        initFirebaseDb();
        configToolbarDrawer();
        getFragmentManager().beginTransaction().replace(R.id.fragmentHolder, new ViewPagerFragment()).commit();
    }

    private void initFirebaseDb() {
        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
        Cursor itemReminder = dataManager.getScheduleRemindFirebase();

        Schedule schedule = new Schedule();
        for(itemReminder.moveToFirst(); !itemReminder.isAfterLast(); itemReminder.moveToNext()){
            int idSchedule = itemReminder.getInt(itemReminder.getColumnIndexOrThrow(DataManager.TABLE_ROW_ID));

            schedule.setId(idSchedule);
            schedule.setChannel(itemReminder.getString(itemReminder.getColumnIndexOrThrow(DataManager.TABLE_ROW_CHANNEL)));
            schedule.setDate(itemReminder.getString(itemReminder.getColumnIndexOrThrow(DataManager.TABLE_ROW_DATE)));
            schedule.setFilmName(itemReminder.getString(itemReminder.getColumnIndexOrThrow(DataManager.TABLE_ROW_FILM_NAME)));
            schedule.setFilmPlot("null");
            schedule.setShowTime(itemReminder.getString(itemReminder.getColumnIndexOrThrow(DataManager.TABLE_ROW_SHOW_TIME)));
            schedule.setReminder(1);

            mFirebaseDatabaseReference.child(scheduleFirebase)
                    .push().setValue(schedule);
        }
        itemReminder.close();
    }

    private void initFirebaseAuth(){
        mPresenter = new MainActivityPresenter(this, this, this, this);
        username = mPresenter.getDIsplayName();
        email = mPresenter.getEmail();
    }

    private void configToolbarDrawer() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        updateNavHeader();
    }

    private void updateNavHeader(){
        View header = navigationView.getHeaderView(0);

        mPhoto = (ImageView) header.findViewById(R.id.imageView);
        mUsername = (TextView) header.findViewById(R.id.userName);
        mEmail = (TextView) header.findViewById(R.id.textView);

        mUsername.setText(username);
        mEmail.setText(email);
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
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        Fragment fragment = null;
        switch (id){
            case R.id.schedule:
                fragment = new ViewPagerFragment();
                getFragmentManager().beginTransaction().replace(R.id.fragmentHolder, fragment).commit();
                break;
            case R.id.reminder:
                fragment = new ReminderFragment();
                getFragmentManager().beginTransaction().replace(R.id.fragmentHolder, fragment).commit();
                break;
            case R.id.settings:
                fragment = new SettingsFragment();
                getFragmentManager().beginTransaction().replace(R.id.fragmentHolder, fragment).commit();
                break;
            case R.id.nav_log_in:
                signIn();
                break;
            case R.id.nav_log_out:
                signOut();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void signIn() {
        Intent signInIntent = mPresenter.signIn();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void signOut() {
        mPresenter.signOut();
        mUsername.setText(getString(R.string.login_not_logged));
        mEmail.setText("");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            mPresenter.doLogIn(data);
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCompleteAuth(boolean isSuccess) {
        if(isSuccess) {
            username = mPresenter.getDIsplayName();
            email = mPresenter.getEmail();
            updateNavHeader();
        } else {
            Toast.makeText(MainActivity.this, "Authentication failed.",
                    Toast.LENGTH_SHORT).show();
        }
    }
}
