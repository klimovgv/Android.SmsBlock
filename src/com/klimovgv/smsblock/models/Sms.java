package com.klimovgv.smsblock.models;

public class Sms {
    long id;
    String phoneNumber;
    String senderName;
    String messageText;
    String timestamp;

    public Sms(String phoneNumber, String senderName, String messageText, String timestamp) {
        this.phoneNumber = phoneNumber;
        this.senderName = senderName;
        this.messageText = messageText;
        this.timestamp = timestamp;
    }

    public Sms(long id, String phoneNumber, String senderName, String messageText, String timestamp) {
        this(phoneNumber, senderName, messageText, timestamp);
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getSenderName() {
        return senderName;
    }

    public String getMessageText() {
        return messageText;
    }

    public String getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "id=" + id +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", senderName='" + senderName + '\'' +
                ", messageText='" + messageText + '\'' +
                ", timestamp='" + timestamp + '\'';
    }
}
