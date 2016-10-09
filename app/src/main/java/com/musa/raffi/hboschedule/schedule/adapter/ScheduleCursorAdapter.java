package com.musa.raffi.hboschedule.schedule.adapter;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.musa.raffi.hboschedule.R;
import com.musa.raffi.hboschedule.models.scheduledb.DataManager;
import static android.content.ContentValues.TAG;

/**
 * Created by Asus on 9/13/2016.
 */

public class ScheduleCursorAdapter extends CursorAdapter{

    public ScheduleCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.schedule_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView txtTime = (TextView) view.findViewById(R.id.txtTime);
        TextView txtTitle = (TextView) view.findViewById(R.id.txtTitle);

        String time = cursor.getString(cursor.getColumnIndexOrThrow(DataManager.TABLE_ROW_SHOW_TIME)).substring(0,5) + " WIB";
        String title = cursor.getString(cursor.getColumnIndexOrThrow(DataManager.TABLE_ROW_FILM_NAME));
        int mIdSchedule = cursor.getInt(cursor.getColumnIndexOrThrow(DataManager.TABLE_ROW_ID));

        txtTime.setText(time);
        txtTitle.setText(title);
    }
}
