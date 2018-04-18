package com.github.veselinazatchepina.books.categories

import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment
import com.github.veselinazatchepina.books.abstracts.navdrawer.NavigationDrawerAbstractActivity


class BookCategoriesActivity : NavigationDrawerAbstractActivity() {

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, BookCategoriesActivity::class.java)
        }
    }

    override fun createFragment(): Fragment {
        return BookCategoriesFragment.createInstance()
    }
}