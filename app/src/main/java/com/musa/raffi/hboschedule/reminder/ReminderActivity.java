package com.musa.raffi.hboschedule.reminder;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.musa.raffi.hboschedule.R;
import com.musa.raffi.hboschedule.models.scheduledb.DataManager;
import com.musa.raffi.hboschedule.reminder.adapter.ItemAdapter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;

import static android.content.ContentValues.TAG;

public class ReminderActivity extends AppCompatActivity implements ReminderViewInterface, ItemAdapter.ReminderClickListener{
    private DataManager dataManager;
    private ReminderPresenter mPresenter;
    private ItemAdapter mAdapter;
    Calendar calendar;
    String dateNow, timeNow;

    @Bind(R.id.recycler_view) RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);
        getSupportActionBar().setTitle("Your Schedule Reminder");
        ButterKnife.bind(this);
        dataManager = new DataManager(ReminderActivity.this);
        mPresenter = new ReminderPresenter(this, dataManager);

        calendar = Calendar.getInstance();
        dateNow = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        timeNow = new SimpleDateFormat("HH:mm:ss").format(new Date());

        configView();

        Log.d(TAG, "onCreate: ReminderActivity");
    }

    private void configView(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new ItemAdapter(this);
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.onResume();
        mPresenter.fetchReminder();
    }

    @Override
    public void onCompleted() {
        Toast.makeText(this, "Load reminder successfully", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onError(String message) {
        Log.d(TAG, "jsonError: " + message);
    }

    @Override
    public void onReminder(Cursor cursor) {
        if(cursor.getCount() == 0) Log.d(TAG, "onReminder: null");
        mAdapter.swapCursor(cursor);
    }

    @Override
    public Observable<Cursor> getReminder() {
        return dataManager.getScheduleRemindRx(dateNow, timeNow);
    }

    @Override
    public void onClick(int idSchedule) {
        Toast.makeText(this, "You choose " + idSchedule, Toast.LENGTH_SHORT).show();
        mPresenter.unSetSchedule(idSchedule);
        mPresenter.fetchReminder();
        mAdapter.notifyDataSetChanged();
    }
}
