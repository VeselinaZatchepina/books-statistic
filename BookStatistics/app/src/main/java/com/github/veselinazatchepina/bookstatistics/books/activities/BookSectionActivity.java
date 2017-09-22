package com.github.veselinazatchepina.bookstatistics.books.activities;


import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.TextAppearanceSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.github.veselinazatchepina.bookstatistics.MyApplication;
import com.github.veselinazatchepina.bookstatistics.R;
import com.github.veselinazatchepina.bookstatistics.books.enums.BookSectionEnums;
import com.github.veselinazatchepina.bookstatistics.books.fragments.BookSectionFragment;
import com.github.veselinazatchepina.bookstatistics.chart.activities.ChartActivity;
import com.github.veselinazatchepina.bookstatistics.preference.activities.ThemePreferencesActivity;
import com.github.veselinazatchepina.bookstatistics.preference.activities.WriteToDeveloperActivity;
import com.github.veselinazatchepina.bookstatistics.utils.ColorationTextChar;
import com.squareup.leakcanary.RefWatcher;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BookSectionActivity extends AppCompatActivity implements BookSectionFragment.CurrentBookCallbacks,
        NavigationView.OnNavigationItemSelectedListener {

    private static final String BOOK_CATEGORY_INTENT = "book_category_intent";
    private static final String BOOK_CATEGORY_TITLE = "book_category_title";

    @BindView(R.id.view_pager)
    ViewPager mSectionViewPager;
    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;
    @BindView(R.id.section_fab)
    FloatingActionButton mSectionFab;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.navigation_view)
    NavigationView mNavigationView;

    private ArrayList<String> mSectionTypes;
    private String mCurrentCategory;

    public static Intent newIntent(Context context, String title) {
        Intent intent = new Intent(context, BookSectionActivity.class);
        intent.putExtra(BOOK_CATEGORY_TITLE, title);
        return intent;
    }

    public static Intent newIntent(Context context, String currentCategory, String title) {
        Intent intent = new Intent(context, BookSectionActivity.class);
        intent.putExtra(BOOK_CATEGORY_INTENT, currentCategory);
        intent.putExtra(BOOK_CATEGORY_TITLE, title);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_section);
        ButterKnife.bind(this);
        defineInputData();
        defineTitle();
        defineTabLayout();
        defineActionbar();
        if (mCurrentCategory == null) {
            defineNavigationDrawer();
        }
        defineViewPager();
        defineFab();
    }

    private void defineActionbar() {
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void defineNavigationDrawer() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.setDrawerListener(toggle);
        toggle.syncState();
        Menu menu = mNavigationView.getMenu();
        MenuItem tools = menu.findItem(R.id.other);
        SpannableString s = new SpannableString(tools.getTitle());
        s.setSpan(new TextAppearanceSpan(this, R.style.NavigationViewStyle), 0, s.length(), 0);
        tools.setTitle(s);
        mNavigationView.setNavigationItemSelectedListener(this);
    }

    private void defineTitle() {
        String oldTitle = getTitle().toString();
        String title = Character.toUpperCase(oldTitle.charAt(0)) + oldTitle.substring(1);
        setTitle(ColorationTextChar.setFirstVowelColor(title, this));
    }

    private void defineInputData() {
        createSectionTypeList();
        mCurrentCategory = getIntent().getStringExtra(BOOK_CATEGORY_INTENT);
        setTitleToActivity();
    }

    private void defineTabLayout() {
        mTabLayout.setupWithViewPager(mSectionViewPager);
    }

    private void createSectionTypeList() {
        mSectionTypes = new ArrayList<>();
        mSectionTypes.add(BookSectionEnums.NEW_BOOK);
        mSectionTypes.add(BookSectionEnums.CURRENT_BOOK);
        mSectionTypes.add(BookSectionEnums.READ_BOOK);
    }

    private void setTitleToActivity() {
        String titleFromIntent = getIntent().getStringExtra(BOOK_CATEGORY_TITLE);
        if (titleFromIntent != null) {
            setTitle(titleFromIntent);
        }
    }

    private void defineViewPager() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        mSectionViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                Fragment mainFragment;
                if (mCurrentCategory != null) {
                    mainFragment = BookSectionFragment.newInstance(mSectionTypes.get(position), mCurrentCategory);
                } else {
                    mainFragment = BookSectionFragment.newInstance(mSectionTypes.get(position));
                }
                return mainFragment;
            }

            @Override
            public int getCount() {
                return mSectionTypes.size();
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return mSectionTypes.get(position);
            }

            @Override
            public void finishUpdate(ViewGroup container) {
                try {
                    super.finishUpdate(container);
                } catch (NullPointerException nullPointerException) {
                    System.out.println(getString(R.string.view_pager_exception));
                }
            }
        });
    }

    private void defineFab() {
        setFabImage();
        mSectionFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                defineActionWhenFabIsPressed();
            }
        });
    }

    private void setFabImage() {
        int fabImageResourceId = setFabImageResourceId();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mSectionFab.setImageDrawable(getResources().getDrawable(fabImageResourceId, getTheme()));
        } else {
            mSectionFab.setImageDrawable(getResources().getDrawable(fabImageResourceId));
        }
    }

    private int setFabImageResourceId() {
        return R.drawable.ic_add_white_24dp;
    }

    private void defineActionWhenFabIsPressed() {
        int currentSectionType = mSectionViewPager.getCurrentItem();
        Intent intent = AddBookActivity.newIntent(this, getString(R.string.add_book_title), currentSectionType, mCurrentCategory);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBookSelected(long currentBookId, String currentSectionType, String currentCategory) {
        startActivity(CurrentBookActivity.newIntent(this, currentBookId, currentSectionType, currentCategory));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RefWatcher refWatcher = MyApplication.getRefWatcher(this);
        refWatcher.watch(this);
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
                intent = BookCategoriesMainActivity.newIntent(this, getString(R.string.book_categories_title));
                break;
            case R.id.menu_chart:
                intent = ChartActivity.newIntent(this, getString(R.string.chart_title));
                break;
            case R.id.menu_all_books:
                intent = BookSectionActivity.newIntent(this, getString(R.string.all_books_title));
                break;
            case R.id.settings:
                intent = ThemePreferencesActivity.newIntent(this, getString(R.string.settings_title));
                break;
            case R.id.write_to_developer:
                intent = WriteToDeveloperActivity.newIntent(this, getString(R.string.write_to_developer_title));
        }
        if (intent != null) {
            startActivity(intent);
        }
        return true;
    }
}
