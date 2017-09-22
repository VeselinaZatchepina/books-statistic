package com.github.veselinazatchepina.bookstatistics.chart.valueformatters;

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

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        int valueInt = ((int) value) - 1;
        if (realmResults.size() > valueInt && valueInt >= 0) {
            String shortCategoryName = "No";
            BookCategory bookCategory = realmResults.get(valueInt).getCategory();
            if (bookCategory != null) {
                String longCategoryName = bookCategory.getCategoryName();
                if (longCategoryName.length() > 3) {
                    shortCategoryName = longCategoryName.substring(0, 3);
                } else {
                    shortCategoryName = longCategoryName;
                }
            }
            return shortCategoryName;
        } else {
            return "";
        }
    }
}
