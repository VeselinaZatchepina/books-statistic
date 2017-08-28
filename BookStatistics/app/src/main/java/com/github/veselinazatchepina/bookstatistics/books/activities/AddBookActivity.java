package com.github.veselinazatchepina.bookstatistics.books.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.MenuItem;
import android.widget.Toast;

import com.github.veselinazatchepina.bookstatistics.MyApplication;
import com.github.veselinazatchepina.bookstatistics.R;
import com.github.veselinazatchepina.bookstatistics.abstracts.SingleFragmentAbstractActivity;
import com.github.veselinazatchepina.bookstatistics.books.fragments.AddBookFragment;
import com.squareup.leakcanary.RefWatcher;


public class AddBookActivity extends SingleFragmentAbstractActivity {

    private static final String CURRENT_BOOK_ID_INTENT = "current_book_id_intent";
    private static final String CURRENT_BOOK_TITLE = "current_book_title";
    private static final String CURRENT_BOOK_SECTION_TYPE = "current_book_section_type";

    private Fragment mCurrentFragment;
    private long mCurrentBookId;
    private int mCurrentSectionType;

    public static Intent newIntent(Context context, String title) {
        Intent intent = new Intent(context, AddBookActivity.class);
        intent.putExtra(CURRENT_BOOK_TITLE, title);
        return intent;
    }

    public static Intent newIntent(Context context, long currentBookId, String title) {
        Intent intent = new Intent(context, AddBookActivity.class);
        intent.putExtra(CURRENT_BOOK_ID_INTENT, currentBookId);
        intent.putExtra(CURRENT_BOOK_TITLE, title);
        return intent;
    }

    public static Intent newIntent(Context context, String title, int sectionType) {
        Intent intent = new Intent(context, AddBookActivity.class);
        intent.putExtra(CURRENT_BOOK_TITLE, title);
        intent.putExtra(CURRENT_BOOK_SECTION_TYPE, sectionType);
        return intent;
    }

    @Override
    public void defineInputData(Bundle saveInstanceState) {
        super.defineInputData(saveInstanceState);
        mCurrentBookId = getIntent().getLongExtra(CURRENT_BOOK_ID_INTENT, -1);
        mCurrentSectionType = getIntent().getIntExtra(CURRENT_BOOK_SECTION_TYPE, -1);
        setTitleToActivity();
    }

    private void setTitleToActivity() {
        if (getIntent().getStringExtra(CURRENT_BOOK_TITLE) != null) {
            setTitle(getIntent().getStringExtra(CURRENT_BOOK_TITLE));
        }
    }

    @Override
    public Fragment createFragment() {
        mCurrentFragment = AddBookFragment.newInstance(mCurrentBookId, mCurrentSectionType);
        return mCurrentFragment;
    }

    @Override
    public int setFabImageResourceId() {
        return R.drawable.ic_check_white_24dp;
    }

    @Override
    public void defineActionWhenFabIsPressed() {
        AddBookFragment addBookFragment = ((AddBookFragment) currentFragment);
        if (!addBookFragment.isSpinnerSelectedItemHint() && addBookFragment.isPagePositiveNumber()
                && addBookFragment.isDateExistInReadSectionType()) {
            addBookFragment.createMapOfBookProperties();
            this.finish();
        }
        if (addBookFragment.isSpinnerSelectedItemHint()) {
            Toast.makeText(this, getString(R.string.toast_choose_category), Toast.LENGTH_LONG).show();
        }
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
    protected void onDestroy() {
        super.onDestroy();
        RefWatcher refWatcher = MyApplication.getRefWatcher(this);
        refWatcher.watch(this);
    }
}
