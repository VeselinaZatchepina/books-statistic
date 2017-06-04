package com.developer.cookie.testdbstructure.valueformatters;

import com.developer.cookie.testdbstructure.database.model.OneMonthDivision;
import com.developer.cookie.testdbstructure.database.model.SixMonthDivision;
import com.developer.cookie.testdbstructure.database.model.ThreeMonthDivision;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import io.realm.RealmObject;

public class XAxisLineChartValueFormatter implements IAxisValueFormatter {

    RealmObject realmObject;
    String currentDivision = "no value";

    public XAxisLineChartValueFormatter(RealmObject realmObject) {
        this.realmObject = realmObject;
    }

    public XAxisLineChartValueFormatter(String currentDivision) {
        this.currentDivision = currentDivision;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        String currentMonth;
        if (currentDivision.equals("one") || realmObject instanceof OneMonthDivision) {
            currentMonth = checkLabelOneMonth(value);
        } else if (currentDivision.equals("three") || realmObject instanceof ThreeMonthDivision) {
            currentMonth = checkLabelThreeMonth(value);
        } else if (currentDivision.equals("six")  || realmObject instanceof SixMonthDivision) {
            currentMonth = checkLabelSixMonth(value);
        } else {
            currentMonth = checkLabelTwelveMonth(value);
        }
        return currentMonth;
    }

    private String checkLabelOneMonth(float value) {
        String currentMonth = String.valueOf((int) value);
        switch ((int) value) {
            case 0:
                currentMonth = "Jan";
                break;
            case 1:
                currentMonth = "Feb";
                break;
            case 2:
                currentMonth = "Mar";
                break;
            case 3:
                currentMonth = "Apr";
                break;
            case 4:
                currentMonth = "May";
                break;
            case 5:
                currentMonth = "June";
                break;
            case 6:
                currentMonth = "July";
                break;
            case 7:
                currentMonth = "Aug";
                break;
            case 8:
                currentMonth = "Sept";
                break;
            case 9:
                currentMonth = "Oct";
                break;
            case 10:
                currentMonth = "Nov";
                break;
            case 11:
                currentMonth = "Dec";
                break;
        }
        return currentMonth;
    }

    private String checkLabelThreeMonth(float value) {
        String currentMonth = String.valueOf((int) value);
        switch ((int) value) {
            case 0:
                currentMonth = "Jan-Mar";
                break;
            case 1:
                currentMonth = "Apr-June";
                break;
            case 2:
                currentMonth = "July-Sept";
                break;
            case 3:
                currentMonth = "Oct-Dec";
                break;
        }
        return currentMonth;
    }

    private String checkLabelSixMonth(float value) {
        String currentMonth = String.valueOf((int) value);
        switch ((int) value) {
            case 0:
                currentMonth = "Jan-June";
                break;
            case 1:
                currentMonth = "July-Dec";
                break;
        }
        return currentMonth;
    }

    private String checkLabelTwelveMonth(float value) {
        String currentMonth = String.valueOf((int) value);
        switch ((int) value) {
            case 0:
                currentMonth = "Jan-Dec";
                break;
        }
        return currentMonth;
    }
}
