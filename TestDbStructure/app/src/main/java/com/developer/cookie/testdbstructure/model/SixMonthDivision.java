package com.developer.cookie.testdbstructure.model;

import io.realm.RealmObject;

public class SixMonthDivision extends RealmObject {
    private long id;
    private String categoryName;
    private int januaryJune = 6;
    private int julySDecember = 12;
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

    public int getJanuaryJune() {
        return januaryJune;
    }

    public void setJanuaryJune(int januaryJune) {
        this.januaryJune = januaryJune;
    }

    public int getJulySDecember() {
        return julySDecember;
    }

    public void setJulySDecember(int julySDecember) {
        this.julySDecember = julySDecember;
    }

    public Year getYear() {
        return year;
    }

    public void setYear(Year year) {
        this.year = year;
    }
}
