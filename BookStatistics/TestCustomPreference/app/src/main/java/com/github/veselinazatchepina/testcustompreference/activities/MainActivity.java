package com.github.veselinazatchepina.testcustompreference.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;

import com.github.veselinazatchepina.testcustompreference.R;
import com.github.veselinazatchepina.testcustompreference.abstracts.NavigationAbstractActivity;

import butterknife.BindView;

public class MainActivity extends NavigationAbstractActivity {

    @BindView(R.id.fab)
    FloatingActionButton mFab;

    @Override
    public void defineInputData(Bundle savedInstanceState) {

    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_navigation_drawer;
    }

    @Override
    public void defineFragment() {

    }

    @Override
    public void defineFab() {
        setFabBackgroundImage(mFab, setFabImageResourceId());
    }

}
