package com.ihunda.android.binauralbeat;

import android.app.Application;

import com.ihunda.android.binauralbeat.db.DBHelper;

/**
 * Created by kalpit on 11/04/16.
 */
public class BBeatApp extends Application {
    private DBHelper dbHelper;

    public DBHelper getDbHelper() {
        return dbHelper;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        dbHelper = new DBHelper(this);
    }
}
