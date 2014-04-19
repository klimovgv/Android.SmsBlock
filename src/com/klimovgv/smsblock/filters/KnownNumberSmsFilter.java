package com.klimovgv.smsblock.filters;

import com.klimovgv.smsblock.models.Sms;

public class KnownNumberSmsFilter extends SmsFilterBase {

    public KnownNumberSmsFilter(int priority, boolean isBlock) {
        super(priority, isBlock);
    }

    @Override
    public boolean filter(Sms sms) {
        if (null == sms.getSenderName()) {
            return true;
        }

        return false;
    }

}
