package com.appspot.expenses_graph.broadcasts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.SmsMessage;
import android.util.Log;
import com.appspot.expenses_graph.db.SmsDataSource;

import java.util.Calendar;
import java.util.Date;

public class SmsBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = "SmsBlock";

    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();

        if (null == bundle)
            return;

        Object[] messages = (Object[]) bundle.get("pdus");

        if (0 == messages.length) {
            Log.i(TAG, "Zero messages received");
            return;
        }

        SmsMessage[] smsMessages = new SmsMessage[messages.length];

        Log.d(TAG, String.format("Received %d sms messages", smsMessages.length));

        SmsDataSource smsDataSource = new SmsDataSource(context);

        try {
            smsDataSource.open();

            for (int i = 0; i < messages.length; i++) {
                smsMessages[i] = SmsMessage.createFromPdu((byte[]) messages[i]);

                String messageBody = smsMessages[i].getMessageBody();
                String phoneNumber = smsMessages[i].getOriginatingAddress();
                Date time = getSmsTimestamp(smsMessages[i]);
                String callerName = getCallerNameForNumber(context, phoneNumber);

                String logMessage =
                        String.format("SMS #%1$d: [%2$tF %2$tT] [%3$s] [%4$s] [%5$s]",
                                i + 1, time, phoneNumber, callerName, messageBody);

                Log.d(TAG, logMessage);

                // apply rules in order
                blockIfCallerNotFromContactList(smsDataSource, messageBody, phoneNumber, time, callerName);
            }
        }
        finally {
            if (smsDataSource != null)
                smsDataSource.close();
        }
    }

    private boolean blockIfCallerNotFromContactList(SmsDataSource smsDataSource, String messageBody, String phoneNumber, Date time, String callerName) {
        // block if sms from a "not-trusted" number
        if (null == callerName) {
            // block sms
            abortBroadcast();

            // save sms to local db
            smsDataSource.createSms(phoneNumber, callerName, messageBody, time);

            Log.d(TAG, "SMS blocked");

            return true;
        }

        return false;
    }

    private Date getSmsTimestamp(SmsMessage smsMessage) {
        long timestamp = smsMessage.getTimestampMillis();

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp);

        return calendar.getTime();
    }

    private String getCallerNameForNumber(Context context, String number) {
        /// number is the phone number
        Uri lookupUri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number));

        String[] phoneNumberProjection = {
                ContactsContract.PhoneLookup._ID,
                ContactsContract.PhoneLookup.NUMBER,
                ContactsContract.PhoneLookup.DISPLAY_NAME
        };

        Cursor cur = context.getContentResolver().query(lookupUri, phoneNumberProjection, null, null, null);

        try {
            if (cur.moveToFirst()) {
                return cur.getString(2);
            }
        } finally {
            if (null != cur)
                cur.close();
        }

        return null;
    }

}