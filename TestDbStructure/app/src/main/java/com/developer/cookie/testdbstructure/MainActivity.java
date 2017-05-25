package com.developer.cookie.testdbstructure;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.developer.cookie.testdbstructure.model.Book;
import com.developer.cookie.testdbstructure.model.BookCategory;
import com.developer.cookie.testdbstructure.model.BookRating;
import com.developer.cookie.testdbstructure.model.Section;

import io.realm.Realm;

public class MainActivity extends AppCompatActivity {

    Realm mRealm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRealm = Realm.getDefaultInstance();
        mRealm.executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    BookCategory bookCategory0 = new BookCategory(0, "IT", 1);
                    BookRating bookRating0 = new BookRating(0, 5, 1);
                    Section section0 = new Section(0, "Old", 1);
                    Book book0 = new Book(0, "author0", bookCategory0, "bookName0", "23.06.2017", "12.04.2017",
                            800, bookRating0, section0);
                    realm.copyToRealm(book0);
                }
        });











    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRealm.close();
    }
}
