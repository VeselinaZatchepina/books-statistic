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
import com.github.veselinazatchepina.bookstatistics.utils.AppBarLayoutExpended;

/**
 * NavigationAbstractActivity helps avoid boilerplate code.
 */
public abstract class NavigationAbstractActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout mDrawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        defineInputData(savedInstanceState);
        setContentView(getLayoutResId());
        defineNavigationDrawer();
        defineAppBarLayoutExpandableValue();
        defineFragment();
        defineFab();
    }

    public abstract void defineInputData(Bundle savedInstanceState);

    @LayoutRes
    public abstract int getLayoutResId();

    private void defineNavigationDrawer() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);
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
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar_layout);
        if (appBarLayout != null) {
            CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) appBarLayout.getLayoutParams();
            CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_layout);
            Configuration configuration = getResources().getConfiguration();
            AppBarLayoutExpended.setAppBarLayoutExpended(this, appBarLayout, layoutParams,
                    collapsingToolbarLayout, configuration);
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
//        Intent intent = AddQuoteActivity.newIntent(this, getTitle().toString());
//        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawer(GravityCompat.START);
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
                //intent = BookCategoriesMainActivity.newIntent(this, Types.BOOK_QUOTE);
                break;
        }
        if (intent != null) {
            startActivity(intent);
        }
        return true;
    }
}
