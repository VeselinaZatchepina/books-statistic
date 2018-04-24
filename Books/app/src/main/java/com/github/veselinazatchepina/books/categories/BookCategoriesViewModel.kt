package com.github.veselinazatchepina.books.categories

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import com.github.veselinazatchepina.books.categories.state.BookCategoriesState
import com.github.veselinazatchepina.books.data.BooksRepository
import com.github.veselinazatchepina.books.data.remote.BooksRemoteDataSource
import io.reactivex.disposables.CompositeDisposable


class BookCategoriesViewModel : ViewModel() {

    private var compositeDisposable: CompositeDisposable = CompositeDisposable()
    private val booksDataSource by lazy {
        BooksRepository.getInstance(BooksRemoteDataSource.getInstance())
    }

    val liveBookCategories = MutableLiveData<BookCategoriesState>()

    fun getAllBookCategories() {
        liveBookCategories.value = BookCategoriesState.BookCategoriesLoad()
        compositeDisposable.add(booksDataSource.getAllBookCategories().subscribe({
            liveBookCategories.postValue(BookCategoriesState.BookCategoriesSuccess(it))
        }, {
            Log.d("GET_CATEGORIES", "RX_ERROR")
            liveBookCategories.postValue(BookCategoriesState.BookCategoriesError())
        }))
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}