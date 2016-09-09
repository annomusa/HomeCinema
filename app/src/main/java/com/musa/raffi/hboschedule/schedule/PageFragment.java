package com.musa.raffi.hboschedule.schedule;

import android.app.Dialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.musa.raffi.hboschedule.Application.app;
import com.musa.raffi.hboschedule.R;
import com.musa.raffi.hboschedule.models.ChannelList;
import com.musa.raffi.hboschedule.models.Constant;
import com.musa.raffi.hboschedule.models.schedule.Schedule;
import com.musa.raffi.hboschedule.models.schedule.ScheduleList;
import com.musa.raffi.hboschedule.service.RestApi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * Created by Asus on 9/8/2016.
 */

public class PageFragment extends Fragment implements ScheduleViewInterface {
//    @Inject
//    RestApi restApi;

    @Bind(R.id.txtDate) TextView txtDate;
    @Bind(R.id.txtAlert) TextView txtAlert;
    @Bind(R.id.list_schedule) ListView listView;

    private int mPageNumber;
    private ProgressDialog mDialog;
    private SimpleAdapter simpleAdapter;
    private SchedulePresenter mPresenter;
    private List<HashMap<String,String>> dummyList;
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

        ( (app) getActivity().getApplication() ).getApiComponent().inject(PageFragment.this);

        mPageNumber = getArguments().getInt(ARG_PAGE);
        dummyList = new ArrayList<>();
        mPresenter = new SchedulePresenter(this);
        mPresenter.onCreate();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_screen_slide_page, container, false);
        ButterKnife.bind(this, rootView);
        txtDate.setText(ChannelList.getInstance().getChannel(mPageNumber).getName()
                + " Wed, 7th Sept 2016");
        txtAlert.setText(getString(R.string.alert));
        simpleAdapter = new SimpleAdapter(getActivity(),
                dummyList,
                R.layout.schedule_item,
                new String[]{ "ShowTime", "FilmName"},
                new int[]{R.id.txtTime, R.id.txtTitle}
                );
        setListViewHeightBasedOnChildren(listView);
        listView.setAdapter(simpleAdapter);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.onResume();
        mPresenter.fetchSchedules();
        mDialog = new ProgressDialog(getActivity().getApplicationContext());
        mDialog.setIndeterminate(true);
        mDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mDialog.setTitle("Downloading List");
        mDialog.setMessage("Please wait...");
//        mDialog.show();
    }

    @Override
    public void onCompleted() {
        mDialog.dismiss();
    }

    @Override
    public void onError(String message) {
        mDialog.dismiss();
        Toast.makeText(getActivity().getApplicationContext(), message, Toast.LENGTH_SHORT).show();
        Log.d("Json", "onError: " + message);
    }

    @Override
    public void onScheduleList(ScheduleList scheduleList) {
        List<Schedule> listSchedule = scheduleList.getDataSchedule().get(0).getSchedules();
        for (int i=0; i<listSchedule.size(); i++){
            String showTime = listSchedule.get(i).getShowTime();
            String title = listSchedule.get(i).getFilmName();
            HashMap<String,String> item = new HashMap<>();
            item.put("ShowTime", showTime);
            item.put("FilmName", title);
            dummyList.add(item);
        }
        simpleAdapter.notifyDataSetChanged();
    }

    @Override
    public Observable<ScheduleList> getSchedule() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        RxJavaCallAdapterFactory rxAdapter = RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io());
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.ROOT_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(rxAdapter)
                .build();
        RestApi apiService = retrofit.create(RestApi.class);
        return apiService.getScheduleList("HBO%20HD", "2016-09-08");
//        return restApi.getScheduleList(ChannelList.getInstance().getChannel(mPageNumber).getName(), "2016-09-08");
    }

    // ----- method to adjust listview inside scrollview
    public void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }
}
