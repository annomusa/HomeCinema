package com.musa.raffi.hboschedule.reminder;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.musa.raffi.hboschedule.R;
import com.musa.raffi.hboschedule.models.Channel;
import com.musa.raffi.hboschedule.models.ChannelList;
import com.musa.raffi.hboschedule.models.DataManager;
import com.musa.raffi.hboschedule.schedule.MainActivity;
import com.musa.raffi.hboschedule.schedule.ScheduleCursorAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ReminderActivity extends AppCompatActivity {
    List<Channel> channelList;
    private ScheduleReminderCursorAdapter scheduleAdapter;
    private DataManager dataManager;
    private Cursor mCursor;

    @Bind(R.id.list_reminder)
    ListView listSchedule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);
        getSupportActionBar().setTitle("Your Schedule Reminder");
        ButterKnife.bind(this);

        dataManager = new DataManager(ReminderActivity.this);
        mCursor = dataManager.getScheduleRemind();
        scheduleAdapter = new ScheduleReminderCursorAdapter(ReminderActivity.this, mCursor);
        listSchedule.setAdapter(scheduleAdapter);
    }
}
