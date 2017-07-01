package com.github.veselinazatchepina.bookstatistics.chart.valueformatters;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.veselinazatchepina.bookstatistics.books.enums.DivisionType;
import com.github.veselinazatchepina.bookstatistics.books.enums.MonthIndex;

public class XAxisLineChartValueFormatter implements IAxisValueFormatter {

    private String currentDivision;

    public XAxisLineChartValueFormatter(String currentDivision) {
        this.currentDivision = currentDivision;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        String currentMonth = String.valueOf((int) value);
        switch (currentDivision) {
            case DivisionType.ONE :
                currentMonth = checkLabelOneMonth(value);
                break;
            case DivisionType.THREE :
                currentMonth = checkLabelThreeMonth(value);
                break;
            case DivisionType.SIX :
                currentMonth = checkLabelSixMonth(value);
                break;
            case DivisionType.TWELVE :
                currentMonth = checkLabelTwelveMonth(value);
                break;
        }
        return currentMonth;
    }

    private String checkLabelOneMonth(float value) {
        String currentMonth = String.valueOf((int) value);
        switch ((int) value) {
            case MonthIndex.ZERO :
                currentMonth = "Jan";
                break;
            case MonthIndex.ONE :
                currentMonth = "Feb";
                break;
            case MonthIndex.TWO :
                currentMonth = "Mar";
                break;
            case MonthIndex.THREE :
                currentMonth = "Apr";
                break;
            case MonthIndex.FOUR :
                currentMonth = "May";
                break;
            case MonthIndex.FIVE :
                currentMonth = "June";
                break;
            case MonthIndex.SIX :
                currentMonth = "July";
                break;
            case MonthIndex.SEVEN :
                currentMonth = "Aug";
                break;
            case MonthIndex.EIGHT :
                currentMonth = "Sept";
                break;
            case MonthIndex.NINE :
                currentMonth = "Oct";
                break;
            case MonthIndex.TEN :
                currentMonth = "Nov";
                break;
            case MonthIndex.ELEVEN :
                currentMonth = "Dec";
                break;
        }
        return currentMonth;
    }

    private String checkLabelThreeMonth(float value) {
        String currentMonth = String.valueOf((int) value);
        switch ((int) value) {
            case MonthIndex.ZERO :
                currentMonth = "Jan-Mar";
                break;
            case MonthIndex.ONE:
                currentMonth = "Apr-June";
                break;
            case MonthIndex.TWO :
                currentMonth = "July-Sept";
                break;
            case MonthIndex.THREE :
                currentMonth = "Oct-Dec";
                break;
        }
        return currentMonth;
    }

    private String checkLabelSixMonth(float value) {
        String currentMonth = String.valueOf((int) value);
        switch ((int) value) {
            case MonthIndex.ZERO :
                currentMonth = "Jan-June";
                break;
            case MonthIndex.ONE :
                currentMonth = "July-Dec";
                break;
        }
        return currentMonth;
    }

    private String checkLabelTwelveMonth(float value) {
        String currentMonth = String.valueOf((int) value);
        switch ((int) value) {
            case MonthIndex.ZERO :
                currentMonth = "Jan-Dec";
                break;
        }
        return currentMonth;
    }
}
