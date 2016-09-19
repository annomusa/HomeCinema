package com.musa.raffi.hboschedule.reminder;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.musa.raffi.hboschedule.R;
import com.musa.raffi.hboschedule.models.scheduledb.DataManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.content.ContentValues.TAG;

/**
 * Created by Asus on 9/16/2016.
 */

public class ItemAdapter extends RecyclerViewCursorAdapter<ItemAdapter.ItemViewHolder> {
    /**
     * Constructor.
     *
     * @param context The Context the Adapter is displayed in.
     */
    protected ItemAdapter(Context context) {
        super(context);
        setupCursorAdapter(null, 0, R.layout.reminder_item, false);
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemViewHolder(mCursorAdapter.newView(mContext, mCursorAdapter.getCursor(), parent));
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        mCursorAdapter.getCursor().moveToPosition(position);
        setViewHolder(holder);
        mCursorAdapter.bindView(null, mContext, mCursorAdapter.getCursor());
    }

    public class ItemViewHolder extends RecyclerViewCursorViewHolder{
        public final TextView mTime;
        public final TextView mFilm;
        public final TextView mChannel;


        /**
         * Constructor.
         *
         * @param view The root view of the ViewHolder.
         */
        public ItemViewHolder(View view) {
            super(view);


            mTime = (TextView) view.findViewById(R.id.time_and_channel);
            mFilm = (TextView) view.findViewById(R.id.film_name);
            mChannel = (TextView) view.findViewById(R.id.channel);
        }

        @Override
        public void bindCursor(Cursor cursor) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String dateReq = cursor.getString(cursor.getColumnIndexOrThrow(DataManager.TABLE_ROW_DATE));
            String time = cursor.getString(cursor.getColumnIndexOrThrow(DataManager.TABLE_ROW_SHOW_TIME)).substring(0,5) + " WIB, ";
            String channel = cursor.getString(cursor.getColumnIndexOrThrow(DataManager.TABLE_ROW_CHANNEL));
            String film = cursor.getString(cursor.getColumnIndexOrThrow(DataManager.TABLE_ROW_FILM_NAME));

            try {
                Date d = sdf.parse(dateReq);
                sdf.applyPattern("EEE, d MMM yyyy");
                dateReq = sdf.format(d);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            mChannel.setText(channel);
            mTime.setText(time + dateReq);
            mFilm.setText(film);
        }
    }
}
