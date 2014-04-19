package com.klimovgv.smsblock.models;

import com.klimovgv.smsblock.filters.EmptySmsFilter;
import com.klimovgv.smsblock.filters.ISmsFilter;
import com.klimovgv.smsblock.filters.KnownNumberSmsFilter;
import com.klimovgv.smsblock.filters.PhoneNumberContainsSmsFilter;

public class Filter {
    long id;
    String name;
    int priority;
    boolean isBlock;
    String phoneNumber;
    boolean isKnownNumberFilter;

    public Filter(int priority, boolean isBlock, String phoneNumber, boolean isKnownNumberFilter) {
        this.priority = priority;
        this.isBlock = isBlock;
        this.phoneNumber = phoneNumber;
        this.isKnownNumberFilter = isKnownNumberFilter;
    }

    public Filter(String name, int priority, boolean isBlock, String phoneNumber, boolean isKnownNumberFilter) {
        this(priority, isBlock, phoneNumber, isKnownNumberFilter);
        this.name = name;
    }

    public Filter(long id, String name, int priority, boolean isBlock, String phoneNumber, boolean isKnownNumberFilter) {
        this(name, priority, isBlock, phoneNumber, isKnownNumberFilter);
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        String restrictionString = isBlock ? "Block" : "Allow";

        if (null == name) {
            if (isKnownNumberFilter()){
                name = String.format("%1$s if not from contact list", restrictionString);
            }
            else {
                name = String.format("%1$s if number contains %2$s", restrictionString, getPhoneNumber());
            }
        }

        return name;
    }

    public int getPriority() {
        return priority;
    }

    public boolean isBlock() {
        return isBlock;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public boolean isKnownNumberFilter() {
        return isKnownNumberFilter;
    }

    public ISmsFilter toSmsFilter(){
        if (isKnownNumberFilter()){
            return new KnownNumberSmsFilter(getPriority(), isBlock());
        }

        if (null == getPhoneNumber()){
            return new EmptySmsFilter(getPriority(), isBlock());
        }

        return new PhoneNumberContainsSmsFilter(getPriority(), isBlock(), getPhoneNumber());
    }
}
