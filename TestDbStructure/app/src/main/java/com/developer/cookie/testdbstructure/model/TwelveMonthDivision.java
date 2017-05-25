package com.developer.cookie.testdbstructure.model;

import io.realm.RealmObject;

public class TwelveMonthDivision extends RealmObject {
    private long id;
    private String categoryName;
    private int januaryDecember = 12;
    private Year year;

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

    public int getJanuaryDecember() {
        return januaryDecember;
    }

    public void setJanuaryDecember(int januaryDecember) {
        this.januaryDecember = januaryDecember;
    }

    public Year getYear() {
        return year;
    }

    public void setYear(Year year) {
        this.year = year;
    }
}
