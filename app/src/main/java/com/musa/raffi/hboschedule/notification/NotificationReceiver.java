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

public class NotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent notifIntent = new Intent(context, NotificationService.class);
        notifIntent.putExtra("title", intent.getStringExtra("title"));
        notifIntent.putExtra("time", intent.getStringExtra("time"));
        notifIntent.putExtra("channel", intent.getStringExtra("channel"));
        context.startService(notifIntent);
//        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
//        PendingIntent pendingIntent = PendingIntent.getActivity(context, (int) System.currentTimeMillis(), intents, 0);
//
//        String title = intent.getStringExtra("title");
//        String text = intent.getStringExtra("text");
//        String channel = intent.getStringExtra("channel");
//
//        Notification notification = new Notification.Builder(context)
//                .setSmallIcon(R.mipmap.ic_launcher)
//                .setContentTitle(channel + " " + text + " Now Playing")
//                .setContentText(title)
//                .setContentIntent(pendingIntent)
//                .build();
//
//        notificationManager.notify(0,notification);
    }
}
