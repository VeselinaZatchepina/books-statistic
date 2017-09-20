package com.github.veselinazatchepina.bookstatistics.chart.valueformatters;

import android.util.Log;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.veselinazatchepina.bookstatistics.database.model.BookCategory;
import com.github.veselinazatchepina.bookstatistics.database.model.BookMonthDivision;

import io.realm.RealmResults;

public class XAxisBarChartValueFormatter implements IAxisValueFormatter {

    private RealmResults<BookMonthDivision> realmResults;

    public XAxisBarChartValueFormatter(RealmResults<BookMonthDivision> realmResults) {
        this.realmResults = realmResults;
    }

    public int getRealmResultsSize() {
        return realmResults.size();
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        Log.v("valueeeeee", String.valueOf((int) value));
        int valueInt = ((int) value) - 1;
        if (realmResults.size() > valueInt && valueInt >= 0) {
            BookCategory bookCategory = realmResults.get(valueInt).getCategory();
            return bookCategory == null ? "No data" : bookCategory.getCategoryName();
        } else {
            return "";
        }
    }
}
