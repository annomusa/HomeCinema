package com.musa.raffi.hboschedule;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.musa.raffi.hboschedule.models.Channel;
import com.musa.raffi.hboschedule.models.ChannelList;

public class ChannelSlidePageFragment extends Fragment {
    public static final String ARG_PAGE = "page";

    private int mPageNumber;

    public static ChannelSlidePageFragment create(int pageNumber) {
        ChannelSlidePageFragment fragment = new ChannelSlidePageFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, pageNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public ChannelSlidePageFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPageNumber = getArguments().getInt(ARG_PAGE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater
                .inflate(R.layout.fragment_screen_slide_page, container, false);

        TextView txtDate = (TextView) rootView.findViewById(R.id.txtDate);
        txtDate.setText(ChannelList.getInstance().getChannel(mPageNumber).getName()
                + " Wed, 7th Sept 2016");

        TextView txtAlert = (TextView) rootView.findViewById(R.id.txtAlert);
        txtAlert.setText(getString(R.string.alert));

        return rootView;
    }

    /**
     * Returns the page number represented by this fragment object.
     */
    public int getPageNumber() {
        return mPageNumber;
    }
}
