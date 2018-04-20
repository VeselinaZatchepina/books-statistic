package com.github.veselinazatchepina.books.addbook

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.github.veselinazatchepina.books.data.BooksRepository
import com.github.veselinazatchepina.books.data.remote.BooksRemoteDataSource
import com.github.veselinazatchepina.books.poko.Book
import com.github.veselinazatchepina.books.poko.BookCategory
import io.reactivex.disposables.CompositeDisposable


class AddBookViewModel : ViewModel() {

    private var compositeDisposable: CompositeDisposable = CompositeDisposable()
    private val booksDataSource by lazy {
        BooksRepository.getInstance(BooksRemoteDataSource.getInstance())
    }

    val liveBookCategories = MutableLiveData<List<BookCategory>>()

    fun getAllBookCategories() {
        compositeDisposable.add(booksDataSource.getAllBookCategories().subscribe({
            liveBookCategories.postValue(it)
        }, {
            liveBookCategories.postValue(emptyList())
        }))
    }

    fun saveBook(book: Book) {
        booksDataSource.saveBook(book)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}