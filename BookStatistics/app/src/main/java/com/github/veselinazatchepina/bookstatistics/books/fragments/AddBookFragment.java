package com.github.veselinazatchepina.bookstatistics.books.fragments;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.veselinazatchepina.bookstatistics.R;
import com.github.veselinazatchepina.bookstatistics.books.enums.BookRating;
import com.github.veselinazatchepina.bookstatistics.books.enums.BookType;
import com.github.veselinazatchepina.bookstatistics.database.BooksRealmRepository;
import com.github.veselinazatchepina.bookstatistics.database.model.BookCategory;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;


/**
 * AddQuoteFragment is used for input properties of the book. It used for save and edit books.
 */
public class AddBookFragment extends Fragment implements DatePickerDialog.OnDateSetListener {

    private static final String DATE_PICKER_START_DATE = "start date";
    private static final String DATE_PICKER_END_DATE = "end date";

    @BindView(R.id.category_spinner)
    Spinner mCategorySpinner;
    @BindView(R.id.rating_spinner)
    Spinner mRatingSpinner;
    @BindView(R.id.type_spinner)
    Spinner mTypeSpinner;
    @BindView(R.id.date_start_input_layout)
    TextInputLayout mDateStartInputLayout;
    @BindView(R.id.date_end_input_layout)
    TextInputLayout mEndDateInputLayout;
    private Unbinder unbinder;

    private BooksRealmRepository mBooksRealmRepository;
    private RealmResults<BookCategory> mBookCategories;
    private List<String> mAllCategories;
    private ArrayAdapter<String> mSpinnerAdapter;
    private String mSelectedValueOfCategory;
    private String mCurrentCategory;
    private EditText mStartDateEditText;
    private EditText mEndDateEditText;

    public AddBookFragment() { }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        defineInputData(savedInstanceState);
        mBooksRealmRepository = new BooksRealmRepository();
        mBookCategories = mBooksRealmRepository.getListOfBookCategories();
    }

    private void defineInputData(Bundle savedInstanceState) {
//        if (savedInstanceState != null) {
//            mQuoteType = savedInstanceState.getString(QUOTE_TYPE_BUNDLE);
//            mQuoteIdForEdit = savedInstanceState.getLong(QUOTE_ID_BUNDLE);
//            mCurrentCategory = savedInstanceState.getString(QUOTE_CATEGORY_NEW_INSTANCE);
//            mSelectedValueOfCategory = savedInstanceState.getString(QUOTE_CATEGORY_VALUE_SAVE_INSTANCE);
//        } else if (getArguments() != null) {
//            mQuoteIdForEdit = getArguments().getLong(QUOTE_ID_NEW_INSTANCE, -1);
//            mQuoteType = getArguments().getString(QUOTE_TYPE_NEW_INSTANCE);
//            mCurrentCategory = getArguments().getString(QUOTE_CATEGORY_NEW_INSTANCE);
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_add_book, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        mBookCategories.addChangeListener(new RealmChangeListener<RealmResults<BookCategory>>() {
            @Override
            public void onChange(RealmResults<BookCategory> element) {
                defineCategorySpinner(element);
            }
        });
        defineRatingSpinner();
        defineTypeSpinner();
        setListenerToCategorySpinner();
        defineStartDate();
        defineEndDate();

        //fillFieldsWithQuoteEditData(savedInstanceState);
        return rootView;
    }

    private void defineCategorySpinner(RealmResults<BookCategory> element) {
        createBookCategoryListForSpinner(element);
        setCategorySpinnerOnCurrentPosition();
    }

    private void createBookCategoryListForSpinner(List<BookCategory> bookCategories) {
        mAllCategories = new ArrayList<>();
        if (bookCategories != null && !bookCategories.isEmpty()) {
            for (BookCategory currentCategory : bookCategories) {
                if (currentCategory != null) {
                    String category = currentCategory.getCategoryName();
                    mAllCategories.add(category.toUpperCase());
                }
            }
            if (isAdded()) {
                mAllCategories.add(getString(R.string.title_spinner_category_add_category));
                mAllCategories.add(getString(R.string.title_spinner_category));
            }
        } else {
            if (isAdded()) {
                mAllCategories.add(getString(R.string.title_spinner_category_add_category));
                mAllCategories.add(getString(R.string.title_spinner_category));
            }
        }
    }

    private void setCategorySpinnerOnCurrentPosition() {
        if (mSelectedValueOfCategory == null) {
            createSpinnerAdapter();
            if (mCurrentCategory != null) {
                mCategorySpinner.setSelection(mSpinnerAdapter.getPosition(mCurrentCategory.toUpperCase()));
            }
        } else {
            if (!mAllCategories.contains(mSelectedValueOfCategory)) {
                mAllCategories.add(0, mSelectedValueOfCategory);
            }
            // https://ru.stackoverflow.com/questions/660436/
            createSpinnerAdapter();
            createSpinnerAdapter();
            if (isAdded()) {
                if (!mSelectedValueOfCategory.equals(getString(R.string.title_spinner_category))) {
                    mCategorySpinner.setSelection(mSpinnerAdapter.getPosition(mSelectedValueOfCategory));
                }
            }
        }
    }

    private void defineRatingSpinner() {
        ArrayAdapter<Integer> ratingSpinnerAdapter = new ArrayAdapter<Integer>(getActivity(), android.R.layout.simple_spinner_dropdown_item);
        ratingSpinnerAdapter.addAll(BookRating.FIVE_STARS,
                BookRating.FOUR_STARS,
                BookRating.THREE_STARS,
                BookRating.TWO_STARS,
                BookRating.ONE_STAR);
        mRatingSpinner.setAdapter(ratingSpinnerAdapter);
    }

    private void defineTypeSpinner() {
        ArrayAdapter<String> typeSpinnerAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item);
        typeSpinnerAdapter.addAll(BookType.NEW_BOOK, BookType.CURRENT_BOOK, BookType.READ_BOOK);
        mTypeSpinner.setAdapter(typeSpinnerAdapter);
    }

    private void defineStartDate() {
        mStartDateEditText = mDateStartInputLayout.getEditText();
        mStartDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createDatePickerDialog().show(getActivity().getFragmentManager(), DATE_PICKER_START_DATE);
            }
        });
    }

    private void defineEndDate() {
        mEndDateEditText = mEndDateInputLayout.getEditText();
        mEndDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createDatePickerDialog().show(getActivity().getFragmentManager(), DATE_PICKER_END_DATE);
            }
        });
    }

    private DatePickerDialog createDatePickerDialog() {
        Calendar now = Calendar.getInstance();
        DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(
                AddBookFragment.this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.setVersion(DatePickerDialog.Version.VERSION_2);
        return datePickerDialog;
    }


//    private void fillFieldsWithQuoteEditData(final Bundle savedInstanceState) {
//        if (mQuoteIdForEdit != -1) {
//            mQuoteTexts.addChangeListener(new RealmChangeListener<RealmResults<QuoteText>>() {
//                @Override
//                public void onChange(RealmResults<QuoteText> element) {
//                    if (element.size() > 0) {
//                        FillViewsWithCurrentQuoteDataHelper.fillViewsWithCurrentQuoteData(element,
//                                mQuoteText, mBookName, mAuthorName, mPageNumber, mPublishName, mYearNumber, mQuoteType);
//                        setSpinnerSelectionOnCurrentCategory(element);
//                    }
//                    updateFragmentFieldsWhenRotate(savedInstanceState);
//                }
//            });
//        }
//    }
//
//    private void setSpinnerSelectionOnCurrentCategory(RealmResults<QuoteText> element) {
//        QuoteText quote = element.first();
//        String currentQuoteCategory = quote.getCategory().getCategoryName();
//        if (mAllCategories != null && !mAllCategories.isEmpty()) {
//            mCategorySpinner.setSelection(mSpinnerAdapter.getPosition(currentQuoteCategory.toUpperCase()));
//        }
 //   }

//    private void updateFragmentFieldsWhenRotate(Bundle savedInstanceState) {
//        if (savedInstanceState != null) {
//            mQuoteText.setText(savedInstanceState.getString(QUOTE_TEXT_SAVE_INSTANCE));
//            mBookName.setText(savedInstanceState.getString(QUOTE_BOOK_NAME_SAVE_INSTANCE));
//            mAuthorName.setText(savedInstanceState.getString(QUOTE_BOOK_AUTHOR_SAVE_INSTANCE));
//            mPageNumber.setText(savedInstanceState.getString(QUOTE_PAGE_SAVE_INSTANCE));
//            mYearNumber.setText(savedInstanceState.getString(QUOTE_YEAR_SAVE_INSTANCE));
//            mPublishName.setText(savedInstanceState.getString(QUOTE_PUBLISHER_NAME_SAVE_INSTANCE));
//            updateSpinnerSelection();
//        }
//    }

//    private void updateSpinnerSelection() {
//        if (mSelectedValueOfCategory != null) {
//            if (!mAllCategories.contains(mSelectedValueOfCategory)) {
//                mAllCategories.add(0, mSelectedValueOfCategory);
//            }
//            createSpinnerAdapter();
//            if (isAdded()) {
//                if (!mSelectedValueOfCategory.equals(getString(R.string.title_spinner_category))) {
//                    mCategorySpinner.setSelection(mSpinnerAdapter.getPosition(mSelectedValueOfCategory));
//                }
//            }
//        }
//        if (mCurrentCategory != null) {
//            mCategorySpinner.setSelection(mSpinnerAdapter.getPosition(mCurrentCategory.toUpperCase()));
//        }
//    }

    private void setListenerToCategorySpinner() {
        mCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                final String selectedItem = parent.getItemAtPosition(position).toString();
                if (selectedItem.equals(getString(R.string.title_spinner_category_add_category))) {
                    createAddCategoryDialog();
                } else {
                    mSelectedValueOfCategory = selectedItem;
                }
            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void createAddCategoryDialog() {
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View dialogView = layoutInflater.inflate(R.layout.dialog_add_category, null);
        AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(getActivity());
        mDialogBuilder.setView(dialogView);
        final EditText userInput = (EditText) dialogView.findViewById(R.id.input_text);
        mDialogBuilder
                .setCancelable(false)
                .setPositiveButton(getString(R.string.dialog_add_category_ok),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                String currentUserInput = userInput.getText().toString();
                                mAllCategories.add(0, currentUserInput);
                                mSpinnerAdapter.clear();
                                mSpinnerAdapter.addAll(mAllCategories);
                                mCategorySpinner.setSelection(0);
                                mSelectedValueOfCategory = currentUserInput;
                            }
                        })
                .setNegativeButton(getString(R.string.dialog_add_category_cancel),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                setCategorySpinnerOnCurrentPosition();
                            }
                        });
        AlertDialog alertDialog = mDialogBuilder.create();
        alertDialog.show();
    }

    private void createSpinnerAdapter() {
        if (isAdded()) {
            // Set hint for mCategorySpinner
            mSpinnerAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item) {
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    View v = super.getView(position, convertView, parent);
                    if (position == getCount()) {
                        ((TextView) v.findViewById(android.R.id.text1)).setText("");
                        ((TextView) v.findViewById(android.R.id.text1)).setHint(getItem(getCount()));
                    }
                    return v;
                }

                @Override
                public int getCount() {
                    return super.getCount() - 1;
                }
            };
        }
        mSpinnerAdapter.addAll(mAllCategories);
        mCategorySpinner.setAdapter(mSpinnerAdapter);
        mCategorySpinner.setSelection(mSpinnerAdapter.getCount());
    }

    /**
     * Method checks if user choose hint in mCategorySpinner.
     *
     * @return true if user choose hint and false else if.
     */
//    public boolean isSpinnerSelectedItemHint() {
//        return mSelectedValueOfCategory.equals(getString(R.string.spinner_hint));
//    }

    /**
     * Method checks if main EditText is empty or not.
     *
     * @return false if EditText not empty and true if else.
     */
//    public boolean isEditTextEmpty() {
//        mCurrentQuoteText = mQuoteText.getText().toString();
//        if (TextUtils.isEmpty(mCurrentQuoteText)) {
//            mQuoteTextInputLayout.setError(getString(R.string.quote_text_empty_error));
//            return true;
//        }
//        if (isAdded()) {
//            if (!mQuoteType.equals(Types.MY_QUOTE)) {
//                mCurrentAuthorName = mAuthorName.getText().toString();
//                if (TextUtils.isEmpty(mCurrentAuthorName)) {
//                    mAuthorNameInputLayout.setError(getString(R.string.author_name_empty_error));
//                    return true;
//                }
//            }
//        }
//        return false;
//    }

//    public boolean isNumbersPositive() {
//        return isCurrentNumberPositive(mPageNumber, mPageNumberInputLayout) &&
//                isCurrentNumberPositive(mYearNumber, mYearNumberInputLayout);
//    }
//
//    private boolean isCurrentNumberPositive(EditText editText, TextInputLayout textInputLayout) {
//        String currentValue = editText.getText().toString();
//        if (!currentValue.isEmpty() && !currentValue.equals(getString(R.string.default_value)) && Integer.valueOf(currentValue) < 0) {
//            textInputLayout.setError(getString(R.string.page_number_error));
//            return false;
//        }
//        return true;
//    }

    /**
     * Method creates quote properties map for QuoteCreator class and pass map to it.
     */
//    public void createMapOfQuoteProperties() {
//        Calendar currentCreateDate = Calendar.getInstance();
//        String currentDate = String.format("%1$td %1$tb %1$tY", currentCreateDate);
//        HashMap<QuotePropertiesEnum, String> mapOfQuoteProperties = new HashMap<>();
//        mapOfQuoteProperties.put(QuotePropertiesEnum.QUOTE_TEXT, mCurrentQuoteText);
//        mapOfQuoteProperties.put(QuotePropertiesEnum.QUOTE_CATEGORY, mSelectedValueOfCategory);
//        mapOfQuoteProperties.put(QuotePropertiesEnum.QUOTE_CREATION_DATE, currentDate);
//        mapOfQuoteProperties.put(QuotePropertiesEnum.QUOTE_TYPE, mQuoteType);
//        final String currentPageNumber;
//        final String currentYearNumber;
//        final String currentPublishName;
//        if (!mQuoteType.equals(Types.MY_QUOTE)) {
//            currentPageNumber = mPageNumber.getText().toString();
//            currentYearNumber = mYearNumber.getText().toString();
//            currentPublishName = mPublishName.getText().toString();
//            mCurrentBookName = mBookName.getText().toString();
//            mapOfQuoteProperties.put(QuotePropertiesEnum.BOOK_NAME, emptyTextCheck(mCurrentBookName));
//            mapOfQuoteProperties.put(QuotePropertiesEnum.BOOK_AUTHOR, mCurrentAuthorName);
//            mapOfQuoteProperties.put(QuotePropertiesEnum.PAGE_NUMBER, emptyTextCheck(currentPageNumber));
//            mapOfQuoteProperties.put(QuotePropertiesEnum.YEAR_NUMBER, emptyTextCheck(currentYearNumber));
//            mapOfQuoteProperties.put(QuotePropertiesEnum.PUBLISHER_NAME, emptyTextCheck(currentPublishName));
//        }
//        if (mQuoteIdForEdit != -1) {
//            mQuoteDataRepository.saveChangedQuote(mQuoteIdForEdit, mapOfQuoteProperties);
//        } else {
//            mQuoteDataRepository.saveQuote(mapOfQuoteProperties);
//        }
//    }

    /**
     * Method checks is current value empty or equals "" and set "-" if it is.
     *
 //    * @param currentValue
     * @return current value
     */
//    private String emptyTextCheck(String currentValue) {
//        if (currentValue.isEmpty() || currentValue.equals("")) {
//            currentValue = getString(R.string.default_value);
//        }
//        return currentValue;
//    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
//        outState.putString(QUOTE_TYPE_BUNDLE, mQuoteType);
//        outState.putLong(QUOTE_ID_BUNDLE, mQuoteIdForEdit);
//        outState.putString(QUOTE_CATEGORY_NEW_INSTANCE, mCurrentCategory);
//        outState.putString(QUOTE_TEXT_SAVE_INSTANCE, mQuoteText.getText().toString());
//        outState.putString(QUOTE_BOOK_NAME_SAVE_INSTANCE, mBookName.getText().toString());
//        outState.putString(QUOTE_BOOK_AUTHOR_SAVE_INSTANCE, mAuthorName.getText().toString());
//        outState.putString(QUOTE_PUBLISHER_NAME_SAVE_INSTANCE, mPublishName.getText().toString());
//        outState.putString(QUOTE_YEAR_SAVE_INSTANCE, mYearNumber.getText().toString());
//        outState.putString(QUOTE_PAGE_SAVE_INSTANCE, mPageNumber.getText().toString());
//        outState.putString(QUOTE_CATEGORY_VALUE_SAVE_INSTANCE, mSelectedValueOfCategory);
    }

    public static AddBookFragment newInstance() {
        //Bundle args = new Bundle();
        AddBookFragment fragment = new AddBookFragment();
        //fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onPause() {
        super.onPause();
//        if (mAlertDialog != null && mAlertDialog.isShowing()) {
//            mAlertDialog.dismiss();
//            mAlertDialog = null;
//        }
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

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String date = dayOfMonth+"/"+(++monthOfYear)+"/"+year;
        if (view.getTag().equals(DATE_PICKER_START_DATE)) {
            mStartDateEditText.setText(date);
        } else {
            mEndDateEditText.setText(date);
        }
    }
}
