package com.github.veselinazatchepina.bookstatistics.books.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.veselinazatchepina.bookstatistics.R;
import com.github.veselinazatchepina.bookstatistics.database.BooksRealmRepository;
import com.github.veselinazatchepina.bookstatistics.database.model.Book;

import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class CurrentBookFragment extends Fragment {

    private static final String CURRENT_BOOK_ID = "current_book_id";

    private long mCurrentBookId;
    private BooksRealmRepository mBooksRealmRepository;
    private RealmResults<Book> mBookWithCurrentId;

    public CurrentBookFragment() {
    }

    public static CurrentBookFragment newInstance(long currentBookId) {
        Bundle args = new Bundle();
        args.putLong(CURRENT_BOOK_ID, currentBookId);
        CurrentBookFragment fragment = new CurrentBookFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        defineInputData(savedInstanceState);
        mBooksRealmRepository = new BooksRealmRepository();
        if (mCurrentBookId != -1) {
            mBookWithCurrentId = mBooksRealmRepository.getBookById(mCurrentBookId);
        }
    }

    private void defineInputData(Bundle savedInstanceState) {
        if (getArguments() != null) {
            mCurrentBookId = getArguments().getLong(CURRENT_BOOK_ID);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_current_book, container, false);
        mBookWithCurrentId.addChangeListener(new RealmChangeListener<RealmResults<Book>>() {
            @Override
            public void onChange(RealmResults<Book> element) {

            }
        });
        return rootView;
    }
}
