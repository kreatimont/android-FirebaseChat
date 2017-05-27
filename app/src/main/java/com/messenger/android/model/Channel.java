package com.messenger.android.model;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.List;

import static com.messenger.android.database.FBDatabaseManager.TableMessage;

@IgnoreExtraProperties
public class Channel {

    private List<String> users;
    private String firebaseId;
    private String title;

    public Channel() {
    }

    public Channel(List<String> users, String firebaseId, String title) {
        this.users = users;
        this.firebaseId = firebaseId;
        this.title = title;
    }

    public List<String> getUsers() {
        return users;
    }

    public void setUsers(List<String> users) {
        this.users = users;
    }

    public String getFirebaseId() {
        return firebaseId;
    }

    public void setFirebaseId(String firebaseId) {
        this.firebaseId = firebaseId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
