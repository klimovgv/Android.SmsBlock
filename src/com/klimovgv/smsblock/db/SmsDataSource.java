package com.klimovgv.smsblock.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import com.klimovgv.smsblock.models.Sms;

import java.util.ArrayList;
import java.util.List;

public class SmsDataSource extends DataSourceBase {

    private String[] allColumns = {
            MySQLiteHelper.COLUMN_SMS_ID,
            MySQLiteHelper.COLUMN_SMS_PHONE_NUMBER,
            MySQLiteHelper.COLUMN_SMS_SENDER_NAME,
            MySQLiteHelper.COLUMN_SMS_MESSAGE_TEXT,
            MySQLiteHelper.COLUMN_SMS_TIMESTAMP
    };


    public SmsDataSource(Context context) {
        super(context);
    }

    public void saveSms(Sms sms) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_SMS_PHONE_NUMBER, sms.getPhoneNumber());
        values.put(MySQLiteHelper.COLUMN_SMS_SENDER_NAME, sms.getSenderName());
        values.put(MySQLiteHelper.COLUMN_SMS_MESSAGE_TEXT, sms.getMessageText());
        values.put(MySQLiteHelper.COLUMN_SMS_TIMESTAMP, sms.getTimestamp());

        long insertId = database.insert(MySQLiteHelper.TABLE_SMS, null, values);

//        Cursor cursor = database.query(MySQLiteHelper.TABLE_SMS,
//                allColumns, MySQLiteHelper.COLUMN_SMS_ID + " = " + insertId, null,
//                null, null, null);
//        cursor.moveToFirst();
//
//        Sms newSms = cursorToSms(cursor);
//        cursor.close();
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
