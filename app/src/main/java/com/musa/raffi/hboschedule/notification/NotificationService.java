package com.musa.raffi.hboschedule.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.musa.raffi.hboschedule.R;
import com.musa.raffi.hboschedule.reminder.ReminderActivity;

/**
 * Created by Asus on 10/3/2016.
 */

public class NotificationService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        String title = intent.getStringExtra("title");
        String time = intent.getStringExtra("time");
        String channel = intent.getStringExtra("channel");
        NotificationManager mManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        Intent mIntent = new Intent(this, ReminderActivity.class);
        mIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                this.getApplicationContext(), 0, mIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new Notification.Builder(getApplicationContext())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(channel + " at " + time)
                .setContentText(title)
                .setContentIntent(pendingIntent)
                .build();

        mManager.notify(0, notification);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
