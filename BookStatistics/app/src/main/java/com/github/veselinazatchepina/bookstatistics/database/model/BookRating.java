package com.github.veselinazatchepina.bookstatistics.database.model;

import io.realm.RealmObject;

public class BookRating extends RealmObject {
    private long id;
    private int starsCount;
    private int ratingBookCount;

    public BookRating() {

    }

    public BookRating(long id, int starsCount, int ratingBookCount) {
        this.id = id;
        this.starsCount = starsCount;
        this.ratingBookCount = ratingBookCount;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getRatingBookCount() {
        return ratingBookCount;
    }

    public void setRatingBookCount(int ratingBookCount) {
        this.ratingBookCount = ratingBookCount;
    }

    public int getStarsCount() {
        return starsCount;
    }

    public void setStarsCount(int starsCount) {
        this.starsCount = starsCount;
    }
}
