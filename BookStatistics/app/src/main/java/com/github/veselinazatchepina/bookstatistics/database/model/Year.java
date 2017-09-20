package com.github.veselinazatchepina.bookstatistics.database.model;

import io.realm.RealmObject;

public class Year extends RealmObject {
    private long id;
    private int yearNumber;
    private int currentYearCount;

    public Year() {
    }

    public Year(long id, int yearNumber) {
        this.id = id;
        this.yearNumber = yearNumber;
    }

    public Year(int yearNumber) {
        this.yearNumber = yearNumber;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getYearNumber() {
        return yearNumber;
    }

    public void setYearNumber(int yearNumber) {
        this.yearNumber = yearNumber;
    }

    public int getCurrentYearCount() {
        return currentYearCount;
    }

    public void setCurrentYearCount(int currentYearCount) {
        this.currentYearCount = currentYearCount;
    }
}
