package com.musa.raffi.hboschedule.reminder;

import android.database.Cursor;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ListView;
import android.widget.Toast;

import com.musa.raffi.hboschedule.R;
import com.musa.raffi.hboschedule.models.channel.Channel;
import com.musa.raffi.hboschedule.models.scheduledb.DataManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ReminderActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{
    private DataManager dataManager;
    private Cursor mCursor;
    private ItemAdapter mAdapter;
    private static final int REMINDER_LOADER = 0;
    Calendar calendar;
    String dateNow, timeNow;

    @Bind(R.id.recycler_view) RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);
        getSupportActionBar().setTitle("Your Schedule Reminder");
        ButterKnife.bind(this);

        calendar = Calendar.getInstance();
        dateNow = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        timeNow = new SimpleDateFormat("kk:mm:ss").format(new Date());

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        dataManager = new DataManager(ReminderActivity.this);

        mAdapter = new ItemAdapter(this);
        recyclerView.setAdapter(mAdapter);
//        Toast.makeText(this,"Reminder oncreate",Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getLoaderManager().initLoader(REMINDER_LOADER, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id){
            case REMINDER_LOADER:
                return new CursorLoader(this){
                    @Override
                    public Cursor loadInBackground() {
                        try {
                            return dataManager.getScheduleRemind(dateNow, timeNow);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }
                };
            default:
                throw new UnsupportedOperationException("Unknown loader id: " + id);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        switch (loader.getId()){
            case REMINDER_LOADER:
                mAdapter.swapCursor(data);
                break;
            default:
                throw new UnsupportedOperationException("Unknown loader id: " + loader.getId());
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        switch (loader.getId()){
            case REMINDER_LOADER:
                mAdapter.swapCursor(null);
                break;
            default:
                throw new UnsupportedOperationException("Unknown loader id: " + loader.getId());
        }
    }
}
