package com.github.veselinazatchepina.bookstatistics.database;


import com.github.veselinazatchepina.bookstatistics.books.enums.BookPropertiesEnum;
import com.github.veselinazatchepina.bookstatistics.database.model.AllBookMonthDivision;
import com.github.veselinazatchepina.bookstatistics.database.model.Book;
import com.github.veselinazatchepina.bookstatistics.database.model.BookCategory;
import com.github.veselinazatchepina.bookstatistics.database.model.BookMonthDivision;

import java.util.HashMap;
import java.util.List;

import io.realm.Realm;

public interface RealmRepository {

    List<BookCategory> getListOfBookCategories();

    void saveQuote(HashMap<BookPropertiesEnum, String> mapOfQuoteProperties);

    List<Book> getAllBooksInCurrentSectionByCategory(String sectionType, String currentCategory);

    List<Book> getAllBooksInCurrentSection(String sectionType);

    List<Book> getBookById(long currentBookId);

    void saveChangedBook(long currentBookId, HashMap<BookPropertiesEnum, String> mapOfBookProperties);

    void deleteAllBooksWithCurrentCategory(String categoryForDelete);

    void deleteBookById(long bookIdForDelete);

    List<AllBookMonthDivision> getAllBookMonth(int begin, int end);

    List<BookMonthDivision> getBookMonthDivision();

    List<BookMonthDivision> getBookMonthDivisionByCategory(String category);

    Realm getRealmConnection();

    void updateBookSectionInTransaction(final long currentBookIdForEdit, final HashMap<BookPropertiesEnum, String> mapOfBookProperties);

    void updateBookRatingInTransaction(final long currentBookIdForEdit, final HashMap<BookPropertiesEnum, String> mapOfBookProperties);

    void closeDbConnect();
}
