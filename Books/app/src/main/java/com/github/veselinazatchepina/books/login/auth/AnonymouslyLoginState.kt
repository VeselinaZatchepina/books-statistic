package com.github.veselinazatchepina.bemotivated.login.auth


sealed class AnonymouslyLoginState {
    class AnonymouslyLoad() : AnonymouslyLoginState()
    class AnonymouslyError(val errorText: String) : AnonymouslyLoginState()
    class AnonymouslySuccess() : AnonymouslyLoginState()
}