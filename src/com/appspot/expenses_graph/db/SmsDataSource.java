package com.appspot.expenses_graph.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import com.appspot.expenses_graph.models.Sms;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SmsDataSource {
    // Database fields
    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;

    private String[] allColumns = {
            MySQLiteHelper.COLUMN_SMS_ID,
            MySQLiteHelper.COLUMN_SMS_PHONE_NUMBER,
            MySQLiteHelper.COLUMN_SMS_SENDER_NAME,
            MySQLiteHelper.COLUMN_SMS_MESSAGE_TEXT,
            MySQLiteHelper.COLUMN_SMS_TIMESTAMP
    };

    private SimpleDateFormat iso8601Format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public SmsDataSource(Context context) {
        dbHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public Sms createSms(String phoneNumber, String senderName, String messageText, Date timestamp) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_SMS_PHONE_NUMBER, phoneNumber);
        values.put(MySQLiteHelper.COLUMN_SMS_SENDER_NAME, senderName);
        values.put(MySQLiteHelper.COLUMN_SMS_MESSAGE_TEXT, messageText);
        values.put(MySQLiteHelper.COLUMN_SMS_TIMESTAMP, iso8601Format.format(timestamp));

        long insertId = database.insert(MySQLiteHelper.TABLE_SMS, null, values);

        Cursor cursor = database.query(MySQLiteHelper.TABLE_SMS,
                allColumns, MySQLiteHelper.COLUMN_SMS_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();

        Sms newSms = cursorToSms(cursor);
        cursor.close();

        return newSms;
    }

    public void deleteSms(Sms sms) {
        long id = sms.getId();

        database.delete(MySQLiteHelper.TABLE_SMS, MySQLiteHelper.COLUMN_SMS_ID + " = " + id, null);
    }

    public List<Sms> getAllSms() {
        List<Sms> smsList = new ArrayList<Sms>();

        Cursor cursor = database.query(MySQLiteHelper.TABLE_SMS, allColumns, null, null, null, null, MySQLiteHelper.COLUMN_SMS_ID+" DESC");

        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            Sms sms = cursorToSms(cursor);
            smsList.add(sms);

            cursor.moveToNext();
        }

        // make sure to close the cursor
        cursor.close();
        return smsList;
    }

    private Sms cursorToSms(Cursor cursor) {
        return new Sms(cursor.getLong(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4));
    }
}
