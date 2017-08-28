package com.github.veselinazatchepina.bookstatistics.database;


import android.util.Log;

import com.github.veselinazatchepina.bookstatistics.books.enums.BookPropertiesEnum;
import com.github.veselinazatchepina.bookstatistics.books.enums.BookTypeEnums;
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

import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;

public class BooksRealmRepository implements RealmRepository {

    private final Realm mRealm;
    private Boolean isInitAllMonth = false;
    private int minusIndex;

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
                String currentStartDateValue = mapOfQuoteProperties.get(BookPropertiesEnum.BOOK_DATE_START);
                String currentEndDateValue = mapOfQuoteProperties.get(BookPropertiesEnum.BOOK_DATE_END);
                if (isNotNullAndEmpty(currentStartDateValue) && isNotNullAndEmpty(currentEndDateValue)
                        && mapOfQuoteProperties.get(BookPropertiesEnum.BOOK_TYPE).equals(BookTypeEnums.READ_BOOK)) {
                    saveBookInTablesForChart(realm, currentBook);
                }
            }
        });
    }

    private boolean isNotNullAndEmpty(String valueForCheck) {
        return valueForCheck != null && !valueForCheck.equals("");
    }

    private Book saveBook(Realm realm, HashMap<BookPropertiesEnum, String> mapOfBookProperties) {
        Book book = realm.createObject(Book.class);
        book.setId(getNextKey(book, realm));
        saveOrUpdateBookFields(book, mapOfBookProperties);
        saveBookObjectFields(realm, book, mapOfBookProperties);
        return book;
    }

    private void saveOrUpdateBookFields(Book book, HashMap<BookPropertiesEnum, String> mapOfBookProperties) {
        String currentBookName = mapOfBookProperties.get(BookPropertiesEnum.BOOK_NAME);
        if (isNotNullAndEmpty(currentBookName)) {
            book.setBookName(currentBookName);
        }
        String currentBookAuthor = mapOfBookProperties.get(BookPropertiesEnum.BOOK_AUTHOR);
        if (isNotNullAndEmpty(currentBookAuthor)) {
            book.setAuthorName(currentBookAuthor);
        }
        String currentPage = mapOfBookProperties.get(BookPropertiesEnum.BOOK_PAGE);
        if (isNotNullAndEmpty(currentPage)) {
            book.setPageCount(Integer.valueOf(mapOfBookProperties.get(BookPropertiesEnum.BOOK_PAGE)));
        }
        book.setDateStart(mapOfBookProperties.get(BookPropertiesEnum.BOOK_DATE_START));
        book.setDateEnd(mapOfBookProperties.get(BookPropertiesEnum.BOOK_DATE_END));
    }

    private void saveBookObjectFields(Realm realm, Book book, HashMap<BookPropertiesEnum, String> mapOfBookProperties) {
        book.setBookCategory(checkAndGetCurrentCategory(realm, mapOfBookProperties.get(BookPropertiesEnum.BOOK_CATEGORY)));
        book.setSection(checkAndGetSection(realm, mapOfBookProperties.get(BookPropertiesEnum.BOOK_TYPE)));
        book.setRating(checkAndGetBookRating(realm, mapOfBookProperties.get(BookPropertiesEnum.BOOK_RATING)));
        String currentStartDateValue = mapOfBookProperties.get(BookPropertiesEnum.BOOK_DATE_START);
        if (isNotNullAndEmpty(currentStartDateValue)) {
            book.setYear(checkAndGetYear(realm, currentStartDateValue));
        }
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

    private void setAllBookCountMinus(AllBookMonthDivision currentMonth, String divisionType) {
        switch (divisionType) {
            case DivisionType.ONE:
                currentMonth.setAllBookCountOneMonth(currentMonth.getAllBookCountOneMonth() - 1);
                break;
            case DivisionType.THREE:
                currentMonth.setAllBookCountThreeMonth(currentMonth.getAllBookCountThreeMonth() - 1);
                break;
            case DivisionType.SIX:
                currentMonth.setAllBookCountSixMonth(currentMonth.getAllBookCountSixMonth() - 1);
                break;
            case DivisionType.TWELVE:
                currentMonth.setAllBookCountTwelveMonth(currentMonth.getAllBookCountTwelveMonth() - 1);
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
                if (minusIndex == 1) {
                    bookMonthDivision.setMay(bookMonthDivision.getMay() - 1);
                } else {
                    bookMonthDivision.setMay(bookMonthDivision.getMay() + 1);
                }
                break;
            case MonthIndex.FIVE:
                if (minusIndex == 1) {
                    bookMonthDivision.setJune(bookMonthDivision.getJune() - 1);
                } else {
                    bookMonthDivision.setJune(bookMonthDivision.getJune() + 1);
                }
                break;
            case MonthIndex.SIX:
                if (minusIndex == 1) {
                    bookMonthDivision.setJuly(bookMonthDivision.getJuly() - 1);
                } else {
                    bookMonthDivision.setJuly(bookMonthDivision.getJuly() + 1);
                }
                break;
            case MonthIndex.SEVEN:
                if (minusIndex == 1) {
                    bookMonthDivision.setAugust(bookMonthDivision.getAugust() - 1);
                } else {
                    bookMonthDivision.setAugust(bookMonthDivision.getAugust() + 1);
                }
                break;
            case MonthIndex.EIGHT:
                if (minusIndex == 1) {
                    bookMonthDivision.setSeptember(bookMonthDivision.getSeptember() - 1);
                } else {
                    bookMonthDivision.setSeptember(bookMonthDivision.getSeptember() + 1);
                }
                break;
            case MonthIndex.NINE:
                if (minusIndex == 1) {
                    bookMonthDivision.setOctober(bookMonthDivision.getOctober() - 1);
                } else {
                    bookMonthDivision.setOctober(bookMonthDivision.getOctober() + 1);
                }
                break;
            case MonthIndex.TEN:
                if (minusIndex == 1) {
                    bookMonthDivision.setNovember(bookMonthDivision.getNovember() - 1);
                } else {
                    bookMonthDivision.setNovember(bookMonthDivision.getNovember() + 1);
                }
                break;
            case MonthIndex.ELEVEN:
                if (minusIndex == 1) {
                    bookMonthDivision.setDecember(bookMonthDivision.getDecember() - 1);
                } else {
                    bookMonthDivision.setDecember(bookMonthDivision.getDecember() + 1);
                }
                break;
        }
    }

    private void checkIndexCase0(BookMonthDivision bookMonthDivision, String monthDivisionType) {
        switch (monthDivisionType) {
            case DivisionType.ONE:
                if (minusIndex == 1) {
                    bookMonthDivision.setJanuary(bookMonthDivision.getJanuary() - 1);
                } else {
                    bookMonthDivision.setJanuary(bookMonthDivision.getJanuary() + 1);
                }
                break;
            case DivisionType.THREE:
                if (minusIndex == 1) {
                    bookMonthDivision.setJanuaryMarch(bookMonthDivision.getJanuaryMarch() - 1);
                } else {
                    bookMonthDivision.setJanuaryMarch(bookMonthDivision.getJanuaryMarch() + 1);
                }
                break;
            case DivisionType.SIX:
                if (minusIndex == 1) {
                    bookMonthDivision.setJanuaryJune(bookMonthDivision.getJanuaryJune() - 1);
                } else {
                    bookMonthDivision.setJanuaryJune(bookMonthDivision.getJanuaryJune() + 1);
                }
                break;
            case DivisionType.TWELVE:
                if (minusIndex == 1) {
                    bookMonthDivision.setJanuaryDecember(bookMonthDivision.getJanuaryDecember() - 1);
                } else {
                    bookMonthDivision.setJanuaryDecember(bookMonthDivision.getJanuaryDecember() + 1);
                }
                break;
        }
    }

    private void checkIndexCase1(BookMonthDivision bookMonthDivision, String monthDivisionType) {
        switch (monthDivisionType) {
            case DivisionType.ONE:
                if (minusIndex == 1) {
                    bookMonthDivision.setFebruary(bookMonthDivision.getFebruary() - 1);
                } else {
                    bookMonthDivision.setFebruary(bookMonthDivision.getFebruary() + 1);
                }
                break;
            case DivisionType.THREE:
                if (minusIndex == 1) {
                    bookMonthDivision.setAprilJune(bookMonthDivision.getAprilJune() - 1);
                } else {
                    bookMonthDivision.setAprilJune(bookMonthDivision.getAprilJune() + 1);
                }
                break;
            case DivisionType.SIX:
                if (minusIndex == 1) {
                    bookMonthDivision.setJulyDecember(bookMonthDivision.getJulyDecember() - 1);
                } else {
                    bookMonthDivision.setJulyDecember(bookMonthDivision.getJulyDecember() + 1);
                }
                break;
        }
    }

    private void checkIndexCase2(BookMonthDivision bookMonthDivision, String monthDivisionType) {
        switch (monthDivisionType) {
            case DivisionType.ONE:
                if (minusIndex == 1) {
                    bookMonthDivision.setMarch(bookMonthDivision.getMarch() - 1);
                } else {
                    bookMonthDivision.setMarch(bookMonthDivision.getMarch() + 1);
                }
                break;
            case DivisionType.THREE:
                if (minusIndex == 1) {
                    bookMonthDivision.setJulySeptember(bookMonthDivision.getJulySeptember() - 1);
                } else {
                    bookMonthDivision.setJulySeptember(bookMonthDivision.getJulySeptember() + 1);
                }
                break;
        }
    }

    private void checkIndexCase3(BookMonthDivision bookMonthDivision, String monthDivisionType) {
        switch (monthDivisionType) {
            case DivisionType.ONE:
                if (minusIndex == 1) {
                    bookMonthDivision.setApril(bookMonthDivision.getApril() - 1);
                } else {
                    bookMonthDivision.setApril(bookMonthDivision.getApril() + 1);
                }
                break;
            case DivisionType.THREE:
                if (minusIndex == 1) {
                    bookMonthDivision.setOctoberDecember(bookMonthDivision.getOctoberDecember() - 1);
                } else {
                    bookMonthDivision.setOctoberDecember(bookMonthDivision.getOctoberDecember() + 1);
                }
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
    public RealmResults<Book> getAllBooksInCurrentSectionByCategory(String sectionType, String currentCategory) {
        return mRealm.where(Book.class).equalTo("section.sectionName", sectionType).equalTo("bookCategory.categoryName", currentCategory).findAllAsync();
    }

    @Override
    public RealmResults<Book> getAllBooksInCurrentSection(String sectionType) {
        return mRealm.where(Book.class).equalTo("section.sectionName", sectionType).findAllAsync();
    }

    @Override
    public RealmResults<Book> getBookById(long currentBookId) {
        return mRealm.where(Book.class).equalTo("id", currentBookId).findAllAsync();
    }

    @Override
    public RealmResults<Book> getBookBySectionAndBookName(String sectionType, String bookName) {
        return mRealm.where(Book.class)
                .contains("bookName", bookName, Case.INSENSITIVE)
                .equalTo("section.sectionName", sectionType)
                .findAll();
    }

    @Override
    public RealmResults<Book> getBookBySectionAndBookNameByCategory(String sectionType, String bookName, String category) {
        return mRealm.where(Book.class)
                .contains("bookName", bookName, Case.INSENSITIVE)
                .equalTo("section.sectionName", sectionType)
                .equalTo("bookCategory.categoryName", category)
                .findAll();
    }

    @Override
    public void saveChangedBook(final long currentBookId, final HashMap<BookPropertiesEnum, String> mapOfBookProperties) {
        mRealm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<Book> books = realm.where(Book.class).equalTo("id", currentBookId).findAll();
                Book currentBook = books.first();
                String oldDateStart = currentBook.getDateStart();
                String oldDateEnd = currentBook.getDateEnd();
                String oldCategory = currentBook.getBookCategory().getCategoryName();
                saveOrUpdateBookFields(currentBook, mapOfBookProperties);
                updateBookCategory(realm, currentBook, mapOfBookProperties);
                updateBookSection(realm, currentBook, mapOfBookProperties);
                updateBookRating(realm, currentBook, mapOfBookProperties);
                String newStartDateValue = mapOfBookProperties.get(BookPropertiesEnum.BOOK_DATE_START);
                if (isNotNullAndEmpty(newStartDateValue)) {
                    updateBookYear(realm, currentBook, newStartDateValue);
                }
                if (!currentBook.getDateStart().equals("") && !currentBook.getDateEnd().equals("")
                        && currentBook.getSection().getSectionName().equals(BookTypeEnums.READ_BOOK)) {
                    saveBookInTablesForChart(realm, currentBook);
                }
                if (!oldDateStart.equals("") && !oldDateEnd.equals("")) {
                    ArrayList<Integer> array = createMonthYearArray(oldDateStart, oldDateEnd);
                    float indexMonth = isContainsList(array, Division.oneMonthDivisionArrays);
                    String divisionType = DivisionType.ONE;
                    if (indexMonth != -1) {
                        RealmResults<AllBookMonthDivision> allBookResults = getAllBookMonthDivisionByMonth(realm, indexMonth);
                        AllBookMonthDivision currentMonth = allBookResults.first();
                        setAllBookCountMinus(currentMonth, divisionType);

                        BookMonthDivision bookMonthDivision;
                        RealmResults<BookMonthDivision> realmResults = getBookMonthDivisionByCategory(realm, oldCategory);
                        bookMonthDivision = realmResults.first();
                        minusIndex = 1;
                        checkMonth((int) indexMonth, bookMonthDivision, divisionType);
                    }
                    indexMonth = isContainsList(array, Division.threeMonthDivisionArrays);
                    divisionType = DivisionType.THREE;
                    if (indexMonth != -1) {
                        RealmResults<AllBookMonthDivision> allBookResults = getAllBookMonthDivisionByMonth(realm, indexMonth);
                        AllBookMonthDivision currentMonth = allBookResults.first();
                        setAllBookCountMinus(currentMonth, divisionType);

                        BookMonthDivision bookMonthDivision;
                        RealmResults<BookMonthDivision> realmResults = getBookMonthDivisionByCategory(realm, oldCategory);
                        bookMonthDivision = realmResults.first();
                        minusIndex = 1;
                        checkMonth((int) indexMonth, bookMonthDivision, divisionType);
                    }
                    indexMonth = isContainsList(array, Division.sixMonthDivisionArrays);
                    divisionType = DivisionType.SIX;
                    if (indexMonth != -1) {
                        RealmResults<AllBookMonthDivision> allBookResults = getAllBookMonthDivisionByMonth(realm, indexMonth);
                        AllBookMonthDivision currentMonth = allBookResults.first();
                        setAllBookCountMinus(currentMonth, divisionType);

                        BookMonthDivision bookMonthDivision;
                        RealmResults<BookMonthDivision> realmResults = getBookMonthDivisionByCategory(realm, oldCategory);
                        bookMonthDivision = realmResults.first();
                        minusIndex = 1;
                        checkMonth((int) indexMonth, bookMonthDivision, divisionType);
                    }
                    indexMonth = isContainsList(array, Division.twelveMonthDivisionArrays);
                    divisionType = DivisionType.TWELVE;
                    if (indexMonth != -1) {
                        RealmResults<AllBookMonthDivision> allBookResults = getAllBookMonthDivisionByMonth(realm, indexMonth);
                        AllBookMonthDivision currentMonth = allBookResults.first();
                        setAllBookCountMinus(currentMonth, divisionType);

                        BookMonthDivision bookMonthDivision;
                        RealmResults<BookMonthDivision> realmResults = getBookMonthDivisionByCategory(realm, oldCategory);
                        bookMonthDivision = realmResults.first();
                        minusIndex = 1;
                        checkMonth((int) indexMonth, bookMonthDivision, divisionType);
                    }
                }
            }
        });
    }

    private void updateBookCategory(Realm realm, Book book, HashMap<BookPropertiesEnum, String> mapOfBookProperties) {
        String valueOfCategory = mapOfBookProperties.get(BookPropertiesEnum.BOOK_CATEGORY).toLowerCase();
        if (!valueOfCategory.equals(book.getBookCategory().getCategoryName())) {
            BookCategory newBookCategory = checkAndGetCurrentCategory(realm, valueOfCategory);
            updateBookCountLastCategory(book);
            book.setBookCategory(newBookCategory);
        }
    }

    private void updateBookCountLastCategory(Book book) {
        BookCategory bookCategory = book.getBookCategory();
        bookCategory.setCategoryBookCount(bookCategory.getCategoryBookCount() - 1);
        if (bookCategory.getCategoryBookCount() == 0) {
            bookCategory.deleteFromRealm();
        }
    }

    private void updateBookSection(Realm realm, Book book, HashMap<BookPropertiesEnum, String> mapOfBookProperties) {
        String valueOfSection = mapOfBookProperties.get(BookPropertiesEnum.BOOK_TYPE);
        if (!valueOfSection.equals(book.getSection().getSectionName())) {
            Section newSection = checkAndGetSection(realm, valueOfSection);
            updateBookCountLastSection(book);
            book.setSection(newSection);
        }
    }

    @Override
    public void updateBookSectionInTransaction(final long currentBookIdForEdit, final HashMap<BookPropertiesEnum, String> mapOfBookProperties) {
        mRealm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Book book = realm.where(Book.class).equalTo("id", currentBookIdForEdit).findAll().first();
                updateBookSection(realm, book, mapOfBookProperties);
            }
        });
    }

    private void updateBookCountLastSection(Book book) {
        Section section = book.getSection();
        section.setSectionBookCount(section.getSectionBookCount() - 1);
        if (section.getSectionBookCount() == 0) {
            section.deleteFromRealm();
        }
    }

    private void updateBookRating(Realm realm, Book book, HashMap<BookPropertiesEnum, String> mapOfBookProperties) {
        int valueOfRating = Integer.valueOf(mapOfBookProperties.get(BookPropertiesEnum.BOOK_RATING));
        if (valueOfRating != book.getRating().getStarsCount()) {
            BookRating newBookRating = checkAndGetBookRating(realm, String.valueOf(valueOfRating));
            updateBookCountLastRating(book);
            book.setRating(newBookRating);
        }
    }

    @Override
    public void updateBookRatingInTransaction(final long currentBookIdForEdit, final HashMap<BookPropertiesEnum, String> mapOfBookProperties) {
        mRealm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Book book = realm.where(Book.class).equalTo("id", currentBookIdForEdit).findAll().first();
                updateBookRating(realm, book, mapOfBookProperties);
            }
        });
    }

    private void updateBookCountLastRating(Book book) {
        BookRating bookRating = book.getRating();
        bookRating.setRatingBookCount(bookRating.getRatingBookCount() - 1);
        if (bookRating.getRatingBookCount() == 0) {
            bookRating.deleteFromRealm();
        }
    }

    private void updateBookYear(Realm realm, Book book, String startDate) {
        int valueOfYear = getYearNumber(startDate);
        if (book.getYear() == null || valueOfYear != book.getYear().getYearNumber()) {
            Year newYear = checkAndGetYear(realm, startDate);
            if (book.getYear() != null) {
                updateBookCountLastYear(book);
            }
            book.setYear(newYear);
        }
    }

    private void updateBookCountLastYear(Book book) {
        Year year = book.getYear();
        year.setCurrentYearCount(year.getCurrentYearCount() - 1);
        if (year.getCurrentYearCount() == 0) {
            year.deleteFromRealm();
        }
    }

    @Override
    public void deleteAllBooksWithCurrentCategory(final String categoryForDelete) {
        mRealm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<Book> books = realm.where(Book.class)
                        .equalTo("bookCategory.categoryName", categoryForDelete)
                        .findAll();
                for (Book book : books) {
                    deleteBookFromDb(book);
                }
            }
        });
    }

    private void deleteBookFromDb(Book book) {
        updateBookCountLastCategory(book);
        updateBookCountLastRating(book);
        updateBookCountLastSection(book);
        if (book.getYear() != null) {
            updateBookCountLastYear(book);
        }
        book.deleteFromRealm();
    }

    @Override
    public void deleteBookById(final long bookIdForDelete) {
        mRealm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<Book> books = realm.where(Book.class)
                        .equalTo("id", bookIdForDelete)
                        .findAll();
                Book book = books.first();
                deleteBookFromDb(book);
            }
        });
    }

    @Override
    public RealmResults<AllBookMonthDivision> getAllBookMonth(int begin, int end) {
        return mRealm.where(AllBookMonthDivision.class).between("id", begin, end).findAllAsync();
    }

    @Override
    public RealmResults<BookMonthDivision> getBookMonthDivision() {
        return mRealm.where(BookMonthDivision.class).findAllAsync();
    }

    @Override
    public RealmResults<BookMonthDivision> getBookMonthDivisionByCategory(String category) {
        return mRealm.where(BookMonthDivision.class).equalTo("category.categoryName", category).findAllAsync();
    }

    @Override
    public RealmResults<Year> getAllYears() {
        return mRealm.where(Year.class).findAll();
    }

    @Override
    public void closeDbConnect() {
        mRealm.close();
    }
}
