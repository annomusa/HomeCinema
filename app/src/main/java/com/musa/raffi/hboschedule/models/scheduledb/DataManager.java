package com.musa.raffi.hboschedule.models.scheduledb;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import rx.Observable;

import static android.content.ContentValues.TAG;

/**
 * Created by Asus on 9/10/2016.
 */

public class DataManager {
    private SQLiteDatabase db;

    private static final String DB_NAME = "wid_db";
    private static final int DB_VERSION = 2;

    private static final String TABLE_SCHEDULE = "wis_table_schedule";
    public static final String TABLE_ROW_ID = "_id";
    public static final String TABLE_ROW_CHANNEL = "channel";
    public static final String TABLE_ROW_DATE = "date";
    public static final String TABLE_ROW_FILM_NAME = "film_name";
    public static final String TABLE_ROW_SHOW_TIME = "show_time";
    public static final String TABLE_ROW_FILM_PLOT = "film_plot";
    public static final String TABLE_ROW_REMINDER = "reminder";

    public DataManager(Context context){
        CustomSQLiteOpenHelper helper = new CustomSQLiteOpenHelper(context);
        db = helper.getWritableDatabase();
    }

    public void AddSchedule(Schedule schedule){
        String query = "INSERT INTO " + TABLE_SCHEDULE + " (" +
                TABLE_ROW_ID + ", " +
                TABLE_ROW_CHANNEL + ", " +
                TABLE_ROW_DATE + ", " +
                TABLE_ROW_FILM_NAME + ", " +
                TABLE_ROW_SHOW_TIME + ", " +
                TABLE_ROW_FILM_PLOT + ", " +
                TABLE_ROW_REMINDER + ") " +
                "VALUES (" +
                "'" + schedule.getId() + "'" + ", " +
                "'" + schedule.getChannel() + "'" + ", " +
                "'" + schedule.getDate() + "'" + ", " +
                "'" + schedule.getFilmName() + "'" + ", " +
                "'" + schedule.getShowTime() + "'" + ", " +
                "'" + schedule.getFilmPlot() + "'" + ", " +
                schedule.isReminder() +
                ");";
//        Log.d(TAG, "AddSchedule: " + query);
        db.execSQL(query);
    }

    public Cursor getSchedule(String channel, String date){
        String query = "select * from " + TABLE_SCHEDULE + " where " + TABLE_ROW_CHANNEL + " = '" + channel + "' and " + TABLE_ROW_DATE + " = '" + date + "';";
        Log.d(TAG, "getSchedule DM: " + query);
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();
        Log.d(TAG, "getSchedule DM: " + c.getCount());
        return c;
    }

    public Cursor getScheduleRemind(String date, String time) {
        String query = "select * from " + TABLE_SCHEDULE +
                " where " + TABLE_ROW_REMINDER + " = 1 and " + TABLE_ROW_DATE + " >= '" + date + "' and " + TABLE_ROW_SHOW_TIME + " >= '" + time + "' " +
                "order by " + TABLE_ROW_DATE + ", " + TABLE_ROW_SHOW_TIME;
        Log.d(TAG, "getScheduleRemind: " + query);
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();
        return c;
    }

    public void setScheduleToRemind(int id){
        String query = "update " + TABLE_SCHEDULE + " set " + TABLE_ROW_REMINDER + " = 1" + " where " + TABLE_ROW_ID + " = " + id;
        Log.d(TAG, "setScheduleToRemind: " + query);
        db.execSQL(query);
    }

    public Cursor unsetScheduleToRemind(int id){
        String query = "update " + TABLE_SCHEDULE + " set " + TABLE_ROW_REMINDER + " = 0" + " where " + TABLE_ROW_ID + " = " + id;
        Log.d(TAG, "unsetScheduleToRemind: " + query);
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();
        return c;
    }

    public Observable<Cursor> getScheduleRemindRx(String date, String time) {
        String query = "select * from " + TABLE_SCHEDULE +
                " where " + TABLE_ROW_REMINDER + " = 1 and " + TABLE_ROW_DATE + " >= '" + date + "' and " + TABLE_ROW_SHOW_TIME + " >= '" + time + "' " +
                "order by " + TABLE_ROW_DATE + ", " + TABLE_ROW_SHOW_TIME;
        Log.d(TAG, "getScheduleRemind: " + query);
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();

        return Observable.just(c);
    }

    public Observable<Cursor> getScheduleRx(String channel, String date){
        String query = "select * from " + TABLE_SCHEDULE + " where " + TABLE_ROW_CHANNEL + " = '" + channel + "' and " + TABLE_ROW_DATE + " = '" + date + "';";
//        Log.d(TAG, "getSchedule DM Rx: " + query);
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();
        Log.d(TAG, "getScheduleRx: count cursor " + c.getCount() + " " + channel);
        return Observable.just(c);
    }

    public Cursor getScheduleWithId(int id){
        String query = "select * from " + TABLE_SCHEDULE + " where " + TABLE_ROW_ID + " = " + id + ";";
        Log.d(TAG, "getScheduleWithId: " + query);
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();
        return c;
    }

    public Cursor getScheduleRemindFirebase() {
        String query = "select * from " + TABLE_SCHEDULE +
                " where " + TABLE_ROW_REMINDER + " = 1 " +
                "order by " + TABLE_ROW_DATE + ", " + TABLE_ROW_SHOW_TIME;
        Log.d(TAG, "getScheduleRemind: " + query);
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();
        return c;
    }

    private class CustomSQLiteOpenHelper extends SQLiteOpenHelper{
        public CustomSQLiteOpenHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            String newTableQueryString = "create table "
                    + TABLE_SCHEDULE + " ("
                    + TABLE_ROW_ID + " integer primary key not null, "
                    + TABLE_ROW_CHANNEL + " text not null, "
                    + TABLE_ROW_DATE + " date not null, "
                    + TABLE_ROW_FILM_NAME + " text not null, "
                    + TABLE_ROW_SHOW_TIME + " time not null, "
                    + TABLE_ROW_FILM_PLOT + " text, "
                    + TABLE_ROW_REMINDER + " boolean not null" + ");";
            db.execSQL(newTableQueryString);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
}
