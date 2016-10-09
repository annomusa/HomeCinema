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
                    setAlarm(context, idSchedule, title, date, time, channel);
                }
            }
        }
    }

    private void setAlarm(Context context, int idSchedule, String title, String date, String time, String channel){
        Intent notifIntent = new Intent(context, NotificationReceiver.class);
        notifIntent.putExtra("title", title);
        notifIntent.putExtra("time", time);
        notifIntent.putExtra("channel", channel);
        notifIntent.putExtra("idSchedule", idSchedule);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, idSchedule, notifIntent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Calendar notifTime = getCalender(date, time);
        alarmManager.set(AlarmManager.RTC, notifTime.getTimeInMillis(), pendingIntent);

        ComponentName receiver = new ComponentName(context, NotificationBootReceiver.class);
        PackageManager pm = context.getPackageManager();
        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
    }

    Calendar getCalender(String date, String time) {
        Calendar result = Calendar.getInstance();
        String dateString = date + " " + time;

        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        Date mDate = null;
        try {
            mDate = format.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        result.setTime(mDate);
        return result;
    }
}
