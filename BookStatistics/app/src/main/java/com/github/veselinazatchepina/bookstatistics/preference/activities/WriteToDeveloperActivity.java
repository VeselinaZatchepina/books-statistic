package com.github.veselinazatchepina.bookstatistics.preference.activities;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;

import com.github.veselinazatchepina.bookstatistics.R;
import com.github.veselinazatchepina.bookstatistics.abstracts.NavigationAbstractActivity;
import com.github.veselinazatchepina.bookstatistics.preference.fragments.WriteToDeveloperFragment;

import butterknife.BindView;

import static com.github.veselinazatchepina.bookstatistics.R.id.fab;

public class WriteToDeveloperActivity extends NavigationAbstractActivity {

    @BindView(fab)
    FloatingActionButton mFloatingActionButton;
    private Fragment mMainFragment;

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, WriteToDeveloperActivity.class);
        return intent;
    }

    @Override
    public void defineInputData(Bundle savedInstanceState) {

    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_navigation_drawer;
    }

    @Override
    public void defineFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        mMainFragment = fragmentManager.findFragmentById(R.id.fragment_container);
        if (mMainFragment == null) {
            mMainFragment = WriteToDeveloperFragment.newInstance();
            fragmentManager.beginTransaction()
                    .add(R.id.fragment_container, mMainFragment)
                    .commit();
        }
    }

    @Override
    public void defineFab() {
        mFloatingActionButton.setVisibility(View.GONE);
    }
}
