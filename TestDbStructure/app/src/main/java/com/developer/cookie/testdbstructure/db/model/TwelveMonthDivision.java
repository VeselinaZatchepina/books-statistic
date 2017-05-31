package com.developer.cookie.testdbstructure.db.model;

import io.realm.RealmObject;

public class TwelveMonthDivision extends RealmObject {
    private long id;
    private BookCategory category;
    private int januaryDecember;
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
