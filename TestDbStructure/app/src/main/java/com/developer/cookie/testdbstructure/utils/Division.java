package com.developer.cookie.testdbstructure.utils;

import java.util.ArrayList;

public class Division {
    public static ArrayList<int[]> oneMonthDivisionArrays;
    public static ArrayList<int[]> threeMonthDivisionArrays;
    public static ArrayList<int[]> sixMonthDivisionArrays;
    public static ArrayList<int[]> twelveMonthDivisionArrays;

    static {
        for (int i = 1; i < 13; i++) {
            oneMonthDivisionArrays.add(new int[]{i});
        }
        threeMonthDivisionArrays.add(new int[]{1, 2, 3});
        threeMonthDivisionArrays.add(new int[]{4, 5, 6});
        threeMonthDivisionArrays.add(new int[]{7, 8, 9});
        threeMonthDivisionArrays.add(new int[]{10, 11, 12});

        sixMonthDivisionArrays.add(new int[]{1, 2, 3, 4, 5, 6});
        sixMonthDivisionArrays.add(new int[]{7, 8, 9, 10, 11, 12});

        twelveMonthDivisionArrays.add(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12});
    }
}
