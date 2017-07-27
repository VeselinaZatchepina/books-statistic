package com.github.veselinazatchepina.bookstatistics.preference.fragments;

import android.os.Bundle;

import com.github.veselinazatchepina.bookstatistics.MyApplication;
import com.github.veselinazatchepina.bookstatistics.R;
import com.squareup.leakcanary.RefWatcher;


public class ThemePreferencesFragment extends android.preference.PreferenceFragment {

    public static ThemePreferencesFragment newInstance() {
        return new ThemePreferencesFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RefWatcher refWatcher = MyApplication.getRefWatcher(getActivity());
        refWatcher.watch(this);
    }
}
