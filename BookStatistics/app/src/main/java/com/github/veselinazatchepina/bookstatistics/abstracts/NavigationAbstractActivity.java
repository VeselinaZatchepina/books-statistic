package com.github.veselinazatchepina.bookstatistics.abstracts;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.Surface;
import android.view.WindowManager;

import com.github.veselinazatchepina.bookstatistics.R;
import com.github.veselinazatchepina.bookstatistics.books.activities.AddBookActivity;
import com.github.veselinazatchepina.bookstatistics.books.activities.BookCategoriesMainActivity;
import com.github.veselinazatchepina.bookstatistics.chart.activities.ChartActivity;
import com.github.veselinazatchepina.bookstatistics.utils.AppBarLayoutExpended;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * NavigationAbstractActivity helps avoid boilerplate code.
 */
public abstract class NavigationAbstractActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.navigation_view)
    NavigationView mNavigationView;
    @BindView(R.id.appbar_layout)
    AppBarLayout mAppBarLayout;
    @BindView(R.id.collapsing_toolbar_layout)
    CollapsingToolbarLayout mCollapsingToolbarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        defineInputData(savedInstanceState);
        setContentView(getLayoutResId());
        ButterKnife.bind(this);
        defineNavigationDrawer();
        defineAppBarLayoutExpandableValue();
        defineFragment();
        defineFab();
    }

    public abstract void defineInputData(Bundle savedInstanceState);

    @LayoutRes
    public abstract int getLayoutResId();

    private void defineNavigationDrawer() {
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.setDrawerListener(toggle);
        toggle.syncState();
        mNavigationView.setNavigationItemSelectedListener(this);
    }

    public void defineAppBarLayoutExpandableValue() {
        getScreenOrientation(this);
    }

    /**
     * Method checks screen orientation. And if it on landscape or reverse landscape we set
     * AppBarLayout not expandable
     *
     * @param context context
     * @return screen orientation as string
     */
    public String getScreenOrientation(Context context) {
        final int screenOrientation = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE))
                .getDefaultDisplay().getOrientation();
        switch (screenOrientation) {
            case Surface.ROTATION_0:
                return "android portrait screen";
            case Surface.ROTATION_90:
                setAppBarNotExpandable();
                return "android landscape screen";
            case Surface.ROTATION_180:
                return "android reverse portrait screen";
            default:
                setAppBarNotExpandable();
                return "android reverse landscape screen";
        }
    }

    private void setAppBarNotExpandable() {
        if (mAppBarLayout != null) {
            CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) mAppBarLayout.getLayoutParams();
            Configuration configuration = getResources().getConfiguration();
            AppBarLayoutExpended.setAppBarLayoutExpended(this, mAppBarLayout, layoutParams,
                    mCollapsingToolbarLayout, configuration);
        }
    }

    public abstract void defineFragment();

    public abstract void defineFab();

    public int setFabImageResourceId() {
        return R.drawable.ic_add_white_24dp;
    }

    public void setFabBackgroundImage(FloatingActionButton fab, int imageResourceId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            fab.setImageDrawable(getResources().getDrawable(imageResourceId, getTheme()));
        } else {
            fab.setImageDrawable(getResources().getDrawable(imageResourceId));
        }
    }

    public void defineActionWhenFabIsPressed() {
        Intent intent = AddBookActivity.newIntent(this);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Intent intent = null;
        switch (item.getItemId()) {
            case R.id.menu_book_categories:
                intent = BookCategoriesMainActivity.newIntent(this);
                break;
            case R.id.menu_chart:
                intent = ChartActivity.newIntent(this);
                break;
        }
        if (intent != null) {
            startActivity(intent);
        }
        return true;
    }
}
