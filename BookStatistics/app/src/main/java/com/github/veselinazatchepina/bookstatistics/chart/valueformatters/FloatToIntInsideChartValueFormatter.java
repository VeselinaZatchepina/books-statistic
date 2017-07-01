package com.github.veselinazatchepina.bookstatistics.chart.valueformatters;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

public class FloatToIntInsideChartValueFormatter implements IValueFormatter {

    public FloatToIntInsideChartValueFormatter() {

    }

    @Override
    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
        return String.valueOf((int) value);
    }
}
