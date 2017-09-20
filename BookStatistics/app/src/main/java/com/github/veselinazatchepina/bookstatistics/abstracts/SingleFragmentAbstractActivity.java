package com.github.veselinazatchepina.bookstatistics.abstracts;


import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.LayoutRes;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.github.veselinazatchepina.bookstatistics.R;
import com.github.veselinazatchepina.bookstatistics.books.activities.AddBookActivity;
import com.github.veselinazatchepina.bookstatistics.utils.AppBarLayoutExpended;
import com.github.veselinazatchepina.bookstatistics.utils.ColorationTextChar;
import com.github.veselinazatchepina.bookstatistics.utils.ThemeUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * SingleFragmentAbstractActivity helps avoid boilerplate code.
 */
public abstract class SingleFragmentAbstractActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.appbar_layout)
    AppBarLayout mAppBarLayout;
    @BindView(R.id.collapsing_toolbar_layout)
    CollapsingToolbarLayout mCollapsingToolbarLayout;
    @BindView(R.id.fab)
    FloatingActionButton mFloatingActionButton;

    private SharedPreferences mPrefs;
    private SharedPreferences.OnSharedPreferenceChangeListener mPrefListener;

    public Fragment mCurrentFragment;
    public int mFabImageResourceId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        definePreferenceListener();
        ThemeUtils.onActivityCreateSetTheme(this);
        defineInputData(savedInstanceState);
        setTitle(ColorationTextChar.setFirstVowelColor(getTitle().toString(), this));
        setContentView(getLayoutResId());
        ButterKnife.bind(this);
        defineToolbar();
        setAppBarNotExpandable();
        setNewTitleStyle();
        defineFragment();
        defineFab();
    }

    private void definePreferenceListener() {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        mPrefListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
                String currentPreferenceThemeName = prefs.getString(key, null);
                ThemeUtils.changeToTheme(SingleFragmentAbstractActivity.this, currentPreferenceThemeName);
            }
        };
    }

    public void defineInputData(Bundle saveInstanceState) {
    }

    @LayoutRes
    protected int getLayoutResId() {
        return R.layout.activity_single_fragment;
    }

    private void defineToolbar() {
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void setAppBarNotExpandable() {
        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) mAppBarLayout.getLayoutParams();
        Configuration configuration = getResources().getConfiguration();
        AppBarLayoutExpended.setAppBarLayoutExpended(this, mAppBarLayout, layoutParams, mCollapsingToolbarLayout, configuration);
    }

    public void setNewTitleStyle() {
        setTitle(ColorationTextChar.setFirstVowelColor(getTitle().toString(), this));
    }

    public void defineFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        mCurrentFragment = fragmentManager.findFragmentById(R.id.fragment_container);
        if (mCurrentFragment == null) {
            mCurrentFragment = createFragment();
            fragmentManager.beginTransaction()
                    .add(R.id.fragment_container, mCurrentFragment)
                    .commit();
        }
    }

    public abstract Fragment createFragment();

    private void defineFab() {
        setFabImage();
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                defineActionWhenFabIsPressed();
            }
        });
    }

    private void setFabImage() {
        mFabImageResourceId = setFabImageResourceId();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mFloatingActionButton.setImageDrawable(getResources().getDrawable(mFabImageResourceId, getTheme()));
        } else {
            mFloatingActionButton.setImageDrawable(getResources().getDrawable(mFabImageResourceId));
        }
    }

    public int setFabImageResourceId() {
        return R.drawable.ic_add_white_24dp;
    }

    public void defineActionWhenFabIsPressed() {
        Intent intent = AddBookActivity.newIntent(this, getString(R.string.add_book_title));
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPrefs.registerOnSharedPreferenceChangeListener(mPrefListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPrefs.unregisterOnSharedPreferenceChangeListener(mPrefListener);
    }
}
