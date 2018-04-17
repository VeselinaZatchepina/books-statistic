package com.github.veselinazatchepina.books.login.auth


sealed class AnonymouslyLoginState {
    class AnonymouslyLoad() : AnonymouslyLoginState()
    class AnonymouslyError(val errorText: String) : AnonymouslyLoginState()
    class AnonymouslySuccess() : AnonymouslyLoginState()
}