package com.github.veselinazatchepina.testcustompreference.activities;

import android.app.FragmentManager;
import android.support.v4.app.Fragment;

import com.github.veselinazatchepina.testcustompreference.R;
import com.github.veselinazatchepina.testcustompreference.abstracts.SingleFragmentAbstractActivity;
import com.github.veselinazatchepina.testcustompreference.fragments.IconPickerFragment;

public class PreferencesActivity extends SingleFragmentAbstractActivity {

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
