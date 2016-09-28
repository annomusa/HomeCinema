package com.musa.raffi.hboschedule.schedule;

import com.musa.raffi.hboschedule.BasePresenterImpl;
import com.musa.raffi.hboschedule.models.schedulepojo.ScheduleList;

import rx.Observer;

/**
 * Created by Asus on 9/8/2016.
 */

public class SchedulePresenter extends BasePresenterImpl implements Observer<ScheduleList> {
    private ScheduleViewInterface mInterface;

    public SchedulePresenter(ScheduleViewInterface viewInterface){
        mInterface = viewInterface;
    }

    @Override
    public void onCompleted() {
        mInterface.jsonCompleted();
    }

    @Override
    public void onError(Throwable e) {
        mInterface.jsonError(e.getMessage());
    }

    @Override
    public void onNext(ScheduleList scheduleList) {
        mInterface.jsonScheduleList(scheduleList);
    }

    public void fetchSchedules(){
        unSubscribeAll();
        subscribe(mInterface.getSchedule(), SchedulePresenter.this);
    }


}
