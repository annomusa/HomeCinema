package com.musa.raffi.hboschedule.settings;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.musa.raffi.hboschedule.main.MainActivity;
import com.musa.raffi.hboschedule.R;

/**
 * Created by Asus on 10/15/2016.
 */

public class SettingsFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).setActionBatTitle(getString(R.string.settings));
    }
}