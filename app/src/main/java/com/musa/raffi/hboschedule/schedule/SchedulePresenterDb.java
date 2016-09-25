package com.musa.raffi.hboschedule.schedule;

import android.database.Cursor;
import android.util.Log;

import com.musa.raffi.hboschedule.BasePresenterImpl;
import com.musa.raffi.hboschedule.models.scheduledb.DataManager;
import com.musa.raffi.hboschedule.models.scheduledb.Schedule;
import com.musa.raffi.hboschedule.models.schedulepojo.ScheduleList;

import rx.Observer;

import static android.content.ContentValues.TAG;

/**
 * Created by Asus on 9/24/2016.
 */

public class SchedulePresenterDb extends BasePresenterImpl implements Observer<Cursor> {
    private ScheduleViewInterface mInterface;
    private DataManager mDataManager;

    SchedulePresenterDb(ScheduleViewInterface mInterface, DataManager dataManager) {
        this.mInterface = mInterface;
        mDataManager = dataManager;
    }

    @Override
    public void onCompleted() {
        mInterface.dbCompleted();
    }

    @Override
    public void onError(Throwable e) {
        mInterface.dbError(e.getMessage());
    }

    @Override
    public void onNext(Cursor cursor) {
        mInterface.dbScheduleList(cursor);
    }

    void setScheduleToRemind(int id){
        mDataManager.setScheduleToRemind(id);
    }

    void fetchSchedulesDb(){
        unSubscribeAll();
        subscribe(mInterface.getCursor(), SchedulePresenterDb.this);
    }

    void addSchedulesToDb(ScheduleList scheduleList){
        Schedule schedule = new Schedule();
        schedule.setChannel(scheduleList.getChannel());
        schedule.setDate(scheduleList.getDate());
        for (int i=0; i<scheduleList.getItems().size(); i++){
            int idSchedule = Integer.parseInt(scheduleList.getItems().get(i).getJadwalId());
            schedule.setId(idSchedule);
            schedule.setFilmName(scheduleList.getItems().get(i).getFilmName());
            schedule.setFilmPlot("null");
            schedule.setShowTime(scheduleList.getItems().get(i).getShowTime());
            schedule.setReminder(0);
            mDataManager.AddSchedule(schedule);
        }
    }
}
