package com.developer.cookie.testdbstructure.db.model;

import io.realm.RealmObject;

public class Year extends RealmObject {
    private long id;
    private int yearNumber;

    public Year() {
    }

    public Year(long id, int yearNumber) {
        this.id = id;
        this.yearNumber = yearNumber;
    }

    public Year(int yearNumber) {
        this.yearNumber = yearNumber;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getYearNumber() {
        return yearNumber;
    }

    public void setYearNumber(int yearNumber) {
        this.yearNumber = yearNumber;
    }
}
