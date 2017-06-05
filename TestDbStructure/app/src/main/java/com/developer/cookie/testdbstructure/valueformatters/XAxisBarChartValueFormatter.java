package com.developer.cookie.testdbstructure.valueformatters;

import com.developer.cookie.testdbstructure.database.model.BookMonthDivision;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import io.realm.RealmResults;

public class XAxisBarChartValueFormatter implements IAxisValueFormatter {

    private RealmResults<BookMonthDivision> realmResults;

    public XAxisBarChartValueFormatter(RealmResults<BookMonthDivision> realmResults) {
        this.realmResults = realmResults;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        return realmResults.get((int) value - 1).getCategory().getCategoryName();
    }
}
