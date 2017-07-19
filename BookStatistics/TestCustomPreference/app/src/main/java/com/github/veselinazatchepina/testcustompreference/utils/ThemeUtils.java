package com.github.veselinazatchepina.testcustompreference.utils;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.github.veselinazatchepina.testcustompreference.R;

public class ThemeUtils {
    private static String sTheme = "ic_first";
    private final static String PREFERENCE_KEY = "customThemeKey";
    private final static String THEME_FIRST = "ic_first";
    private final static String THEME_SECOND = "ic_second";
    private final static String THEME_THIRD = "ic_third";

    public static void changeToTheme(Activity activity, String theme) {
        sTheme = theme;
        activity.finish();
        activity.startActivity(new Intent(activity, activity.getClass()));
        activity.overridePendingTransition(android.R.anim.fade_in,
                android.R.anim.fade_out);
    }

    public static void onActivityCreateSetTheme(Activity activity) {
        setThemeNameFromPreference(activity);
        switch (sTheme) {
            case THEME_FIRST:
                activity.setTheme(R.style.AppTheme1);
                break;
            case THEME_SECOND:
                activity.setTheme(R.style.AppTheme2);
                break;
            case THEME_THIRD:
                activity.setTheme(R.style.AppTheme3);
                break;
        }
    }

    private static void setThemeNameFromPreference(Activity activity) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        String value = prefs.getString(PREFERENCE_KEY, null);
        if (value != null) {
            sTheme = value;
        }
    }
}
