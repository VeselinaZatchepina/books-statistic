package com.github.veselinazatchepina.bookstatistics.books.enums;

import java.util.ArrayList;
import java.util.List;

/**
 * Division type (month) for barchart.
 */
public final class AllMonth {
    private static final String JANUARY = "january";
    private static final String FEBRUARY = "february";
    private static final String MARCH = "march";
    private static final String APRIL = "april";
    private static final String MAY = "may";
    private static final String JUNE = "june";
    private static final String JULY = "july";
    private static final String AUGUST = "august";
    private static final String SEPTEMBER = "september";
    private static final String OCTOBER = "october";
    private static final String NOVEMBER = "november";
    private static final String DECEMBER = "december";

    private static final String JANUARY_MARCH = "januaryMarch";
    private static final String APRIL_JUNE = "aprilJune";
    private static final String JULY_SEPTEMBER = "julySeptember";
    private static final String OCTOBER_DECEMBER = "octoberDecember";

    private static final String JANUARY_JUNE = "januaryJune";
    private static final String JULY_DECEMBER = "julyDecember";

    public static final String JANUARY_DECEMBER = "januaryDecember";

    public static List<String> listOfAllMonth = new ArrayList<>();

    static {
        listOfAllMonth.add(JANUARY);
        listOfAllMonth.add(FEBRUARY);
        listOfAllMonth.add(MARCH);
        listOfAllMonth.add(APRIL);
        listOfAllMonth.add(MAY);
        listOfAllMonth.add(JUNE);
        listOfAllMonth.add(JULY);
        listOfAllMonth.add(AUGUST);
        listOfAllMonth.add(SEPTEMBER);
        listOfAllMonth.add(OCTOBER);
        listOfAllMonth.add(NOVEMBER);
        listOfAllMonth.add(DECEMBER);
        listOfAllMonth.add(JANUARY_MARCH);
        listOfAllMonth.add(APRIL_JUNE);
        listOfAllMonth.add(JULY_SEPTEMBER);
        listOfAllMonth.add(OCTOBER_DECEMBER);
        listOfAllMonth.add(JANUARY_JUNE);
        listOfAllMonth.add(JULY_DECEMBER);
        listOfAllMonth.add(JANUARY_DECEMBER);
    }

    private AllMonth() {
    }
}
