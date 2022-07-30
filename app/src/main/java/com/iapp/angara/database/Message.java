package com.iapp.angara.database;

import androidx.annotation.NonNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.TimeZone;

public class Message implements Element {

    private static final Calendar GMT_CALENDAR = Calendar.getInstance(TimeZone.getTimeZone("GMT"));

    public static final String ID = "id";
    public static final String USER_ID = "userId";
    public static final String EMAIL = "email";
    public static final String USER_NAME = "userName";
    public static final String MESSAGE = "message";
    public static final String TIME = "time";
    public static final String PINNED = "pinned";

    private long id;
    private long userId;
    private String email;
    private String userName;
    private String message;
    private Date date;
    private boolean pinned;

    public Message() {}

    public Message(long messageId, long userId, String email, String userName, String message) {
        this.id = messageId;
        this.userId = userId;
        this.email = email;
        this.userName = userName;
        this.message = message;

        GMT_CALENDAR.setTime(new Date());
        date = GMT_CALENDAR.getTime();
    }

    @Override
    public long getId() {
        return id;
    }

    public void setId(long idMessage) {
        this.id = idMessage;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTime() {
        return date.getTime();
    }

    public void setTime(long time) {
        date = new Date(time);
    }

    public boolean isMessageOf(String userName) {
        return this.userName.equals(userName);
    }

    public void setPinned(boolean pinned) {
        this.pinned = pinned;
    }

    public boolean isPinned() {
        return pinned;
    }

    @NonNull
    @Override
    public String toString() {
        SimpleDateFormat formatDate = new SimpleDateFormat("dd.MM.yyyy hh:mm");

        return String.format(
                "id = %d\n" +
                "email = %s\n " +
                "userName = %s\n" +
                "message = %s\n" +
                formatDate.format(date),
                id, email, userName, message);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message1 = (Message) o;
        return id == message1.id && userId == message1.userId && pinned == message1.pinned && Objects.equals(email, message1.email) && Objects.equals(userName, message1.userName) && Objects.equals(message, message1.message) && Objects.equals(date, message1.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, email, userName, message, date, pinned);
    }
}
