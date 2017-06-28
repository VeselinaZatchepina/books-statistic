package com.github.veselinazatchepina.bookstatistics.books.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.veselinazatchepina.bookstatistics.R;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public class BookSectionFragment extends Fragment {

    private static final String CURRENT_BOOK_SECTION = "current_book_section";

    private Unbinder unbinder;

    public BookSectionFragment() { }

    public static BookSectionFragment newInstance(String sectionType) {
        Bundle args = new Bundle();
        args.putString(CURRENT_BOOK_SECTION, sectionType);
        BookSectionFragment fragment = new BookSectionFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_book_section, container, false);
        unbinder = ButterKnife.bind(this, rootView);

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
