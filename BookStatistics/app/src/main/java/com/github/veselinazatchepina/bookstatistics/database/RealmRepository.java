package com.github.veselinazatchepina.bookstatistics.database;


import com.github.veselinazatchepina.bookstatistics.books.enums.BookPropertiesEnum;
import com.github.veselinazatchepina.bookstatistics.database.model.Book;
import com.github.veselinazatchepina.bookstatistics.database.model.BookCategory;
import com.github.veselinazatchepina.bookstatistics.database.model.BookMonthDivision;
import com.github.veselinazatchepina.bookstatistics.database.model.Year;

import java.util.HashMap;
import java.util.List;

public interface RealmRepository {

    List<BookCategory> getListOfBookCategories();

    void saveCurrentBook(HashMap<BookPropertiesEnum, String> mapOfQuoteProperties);

    List<Book> getAllBooksInCurrentSectionByCategory(String sectionType, String currentCategory);

    List<Book> getBookBySectionAndBookNameByCategory(String sectionType, String bookName, String category);

    List<Book> getAllBooksInCurrentSection(String sectionType);

    List<Book> getBookById(long currentBookId);

    void saveChangedBook(long currentBookId, HashMap<BookPropertiesEnum, String> mapOfBookProperties);

    void deleteAllBooksWithCurrentCategory(String categoryForDelete);

    void deleteBookById(long bookIdForDelete);

    List<Book> getBookBySectionAndBookName(String sectionType, String bookName);

    List<BookMonthDivision> getBookMonthDivision(int year);

    List<BookMonthDivision> getBookMonthDivisionByCategory(String category, int year);

    void updateBookRatingInTransaction(final long currentBookIdForEdit, final HashMap<BookPropertiesEnum, String> mapOfBookProperties);

    void closeDbConnect();

    List<Year> getAllYears();
}
