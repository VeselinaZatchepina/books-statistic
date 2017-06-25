package com.github.veselinazatchepina.bookstatistics.database;


import com.github.veselinazatchepina.bookstatistics.books.enums.BookPropertiesEnum;
import com.github.veselinazatchepina.bookstatistics.database.model.Book;
import com.github.veselinazatchepina.bookstatistics.database.model.BookCategory;
import com.github.veselinazatchepina.bookstatistics.database.model.BookRating;
import com.github.veselinazatchepina.bookstatistics.database.model.Section;
import com.github.veselinazatchepina.bookstatistics.database.model.Year;

import java.util.HashMap;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;

public class BooksRealmRepository implements RealmRepository {

    private final Realm mRealm;

    public BooksRealmRepository() {
        mRealm = Realm.getDefaultInstance();
    }

    @Override
    public RealmResults<BookCategory> getListOfBookCategories() {
        return mRealm.where(BookCategory.class).findAllSortedAsync("id");
    }

    @Override
    public void saveQuote(final HashMap<BookPropertiesEnum, String> mapOfQuoteProperties) {
        mRealm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                saveBook(realm, mapOfQuoteProperties);
            }
        });
    }

    private void saveBook(Realm realm, HashMap<BookPropertiesEnum, String> mapOfQuoteProperties) {
        Book book = realm.createObject(Book.class);
        book.setId(getNextKey(book, realm));
        book.setBookName(mapOfQuoteProperties.get(BookPropertiesEnum.BOOK_NAME));
        book.setAuthorName(mapOfQuoteProperties.get(BookPropertiesEnum.BOOK_AUTHOR));
        book.setPageCount(Integer.valueOf(mapOfQuoteProperties.get(BookPropertiesEnum.BOOK_PAGE)));
        book.setDateStart(mapOfQuoteProperties.get(BookPropertiesEnum.BOOK_DATE_START));
        book.setDateEnd(mapOfQuoteProperties.get(BookPropertiesEnum.BOOK_DATE_END));
        book.setBookCategory(checkAndGetCurrentCategory(realm, mapOfQuoteProperties.get(BookPropertiesEnum.BOOK_CATEGORY)));
        book.setSection(createSection(realm, mapOfQuoteProperties));
        book.setRating(createBookRating(realm, mapOfQuoteProperties));
        book.setYear(createYear(realm, mapOfQuoteProperties));
    }

    private BookCategory checkAndGetCurrentCategory(Realm realm, String valueOfCategory) {
        RealmResults<BookCategory> bookCategories = realm.where(BookCategory.class)
                .contains("categoryName", valueOfCategory)
                .findAll();
        BookCategory bookCategory;
        if (bookCategories == null || bookCategories.isEmpty()) {
            bookCategory = createBookCategory(realm, valueOfCategory);
        } else {
            bookCategory = bookCategories.first();
            changeBookCountCurrentCategory(bookCategory);
        }
        return bookCategory;
    }

    private BookCategory createBookCategory(Realm realm, String valueOfCategory) {
        BookCategory bookCategory = realm.createObject(BookCategory.class);
        bookCategory.setId(getNextKey(bookCategory, realm));
        bookCategory.setCategoryName(valueOfCategory);
        bookCategory.setCategoryBookCount(1);
        return bookCategory;
    }

    private void changeBookCountCurrentCategory(BookCategory bookCategory) {
        bookCategory.setCategoryBookCount(
                bookCategory.getCategoryBookCount() + 1);
    }

    private Section createSection(Realm realm, HashMap<BookPropertiesEnum, String> mapOfQuoteProperties) {
        Section section = realm.createObject(Section.class);
        section.setId(getNextKey(section, realm));
        section.setSectionName(mapOfQuoteProperties.get(BookPropertiesEnum.BOOK_TYPE));
        section.setSectionBookCount(1);
        return section;
    }

    private BookRating createBookRating(Realm realm, HashMap<BookPropertiesEnum, String> mapOfQuoteProperties) {
        BookRating bookRating = realm.createObject(BookRating.class);
        bookRating.setId(getNextKey(bookRating, realm));
        bookRating.setStarsCount(Integer.valueOf(mapOfQuoteProperties.get(BookPropertiesEnum.BOOK_RATING)));
        bookRating.setRatingBookCount(1);
        return bookRating;
    }

    private Year createYear(Realm realm, HashMap<BookPropertiesEnum, String> mapOfQuoteProperties) {
        Year year = realm.createObject(Year.class);
        year.setId(getNextKey(year, realm));
        year.setYearNumber(getYearNumber(mapOfQuoteProperties.get(BookPropertiesEnum.BOOK_DATE_START)));
        return year;
    }

    private int getYearNumber(String date) {
        String[] dayMonthYearArray = date.split("/");
        return Integer.valueOf(dayMonthYearArray[2].trim());
    }

    /**
     * Method is used for increment "id".
     *
     * @param currentClass current class
     * @param realm current db
     * @return id
     */
    private int getNextKey(RealmObject currentClass, Realm realm) {
        int id;
        try {
            id = realm.where(currentClass.getClass()).max("id").intValue() + 1;
        } catch (ArrayIndexOutOfBoundsException ex) {
            id = 0;
        }
        return id;
    }

    @Override
    public void closeDbConnect() {
        mRealm.close();
    }
}
