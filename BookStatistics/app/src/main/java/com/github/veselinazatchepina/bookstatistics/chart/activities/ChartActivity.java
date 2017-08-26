package com.github.veselinazatchepina.bookstatistics.chart.activities;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;

import com.github.veselinazatchepina.bookstatistics.MyApplication;
import com.github.veselinazatchepina.bookstatistics.R;
import com.github.veselinazatchepina.bookstatistics.abstracts.NavigationAbstractActivity;
import com.github.veselinazatchepina.bookstatistics.chart.fragments.ChartFragment;
import com.squareup.leakcanary.RefWatcher;

import butterknife.BindView;

import static com.github.veselinazatchepina.bookstatistics.R.id.fab;

public class ChartActivity extends NavigationAbstractActivity {

    private static final String CHART_ACTIVITY_TITLE = "chart_activity_title";

    @BindView(fab)
    FloatingActionButton mFloatingActionButton;

    private Fragment mMainFragment;

    public static Intent newIntent(Context context, String title) {
        Intent intent = new Intent(context, ChartActivity.class);
        intent.putExtra(CHART_ACTIVITY_TITLE, title);
        return intent;
    }

    @Override
    public void defineInputData(Bundle savedInstanceState) {
        setTitleToActivity();
    }

    private void setTitleToActivity() {
        if (getIntent().getStringExtra(CHART_ACTIVITY_TITLE) != null) {
            setTitle(getIntent().getStringExtra(CHART_ACTIVITY_TITLE));
        }
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
        FragmentManager fragmentManager = getSupportFragmentManager();
        mMainFragment = fragmentManager.findFragmentById(R.id.fragment_container);
        if (mMainFragment == null) {
            mMainFragment = ChartFragment.newInstance();
            fragmentManager.beginTransaction()
                    .add(R.id.fragment_container, mMainFragment)
                    .commit();
        }
    }

    @Override
    public void defineFab() {
        mFloatingActionButton.setVisibility(View.GONE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RefWatcher refWatcher = MyApplication.getRefWatcher(this);
        refWatcher.watch(this);
    }
}
