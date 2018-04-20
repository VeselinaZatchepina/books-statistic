package com.github.veselinazatchepina.books.addbook

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import com.github.veselinazatchepina.books.R
import com.github.veselinazatchepina.books.abstracts.SingleFragmentAbstractActivity


class AddBookActivity : SingleFragmentAbstractActivity() {

    private val addBookViewModel: AddBookViewModel by lazy {
        ViewModelProviders.of(this).get(AddBookViewModel::class.java)
    }

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, AddBookActivity::class.java)
        }
    }

    override fun createFragment() = AddBookFragment.createInstance()

    override fun defineExpandableAppBar() {
        setAppBarNotExpandable()
    }

    override fun setFabImageResId() = R.drawable.ic_done_white_24dp

    override fun defineActionWhenFabIsPressed() {
        val fragment = supportFragmentManager.findFragmentById(R.id.fragmentContainer) as AddBookFragment
        if (fragment.saveBook()) {
            finish()
        }
    }
}