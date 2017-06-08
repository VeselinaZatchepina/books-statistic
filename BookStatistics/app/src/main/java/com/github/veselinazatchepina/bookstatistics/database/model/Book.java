package com.github.veselinazatchepina.bookstatistics.database.model;

import io.realm.RealmObject;


public class Book extends RealmObject {
    private long id;
    private String bookName;
    private String authorName;
    private int pageCount;
    private BookCategory bookCategory;
    private Section section;
    private String dateStart;
    private String dateEnd;
    private BookRating rating;
    private Year year;

    public Book() {

    }

    public Book(long id, String authorName, BookCategory bookCategory,
                String bookName, String dateEnd, String dateStart,
                int pageCount, BookRating rating, Section section, Year year) {
        this.id = id;
        this.authorName = authorName;
        this.bookCategory = bookCategory;
        this.bookName = bookName;
        this.dateEnd = dateEnd;
        this.dateStart = dateStart;
        this.pageCount = pageCount;
        this.rating = rating;
        this.section = section;
        this.year = year;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public Section getSection() {
        return section;
    }

    public void setSection(Section section) {
        this.section = section;
    }

    public BookCategory getBookCategory() {
        return bookCategory;
    }

    public void setBookCategory(BookCategory bookCategory) {
        this.bookCategory = bookCategory;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(String dateEnd) {
        this.dateEnd = dateEnd;
    }

    public String getDateStart() {
        return dateStart;
    }

    public void setDateStart(String dateStart) {
        this.dateStart = dateStart;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public BookRating getRating() {
        return rating;
    }

    public void setRating(BookRating rating) {
        this.rating = rating;
    }

    public Year getYear() {
        return year;
    }

    public void setYear(Year year) {
        this.year = year;
    }
}
