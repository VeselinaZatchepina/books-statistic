package com.github.veselinazatchepina.bookstatistics.database;


import com.github.veselinazatchepina.bookstatistics.database.model.BookCategory;

import io.realm.Realm;
import io.realm.RealmResults;

public class BooksRealmRepository implements RealmRepository {

    private final Realm mRealm;

    public BooksRealmRepository() {
        mRealm = Realm.getDefaultInstance();
    }

    @Override
    public RealmResults<BookCategory> getListOfBookCategories() {
        return mRealm.where(BookCategory.class).findAllSortedAsync("id");
    }

    @Override
    public void closeDbConnect() {
        mRealm.close();
    }
}
