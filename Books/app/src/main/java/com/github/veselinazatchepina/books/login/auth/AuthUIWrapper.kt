package com.github.veselinazatchepina.books.login.auth

import android.content.Intent
import com.firebase.ui.auth.AuthUI


class AuthUIWrapper {

    private var providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.PhoneBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build())

    fun getAuthIntent(): Intent {
        return AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build()
    }
}
