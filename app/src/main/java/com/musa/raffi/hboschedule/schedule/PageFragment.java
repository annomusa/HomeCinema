package com.musa.raffi.hboschedule.schedule;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.musa.raffi.hboschedule.Application.App;
import com.musa.raffi.hboschedule.R;
import com.musa.raffi.hboschedule.models.channel.SingletonChannelList;
import com.musa.raffi.hboschedule.models.scheduledb.DataManager;
import com.musa.raffi.hboschedule.models.schedulepojo.ScheduleList;
import com.musa.raffi.hboschedule.notification.NewNotificationReceiver;
import com.musa.raffi.hboschedule.notification.NotificationReceiver;
import com.musa.raffi.hboschedule.notification.NotificationService;
import com.musa.raffi.hboschedule.reminder.ReminderActivity;
import com.musa.raffi.hboschedule.schedule.adapter.ScheduleCursorAdapter;
import com.musa.raffi.hboschedule.service.RestApi;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import static android.content.ContentValues.TAG;

/**
 * Created by Asus on 9/8/2016.
 */

public class PageFragment extends Fragment implements ScheduleViewInterface, DatePickerDialog.OnDateSetListener{
    @Bind(R.id.txtDate) TextView txtDate;
    @Bind(R.id.txtAlert) TextView txtAlert;
    @Bind(R.id.list_schedule) ListView listView;
    @Bind(R.id.imageButton) ImageButton imageButton;
    @Bind(R.id.empty_schedule) TextView emptySchedule;
    @Bind(R.id.relative_layout) RelativeLayout rl;
    @Inject RestApi restApiInject;

    private SchedulePresenter mPresenterJson;
    private SchedulePresenterDb mPresenterDb;
    private ProgressDialog mProgress;

    int mPageNumber;
    private String mChannelReq;
    private String mDateReq;
    private ScheduleCursorAdapter scheduleAdapter;
    private String mShowDate;
    public static final String ARG_PAGE = "ARG_PAGE";
    View.OnClickListener mOnClickListener;

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
        DataManager mDataManager = new DataManager(getActivity().getApplicationContext());

        mPresenterJson = new SchedulePresenter(this);
        mPresenterJson.onCreate();
        mPresenterDb = new SchedulePresenterDb(this, mDataManager);
        mPresenterDb.onCreate();

        mPageNumber = getArguments().getInt(ARG_PAGE);
        mChannelReq = SingletonChannelList.getInstance().getChannel(mPageNumber).getName();
        Log.d(TAG, "onCreate: PageFragment " + mChannelReq);
        mDateReq = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        mProgress = new ProgressDialog(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_screen_slide_page, container, false);
        ButterKnife.bind(this, rootView);

        mShowDate = generalizeDateToShow(mDateReq);
        txtDate.setText(mShowDate);
        txtAlert.setText(getString(R.string.alert));

        scheduleAdapter = new ScheduleCursorAdapter(getActivity().getApplicationContext(), null);
        listView.setAdapter(scheduleAdapter);
        emptySchedule.setVisibility(View.GONE);

        configSetOnClick();
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mProgress = ProgressDialog.show(getActivity(), "Search", "Searching...", true, false);
        retrieveList();
    }

    private void configSetOnClick(){
        listView.setOnItemClickListener((parent, view, position, id) -> {
            Cursor itemCursor = ((ScheduleCursorAdapter) parent.getAdapter()).getCursor();
            int idSchedule = itemCursor.getInt(itemCursor.getColumnIndex(DataManager.TABLE_ROW_ID));

            Calendar mCalendar = mPresenterDb.getCalender(idSchedule);

            String title = itemCursor.getString(itemCursor.getColumnIndex(DataManager.TABLE_ROW_FILM_NAME));
            String time = itemCursor.getString(itemCursor.getColumnIndex(DataManager.TABLE_ROW_SHOW_TIME)).substring(0,5) + " WIB";
            String channel = itemCursor.getString(itemCursor.getColumnIndex(DataManager.TABLE_ROW_CHANNEL));
            showDialog(idSchedule, title, time, channel, mCalendar);
        });

        imageButton.setOnClickListener(v -> {
            Calendar now = Calendar.getInstance();
            DatePickerDialog dpd = DatePickerDialog.newInstance(
                    PageFragment.this,
                    now.get(Calendar.YEAR),
                    now.get(Calendar.MONTH),
                    now.get(Calendar.DAY_OF_MONTH)
            );
            dpd.vibrate(false);
            dpd.show(getFragmentManager(), "Datepickerdialog");
        });
    }

    private void showDialog(int idSchedule, String title, String time, String channel, Calendar calendar) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setMessage(Html.fromHtml("Do you want to add " + "<b>" + title + "</b> on " + time + "?"));

        alertDialogBuilder.setPositiveButton("Yes", (dialog, which) -> {
            mPresenterDb.setScheduleToRemind(idSchedule);
            showSnackBar();
            setAlarm(idSchedule, title, time, channel, calendar);
        });

        mOnClickListener = v -> {
            mPresenterDb.unsetScheduleToRemind(idSchedule);
            Intent intent = new Intent(getActivity().getApplicationContext(), NotificationReceiver.class);
            PendingIntent cancelIntent = PendingIntent.getBroadcast(getActivity(), idSchedule, intent, 0);
            AlarmManager alarmMgr = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
            alarmMgr.cancel(cancelIntent);
        };

        alertDialogBuilder.setNegativeButton("No", (dialog, which) -> {

        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void setAlarm(int idSchedule, String title, String time, String channel, Calendar calendar){
        Intent notifIntent = new Intent(getActivity().getApplicationContext(), NotificationReceiver.class);
        notifIntent.putExtra("title", title);
        notifIntent.putExtra("time", time);
        notifIntent.putExtra("channel", channel);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), idSchedule, notifIntent, 0);
        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC, calendar.getTimeInMillis(), pendingIntent);
    }

    private void showSnackBar(){
        Snackbar snackbar = Snackbar.make(rl, "Schedule added to reminder list", Snackbar.LENGTH_SHORT)
                .setAction("Undo", mOnClickListener);
        snackbar.setActionTextColor(Color.RED);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor(Color.DKGRAY);
        TextView snackText = (TextView) snackBarView.findViewById(android.support.design.R.id.snackbar_text);
        snackText.setTextColor(Color.YELLOW);
        snackbar.show();
    }

    public static String generalizeDateToShow(String date){
        String res = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date d = sdf.parse(date);
            sdf.applyPattern("EEE, d MMM yyyy");
            res = sdf.format(d);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return res;
    }

    @Override
    public void jsonCompleted() {
        retrieveList();
    }

    @Override
    public void jsonError(String message) {
        Log.d(TAG, "jsonError: " + message + " " + mChannelReq);
        scheduleAdapter.changeCursor(null);
        emptySchedule.setVisibility(View.VISIBLE);
        mProgress.dismiss();
    }

    @Override
    public void jsonScheduleList(ScheduleList scheduleList) {
        Log.d(TAG, "jsonScheduleList: json fetching");
        mPresenterDb.addSchedulesToDb(scheduleList);
    }

    @Override
    public Observable<ScheduleList> getSchedule() {
        return restApiInject.getScheduleListByUrl("http://homecinema.pe.hu/api/v1/schedule?channel=" + mChannelReq + "&date_now=" + mDateReq);
    }

    @Override
    public void dbCompleted() {

    }

    @Override
    public void dbError(String message) {
        Log.d(TAG, "dbError: " + message);
        mProgress.dismiss();
        scheduleAdapter.changeCursor(null);
        emptySchedule.setVisibility(View.VISIBLE);
    }

    @Override
    public void dbScheduleList(Cursor cursor) {
        if(cursor.getCount() == 0){
            Log.d(TAG, "dbScheduleList: db null");
            mPresenterJson.onResume();
            mPresenterJson.fetchSchedules();
        } else {
            Log.d(TAG, "dbScheduleList: db not null");
            scheduleAdapter.changeCursor(cursor);
            emptySchedule.setVisibility(View.GONE);
            mProgress.dismiss();
        }
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        monthOfYear++;
        mDateReq = year + "-" + (monthOfYear<10?("0"+monthOfYear):monthOfYear) + "-" + (dayOfMonth);
        mShowDate = generalizeDateToShow(mDateReq);
        txtDate.setText(mShowDate);
        mProgress = ProgressDialog.show(getActivity(), "Search", "Searching...", true, false);
        retrieveList();
    }

    void retrieveList(){
        mPresenterDb.onResume();
        mPresenterDb.fetchSchedulesDb(mChannelReq, mDateReq);
    }
}
