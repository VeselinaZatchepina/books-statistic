package com.developer.cookie.testdbstructure.database;

import com.developer.cookie.testdbstructure.database.model.AllBookMonthDivision;
import com.developer.cookie.testdbstructure.database.model.Book;
import com.developer.cookie.testdbstructure.database.model.BookCategory;
import com.developer.cookie.testdbstructure.database.model.BookMonthDivision;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;

public class RealmRepository {

    private final Realm mRealm;
    private Boolean isInitAllMonth = false;

    public RealmRepository() {
        mRealm = Realm.getDefaultInstance();
    }

    public void saveBook() {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                createBook(realm, createBookCategory(realm, "IT0"), "10.02.2017", "12.02.2017");
                createBook(realm, createBookCategory(realm, "IT1"), "10.02.2017", "12.02.2017");
                createBook(realm, createBookCategory(realm, "IT2"), "10.06.2017", "12.06.2017");
                createBook(realm, createBookCategory(realm, "IT0"), "10.06.2017", "12.06.2017");
                createBook(realm, createBookCategory(realm, "IT0"), "10.12.2017", "13.12.2017");
                createBook(realm, createBookCategory(realm, "IT3"), "10.01.2017", "12.04.2017");
                createBook(realm, createBookCategory(realm, "IT1"), "10.07.2017", "12.09.2017");
                createBook(realm, createBookCategory(realm, "IT3"), "10.10.2017", "12.11.2017");
                createBook(realm, createBookCategory(realm, "IT0"), "10.09.2017", "12.09.2017");
                createBook(realm, createBookCategory(realm, "IT0"), "10.12.2017", "13.12.2017");
            }
        });
    }

    private Book createBook(Realm realm, BookCategory bookCategory, String dateStart, String dateEnd) {
        Book book = realm.createObject(Book.class);
        book.setId(getNextKey(book, realm));
        book.setBookCategory(bookCategory);
        book.setDateStart(dateStart);
        book.setDateEnd(dateEnd);
        return book;
    }

    private BookCategory createBookCategory(Realm realm, String bookCategoryName) {
        RealmResults<BookCategory> bookCategories = realm.where(BookCategory.class).equalTo("categoryName", bookCategoryName).findAll();
        BookCategory bookCategory;
        if (!bookCategories.isEmpty()) {
            bookCategory = bookCategories.first();
            bookCategory.setCategoryBookCount(bookCategory.getCategoryBookCount() + 1);
        } else {
            bookCategory = realm.createObject(BookCategory.class);
            bookCategory.setId(getNextKey(bookCategory, realm));
            bookCategory.setCategoryName(bookCategoryName);
            bookCategory.setCategoryBookCount(1);
        }
        return bookCategory;
    }

    private int getNextKey(RealmObject currentClass, Realm realm) {
        int id;
        try {
            id = realm.where(currentClass.getClass()).max("id").intValue() + 1;
        } catch (ArrayIndexOutOfBoundsException ex) {
            id = 0;
        }
        return id;
    }

    public void saveBookInAllBookMonthDivision(final Book book, final float index, final String divisionType) {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                if (!isInitAllMonth) {
                    initAllBookMonth();
                }
                RealmResults<AllBookMonthDivision> allBookResults = mRealm.where(AllBookMonthDivision.class)
                        .equalTo("month", index).findAll();
                AllBookMonthDivision currentMonth = allBookResults.first();
                currentMonth.setYear(book.getYear());
                switch (divisionType) {
                    case "one":
                        currentMonth.setAllBookCountOneMonth(currentMonth.getAllBookCountOneMonth() + 1);
                        break;
                    case "three":
                        currentMonth.setAllBookCountThreeMonth(currentMonth.getAllBookCountThreeMonth() + 1);
                        break;
                    case "six":
                        currentMonth.setAllBookCountSixMonth(currentMonth.getAllBookCountSixMonth() + 1);
                        break;
                    case "twelve":
                        currentMonth.setAllBookCountTwelveMonth(currentMonth.getAllBookCountTwelveMonth() + 1);
                        break;
                }
            }
        });
    }

    private void initAllBookMonth() {
        AllBookMonthDivision allBookMonthDivision;
        RealmResults<AllBookMonthDivision> allBookResults = mRealm.where(AllBookMonthDivision.class).findAll();
        if (allBookResults.isEmpty()) {
            for (int i = 0; i < 12; i++) {
                allBookMonthDivision = mRealm.createObject(AllBookMonthDivision.class);
                allBookMonthDivision.setId(getNextKey(allBookMonthDivision, mRealm));
                allBookMonthDivision.setMonth(i);
            }
        }
        isInitAllMonth = true;
    }

    public void saveBookInMonthDivision(final Book book, final float index, final String monthDivisionType) {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                BookMonthDivision bookMonthDivision;
                RealmResults<BookMonthDivision> realmResults = mRealm.where(BookMonthDivision.class)
                        .equalTo("category.categoryName", book.getBookCategory().getCategoryName())
                        .findAll();
                if (realmResults.isEmpty()) {
                    bookMonthDivision = mRealm.createObject(BookMonthDivision.class);
                    bookMonthDivision.setId(getNextKey(bookMonthDivision, mRealm));
                    bookMonthDivision.setCategoryIndex(bookMonthDivision.getId());
                    bookMonthDivision.setYear(book.getYear());
                    bookMonthDivision.setCategory(book.getBookCategory());
                } else {
                    bookMonthDivision = realmResults.first();
                }
                checkMonth((int) index, bookMonthDivision, monthDivisionType);
            }
        });
    }

    private void checkMonth(int index, BookMonthDivision bookMonthDivision, String monthDivisionType) {
        switch (index) {
            case 0:
                checkIndexCase0(bookMonthDivision, monthDivisionType);
                break;
            case 1:
                checkIndexCase1(bookMonthDivision, monthDivisionType);
                break;
            case 2:
                checkIndexCase2(bookMonthDivision, monthDivisionType);
                break;
            case 3:
                checkIndexCase3(bookMonthDivision, monthDivisionType);
                break;
            case 4:
                bookMonthDivision.setMay(bookMonthDivision.getMay() + 1);
                break;
            case 5:
                bookMonthDivision.setJune(bookMonthDivision.getJune() + 1);
                break;
            case 6:
                bookMonthDivision.setJuly(bookMonthDivision.getJuly() + 1);
                break;
            case 7:
                bookMonthDivision.setAugust(bookMonthDivision.getAugust() + 1);
                break;
            case 8:
                bookMonthDivision.setSeptember(bookMonthDivision.getSeptember() + 1);
                break;
            case 9:
                bookMonthDivision.setOctober(bookMonthDivision.getOctober() + 1);
                break;
            case 10:
                bookMonthDivision.setNovember(bookMonthDivision.getNovember() + 1);
                break;
            case 11:
                bookMonthDivision.setDecember(bookMonthDivision.getDecember() + 1);
                break;
        }
    }

    private void checkIndexCase0(BookMonthDivision bookMonthDivision, String monthDivisionType) {
        switch (monthDivisionType) {
            case "one" :
                bookMonthDivision.setJanuary(bookMonthDivision.getJanuary() + 1);
                break;
            case "three" :
                bookMonthDivision.setJanuaryMarch(bookMonthDivision.getJanuaryMarch() + 1);
                break;
            case "six" :
                bookMonthDivision.setJanuaryJune(bookMonthDivision.getJanuaryJune() + 1);
                break;
            case "twelve" :
                bookMonthDivision.setJanuaryDecember(bookMonthDivision.getJanuaryDecember() + 1);
                break;
        }
    }

    private void checkIndexCase1(BookMonthDivision bookMonthDivision, String monthDivisionType) {
        switch (monthDivisionType) {
            case "one" :
                bookMonthDivision.setFebruary(bookMonthDivision.getFebruary() + 1);
                break;
            case "three" :
                bookMonthDivision.setAprilJune(bookMonthDivision.getAprilJune() + 1);
                break;
            case "six" :
                bookMonthDivision.setJulyDecember(bookMonthDivision.getJulyDecember() + 1);
                break;
        }
    }

    private void checkIndexCase2(BookMonthDivision bookMonthDivision, String monthDivisionType) {
        switch (monthDivisionType) {
            case "one" :
                bookMonthDivision.setMarch(bookMonthDivision.getMarch() + 1);
                break;
            case "three" :
                bookMonthDivision.setJulySeptember(bookMonthDivision.getJulySeptember() + 1);
                break;
        }
    }

    private void checkIndexCase3(BookMonthDivision bookMonthDivision, String monthDivisionType) {
        switch (monthDivisionType) {
            case "one" :
                bookMonthDivision.setApril(bookMonthDivision.getApril() + 1);
                break;
            case "three" :
                bookMonthDivision.setOctoberDecember(bookMonthDivision.getOctoberDecember() + 1);
                break;
        }
    }

    public RealmResults<AllBookMonthDivision> getAllBookMonth(int begin, int end) {
        return mRealm.where(AllBookMonthDivision.class).between("id", begin, end).findAll();
    }

    public RealmResults<BookMonthDivision> getBookMonthDivisionByCategory(String category) {
        return mRealm.where(BookMonthDivision.class).equalTo("category.categoryName", category).findAll();
    }

    public RealmResults<Book> getAllBooks() {
        return mRealm.where(Book.class).findAll();
    }

    public RealmResults<BookMonthDivision> getBookMonthDivision() {
        return mRealm.where(BookMonthDivision.class).findAll();
    }

    public void closeDb() {
        mRealm.close();
    }

}
