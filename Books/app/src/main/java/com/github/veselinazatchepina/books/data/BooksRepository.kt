package com.github.veselinazatchepina.books.data

import com.github.veselinazatchepina.books.data.remote.BooksRemoteDataSource
import io.reactivex.Observable


class BooksRepository(private val booksRemoteDataSource: BooksRemoteDataSource) : BooksDataSource {

    companion object {
        private var INSTANCE: BooksRepository? = null

        fun getInstance(booksRemoteDataSource: BooksRemoteDataSource): BooksRepository {
            if (INSTANCE == null) {
                INSTANCE = BooksRepository(booksRemoteDataSource)
            }
            return INSTANCE!!
        }
    }

    override fun isUserAuthenticated() = booksRemoteDataSource.isUserAuthenticated()

    override fun signInAnonymously() = booksRemoteDataSource.signInAnonymously()

    override fun isUserExists() = booksRemoteDataSource.isUserExists()

    override fun saveUserId() {
        booksRemoteDataSource.saveUserId()
    }

    override fun logout() {
        booksRemoteDataSource.logout()
    }

    override fun isUserSignInAnonymously() = booksRemoteDataSource.isUserSignInAnonymously()


    override fun linkUserWithEmailAuth(email: String, password: String): Observable<Boolean?> =
            booksRemoteDataSource.linkUserWithEmailAuth(email, password)

}