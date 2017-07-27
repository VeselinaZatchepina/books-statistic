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

    private Fragment mCurrentFragment;
    private long mCurrentBookId;

    public static Intent newIntent(Context context) {
        return new Intent(context, AddBookActivity.class);
    }

    public static Intent newIntent(Context context, long currentBookId) {
        Intent intent = new Intent(context, AddBookActivity.class);
        intent.putExtra(CURRENT_BOOK_ID_INTENT, currentBookId);
        return intent;
    }

    @Override
    public void defineInputData(Bundle saveInstanceState) {
        super.defineInputData(saveInstanceState);
        mCurrentBookId = getIntent().getLongExtra(CURRENT_BOOK_ID_INTENT, -1);
    }

    @Override
    public Fragment createFragment() {
        mCurrentFragment = AddBookFragment.newInstance(mCurrentBookId);
        return mCurrentFragment;
    }

    @Override
    public int setFabImageResourceId() {
        return R.drawable.ic_check_white_24dp;
    }

    @Override
    public void defineActionWhenFabIsPressed() {
        AddBookFragment addBookFragment = ((AddBookFragment) currentFragment);
        if (!addBookFragment.isSpinnerSelectedItemHint() && addBookFragment.isPagePositiveNumber()) {
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
