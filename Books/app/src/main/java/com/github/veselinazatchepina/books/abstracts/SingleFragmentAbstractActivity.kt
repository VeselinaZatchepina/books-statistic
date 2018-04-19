package com.github.veselinazatchepina.books.abstracts

import android.content.res.Configuration
import android.os.Bundle
import android.support.design.widget.CoordinatorLayout
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import com.github.veselinazatchepina.books.R
import com.github.veselinazatchepina.books.addbook.AddBookActivity
import com.github.veselinazatchepina.books.utils.AppBarLayoutExpanded
import com.github.veselinazatchepina.books.utils.setFirstVowelColor
import kotlinx.android.synthetic.main.activity_single_fragment.*


abstract class SingleFragmentAbstractActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutResId())
        defineInputData()
        defineToolbar()
        setAppBarNotExpandable()
        defineNavigationDrawer()
        defineFab()
        if (title != null) {
            setNewTitleStyle(title.toString())
        }
        defineFragment()
    }

    abstract fun createFragment(): Fragment

    open fun getLayoutResId(): Int = R.layout.activity_single_fragment

    open fun defineInputData() {}

    private fun defineToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun setAppBarNotExpandable() {
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            AppBarLayoutExpanded.setAppBarLayoutExpended(this,
                    appbarLayout,
                    appbarLayout.layoutParams as CoordinatorLayout.LayoutParams,
                    collapsingToolbarLayout,
                    resources.configuration)
        }
    }

    open fun defineNavigationDrawer() {}

    private fun defineFab() {
        val fabImageResourceId = setFabImageResId()
        addFabButton.setImageDrawable(ContextCompat.getDrawable(this, fabImageResourceId))
        addFabButton.setOnClickListener {
            defineActionWhenFabIsPressed()
        }
    }

    open fun setFabImageResId(): Int = R.drawable.ic_add_white_24dp

    open fun defineActionWhenFabIsPressed() {
        startActivity(AddBookActivity.newIntent(this))
    }

    open fun setNewTitleStyle(title: String) {
        setTitle(title.setFirstVowelColor(this))
    }

    private fun defineFragment() {
        var currentFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainer)
        if (currentFragment == null) {
            currentFragment = createFragment()
            supportFragmentManager.beginTransaction()
                    .add(R.id.fragmentContainer, currentFragment)
                    .commit()
        }
    }
}