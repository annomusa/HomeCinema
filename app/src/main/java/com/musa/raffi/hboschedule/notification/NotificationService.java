package com.musa.raffi.hboschedule.notification;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.IBinder;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;

import com.musa.raffi.hboschedule.R;
import com.musa.raffi.hboschedule.reminder.ReminderActivity;

/**
 * Created by Asus on 10/3/2016.
 */

public class NotificationService extends IntentService {

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * name Used to name the worker thread, important only for debugging.
     */
    public NotificationService() {
        super("Notification Service");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String title = intent.getStringExtra("title");
        String time = intent.getStringExtra("time");
        String channel = intent.getStringExtra("channel");
        int idSchedule = intent.getIntExtra("idSchedule", 0);
        Log.d("asdf", "onStartCommand: " + idSchedule);

        NotificationManager mManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        Intent mIntent = new Intent(this, ReminderActivity.class);
        mIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                this.getApplicationContext(), 0, mIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isSetVibrate = preferences.getBoolean(getString(R.string.settings_perform_vibrate), true);
        boolean isSetSound = preferences.getBoolean(getString(R.string.settings_perform_sound), true);

        Notification notification = new Notification.Builder(getApplicationContext())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(channel + " at " + time)
                .setContentText(title)
                .setContentIntent(pendingIntent)
                .setVibrate(isSetVibrate ? new long[]{1000, 1000, 1000, 1000} : new long[]{0})
                .setLights(Color.YELLOW, 1500, 1500)
                .setSound(isSetSound ? RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION) : null)
                .build();

        mManager.notify(idSchedule, notification);
    }
}
