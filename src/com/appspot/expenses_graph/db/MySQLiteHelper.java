package com.appspot.expenses_graph.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteHelper extends SQLiteOpenHelper {

    public static final String TABLE_SMS = "sms";

    public static final String COLUMN_SMS_ID = "_id";
    public static final String COLUMN_SMS_PHONE_NUMBER = "phone_number";
    public static final String COLUMN_SMS_SENDER_NAME = "sender_name";
    public static final String COLUMN_SMS_MESSAGE_TEXT = "message_text";
    public static final String COLUMN_SMS_TIMESTAMP = "timestamp";

    private static final String DATABASE_NAME = "expenses.db";
    private static final int DATABASE_VERSION = 1;

    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        String createSql =
                "create table " + TABLE_SMS + "("
                + COLUMN_SMS_ID + " integer primary key autoincrement, "
                + COLUMN_SMS_PHONE_NUMBER + " text not null, "
                + COLUMN_SMS_SENDER_NAME + " text, "
                + COLUMN_SMS_MESSAGE_TEXT + " text not null, "
                + COLUMN_SMS_TIMESTAMP + " text not null "
                + ");";

        database.execSQL(createSql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(MySQLiteHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SMS);
        onCreate(db);
    }

}
