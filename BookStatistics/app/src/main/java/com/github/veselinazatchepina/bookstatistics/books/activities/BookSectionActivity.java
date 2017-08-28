package com.github.veselinazatchepina.bookstatistics.books.activities;


import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.github.veselinazatchepina.bookstatistics.MyApplication;
import com.github.veselinazatchepina.bookstatistics.R;
import com.github.veselinazatchepina.bookstatistics.books.enums.BookTypeEnums;
import com.github.veselinazatchepina.bookstatistics.books.fragments.BookSectionFragment;
import com.github.veselinazatchepina.bookstatistics.utils.ColorationTextChar;
import com.squareup.leakcanary.RefWatcher;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BookSectionActivity extends AppCompatActivity implements BookSectionFragment.CurrentBookCallbacks {

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
        defineActionBar();
        defineTabLayout();
        defineViewPager();
        defineFab();
    }

    private void defineTitle() {
        String oldTitle = getTitle().toString();
        String title = Character.toUpperCase(oldTitle.charAt(0)) + oldTitle.substring(1);;
        setTitle(ColorationTextChar.setFirstVowelColor(title, this));
    }

    private void defineInputData() {
        createSectionTypeList();
        mCurrentCategory = getIntent().getStringExtra(BOOK_CATEGORY_INTENT);
        setTitleToActivity();
    }

    private void defineActionBar() {
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void defineTabLayout() {
        mTabLayout.setupWithViewPager(mSectionViewPager);
    }

    private void createSectionTypeList() {
        mSectionTypes = new ArrayList<>();
        mSectionTypes.add(BookTypeEnums.NEW_BOOK);
        mSectionTypes.add(BookTypeEnums.CURRENT_BOOK);
        mSectionTypes.add(BookTypeEnums.READ_BOOK);
    }

    private void setTitleToActivity() {
        if (getIntent().getStringExtra(BOOK_CATEGORY_TITLE) != null) {
            setTitle(getIntent().getStringExtra(BOOK_CATEGORY_TITLE));
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
                    System.out.println("Catch the NullPointerException in FragmentPagerAdapter.finishUpdate");
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
        Intent intent = AddBookActivity.newIntent(this, "Add book");
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
}
