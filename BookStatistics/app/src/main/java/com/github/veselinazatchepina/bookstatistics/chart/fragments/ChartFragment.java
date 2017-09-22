package com.github.veselinazatchepina.bookstatistics.chart.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.realm.implementation.RealmBarDataSet;
import com.github.mikephil.charting.data.realm.implementation.RealmLineDataSet;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.veselinazatchepina.bookstatistics.MyApplication;
import com.github.veselinazatchepina.bookstatistics.R;
import com.github.veselinazatchepina.bookstatistics.books.enums.AllMonth;
import com.github.veselinazatchepina.bookstatistics.books.enums.DivisionType;
import com.github.veselinazatchepina.bookstatistics.books.enums.MonthIndex;
import com.github.veselinazatchepina.bookstatistics.chart.valueformatters.FloatToIntInsideChartValueFormatter;
import com.github.veselinazatchepina.bookstatistics.chart.valueformatters.FloatToIntValueFormatter;
import com.github.veselinazatchepina.bookstatistics.chart.valueformatters.XAxisBarChartValueFormatter;
import com.github.veselinazatchepina.bookstatistics.chart.valueformatters.XAxisLineChartValueFormatter;
import com.github.veselinazatchepina.bookstatistics.database.BooksRealmRepository;
import com.github.veselinazatchepina.bookstatistics.database.model.AllBookMonthDivision;
import com.github.veselinazatchepina.bookstatistics.database.model.BookCategory;
import com.github.veselinazatchepina.bookstatistics.database.model.BookMonthDivision;
import com.github.veselinazatchepina.bookstatistics.database.model.Year;
import com.squareup.leakcanary.RefWatcher;

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
    @BindView(R.id.year_spinner_all_books)
    Spinner mYearSpinnerAllBooks;
    @BindView(R.id.month_division_spinner_all_books)
    Spinner mMonthDivisionSpinnerAllBooks;
    @BindView(R.id.year_spinner_all_categories)
    Spinner mYearSpinnerAllCategories;
    @BindView(R.id.month_division_spinner_all_categories)
    Spinner mMonthTypeSpinnerAllCategories;
    @BindView(R.id.year_spinner_current_category)
    Spinner mYearSpinnerCurrentCategory;
    @BindView(R.id.category_spinner)
    Spinner mCategorySpinner;
    @BindView(R.id.month_division_spinner_current_category)
    Spinner mMonthDivisionSpinnerCurrentCategory;
    private Unbinder unbinder;

    View mRootView;
    private BooksRealmRepository mBooksRealmRepository;
    private RealmResults<AllBookMonthDivision> mAllBooksMonthDivision;
    private RealmResults<BookMonthDivision> mBookMonthDivisionsByCategory;
    private RealmResults<Year> mAllYears;
    private RealmResults<BookCategory> mAllBookCategories;

    ArrayAdapter<Integer> mYearSpinnerAdapter;
    ArrayAdapter<String> mMonthDivisionSpinnerAdapter;
    ArrayAdapter<String> mCategorySpinnerAdapter;
    ArrayAdapter<String> mMonthTypeSpinnerAdapter;

    String mDivisionTypeAllBooks = DivisionType.ONE;
    String mCategoryNameCurrentCategory = DivisionType.THREE;
    String mDivisionTypeCurrentCategory = DivisionType.THREE;
    String mMonthTypeAllCategories = AllMonth.JANUARY_DECEMBER;

    int mCurrentYearAllBooksChart = 0;
    int mCurrentYearAllCategoriesChart = 0;
    int mCurrentYearCurrentCategory = 0;

    public ChartFragment() {
    }

    public static ChartFragment newInstance() {
        return new ChartFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBooksRealmRepository = new BooksRealmRepository();
        mAllYears = mBooksRealmRepository.getAllYears();
        mAllBookCategories = mBooksRealmRepository.getListOfBookCategories();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_chart, container, false);
        unbinder = ButterKnife.bind(this, mRootView);
        defineYearSpinners();
        defineMonthDivisionSpinners();
        defineCategorySpinner();
        defineMonthTypeSpinner();
        setListenerToYearSpinnerAllBooks();
        setListenerToMonthDivisionSpinnerAllBooks();
        setListenerToMonthTypeSpinnerAllCategories();
        setListenerToYearSpinnerAllCategories();
        setListenerToYearSpinnerCurrentCategory();
        setListenerToMonthDivisionSpinnerCurrentCategory();
        setListenerToCategorySpinnerCurrentCategory();
        return mRootView;
    }

    private void createLineChart(RealmResults<AllBookMonthDivision> allBooksMonthDivision) {
        mLineChartAllBooks.getXAxis().setValueFormatter(new XAxisLineChartValueFormatter(mDivisionTypeAllBooks));
        setLineChartStyle(mLineChartAllBooks);
        RealmLineDataSet<AllBookMonthDivision> lineDataSet =
                new RealmLineDataSet<AllBookMonthDivision>(allBooksMonthDivision, "month", getColumnDivisionTypeName());
        setRealmLineDataSetStyle(lineDataSet);
        ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        dataSets.add(lineDataSet);
        LineData lineData = new LineData(dataSets);
        mLineChartAllBooks.setData(lineData);
        mLineChartAllBooks.setVisibleYRangeMaximum(40, YAxis.AxisDependency.LEFT);
        mLineChartAllBooks.setVisibleYRangeMaximum(40, YAxis.AxisDependency.RIGHT);
        mLineChartAllBooks.setVisibleYRangeMinimum(1, YAxis.AxisDependency.RIGHT);
        mLineChartAllBooks.setVisibleYRangeMinimum(1, YAxis.AxisDependency.LEFT);
        mLineChartAllBooks.invalidate();
        mLineChartAllBooks.animateY(1400, Easing.EasingOption.EaseInOutQuart);
    }

    private String getColumnDivisionTypeName() {
        String columnName = "allBookCountThreeMonth";
        switch (mDivisionTypeAllBooks) {
            case DivisionType.ONE:
                columnName = "allBookCountOneMonth";
                break;
            case DivisionType.THREE:
                columnName = "allBookCountThreeMonth";
                break;
            case DivisionType.SIX:
                columnName = "allBookCountSixMonth";
                break;
            case DivisionType.TWELVE:
                columnName = "allBookCountTwelveMonth";
                break;
        }
        return columnName;
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
        lineDataSet.setColor(ColorTemplate.rgb("#339064"));
        lineDataSet.setCircleColor(ColorTemplate.rgb("#339064"));
        lineDataSet.setLineWidth(1.8f);
        lineDataSet.setCircleRadius(3.6f);
        lineDataSet.setValueTextSize(8f);
        lineDataSet.setValueFormatter(new FloatToIntInsideChartValueFormatter());
    }

    private void createBarChart(RealmResults<BookMonthDivision> bookMonthDivisions) {
        setBarChartStyle(mBarChartAllCategories, bookMonthDivisions);
        RealmBarDataSet<BookMonthDivision> barDataSet =
                new RealmBarDataSet<BookMonthDivision>(bookMonthDivisions, "categoryIndex", mMonthTypeAllCategories);
        setRealmBarDataSetStyle(barDataSet);
        ArrayList<IBarDataSet> barDataSets = new ArrayList<IBarDataSet>();
        barDataSets.add(barDataSet);
        BarData barData = new BarData(barDataSets);
        barData.notifyDataChanged();
        mBarChartAllCategories.setData(barData);
        mBarChartAllCategories.setVisibleXRangeMaximum(3);
        mBarChartAllCategories.notifyDataSetChanged();
        mBarChartAllCategories.invalidate();
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
        barDataSet.setColors(new int[]{ColorTemplate.rgb("#339064"), ColorTemplate.rgb("#c1272d")});
        barDataSet.setValueFormatter(new FloatToIntInsideChartValueFormatter());
    }

    private void createCategoryLineChart(RealmResults<BookMonthDivision> bookMonthDivisionsByCategory) {
        BookMonthDivision bookMonthDivision = bookMonthDivisionsByCategory.first();
        ArrayList<Float> listOfBookCount = createBooksCountList(bookMonthDivision);
        List<Entry> entries = createEntry(listOfBookCount);
        mLineChartBooksCurrentCategory.getXAxis().setValueFormatter(new XAxisLineChartValueFormatter(mDivisionTypeCurrentCategory));
        setLineChartStyle(mLineChartBooksCurrentCategory);
        LineDataSet dataSet = new LineDataSet(entries, "Label");
        setLineDataSetStyle(dataSet);
        ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        dataSets.add(dataSet);
        LineData lineData = new LineData(dataSets);
        mLineChartBooksCurrentCategory.setData(lineData);
        mLineChartBooksCurrentCategory.setVisibleYRangeMaximum(40, YAxis.AxisDependency.LEFT);
        mLineChartBooksCurrentCategory.setVisibleYRangeMaximum(40, YAxis.AxisDependency.RIGHT);
        mLineChartBooksCurrentCategory.setVisibleYRangeMinimum(1, YAxis.AxisDependency.RIGHT);
        mLineChartBooksCurrentCategory.setVisibleYRangeMinimum(1, YAxis.AxisDependency.LEFT);
        mLineChartBooksCurrentCategory.invalidate();
        mLineChartBooksCurrentCategory.animateY(1400, Easing.EasingOption.EaseInOutQuart);
    }

    private ArrayList<Float> createBooksCountList(BookMonthDivision bookMonthDivision) {
        ArrayList<Float> listOfBookCount = new ArrayList<>();
        switch (mDivisionTypeCurrentCategory) {
            case DivisionType.ONE:
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
                break;
            case DivisionType.THREE:
                listOfBookCount.add(bookMonthDivision.getJanuaryMarch());
                listOfBookCount.add(bookMonthDivision.getAprilJune());
                listOfBookCount.add(bookMonthDivision.getJulySeptember());
                listOfBookCount.add(bookMonthDivision.getOctoberDecember());
                break;
            case DivisionType.SIX:
                listOfBookCount.add(bookMonthDivision.getJanuaryJune());
                listOfBookCount.add(bookMonthDivision.getJulyDecember());
                break;
            case DivisionType.TWELVE:
                listOfBookCount.add(bookMonthDivision.getJanuaryDecember());
                break;
        }
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
        dataSet.setColor(ColorTemplate.rgb("#339064"));
        dataSet.setCircleColor(ColorTemplate.rgb("#339064"));
        dataSet.setLineWidth(1.8f);
        dataSet.setCircleRadius(3.6f);
        dataSet.setValueTextSize(8f);
        dataSet.setValueFormatter(new FloatToIntInsideChartValueFormatter());
    }

    private void defineYearSpinners() {
        mYearSpinnerAdapter = new ArrayAdapter<Integer>(getActivity(), android.R.layout.simple_spinner_dropdown_item);
        for (Year year : mAllYears) {
            mYearSpinnerAdapter.add(year.getYearNumber());
        }
        mYearSpinnerAllBooks.setAdapter(mYearSpinnerAdapter);
        mYearSpinnerAllCategories.setAdapter(mYearSpinnerAdapter);
        mYearSpinnerCurrentCategory.setAdapter(mYearSpinnerAdapter);
    }

    private void defineMonthDivisionSpinners() {
        mMonthDivisionSpinnerAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item);
        mMonthDivisionSpinnerAdapter.addAll(DivisionType.ONE,
                DivisionType.THREE,
                DivisionType.SIX,
                DivisionType.TWELVE);
        mMonthDivisionSpinnerAllBooks.setAdapter(mMonthDivisionSpinnerAdapter);
        mMonthDivisionSpinnerCurrentCategory.setAdapter(mMonthDivisionSpinnerAdapter);
    }

    private void defineMonthTypeSpinner() {
        mMonthTypeSpinnerAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item);
        mMonthTypeSpinnerAdapter.addAll(AllMonth.listOfAllMonth);
        mMonthTypeSpinnerAllCategories.setAdapter(mMonthTypeSpinnerAdapter);
    }

    private void defineCategorySpinner() {
        mCategorySpinnerAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item);
        for (BookCategory bookCategory : mAllBookCategories) {
            mCategorySpinnerAdapter.add(bookCategory.getCategoryName());
        }
        mCategorySpinner.setAdapter(mCategorySpinnerAdapter);
    }

    private void setListenerToYearSpinnerAllBooks() {
        mYearSpinnerAllBooks.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mCurrentYearAllBooksChart = Integer.valueOf(parent.getItemAtPosition(position).toString());
                mDivisionTypeAllBooks = mMonthDivisionSpinnerAllBooks.getSelectedItem().toString();
                RealmResults<AllBookMonthDivision> allBooksMonthDivision = mBooksRealmRepository.getAllBookMonthByYear(MonthIndex.ZERO,
                        getEndIndexForAllBookMonthDivision(),
                        mCurrentYearAllBooksChart);
                allBooksMonthDivision.addChangeListener(new RealmChangeListener<RealmResults<AllBookMonthDivision>>() {
                    @Override
                    public void onChange(RealmResults<AllBookMonthDivision> element) {
                        if (isAdded() && !element.isEmpty()) {
                            createLineChart(element);
                        } else {
                            mLineChartAllBooks.clear();
                            mLineChartAllBooks.setNoDataText("No books here");
                            mLineChartAllBooks.setNoDataTextColor(getResources().getColor(R.color.card_background));
                        }
                    }
                });
            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setListenerToMonthDivisionSpinnerAllBooks() {
        mMonthDivisionSpinnerAllBooks.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (mYearSpinnerAllBooks.getSelectedItem() != null) {
                    mCurrentYearAllBooksChart = Integer.valueOf(mYearSpinnerAllBooks.getSelectedItem().toString());
                }
                mDivisionTypeAllBooks = parent.getItemAtPosition(position).toString();
                mAllBooksMonthDivision = mBooksRealmRepository.getAllBookMonthByYear(MonthIndex.ZERO,
                        getEndIndexForAllBookMonthDivision(),
                        mCurrentYearAllBooksChart);
                mAllBooksMonthDivision.addChangeListener(new RealmChangeListener<RealmResults<AllBookMonthDivision>>() {
                    @Override
                    public void onChange(RealmResults<AllBookMonthDivision> element) {
                        if (isAdded() && !element.isEmpty()) {
                            createLineChart(element);
                        } else {
                            mLineChartAllBooks.clear();
                            mLineChartAllBooks.setNoDataText("No books here");
                            mLineChartAllBooks.setNoDataTextColor(getResources().getColor(R.color.card_background));
                        }
                    }
                });
            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setListenerToYearSpinnerAllCategories() {
        mYearSpinnerAllCategories.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mCurrentYearAllCategoriesChart = Integer.valueOf(parent.getItemAtPosition(position).toString());
                mMonthTypeAllCategories = mMonthTypeSpinnerAllCategories.getSelectedItem().toString();
                RealmResults<BookMonthDivision> bookMonthDivisions = mBooksRealmRepository.getBookMonthDivision(mCurrentYearAllCategoriesChart);
                bookMonthDivisions.addChangeListener(new RealmChangeListener<RealmResults<BookMonthDivision>>() {
                    @Override
                    public void onChange(RealmResults<BookMonthDivision> element) {
                        if (isAdded() && !element.isEmpty()) {
                            createBarChart(element);
                        } else {
                            mBarChartAllCategories.clear();
                            mBarChartAllCategories.setNoDataText("No books here");
                            mBarChartAllCategories.setNoDataTextColor(getResources().getColor(R.color.card_background));
                        }
                    }
                });
            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setListenerToMonthTypeSpinnerAllCategories() {
        mMonthTypeSpinnerAllCategories.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mMonthTypeAllCategories = parent.getItemAtPosition(position).toString();
                if (mYearSpinnerAllCategories.getSelectedItem() != null) {
                    mCurrentYearAllCategoriesChart = Integer.valueOf(mYearSpinnerAllCategories.getSelectedItem().toString());
                }
                RealmResults<BookMonthDivision> bookMonthDivisions = mBooksRealmRepository.getBookMonthDivision(mCurrentYearAllCategoriesChart);
                bookMonthDivisions.addChangeListener(new RealmChangeListener<RealmResults<BookMonthDivision>>() {
                    @Override
                    public void onChange(RealmResults<BookMonthDivision> element) {
                        if (isAdded() && !element.isEmpty()) {
                            createBarChart(element);
                        } else {
                            mBarChartAllCategories.clear();
                            mBarChartAllCategories.setNoDataText("No books here");
                            mBarChartAllCategories.setNoDataTextColor(getResources().getColor(R.color.card_background));
                        }
                    }
                });
            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setListenerToYearSpinnerCurrentCategory() {
        mYearSpinnerCurrentCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mDivisionTypeCurrentCategory = mMonthDivisionSpinnerCurrentCategory.getSelectedItem().toString();
                mCurrentYearCurrentCategory = Integer.valueOf(parent.getItemAtPosition(position).toString());
                mBookMonthDivisionsByCategory = mBooksRealmRepository.getBookMonthDivisionByCategory(mCategoryNameCurrentCategory, mCurrentYearCurrentCategory);
                mBookMonthDivisionsByCategory.addChangeListener(new RealmChangeListener<RealmResults<BookMonthDivision>>() {
                    @Override
                    public void onChange(RealmResults<BookMonthDivision> element) {
                        if (isAdded() && !element.isEmpty()) {
                            createCategoryLineChart(element);
                        } else {
                            mLineChartBooksCurrentCategory.clear();
                            mLineChartBooksCurrentCategory.setNoDataText("No books here");
                            mLineChartBooksCurrentCategory.setNoDataTextColor(getResources().getColor(R.color.card_background));
                        }
                    }
                });
            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setListenerToMonthDivisionSpinnerCurrentCategory() {
        mMonthDivisionSpinnerCurrentCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mDivisionTypeCurrentCategory = parent.getItemAtPosition(position).toString();
                if (mYearSpinnerCurrentCategory.getSelectedItem() != null) {
                    mCurrentYearCurrentCategory = Integer.valueOf(mYearSpinnerCurrentCategory.getSelectedItem().toString());
                }
                mBookMonthDivisionsByCategory = mBooksRealmRepository.getBookMonthDivisionByCategory(mCategoryNameCurrentCategory, mCurrentYearCurrentCategory);
                mBookMonthDivisionsByCategory.addChangeListener(new RealmChangeListener<RealmResults<BookMonthDivision>>() {
                    @Override
                    public void onChange(RealmResults<BookMonthDivision> element) {
                        if (isAdded() && !element.isEmpty()) {
                            createCategoryLineChart(element);
                        } else {
                            mLineChartBooksCurrentCategory.clear();
                            mLineChartBooksCurrentCategory.setNoDataText("No books here");
                            mLineChartBooksCurrentCategory.setNoDataTextColor(getResources().getColor(R.color.card_background));
                        }
                    }
                });
            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setListenerToCategorySpinnerCurrentCategory() {
        mCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mCategoryNameCurrentCategory = parent.getItemAtPosition(position).toString();
                if (mYearSpinnerCurrentCategory.getSelectedItem() != null) {
                    mCurrentYearCurrentCategory = Integer.valueOf(mYearSpinnerCurrentCategory.getSelectedItem().toString());
                }
                mBookMonthDivisionsByCategory = mBooksRealmRepository.getBookMonthDivisionByCategory(mCategoryNameCurrentCategory, mCurrentYearCurrentCategory);
                mBookMonthDivisionsByCategory.addChangeListener(new RealmChangeListener<RealmResults<BookMonthDivision>>() {
                    @Override
                    public void onChange(RealmResults<BookMonthDivision> element) {
                        if (isAdded() && !element.isEmpty()) {
                            createCategoryLineChart(element);
                        } else {
                            mLineChartBooksCurrentCategory.clear();
                            mLineChartBooksCurrentCategory.setNoDataText("No books here");
                            mLineChartBooksCurrentCategory.setNoDataTextColor(getResources().getColor(R.color.card_background));
                        }
                    }
                });
            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private int getEndIndexForAllBookMonthDivision() {
        int endIndex = 0;
        switch (mDivisionTypeAllBooks) {
            case DivisionType.ONE:
                endIndex = 11;
                break;
            case DivisionType.THREE:
                endIndex = 4;
                break;
            case DivisionType.SIX:
                endIndex = 2;
                break;
        }
        return endIndex;
    }

    @Override
    public void onPause() {
        super.onPause();
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
        RefWatcher refWatcher = MyApplication.getRefWatcher(getActivity());
        refWatcher.watch(this);
    }
}
