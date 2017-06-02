package com.developer.cookie.testdbstructure.database.model;

import io.realm.RealmObject;

public class OneMonthDivision extends RealmObject {
    private long id;
    private float categoryIndex;
    private BookCategory category;
    private float january;
    private float february;
    private float march;
    private float april;
    private float may;
    private float june;
    private float july;
    private float august;
    private float september;
    private float october;
    private float november;
    private float december;
    private Year year;

    public float getJanuary() {
        return january;
    }

    public void setJanuary(float january) {
        this.january = january;
    }

    public float getFebruary() {
        return february;
    }

    public void setFebruary(float february) {
        this.february = february;
    }

    public float getMarch() {
        return march;
    }

    public void setMarch(float march) {
        this.march = march;
    }

    public float getApril() {
        return april;
    }

    public void setApril(float april) {
        this.april = april;
    }

    public float getMay() {
        return may;
    }

    public void setMay(float may) {
        this.may = may;
    }

    public float getJune() {
        return june;
    }

    public void setJune(float june) {
        this.june = june;
    }

    public float getJuly() {
        return july;
    }

    public void setJuly(float july) {
        this.july = july;
    }

    public float getAugust() {
        return august;
    }

    public void setAugust(float august) {
        this.august = august;
    }

    public float getSeptember() {
        return september;
    }

    public void setSeptember(float september) {
        this.september = september;
    }

    public float getOctober() {
        return october;
    }

    public void setOctober(float october) {
        this.october = october;
    }

    public float getNovember() {
        return november;
    }

    public void setNovember(float november) {
        this.november = november;
    }

    public float getDecember() {
        return december;
    }

    public void setDecember(float december) {
        this.december = december;
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

    public float getCategoryIndex() {
        return categoryIndex;
    }

    public void setCategoryIndex(float categoryIndex) {
        this.categoryIndex = categoryIndex;
    }
}
