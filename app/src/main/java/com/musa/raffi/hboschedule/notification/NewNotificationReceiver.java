package com.musa.raffi.hboschedule.notification;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

import java.util.Calendar;

/**
 * Created by Asus on 10/4/2016.
 */

public class NewNotificationReceiver extends WakefulBroadcastReceiver {
    private AlarmManager alarmMgr;
    private PendingIntent notifIntent;
    String mTitle, mTime, mChannel;
    Calendar mCalendar;

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent service = new Intent(context, NewNotificationService.class);
        service.putExtra("title", mTitle);
        service.putExtra("time", mTime);
        service.putExtra("channel", mChannel);

        startWakefulService(context, service);
    }

    public void setNotification(Context context, int idSchedule, String title, String time, String channel, Calendar calendar){
        alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, NewNotificationReceiver.class);
        notifIntent = PendingIntent.getBroadcast(context, idSchedule, intent, 0);
        mTitle = title;
        mTime = time;
        mChannel = channel;

        alarmMgr.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), notifIntent);
    }

    public void cancelNotification(Context context, int idSchedule){
        Intent intent = new Intent(context, NewNotificationReceiver.class);
        PendingIntent cancelIntent = PendingIntent.getActivity(context, idSchedule, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        if(alarmMgr != null){
            alarmMgr.cancel(cancelIntent);
        }
    }
}
