package com.developer.cookie.testdbstructure.database.model;

import io.realm.RealmObject;

public class TwelveMonthDivision extends RealmObject {
    private int id;
    private float categoryIndex;
    private BookCategory category;
    private float januaryDecember;
    private Year year;

    public BookCategory getCategory() {
        return category;
    }

    public void setCategory(BookCategory category) {
        this.category = category;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getJanuaryDecember() {
        return januaryDecember;
    }

    public void setJanuaryDecember(float januaryDecember) {
        this.januaryDecember = januaryDecember;
    }

    public Year getYear() {
        return year;
    }

    public void setYear(Year year) {
        this.year = year;
    }

    public float getCategoryIndex() {
        return categoryIndex;
    }

    public void setCategoryIndex(float categoryIndex) {
        this.categoryIndex = categoryIndex;
    }
}
