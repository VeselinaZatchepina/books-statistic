package com.github.veselinazatchepina.bookstatistics.preference.fragments;

import android.os.Bundle;

import com.github.veselinazatchepina.bookstatistics.R;


public class ThemePreferencesFragment extends android.preference.PreferenceFragment {

    public static ThemePreferencesFragment newInstance() {
        return new ThemePreferencesFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
}
