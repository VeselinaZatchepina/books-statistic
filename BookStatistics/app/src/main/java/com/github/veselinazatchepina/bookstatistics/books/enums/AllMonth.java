package com.github.veselinazatchepina.bookstatistics.books.enums;

import java.util.ArrayList;
import java.util.List;

public final class AllMonth {
    public static final String JANUARY = "january";
    public static final String FEBRUARY = "february";
    public static final String MARCH = "march";
    public static final String APRIL = "april";
    public static final String MAY = "may";
    public static final String JUNE = "june";
    public static final String JULY = "july";
    public static final String AUGUST = "august";
    public static final String SEPTEMBER = "september";
    public static final String OCTOBER = "october";
    public static final String NOVEMBER = "november";
    public static final String DECEMBER = "december";

    public static final String JANUARY_MARCH = "januaryMarch";
    public static final String APRIL_JUNE = "aprilJune";
    public static final String JULY_SEPTEMBER = "julySeptember";
    public static final String OCTOBER_DECEMBER = "octoberDecember";

    public static final String JANUARY_JUNE = "januaryJune";
    public static final String JULY_DECEMBER = "julyDecember";

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

    private AllMonth() { }
}
