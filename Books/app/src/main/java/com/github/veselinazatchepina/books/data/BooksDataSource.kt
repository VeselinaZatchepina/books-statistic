package com.github.veselinazatchepina.books.data

import com.github.veselinazatchepina.books.poko.BookCategory
import io.reactivex.Observable


interface BooksDataSource {

    fun isUserAuthenticated(): Boolean

    fun signInAnonymously(): Observable<Boolean?>

    fun logout()

    fun isUserSignInAnonymously(): Boolean

    fun linkUserWithEmailAuth(email: String, password: String): Observable<Boolean?>

    fun getAllBookCategories(): Observable<List<BookCategory>>
}