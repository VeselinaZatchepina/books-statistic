package com.developer.cookie.testdbstructure.db;

import com.developer.cookie.testdbstructure.db.model.AllBookOneMonthDivision;
import com.developer.cookie.testdbstructure.db.model.Book;
import com.developer.cookie.testdbstructure.db.model.BookCategory;
import com.developer.cookie.testdbstructure.db.model.OneMonthDivision;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;

public class RealmRepository {

    private final Realm mRealm;
    private Boolean isInitAllOneMonth = false;

    public RealmRepository() {
        mRealm = Realm.getDefaultInstance();
    }

    public void saveBook() {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
//                createBook(realm, createBookCategory(realm, "IT0"), "10.02.2017", "12.02.2017");
//                createBook(realm, createBookCategory(realm, "IT1"), "10.02.2017", "12.02.2017");
//                createBook(realm, createBookCategory(realm, "IT2"), "10.06.2017", "12.06.2017");
//                createBook(realm, createBookCategory(realm, "IT0"), "10.06.2017", "12.06.2017");
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

    public RealmResults<Book> getAllBooks() {
        return mRealm.where(Book.class).findAll();
    }

    public void saveBookInAllBookOneMonth(final Book book, final int index) {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                if (!isInitAllOneMonth) {
                    initAllBookOneMonth();
                }
                RealmResults<AllBookOneMonthDivision> allBookResults = mRealm.where(AllBookOneMonthDivision.class)
                        .equalTo("month", index).findAll();
                AllBookOneMonthDivision currentMonth = allBookResults.first();
                currentMonth.setYear(book.getYear());
                currentMonth.setAllBookCount(currentMonth.getAllBookCount() + 1);

            }
        });
    }

    private void initAllBookOneMonth() {
        AllBookOneMonthDivision oneMonthDivisionForAllCategory;
        RealmResults<AllBookOneMonthDivision> allBookResults = mRealm.where(AllBookOneMonthDivision.class).findAll();
        if (allBookResults.isEmpty()) {
            for (int i = 0; i < 12; i++) {
                oneMonthDivisionForAllCategory = mRealm.createObject(AllBookOneMonthDivision.class);
                oneMonthDivisionForAllCategory.setId(getNextKey(oneMonthDivisionForAllCategory, mRealm));
                oneMonthDivisionForAllCategory.setMonth(i);
            }
        }
        isInitAllOneMonth = true;
    }

    public void saveBookInOneMonthDivision(final Book book, final int index) {
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
                    oneMonthDivision.setYear(book.getYear());
                    oneMonthDivision.setCategory(book.getBookCategory());
                } else {
                    oneMonthDivision = realmResults.first();
                }
                checkOneMonth(index, oneMonthDivision);
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

    public RealmResults<Book> getBookByEndDate(String date) {
        return mRealm.where(Book.class).equalTo("dateEnd", date).findAll();
    }

    public void closeDb() {
        mRealm.close();
    }

}
