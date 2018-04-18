package com.github.veselinazatchepina.books.login

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.content.Intent
import android.util.Log
import com.github.veselinazatchepina.books.data.BooksRepository
import com.github.veselinazatchepina.books.data.remote.BooksRemoteDataSource
import com.github.veselinazatchepina.books.login.auth.AuthUIWrapper
import com.github.veselinazatchepina.books.login.state.AnonymouslyLoginState
import io.reactivex.disposables.CompositeDisposable


class LoginViewModel : ViewModel() {

    private var compositeDisposable: CompositeDisposable = CompositeDisposable()
    private val booksDataSource by lazy {
        BooksRepository.getInstance(BooksRemoteDataSource.getInstance())
    }
    private val authUIWrapper: AuthUIWrapper by lazy {
        AuthUIWrapper()
    }
    var liveIsUserLogin: MutableLiveData<AnonymouslyLoginState> = MutableLiveData()

    fun signInWithAuthUI(): Intent {
        return authUIWrapper.getAuthIntent()
    }

    fun signInAnonymously() {
        liveIsUserLogin.postValue(AnonymouslyLoginState.AnonymouslyLoad())
        compositeDisposable.add(booksDataSource.signInAnonymously()
                .subscribe({
                    if (it != null && it) {
                        liveIsUserLogin.postValue(AnonymouslyLoginState.AnonymouslySuccess())
                    } else {
                        liveIsUserLogin.postValue(AnonymouslyLoginState.AnonymouslyError("Please try again!"))
                    }
                }, {
                    liveIsUserLogin.postValue(AnonymouslyLoginState.AnonymouslyError("Please try again!"))
                    Log.d("ANONYMOUSLY_LOGIN_STATE", it.message)
                }))

    }

    fun isUserAuthenticated(): Boolean = booksDataSource.isUserAuthenticated()

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}
