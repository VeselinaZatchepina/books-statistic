package com.github.veselinazatchepina.bookstatistics.preference.activities;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.github.veselinazatchepina.bookstatistics.R;
import com.github.veselinazatchepina.bookstatistics.abstracts.SingleFragmentAbstractActivity;
import com.github.veselinazatchepina.bookstatistics.books.activities.BookSectionActivity;
import com.github.veselinazatchepina.bookstatistics.preference.fragments.IconPickerFragment;


public class PreferencesActivity extends SingleFragmentAbstractActivity {

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, PreferencesActivity.class);
        return intent;
    }

    @Override
    public Fragment createFragment() {
        return null;
    }

    @Override
    public void defineFragment() {
        FragmentManager fragmentManager = getFragmentManager();
        android.app.Fragment currentFragment = fragmentManager.findFragmentById(R.id.fragment_container);
        if (currentFragment == null) {
            currentFragment = IconPickerFragment.newInstance();
            fragmentManager.beginTransaction()
                    .add(R.id.fragment_container, currentFragment)
                    .commit();
        }
    }
}
