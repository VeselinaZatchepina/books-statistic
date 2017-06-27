package com.github.veselinazatchepina.bookstatistics.database;


import android.util.Log;

import com.github.veselinazatchepina.bookstatistics.books.enums.BookPropertiesEnum;
import com.github.veselinazatchepina.bookstatistics.books.enums.DivisionType;
import com.github.veselinazatchepina.bookstatistics.books.enums.MonthIndex;
import com.github.veselinazatchepina.bookstatistics.books.fragments.AddBookFragment;
import com.github.veselinazatchepina.bookstatistics.database.model.AllBookMonthDivision;
import com.github.veselinazatchepina.bookstatistics.database.model.Book;
import com.github.veselinazatchepina.bookstatistics.database.model.BookCategory;
import com.github.veselinazatchepina.bookstatistics.database.model.BookMonthDivision;
import com.github.veselinazatchepina.bookstatistics.database.model.BookRating;
import com.github.veselinazatchepina.bookstatistics.database.model.Section;
import com.github.veselinazatchepina.bookstatistics.database.model.Year;
import com.github.veselinazatchepina.bookstatistics.utils.Division;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;

public class BooksRealmRepository implements RealmRepository {

    private final Realm mRealm;
    private Boolean isInitAllMonth = false;

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
                Book currentBook = saveBook(realm, mapOfQuoteProperties);
                saveBookInTablesForChart(realm, currentBook);
            }
        });
    }

    private Book saveBook(Realm realm, HashMap<BookPropertiesEnum, String> mapOfQuoteProperties) {
        Book book = realm.createObject(Book.class);
        book.setId(getNextKey(book, realm));
        book.setBookName(mapOfQuoteProperties.get(BookPropertiesEnum.BOOK_NAME));
        book.setAuthorName(mapOfQuoteProperties.get(BookPropertiesEnum.BOOK_AUTHOR));
        book.setPageCount(Integer.valueOf(mapOfQuoteProperties.get(BookPropertiesEnum.BOOK_PAGE)));
        book.setDateStart(mapOfQuoteProperties.get(BookPropertiesEnum.BOOK_DATE_START));
        book.setDateEnd(mapOfQuoteProperties.get(BookPropertiesEnum.BOOK_DATE_END));
        book.setBookCategory(checkAndGetCurrentCategory(realm, mapOfQuoteProperties.get(BookPropertiesEnum.BOOK_CATEGORY)));
        book.setSection(checkAndGetSection(realm, mapOfQuoteProperties.get(BookPropertiesEnum.BOOK_TYPE)));
        book.setRating(checkAndGetBookRating(realm, mapOfQuoteProperties.get(BookPropertiesEnum.BOOK_RATING)));
        book.setYear(checkAndGetYear(realm, mapOfQuoteProperties.get(BookPropertiesEnum.BOOK_DATE_START)));
        return book;
    }

    private BookCategory checkAndGetCurrentCategory(Realm realm, String valueOfCategory) {
        RealmResults<BookCategory> bookCategories = realm.where(BookCategory.class)
                .equalTo("categoryName", valueOfCategory)
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

    private Section checkAndGetSection(Realm realm, String sectionValue) {
        RealmResults<Section> sections = realm.where(Section.class)
                .equalTo("sectionName", sectionValue)
                .findAll();
        Section section;
        if (sections == null || sections.isEmpty()) {
            section = createSection(realm, sectionValue);
        } else {
            section = sections.first();
            changeSectionCount(section);
        }
        return section;
    }

    private Section createSection(Realm realm, String sectionValue) {
        Section section = realm.createObject(Section.class);
        section.setId(getNextKey(section, realm));
        section.setSectionName(sectionValue);
        section.setSectionBookCount(1);
        return section;
    }

    private void changeSectionCount(Section section) {
        section.setSectionBookCount(
                section.getSectionBookCount() + 1);
    }

    private BookRating checkAndGetBookRating(Realm realm, String ratingValue) {
        RealmResults<BookRating> bookRatings = realm.where(BookRating.class)
                .equalTo("starsCount", Integer.valueOf(ratingValue))
                .findAll();
        BookRating bookRating;
        if (bookRatings == null || bookRatings.isEmpty()) {
            bookRating = createBookRating(realm, ratingValue);
        } else {
            bookRating = bookRatings.first();
            changeBookRatingCount(bookRating);
        }
        return bookRating;
    }

    private BookRating createBookRating(Realm realm, String bookRatingValue) {
        BookRating bookRating = realm.createObject(BookRating.class);
        bookRating.setId(getNextKey(bookRating, realm));
        bookRating.setStarsCount(Integer.valueOf(bookRatingValue));
        bookRating.setRatingBookCount(1);
        return bookRating;
    }

    private void changeBookRatingCount(BookRating bookRating) {
        bookRating.setRatingBookCount(
                bookRating.getRatingBookCount() + 1);
    }

    private Year checkAndGetYear(Realm realm, String date) {
        int currentYear = getYearNumber(date);
        RealmResults<Year> years = realm.where(Year.class)
                .equalTo("yearNumber", currentYear)
                .findAll();
        Year year;
        if (years == null || years.isEmpty()) {
            year = createYear(realm, currentYear);
        } else {
            year = years.first();
            changeYearCount(year);
        }
        return year;
    }

    private Year createYear(Realm realm, int yearValue) {
        Year year = realm.createObject(Year.class);
        year.setId(getNextKey(year, realm));
        year.setYearNumber(yearValue);
        year.setCurrentYearCount(1);
        return year;
    }

    private void changeYearCount(Year year) {
        year.setCurrentYearCount(
                year.getCurrentYearCount() + 1);
    }

    private int getYearNumber(String date) {
        String[] dayMonthYearArray = date.split("/");
        return Integer.valueOf(dayMonthYearArray[2].trim());
    }

    private void saveBookInTablesForChart(Realm realm, Book book) {
        ArrayList<Integer> bookMonthYearArray = createMonthYearArray(book.getDateStart(), book.getDateEnd());
        checkDivision(realm, bookMonthYearArray, book);
    }

    private ArrayList<Integer> createMonthYearArray(String dateStart, String dateEnd) {
        int[] dateStartArray = getCurrentBookMonthYearArray(dateStart);
        int[] dateEndArray = getCurrentBookMonthYearArray(dateEnd);
        ArrayList<Integer> monthArray = new ArrayList<>();
        for (int i = dateStartArray[0]; i <= dateEndArray[0]; i++) {
            monthArray.add(i);
        }
        return monthArray;
    }

    private int[] getCurrentBookMonthYearArray(String dateValue) {
        Date date = getDateFromString(dateValue);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int month = calendar.get(Calendar.MONTH) + 1;
        int year = calendar.get(Calendar.YEAR);
        return new int[]{month, year};
    }

    private Date getDateFromString(String dateValue) {
        Date date = null;
        try {
            DateFormat format = new SimpleDateFormat("dd/MM/yy", Locale.ENGLISH);
            date = format.parse(dateValue);
        } catch (ParseException p) {
            Log.v(AddBookFragment.class.getSimpleName(), p.toString());
        }
        return date;
    }

    private void checkDivision(Realm realm, List<Integer> array, Book book) {
        float indexOneMonth = isContainsList(array, Division.oneMonthDivisionArrays);
        if (indexOneMonth != -1) {
            saveBookInAllBookMonthDivision(realm, book, indexOneMonth, DivisionType.ONE);
            saveBookInMonthDivision(realm, book, indexOneMonth, DivisionType.ONE);
        }
        float indexThreeMonth = isContainsList(array, Division.threeMonthDivisionArrays);
        if (indexThreeMonth != -1) {
            saveBookInAllBookMonthDivision(realm, book, indexThreeMonth, DivisionType.THREE);
            saveBookInMonthDivision(realm, book, indexThreeMonth, DivisionType.THREE);
        }
        float indexSixMonth = isContainsList(array, Division.sixMonthDivisionArrays);
        if (indexSixMonth != -1) {
            saveBookInAllBookMonthDivision(realm, book, indexSixMonth, DivisionType.SIX);
            saveBookInMonthDivision(realm, book, indexSixMonth, DivisionType.SIX);
        }
        float indexTwelveMonth = isContainsList(array, Division.twelveMonthDivisionArrays);
        if (indexTwelveMonth != -1) {
            saveBookInAllBookMonthDivision(realm, book, indexTwelveMonth, DivisionType.TWELVE);
            saveBookInMonthDivision(realm, book, indexTwelveMonth, DivisionType.TWELVE);
        }
    }

    private float isContainsList(List<Integer> array, ArrayList<List<Integer>> monthDivision) {
        float index = -1;
        for (List<Integer> element : monthDivision) {
            if (element.containsAll(array)) {
                index = monthDivision.indexOf(element);
            }
        }
        return index;
    }

    private void saveBookInAllBookMonthDivision(Realm realm, final Book book, final float index, final String divisionType) {
        if (!isInitAllMonth) {
            initAllBookMonth(realm);
        }
        RealmResults<AllBookMonthDivision> allBookResults = getAllBookMonthDivisionByMonth(realm, index);
        AllBookMonthDivision currentMonth = allBookResults.first();
        currentMonth.setYear(book.getYear());
        setAllBookCount(currentMonth, divisionType);
    }

    private void initAllBookMonth(Realm realm) {
        RealmResults<AllBookMonthDivision> allBookResults = realm.where(AllBookMonthDivision.class).findAll();
        if (allBookResults.isEmpty()) {
            createAllBookMonthDivision(realm);
        }
        isInitAllMonth = true;
    }

    private RealmResults<AllBookMonthDivision> getAllBookMonthDivisionByMonth(Realm realm, float index) {
        return realm.where(AllBookMonthDivision.class).equalTo("month", index).findAll();
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

    private void createAllBookMonthDivision(Realm realm) {
        for (int i = 0; i <= MonthIndex.ELEVEN; i++) {
            AllBookMonthDivision allBookMonthDivision = realm.createObject(AllBookMonthDivision.class);
            allBookMonthDivision.setId(getNextKey(allBookMonthDivision, realm));
            allBookMonthDivision.setMonth(i);
        }
    }

    private void saveBookInMonthDivision(Realm realm, final Book book, final float index, final String monthDivisionType) {
        BookMonthDivision bookMonthDivision;
        RealmResults<BookMonthDivision> realmResults = getBookMonthDivisionByCategory(realm, book.getBookCategory().getCategoryName());
        if (realmResults.isEmpty()) {
            bookMonthDivision = createBookMonthDivision(realm, book);
        } else {
            bookMonthDivision = realmResults.first();
        }
        checkMonth((int) index, bookMonthDivision, monthDivisionType);
    }

    private RealmResults<BookMonthDivision> getBookMonthDivisionByCategory(Realm realm, String category) {
        return realm.where(BookMonthDivision.class).equalTo("category.categoryName", category).findAll();
    }

    private BookMonthDivision createBookMonthDivision(Realm realm, Book book) {
        BookMonthDivision bookMonthDivision = realm.createObject(BookMonthDivision.class);
        bookMonthDivision.setId(getNextKey(bookMonthDivision, realm));
        bookMonthDivision.setCategoryIndex(bookMonthDivision.getId());
        bookMonthDivision.setYear(book.getYear());
        bookMonthDivision.setCategory(book.getBookCategory());
        return bookMonthDivision;
    }

    private void checkMonth(int index, BookMonthDivision bookMonthDivision, String monthDivisionType) {
        switch (index) {
            case MonthIndex.ZERO:
                checkIndexCase0(bookMonthDivision, monthDivisionType);
                break;
            case MonthIndex.ONE:
                checkIndexCase1(bookMonthDivision, monthDivisionType);
                break;
            case MonthIndex.TWO:
                checkIndexCase2(bookMonthDivision, monthDivisionType);
                break;
            case MonthIndex.THREE:
                checkIndexCase3(bookMonthDivision, monthDivisionType);
                break;
            case MonthIndex.FOUR:
                bookMonthDivision.setMay(bookMonthDivision.getMay() + 1);
                break;
            case MonthIndex.FIVE:
                bookMonthDivision.setJune(bookMonthDivision.getJune() + 1);
                break;
            case MonthIndex.SIX:
                bookMonthDivision.setJuly(bookMonthDivision.getJuly() + 1);
                break;
            case MonthIndex.SEVEN:
                bookMonthDivision.setAugust(bookMonthDivision.getAugust() + 1);
                break;
            case MonthIndex.EIGHT:
                bookMonthDivision.setSeptember(bookMonthDivision.getSeptember() + 1);
                break;
            case MonthIndex.NINE:
                bookMonthDivision.setOctober(bookMonthDivision.getOctober() + 1);
                break;
            case MonthIndex.TEN:
                bookMonthDivision.setNovember(bookMonthDivision.getNovember() + 1);
                break;
            case MonthIndex.ELEVEN:
                bookMonthDivision.setDecember(bookMonthDivision.getDecember() + 1);
                break;
        }
    }

    private void checkIndexCase0(BookMonthDivision bookMonthDivision, String monthDivisionType) {
        switch (monthDivisionType) {
            case DivisionType.ONE:
                bookMonthDivision.setJanuary(bookMonthDivision.getJanuary() + 1);
                break;
            case DivisionType.THREE:
                bookMonthDivision.setJanuaryMarch(bookMonthDivision.getJanuaryMarch() + 1);
                break;
            case DivisionType.SIX:
                bookMonthDivision.setJanuaryJune(bookMonthDivision.getJanuaryJune() + 1);
                break;
            case DivisionType.TWELVE:
                bookMonthDivision.setJanuaryDecember(bookMonthDivision.getJanuaryDecember() + 1);
                break;
        }
    }

    private void checkIndexCase1(BookMonthDivision bookMonthDivision, String monthDivisionType) {
        switch (monthDivisionType) {
            case DivisionType.ONE:
                bookMonthDivision.setFebruary(bookMonthDivision.getFebruary() + 1);
                break;
            case DivisionType.THREE:
                bookMonthDivision.setAprilJune(bookMonthDivision.getAprilJune() + 1);
                break;
            case DivisionType.SIX:
                bookMonthDivision.setJulyDecember(bookMonthDivision.getJulyDecember() + 1);
                break;
        }
    }

    private void checkIndexCase2(BookMonthDivision bookMonthDivision, String monthDivisionType) {
        switch (monthDivisionType) {
            case DivisionType.ONE:
                bookMonthDivision.setMarch(bookMonthDivision.getMarch() + 1);
                break;
            case DivisionType.THREE:
                bookMonthDivision.setJulySeptember(bookMonthDivision.getJulySeptember() + 1);
                break;
        }
    }

    private void checkIndexCase3(BookMonthDivision bookMonthDivision, String monthDivisionType) {
        switch (monthDivisionType) {
            case DivisionType.ONE:
                bookMonthDivision.setApril(bookMonthDivision.getApril() + 1);
                break;
            case DivisionType.THREE:
                bookMonthDivision.setOctoberDecember(bookMonthDivision.getOctoberDecember() + 1);
                break;
        }
    }

    /**
     * Method is used for increment "id".
     *
     * @param currentClass current class
     * @param realm        current db
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
