package com.github.veselinazatchepina.books.abstracts.navdrawer

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.github.veselinazatchepina.books.data.BooksRepository
import com.github.veselinazatchepina.books.data.remote.BooksRemoteDataSource
import io.reactivex.disposables.CompositeDisposable


class NavDrawerViewModel : ViewModel() {

    private var compositeDisposable: CompositeDisposable = CompositeDisposable()
    private val booksDataSource by lazy {
        BooksRepository.getInstance(BooksRemoteDataSource.getInstance())
    }

    val liveIsUserSignInAnonymously = MutableLiveData<Boolean>()
    val liveUserProfileLinkState = MutableLiveData<UserProfileLinkState>()

    fun logout() {
        booksDataSource.logout()
    }

    fun isUserSignInAnonymously() {
        liveIsUserSignInAnonymously.value = booksDataSource.isUserSignInAnonymously()
    }

    fun linkUserWithEmailAuth(email: String, password: String) {
        liveUserProfileLinkState.value = UserProfileLinkState.UserProfileLinkLoad()
        compositeDisposable.add(booksDataSource.linkUserWithEmailAuth(email, password)
                .subscribe({ isLinked ->
                    if (isLinked != null && isLinked) {
                        liveUserProfileLinkState.postValue(UserProfileLinkState.UserProfileLinkSuccess())
                    } else {
                        liveUserProfileLinkState.postValue(UserProfileLinkState.UserProfileLinkError())
                    }
                }, {
                    liveUserProfileLinkState.postValue(UserProfileLinkState.UserProfileLinkError())
                }))
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}