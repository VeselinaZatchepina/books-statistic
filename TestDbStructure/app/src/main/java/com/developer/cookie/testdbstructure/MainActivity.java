package com.developer.cookie.testdbstructure;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.developer.cookie.testdbstructure.db.RealmRepository;
import com.developer.cookie.testdbstructure.db.model.Book;
import com.developer.cookie.testdbstructure.db.model.SixMonthDivision;
import com.developer.cookie.testdbstructure.db.model.ThreeMonthDivision;
import com.developer.cookie.testdbstructure.db.model.TwelveMonthDivision;
import com.developer.cookie.testdbstructure.utils.Division;

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

        RealmResults<Book> books = mRealmRepository.getBookByEndDate("13.12.2017");
        for (Book currentBook : books) {
            ArrayList<Integer> bookMonthYearArray = createMonthYearArray(currentBook.getDateStart(), currentBook.getDateEnd());
            checkDivision(bookMonthYearArray, currentBook, mRealmRepository);
        }

        // TODO table with all books

//        final RealmResults<OneMonthDivision> realmResults = mRealm.where(OneMonthDivision.class).findAll();
//        LineChart lineChart = (LineChart) findViewById(R.id.lineChart);
//        lineChart.setExtraBottomOffset(5f);
//        lineChart.getAxisLeft().setDrawGridLines(false);
//        lineChart.getXAxis().setDrawGridLines(false);
//        lineChart.getXAxis().setLabelCount(5);
//        lineChart.getXAxis().setGranularity(1f);

//                    IAxisValueFormatter formatter = new IAxisValueFormatter() {
//                        @Override
//                        public String getFormattedValue(float value, AxisBase axis) {
//                            return realmResults.get((int) value).getCategory().getCategoryName();
//                        }
//                    };
//                    lineChart.getXAxis().setValueFormatter(formatter);

//        RealmLineDataSet<OneMonthDivision> lineDataSet = new RealmLineDataSet<OneMonthDivision>(realmResults, "id", "february");
//        lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
//        lineDataSet.setLabel("Result Scores");
//        lineDataSet.setDrawCircleHole(false);
//        lineDataSet.setColor(ColorTemplate.rgb("#FF5722"));
//        lineDataSet.setCircleColor(ColorTemplate.rgb("#FF5722"));
//        lineDataSet.setLineWidth(1.8f);
//        lineDataSet.setCircleRadius(3.6f);
//
//        ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
//        dataSets.add(lineDataSet);
//
//        LineData lineData = new LineData(dataSets);
//
//        // set data
//        lineChart.setData(lineData);
//        lineChart.invalidate();
//        lineChart.animateY(1400, Easing.EasingOption.EaseInOutQuart);

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
//        if (isContainsList(array, Division.threeMonthDivisionArrays) != -1) {
//            Log.v("LOG", "OK3");
//            ThreeMonthDivision threeMonthDivision;
//            RealmResults<ThreeMonthDivision> realmResults = realm.where(ThreeMonthDivision.class).equalTo("category.categoryName", book.getBookCategory().getCategoryName()).findAll();
//            if (!realmResults.isEmpty()) {
//                threeMonthDivision = realmResults.first();
//            } else {
//                threeMonthDivision = realm.createObject(ThreeMonthDivision.class);
//                threeMonthDivision.setId(0);
//                threeMonthDivision.setYear(book.getYear());
//                threeMonthDivision.setCategory(book.getBookCategory());
//            }
//            checkThreeMonth(isContainsList(array, Division.threeMonthDivisionArrays), threeMonthDivision);
//            // for all category
//            ThreeMonthDivision threeMonthDivisionForAllCategory;
//            RealmResults<ThreeMonthDivision> allCategoryResults = realm.where(ThreeMonthDivision.class)
//                    .equalTo("category.categoryName", "AllThreeMonth")
//                    .equalTo("year.yearNumber", book.getYear().getYearNumber()).findAll();
//            if (!allCategoryResults.isEmpty()) {
//                threeMonthDivisionForAllCategory = allCategoryResults.first();
//            } else {
//                threeMonthDivisionForAllCategory = realm.createObject(ThreeMonthDivision.class);
//                threeMonthDivisionForAllCategory.setId(0);
//                threeMonthDivisionForAllCategory.setYear(book.getYear());
//                BookCategory bookCategory = realm.createObject(BookCategory.class);
//                bookCategory.setId(3);
//                bookCategory.setCategoryName("AllThreeMonth");
//                bookCategory.setCategoryBookCount(1);
//                threeMonthDivisionForAllCategory.setCategory(bookCategory); // правильно обновлять book category count и надо ли?
//            }
//            checkThreeMonth(isContainsList(array, Division.threeMonthDivisionArrays), threeMonthDivisionForAllCategory);
//        }
//        if (isContainsList(array, Division.sixMonthDivisionArrays) != -1) {
//            Log.v("LOG", "OK6");
//            SixMonthDivision sixMonthDivision;
//            RealmResults<SixMonthDivision> realmResults = realm.where(SixMonthDivision.class).equalTo("category.categoryName", book.getBookCategory().getCategoryName()).findAll();
//            if (!realmResults.isEmpty()) {
//                sixMonthDivision = realmResults.first();
//            } else {
//                sixMonthDivision = realm.createObject(SixMonthDivision.class);
//                sixMonthDivision.setId(0);
//                sixMonthDivision.setYear(book.getYear());
//                sixMonthDivision.setCategory(book.getBookCategory());
//            }
//            checkSixMonth(isContainsList(array, Division.sixMonthDivisionArrays), sixMonthDivision);
//            // for all category
//            SixMonthDivision sixMonthDivisionForAllCategory;
//            RealmResults<SixMonthDivision> allCategoryResults = realm.where(SixMonthDivision.class)
//                    .equalTo("category.categoryName", "AllSixMonth")
//                    .equalTo("year.yearNumber", book.getYear().getYearNumber()).findAll();
//            if (!allCategoryResults.isEmpty()) {
//                sixMonthDivisionForAllCategory = allCategoryResults.first();
//            } else {
//                sixMonthDivisionForAllCategory = realm.createObject(SixMonthDivision.class);
//                sixMonthDivisionForAllCategory.setId(0);
//                sixMonthDivisionForAllCategory.setYear(book.getYear());
//                BookCategory bookCategory = realm.createObject(BookCategory.class);
//                bookCategory.setId(3);
//                bookCategory.setCategoryName("AllSixMonth");
//                bookCategory.setCategoryBookCount(1);
//                sixMonthDivisionForAllCategory.setCategory(bookCategory); // правильно обновлять book category count и надо ли?
//            }
//            checkSixMonth(isContainsList(array, Division.sixMonthDivisionArrays), sixMonthDivisionForAllCategory);
//        }
//        if (isContainsList(array, Division.twelveMonthDivisionArrays) != -1) {
//            Log.v("LOG", "OK12");
//            TwelveMonthDivision twelveMonthDivision;
//            RealmResults<TwelveMonthDivision> realmResults = realm.where(TwelveMonthDivision.class).equalTo("category.categoryName", book.getBookCategory().getCategoryName()).findAll();
//            if (!realmResults.isEmpty()) {
//                twelveMonthDivision = realmResults.first();
//            } else {
//                twelveMonthDivision = realm.createObject(TwelveMonthDivision.class);
//                twelveMonthDivision.setId(0);
//                twelveMonthDivision.setYear(book.getYear());
//                twelveMonthDivision.setCategory(book.getBookCategory());
//            }
//            checkTwelveMonth(isContainsList(array, Division.twelveMonthDivisionArrays), twelveMonthDivision);
//            // for all category
//            TwelveMonthDivision twelveMonthDivisionForAllCategory;
//            RealmResults<TwelveMonthDivision> allCategoryResults = realm.where(TwelveMonthDivision.class)
//                    .equalTo("category.categoryName", "AllTwelveMonth")
//                    .equalTo("year.yearNumber", book.getYear().getYearNumber()).findAll();
//            if (!allCategoryResults.isEmpty()) {
//                twelveMonthDivisionForAllCategory = allCategoryResults.first();
//            } else {
//                twelveMonthDivisionForAllCategory = realm.createObject(TwelveMonthDivision.class);
//                twelveMonthDivisionForAllCategory.setId(0);
//                twelveMonthDivisionForAllCategory.setYear(book.getYear());
//                BookCategory bookCategory = realm.createObject(BookCategory.class);
//                bookCategory.setId(3);
//                bookCategory.setCategoryName("AllTwelveMonth");
//                bookCategory.setCategoryBookCount(1);
//                twelveMonthDivisionForAllCategory.setCategory(bookCategory); // правильно обновлять book category count и надо ли?
//            }
//            checkTwelveMonth(isContainsList(array, Division.twelveMonthDivisionArrays), twelveMonthDivisionForAllCategory);
//        }
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



    private String checkThreeMonth(int index, ThreeMonthDivision threeMonthDivision) {
        switch (index) {
            case 0:
                threeMonthDivision.setJanuaryMarch(threeMonthDivision.getJanuaryMarch() + 1);
                break;
            case 1:
                threeMonthDivision.setAprilJune(threeMonthDivision.getAprilJune() + 1);
                break;
            case 2:
                threeMonthDivision.setJulySeptember(threeMonthDivision.getJulySeptember() + 1);
                break;
            case 3:
                threeMonthDivision.setOctoberDecember(threeMonthDivision.getOctoberDecember() + 1);
                break;
        }
        return null;
    }

    private String checkSixMonth(int index, SixMonthDivision sixMonthDivision) {
        switch (index) {
            case 0:
                sixMonthDivision.setJanuaryJune(sixMonthDivision.getJanuaryJune() + 1);
                break;
            case 1:
                sixMonthDivision.setJulyDecember(sixMonthDivision.getJulyDecember() + 1);
                break;
        }
        return null;
    }

    private String checkTwelveMonth(int index, TwelveMonthDivision twelveMonthDivision) {
        switch (index) {
            case 0:
                twelveMonthDivision.setJanuaryDecember(twelveMonthDivision.getJanuaryDecember() + 1);
                break;
        }
        return null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRealmRepository.closeDb();
    }
}
