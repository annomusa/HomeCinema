package com.musa.raffi.hboschedule.reminder;

import android.content.Context;
import android.database.Cursor;

import com.musa.raffi.hboschedule.base.BasePresenterImpl;
import com.musa.raffi.hboschedule.models.scheduledb.DataManager;

import rx.Observable;
import rx.Observer;

/**
 * Created by Asus on 9/23/2016.
 */

public class ReminderPresenter extends BasePresenterImpl implements Observer<Cursor> {
    private ReminderViewInterface mInterface;
    private DataManager mDataManager;

    public ReminderPresenter(ReminderViewInterface mInterface, Context ctx) {
        this.mInterface = mInterface;
        mDataManager = new DataManager(ctx);
    }

    @Override
    public void onCompleted() {
        mInterface.onCompleted();
    }

    @Override
    public void onError(Throwable e) {
        mInterface.onError(e.getMessage());
    }

    @Override
    public void onNext(Cursor cursor) {
        mInterface.onReminder(cursor);
    }

    public void fetchReminder(String dateNow, String timeNow){
        unSubscribeAll();
        subscribe(getReminderDb(dateNow, timeNow), ReminderPresenter.this);
    }

    private Observable<Cursor> getReminderDb(String dateNow, String timeNow){
        return mDataManager.getScheduleRemindRx(dateNow, timeNow);
    }

    public void unSetSchedule(int id){
        mDataManager.unsetScheduleToRemind(id);
    }
}
