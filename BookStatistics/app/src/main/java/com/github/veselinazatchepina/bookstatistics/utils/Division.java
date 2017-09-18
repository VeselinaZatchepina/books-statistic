package com.github.veselinazatchepina.bookstatistics.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Division {
    public static ArrayList<List<Integer>> oneMonthDivisionArrays = new ArrayList<>();
    public static ArrayList<List<Integer>> threeMonthDivisionArrays = new ArrayList<>();
    public static ArrayList<List<Integer>> sixMonthDivisionArrays = new ArrayList<>();
    public static ArrayList<List<Integer>> twelveMonthDivisionArrays = new ArrayList<>();

    static {
        for (int i = 1; i < 13; i++) {
            oneMonthDivisionArrays.add(Arrays.asList(i));
        }
        threeMonthDivisionArrays.add(Arrays.asList(1, 2, 3));
        threeMonthDivisionArrays.add(Arrays.asList(4, 5, 6));
        threeMonthDivisionArrays.add(Arrays.asList(7, 8, 9));
        threeMonthDivisionArrays.add(Arrays.asList(10, 11, 12));

        sixMonthDivisionArrays.add(Arrays.asList(1, 2, 3, 4, 5, 6));
        sixMonthDivisionArrays.add(Arrays.asList(7, 8, 9, 10, 11, 12));

        twelveMonthDivisionArrays.add(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12));
    }
}
