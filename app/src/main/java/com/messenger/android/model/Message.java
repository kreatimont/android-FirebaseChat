package com.messenger.android.model;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Date;

@IgnoreExtraProperties
public class Message {

    private String title;
    private String message;
    private Date timestamp;
    private NoteGrade grade;
    private String userId;
    private String userName;

    public Message() {}


    public Message(String title, String message, String userId, String userName) {
        this.title = title;
        this.message = message;
        this.grade = NoteGrade.Light;
        this.timestamp = new Date(System.currentTimeMillis());
        this.userId = userId;
        this.userName = userName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public NoteGrade getGrade() {
        return grade;
    }

    public void setGrade(NoteGrade grade) {
        this.grade = grade;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
