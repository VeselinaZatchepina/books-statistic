package com.developer.cookie.testdbstructure;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.developer.cookie.testdbstructure.database.RealmRepository;
import com.developer.cookie.testdbstructure.database.model.AllBookOneMonthDivision;
import com.developer.cookie.testdbstructure.database.model.Book;
import com.developer.cookie.testdbstructure.database.model.TwelveMonthDivision;
import com.developer.cookie.testdbstructure.utils.Division;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.BarData;
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
        int indexOneMonth = isContainsList(array, Division.oneMonthDivisionArrays);
        if (indexOneMonth != -1) {
            Log.v("OK", "OK1");
            realmRepository.saveBookInAllBookOneMonth(book, indexOneMonth);
            realmRepository.saveBookInOneMonthDivision(book, indexOneMonth);
        }
        int indexThreeMonth = isContainsList(array, Division.threeMonthDivisionArrays);
        if (indexThreeMonth != -1) {
            Log.v("OK", "OK3");
            realmRepository.saveBookInAllBookThreeMonth(book, indexThreeMonth);
            realmRepository.saveBookInThreeMonthDivision(book, indexThreeMonth);
        }
        int indexSixMonth = isContainsList(array, Division.sixMonthDivisionArrays);
        if (indexSixMonth != -1) {
            Log.v("OK", "OK6");
            realmRepository.saveBookInAllBookSixMonth(book, indexSixMonth);
            realmRepository.saveBookInSixMonthDivision(book, indexSixMonth);
        }
        int indexTwelveMonth = isContainsList(array, Division.twelveMonthDivisionArrays);
        if (indexTwelveMonth != -1) {
            Log.v("OK", "OK12");
            realmRepository.saveBookInAllBookTwelveMonth(book, indexTwelveMonth);
            realmRepository.saveBookInTwelveMonthDivision(book, indexTwelveMonth);
        }
    }

    private int isContainsList(List<Integer> array, ArrayList<List<Integer>> monthDivision) {
        int index = -1;
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
        lineChart.setExtraBottomOffset(5f);
        lineChart.getAxisLeft().setDrawGridLines(false);
        lineChart.getXAxis().setDrawGridLines(false);
        lineChart.getXAxis().setLabelCount(5);
        lineChart.getXAxis().setGranularity(1f);
        RealmLineDataSet<AllBookOneMonthDivision> lineDataSet = new RealmLineDataSet<AllBookOneMonthDivision>(realmResults, "month", "allBookCount");
        lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        lineDataSet.setLabel("Result Scores");
        lineDataSet.setDrawCircleHole(false);
        lineDataSet.setColor(ColorTemplate.rgb("#FF5722"));
        lineDataSet.setCircleColor(ColorTemplate.rgb("#FF5722"));
        lineDataSet.setLineWidth(1.8f);
        lineDataSet.setCircleRadius(3.6f);
        ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        dataSets.add(lineDataSet);
        LineData lineData = new LineData(dataSets);
        lineChart.setData(lineData);
        lineChart.invalidate();
        lineChart.animateY(1400, Easing.EasingOption.EaseInOutQuart);
    }

    private void createBarChart() {
        final RealmResults<TwelveMonthDivision> twelveRealmResults = mRealmRepository.getTwelveMonthDivision();
        BarChart barChart = (BarChart) findViewById(R.id.barChart);
        RealmBarDataSet<TwelveMonthDivision> barDataSet = new RealmBarDataSet<TwelveMonthDivision>(twelveRealmResults, "id", "januaryDecember");
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
}
