package com.github.veselinazatchepina.bookstatistics.books.fragments;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.ShareActionProvider;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.github.veselinazatchepina.bookstatistics.MyApplication;
import com.github.veselinazatchepina.bookstatistics.R;
import com.github.veselinazatchepina.bookstatistics.database.BooksRealmRepository;
import com.github.veselinazatchepina.bookstatistics.database.model.Book;
import com.github.veselinazatchepina.bookstatistics.utils.ColorationTextChar;
import com.squareup.leakcanary.RefWatcher;

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
    private Intent mSharingIntent;

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
        getActivity().setTitle(ColorationTextChar.setFirstVowelColor("Current book", getActivity()));
        if (getArguments() != null) {
            mCurrentBookId = getArguments().getLong(CURRENT_BOOK_ID);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_current_book, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        setHasOptionsMenu(true);
        mBookWithCurrentId.addChangeListener(new RealmChangeListener<RealmResults<Book>>() {
            @Override
            public void onChange(RealmResults<Book> element) {
                if (isAdded() && !element.isEmpty()) {
                    Book currentBook = element.first();
                    mCurrentBookName.setText("\"" + currentBook.getBookName() + "\"");
                    mCurrentBookAuthor.setText(currentBook.getAuthorName());
                    mCurrentBookCategory.setText(currentBook.getBookCategory().getCategoryName());
                    mCurrentBookSection.setText(currentBook.getSection().getSectionName());
                    mCurrentBookPages.setText(String.valueOf(currentBook.getPageCount()));
                    mCurrentBookDateStart.setText(currentBook.getDateStart().replace("/", "."));
                    mCurrentBookDateEnd.setText(currentBook.getDateEnd().replace("/", "."));
                    mCurrentBookRating.setText(String.valueOf(currentBook.getRating().getStarsCount()));
                }
            }
        });
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.current_book, menu);
        setShareAction(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_quote:
                openDeleteBookDialog();
                break;
            case R.id.menu_item_share:
                startActivity(Intent.createChooser(mSharingIntent, "Select conversation"));
                break;
        }
        return super.onOptionsItemSelected(item);
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
                                mBooksRealmRepository.deleteBookById(mBookWithCurrentId.first().getId());
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

    private void setShareAction(Menu menu) {
        MenuItem item = menu.findItem(R.id.menu_item_share);
        ShareActionProvider shareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
        mSharingIntent = new Intent(Intent.ACTION_SEND);
        mSharingIntent.setType("text/plain");
        String quoteTextForShareBody = "\"" + mBookWithCurrentId.first().getBookName() + "\"\n" + mBookWithCurrentId.first().getAuthorName();
        mSharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "It is great Book!");
        mSharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, quoteTextForShareBody);
        if (shareActionProvider != null) {
            shareActionProvider.setShareIntent(mSharingIntent);
        }
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
