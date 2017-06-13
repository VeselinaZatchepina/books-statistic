package com.github.veselinazatchepina.bookstatistics.books.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;

import com.github.veselinazatchepina.bookstatistics.R;
import com.github.veselinazatchepina.bookstatistics.abstracts.NavigationAbstractActivity;
import com.github.veselinazatchepina.bookstatistics.books.fragments.BookCategoriesFragment;

import butterknife.BindView;

import static com.github.veselinazatchepina.bookstatistics.R.id.fab;

public class BookCategoriesMainActivity extends NavigationAbstractActivity {

    @BindView(fab)
    FloatingActionButton mFloatingActionButton;
    private Fragment mMainFragment;
    private int mFabImageResourceId = setFabImageResourceId();

    public static Intent newIntent(Context context) {
        return new Intent(context, BookCategoriesMainActivity.class);
    }

    @Override
    public void defineInputData(Bundle saveInstanceState) {
//        if (saveInstanceState != null) {
//            mMainFragment = getSupportFragmentManager().getFragment(saveInstanceState, MAIN_FRAGMENT_TAG_BUNDLE);
//            mTitle = saveInstanceState.getString(QUOTE_TYPE_BUNDLE);
//            mCurrentId = saveInstanceState.getLong(CURRENT_ID_BUNDLE);
//        } else if (getIntent().getStringExtra(QUOTE_TYPE_INTENT) != null) {
//            mTitle = getIntent().getStringExtra(QUOTE_TYPE_INTENT);
//        } else {
//            mTitle = Types.BOOK_QUOTE;
//        }
//        setTitle(mTitle);
//        defineQuoteType();
    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_navigation_drawer;
    }

    @Override
    public void defineFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        mMainFragment = fragmentManager.findFragmentById(R.id.fragment_container);
        if (mMainFragment == null) {
            mMainFragment = BookCategoriesFragment.newInstance();
            fragmentManager.beginTransaction()
                    .add(R.id.fragment_container, mMainFragment)
                    .commit();
        }
    }

    @Override
    public void defineFab() {
        setFabBackgroundImage(mFloatingActionButton, mFabImageResourceId);
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                defineActionWhenFabIsPressed();
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
//        if (mMainFragment.isAdded()) {
//            getSupportFragmentManager().putFragment(outState, MAIN_FRAGMENT_TAG_BUNDLE, mMainFragment);
//        }
//        outState.putString(QUOTE_TYPE_BUNDLE, mTitle);
//        outState.putLong(CURRENT_ID_BUNDLE, mCurrentId);
//        super.onSaveInstanceState(outState);
    }
}
