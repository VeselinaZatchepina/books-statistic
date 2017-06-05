package com.developer.cookie.testdbstructure.database;

import com.developer.cookie.testdbstructure.database.model.AllBookMonthDivision;
import com.developer.cookie.testdbstructure.database.model.Book;
import com.developer.cookie.testdbstructure.database.model.BookCategory;
import com.developer.cookie.testdbstructure.database.model.BookMonthDivision;
import com.developer.cookie.testdbstructure.utils.DivisionType;
import com.developer.cookie.testdbstructure.utils.MonthIndex;

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
                createBook(realm, getBookCategory(realm, "IT0"), "10.02.2017", "12.02.2017");
                createBook(realm, getBookCategory(realm, "IT1"), "10.02.2017", "12.02.2017");
                createBook(realm, getBookCategory(realm, "IT2"), "10.06.2017", "12.06.2017");
                createBook(realm, getBookCategory(realm, "IT0"), "10.12.2017", "13.12.2017");
                createBook(realm, getBookCategory(realm, "IT3"), "10.01.2017", "12.04.2017");
                createBook(realm, getBookCategory(realm, "IT1"), "10.07.2017", "12.09.2017");
                createBook(realm, getBookCategory(realm, "IT3"), "10.10.2017", "12.11.2017");
                createBook(realm, getBookCategory(realm, "IT0"), "10.09.2017", "12.09.2017");
                createBook(realm, getBookCategory(realm, "IT0"), "10.12.2017", "13.12.2017");
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

    private BookCategory getBookCategory(Realm realm, String bookCategoryName) {
        RealmResults<BookCategory> bookCategories = getBookCategoryByCategoryName(realm, bookCategoryName);
        BookCategory bookCategory;
        if (!bookCategories.isEmpty()) {
            bookCategory = bookCategories.first();
            bookCategory.setCategoryBookCount(bookCategory.getCategoryBookCount() + 1);
        } else {
            bookCategory = createBookCategory(realm, bookCategoryName);
        }
        return bookCategory;
    }

    private RealmResults<BookCategory> getBookCategoryByCategoryName(Realm realm, String bookCategoryName) {
        return realm.where(BookCategory.class).equalTo("categoryName", bookCategoryName).findAll();
    }

    private BookCategory createBookCategory(Realm realm, String bookCategoryName) {
        BookCategory bookCategory = realm.createObject(BookCategory.class);
        bookCategory.setId(getNextKey(bookCategory, realm));
        bookCategory.setCategoryName(bookCategoryName);
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
                RealmResults<AllBookMonthDivision> allBookResults = getAllBookMonthDivisionByMonth(index);
                AllBookMonthDivision currentMonth = allBookResults.first();
                currentMonth.setYear(book.getYear());
                setAllBookCount(currentMonth, divisionType);
            }
        });
    }

    private RealmResults<AllBookMonthDivision> getAllBookMonthDivisionByMonth(float index) {
        return mRealm.where(AllBookMonthDivision.class).equalTo("month", index).findAll();
    }

    private void setAllBookCount(AllBookMonthDivision currentMonth, String divisionType) {
        switch (divisionType) {
            case DivisionType.ONE:
                currentMonth.setAllBookCountOneMonth(currentMonth.getAllBookCountOneMonth() + 1);
                break;
            case DivisionType.THREE:
                currentMonth.setAllBookCountThreeMonth(currentMonth.getAllBookCountThreeMonth() + 1);
                break;
            case DivisionType.SIX:
                currentMonth.setAllBookCountSixMonth(currentMonth.getAllBookCountSixMonth() + 1);
                break;
            case DivisionType.TWELVE:
                currentMonth.setAllBookCountTwelveMonth(currentMonth.getAllBookCountTwelveMonth() + 1);
                break;
        }
    }

    private void initAllBookMonth() {
        RealmResults<AllBookMonthDivision> allBookResults = mRealm.where(AllBookMonthDivision.class).findAll();
        if (allBookResults.isEmpty()) {
            createAllBookMonthDivision();
        }
        isInitAllMonth = true;
    }

    private void createAllBookMonthDivision() {
        for (int i = 0; i <= MonthIndex.ELEVEN; i++) {
            AllBookMonthDivision allBookMonthDivision = mRealm.createObject(AllBookMonthDivision.class);
            allBookMonthDivision.setId(getNextKey(allBookMonthDivision, mRealm));
            allBookMonthDivision.setMonth(i);
        }
    }

    public void saveBookInMonthDivision(final Book book, final float index, final String monthDivisionType) {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                BookMonthDivision bookMonthDivision;
                RealmResults<BookMonthDivision> realmResults = getBookMonthDivisionByCategory(book.getBookCategory().getCategoryName());
                if (realmResults.isEmpty()) {
                    bookMonthDivision = createBookMonthDivision(book);
                } else {
                    bookMonthDivision = realmResults.first();
                }
                checkMonth((int) index, bookMonthDivision, monthDivisionType);
            }
        });
    }

    private BookMonthDivision createBookMonthDivision(Book book) {
        BookMonthDivision bookMonthDivision = mRealm.createObject(BookMonthDivision.class);
        bookMonthDivision.setId(getNextKey(bookMonthDivision, mRealm));
        bookMonthDivision.setCategoryIndex(bookMonthDivision.getId());
        bookMonthDivision.setYear(book.getYear());
        bookMonthDivision.setCategory(book.getBookCategory());
        return bookMonthDivision;
    }

    private void checkMonth(int index, BookMonthDivision bookMonthDivision, String monthDivisionType) {
        switch (index) {
            case MonthIndex.ZERO :
                checkIndexCase0(bookMonthDivision, monthDivisionType);
                break;
            case MonthIndex.ONE :
                checkIndexCase1(bookMonthDivision, monthDivisionType);
                break;
            case MonthIndex.TWO :
                checkIndexCase2(bookMonthDivision, monthDivisionType);
                break;
            case MonthIndex.THREE :
                checkIndexCase3(bookMonthDivision, monthDivisionType);
                break;
            case MonthIndex.FOUR :
                bookMonthDivision.setMay(bookMonthDivision.getMay() + 1);
                break;
            case MonthIndex.FIVE :
                bookMonthDivision.setJune(bookMonthDivision.getJune() + 1);
                break;
            case MonthIndex.SIX :
                bookMonthDivision.setJuly(bookMonthDivision.getJuly() + 1);
                break;
            case MonthIndex.SEVEN :
                bookMonthDivision.setAugust(bookMonthDivision.getAugust() + 1);
                break;
            case MonthIndex.EIGHT :
                bookMonthDivision.setSeptember(bookMonthDivision.getSeptember() + 1);
                break;
            case MonthIndex.NINE :
                bookMonthDivision.setOctober(bookMonthDivision.getOctober() + 1);
                break;
            case MonthIndex.TEN :
                bookMonthDivision.setNovember(bookMonthDivision.getNovember() + 1);
                break;
            case MonthIndex.ELEVEN :
                bookMonthDivision.setDecember(bookMonthDivision.getDecember() + 1);
                break;
        }
    }

    private void checkIndexCase0(BookMonthDivision bookMonthDivision, String monthDivisionType) {
        switch (monthDivisionType) {
            case DivisionType.ONE :
                bookMonthDivision.setJanuary(bookMonthDivision.getJanuary() + 1);
                break;
            case DivisionType.THREE :
                bookMonthDivision.setJanuaryMarch(bookMonthDivision.getJanuaryMarch() + 1);
                break;
            case DivisionType.SIX :
                bookMonthDivision.setJanuaryJune(bookMonthDivision.getJanuaryJune() + 1);
                break;
            case DivisionType.TWELVE :
                bookMonthDivision.setJanuaryDecember(bookMonthDivision.getJanuaryDecember() + 1);
                break;
        }
    }

    private void checkIndexCase1(BookMonthDivision bookMonthDivision, String monthDivisionType) {
        switch (monthDivisionType) {
            case DivisionType.ONE :
                bookMonthDivision.setFebruary(bookMonthDivision.getFebruary() + 1);
                break;
            case DivisionType.THREE :
                bookMonthDivision.setAprilJune(bookMonthDivision.getAprilJune() + 1);
                break;
            case DivisionType.SIX :
                bookMonthDivision.setJulyDecember(bookMonthDivision.getJulyDecember() + 1);
                break;
        }
    }

    private void checkIndexCase2(BookMonthDivision bookMonthDivision, String monthDivisionType) {
        switch (monthDivisionType) {
            case DivisionType.ONE :
                bookMonthDivision.setMarch(bookMonthDivision.getMarch() + 1);
                break;
            case DivisionType.THREE :
                bookMonthDivision.setJulySeptember(bookMonthDivision.getJulySeptember() + 1);
                break;
        }
    }

    private void checkIndexCase3(BookMonthDivision bookMonthDivision, String monthDivisionType) {
        switch (monthDivisionType) {
            case DivisionType.ONE :
                bookMonthDivision.setApril(bookMonthDivision.getApril() + 1);
                break;
            case DivisionType.THREE :
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
