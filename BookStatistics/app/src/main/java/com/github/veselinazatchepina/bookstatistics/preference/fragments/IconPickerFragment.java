package com.github.veselinazatchepina.bookstatistics.preference.fragments;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.github.veselinazatchepina.bookstatistics.R;


public class IconPickerFragment extends PreferenceFragment {

    public static IconPickerFragment newInstance() {
        return new IconPickerFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
}
