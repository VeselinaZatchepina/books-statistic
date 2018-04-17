package com.github.veselinazatchepina.books.login


sealed class UserExistenceState {
    class UserExistenceLoad : UserExistenceState()
    class UserExistenceError : UserExistenceState()
    class UserExistenceSuccess : UserExistenceState()
}