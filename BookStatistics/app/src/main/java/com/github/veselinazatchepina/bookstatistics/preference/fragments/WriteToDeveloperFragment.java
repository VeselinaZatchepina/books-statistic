package com.github.veselinazatchepina.bookstatistics.preference.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.veselinazatchepina.bookstatistics.R;

public class WriteToDeveloperFragment extends Fragment {

    public WriteToDeveloperFragment() {
    }

    public static WriteToDeveloperFragment newInstance() {
        return new WriteToDeveloperFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_write_to_developer, container, false);
        return rootView;
    }
}
