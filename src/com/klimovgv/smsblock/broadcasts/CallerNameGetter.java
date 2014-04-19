package com.klimovgv.smsblock.broadcasts;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

public class CallerNameGetter {
    private final Context context;

    public CallerNameGetter(Context context) {

        this.context = context;
    }

    public String getCallerName(String phoneNumber) {
        /// number is the phone number
        Uri lookupUri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber));

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
