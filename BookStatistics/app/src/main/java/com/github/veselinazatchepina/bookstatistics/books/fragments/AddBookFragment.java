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
import com.github.veselinazatchepina.bookstatistics.books.enums.BookPropertiesEnum;
import com.github.veselinazatchepina.bookstatistics.books.enums.BookRatingEnums;
import com.github.veselinazatchepina.bookstatistics.books.enums.BookTypeEnums;
import com.github.veselinazatchepina.bookstatistics.database.BooksRealmRepository;
import com.github.veselinazatchepina.bookstatistics.database.model.Book;
import com.github.veselinazatchepina.bookstatistics.database.model.BookCategory;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
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
    private static final String CURRENT_BOOK_ID = "current_book_id";


    @BindView(R.id.category_spinner)
    Spinner mCategorySpinner;
    @BindView(R.id.rating_spinner)
    Spinner mRatingSpinner;
    @BindView(R.id.type_spinner)
    Spinner mTypeSpinner;
    @BindView(R.id.book_name)
    EditText mBookName;
    @BindView(R.id.book_author)
    EditText mBookAuthor;
    @BindView(R.id.book_page)
    EditText mBookPage;
    @BindView(R.id.book_page_input_layout)
    TextInputLayout mBookPageInputLayout;
    @BindView(R.id.date_start_input_layout)
    TextInputLayout mDateStartInputLayout;
    @BindView(R.id.date_end_input_layout)
    TextInputLayout mEndDateInputLayout;
    private Unbinder unbinder;

    private BooksRealmRepository mBooksRealmRepository;
    private RealmResults<BookCategory> mBookCategories;
    private List<String> mAllCategories;
    private ArrayAdapter<String> mCategorySpinnerAdapter;
    ArrayAdapter<Integer> mRatingSpinnerAdapter;
    ArrayAdapter<String> mTypeSpinnerAdapter;
    private String mSelectedValueOfCategory;
    private String mCurrentCategory;
    private EditText mStartDateEditText;
    private EditText mEndDateEditText;
    private long mCurrentBookIdForEdit;
    private RealmResults<Book> mBookForEdit;

    public AddBookFragment() { }

    public static AddBookFragment newInstance(long currentBookId) {
        Bundle args = new Bundle();
        args.putLong(CURRENT_BOOK_ID, currentBookId);
        AddBookFragment fragment = new AddBookFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        defineInputData(savedInstanceState);
        mBooksRealmRepository = new BooksRealmRepository();
        mBookCategories = mBooksRealmRepository.getListOfBookCategories();
        getBookForEditById();
    }

    private void defineInputData(Bundle savedInstanceState) {
        if (getArguments() != null) {
            mCurrentBookIdForEdit = getArguments().getLong(CURRENT_BOOK_ID, -1);
        }
    }

    private void getBookForEditById() {
        if (mCurrentBookIdForEdit != -1) {
            mBookForEdit = mBooksRealmRepository.getBookById(mCurrentBookIdForEdit);
        }
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
        if (mCurrentBookIdForEdit != -1) {
            fillFieldsWithBookEditData(savedInstanceState);
        }
        return rootView;
    }

    private void defineCategorySpinner(RealmResults<BookCategory> element) {
        if (isAdded()) {
            createBookCategoryListForSpinner(element);
            createSpinnerAdapter();
        }
        //setCategorySpinnerOnCurrentPosition();
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
        //if (mSelectedValueOfCategory == null) {
            createSpinnerAdapter();
//            if (mCurrentCategory != null) {
//                mCategorySpinner.setSelection(mSpinnerAdapter.getPosition(mCurrentCategory.toUpperCase()));
//            }
//        } else {
//            if (!mAllCategories.contains(mSelectedValueOfCategory)) {
//                mAllCategories.add(0, mSelectedValueOfCategory);
//            }
//            // https://ru.stackoverflow.com/questions/660436/
//            createSpinnerAdapter();
//            createSpinnerAdapter();
//            if (isAdded()) {
//                if (!mSelectedValueOfCategory.equals(getString(R.string.title_spinner_category))) {
//                    mCategorySpinner.setSelection(mSpinnerAdapter.getPosition(mSelectedValueOfCategory));
//                }
//            }
//        }
    }

    private void defineRatingSpinner() {
        mRatingSpinnerAdapter = new ArrayAdapter<Integer>(getActivity(), android.R.layout.simple_spinner_dropdown_item);
        mRatingSpinnerAdapter.addAll(BookRatingEnums.FIVE_STARS,
                BookRatingEnums.FOUR_STARS,
                BookRatingEnums.THREE_STARS,
                BookRatingEnums.TWO_STARS,
                BookRatingEnums.ONE_STAR);
        mRatingSpinner.setAdapter(mRatingSpinnerAdapter);
    }

    private void defineTypeSpinner() {
        mTypeSpinnerAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item);
        mTypeSpinnerAdapter.addAll(BookTypeEnums.NEW_BOOK, BookTypeEnums.CURRENT_BOOK, BookTypeEnums.READ_BOOK);
        mTypeSpinner.setAdapter(mTypeSpinnerAdapter);
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

    private void fillFieldsWithBookEditData(final Bundle savedInstanceState) {
        if (mCurrentBookIdForEdit != -1) {
            mBookForEdit.addChangeListener(new RealmChangeListener<RealmResults<Book>>() {
                @Override
                public void onChange(RealmResults<Book> element) {
                    if (element.size() > 0) {
                        setBookDataToFields(element);
                    }
                    //updateFragmentFieldsWhenRotate(savedInstanceState);
                }
            });
        }
    }

    private void setBookDataToFields(RealmResults<Book> element) {
        Book book = element.first();
        if (isAdded()) {
            mBookName.setText(book.getBookName());
            mBookAuthor.setText(book.getAuthorName());
            mRatingSpinner.setSelection(mRatingSpinnerAdapter.getPosition(book.getRating().getStarsCount()));
            mBookPage.setText(String.valueOf(book.getPageCount()));
            setCategorySpinnerSelectionOnCurrentCategory(element);
            mTypeSpinner.setSelection(mTypeSpinnerAdapter.getPosition(book.getSection().getSectionName()));
            mDateStartInputLayout.getEditText().setText(book.getDateStart());
            mEndDateInputLayout.getEditText().setText(book.getDateEnd());
        }
    }

    private void setCategorySpinnerSelectionOnCurrentCategory(RealmResults<Book> element) {
        Book book = element.first();
        String currentBookCategory = book.getBookCategory().getCategoryName();
        if (mAllCategories != null && !mAllCategories.isEmpty()) {
            mCategorySpinner.setSelection(mCategorySpinnerAdapter.getPosition(currentBookCategory.toUpperCase()));
        }
    }

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
                                mCategorySpinnerAdapter.clear();
                                mCategorySpinnerAdapter.addAll(mAllCategories);
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
            mCategorySpinnerAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item) {
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
        mCategorySpinnerAdapter.addAll(mAllCategories);
        mCategorySpinner.setAdapter(mCategorySpinnerAdapter);
        mCategorySpinner.setSelection(mCategorySpinnerAdapter.getCount());
    }

    /*
     * Method checks if user choose hint in category spinner.
     *
     * @return true if user choose hint and false else if.
     */
    public boolean isSpinnerSelectedItemHint() {
        return mSelectedValueOfCategory.equals(getString(R.string.error_category_spinner));
    }

    public boolean isPagePositiveNumber() {
        return isCurrentNumberPositive(mBookPage, mBookPageInputLayout);
    }

    private boolean isCurrentNumberPositive(EditText editText, TextInputLayout textInputLayout) {
        String currentValue = editText.getText().toString();
        if (!currentValue.isEmpty() && !currentValue.equals(getString(R.string.default_value)) && Integer.valueOf(currentValue) < 0) {
            textInputLayout.setError(getString(R.string.error_page_number));
            return false;
        }
        return true;
    }

    /**
     * Method creates book properties map for save in db.
     */
    public void createMapOfBookProperties() {
        HashMap<BookPropertiesEnum, String> mapOfBookProperties = new HashMap<>();
        mapOfBookProperties.put(BookPropertiesEnum.BOOK_NAME, mBookName.getText().toString());
        mapOfBookProperties.put(BookPropertiesEnum.BOOK_AUTHOR, mBookAuthor.getText().toString());
        mapOfBookProperties.put(BookPropertiesEnum.BOOK_RATING, mRatingSpinner.getSelectedItem().toString());
        mapOfBookProperties.put(BookPropertiesEnum.BOOK_PAGE, mBookPage.getText().toString());
        mapOfBookProperties.put(BookPropertiesEnum.BOOK_CATEGORY, mCategorySpinner.getSelectedItem().toString().toLowerCase());
        mapOfBookProperties.put(BookPropertiesEnum.BOOK_TYPE, mTypeSpinner.getSelectedItem().toString());
        mapOfBookProperties.put(BookPropertiesEnum.BOOK_DATE_START, mStartDateEditText.getText().toString());
        mapOfBookProperties.put(BookPropertiesEnum.BOOK_DATE_END, mEndDateEditText.getText().toString());

        if (mCurrentBookIdForEdit != -1) {
            mBooksRealmRepository.saveChangedBook(mCurrentBookIdForEdit, mapOfBookProperties);
        } else {
            mBooksRealmRepository.saveQuote(mapOfBookProperties);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

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
