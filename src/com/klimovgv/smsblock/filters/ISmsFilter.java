package com.klimovgv.smsblock.filters;

import com.klimovgv.smsblock.models.Sms;

public interface ISmsFilter extends Comparable<ISmsFilter> {
    int getPriority();
    boolean isBlock();
    boolean filter(Sms sms);
}
