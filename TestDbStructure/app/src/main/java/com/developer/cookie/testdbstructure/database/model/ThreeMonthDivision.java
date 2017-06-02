package com.developer.cookie.testdbstructure.database.model;

import io.realm.RealmObject;

public class ThreeMonthDivision extends RealmObject {
    private long id;
    private float categoryIndex;
    private BookCategory category;
    private float januaryMarch;
    private float aprilJune;
    private float julySeptember;
    private float octoberDecember;
    private Year year;

    public float getCategoryIndex() {
        return categoryIndex;
    }

    public void setCategoryIndex(float categoryIndex) {
        this.categoryIndex = categoryIndex;
    }

    public float getJanuaryMarch() {
        return januaryMarch;
    }

    public void setJanuaryMarch(float januaryMarch) {
        this.januaryMarch = januaryMarch;
    }

    public float getAprilJune() {
        return aprilJune;
    }

    public void setAprilJune(float aprilJune) {
        this.aprilJune = aprilJune;
    }

    public float getJulySeptember() {
        return julySeptember;
    }

    public void setJulySeptember(float julySeptember) {
        this.julySeptember = julySeptember;
    }

    public float getOctoberDecember() {
        return octoberDecember;
    }

    public void setOctoberDecember(float octoberDecember) {
        this.octoberDecember = octoberDecember;
    }

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

    public Year getYear() {
        return year;
    }

    public void setYear(Year year) {
        this.year = year;
    }
}
