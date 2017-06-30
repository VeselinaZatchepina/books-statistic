package com.github.veselinazatchepina.bookstatistics.books.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.veselinazatchepina.bookstatistics.R;
import com.github.veselinazatchepina.bookstatistics.database.BooksRealmRepository;
import com.github.veselinazatchepina.bookstatistics.database.model.Book;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class CurrentBookFragment extends Fragment {

    private static final String CURRENT_BOOK_ID = "current_book_id";

    @BindView(R.id.current_book_name)
    TextView mCurrentBookName;
    @BindView(R.id.current_book_author)
    TextView mCurrentBookAuthor;
    @BindView(R.id.current_book_category)
    TextView mCurrentBookCategory;
    @BindView(R.id.current_book_section)
    TextView mCurrentBookSection;
    @BindView(R.id.current_book_pages)
    TextView mCurrentBookPages;
    @BindView(R.id.current_book_date_start)
    TextView mCurrentBookDateStart;
    @BindView(R.id.current_book_date_end)
    TextView mCurrentBookDateEnd;
    @BindView(R.id.current_book_rating)
    TextView mCurrentBookRating;
    private Unbinder unbinder;

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
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_current_book, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        mBookWithCurrentId.addChangeListener(new RealmChangeListener<RealmResults<Book>>() {
            @Override
            public void onChange(RealmResults<Book> element) {
                Book currentBook = element.first();
                mCurrentBookName.setText(currentBook.getBookName());
                mCurrentBookAuthor.setText(currentBook.getAuthorName());
                mCurrentBookCategory.setText(currentBook.getBookCategory().getCategoryName());
                mCurrentBookSection.setText(currentBook.getSection().getSectionName());
                mCurrentBookPages.setText(String.valueOf(currentBook.getPageCount()));
                mCurrentBookDateStart.setText(currentBook.getDateStart());
                mCurrentBookDateEnd.setText(currentBook.getDateEnd());
                mCurrentBookRating.setText(String.valueOf(currentBook.getRating().getStarsCount()));
            }
        });
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
