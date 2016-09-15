package com.musa.raffi.hboschedule.reminder;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.musa.raffi.hboschedule.R;
import com.musa.raffi.hboschedule.models.DataManager;

import static android.content.ContentValues.TAG;

/**
 * Created by Asus on 9/14/2016.
 */

public class ScheduleReminderCursorAdapter extends CursorAdapter {
    public ScheduleReminderCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.reminder_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView txtTime = (TextView) view.findViewById(R.id.txtTime);
        TextView txtTitle = (TextView) view.findViewById(R.id.txtTitle);
        TextView txtChannel = (TextView) view.findViewById(R.id.txtChannel);

        String channel = cursor.getString(cursor.getColumnIndexOrThrow(DataManager.TABLE_ROW_CHANNEL));
        String time = cursor.getString(cursor.getColumnIndexOrThrow(DataManager.TABLE_ROW_SHOW_TIME)) ;
        String title = cursor.getString(cursor.getColumnIndexOrThrow(DataManager.TABLE_ROW_FILM_NAME));

        String timeAndChannel = time.substring(0,4) + " WIB";
//        Log.d(TAG, "bindView: " + cursor.getInt(cursor.getColumnIndexOrThrow(DataManager.TABLE_ROW_ID)) + " " + time + " " + title);

        txtTime.setText(timeAndChannel);
        txtChannel.setText(channel);
        txtTitle.setText(title);
    }
}
