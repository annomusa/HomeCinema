package com.musa.raffi.hboschedule.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

import com.musa.raffi.hboschedule.R;
import com.musa.raffi.hboschedule.reminder.ReminderActivity;

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
}
