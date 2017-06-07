package com.github.veselinazatchepina.bookstatistics.books.activities;

import android.os.Bundle;

import com.github.veselinazatchepina.bookstatistics.R;
import com.github.veselinazatchepina.bookstatistics.abstracts.NavigationAbstractActivity;

public class BookCategoriesMainActivity extends NavigationAbstractActivity {

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
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        mMainFragment = fragmentManager.findFragmentById(R.id.container);
//        if (mMainFragment == null) {
//            mMainFragment = QuoteCategoryFragment.newInstance(mQuoteType);
//            fragmentManager.beginTransaction()
//                    .add(R.id.container, mMainFragment)
//                    .commit();
//        }
    }

    @Override
    public void defineFab() {
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        setFabBackgroundImage(fab, mFabImageResourceId);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                defineActionWhenFabIsPressed();
//            }
//        });
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
