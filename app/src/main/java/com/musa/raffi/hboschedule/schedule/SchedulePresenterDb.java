package com.musa.raffi.hboschedule.schedule;

import android.content.Context;
import android.database.Cursor;

import com.musa.raffi.hboschedule.base.BasePresenterImpl;
import com.musa.raffi.hboschedule.models.scheduledb.DataManager;
import com.musa.raffi.hboschedule.models.scheduledb.Schedule;
import com.musa.raffi.hboschedule.models.schedulepojo.ScheduleList;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import rx.Observable;
import rx.Observer;

/**
 * Created by Asus on 9/24/2016.
 */

public class SchedulePresenterDb extends BasePresenterImpl implements Observer<Cursor> {
    private ScheduleViewInterface mInterface;
    private DataManager mDataManager;

    SchedulePresenterDb(ScheduleViewInterface mInterface, Context ctx) {
        this.mInterface = mInterface;
        mDataManager = new DataManager(ctx);
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

    private Observable<Cursor> getCursor(String channelReq, String dateReq) {
        return mDataManager.getScheduleRx(channelReq, dateReq);
    }

    Calendar getCalender(int id) {
        Calendar result = Calendar.getInstance();
        Cursor c = mDataManager.getScheduleWithId(id);
        String dateString = c.getString(c.getColumnIndexOrThrow(DataManager.TABLE_ROW_DATE)) +
                " " + c.getString(c.getColumnIndexOrThrow(DataManager.TABLE_ROW_SHOW_TIME));

        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        Date date = null;
        try {
            date = format.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        result.setTime(date);
        return result;
    }

    void setScheduleToRemind(int id){
        mDataManager.setScheduleToRemind(id);
    }

    void unsetScheduleToRemind(int id) {
        mDataManager.unsetScheduleToRemind(id);
    }

    void fetchSchedulesDb(String channelReq, String dateReq){
        unSubscribeAll();
        subscribe(getCursor(channelReq, dateReq), SchedulePresenterDb.this);
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
