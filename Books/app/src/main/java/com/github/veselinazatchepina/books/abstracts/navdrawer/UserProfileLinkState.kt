package com.github.veselinazatchepina.books.abstracts.navdrawer


sealed class UserProfileLinkState {
    class UserProfileLinkLoad : UserProfileLinkState()
    class UserProfileLinkError : UserProfileLinkState()
    class UserProfileLinkSuccess : UserProfileLinkState()
}