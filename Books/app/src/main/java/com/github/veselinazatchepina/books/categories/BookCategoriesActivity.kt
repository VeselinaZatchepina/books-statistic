package com.github.veselinazatchepina.books.categories

import android.content.Context
import android.content.Intent
import com.github.veselinazatchepina.books.abstracts.navdrawer.NavigationDrawerAbstractActivity


class BookCategoriesActivity : NavigationDrawerAbstractActivity() {

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, BookCategoriesActivity::class.java)
        }
    }

    override fun createFragment() = BookCategoriesFragment.createInstance()
}