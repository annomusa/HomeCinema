package com.musa.raffi.hboschedule.notification;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.widget.Toast;

import com.musa.raffi.hboschedule.Utility;
import com.musa.raffi.hboschedule.models.scheduledb.DataManager;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Asus on 10/4/2016.
 */

public class NotificationBootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            Calendar calendar = Calendar.getInstance();
            String dateNow = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(calendar.getTime());
            String timeNow = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH).format(calendar.getTime());

            DataManager dataManager = new DataManager(context);
            try (Cursor c = dataManager.getScheduleRemind(dateNow, timeNow)) {
                while (c.moveToNext()) {
                    int idSchedule = c.getInt(c.getColumnIndexOrThrow(DataManager.TABLE_ROW_ID));
                    String title = c.getString(c.getColumnIndexOrThrow(DataManager.TABLE_ROW_FILM_NAME));
                    String time = c.getString(c.getColumnIndexOrThrow(DataManager.TABLE_ROW_SHOW_TIME));
                    String date = c.getString(c.getColumnIndexOrThrow(DataManager.TABLE_ROW_DATE));
                    String channel = c.getString(c.getColumnIndexOrThrow(DataManager.TABLE_ROW_CHANNEL));
                    Calendar notifTime = Utility.getCalender(date, time);
                    NotificationReceiver.setNotification(idSchedule, title, time, channel, notifTime, context);
                }
            }

        }
    }


}
