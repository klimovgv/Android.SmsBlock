package com.klimovgv.smsblock.filters;

import com.klimovgv.smsblock.models.Sms;

public class EmptySmsFilter extends SmsFilterBase {
    public EmptySmsFilter(int priority, boolean isBlock) {
        super(priority, isBlock);
    }

    @Override
    public boolean filter(Sms sms) {
        return false;
    }
}
