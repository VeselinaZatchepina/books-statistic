package com.github.veselinazatchepina.bookstatistics.database.model;


import io.realm.RealmObject;

public class BookMonthDivision extends RealmObject {
    private long id;
    private float categoryIndex;
    private BookCategory category;
    private Year year;

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

    private float januaryMarch;
    private float aprilJune;
    private float julySeptember;
    private float octoberDecember;

    private float januaryJune;
    private float julyDecember;

    private float januaryDecember;

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

    public BookCategory getCategory() {
        return category;
    }

    public void setCategory(BookCategory category) {
        this.category = category;
    }

    public Year getYear() {
        return year;
    }

    public void setYear(Year year) {
        this.year = year;
    }

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

    public float getJanuaryDecember() {
        return januaryDecember;
    }

    public void setJanuaryDecember(float januaryDecember) {
        this.januaryDecember = januaryDecember;
    }
}
