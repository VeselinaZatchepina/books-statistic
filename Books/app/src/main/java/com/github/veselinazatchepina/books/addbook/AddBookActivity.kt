package com.github.veselinazatchepina.books.addbook

import android.content.Context
import android.content.Intent
import com.github.veselinazatchepina.books.abstracts.SingleFragmentAbstractActivity


class AddBookActivity : SingleFragmentAbstractActivity() {

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, AddBookActivity::class.java)
        }
    }

    override fun createFragment() = AddBookFragment.createInstance()

    override fun defineExpandableAppBar() {
        setAppBarNotExpandable()
    }
}