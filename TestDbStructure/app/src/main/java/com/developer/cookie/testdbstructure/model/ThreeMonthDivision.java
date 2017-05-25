package com.developer.cookie.testdbstructure.model;

import io.realm.RealmObject;

public class ThreeMonthDivision extends RealmObject {
    private long id;
    private String categoryName;
    private int januaryMarch = 3;
    private int aprilJune = 6;
    private int julySeptember = 9;
    private int octoberDecember = 12;
    private Year year;

    public int getAprilJune() {
        return aprilJune;
    }

    public void setAprilJune(int aprilJune) {
        this.aprilJune = aprilJune;
    }

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
