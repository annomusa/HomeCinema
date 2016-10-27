package com.musa.raffi.hboschedule.reminder;

/**
 * Created by Asus on 10/15/2016.
 */

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.musa.raffi.hboschedule.main.MainActivity;
import com.musa.raffi.hboschedule.R;
import com.musa.raffi.hboschedule.notification.NotificationReceiver;
import com.musa.raffi.hboschedule.reminder.adapter.ItemAdapter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

import static android.content.ContentValues.TAG;

public class ReminderFragment extends Fragment implements ReminderViewInterface, ItemAdapter.ReminderClickListener{
    private ReminderPresenter mPresenter;
    private ItemAdapter mAdapter;
    String dateNow, timeNow;
    private Context mContext;

    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.empty)
    TextView empty;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        mPresenter = new ReminderPresenter(this, mContext);

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR_OF_DAY, -1);

        dateNow = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(calendar.getTime());
        timeNow = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH).format(calendar.getTime());

        Log.d(TAG, "onCreate: ReminderActivity");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_reminder, container, false);
        ButterKnife.bind(this, view);
        configView();
        return view;
    }

    private void configView(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new ItemAdapter(mContext, this);
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).setActionBatTitle(getString(R.string.reminder_title));
        mPresenter.onResume();
        mPresenter.fetchReminder(dateNow, timeNow);
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(String message) {
        Log.d(TAG, "dbError: " + message);
    }

    @Override
    public void onReminder(Cursor cursor) {
        if(cursor.getCount() == 0) {
            Log.d(TAG, "onReminder: null");
            empty.setVisibility(View.VISIBLE);
        } else empty.setVisibility(View.GONE);
        mAdapter.swapCursor(cursor);
    }

    @Override
    public void onClick(int idSchedule, String film, String channel) {
        showDialog(idSchedule, film, channel);
    }

    private void showDialog(int idSchedule, String film, String channel) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
        alertDialogBuilder.setMessage(Html.fromHtml("Do you want to delete " + "<b>" + film + "</b> on " + channel + " from reminder list?"));

        alertDialogBuilder.setPositiveButton("Yes", (dialog, which) -> {
            mPresenter.unSetSchedule(idSchedule);
            mPresenter.unSetSchedule(idSchedule);
            mPresenter.fetchReminder(dateNow, timeNow);
            mAdapter.notifyDataSetChanged();
            NotificationReceiver.cancelNotification(idSchedule, mContext);
        });

        alertDialogBuilder.setNegativeButton("No", (dialog, which) -> {

        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}

