package com.github.veselinazatchepina.bookstatistics.database;


import com.github.veselinazatchepina.bookstatistics.books.enums.BookPropertiesEnum;
import com.github.veselinazatchepina.bookstatistics.database.model.AllBookMonthDivision;
import com.github.veselinazatchepina.bookstatistics.database.model.Book;
import com.github.veselinazatchepina.bookstatistics.database.model.BookCategory;
import com.github.veselinazatchepina.bookstatistics.database.model.BookMonthDivision;
import com.github.veselinazatchepina.bookstatistics.database.model.Section;

import java.util.HashMap;
import java.util.List;

public interface RealmRepository {

    List<BookCategory> getListOfBookCategories();

    void saveQuote(HashMap<BookPropertiesEnum, String> mapOfQuoteProperties);

    List<Section> getAllBookSection();

    List<Book> getAllBooksInCurrentSection(String sectionType, String currentCategory);

    List<Book> getBookById(long currentBookId);

    void saveChangedBook(long currentBookId, HashMap<BookPropertiesEnum, String> mapOfBookProperties);

    void deleteAllBooksWithCurrentCategory(String categoryForDelete);

    void deleteBookById(long bookIdForDelete);

    List<AllBookMonthDivision> getAllBookMonth(int begin, int end);

    List<BookMonthDivision> getBookMonthDivision();

    List<BookMonthDivision> getBookMonthDivisionByCategory(String category);

    void closeDbConnect();
}
