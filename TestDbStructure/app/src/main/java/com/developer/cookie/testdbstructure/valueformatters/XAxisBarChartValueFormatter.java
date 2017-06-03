package com.developer.cookie.testdbstructure.valueformatters;

import com.developer.cookie.testdbstructure.database.model.OneMonthDivision;
import com.developer.cookie.testdbstructure.database.model.SixMonthDivision;
import com.developer.cookie.testdbstructure.database.model.ThreeMonthDivision;
import com.developer.cookie.testdbstructure.database.model.TwelveMonthDivision;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import io.realm.RealmResults;

/**
 * @author Veselina Zatchepina
 */

public class XAxisBarChartValueFormatter implements IAxisValueFormatter {

    RealmResults realmResults;

    public XAxisBarChartValueFormatter(RealmResults realmResults) {
        this.realmResults = realmResults;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        String currentMonth;
        if (realmResults.first() instanceof OneMonthDivision) {
            currentMonth = ((OneMonthDivision) realmResults.get((int) value - 1)).getCategory().getCategoryName();
        } else if (realmResults.first() instanceof ThreeMonthDivision) {
            currentMonth = ((ThreeMonthDivision) realmResults.get((int) value - 1)).getCategory().getCategoryName();
        } else if (realmResults.first() instanceof SixMonthDivision) {
            currentMonth = ((SixMonthDivision) realmResults.get((int) value - 1)).getCategory().getCategoryName();
        } else {
            currentMonth = ((TwelveMonthDivision) realmResults.get((int) value - 1)).getCategory().getCategoryName();
        }
        return currentMonth;
    }
}
