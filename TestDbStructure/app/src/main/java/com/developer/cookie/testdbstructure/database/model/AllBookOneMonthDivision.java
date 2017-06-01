package com.developer.cookie.testdbstructure.database.model;

import io.realm.RealmObject;

public class AllBookOneMonthDivision extends RealmObject {
    private long id;
    private float month;
    private float allBookCount;
    private Year year;

    public float getAllBookCount() {
        return allBookCount;
    }

    public void setAllBookCount(float allBookCount) {
        this.allBookCount = allBookCount;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public float getMonth() {
        return month;
    }

    public void setMonth(float month) {
        this.month = month;
    }

    public Year getYear() {
        return year;
    }

    public void setYear(Year year) {
        this.year = year;
    }
}
