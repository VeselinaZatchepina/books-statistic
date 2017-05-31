package com.developer.cookie.testdbstructure.db.model;

import io.realm.RealmObject;

public class ThreeMonthDivision extends RealmObject {
    private long id;
    private BookCategory category;
    private int januaryMarch;
    private int aprilJune;
    private int julySeptember;
    private int octoberDecember;
    private Year year;

    public int getAprilJune() {
        return aprilJune;
    }

    public void setAprilJune(int aprilJune) {
        this.aprilJune = aprilJune;
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

    public int getJanuaryMarch() {
        return januaryMarch;
    }

    public void setJanuaryMarch(int januaryMarch) {
        this.januaryMarch = januaryMarch;
    }

    public int getJulySeptember() {
        return julySeptember;
    }

    public void setJulySeptember(int julySeptember) {
        this.julySeptember = julySeptember;
    }

    public int getOctoberDecember() {
        return octoberDecember;
    }

    public void setOctoberDecember(int octoberDecember) {
        this.octoberDecember = octoberDecember;
    }

    public Year getYear() {
        return year;
    }

    public void setYear(Year year) {
        this.year = year;
    }
}
