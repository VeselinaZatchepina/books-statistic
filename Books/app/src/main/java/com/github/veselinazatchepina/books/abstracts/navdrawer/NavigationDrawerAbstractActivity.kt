package com.github.veselinazatchepina.books.abstracts.navdrawer

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.support.design.widget.NavigationView
import android.support.design.widget.Snackbar
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.view.MenuItem
import android.view.View
import com.github.veselinazatchepina.bemotivated.abstracts.navdrawer.AddLinkDialog
import com.github.veselinazatchepina.bemotivated.abstracts.navdrawer.NavDrawerViewModel
import com.github.veselinazatchepina.books.R
import com.github.veselinazatchepina.books.abstracts.SingleFragmentAbstractActivity
import com.github.veselinazatchepina.books.categories.BookCategoriesActivity
import com.github.veselinazatchepina.books.login.LoginMainActivity
import kotlinx.android.synthetic.main.activity_nav_drawer.*
import kotlinx.android.synthetic.main.activity_single_fragment.*
import kotlinx.android.synthetic.main.nav_header.view.*


abstract class NavigationDrawerAbstractActivity : SingleFragmentAbstractActivity(),
        NavigationView.OnNavigationItemSelectedListener {

    private val navDrawerViewModel: NavDrawerViewModel by lazy {
        ViewModelProviders.of(this).get(NavDrawerViewModel::class.java)
    }

    companion object {
        private const val TAG_ADD_LINK_DIALOG = "tag_add_link_dialog"
    }

    override fun getLayoutResId(): Int = R.layout.activity_nav_drawer

    override fun defineNavigationDrawer() {
        val toggle = ActionBarDrawerToggle(this,
                drawerLayout,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        navView.setNavigationItemSelectedListener(this)
        defineLogoutButton()
        defineLinkButton()
    }

    private fun defineLogoutButton() {
        navView.getHeaderView(0).loginBtnNavHeader.setOnClickListener {
            navDrawerViewModel.logout()
            startActivity(LoginMainActivity.newIntent(this))
        }
    }

    private fun defineLinkButton() {
        navDrawerViewModel.isUserSignInAnonymously()
        navDrawerViewModel.liveIsUserSignInAnonymously.observe(this, Observer {
            val headerView = navView.getHeaderView(0)
            if (it == true) {
                headerView.linkBtnNavHeader.setOnClickListener {
                    AddLinkDialog.newInstance().show(supportFragmentManager, TAG_ADD_LINK_DIALOG)
                    navDrawerViewModel.liveUserProfileLinkState.observe(this, Observer {
                        if (it != null && it is UserProfileLinkState.UserProfileLinkError) {
                            defineErrorSnackbar("Please try again!")
                        }
                    })
                }
            } else {
                headerView.linkBtnNavHeader.visibility = View.GONE
            }
        })
    }

    private fun defineErrorSnackbar(errorText: String) {
        val snackbar = Snackbar
                .make(drawerLayout, errorText, Snackbar.LENGTH_LONG);
        snackbar.show()
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        var intent: Intent? = null
        when (item.itemId) {
            R.id.all_books -> intent = null
            R.id.book_categories -> intent = BookCategoriesActivity.newIntent(this)
            R.id.chart -> intent = null
            R.id.write_to_developer -> intent = null
        }
        if (intent != null) {
            startActivity(intent)
        }
        return true
    }
}