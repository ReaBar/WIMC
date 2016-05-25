package com.example.reabar.wimc.Model;

/**
 * Created by reabar on 25.5.2016.
 */
public class User {
    private String userName, userObjectId, password;

    public User(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserId() {
        return userObjectId;
    }

    // TODO: 25.5.2016 set user id from firebase after pushing
    public void setUserId(String userId) {
        this.userObjectId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
