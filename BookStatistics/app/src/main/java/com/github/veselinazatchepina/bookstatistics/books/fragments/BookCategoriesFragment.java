package com.github.veselinazatchepina.bookstatistics.books.fragments;


import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.veselinazatchepina.bookstatistics.R;
import com.github.veselinazatchepina.bookstatistics.database.BooksRealmRepository;
import com.github.veselinazatchepina.bookstatistics.database.model.BookCategory;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.realm.OrderedRealmCollection;
import io.realm.RealmChangeListener;
import io.realm.RealmRecyclerViewAdapter;
import io.realm.RealmResults;


public class BookCategoriesFragment extends Fragment {

    @BindView(R.id.title_current_category)
    TextView mTitleCurrentCategory;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    private Unbinder unbinder;

    private BooksRealmRepository mBooksRealmRepository;
    private RealmResults<BookCategory> mBookCategories;
    private BookCategoryCallbacks mCallbacks;
    BookCategoryRecyclerViewAdapter mBookCategoryRecyclerViewAdapter;
    private String mCategoryForDelete;

    public BookCategoriesFragment() {

    }

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
//        } else if (getArguments() != null) {
//            mQuoteType = getArguments().getString(CURRENT_QUOTE_TYPE_NEW_INSTANCE);
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recycler_view, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        setCurrentCategoryTitleIsGone();
        defineRecyclerView();
        mBookCategories.addChangeListener(new RealmChangeListener<RealmResults<BookCategory>>() {
            @Override
            public void onChange(RealmResults<BookCategory> element) {
                mBookCategoryRecyclerViewAdapter.mData = element;
            }
        });
        return rootView;
    }

    private void setCurrentCategoryTitleIsGone() {
        mTitleCurrentCategory.setVisibility(View.GONE);
    }

    private void defineRecyclerView() {
        mBookCategoryRecyclerViewAdapter = new BookCategoryRecyclerViewAdapter(getActivity(), mBookCategories, true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mBookCategoryRecyclerViewAdapter);
    }

    public static BookCategoriesFragment newInstance() {
        return new BookCategoriesFragment();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        //outState.putString(QUOTE_TYPE_BUNDLE, mQuoteType);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallbacks = (BookCategoryCallbacks) context;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mBooksRealmRepository.closeDbConnect();
    }

    class BookCategoryRecyclerViewAdapter extends RealmRecyclerViewAdapter<BookCategory, BookCategoryRecyclerViewAdapter.MyViewHolder> {
        private static final int EMPTY_LIST = 0;
        private static final int NOT_EMPTY_LIST = 1;

        private OrderedRealmCollection<BookCategory> mData;

        BookCategoryRecyclerViewAdapter(@NonNull Context context, @Nullable OrderedRealmCollection<BookCategory> data, boolean autoUpdate) {
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
                            .inflate(R.layout.books_categories_recycler_view_item, parent, false);
                    return new MyViewHolder(itemView);
            }
            return null;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            if (!mData.isEmpty()) {
                holder.itemBookCategory.setText(mData.get(position).getCategoryName());
                holder.itemBookCategory.setAllCaps(true);
                holder.itemBookCount.setText(String.valueOf(mData.get(position).getCategoryBookCount()));
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
            @Nullable
            @BindView(R.id.book_category_name)
            TextView itemBookCategory;
            @Nullable
            @BindView(R.id.book_count)
            TextView itemBookCount;

            MyViewHolder(View container) {
                super(container);
                ButterKnife.bind(this, container);
                container.setOnClickListener(this);
                container.setOnLongClickListener(this);
            }

            @Override
            public void onClick(View view) {
                if (!mData.isEmpty()) {
                    mCallbacks.onCategorySelected(itemBookCategory.getText().toString());
                }
            }

            @Override
            public boolean onLongClick(View view) {
                if (!mData.isEmpty()) {
                    mCategoryForDelete = itemBookCategory.getText().toString();
                    openDeleteBookCategoryDialog();
                }
                return false;
            }

            private void openDeleteBookCategoryDialog() {
                LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
                View dialogView = layoutInflater.inflate(R.layout.dialog_delete, null);
                AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(getActivity());
                mDialogBuilder.setView(dialogView);
                mDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton(getString(R.string.dialog_ok_button),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        mBooksRealmRepository.deleteAllBooksWithCurrentCategory(mCategoryForDelete);
                                        showSnackbar();
                                    }
                                })
                        .setNegativeButton(getString(R.string.dialog_cancel_button),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                AlertDialog alertDialog = mDialogBuilder.create();
                alertDialog.show();
            }

            private void showSnackbar() {
                final CoordinatorLayout coordinatorLayout = ButterKnife.findById(getActivity(), R.id.coordinator_layout);
                Snackbar snackbarIsDeleted = Snackbar.make(coordinatorLayout, getString(R.string.book_category) +
                        mCategoryForDelete + getString(R.string.is_deleted), Snackbar.LENGTH_LONG);
                snackbarIsDeleted.show();
            }
        }
    }

    public interface BookCategoryCallbacks {

        /* Method starts activity with all books of current category
        *
        * @param currentCategory
        */
        void onCategorySelected(String currentCategory);
    }
}