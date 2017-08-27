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

import com.github.veselinazatchepina.bookstatistics.MyApplication;
import com.github.veselinazatchepina.bookstatistics.R;
import com.github.veselinazatchepina.bookstatistics.books.enums.BookPropertiesEnum;
import com.github.veselinazatchepina.bookstatistics.books.enums.BookRatingEnums;
import com.github.veselinazatchepina.bookstatistics.database.BooksRealmRepository;
import com.squareup.leakcanary.RefWatcher;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ChangeRatingDialogFragment extends DialogFragment {

    private static final String CURRENT_BOOK_ID = "current_book_id";

    @BindView(R.id.rating_spinner_for_change)
    Spinner mRatingSpinner;
    private Unbinder unbinder;

    ArrayAdapter<Integer> mRatingSpinnerAdapter;
    private long mCurrentBookIdForEdit;
    private BooksRealmRepository mBooksRealmRepository;

    public ChangeRatingDialogFragment() {
    }

    public static ChangeRatingDialogFragment newInstance(long currentBookId) {
        Bundle args = new Bundle();
        args.putLong(CURRENT_BOOK_ID, currentBookId);
        ChangeRatingDialogFragment fragment = new ChangeRatingDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View dialogView = layoutInflater.inflate(R.layout.dialog_change_rating, null);
        unbinder = ButterKnife.bind(this, dialogView);
        defineInputData();
        defineRatingSpinner();
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
        dialogBuilder.setTitle(R.string.title_change_rating);
        dialogBuilder
                .setCancelable(false)
                .setPositiveButton(getString(R.string.dialog_change_rating_ok_button),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                mBooksRealmRepository.updateBookRatingInTransaction(mCurrentBookIdForEdit,
                                        createMapOfProperties());
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

    private void defineRatingSpinner() {
        mRatingSpinnerAdapter = new ArrayAdapter<Integer>(getActivity(), android.R.layout.simple_spinner_dropdown_item);
        mRatingSpinnerAdapter.addAll(BookRatingEnums.FIVE_STARS,
                BookRatingEnums.FOUR_STARS,
                BookRatingEnums.THREE_STARS,
                BookRatingEnums.TWO_STARS,
                BookRatingEnums.ONE_STAR);
        mRatingSpinner.setAdapter(mRatingSpinnerAdapter);
        mRatingSpinner.setSelection(mRatingSpinnerAdapter.getPosition(mBooksRealmRepository
                .getBookById(mCurrentBookIdForEdit)
                .first()
                .getRating()
                .getStarsCount()));
    }

    private HashMap<BookPropertiesEnum, String> createMapOfProperties() {
        HashMap<BookPropertiesEnum, String> mapOfBookProperties = new HashMap<>();
        mapOfBookProperties.put(BookPropertiesEnum.BOOK_RATING, mRatingSpinner.getSelectedItem().toString());
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
