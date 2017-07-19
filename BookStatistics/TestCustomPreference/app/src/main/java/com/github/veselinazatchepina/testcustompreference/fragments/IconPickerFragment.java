package com.github.veselinazatchepina.testcustompreference.fragments;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.github.veselinazatchepina.testcustompreference.R;

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
