package com.github.veselinazatchepina.bookstatistics.chart.valueformatters;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

public class FloatToIntValueFormatter implements IAxisValueFormatter {

    public FloatToIntValueFormatter() {

    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        return String.valueOf((int) value);
    }
}
