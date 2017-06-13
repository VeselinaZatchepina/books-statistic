package com.github.veselinazatchepina.bookstatistics.database;


import com.github.veselinazatchepina.bookstatistics.database.model.BookCategory;

import java.util.List;

public interface RealmRepository {

    List<BookCategory> getListOfBookCategories();

    void closeDbConnect();
}
