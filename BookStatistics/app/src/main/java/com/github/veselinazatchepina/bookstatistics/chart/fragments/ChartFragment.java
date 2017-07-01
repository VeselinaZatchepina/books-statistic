package com.github.veselinazatchepina.bookstatistics.chart.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import com.github.veselinazatchepina.bookstatistics.R;
import com.github.veselinazatchepina.bookstatistics.books.enums.DivisionType;
import com.github.veselinazatchepina.bookstatistics.chart.valueformatters.FloatToIntInsideChartValueFormatter;
import com.github.veselinazatchepina.bookstatistics.chart.valueformatters.FloatToIntValueFormatter;
import com.github.veselinazatchepina.bookstatistics.chart.valueformatters.XAxisBarChartValueFormatter;
import com.github.veselinazatchepina.bookstatistics.chart.valueformatters.XAxisLineChartValueFormatter;
import com.github.veselinazatchepina.bookstatistics.database.BooksRealmRepository;
import com.github.veselinazatchepina.bookstatistics.database.model.AllBookMonthDivision;
import com.github.veselinazatchepina.bookstatistics.database.model.BookMonthDivision;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;


public class ChartFragment extends Fragment {

    @BindView(R.id.lineChart_all_books)
    LineChart mLineChartAllBooks;
    @BindView(R.id.barChart_all_categories)
    BarChart mBarChartAllCategories;
    @BindView(R.id.lineChart_books_current_category)
    LineChart mLineChartBooksCurrentCategory;
    private Unbinder unbinder;

    View mRootView;
    private BooksRealmRepository mBooksRealmRepository;
    private RealmResults<AllBookMonthDivision> mAllBooksMonthDivision;
    private RealmResults<BookMonthDivision> mBookMonthDivisions;
    private RealmResults<BookMonthDivision> mBookMonthDivisionsByCategory;

    public ChartFragment() {
    }

    public static ChartFragment newInstance() {
        return new ChartFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        defineInputData(savedInstanceState);
        mBooksRealmRepository = new BooksRealmRepository();
        mAllBooksMonthDivision = mBooksRealmRepository.getAllBookMonth(0, 4);
        mBookMonthDivisions = mBooksRealmRepository.getBookMonthDivision();
        mBookMonthDivisionsByCategory = mBooksRealmRepository.getBookMonthDivisionByCategory("aaa");
    }

    private void defineInputData(Bundle savedInstanceState) {

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_chart, container, false);
        unbinder = ButterKnife.bind(this, mRootView);
        mAllBooksMonthDivision.addChangeListener(new RealmChangeListener<RealmResults<AllBookMonthDivision>>() {
            @Override
            public void onChange(RealmResults<AllBookMonthDivision> element) {
                if (isAdded() && !element.isEmpty()) {
                    createLineChart(element);
                }
            }
        });
        mBookMonthDivisions.addChangeListener(new RealmChangeListener<RealmResults<BookMonthDivision>>() {
            @Override
            public void onChange(RealmResults<BookMonthDivision> element) {
                if (isAdded() && !element.isEmpty()) {
                    createBarChart(element);
                }
            }
        });
        mBookMonthDivisionsByCategory.addChangeListener(new RealmChangeListener<RealmResults<BookMonthDivision>>() {
            @Override
            public void onChange(RealmResults<BookMonthDivision> element) {
                if (isAdded() && !element.isEmpty()) {
                    createCategoryLineChart(element);
                }
            }
        });
        return mRootView;
    }

    private void createLineChart(RealmResults<AllBookMonthDivision> allBooksMonthDivision) {
        mLineChartAllBooks.getXAxis().setValueFormatter(new XAxisLineChartValueFormatter(DivisionType.THREE));
        setLineChartStyle(mLineChartAllBooks);
        RealmLineDataSet<AllBookMonthDivision> lineDataSet =
                new RealmLineDataSet<AllBookMonthDivision>(allBooksMonthDivision, "month", "allBookCountThreeMonth");
        setRealmLineDataSetStyle(lineDataSet);
        ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        dataSets.add(lineDataSet);
        LineData lineData = new LineData(dataSets);
        mLineChartAllBooks.setData(lineData);
        mLineChartAllBooks.invalidate();
        mLineChartAllBooks.animateY(1400, Easing.EasingOption.EaseInOutQuart);
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

    private void createBarChart(RealmResults<BookMonthDivision> bookMonthDivisions) {
        setBarChartStyle(mBarChartAllCategories, bookMonthDivisions);
        RealmBarDataSet<BookMonthDivision> barDataSet =
                new RealmBarDataSet<BookMonthDivision>(bookMonthDivisions, "categoryIndex", "januaryJune");
        setRealmBarDataSetStyle(barDataSet);
        ArrayList<IBarDataSet> barDataSets = new ArrayList<IBarDataSet>();
        barDataSets.add(barDataSet);
        BarData barData = new BarData(barDataSets);
        mBarChartAllCategories.setData(barData);
        mBarChartAllCategories.setFitBars(true);
        mBarChartAllCategories.animateY(1400, Easing.EasingOption.EaseInOutQuart);
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

    private void createCategoryLineChart(RealmResults<BookMonthDivision> bookMonthDivisionsByCategory) {
        BookMonthDivision bookMonthDivision = bookMonthDivisionsByCategory.first();
        ArrayList<Float> listOfBookCount = createBooksCountList(bookMonthDivision);
        List<Entry> entries = createEntry(listOfBookCount);
        mLineChartBooksCurrentCategory.getXAxis().setValueFormatter(new XAxisLineChartValueFormatter(DivisionType.ONE));
        setLineChartStyle(mLineChartBooksCurrentCategory);
        LineDataSet dataSet = new LineDataSet(entries, "Label");
        setLineDataSetStyle(dataSet);
        ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        dataSets.add(dataSet);
        LineData lineData = new LineData(dataSets);
        mLineChartBooksCurrentCategory.setData(lineData);
        mLineChartBooksCurrentCategory.invalidate();
        mLineChartBooksCurrentCategory.animateY(1400, Easing.EasingOption.EaseInOutQuart);
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

    private void setLineDataSetStyle(LineDataSet dataSet) {
        dataSet.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
        dataSet.setColor(ColorTemplate.rgb("#FF5722"));
        dataSet.setCircleColor(ColorTemplate.rgb("#FF5722"));
        dataSet.setLineWidth(1.8f);
        dataSet.setCircleRadius(3.6f);
        dataSet.setValueTextSize(8f);
        dataSet.setValueFormatter(new FloatToIntInsideChartValueFormatter());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mBooksRealmRepository.closeDbConnect();
    }
}
