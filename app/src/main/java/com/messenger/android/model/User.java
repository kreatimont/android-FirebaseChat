package com.messenger.android.model;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class User {

    private String firebaseId;
    private String login;
    private String name;

    public User () {
    }

    public User(String firebaseId, String login, String name) {
        this.firebaseId = firebaseId;
        this.login = login;
        this.name = name;
    }

    public String getFirebaseId() {
        return firebaseId;
    }

    public void setFirebaseId(String firebaseId) {
        this.firebaseId = firebaseId;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
