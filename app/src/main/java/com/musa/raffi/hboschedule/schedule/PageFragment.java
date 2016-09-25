package com.musa.raffi.hboschedule.schedule;

import android.app.Fragment;
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
import com.musa.raffi.hboschedule.models.schedulepojo.ScheduleList;
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

public class PageFragment extends Fragment implements ScheduleViewInterface, DatePickerDialog.OnDateSetListener, ScheduleCursorAdapter.ItemClickListener{
    @Bind(R.id.txtDate) TextView txtDate;
    @Bind(R.id.txtAlert) TextView txtAlert;
    @Bind(R.id.list_schedule) ListView listView;
    @Bind(R.id.imageButton) ImageButton imageButton;
    @Inject RestApi restApiInject;

    private DataManager mDataManager;
    private SchedulePresenter mPresenter;
    private SchedulePresenterDb mPresenterDb;

    int mPageNumber;
    private String channelReq;
    private String dateReq;
    private ScheduleCursorAdapter scheduleAdapter;
    private String showDate;
    public static final String ARG_PAGE = "ARG_PAGE";

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
        mDataManager = new DataManager(getActivity().getApplicationContext());

        mPresenter = new SchedulePresenter(this);
        mPresenter.onCreate();
        mPresenterDb = new SchedulePresenterDb(this, mDataManager);
        mPresenterDb.onCreate();

        mPageNumber = getArguments().getInt(ARG_PAGE);
        channelReq = SingletonChannelList.getInstance().getChannel(mPageNumber).getName();
        Log.d(TAG, "onCreate: PageFragment " + channelReq);
        dateReq = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_screen_slide_page, container, false);
        ButterKnife.bind(this, rootView);

        showDate = generalizeDateToShow(dateReq);
        txtDate.setText(showDate);
        txtAlert.setText(getString(R.string.alert));

        scheduleAdapter = new ScheduleCursorAdapter(getActivity().getApplicationContext(), null, this);
        listView.setAdapter(scheduleAdapter);

        configSetOnClick();
        return rootView;
    }

    private void configSetOnClick(){
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
    public void onResume() {
        super.onResume();
        mPresenterDb.onResume();
        mPresenterDb.fetchSchedulesDb();
    }

    @Override
    public void jsonCompleted() {
        Log.d(TAG, "jsonCompleted: Retrieve json completed!");
        mPresenterDb.onResume();
        mPresenterDb.fetchSchedulesDb();
    }

    @Override
    public void jsonError(String message) {
        Toast.makeText(getActivity().getApplicationContext(), "Schedule not found, choose another date.", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "jsonError: " + message + " " + channelReq);
    }

    @Override
    public void jsonScheduleList(ScheduleList scheduleList) {
        Log.d(TAG, "jsonScheduleList: json fetching");
        mPresenterDb.addSchedulesToDb(scheduleList);
    }

    @Override
    public Observable<ScheduleList> getSchedule() {
        return restApiInject.getScheduleListByUrl("http://homecinema.pe.hu/api/v1/schedule?channel=" + channelReq + "&date_now=" + dateReq);
    }

    @Override
    public void dbCompleted() {
        Log.d(TAG, "dbCompleted: Retrieve from db completed!");
    }

    @Override
    public void dbError(String message) {
        Log.d(TAG, "dbError: " + message);
    }

    @Override
    public void dbScheduleList(Cursor cursor) {
        if(cursor.getCount() == 0){
            Log.d(TAG, "dbScheduleList: db null");
            mPresenter.onResume();
            mPresenter.fetchSchedules();
        } else {
            Log.d(TAG, "dbScheduleList: db not null");
            scheduleAdapter.changeCursor(cursor);
            txtDate.setText(showDate);
        }
    }

    @Override
    public Observable<Cursor> getCursor() {
        return mDataManager.getScheduleRx(channelReq, dateReq);
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        monthOfYear++;
        dateReq = year + "-" + (monthOfYear<10?("0"+monthOfYear):monthOfYear) + "-" + (dayOfMonth);
        showDate = generalizeDateToShow(dateReq);

        mPresenterDb.onResume();
        mPresenterDb.fetchSchedulesDb();
    }

    @Override
    public void onClick(int idSchedule) {
        mPresenterDb.setScheduleToRemind(idSchedule);
        Toast.makeText(getActivity().getApplicationContext(), "Choosen schedule have been saved.", Toast.LENGTH_SHORT).show();
    }
}
