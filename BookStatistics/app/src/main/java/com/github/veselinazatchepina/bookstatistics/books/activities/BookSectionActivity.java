package com.github.veselinazatchepina.bookstatistics.books.activities;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;

import com.github.veselinazatchepina.bookstatistics.R;
import com.github.veselinazatchepina.bookstatistics.books.enums.BookTypeEnums;
import com.github.veselinazatchepina.bookstatistics.books.fragments.BookSectionFragment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.hugeterry.coordinatortablayout.CoordinatorTabLayout;

public class BookSectionActivity extends AppCompatActivity {

    @BindView(R.id.view_pager)
    ViewPager mSectionViewPager;
    @BindView(R.id.coordinator_tab_layout)
    CoordinatorTabLayout mCoordinatorTabLayout;

    private Fragment mMainFragment;
    private ArrayList<String> mSectionTypes;

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, BookSectionActivity.class);
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
    }

    private void defineInputData() {
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
                mMainFragment = BookSectionFragment.newInstance(mSectionTypes.get(position));
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
}
