package com.github.veselinazatchepina.bookstatistics.books.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.MenuItem;

import com.github.veselinazatchepina.bookstatistics.R;
import com.github.veselinazatchepina.bookstatistics.abstracts.SingleFragmentAbstractActivity;
import com.github.veselinazatchepina.bookstatistics.books.fragments.AddBookFragment;


public class AddBookActivity extends SingleFragmentAbstractActivity {

    private Fragment mCurrentFragment;

    @Override
    public void defineInputData(Bundle saveInstanceState) {
        super.defineInputData(saveInstanceState);
//        if (saveInstanceState != null) {
//            mCurrentFragment = getSupportFragmentManager().getFragment(saveInstanceState, CURRENT_FRAGMENT_TAG_BUNDLE);
//            mQuotesType = saveInstanceState.getString(QUOTE_TYPE_BUNDLE);
//        } else {
//            mQuotesType = getIntent().getStringExtra(QUOTE_TYPE_INTENT);
//            mCurrentCategory = getIntent().getStringExtra(QUOTE_CATEGORY_INTENT);
//            mCurrentId = getIntent().getLongExtra(QUOTE_TEXT_ID_INTENT, -1);
//        }
    }

    @Override
    public Fragment createFragment() {
        mCurrentFragment = AddBookFragment.newInstance();
        return mCurrentFragment;
    }

    @Override
    public int setFabImageResourceId() {
        return R.drawable.ic_check_white_24dp;
    }

    @Override
    public void defineActionWhenFabIsPressed() {
//        AddQuoteFragment addQuoteFragment = ((AddQuoteFragment) currentFragment);
//        if (!addQuoteFragment.isEditTextEmpty() && !addQuoteFragment.isSpinnerSelectedItemHint() &&
//                addQuoteFragment.isNumbersPositive()) {
//            addQuoteFragment.createMapOfQuoteProperties();
//            this.finish();
//        }
//        if (addQuoteFragment.isSpinnerSelectedItemHint()) {
//            Toast.makeText(this, getString(R.string.toast_choose_category), Toast.LENGTH_LONG).show();
//        }
    }

    public static Intent newIntent(Context context) {
        return new Intent(context, AddBookActivity.class);
    }
//
//    public static Intent newIntent(Context context, String titleName) {
//        Intent intent = new Intent(context, AddQuoteActivity.class);
//        intent.putExtra(QUOTE_TYPE_INTENT, titleName);
//        return intent;
//    }
//
//    public static Intent newIntent(Context context, String titleName, String currentCategory) {
//        Intent intent = new Intent(context, AddQuoteActivity.class);
//        intent.putExtra(QUOTE_TYPE_INTENT, titleName);
//        intent.putExtra(QUOTE_CATEGORY_INTENT, currentCategory);
//        return intent;
//    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
//        if (mCurrentFragment.isAdded()) {
//            getSupportFragmentManager().putFragment(outState, CURRENT_FRAGMENT_TAG_BUNDLE, mCurrentFragment);
//        }
//        outState.putString(QUOTE_TYPE_BUNDLE, mQuotesType);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent upIntent = BookCategoriesMainActivity.newIntent(this);
                startActivity(upIntent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}