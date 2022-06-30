package com.iapp.angara.database;

import java.util.Date;

public class Message {

    private String email;
    private String userName;
    private String message;
    private Date date;

    public Message() {}

    public Message(String email, String userName, String message) {
        this.email = email;
        this.userName = userName;
        this.email = email;
        this.message = message;
        date = new Date();
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
}
