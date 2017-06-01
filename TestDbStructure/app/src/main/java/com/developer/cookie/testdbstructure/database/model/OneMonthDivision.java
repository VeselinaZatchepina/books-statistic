package com.developer.cookie.testdbstructure.database.model;

import io.realm.RealmObject;

public class OneMonthDivision extends RealmObject {
    private long id;
    private BookCategory category;
    private int january;
    private int february;
    private int march;
    private int april;
    private int may;
    private int june;
    private int july;
    private int august;
    private int september;
    private int october;
    private int november;
    private int december;
    private Year year;

    public int getApril() {
        return april;
    }

    public void setApril(int april) {
        this.april = april;
    }

    public int getAugust() {
        return august;
    }

    public void setAugust(int august) {
        this.august = august;
    }

    public BookCategory getCategory() {
        return category;
    }

    public void setCategory(BookCategory category) {
        this.category = category;
    }

    public int getDecember() {
        return december;
    }

    public void setDecember(int december) {
        this.december = december;
    }

    public int getFebruary() {
        return february;
    }

    public void setFebruary(int february) {
        this.february = february;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getJuly() {
        return july;
    }

    public void setJuly(int july) {
        this.july = july;
    }

    public int getJanuary() {
        return january;
    }

    public void setJanuary(int january) {
        this.january = january;
    }

    public int getJune() {
        return june;
    }

    public void setJune(int june) {
        this.june = june;
    }

    public int getMarch() {
        return march;
    }

    public void setMarch(int march) {
        this.march = march;
    }

    public int getMay() {
        return may;
    }

    public void setMay(int may) {
        this.may = may;
    }

    public int getNovember() {
        return november;
    }

    public void setNovember(int november) {
        this.november = november;
    }

    public int getOctober() {
        return october;
    }

    public void setOctober(int october) {
        this.october = october;
    }

    public int getSeptember() {
        return september;
    }

    public void setSeptember(int september) {
        this.september = september;
    }

    public Year getYear() {
        return year;
    }

    public void setYear(Year year) {
        this.year = year;
    }
}
