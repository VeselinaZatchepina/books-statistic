package com.github.veselinazatchepina.bookstatistics.abstracts;


import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
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

    public Fragment currentFragment;
    public FloatingActionButton fab;
    public int fabImageResourceId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        defineInputData(savedInstanceState);
        setContentView(getLayoutResId());
        ButterKnife.bind(this);
        defineToolbar();
        setAppBarNotExpandable();
        setNewTitleStyle();
        defineFragment();
        defineFab();
    }

    public void defineInputData(Bundle saveInstanceState) { }

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

    private void defineFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        currentFragment = fragmentManager.findFragmentById(R.id.fragment_container);
        if (currentFragment == null) {
            currentFragment = createFragment();
            fragmentManager.beginTransaction()
                    .add(R.id.fragment_container, currentFragment)
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
        fabImageResourceId = setFabImageResourceId();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mFloatingActionButton.setImageDrawable(getResources().getDrawable(fabImageResourceId, getTheme()));
        } else {
            mFloatingActionButton.setImageDrawable(getResources().getDrawable(fabImageResourceId));
        }
    }

    public int setFabImageResourceId() {
        return R.drawable.ic_add_white_24dp;
    }

    public void defineActionWhenFabIsPressed() {
        Intent intent = AddBookActivity.newIntent(this);
        startActivity(intent);
    }
}
