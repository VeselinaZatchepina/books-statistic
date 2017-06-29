package com.github.veselinazatchepina.bookstatistics.books.activities;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.github.veselinazatchepina.bookstatistics.abstracts.SingleFragmentAbstractActivity;
import com.github.veselinazatchepina.bookstatistics.books.fragments.CurrentBookFragment;

public class CurrentBookActivity extends SingleFragmentAbstractActivity {

    private static final String CURRENT_BOOK_ID_INTENT = "current_book_id_intent";

    private long mCurrentBookId;

    public static Intent newIntent(Context context, long currentBookId) {
        Intent intent = new Intent(context, CurrentBookActivity.class);
        intent.putExtra(CURRENT_BOOK_ID_INTENT, currentBookId);
        return intent;
    }

    @Override
    public void defineInputData(Bundle saveInstanceState) {
        mCurrentBookId = getIntent().getLongExtra(CURRENT_BOOK_ID_INTENT, -1);
    }

    @Override
    public Fragment createFragment() {
        return CurrentBookFragment.newInstance(mCurrentBookId);
    }
}
