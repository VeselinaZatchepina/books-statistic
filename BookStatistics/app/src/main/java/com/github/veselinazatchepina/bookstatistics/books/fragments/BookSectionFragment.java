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
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.github.veselinazatchepina.bookstatistics.R;
import com.github.veselinazatchepina.bookstatistics.database.BooksRealmRepository;
import com.github.veselinazatchepina.bookstatistics.database.model.Book;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.realm.Case;
import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmRecyclerViewAdapter;
import io.realm.RealmResults;
import io.realm.Sort;

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
    private CurrentBookCallbacks mCallbacks;
    private long mBookIdForDelete;
    private String mSortedBy;

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
        defineInputData(savedInstanceState);
        mBooksRealmRepository = new BooksRealmRepository();
        if (mCurrentCategory != null) {
            mBooksInCurrentSection = mBooksRealmRepository.getAllBooksInCurrentSectionByCategory(mCurrentSectionType, mCurrentCategory);
        } else {
            mBooksInCurrentSection = mBooksRealmRepository.getAllBooksInCurrentSection(mCurrentSectionType);
        }
    }

    private void defineInputData(Bundle savedInstanceState) {
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
                mBookSectionRecyclerViewAdapter.updateData(element);
            }
        });
        return rootView;
    }

    private void setCurrentCategoryTitleIsGone() {
        mTitleCurrentCategory.setVisibility(View.GONE);
    }

    private void defineRecyclerView() {
        mBookSectionRecyclerViewAdapter = new BookSectionRecyclerViewAdapter(getActivity(), mBooksRealmRepository.getRealmConnection(), mBooksInCurrentSection, true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mBookSectionRecyclerViewAdapter);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.book_section, menu);
        MenuItem searchMenuItem = menu.findItem(R.id.search_book);
        final SearchView searchView = (SearchView) searchMenuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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
                        mSortedBy = "bookName";
                        break;
                    case R.id.filter_by_book_author:
                        mSortedBy = "authorName";
                        break;
                    case R.id.filter_by_rating:
                        mSortedBy = "rating.starsCount";
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
        if (mSortedBy.equals("rating.starsCount")) {
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
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    class BookSectionRecyclerViewAdapter extends RealmRecyclerViewAdapter<Book, BookSectionRecyclerViewAdapter.MyViewHolder> implements Filterable {
        private static final int EMPTY_LIST = 0;
        private static final int NOT_EMPTY_LIST = 1;

        Realm mRealm;

        BookSectionRecyclerViewAdapter(@NonNull Context context, Realm realm, @Nullable OrderedRealmCollection<Book> data, boolean autoUpdate) {
            super(context, data, autoUpdate);
            mRealm = realm;
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
                holder.bookNameTextView.setText(currentBook.getBookName());
                holder.bookAuthorTextView.setText(currentBook.getAuthorName());
                holder.book = currentBook;
            }
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
            if(text == null || "".equals(text)) {
                updateData(mRealm.where(Book.class).findAll());
            } else {
                updateData(mRealm.where(Book.class)
                        .contains("bookName", text, Case.INSENSITIVE)
                        .findAll());
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
