package com.musa.raffi.hboschedule.schedule;

import com.musa.raffi.hboschedule.BasePresenterImpl;
import com.musa.raffi.hboschedule.models.schedule.ScheduleList;

import rx.Observable;
import rx.Observer;

/**
 * Created by Asus on 9/8/2016.
 */

public class SchedulePresenter extends BasePresenterImpl implements Observer<ScheduleList> {
    private ScheduleViewInterface mScheduleInterface;

    public SchedulePresenter(ScheduleViewInterface viewInterface){
        mScheduleInterface = viewInterface;
    }

    @Override
    public void onCompleted() {
        mScheduleInterface.onCompleted();
    }

    @Override
    public void onError(Throwable e) {
        mScheduleInterface.onError(e.getMessage());
    }

    @Override
    public void onNext(ScheduleList scheduleList) {
        mScheduleInterface.onScheduleList(scheduleList);
    }

    public void fetchSchedules(){
        unSubscribeAll();
        subscribe(mScheduleInterface.getSchedule(), SchedulePresenter.this);
    }
}
