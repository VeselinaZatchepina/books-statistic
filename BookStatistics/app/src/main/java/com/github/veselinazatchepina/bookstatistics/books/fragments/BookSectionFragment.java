package com.github.veselinazatchepina.bookstatistics.books.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import io.realm.OrderedRealmCollection;
import io.realm.RealmChangeListener;
import io.realm.RealmRecyclerViewAdapter;
import io.realm.RealmResults;

public class BookSectionFragment extends Fragment {

    private static final String CURRENT_BOOK_SECTION = "current_book_section";
    private static final String CURRENT_BOOK_CATEGORY = "current_book_category";

    @BindView(R.id.title_current_category)
    TextView mTitleCurrentCategory;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    private Unbinder unbinder;
    private String mCurrentSectionType;
    private String mCurrentCategory;
    private BooksRealmRepository mBooksRealmRepository;
    private RealmResults<Book> mBooksInCurrentSection;
    BookSectionRecyclerViewAdapter mBookSectionRecyclerViewAdapter;

    public BookSectionFragment() { }

    public static BookSectionFragment newInstance(String sectionType, String currentCategory) {
        Bundle args = new Bundle();
        args.putString(CURRENT_BOOK_SECTION, sectionType);
        args.putString(CURRENT_BOOK_CATEGORY, currentCategory);
        BookSectionFragment fragment = new BookSectionFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        defineInputData(savedInstanceState);
        mBooksRealmRepository = new BooksRealmRepository();
        mBooksInCurrentSection = mBooksRealmRepository.getAllBooksInCurrentSection(mCurrentSectionType, mCurrentCategory);
    }

    private void defineInputData(Bundle savedInstanceState) {
//        if (savedInstanceState != null) {
//            mQuoteType = savedInstanceState.getString(QUOTE_TYPE_BUNDLE);
//        } else
        if (getArguments() != null) {
            mCurrentSectionType = getArguments().getString(CURRENT_BOOK_SECTION);
            mCurrentCategory = getArguments().getString(CURRENT_BOOK_CATEGORY);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_recycler_view, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        setCurrentCategoryTitleIsGone();
        defineRecyclerView();
        mBooksInCurrentSection.addChangeListener(new RealmChangeListener<RealmResults<Book>>() {
            @Override
            public void onChange(RealmResults<Book> element) {
                mBookSectionRecyclerViewAdapter.mData = element;
            }
        });
        return rootView;
    }

    private void setCurrentCategoryTitleIsGone() {
        mTitleCurrentCategory.setVisibility(View.GONE);
    }

    private void defineRecyclerView() {
        mBookSectionRecyclerViewAdapter = new BookSectionRecyclerViewAdapter(getActivity(), mBooksInCurrentSection, true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mBookSectionRecyclerViewAdapter);
    }

    class BookSectionRecyclerViewAdapter extends RealmRecyclerViewAdapter<Book, BookSectionRecyclerViewAdapter.MyViewHolder> {
        private static final int EMPTY_LIST = 0;
        private static final int NOT_EMPTY_LIST = 1;

        private OrderedRealmCollection<Book> mData;

        BookSectionRecyclerViewAdapter(@NonNull Context context, @Nullable OrderedRealmCollection<Book> data, boolean autoUpdate) {
            super(context, data, autoUpdate);
            mData = data;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            switch (viewType) {
                case EMPTY_LIST:
                    View itemViewEmpty = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.fragment_empty_recycler_view, parent, false);
                    return new MyViewHolder(itemViewEmpty);
                case NOT_EMPTY_LIST:
                    View itemView = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.book_section_recycler_view_item, parent, false);
                    return new MyViewHolder(itemView);
            }
            return null;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            if (!mData.isEmpty()) {
                Book currentBook = mData.get(position);
                holder.bookNameTextView.setText(currentBook.getBookName());
                holder.bookAuthorTextView.setText(currentBook.getBookName());
            }
        }

        @Override
        public int getItemCount() {
            if (mData.isEmpty()) {
                return 1;
            }
            return mData.size();
        }

        @Override
        public int getItemViewType(int position) {
            return mData.isEmpty() ? EMPTY_LIST : NOT_EMPTY_LIST;
        }

        class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
            //TODO check nullable when data is exists
            @Nullable
            @BindView(R.id.book_name_in_section)
            TextView bookNameTextView;
            @Nullable
            @BindView(R.id.book_author_in_section)
            TextView bookAuthorTextView;

            MyViewHolder(View container) {
                super(container);
                ButterKnife.bind(this, container);
                container.setOnClickListener(this);
                container.setOnLongClickListener(this);
            }

            @Override
            public void onClick(View view) {
                if (!mData.isEmpty()) {
                    //mCallbacks.onCategorySelected(itemBookCategory.getText().toString());
                }
            }

            @Override
            public boolean onLongClick(View view) {
                if (!mData.isEmpty()) {
                    //mCategoryForDelete = itemBookCategory.getText().toString();
                    //openDeleteQuoteCategoryDialog();
                }
                return false;
            }

            private void openDeleteQuoteCategoryDialog() {
//                LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
//                View dialogView = layoutInflater.inflate(R.layout.dialog_delete, null);
//                AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(getActivity());
//                mDialogBuilder.setView(dialogView);
//                mDialogBuilder
//                        .setCancelable(false)
//                        .setPositiveButton(getString(R.string.dialog_ok_button),
//                                new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dialog, int id) {
//                                        mQuoteDataRepository.deleteAllQuotesWithCurrentCategory(mCategoryForDelete, mQuoteType);
//                                        showSnackbar();
//                                    }
//                                })
//                        .setNegativeButton(getString(R.string.dialog_cancel_button),
//                                new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dialog, int id) {
//                                        dialog.cancel();
//                                    }
//                                });
//                AlertDialog alertDialog = mDialogBuilder.create();
//                alertDialog.show();
            }

            private void showSnackbar() {
//                final CoordinatorLayout coordinatorLayout = (CoordinatorLayout) getActivity().findViewById(R.id.coordinator_layout);
//                Snackbar snackbarIsDeleted = Snackbar.make(coordinatorLayout, getString(R.string.quote_category) +
//                        mCategoryForDelete + getString(R.string.is_deleted), Snackbar.LENGTH_LONG);
//                snackbarIsDeleted.show();
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
