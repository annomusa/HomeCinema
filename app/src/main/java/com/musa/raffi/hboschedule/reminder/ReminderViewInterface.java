package com.musa.raffi.hboschedule.reminder;

import android.database.Cursor;

import rx.Observable;

/**
 * Created by Asus on 9/23/2016.
 */

public interface ReminderViewInterface {
    void onCompleted();
    void onError(String message);
    void onReminder(Cursor cursor);
    Observable<Cursor> getReminder();
}
