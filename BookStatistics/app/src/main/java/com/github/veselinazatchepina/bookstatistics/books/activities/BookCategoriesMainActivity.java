package com.github.veselinazatchepina.bookstatistics.books.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;

import com.github.veselinazatchepina.bookstatistics.MyApplication;
import com.github.veselinazatchepina.bookstatistics.R;
import com.github.veselinazatchepina.bookstatistics.abstracts.NavigationAbstractActivity;
import com.github.veselinazatchepina.bookstatistics.books.fragments.BookCategoriesFragment;
import com.squareup.leakcanary.RefWatcher;

import butterknife.BindView;

import static com.github.veselinazatchepina.bookstatistics.R.id.fab;

public class BookCategoriesMainActivity extends NavigationAbstractActivity
        implements BookCategoriesFragment.BookCategoryCallbacks {

    private static final String BOOK_CATEGORY_TITLE = "book_category_title";

    @BindView(fab)
    FloatingActionButton mFloatingActionButton;

    private int mFabImageResourceId = setFabImageResourceId();

    public static Intent newIntent(Context context, String title) {
        Intent intent = new Intent(context, BookCategoriesMainActivity.class);
        intent.putExtra(BOOK_CATEGORY_TITLE, title);
        return intent;
    }

    @Override
    public void defineInputData(Bundle saveInstanceState) {
        if (getIntent().getStringExtra(BOOK_CATEGORY_TITLE) != null) {
            setTitle(getIntent().getStringExtra(BOOK_CATEGORY_TITLE));
        } else {
            setTitle(getString(R.string.book_categories_title));
        }
    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_navigation_drawer;
    }

    @Override
    public void defineFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment currentFragment = fragmentManager.findFragmentById(R.id.fragment_container);
        if (currentFragment == null) {
            currentFragment = BookCategoriesFragment.newInstance();
            fragmentManager.beginTransaction()
                    .add(R.id.fragment_container, currentFragment)
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
    public void onCategorySelected(String currentCategory) {
        startActivity(BookSectionActivity.newIntent(this, currentCategory, currentCategory));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RefWatcher refWatcher = MyApplication.getRefWatcher(this);
        refWatcher.watch(this);
    }
}
