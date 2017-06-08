package com.github.veselinazatchepina.bookstatistics.database.model;

import io.realm.RealmObject;

public class Section extends RealmObject {
    private long id;
    private String sectionName;
    private int sectionBookCount;

    public Section() {

    }

    public Section(long id, String sectionName, int sectionBookCount) {
        this.id = id;
        this.sectionName = sectionName;
        this.sectionBookCount = sectionBookCount;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getSectionBookCount() {
        return sectionBookCount;
    }

    public void setSectionBookCount(int sectionBookCount) {
        this.sectionBookCount = sectionBookCount;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }
}
