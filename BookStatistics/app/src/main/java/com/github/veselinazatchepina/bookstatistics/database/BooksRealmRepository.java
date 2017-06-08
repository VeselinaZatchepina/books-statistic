package com.github.veselinazatchepina.bookstatistics.database;


import com.github.veselinazatchepina.bookstatistics.database.model.BookCategory;

import java.util.List;

import io.realm.Realm;

public class BooksRealmRepository implements RealmRepository {

    private final Realm mRealm;

    public BooksRealmRepository() {
        mRealm = Realm.getDefaultInstance();
    }

    @Override
    public List<BookCategory> getListOfBookCategories() {
        return mRealm.where(BookCategory.class).findAllSortedAsync("id");
    }


}
