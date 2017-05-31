package com.developer.cookie.testdbstructure.db.model;

import io.realm.RealmObject;

public class SixMonthDivision extends RealmObject {
    private long id;
    private BookCategory category;
    private int januaryJune;
    private int julyDecember;
    private Year year;

    public BookCategory getCategory() {
        return category;
    }

    public void setCategory(BookCategory category) {
        this.category = category;
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

    public int getJulyDecember() {
        return julyDecember;
    }

    public void setJulyDecember(int julySDecember) {
        this.julyDecember = julySDecember;
    }

    public Year getYear() {
        return year;
    }

    public void setYear(Year year) {
        this.year = year;
    }
}
