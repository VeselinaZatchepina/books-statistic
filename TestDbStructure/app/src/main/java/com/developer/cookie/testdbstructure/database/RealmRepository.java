package com.developer.cookie.testdbstructure.database;

import com.developer.cookie.testdbstructure.database.model.AllBookMonthDivision;
import com.developer.cookie.testdbstructure.database.model.Book;
import com.developer.cookie.testdbstructure.database.model.BookCategory;
import com.developer.cookie.testdbstructure.database.model.OneMonthDivision;
import com.developer.cookie.testdbstructure.database.model.SixMonthDivision;
import com.developer.cookie.testdbstructure.database.model.ThreeMonthDivision;
import com.developer.cookie.testdbstructure.database.model.TwelveMonthDivision;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;

public class RealmRepository {

    private final Realm mRealm;
    private Boolean isInitAllOneMonth = false;
    private Boolean isInitAllThreeMonth = false;
    private Boolean isInitAllSixMonth = false;
    private Boolean isInitAllTwelveMonth = false;
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
                createBook(realm, createBookCategory(realm, "IT4"), "10.12.2017", "13.12.2017");
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

    public void saveBookInOneMonthDivision(final Book book, final float index) {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                OneMonthDivision oneMonthDivision;
                RealmResults<OneMonthDivision> realmResults = mRealm.where(OneMonthDivision.class)
                        .equalTo("category.categoryName", book.getBookCategory().getCategoryName())
                        .findAll();
                if (realmResults.isEmpty()) {
                    oneMonthDivision = mRealm.createObject(OneMonthDivision.class);
                    oneMonthDivision.setId(getNextKey(oneMonthDivision, mRealm));
                    oneMonthDivision.setCategoryIndex(oneMonthDivision.getId());
                    oneMonthDivision.setYear(book.getYear());
                    oneMonthDivision.setCategory(book.getBookCategory());
                } else {
                    oneMonthDivision = realmResults.first();
                }
                checkOneMonth((int) index, oneMonthDivision);
            }
        });
    }

    private void checkOneMonth(int index, OneMonthDivision oneMonthDivisions) {
        switch (index) {
            case 0:
                oneMonthDivisions.setJanuary(oneMonthDivisions.getJanuary() + 1);
                break;
            case 1:
                oneMonthDivisions.setFebruary(oneMonthDivisions.getFebruary() + 1);
                break;
            case 2:
                oneMonthDivisions.setMarch(oneMonthDivisions.getMarch() + 1);
                break;
            case 3:
                oneMonthDivisions.setApril(oneMonthDivisions.getApril() + 1);
                break;
            case 4:
                oneMonthDivisions.setMay(oneMonthDivisions.getMay() + 1);
                break;
            case 5:
                oneMonthDivisions.setJune(oneMonthDivisions.getJune() + 1);
                break;
            case 6:
                oneMonthDivisions.setJuly(oneMonthDivisions.getJuly() + 1);
                break;
            case 7:
                oneMonthDivisions.setAugust(oneMonthDivisions.getAugust() + 1);
                break;
            case 8:
                oneMonthDivisions.setSeptember(oneMonthDivisions.getSeptember() + 1);
                break;
            case 9:
                oneMonthDivisions.setOctober(oneMonthDivisions.getOctober() + 1);
                break;
            case 10:
                oneMonthDivisions.setNovember(oneMonthDivisions.getNovember() + 1);
                break;
            case 11:
                oneMonthDivisions.setDecember(oneMonthDivisions.getDecember() + 1);
                break;
        }
    }

    public void saveBookInThreeMonthDivision(final Book book, final float index) {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                ThreeMonthDivision threeMonthDivision;
                RealmResults<ThreeMonthDivision> realmResults = mRealm.where(ThreeMonthDivision.class)
                        .equalTo("category.categoryName", book.getBookCategory().getCategoryName())
                        .findAll();
                if (realmResults.isEmpty()) {
                    threeMonthDivision = mRealm.createObject(ThreeMonthDivision.class);
                    threeMonthDivision.setId(getNextKey(threeMonthDivision, mRealm));
                    threeMonthDivision.setCategoryIndex(threeMonthDivision.getId());
                    threeMonthDivision.setYear(book.getYear());
                    threeMonthDivision.setCategory(book.getBookCategory());
                } else {
                    threeMonthDivision = realmResults.first();
                }
                checkThreeMonth((int) index, threeMonthDivision);
            }
        });
    }

    private String checkThreeMonth(int index, ThreeMonthDivision threeMonthDivision) {
        switch (index) {
            case 0:
                threeMonthDivision.setJanuaryMarch(threeMonthDivision.getJanuaryMarch() + 1);
                break;
            case 1:
                threeMonthDivision.setAprilJune(threeMonthDivision.getAprilJune() + 1);
                break;
            case 2:
                threeMonthDivision.setJulySeptember(threeMonthDivision.getJulySeptember() + 1);
                break;
            case 3:
                threeMonthDivision.setOctoberDecember(threeMonthDivision.getOctoberDecember() + 1);
                break;
        }
        return null;
    }

    public void saveBookInSixMonthDivision(final Book book, final float index) {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                SixMonthDivision sixMonthDivision;
                RealmResults<SixMonthDivision> realmResults = mRealm.where(SixMonthDivision.class)
                        .equalTo("category.categoryName", book.getBookCategory().getCategoryName())
                        .findAll();
                if (realmResults.isEmpty()) {
                    sixMonthDivision = mRealm.createObject(SixMonthDivision.class);
                    sixMonthDivision.setId(getNextKey(sixMonthDivision, mRealm));
                    sixMonthDivision.setCategoryIndex(sixMonthDivision.getId());
                    sixMonthDivision.setYear(book.getYear());
                    sixMonthDivision.setCategory(book.getBookCategory());
                } else {
                    sixMonthDivision = realmResults.first();
                }
                checkSixMonth((int) index, sixMonthDivision);
            }
        });
    }

    private String checkSixMonth(int index, SixMonthDivision sixMonthDivision) {
        switch (index) {
            case 0:
                sixMonthDivision.setJanuaryJune(sixMonthDivision.getJanuaryJune() + 1);
                break;
            case 1:
                sixMonthDivision.setJulyDecember(sixMonthDivision.getJulyDecember() + 1);
                break;
        }
        return null;
    }

    public void saveBookInTwelveMonthDivision(final Book book, final float index) {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                TwelveMonthDivision twelveMonthDivision;
                RealmResults<TwelveMonthDivision> realmResults = mRealm.where(TwelveMonthDivision.class)
                        .equalTo("category.categoryName", book.getBookCategory().getCategoryName())
                        .findAll();
                if (realmResults.isEmpty()) {
                    twelveMonthDivision = mRealm.createObject(TwelveMonthDivision.class);
                    twelveMonthDivision.setId(getNextKey(twelveMonthDivision, mRealm));
                    twelveMonthDivision.setCategoryIndex(twelveMonthDivision.getId());
                    twelveMonthDivision.setYear(book.getYear());
                    twelveMonthDivision.setCategory(book.getBookCategory());
                } else {
                    twelveMonthDivision = realmResults.first();
                }
                checkTwelveMonth((int) index, twelveMonthDivision);
            }
        });
    }

    private String checkTwelveMonth(int index, TwelveMonthDivision twelveMonthDivision) {
        switch (index) {
            case 0:
                twelveMonthDivision.setJanuaryDecember(twelveMonthDivision.getJanuaryDecember() + 1);
                break;
        }
        return null;
    }

    public RealmResults<Book> getBookByEndDate(String date) {
        return mRealm.where(Book.class).equalTo("dateEnd", date).findAll();
    }

    public RealmResults<AllBookMonthDivision> getAllBookMonth(int begin, int end) {
        return mRealm.where(AllBookMonthDivision.class).between("id", begin, end).findAll();
    }

    public RealmResults<TwelveMonthDivision> getTwelveMonthDivision() {
        return mRealm.where(TwelveMonthDivision.class).findAll();
    }

    public RealmResults<OneMonthDivision> getOneMonthDivision() {
        return mRealm.where(OneMonthDivision.class).findAll();
    }

    public RealmResults<ThreeMonthDivision> getThreeMonthDivision() {
        return mRealm.where(ThreeMonthDivision.class).findAll();
    }

    public RealmResults<SixMonthDivision> getSixMonthDivision() {
        return mRealm.where(SixMonthDivision.class).findAll();
    }

    public RealmResults<OneMonthDivision> getOneMonthDivisionByCategory(String categoryName) {
        return mRealm.where(OneMonthDivision.class).equalTo("category.categoryName", categoryName).findAll();
    }

    public RealmResults<Book> getAllBooks() {
        return mRealm.where(Book.class).findAll();
    }

    public void closeDb() {
        mRealm.close();
    }

}
