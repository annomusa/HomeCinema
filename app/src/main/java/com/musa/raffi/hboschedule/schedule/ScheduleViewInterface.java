package com.musa.raffi.hboschedule.schedule;

import android.database.Cursor;

import com.musa.raffi.hboschedule.models.schedulepojo.ScheduleList;

import rx.Observable;

/**
 * Created by Asus on 9/8/2016.
 */

public interface ScheduleViewInterface {
    void jsonCompleted();

    void jsonError(String message);

    void jsonScheduleList(ScheduleList scheduleList);

    Observable<ScheduleList> getSchedule();

    void dbCompleted();

    void dbError(String message);

    void dbScheduleList(Cursor cursor);

    Observable<Cursor> getCursor();
}
