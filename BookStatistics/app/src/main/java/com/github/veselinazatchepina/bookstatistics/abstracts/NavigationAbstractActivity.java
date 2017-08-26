package com.github.veselinazatchepina.bookstatistics.abstracts;

import android.content.Context;
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
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.TextAppearanceSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Surface;
import android.view.WindowManager;

import com.github.veselinazatchepina.bookstatistics.R;
import com.github.veselinazatchepina.bookstatistics.books.activities.AddBookActivity;
import com.github.veselinazatchepina.bookstatistics.books.activities.BookCategoriesMainActivity;
import com.github.veselinazatchepina.bookstatistics.books.activities.BookSectionActivity;
import com.github.veselinazatchepina.bookstatistics.chart.activities.ChartActivity;
import com.github.veselinazatchepina.bookstatistics.preference.activities.ThemePreferencesActivity;
import com.github.veselinazatchepina.bookstatistics.preference.activities.WriteToDeveloperActivity;
import com.github.veselinazatchepina.bookstatistics.utils.AppBarLayoutExpended;
import com.github.veselinazatchepina.bookstatistics.utils.ColorationTextChar;
import com.github.veselinazatchepina.bookstatistics.utils.ThemeUtils;

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

    SharedPreferences prefs;
    SharedPreferences.OnSharedPreferenceChangeListener prefListener;
    String value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        definePreferenceListener();
        ThemeUtils.onActivityCreateSetTheme(this);
        defineInputData(savedInstanceState);
        setContentView(getLayoutResId());
        ButterKnife.bind(this);
        setTitle(ColorationTextChar.setFirstVowelColor(getTitle().toString(), this));
        defineNavigationDrawer();
        defineAppBarLayoutExpandableValue();
        defineFragment();
        defineFab();
    }

    private void definePreferenceListener() {
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
                value = prefs.getString(key, null);
                ThemeUtils.changeToTheme(NavigationAbstractActivity.this, value);
            }
        };
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
        Menu menu = mNavigationView.getMenu();
        MenuItem tools= menu.findItem(R.id.other);
        SpannableString s = new SpannableString(tools.getTitle());
        s.setSpan(new TextAppearanceSpan(this, R.style.NavigationViewStyle), 0, s.length(), 0);
        tools.setTitle(s);
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

    public void setAppBarNotExpandable() {
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
        Intent intent = AddBookActivity.newIntent(this, "Add book");
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
                intent = BookCategoriesMainActivity.newIntent(this, "Book categories");
                break;
            case R.id.menu_chart:
                intent = ChartActivity.newIntent(this, "Charts");
                break;
            case R.id.menu_all_books:
                intent = BookSectionActivity.newIntent(this, "All books");
                break;
            case R.id.settings:
                intent = ThemePreferencesActivity.newIntent(this, "Settings");
                break;
            case R.id.write_to_developer:
                intent = WriteToDeveloperActivity.newIntent(this, "Write to developer");
        }
        if (intent != null) {
            startActivity(intent);
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        prefs.registerOnSharedPreferenceChangeListener(prefListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        prefs.unregisterOnSharedPreferenceChangeListener(prefListener);
    }
}
