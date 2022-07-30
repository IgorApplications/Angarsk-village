package com.iapp.angara.database;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

public class Report implements Element {

    public static final String ID = "id";
    public static final String SENDER_ID = "senderId";
    public static final String SENDER_NAME = "senderName";
    public static final String GUILTY_ID = "guiltyId";
    public static final String GUILTY_NAME = "guiltyName";
    public static final String CAUSE = "cause";
    public static final String MESSAGE = "message";
    public static final String REVIEWED = "reviewed";
    public static final String RECEIVED = "received";
    public static final String REJECTED = "rejected";

    private long id;
    private long senderId;
    private String senderName;
    private long guiltyId;
    private String guiltyName;
    private String cause;
    private String message;
    private boolean reviewed;
    private boolean received;
    private boolean rejected;

    public Report() {}

    public Report(long id, long senderId, String senderName, long guiltyId,
                  String guiltyName, String cause, String message) {
        this.id = id;
        this.senderId = senderId;
        this.senderName = senderName;
        this.guiltyId = guiltyId;
        this.guiltyName = guiltyName;
        this.cause = cause;
        this.message = message;
    }

    @Override
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getSenderId() {
        return senderId;
    }

    public void setSenderId(long senderId) {
        this.senderId = senderId;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public long getGuiltyId() {
        return guiltyId;
    }

    public void setGuiltyId(long guiltyId) {
        this.guiltyId = guiltyId;
    }

    public String getGuiltyName() {
        return guiltyName;
    }

    public void setGuiltyName(String guiltyName) {
        this.guiltyName = guiltyName;
    }

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isReviewed() {
        return reviewed;
    }

    public void setReviewed(boolean reviewed) {
        this.reviewed = reviewed;
    }

    public boolean isReceived() {
        return received;
    }

    public void setReceived(boolean received) {
        this.received = received;
    }

    public boolean isRejected() {
        return rejected;
    }

    public void setRejected(boolean rejected) {
        this.rejected = rejected;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Report report = (Report) o;
        return id == report.id && senderId == report.senderId && guiltyId == report.guiltyId && reviewed == report.reviewed && received == report.received && Objects.equals(senderName, report.senderName) && Objects.equals(guiltyName, report.guiltyName) && Objects.equals(cause, report.cause) && Objects.equals(message, report.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, senderId, senderName, guiltyId, guiltyName, cause, message, reviewed, received);
    }

    @Override
    public String toString() {
        return "Report{" +
                "id=" + id +
                ", senderId=" + senderId +
                ", senderName='" + senderName + '\'' +
                ", guiltyId=" + guiltyId +
                ", guiltyName='" + guiltyName + '\'' +
                ", cause='" + cause + '\'' +
                ", message='" + message + '\'' +
                ", reviewed=" + reviewed +
                ", received=" + received +
                '}';
    }
}
