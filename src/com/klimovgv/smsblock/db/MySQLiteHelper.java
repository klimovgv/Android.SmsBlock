package com.klimovgv.smsblock.db;

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

    public static final String TABLE_FILTERS = "filters";
    public static final String COLUMN_FILTER_ID = "_id";
    public static final String COLUMN_FILTER_NAME = "name";
    public static final String COLUMN_FILTER_PRIORITY = "priority";
    public static final String COLUMN_FILTER_IS_BLOCK = "is_block";
    public static final String COLUMN_FILTER_IS_KNOWN_NUMBER = "is_known_number";
    public static final String COLUMN_FILTER_PHONE_NUMBER = "phone_number";


    private static final String DATABASE_NAME = "klimovgv.smsblock.db";
    private static final int DATABASE_VERSION = 4;

    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        String createSmsTable =
                "create table " + TABLE_SMS + "("
                + COLUMN_SMS_ID + " integer primary key autoincrement, "
                + COLUMN_SMS_PHONE_NUMBER + " text not null, "
                + COLUMN_SMS_SENDER_NAME + " text, "
                + COLUMN_SMS_MESSAGE_TEXT + " text not null, "
                + COLUMN_SMS_TIMESTAMP + " text not null "
                + ");";
        database.execSQL(createSmsTable);

        String createFiltersTable =
                "create table " + TABLE_FILTERS + "("
                        + COLUMN_FILTER_ID + " integer primary key autoincrement, "
                        + COLUMN_FILTER_NAME + " text not null, "
                        + COLUMN_FILTER_PRIORITY + " integer not null, "
                        + COLUMN_FILTER_PHONE_NUMBER + " text, "
                        + COLUMN_FILTER_IS_BLOCK + " integer not null, "
                        + COLUMN_FILTER_IS_KNOWN_NUMBER + " integer not null "
                        + ");";
        database.execSQL(createFiltersTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(MySQLiteHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SMS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FILTERS);

        onCreate(db);
    }

}
