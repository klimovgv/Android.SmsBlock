package com.appspot.expenses_graph.models;

public class Sms {
    long id;
    String phoneNumber;
    String callerName;
    String messageText;
    String timestamp;

    public Sms(String phoneNumber, String callerName, String messageText, String timestamp) {
        this.phoneNumber = phoneNumber;
        this.callerName = callerName;
        this.messageText = messageText;
        this.timestamp = timestamp;
    }

    public Sms(long id, String phoneNumber, String callerName, String messageText, String timestamp) {
        this(phoneNumber, callerName, messageText, timestamp);
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getCallerName() {
        return callerName;
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
                ", callerName='" + callerName + '\'' +
                ", messageText='" + messageText + '\'' +
                ", timestamp='" + timestamp + '\'';
    }
}
