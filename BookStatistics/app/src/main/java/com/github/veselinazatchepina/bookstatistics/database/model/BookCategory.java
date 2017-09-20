package com.github.veselinazatchepina.bookstatistics.database.model;

import io.realm.RealmObject;

public class BookCategory extends RealmObject {
    private long id;
    private String categoryName;
    private int categoryBookCount;

    public BookCategory() {

    }

    public BookCategory(long id, String categoryName, int categoryBookCount) {
        this.id = id;
        this.categoryName = categoryName;
        this.categoryBookCount = categoryBookCount;
    }

    public int getCategoryBookCount() {
        return categoryBookCount;
    }

    public void setCategoryBookCount(int categoryBookCount) {
        this.categoryBookCount = categoryBookCount;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
