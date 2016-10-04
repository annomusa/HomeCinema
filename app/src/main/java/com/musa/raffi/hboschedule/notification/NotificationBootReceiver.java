package com.musa.raffi.hboschedule.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Asus on 10/4/2016.
 */

public class NotificationBootReceiver extends BroadcastReceiver {
    NewNotificationReceiver notif = new NewNotificationReceiver();

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
//            notif.setNotification(context);
        }
    }
}
