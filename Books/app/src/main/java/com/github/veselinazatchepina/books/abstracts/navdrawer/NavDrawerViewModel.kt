package com.github.veselinazatchepina.bemotivated.abstracts.navdrawer

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

    fun logout() {
        booksDataSource.logout()
    }

    fun isUserSignInAnonymously() {
        liveIsUserSignInAnonymously.value = booksDataSource.isUserSignInAnonymously()
    }

    fun linkUserWithEmailAuth(email: String, password: String) {
        booksDataSource.linkUserWithEmailAuth(email, password)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}