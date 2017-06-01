package com.developer.cookie.testdbstructure.database.model;

import io.realm.RealmObject;

public class AllBookSixMonthDivision extends RealmObject {
    private long id;
    private int month;
    private int allBookCount;
    private Year year;

    public int getAllBookCount() {
        return allBookCount;
    }

    public void setAllBookCount(int allBookCount) {
        this.allBookCount = allBookCount;
    }

    public float getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public Year getYear() {
        return year;
    }

    public void setYear(Year year) {
        this.year = year;
    }
}
