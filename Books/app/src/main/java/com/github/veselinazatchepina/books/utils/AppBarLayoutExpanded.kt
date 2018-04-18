package com.github.veselinazatchepina.books.utils

import android.content.Context
import android.content.res.Configuration
import android.support.design.widget.AppBarLayout
import android.support.design.widget.CollapsingToolbarLayout
import android.support.design.widget.CoordinatorLayout
import com.github.veselinazatchepina.books.R


/**
 * Class helps set AppBarLayout not expandable
 */
class AppBarLayoutExpanded {

    companion object {
        fun setAppBarLayoutExpended(context: Context,
                                    appBarLayout: AppBarLayout,
                                    layoutParams: CoordinatorLayout.LayoutParams,
                                    collapsingToolbarLayout: CollapsingToolbarLayout,
                                    configuration: Configuration) {
            appBarLayout.setExpanded(false, false)
            layoutParams.height = context.resources.getDimension(R.dimen.abstract_toolbar_height).toInt()
            collapsingToolbarLayout.isTitleEnabled = false
        }
    }
}