package com.github.veselinazatchepina.books.login

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.veselinazatchepina.books.R
import com.github.veselinazatchepina.books.login.auth.AnonymouslyLoginState
import com.github.veselinazatchepina.books.login.auth.AuthViewModel
import kotlinx.android.synthetic.main.fragment_login.*
import org.jetbrains.anko.support.v4.toast


class LoginFragment : Fragment() {

    private val RC_SIGN_IN = 123
    private var rootView: View? = null
    private val authViewModel: AuthViewModel by lazy {
        ViewModelProviders.of(this).get(AuthViewModel::class.java)
    }

    companion object {
        fun createInstance(): LoginFragment {
            return LoginFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        checkIfUserAuthenticated()
        rootView = inflater.inflate(R.layout.fragment_login, container, false)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        defineLogInButton()
        defineGuestButton()
    }

    private fun checkIfUserAuthenticated() {
        if (authViewModel.isUserAuthenticated()) {
            toast("Authenticated!")
        }
    }

    private fun defineLogInButton() {
        loginButton.setOnClickListener {
            startActivityForResult(authViewModel.signInWithAuthUI(), RC_SIGN_IN)
        }
    }

    private fun defineGuestButton() {
        guestButton.setOnClickListener {
            authViewModel.signInAnonymously()
            authViewModel.liveIntent.observe(this, Observer {
                defineAnonymouslyAction(it)
            })
        }
    }

    private fun defineAnonymouslyAction(state: AnonymouslyLoginState?) {
        when (state) {
            is AnonymouslyLoginState.AnonymouslySuccess -> {
                if (this.isAdded) {
                    authViewModel.isUserExists()
                    authViewModel.liveIsUserExists.observe(this, Observer {
                        defineLoginAction(it)
                    })
                }
            }
            is AnonymouslyLoginState.AnonymouslyError -> {
                if (this.isAdded) {
                    anonymProgressBar.visibility = View.GONE
                    toast(state.errorText)
                }
            }
            is AnonymouslyLoginState.AnonymouslyLoad -> {
                if (this.isAdded) {
                    anonymProgressBar.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun defineLoginAction(state: UserExistenceState?) {
        when (state) {
            is UserExistenceState.UserExistenceSuccess -> {
                if (this.isAdded) {
                    anonymProgressBar.visibility = View.GONE
                }
            }
            is UserExistenceState.UserExistenceError -> {
                if (this.isAdded) {
                    anonymProgressBar.visibility = View.GONE
                    authViewModel.saveUser()
                }
            }
            is UserExistenceState.UserExistenceLoad -> {
                if (this.isAdded) {
                    anonymProgressBar.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == Activity.RESULT_OK) {
                authViewModel.isUserExists()
                authViewModel.liveIsUserExists.observe(this, Observer {
                    defineLoginAction(it)
                })
            } else {
                toast("Please try again!")
            }
        }
    }
}