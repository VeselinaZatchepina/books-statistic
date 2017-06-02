package com.developer.cookie.testdbstructure;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.developer.cookie.testdbstructure.database.RealmRepository;
import com.developer.cookie.testdbstructure.database.model.AllBookOneMonthDivision;
import com.developer.cookie.testdbstructure.database.model.AllBookSixMonthDivision;
import com.developer.cookie.testdbstructure.database.model.AllBookThreeMonthDivision;
import com.developer.cookie.testdbstructure.database.model.Book;
import com.developer.cookie.testdbstructure.database.model.SixMonthDivision;
import com.developer.cookie.testdbstructure.utils.Division;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.realm.implementation.RealmBarDataSet;
import com.github.mikephil.charting.data.realm.implementation.RealmLineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.realm.RealmObject;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {

    RealmRepository mRealmRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRealmRepository = new RealmRepository();
        mRealmRepository.saveBook();

        RealmResults<Book> books = mRealmRepository.getAllBooks();
        for (Book currentBook : books) {
            ArrayList<Integer> bookMonthYearArray = createMonthYearArray(currentBook.getDateStart(), currentBook.getDateEnd());
            checkDivision(bookMonthYearArray, currentBook, mRealmRepository);
        }

        createLineChart();
        createBarChart();

        // TODO third chart
    }

    private ArrayList<Integer> createMonthYearArray(String dateStart, String dateEnd) {
        int[] dateStartArray = getCurrentBookMonthYearArray(dateStart);
        int[] dateEndArray = getCurrentBookMonthYearArray(dateEnd);
        ArrayList<Integer> monthArray = new ArrayList<>();
        for (int i = dateStartArray[0]; i <= dateEndArray[0]; i++) {
            monthArray.add(i);
        }
        return monthArray;
    }

    private int[] getCurrentBookMonthYearArray(String dateValue) {
        try {
            DateFormat format = new SimpleDateFormat("dd.MM.yy", Locale.ENGLISH);
            Date date = format.parse(dateValue);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            int month = calendar.get(Calendar.MONTH) + 1;
            int year = calendar.get(Calendar.YEAR);
            return  new int[] {month, year};
        } catch (ParseException p) {
            Log.v(MainActivity.class.getSimpleName(), p.toString());
        }
        return null;
    }

    private void checkDivision(List<Integer> array, Book book, RealmRepository realmRepository) {
        float indexOneMonth = isContainsList(array, Division.oneMonthDivisionArrays);
        if (indexOneMonth != -1) {
            Log.v("OK", "OK1");
            realmRepository.saveBookInAllBookOneMonth(book, indexOneMonth);
            realmRepository.saveBookInOneMonthDivision(book, indexOneMonth);
        }
        float indexThreeMonth = isContainsList(array, Division.threeMonthDivisionArrays);
        if (indexThreeMonth != -1) {
            Log.v("OK", "OK3");
            realmRepository.saveBookInAllBookThreeMonth(book, indexThreeMonth);
            realmRepository.saveBookInThreeMonthDivision(book, indexThreeMonth);
        }
        float indexSixMonth = isContainsList(array, Division.sixMonthDivisionArrays);
        if (indexSixMonth != -1) {
            Log.v("OK", "OK6");
            realmRepository.saveBookInAllBookSixMonth(book, indexSixMonth);
            realmRepository.saveBookInSixMonthDivision(book, indexSixMonth);
        }
        float indexTwelveMonth = isContainsList(array, Division.twelveMonthDivisionArrays);
        if (indexTwelveMonth != -1) {
            Log.v("OK", "OK12");
            realmRepository.saveBookInAllBookTwelveMonth(book, indexTwelveMonth);
            realmRepository.saveBookInTwelveMonthDivision(book, indexTwelveMonth);
        }
    }

    private float isContainsList(List<Integer> array, ArrayList<List<Integer>> monthDivision) {
        float index = -1;
        for (List<Integer> element : monthDivision) {
            if (element.containsAll(array)) {
                index = monthDivision.indexOf(element);
            }
        }
        return index;
    }

    private void createLineChart() {
        final RealmResults<AllBookOneMonthDivision> realmResults = mRealmRepository.getAllBookOneMonth();
        LineChart lineChart = (LineChart) findViewById(R.id.lineChart);
        Description description = lineChart.getDescription();
        description.setEnabled(false);
        Legend leg = lineChart.getLegend();
        leg.setEnabled(false);
        lineChart.getAxisRight().setValueFormatter(new LineChartValueFormatter());
        lineChart.getAxisRight().setGranularity(1f);
        lineChart.getAxisRight().setAxisMinValue(0f);
        lineChart.getAxisLeft().setValueFormatter(new LineChartValueFormatter());
        lineChart.getAxisLeft().setGranularity(1f);
        lineChart.getAxisLeft().setAxisMinValue(0f);
        lineChart.getAxisLeft().setDrawGridLines(false);
        lineChart.getXAxis().setGranularity(1f);
        lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        lineChart.getXAxis().setValueFormatter(new XAxisLineChartValueFormatter(realmResults.first()));
        lineChart.getXAxis().setAxisMinValue(0f);
        lineChart.getXAxis().setDrawGridLines(false);

        RealmLineDataSet<AllBookOneMonthDivision> lineDataSet =
                new RealmLineDataSet<AllBookOneMonthDivision>(realmResults, "month", "allBookCount");
        lineDataSet.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
        lineDataSet.setColor(ColorTemplate.rgb("#FF5722"));
        lineDataSet.setCircleColor(ColorTemplate.rgb("#FF5722"));
        lineDataSet.setLineWidth(1.8f);
        lineDataSet.setCircleRadius(3.6f);
        lineDataSet.setValueFormatter(new InsideLineChartValueFormatter());
        ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        dataSets.add(lineDataSet);
        LineData lineData = new LineData(dataSets);
        lineChart.setData(lineData);
        lineChart.invalidate();
        lineChart.animateY(1400, Easing.EasingOption.EaseInOutQuart);
    }

    private void createBarChart() {
        final RealmResults<SixMonthDivision> twelveRealmResults = mRealmRepository.getSixMonthDivision();
        BarChart barChart = (BarChart) findViewById(R.id.barChart);
        barChart.getAxisLeft().setValueFormatter(new XAxisLineChartValueFormatter(twelveRealmResults.first()));
        RealmBarDataSet<SixMonthDivision> barDataSet =
                new RealmBarDataSet<SixMonthDivision>(twelveRealmResults, "categoryIndex", "januaryJune");
        barDataSet.setColors(new int[]{ColorTemplate.rgb("#FF5722"), ColorTemplate.rgb("#03A9F4")});
        barDataSet.setLabel("Realm BarDataSet");
        ArrayList<IBarDataSet> barDataSets = new ArrayList<IBarDataSet>();
        barDataSets.add(barDataSet);
        BarData barData = new BarData(barDataSets);
        barChart.setData(barData);
        barChart.setFitBars(true);
        barChart.animateY(1400, Easing.EasingOption.EaseInOutQuart);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRealmRepository.closeDb();
    }

    public class LineChartValueFormatter implements IAxisValueFormatter {

        public LineChartValueFormatter() {

        }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            return String.valueOf((int) value);
        }
    }

    public class InsideLineChartValueFormatter implements IValueFormatter {

        public InsideLineChartValueFormatter() {

        }

        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
            return String.valueOf((int) value);
        }
    }

    public class XAxisLineChartValueFormatter implements IAxisValueFormatter {

        RealmObject realmObject;

        public XAxisLineChartValueFormatter(RealmObject realmObject) {
            this.realmObject = realmObject;
        }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            String currentMonth;
            if (realmObject instanceof AllBookOneMonthDivision) {
                currentMonth = checkLabelOneMonth(value);
            } else if (realmObject instanceof AllBookThreeMonthDivision) {
                currentMonth = checkLabelThreeMonth(value);
            } else if (realmObject instanceof AllBookSixMonthDivision) {
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
}
