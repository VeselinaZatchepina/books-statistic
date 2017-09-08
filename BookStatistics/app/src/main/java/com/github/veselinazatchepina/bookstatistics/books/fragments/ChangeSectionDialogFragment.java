package com.github.veselinazatchepina.bookstatistics.books.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.github.veselinazatchepina.bookstatistics.MyApplication;
import com.github.veselinazatchepina.bookstatistics.R;
import com.github.veselinazatchepina.bookstatistics.books.enums.BookPropertiesEnum;
import com.github.veselinazatchepina.bookstatistics.books.enums.BookTypeEnums;
import com.github.veselinazatchepina.bookstatistics.database.BooksRealmRepository;
import com.github.veselinazatchepina.bookstatistics.database.model.Book;
import com.squareup.leakcanary.RefWatcher;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class ChangeSectionDialogFragment extends DialogFragment {

    private static final String CURRENT_BOOK_ID = "current_book_id";

    @BindView(R.id.section_spinner_for_change)
    Spinner mTypeSpinner;
    private Unbinder unbinder;

    ArrayAdapter<String> mTypeSpinnerAdapter;
    private long mCurrentBookIdForEdit;
    private BooksRealmRepository mBooksRealmRepository;

    public ChangeSectionDialogFragment() {
    }

    public static ChangeSectionDialogFragment newInstance(long currentBookId) {
        Bundle args = new Bundle();
        args.putLong(CURRENT_BOOK_ID, currentBookId);
        ChangeSectionDialogFragment fragment = new ChangeSectionDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View dialogView = layoutInflater.inflate(R.layout.dialog_change_section, null);
        unbinder = ButterKnife.bind(this, dialogView);
        defineInputData();
        defineSectionSpinner();
        return defineAlterDialogBuilder(dialogView).create();
    }

    private void defineInputData() {
        if (getArguments() != null) {
            mCurrentBookIdForEdit = getArguments().getLong(CURRENT_BOOK_ID, -1);
        }
        mBooksRealmRepository = new BooksRealmRepository();
    }

    private AlertDialog.Builder defineAlterDialogBuilder(View dialogView) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        dialogBuilder.setView(dialogView);
        dialogBuilder.setTitle(R.string.title_change_section);
        dialogBuilder
                .setCancelable(false)
                .setPositiveButton(getString(R.string.dialog_change_section_ok_button),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Book book = mBooksRealmRepository.getBookById(mCurrentBookIdForEdit).first();
                                if (book.getDateStart().equals("") || book.getDateEnd().equals("") && mTypeSpinner.getSelectedItem().toString().equals(BookTypeEnums.READ_BOOK)) {
                                    Toast.makeText(getActivity(), "You need to fill start and end date of reading", Toast.LENGTH_LONG).show();
                                } else {
                                    mBooksRealmRepository.saveChangedBook(mCurrentBookIdForEdit,
                                            createMapOfProperties());
                                }
                            }
                        })
                .setNegativeButton(getString(R.string.dialog_cancel_button),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        return dialogBuilder;
    }

    private void defineSectionSpinner() {
        mTypeSpinnerAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item);
        mTypeSpinnerAdapter.addAll(BookTypeEnums.NEW_BOOK, BookTypeEnums.CURRENT_BOOK, BookTypeEnums.READ_BOOK);
        mTypeSpinner.setAdapter(mTypeSpinnerAdapter);
        mTypeSpinner.setSelection(mTypeSpinnerAdapter.getPosition(mBooksRealmRepository
                .getBookById(mCurrentBookIdForEdit)
                .first()
                .getSection()
                .getSectionName()));
    }

    private HashMap<BookPropertiesEnum, String> createMapOfProperties() {
        HashMap<BookPropertiesEnum, String> mapOfBookProperties = new HashMap<>();
        Book book = mBooksRealmRepository.getBookById(mCurrentBookIdForEdit).first();
        mapOfBookProperties.put(BookPropertiesEnum.BOOK_NAME, book.getBookName());
        mapOfBookProperties.put(BookPropertiesEnum.BOOK_AUTHOR, book.getAuthorName());
        mapOfBookProperties.put(BookPropertiesEnum.BOOK_RATING, String.valueOf(book.getRating().getStarsCount()));
        mapOfBookProperties.put(BookPropertiesEnum.BOOK_PAGE, String.valueOf(book.getPageCount()));
        mapOfBookProperties.put(BookPropertiesEnum.BOOK_CATEGORY, book.getBookCategory().getCategoryName());
        mapOfBookProperties.put(BookPropertiesEnum.BOOK_TYPE, mTypeSpinner.getSelectedItem().toString());
        mapOfBookProperties.put(BookPropertiesEnum.BOOK_DATE_START,book.getDateStart());
        mapOfBookProperties.put(BookPropertiesEnum.BOOK_DATE_END, book.getDateEnd());
        return mapOfBookProperties;
    }

    @Override
    public void onStart() {
        super.onStart();
        ((AlertDialog) getDialog()).getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.book_accent_background));
        ((AlertDialog) getDialog()).getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.book_accent_background));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RefWatcher refWatcher = MyApplication.getRefWatcher(getActivity());
        refWatcher.watch(this);
    }
}
