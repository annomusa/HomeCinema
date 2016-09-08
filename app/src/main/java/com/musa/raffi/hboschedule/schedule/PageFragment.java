package com.musa.raffi.hboschedule.schedule;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.musa.raffi.hboschedule.R;
import com.musa.raffi.hboschedule.models.ChannelList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Asus on 9/8/2016.
 */

public class PageFragment extends Fragment {
    public static final String ARG_PAGE = "ARG_PAGE";

    private List<HashMap<String,String>> dummyList;


    private int mPageNumber;

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
        mPageNumber = getArguments().getInt(ARG_PAGE);

        dummyList = new ArrayList<>();
        HashMap<String,String> item = new HashMap<>();
        item.put("time", "10.00");
        item.put("title", "Power Rangers");
        dummyList.add(item);

        HashMap<String,String> item2 = new HashMap<>();
        item2.put("time", "11.00");
        item2.put("title", "Power Rangers 2");
        dummyList.add(item2);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_screen_slide_page, container, false);

        TextView txtDate = (TextView) rootView.findViewById(R.id.txtDate);
        txtDate.setText(ChannelList.getInstance().getChannel(mPageNumber).getName()
                + " Wed, 7th Sept 2016");

        TextView txtAlert = (TextView) rootView.findViewById(R.id.txtAlert);
        txtAlert.setText(getString(R.string.alert));

        ListView listView = (ListView) rootView.findViewById(R.id.list_schedule);
        SimpleAdapter adapter = new SimpleAdapter(getActivity(),
                dummyList,
                R.layout.schedule_item,
                new String[]{ "time", "title"},
                new int[]{R.id.txtTime, R.id.txtTitle}
                );

        setListViewHeightBasedOnChildren(listView);

        listView.setAdapter(adapter);

        return rootView;
    }

    public void setListViewHeightBasedOnChildren(ListView listView) {
        SimpleAdapter adapter = new SimpleAdapter(getActivity(),
                dummyList,
                R.layout.schedule_item,
                new String[]{ "time", "title"},
                new int[]{R.id.txtTime, R.id.txtTitle}
        );

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < adapter.getCount(); i++) {
            view = adapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (adapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

}
