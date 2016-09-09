package com.musa.raffi.hboschedule.schedule;

import com.musa.raffi.hboschedule.models.schedule.ScheduleList;

import rx.Observable;

/**
 * Created by Asus on 9/8/2016.
 */

public interface ScheduleViewInterface {
    void onCompleted();

    void onError(String message);

    void onScheduleList(ScheduleList scheduleList);

     Observable<ScheduleList> getSchedule();
}
