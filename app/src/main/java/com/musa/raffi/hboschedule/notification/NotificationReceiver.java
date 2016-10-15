package com.musa.raffi.hboschedule.notification;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.content.WakefulBroadcastReceiver;

import com.musa.raffi.hboschedule.R;
import com.musa.raffi.hboschedule.reminder.ReminderActivity;

import java.util.Calendar;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by Asus on 9/19/2016.
 */

public class NotificationReceiver extends WakefulBroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent notifService = new Intent(context, NotificationService.class);
        notifService.putExtra("title", intent.getStringExtra("title"));
        notifService.putExtra("time", intent.getStringExtra("time"));
        notifService.putExtra("channel", intent.getStringExtra("channel"));
        notifService.putExtra("idSchedule", intent.getIntExtra("idSchedule", 0));
        startWakefulService(context, notifService);
    }

    public static void setNotification(int idSchedule, String title, String time, String channel, Calendar calendar, Context ctx){
        Intent notifIntent = new Intent(ctx, NotificationReceiver.class);
        notifIntent.putExtra("title", title);
        notifIntent.putExtra("time", time);
        notifIntent.putExtra("channel", channel);
        notifIntent.putExtra("idSchedule", idSchedule);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(ctx, idSchedule, notifIntent, 0);
        AlarmManager alarmManager = (AlarmManager) ctx.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC, calendar.getTimeInMillis(), pendingIntent);

        ComponentName receiver = new ComponentName(ctx, NotificationBootReceiver.class);
        PackageManager pm = ctx.getPackageManager();
        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
    }

    public static void cancelNotification(int idSchedule, Context mContext) {
        Intent intent = new Intent(mContext, NotificationReceiver.class);
        PendingIntent cancelIntent = PendingIntent.getBroadcast(mContext, idSchedule, intent, 0);
        AlarmManager alarmMgr = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        alarmMgr.cancel(cancelIntent);

        ComponentName receiver = new ComponentName(mContext, NotificationBootReceiver.class);
        PackageManager pm = mContext.getPackageManager();
        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
    }
}
