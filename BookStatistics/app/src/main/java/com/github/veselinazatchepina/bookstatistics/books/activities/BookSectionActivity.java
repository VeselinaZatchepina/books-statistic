package com.github.veselinazatchepina.bookstatistics.books.activities;


import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.github.veselinazatchepina.bookstatistics.MyApplication;
import com.github.veselinazatchepina.bookstatistics.R;
import com.github.veselinazatchepina.bookstatistics.books.enums.BookTypeEnums;
import com.github.veselinazatchepina.bookstatistics.books.fragments.BookSectionFragment;
import com.squareup.leakcanary.RefWatcher;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.hugeterry.coordinatortablayout.CoordinatorTabLayout;

public class BookSectionActivity extends AppCompatActivity implements BookSectionFragment.CurrentBookCallbacks {

    private static final String BOOK_CATEGORY_INTENT = "book_category_intent";

    @BindView(R.id.view_pager)
    ViewPager mSectionViewPager;
    @BindView(R.id.coordinator_tab_layout)
    CoordinatorTabLayout mCoordinatorTabLayout;
    @BindView(R.id.section_fab)
    FloatingActionButton mSectionFab;

    private Fragment mMainFragment;
    private ArrayList<String> mSectionTypes;
    private String mCurrentCategory;

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, BookSectionActivity.class);
        return intent;
    }

    public static Intent newIntent(Context context, String currentCategory) {
        Intent intent = new Intent(context, BookSectionActivity.class);
        intent.putExtra(BOOK_CATEGORY_INTENT, currentCategory);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_section);
        ButterKnife.bind(this);
        defineInputData();
        defineViewPager();
        defineTabLayout();
        defineFab();
    }

    private void defineInputData() {
        createSectionTypeList();
        mCurrentCategory = getIntent().getStringExtra(BOOK_CATEGORY_INTENT);
    }

    private void createSectionTypeList() {
        mSectionTypes = new ArrayList<>();
        mSectionTypes.add(BookTypeEnums.NEW_BOOK);
        mSectionTypes.add(BookTypeEnums.CURRENT_BOOK);
        mSectionTypes.add(BookTypeEnums.READ_BOOK);
    }

    private void defineViewPager() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        mSectionViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                if (mCurrentCategory != null) {
                    mMainFragment = BookSectionFragment.newInstance(mSectionTypes.get(position), mCurrentCategory);
                } else {
                    mMainFragment = BookSectionFragment.newInstance(mSectionTypes.get(position));
                }
                return mMainFragment;
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
                try{
                    super.finishUpdate(container);
                } catch (NullPointerException nullPointerException){
                    System.out.println("Catch the NullPointerException in FragmentPagerAdapter.finishUpdate");
                }
            }
        });
    }

    private void defineTabLayout() {
        mCoordinatorTabLayout.setTitle("Book sections")
                .setupWithViewPager(mSectionViewPager)
                .setBackEnable(true);;
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
        Intent intent = AddBookActivity.newIntent(this);
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
