package com.github.veselinazatchepina.books.login.state


sealed class AnonymouslyLoginState {
    class AnonymouslyLoad() : AnonymouslyLoginState()
    class AnonymouslyError(val errorText: String) : AnonymouslyLoginState()
    class AnonymouslySuccess() : AnonymouslyLoginState()
}