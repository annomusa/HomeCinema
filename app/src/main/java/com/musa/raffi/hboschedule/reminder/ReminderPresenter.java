package com.musa.raffi.hboschedule.reminder;

import android.database.Cursor;

import com.musa.raffi.hboschedule.BasePresenterImpl;
import com.musa.raffi.hboschedule.models.scheduledb.DataManager;

import rx.Observer;

/**
 * Created by Asus on 9/23/2016.
 */

public class ReminderPresenter extends BasePresenterImpl implements Observer<Cursor> {
    private ReminderViewInterface mInterface;
    private DataManager mDataManager;

    public ReminderPresenter(ReminderViewInterface mInterface, DataManager dataManager) {
        this.mInterface = mInterface;
        mDataManager = dataManager;
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

    public void fetchReminder(){
        unSubscribeAll();
        subscribe(mInterface.getReminder(), ReminderPresenter.this);
    }

    public void unSetSchedule(int id){
        mDataManager.unsetScheduleToRemind(id);
    }
}
