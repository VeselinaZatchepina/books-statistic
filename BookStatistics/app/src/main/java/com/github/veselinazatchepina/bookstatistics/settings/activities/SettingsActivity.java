package com.github.veselinazatchepina.bookstatistics.settings.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;

import com.github.veselinazatchepina.bookstatistics.R;
import com.github.veselinazatchepina.bookstatistics.abstracts.NavigationAbstractActivity;

import butterknife.BindView;

import static com.github.veselinazatchepina.bookstatistics.R.id.fab;


public class SettingsActivity extends NavigationAbstractActivity {

    @BindView(fab)
    FloatingActionButton mFloatingActionButton;

    public static Intent newIntent(Context context) {
        return new Intent(context, SettingsActivity.class);
    }

    @Override
    public void defineInputData(Bundle savedInstanceState) {

    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_navigation_drawer;
    }

    @Override
    public void defineAppBarLayoutExpandableValue() {
        setAppBarNotExpandable();
    }

    @Override
    public void defineFragment() {

    }

    @Override
    public void defineFab() {
        mFloatingActionButton.setVisibility(View.GONE);
    }
}
