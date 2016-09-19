package com.musa.raffi.hboschedule.schedule;

import android.app.AlarmManager;
import android.app.Fragment;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.musa.raffi.hboschedule.Application.App;
import com.musa.raffi.hboschedule.R;
import com.musa.raffi.hboschedule.models.channel.SingletonChannelList;
import com.musa.raffi.hboschedule.models.scheduledb.DataManager;
import com.musa.raffi.hboschedule.models.scheduledb.Schedule;
import com.musa.raffi.hboschedule.models.schedulepojo.ScheduleList;
import com.musa.raffi.hboschedule.service.RestApi;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import static android.content.ContentValues.TAG;
import static android.content.Context.ALARM_SERVICE;

/**
 * Created by Asus on 9/8/2016.
 */

public class PageFragment extends Fragment implements ScheduleViewInterface, DatePickerDialog.OnDateSetListener{
    @Bind(R.id.txtDate) TextView txtDate;
    @Bind(R.id.txtAlert) TextView txtAlert;
    @Bind(R.id.list_schedule) ListView listView;
    @Bind(R.id.imageButton) ImageButton imageButton;
    @Inject RestApi restApiInject;

    private int mPageNumber;
    private List<HashMap<String,String>> dummyList;
    public static final String ARG_PAGE = "ARG_PAGE";
    private DataManager dataManager;
    private SchedulePresenter mPresenter;
    private String channelReq;
    private String dateReq;
    private ScheduleCursorAdapter scheduleAdapter;
    private String showDate;
    AlarmManager alarmManager;
    PendingIntent pendingIntent;

    public static PageFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        PageFragment fragment = new PageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        App.getApiComponent(getActivity()).inject(this);
        mPresenter = new SchedulePresenter(this);
        mPresenter.onCreate();
        mPageNumber = getArguments().getInt(ARG_PAGE);
        channelReq = SingletonChannelList.getInstance().getChannel(mPageNumber).getName();
        Log.d(TAG, "onCreate: " + channelReq);
        dateReq = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        dummyList = new ArrayList<>();

        dataManager = new DataManager(getActivity().getApplicationContext());
        alarmManager = (AlarmManager) getActivity().getApplicationContext().getSystemService(ALARM_SERVICE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_screen_slide_page, container, false);
        ButterKnife.bind(this, rootView);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date d = sdf.parse(dateReq);
            sdf.applyPattern("EEE, d MMM yyyy");
            showDate = sdf.format(d);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        txtDate.setText(showDate);
        txtAlert.setText(getString(R.string.alert));

        final Cursor newCursor = dataManager.getSchedule(channelReq, dateReq);
        Log.d(TAG, "onCreateView: " + newCursor.getCount());
        if(newCursor.getCount() == 0) {
            mPresenter.onResume();
            mPresenter.fetchSchedules();
        }
        scheduleAdapter = new ScheduleCursorAdapter(getActivity().getApplicationContext(), newCursor);
        listView.setAdapter(scheduleAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor itemCursor = ((ScheduleCursorAdapter) parent.getAdapter()).getCursor();
                int a = itemCursor.getColumnIndex(DataManager.TABLE_ROW_ID);
                int rowId = itemCursor.getInt(a);
                Log.d(TAG, "onItemClick: " + rowId);
                dataManager.setScheduleToRemind(rowId);
                Toast.makeText(getActivity().getApplicationContext(), "Choosen schedule have been saved.", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getActivity(), NotificationReceiver.class);
                intent.putExtra("title", itemCursor.getString(itemCursor.getColumnIndexOrThrow(DataManager.TABLE_ROW_SHOW_TIME)));
                intent.putExtra("text", itemCursor.getString(itemCursor.getColumnIndexOrThrow(DataManager.TABLE_ROW_FILM_NAME)));
                pendingIntent = PendingIntent.getBroadcast(getActivity().getApplicationContext(), 0, intent, 0);

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                String dateNotif = itemCursor.getString(itemCursor.getColumnIndexOrThrow(DataManager.TABLE_ROW_DATE)) + " " +
                        itemCursor.getString(itemCursor.getColumnIndexOrThrow(DataManager.TABLE_ROW_SHOW_TIME));
                Log.d(TAG, "onItemClick: " + dateNotif);

                Calendar calendar = Calendar.getInstance();
                try {
                    calendar.setTime(sdf.parse(dateNotif));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Log.d(TAG, "onItemClick: " + sdf.format(calendar.getTime()));
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            }
        });
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        PageFragment.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                dpd.vibrate(false);
                dpd.show(getFragmentManager(), "Datepickerdialog");
            }
        });
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onCompleted() {
        Log.d(TAG, "onCompleted: !");
    }

    @Override
    public void onError(String message) {
        Toast.makeText(getActivity().getApplicationContext(), "Schedule not found, choose another date.", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onError: " + message + " " + channelReq);
    }

    @Override
    public void onScheduleList(ScheduleList scheduleList) {
        Schedule schedule = new Schedule();
        schedule.setChannel(scheduleList.getChannel());
        schedule.setDate(dateReq);
        Log.d(TAG, "onScheduleList: " + scheduleList.getChannel());
        for (int i=0; i<scheduleList.getItems().size(); i++){
            int idSchedule = Integer.parseInt(scheduleList.getItems().get(i).getJadwalId());
            schedule.setId(idSchedule);
            schedule.setFilmName(scheduleList.getItems().get(i).getFilmName());
            schedule.setFilmPlot("null");
            schedule.setShowTime(scheduleList.getItems().get(i).getShowTime());
            schedule.setReminder(0);
            dataManager.AddSchedule(schedule);
        }
        txtDate.setText(showDate);
        Cursor newCursor = dataManager.getSchedule(channelReq, dateReq);
        scheduleAdapter.changeCursor(newCursor);
    }

    @Override
    public Observable<ScheduleList> getSchedule() {
        return restApiInject.getScheduleListByUrl("http://homecinema.pe.hu/api/v1/schedule?channel=" + channelReq + "&date_now=" + dateReq);
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        monthOfYear++;
        dateReq = year + "-" + (monthOfYear<10?("0"+monthOfYear):monthOfYear) + "-" + (dayOfMonth);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date d = sdf.parse(dateReq);
            sdf.applyPattern("EEE, d MMM yyyy");
            showDate = sdf.format(d);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Cursor newCursor = dataManager.getSchedule(channelReq, dateReq);
        Log.d(TAG, "onDateSet: " + dateReq + " " + channelReq + " " + newCursor.getCount());
        if (newCursor.getCount() == 0){
            mPresenter.onResume();
            mPresenter.fetchSchedules();
        } else {
            scheduleAdapter.changeCursor(newCursor);
            txtDate.setText(showDate);
        }
    }
}
