package com.klimovgv.smsblock.filters;

public abstract class SmsFilterBase implements ISmsFilter {

    private int priority;
    private boolean isBlock;

    protected SmsFilterBase(int priority, boolean isBlock) {
        this.priority = priority;
        this.isBlock = isBlock;
    }

    @Override
    public int getPriority() {
        return priority;
    }

    @Override
    public boolean isBlock() {
        return isBlock;
    }

    @Override
    public int compareTo(ISmsFilter another) {
        if (getPriority() > another.getPriority())
            return 1;

        if (getPriority() < another.getPriority())
            return -1;

        return 0;
    }
}
