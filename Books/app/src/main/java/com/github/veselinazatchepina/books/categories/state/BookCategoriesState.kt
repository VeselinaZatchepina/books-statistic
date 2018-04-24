package com.github.veselinazatchepina.books.categories.state

import com.github.veselinazatchepina.books.poko.BookCategory

sealed class BookCategoriesState {
    class BookCategoriesLoad() : BookCategoriesState()
    class BookCategoriesError() : BookCategoriesState()
    class BookCategoriesSuccess(var bookCategories: List<BookCategory>) : BookCategoriesState()
}