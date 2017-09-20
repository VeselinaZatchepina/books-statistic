package com.github.veselinazatchepina.bookstatistics;


import android.app.Application;
import android.content.Context;

import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import io.realm.Realm;
import io.realm.RealmConfiguration;


public class MyApplication extends Application {

    private RefWatcher mRefWatcher;

    public static RefWatcher getRefWatcher(Context context) {
        MyApplication application = (MyApplication) context.getApplicationContext();
        return application.mRefWatcher;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        mRefWatcher = LeakCanary.install(this);
        Realm.init(this);
        RealmConfiguration configuration = new RealmConfiguration.Builder()
                .name("books.realm")
                .build();
        Realm.setDefaultConfiguration(configuration);
    }
}