package com.github.veselinazatchepina.bookstatistics.books.fragments;


import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.menu.ActionMenuItemView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.veselinazatchepina.bookstatistics.MyApplication;
import com.github.veselinazatchepina.bookstatistics.R;
import com.github.veselinazatchepina.bookstatistics.database.BooksRealmRepository;
import com.github.veselinazatchepina.bookstatistics.database.model.Book;
import com.squareup.leakcanary.RefWatcher;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.realm.OrderedRealmCollection;
import io.realm.RealmChangeListener;
import io.realm.RealmRecyclerViewAdapter;
import io.realm.RealmResults;
import io.realm.Sort;

public class BookSectionFragment extends Fragment {

    private static final String CURRENT_BOOK_SECTION = "current_book_section";
    private static final String CURRENT_BOOK_CATEGORY = "current_book_category";
    private static final String TAG_CHANGE_RATING_DIALOG = "tag_change_rating_dialog";
    private static final String TAG_CHANGE_SECTION_DIALOG = "tag_change_section_dialog";

    @BindView(R.id.title_current_category)
    TextView mTitleCurrentCategory;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    private Unbinder unbinder;

    private String mCurrentSectionType;
    private String mCurrentCategory;
    private BooksRealmRepository mBooksRealmRepository;
    private RealmResults<Book> mBooksInCurrentSection;
    private RealmResults<Book> mAllBooks;
    BookSectionRecyclerViewAdapter mBookSectionRecyclerViewAdapter;
    private CurrentBookCallbacks mCallbacks;
    private long mBookIdForDelete;
    private String mSortedBy;
    private SearchView mSearchView;
    private ChangeSectionDialogFragment mChangeSectionDialogFragment;
    private ChangeRatingDialogFragment mChangeRatingDialogFragment;

    public BookSectionFragment() {
    }

    public static BookSectionFragment newInstance(String sectionType, String currentCategory) {
        Bundle args = new Bundle();
        args.putString(CURRENT_BOOK_SECTION, sectionType);
        args.putString(CURRENT_BOOK_CATEGORY, currentCategory);
        BookSectionFragment fragment = new BookSectionFragment();
        fragment.setArguments(args);
        return fragment;
    }

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
        setHasOptionsMenu(true);
        defineInputData();
        getBooksInCurrentSection();
    }

    private void defineInputData() {
        if (getArguments() != null) {
            mCurrentSectionType = getArguments().getString(CURRENT_BOOK_SECTION);
            mCurrentCategory = getArguments().getString(CURRENT_BOOK_CATEGORY);
        }
    }

    private void getBooksInCurrentSection() {
        mBooksRealmRepository = new BooksRealmRepository();
        if (mCurrentCategory != null) {
            mBooksInCurrentSection = mBooksRealmRepository.getAllBooksInCurrentSectionByCategory(mCurrentSectionType, mCurrentCategory);
            mAllBooks = mBooksRealmRepository.getAllBooksCurrentCategory(mCurrentCategory);
        } else {
            mBooksInCurrentSection = mBooksRealmRepository.getAllBooksInCurrentSection(mCurrentSectionType);
            mAllBooks = mBooksRealmRepository.getAllBooks();
        }
        mAllBooks.addChangeListener(new RealmChangeListener<RealmResults<Book>>() {
            @Override
            public void onChange(RealmResults<Book> element) {
                if (getActivity() != null && element.isEmpty()) {
                    getActivity().finish();
                }
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_recycler_view, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        setCurrentCategoryTitleIsGone();
        defineRecyclerView();
        defineListenerToBooksInCurrentSectionResults();
        return rootView;
    }

    private void setCurrentCategoryTitleIsGone() {
        mTitleCurrentCategory.setVisibility(View.GONE);
    }

    private void defineRecyclerView() {
        mBookSectionRecyclerViewAdapter = new BookSectionRecyclerViewAdapter(getActivity(),
                mBooksInCurrentSection,
                true,
                mCurrentSectionType);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mBookSectionRecyclerViewAdapter);
    }

    private void defineListenerToBooksInCurrentSectionResults() {
        mBooksInCurrentSection.addChangeListener(new RealmChangeListener<RealmResults<Book>>() {
            @Override
            public void onChange(RealmResults<Book> element) {
                if (mBookSectionRecyclerViewAdapter != null) {
                    mBookSectionRecyclerViewAdapter.updateData(element);
                }
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        clearSearchView();
        inflater.inflate(R.menu.book_section, menu);
        MenuItem searchMenuItem = menu.findItem(R.id.search_book);
        mSearchView = (SearchView) searchMenuItem.getActionView();
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mBookSectionRecyclerViewAdapter.filterResults(newText);
                return true;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void clearSearchView() {
        if (mSearchView != null) {
            mSearchView.setOnQueryTextListener(null);
        }
        mSearchView = null;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.filter_book:
                ActionMenuItemView view = ButterKnife.findById(getActivity(), R.id.filter_book);
                showPopup(view);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showPopup(View v) {
        PopupMenu popup = new PopupMenu(getActivity(), v);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.filter_by_book_name:
                        mSortedBy = getString(R.string.sort_by_book_name_realm);
                        break;
                    case R.id.filter_by_book_author:
                        mSortedBy = getString(R.string.sort_by_author_name_realm);
                        break;
                    case R.id.filter_by_rating:
                        mSortedBy = getString(R.string.sort_by_rating_realm);
                        break;
                }
                sortAndUpdateRecyclerView(mSortedBy);
                return true;
            }
        });
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.popup_book_section, popup.getMenu());
        popup.show();
    }

    private void sortAndUpdateRecyclerView(String mSortedBy) {
        if (mSortedBy.equals(getString(R.string.sort_by_rating_realm))) {
            mBooksInCurrentSection = mBooksInCurrentSection.sort(mSortedBy, Sort.DESCENDING);
        } else {
            mBooksInCurrentSection = mBooksInCurrentSection.sort(mSortedBy, Sort.ASCENDING);
        }
        mBookSectionRecyclerViewAdapter.changeDate();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallbacks = (CurrentBookCallbacks) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
        clearSearchView();

    }

    @Override
    public void onPause() {
        super.onPause();
        mChangeSectionDialogFragment = null;
        mChangeRatingDialogFragment = null;
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

    class BookSectionRecyclerViewAdapter extends RealmRecyclerViewAdapter<Book, BookSectionRecyclerViewAdapter.MyViewHolder> implements Filterable {
        private static final int EMPTY_LIST = 0;
        private static final int NOT_EMPTY_LIST = 1;

        String currentSectionType;

        BookSectionRecyclerViewAdapter(@NonNull Context context, @Nullable OrderedRealmCollection<Book> data, boolean autoUpdate, String currentSectionType) {
            super(context, data, autoUpdate);
            this.currentSectionType = currentSectionType;
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
            if (getData() != null && !getData().isEmpty()) {
                Book currentBook = getData().get(position);
                if (currentBook.getBookName() != null && !currentBook.getBookName().equals("")) {
                    holder.bookNameTextView.setText("\"" + currentBook.getBookName() + "\"");
                } else {
                    holder.bookNameTextView.setText(getResources().getString(R.string.empty_field_book_name));
                }
                if (currentBook.getAuthorName() != null && !currentBook.getAuthorName().equals("")) {
                    holder.bookAuthorTextView.setText(currentBook.getAuthorName());
                } else {
                    holder.bookAuthorTextView.setText(getResources().getString(R.string.empty_field_book_author));
                }
                holder.book = currentBook;
                setListenerToChangeSectionImageView(holder);
                setListenerToChangeRating(holder);
            }
        }

        private void setListenerToChangeSectionImageView(final MyViewHolder holder) {
            holder.goToOtherSectionImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mChangeSectionDialogFragment = ChangeSectionDialogFragment.newInstance(holder.book.getId());
                    mChangeSectionDialogFragment.show(getActivity().getSupportFragmentManager(), TAG_CHANGE_SECTION_DIALOG);
                }
            });
        }

        private void setListenerToChangeRating(final MyViewHolder holder) {
            holder.ratingImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mChangeRatingDialogFragment = ChangeRatingDialogFragment.newInstance(holder.book.getId());
                    mChangeRatingDialogFragment.show(getActivity().getSupportFragmentManager(), TAG_CHANGE_RATING_DIALOG);
                }
            });
        }

        @Override
        public int getItemCount() {
            if (getData() != null && getData().isEmpty()) {
                return 1;
            }
            return getData().size();
        }

        @Override
        public int getItemViewType(int position) {
            return getData().isEmpty() ? EMPTY_LIST : NOT_EMPTY_LIST;
        }

        public void changeDate() {
            updateData(mBooksInCurrentSection);
            notifyDataSetChanged();
        }

        @Override
        public Filter getFilter() {
            BookFilter filter = new BookFilter(this);
            return filter;
        }

        public void filterResults(String text) {
            text = text == null ? null : text.toLowerCase().trim();
            if (text == null || "".equals(text)) {
                if (mCurrentCategory == null) {
                    updateData(mBooksRealmRepository.getAllBooksInCurrentSection(currentSectionType));
                } else {
                    updateData(mBooksRealmRepository.getAllBooksInCurrentSectionByCategory(currentSectionType, mCurrentCategory));
                }
            } else {
                if (mCurrentCategory == null) {
                    updateData(mBooksRealmRepository.getBookBySectionAndBookName(currentSectionType, text));
                } else {
                    updateData(mBooksRealmRepository.getBookBySectionAndBookNameByCategory(currentSectionType, text, mCurrentCategory));
                }
            }
        }

        private class BookFilter
                extends Filter {
            private final BookSectionRecyclerViewAdapter adapter;

            private BookFilter(BookSectionRecyclerViewAdapter adapter) {
                super();
                this.adapter = adapter;
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                return new FilterResults();
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                adapter.filterResults(constraint.toString());
            }
        }

        class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
            @Nullable
            @BindView(R.id.book_name_in_section)
            TextView bookNameTextView;
            @Nullable
            @BindView(R.id.book_author_in_section)
            TextView bookAuthorTextView;
            @Nullable
            @BindView(R.id.go_to_other_section_image)
            ImageView goToOtherSectionImageView;
            @Nullable
            @BindView(R.id.set_rating_image)
            ImageView ratingImageView;
            Book book;

            MyViewHolder(View container) {
                super(container);
                ButterKnife.bind(this, container);
                container.setOnClickListener(this);
                container.setOnLongClickListener(this);
            }

            @Override
            public void onClick(View view) {
                if (getData() != null && !getData().isEmpty()) {
                    mCallbacks.onBookSelected(book.getId(), mCurrentSectionType, mCurrentCategory);
                }
            }

            @Override
            public boolean onLongClick(View view) {
                if (getData() != null && !getData().isEmpty()) {
                    mBookIdForDelete = book.getId();
                    openDeleteBookDialog();
                }
                return false;
            }

            private void openDeleteBookDialog() {
                LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
                View dialogView = layoutInflater.inflate(R.layout.dialog_delete, null);
                TextView deleteDialogTitle = ButterKnife.findById(dialogView, R.id.dialog_delete_title);
                deleteDialogTitle.setText(getResources().getString(R.string.dialog_delete_current_book_title));
                AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(getActivity());
                mDialogBuilder.setView(dialogView);
                mDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton(getString(R.string.dialog_ok_button),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        mBooksRealmRepository.deleteBookById(mBookIdForDelete);
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
                Button nButton = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
                nButton.setTextColor(getResources().getColor(R.color.book_accent_background));
                Button pButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
                pButton.setTextColor(getResources().getColor(R.color.book_accent_background));
            }
        }
    }

    public interface CurrentBookCallbacks {

        /* Method starts activity with current books
        *
        * @param currentCategory
        */
        void onBookSelected(long currentBookId, String currentSectionType, String currentCategory);
    }
}
