package com.musa.raffi.hboschedule.schedule;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.musa.raffi.hboschedule.Application.App;
import com.musa.raffi.hboschedule.R;
import com.musa.raffi.hboschedule.Utility;
import com.musa.raffi.hboschedule.models.channel.SingletonChannelList;
import com.musa.raffi.hboschedule.models.scheduledb.DataManager;
import com.musa.raffi.hboschedule.models.schedulepojo.ScheduleList;
import com.musa.raffi.hboschedule.notification.NotificationBootReceiver;
import com.musa.raffi.hboschedule.notification.NotificationReceiver;
import com.musa.raffi.hboschedule.schedule.adapter.ScheduleCursorAdapter;
import com.musa.raffi.hboschedule.service.RestApi;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import static android.content.ContentValues.TAG;

/**
 * Created by Asus on 9/8/2016.
 */

public class PageFragment extends Fragment implements ScheduleViewInterface, DatePickerDialog.OnDateSetListener{
    @Inject RestApi restApiInject;
    @Bind(R.id.txtDate) TextView txtDate;
    @Bind(R.id.txtAlert) TextView txtAlert;
    @Bind(R.id.list_schedule) ListView listView;
    @Bind(R.id.relative_layout) RelativeLayout rl;
    @Bind(R.id.imageButton) ImageButton dateButton;
    @Bind(R.id.btn_refresh) Button refreshButton;
    @Bind(R.id.empty_schedule) TextView emptySchedule;

    private SchedulePresenter mPresenterJson;
    private SchedulePresenterDb mPresenterDb;
    private ScheduleCursorAdapter scheduleAdapter;

    int mPageNumber;
    int sumVisible;
    Animation mAnimFlash;
    private String mDateReq;
    private String mShowDate;
    private String mChannelReq;
    View.OnClickListener mOnClickListener;
    public static final String ARG_PAGE = "ARG_PAGE";
    private Context mContext;
    private boolean mIsVisible;

    public static PageFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        PageFragment fragment = new PageFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Log.d(TAG, "setUserVisibleHint: " + sumVisible + " " + mChannelReq);

        if(isVisibleToUser) {
            sumVisible++;
            mIsVisible = true;
        }
        if(sumVisible == 1 && mChannelReq != null){
            showFetchSchedule();
            retrieveListDb();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.getApiComponent(getActivity()).inject(this);


        mContext = getActivity().getApplicationContext();
        mPageNumber = getArguments().getInt(ARG_PAGE);
        mChannelReq = SingletonChannelList.getInstance().getChannel(mPageNumber).getName();
        mAnimFlash = AnimationUtils.loadAnimation(mContext, R.anim.flash);
        mAnimFlash.setDuration(1000);
        Log.d(TAG, "onCreate: PageFragment " + mChannelReq);
        mDateReq = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(new Date());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mRootView = inflater.inflate(R.layout.fragment_screen_slide_page, container, false);
        ButterKnife.bind(this, mRootView);

        mShowDate = Utility.generalizeDateToShow(mDateReq);
        txtDate.setText(mShowDate);

        scheduleAdapter = new ScheduleCursorAdapter(getActivity().getApplicationContext(), null);
        listView.setAdapter(scheduleAdapter);
        refreshButton.setVisibility(View.GONE);

        configSetOnClick();
        return mRootView;
    }

    @OnClick(R.id.btn_refresh)
    public void refreshConnection(){
        showFetchSchedule();
        retrieveListDb();
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenterDb = new SchedulePresenterDb(this, mContext);
        mPresenterDb.onCreate();

        if(mIsVisible){
            showFetchSchedule();
            retrieveListDb();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        mPresenterDb.onDestroy();
        mPresenterDb = null;
    }

    void retrieveListDb(){
        mPresenterDb.onResume();
        mPresenterDb.fetchSchedulesDb(mChannelReq, mDateReq);
    }

    void retrieveListJson(){
        mPresenterJson = new SchedulePresenter(this);
        mPresenterJson.onCreate();
        mPresenterJson.onResume();
        mPresenterJson.fetchSchedules();
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

        dateButton.setOnClickListener(v -> {
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

    @Override
    public void jsonCompleted() {
        mPresenterJson.onDestroy();
        mPresenterJson = null;
        retrieveListDb();
    }

    @Override
    public void jsonError(String message) {
        Log.d(TAG, "jsonError: " + message + " " + mChannelReq);
        scheduleAdapter.changeCursor(null);
        if(message.equals("timeout") || message.equals("null")){
            emptySchedule.setText(R.string.timeout);
            emptySchedule.setVisibility(View.VISIBLE);
            refreshButton.setVisibility(View.VISIBLE);
        } else{
            emptySchedule.setText(R.string.schedule_not_available);
            emptySchedule.setVisibility(View.VISIBLE);
        }
        emptySchedule.clearAnimation();
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
        mPresenterDb.onDestroy();
    }

    @Override
    public void dbError(String message) {
        Log.d(TAG, "dbError: " + message);
        scheduleAdapter.changeCursor(null);

        emptySchedule.setVisibility(View.VISIBLE);
        refreshButton.setVisibility(View.VISIBLE);
    }

    @Override
    public void dbScheduleList(Cursor cursor) {
        if(cursor.getCount() == 0){
            if(isInternetAvailable()){
                Log.d(TAG, "dbScheduleList: connected");
                retrieveListJson();
            } else {
                Log.d(TAG, "dbScheduleList: not connected");
                emptySchedule.setText(R.string.offline_mode);
                emptySchedule.setVisibility(View.VISIBLE);
                emptySchedule.clearAnimation();
                refreshButton.setVisibility(View.VISIBLE);
            }
        } else {
            scheduleAdapter.changeCursor(cursor);
            emptySchedule.setVisibility(View.GONE);
            emptySchedule.clearAnimation();
            refreshButton.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        monthOfYear++;
        mDateReq = year + "-" + (monthOfYear<10?("0"+monthOfYear):monthOfYear) + "-" + (dayOfMonth<10?("0"+dayOfMonth):dayOfMonth);
        Log.d(TAG, "onDateSet: " + mDateReq);
        mShowDate = Utility.generalizeDateToShow(mDateReq);
        txtDate.setText(mShowDate);

        showFetchSchedule();
        retrieveListDb();
    }

    private void showFetchSchedule(){
        refreshButton.setVisibility(View.GONE);
        scheduleAdapter.changeCursor(null);
        emptySchedule.setText(R.string.fetching_schedule);
        emptySchedule.setVisibility(View.VISIBLE);
        mAnimFlash.setDuration(750);
        emptySchedule.setAnimation(mAnimFlash);
    }

    private void showDialog(int idSchedule, String title, String time, String channel, Calendar calendar) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setMessage(Html.fromHtml("Do you want to add " + "<b>" + title + "</b> on " + time + "?"));

        alertDialogBuilder.setPositiveButton("Yes", (dialog, which) -> {
            Calendar currentTime = Calendar.getInstance();
            if(currentTime.before(calendar)){
                mPresenterDb.setScheduleToRemind(idSchedule);
                showSnackBar(getString(R.string.added_schedule), true);
                setAlarm(idSchedule, title, time, channel, calendar);
            } else {
                showSnackBar(getString(R.string.added_schedule_fail), false);
            }
        });

        mOnClickListener = v -> {
            mPresenterDb.unsetScheduleToRemind(idSchedule);
            cancelAlarm(idSchedule);
        };

        alertDialogBuilder.setNegativeButton("No", (dialog, which) -> {

        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void showSnackBar(String msg, boolean isCorrect){
        Snackbar snackbar;
        if (isCorrect)
            snackbar = Snackbar.make(rl, msg, Snackbar.LENGTH_SHORT).setAction("Undo", mOnClickListener);
        else
            snackbar = Snackbar.make(rl, msg, Snackbar.LENGTH_SHORT);

        snackbar.setActionTextColor(Color.RED);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor(Color.DKGRAY);
        TextView snackText = (TextView) snackBarView.findViewById(android.support.design.R.id.snackbar_text);
        snackText.setTextColor(Color.YELLOW);
        snackbar.show();
    }

    private void setAlarm(int idSchedule, String title, String time, String channel, Calendar calendar){
        Intent notifIntent = new Intent(mContext, NotificationReceiver.class);
        notifIntent.putExtra("title", title);
        notifIntent.putExtra("time", time);
        notifIntent.putExtra("channel", channel);
        notifIntent.putExtra("idSchedule", idSchedule);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), idSchedule, notifIntent, 0);
        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC, calendar.getTimeInMillis(), pendingIntent);

        ComponentName receiver = new ComponentName(mContext, NotificationBootReceiver.class);
        PackageManager pm = mContext.getPackageManager();
        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
    }

    private void cancelAlarm(int idSchedule) {
        Intent intent = new Intent(mContext, NotificationReceiver.class);
        PendingIntent cancelIntent = PendingIntent.getBroadcast(getActivity(), idSchedule, intent, 0);
        AlarmManager alarmMgr = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        alarmMgr.cancel(cancelIntent);

        ComponentName receiver = new ComponentName(mContext, NotificationBootReceiver.class);
        PackageManager pm = mContext.getPackageManager();
        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
    }

    public boolean isInternetAvailable() {
        ConnectivityManager cm =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
