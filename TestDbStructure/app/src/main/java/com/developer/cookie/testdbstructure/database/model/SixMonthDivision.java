package com.developer.cookie.testdbstructure.database.model;

import io.realm.RealmObject;

public class SixMonthDivision extends RealmObject {
    private long id;
    private float categoryIndex;
    private BookCategory category;
    private float januaryJune;
    private float julyDecember;
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

    public float getCategoryIndex() {
        return categoryIndex;
    }

    public void setCategoryIndex(float categoryIndex) {
        this.categoryIndex = categoryIndex;
    }

    public float getJanuaryJune() {
        return januaryJune;
    }

    public void setJanuaryJune(float januaryJune) {
        this.januaryJune = januaryJune;
    }

    public float getJulyDecember() {
        return julyDecember;
    }

    public void setJulyDecember(float julyDecember) {
        this.julyDecember = julyDecember;
    }

    public Year getYear() {
        return year;
    }

    public void setYear(Year year) {
        this.year = year;
    }
}
