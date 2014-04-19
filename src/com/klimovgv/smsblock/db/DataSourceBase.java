package com.klimovgv.smsblock.db;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Grisha on 19/04/2014.
 */
public class DataSourceBase {
    // Database fields
    protected SQLiteDatabase database;
    protected MySQLiteHelper dbHelper;

    public DataSourceBase(Context context) {
        dbHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }
}
