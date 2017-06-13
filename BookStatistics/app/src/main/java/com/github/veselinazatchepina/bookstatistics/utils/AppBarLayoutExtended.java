package com.github.veselinazatchepina.bookstatistics.utils;


import android.content.Context;
import android.content.res.Configuration;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;

import com.github.veselinazatchepina.bookstatistics.R;


/**
 * Class helps set AppBarLayout not expandable
 */
public class AppBarLayoutExtended {

    public static void setAppBarLayoutExpended(Context context, AppBarLayout appBarLayout, CoordinatorLayout.LayoutParams layoutParams,
                                               CollapsingToolbarLayout collapsingToolbarLayout, Configuration configuration) {
        appBarLayout.setExpanded(false, false);
        if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            layoutParams.height = (int) context.getResources().getDimension(R.dimen.toolbar_height_normal_portrait);
        } else {
            layoutParams.height = (int) context.getResources().getDimension(R.dimen.toolbar_height_normal_landscape);
        }
        collapsingToolbarLayout.setTitleEnabled(false);
    }
}
