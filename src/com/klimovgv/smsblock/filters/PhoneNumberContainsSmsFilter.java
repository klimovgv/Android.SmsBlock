package com.klimovgv.smsblock.filters;

import com.klimovgv.smsblock.models.Sms;

public class PhoneNumberContainsSmsFilter extends SmsFilterBase {

    private String phoneNumberPart;

    public PhoneNumberContainsSmsFilter(int priority, boolean isBlock, String phoneNumberPart) {
        super(priority, isBlock);
        this.phoneNumberPart = phoneNumberPart;
    }

    @Override
    public boolean filter(Sms sms) {
        if (sms.getPhoneNumber().contains(this.phoneNumberPart)){
            return true;
        }

        return false;
    }
}
