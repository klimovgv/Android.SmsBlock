package com.klimovgv.smsblock.broadcasts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import com.klimovgv.smsblock.db.FilterDataSource;
import com.klimovgv.smsblock.db.SmsDataSource;
import com.klimovgv.smsblock.filters.ISmsFilter;
import com.klimovgv.smsblock.filters.KnownNumberSmsFilter;
import com.klimovgv.smsblock.filters.PhoneNumberContainsSmsFilter;
import com.klimovgv.smsblock.models.Sms;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class SmsBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = "SmsBlock";

    private SimpleDateFormat iso8601Format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();

        if (null == bundle)
            return;

        Object[] messages = (Object[]) bundle.get("pdus");

        if (0 == messages.length) {
            Log.d(TAG, "Zero messages received");
            return;
        }

        Log.d(TAG, String.format("Received %d sms messages", messages.length));

        SmsMessage[] smsMessages = new SmsMessage[messages.length];
        SmsDataSource smsDataSource = new SmsDataSource(context);
        CallerNameGetter callerNameGetter = new CallerNameGetter(context);

        try {
            smsDataSource.open();

            // get sms filters
            List<ISmsFilter> filters = getSmsFilters(context);

            // get transport sms messages (could be more than 1 if message body is long enough)
            for (int i = 0; i < messages.length; i++) {
                smsMessages[i] = SmsMessage.createFromPdu((byte[]) messages[i]);
            }

            // union transport sms messages in one big sms message to show
            Sms sms = convertToSmsModel(callerNameGetter, smsMessages);

            // apply filters
            boolean wasApplied = false;
            boolean doBlockSms = false;

            for (int k = 0; k < filters.size() && !wasApplied; k++) {
                ISmsFilter smsFilter = filters.get(k);

                wasApplied = smsFilter.filter(sms);
                doBlockSms = smsFilter.isBlock();
            }

            if (wasApplied && doBlockSms) {
                blockSms(smsDataSource, sms);
            }
        }
        finally {
            if (smsDataSource != null)
                smsDataSource.close();
        }
    }

    private void blockSms(SmsDataSource smsDataSource, Sms sms) {
        // block sms
        abortBroadcast();

        // save sms to local db
        smsDataSource.saveSms(sms);

        Log.d(TAG, "SMS blocked");
    }

    private List<ISmsFilter> getSmsFilters(Context context) {
        FilterDataSource filtersDataSource = new FilterDataSource(context);

        try {
            filtersDataSource.open();

            List<ISmsFilter> filters = filtersDataSource.getAllFilters();

            if (filters.size() == 0) {
                filters.add(new KnownNumberSmsFilter(4, true));
                filters.add(new PhoneNumberContainsSmsFilter(2, false, "TCS Bank"));
                filters.add(new PhoneNumberContainsSmsFilter(3, false, "Alfa-Bank"));
                filters.add(new PhoneNumberContainsSmsFilter(1, false, "Balance"));
            }

            // sort filters by priority
            Collections.sort(filters);

            return filters;
        }
        finally {
            if (filtersDataSource != null)
                filtersDataSource.close();
        }
    }

    private Sms convertToSmsModel(CallerNameGetter callerNameGetter, SmsMessage[] smsMessages) {
        String phoneNumber = smsMessages[0].getOriginatingAddress();
        String senderName = callerNameGetter.getCallerName(phoneNumber);
        String messageText = getMessageBody(smsMessages);
        String timestamp = getSmsTimestampString(smsMessages[0]);

        String logMessage =
                String.format("SMS: [%1$s] [%2$s] [%3$s] [%4$s]",
                        timestamp, phoneNumber, senderName, messageText);

        Log.d(TAG, logMessage);

        return new Sms(phoneNumber, senderName, messageText, timestamp);
    }

    private String getMessageBody(SmsMessage[] smsMessages) {
        String messageBody = "";

        for (int i = 0; i < smsMessages.length; i++) {
            messageBody += smsMessages[i].getMessageBody();
        }

        return messageBody;
    }

    private String getSmsTimestampString(SmsMessage smsMessage) {
        long timestamp = smsMessage.getTimestampMillis();

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp);

        Date timestampDate = calendar.getTime();
        String timestampString = iso8601Format.format(timestampDate);

        return timestampString;
    }

}