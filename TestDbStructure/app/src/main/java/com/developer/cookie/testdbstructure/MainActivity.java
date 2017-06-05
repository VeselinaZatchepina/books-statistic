package com.developer.cookie.testdbstructure;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.developer.cookie.testdbstructure.database.RealmRepository;
import com.developer.cookie.testdbstructure.database.model.AllBookMonthDivision;
import com.developer.cookie.testdbstructure.database.model.Book;
import com.developer.cookie.testdbstructure.database.model.BookMonthDivision;
import com.developer.cookie.testdbstructure.utils.Division;
import com.developer.cookie.testdbstructure.utils.DivisionType;
import com.developer.cookie.testdbstructure.valueformatters.FloatToIntInsideChartValueFormatter;
import com.developer.cookie.testdbstructure.valueformatters.FloatToIntValueFormatter;
import com.developer.cookie.testdbstructure.valueformatters.XAxisBarChartValueFormatter;
import com.developer.cookie.testdbstructure.valueformatters.XAxisLineChartValueFormatter;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.realm.implementation.RealmBarDataSet;
import com.github.mikephil.charting.data.realm.implementation.RealmLineDataSet;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
        createCategoryLineChart();
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
            Date date = getDateFromString(dateValue);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            int month = calendar.get(Calendar.MONTH) + 1;
            int year = calendar.get(Calendar.YEAR);
            return  new int[] {month, year};
    }

    private Date getDateFromString(String dateValue) {
        Date date = null;
        try {
            DateFormat format = new SimpleDateFormat("dd.MM.yy", Locale.ENGLISH);
            date = format.parse(dateValue);
        } catch (ParseException p) {
            Log.v(MainActivity.class.getSimpleName(), p.toString());
        }
        return date;
    }

    private void checkDivision(List<Integer> array, Book book, RealmRepository realmRepository) {
        float indexOneMonth = isContainsList(array, Division.oneMonthDivisionArrays);
        if (indexOneMonth != -1) {
            realmRepository.saveBookInAllBookMonthDivision(book, indexOneMonth, DivisionType.ONE);
            realmRepository.saveBookInMonthDivision(book, indexOneMonth, DivisionType.ONE);
        }
        float indexThreeMonth = isContainsList(array, Division.threeMonthDivisionArrays);
        if (indexThreeMonth != -1) {
            realmRepository.saveBookInAllBookMonthDivision(book, indexThreeMonth, DivisionType.THREE);
            realmRepository.saveBookInMonthDivision(book, indexThreeMonth, DivisionType.THREE);
        }
        float indexSixMonth = isContainsList(array, Division.sixMonthDivisionArrays);
        if (indexSixMonth != -1) {
            realmRepository.saveBookInAllBookMonthDivision(book, indexSixMonth, DivisionType.SIX);
            realmRepository.saveBookInMonthDivision(book, indexSixMonth, DivisionType.SIX);
        }
        float indexTwelveMonth = isContainsList(array, Division.twelveMonthDivisionArrays);
        if (indexTwelveMonth != -1) {
            realmRepository.saveBookInAllBookMonthDivision(book, indexTwelveMonth, DivisionType.TWELVE);
            realmRepository.saveBookInMonthDivision(book, indexTwelveMonth, DivisionType.TWELVE);
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
        final RealmResults<AllBookMonthDivision> realmResults = mRealmRepository.getAllBookMonth(0, 4);
        LineChart lineChart = (LineChart) findViewById(R.id.lineChart);
        lineChart.getXAxis().setValueFormatter(new XAxisLineChartValueFormatter(DivisionType.THREE));
        setLineChartStyle(lineChart);
        RealmLineDataSet<AllBookMonthDivision> lineDataSet =
                new RealmLineDataSet<AllBookMonthDivision>(realmResults, "month", "allBookCountThreeMonth");
        setRealmLineDataSetStyle(lineDataSet);
        ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        dataSets.add(lineDataSet);
        LineData lineData = new LineData(dataSets);
        lineChart.setData(lineData);
        lineChart.invalidate();
        lineChart.animateY(1400, Easing.EasingOption.EaseInOutQuart);
    }

    private void createBarChart() {
        final RealmResults<BookMonthDivision> bookMonthDivisions = mRealmRepository.getBookMonthDivision();
        BarChart barChart = (BarChart) findViewById(R.id.barChart);
        setBarChartStyle(barChart, bookMonthDivisions);
        RealmBarDataSet<BookMonthDivision> barDataSet =
                new RealmBarDataSet<BookMonthDivision>(bookMonthDivisions, "categoryIndex", "januaryJune");
        setRealmBarDataSetStyle(barDataSet);
        ArrayList<IBarDataSet> barDataSets = new ArrayList<IBarDataSet>();
        barDataSets.add(barDataSet);
        BarData barData = new BarData(barDataSets);
        barChart.setData(barData);
        barChart.setFitBars(true);
        barChart.animateY(1400, Easing.EasingOption.EaseInOutQuart);
    }

    private void createCategoryLineChart() {
        RealmResults<BookMonthDivision> realmResults = mRealmRepository.getBookMonthDivisionByCategory("IT0");
        BookMonthDivision bookMonthDivision = realmResults.first();
        ArrayList<Float> listOfBookCount = createBooksCountList(bookMonthDivision);
        List<Entry> entries = createEntry(listOfBookCount);
        LineChart lineChart = (LineChart) findViewById(R.id.lineChart_category);
        lineChart.getXAxis().setValueFormatter(new XAxisLineChartValueFormatter(DivisionType.ONE));
        setLineChartStyle(lineChart);
        LineDataSet dataSet = new LineDataSet(entries, "Label");
        setLineDataSetStyle(dataSet);
        ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        dataSets.add(dataSet);
        LineData lineData = new LineData(dataSets);
        lineChart.setData(lineData);
        lineChart.invalidate();
        lineChart.animateY(1400, Easing.EasingOption.EaseInOutQuart);
    }

    private ArrayList<Float> createBooksCountList(BookMonthDivision bookMonthDivision) {
        ArrayList<Float> listOfBookCount = new ArrayList<>();
        listOfBookCount.add(bookMonthDivision.getJanuary());
        listOfBookCount.add(bookMonthDivision.getFebruary());
        listOfBookCount.add(bookMonthDivision.getMarch());
        listOfBookCount.add(bookMonthDivision.getApril());
        listOfBookCount.add(bookMonthDivision.getMay());
        listOfBookCount.add(bookMonthDivision.getJune());
        listOfBookCount.add(bookMonthDivision.getJuly());
        listOfBookCount.add(bookMonthDivision.getAugust());
        listOfBookCount.add(bookMonthDivision.getSeptember());
        listOfBookCount.add(bookMonthDivision.getOctober());
        listOfBookCount.add(bookMonthDivision.getNovember());
        listOfBookCount.add(bookMonthDivision.getDecember());
        return listOfBookCount;
    }

    private List<Entry> createEntry(ArrayList<Float> listOfBookCount) {
        List<Entry> entries = new ArrayList<Entry>();
        for (int i = 0; i < listOfBookCount.size(); i++) {
            entries.add(new Entry((float) i, listOfBookCount.get(i)));
        }
        return entries;
    }

    private void setLineChartStyle(LineChart lineChart) {
        Description description = lineChart.getDescription();
        description.setEnabled(false);
        Legend leg = lineChart.getLegend();
        leg.setEnabled(false);
        lineChart.getAxisRight().setValueFormatter(new FloatToIntValueFormatter());
        lineChart.getAxisRight().setGranularity(1f);
        lineChart.getAxisRight().setAxisMinValue(0f);
        lineChart.getAxisLeft().setValueFormatter(new FloatToIntValueFormatter());
        lineChart.getAxisLeft().setGranularity(1f);
        lineChart.getAxisLeft().setAxisMinValue(0f);
        lineChart.getAxisLeft().setDrawGridLines(false);
        lineChart.getXAxis().setGranularity(1f);
        lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        lineChart.getXAxis().setAxisMinValue(0f);
        lineChart.getXAxis().setDrawGridLines(false);
    }

    private void setRealmLineDataSetStyle(RealmLineDataSet<AllBookMonthDivision> lineDataSet) {
        lineDataSet.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
        lineDataSet.setColor(ColorTemplate.rgb("#FF5722"));
        lineDataSet.setCircleColor(ColorTemplate.rgb("#FF5722"));
        lineDataSet.setLineWidth(1.8f);
        lineDataSet.setCircleRadius(3.6f);
        lineDataSet.setValueTextSize(8f);
        lineDataSet.setValueFormatter(new FloatToIntInsideChartValueFormatter());
    }

    private void setLineDataSetStyle(LineDataSet dataSet) {
        dataSet.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
        dataSet.setColor(ColorTemplate.rgb("#FF5722"));
        dataSet.setCircleColor(ColorTemplate.rgb("#FF5722"));
        dataSet.setLineWidth(1.8f);
        dataSet.setCircleRadius(3.6f);
        dataSet.setValueTextSize(8f);
        dataSet.setValueFormatter(new FloatToIntInsideChartValueFormatter());
    }

    private void setBarChartStyle(BarChart barChart, RealmResults<BookMonthDivision> bookMonthDivisions) {
        barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        barChart.getAxisRight().setGranularity(1f);
        barChart.getAxisLeft().setGranularity(1f);
        barChart.getXAxis().setGranularity(1f);
        barChart.getAxisLeft().setAxisMinValue(0f);
        barChart.getAxisRight().setAxisMinValue(0f);
        Description description = barChart.getDescription();
        description.setEnabled(false);
        Legend legend = barChart.getLegend();
        legend.setEnabled(false);
        barChart.getAxisLeft().setValueFormatter(new FloatToIntValueFormatter());
        barChart.getAxisRight().setValueFormatter(new FloatToIntValueFormatter());
        barChart.getXAxis().setValueFormatter(new XAxisBarChartValueFormatter(bookMonthDivisions));
    }

    private void setRealmBarDataSetStyle(RealmBarDataSet<BookMonthDivision> barDataSet) {
        barDataSet.setValueTextSize(8f);
        barDataSet.setColors(new int[]{ColorTemplate.rgb("#FF5722"), ColorTemplate.rgb("#03A9F4")});
        barDataSet.setValueFormatter(new FloatToIntInsideChartValueFormatter());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRealmRepository.closeDb();
    }
}
