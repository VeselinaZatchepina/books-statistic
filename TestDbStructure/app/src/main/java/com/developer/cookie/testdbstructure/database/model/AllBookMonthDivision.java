package com.developer.cookie.testdbstructure.database.model;


import io.realm.RealmObject;

public class AllBookMonthDivision extends RealmObject {
    private long id;
    private float month;
    private float allBookCountOneMonth;
    private float allBookCountThreeMonth;
    private float allBookCountSixMonth;
    private float allBookCountTwelveMonth;
    private Year year;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public float getMonth() {
        return month;
    }

    public void setMonth(float month) {
        this.month = month;
    }

    public Year getYear() {
        return year;
    }

    public void setYear(Year year) {
        this.year = year;
    }

    public float getAllBookCountOneMonth() {
        return allBookCountOneMonth;
    }

    public void setAllBookCountOneMonth(float allBookCountOneMonth) {
        this.allBookCountOneMonth = allBookCountOneMonth;
    }

    public float getAllBookCountThreeMonth() {
        return allBookCountThreeMonth;
    }

    public void setAllBookCountThreeMonth(float allBookCountThreeMonth) {
        this.allBookCountThreeMonth = allBookCountThreeMonth;
    }

    public float getAllBookCountSixMonth() {
        return allBookCountSixMonth;
    }

    public void setAllBookCountSixMonth(float allBookCountSixMonth) {
        this.allBookCountSixMonth = allBookCountSixMonth;
    }

    public float getAllBookCountTwelveMonth() {
        return allBookCountTwelveMonth;
    }

    public void setAllBookCountTwelveMonth(float allBookCountTwelveMonth) {
        this.allBookCountTwelveMonth = allBookCountTwelveMonth;
    }
}
