package com.github.veselinazatchepina.bookstatistics.database;


import com.github.veselinazatchepina.bookstatistics.books.enums.BookPropertiesEnum;
import com.github.veselinazatchepina.bookstatistics.database.model.BookCategory;

import java.util.HashMap;
import java.util.List;

public interface RealmRepository {

    List<BookCategory> getListOfBookCategories();

    void saveQuote(HashMap<BookPropertiesEnum, String> mapOfQuoteProperties);

    void closeDbConnect();
}
