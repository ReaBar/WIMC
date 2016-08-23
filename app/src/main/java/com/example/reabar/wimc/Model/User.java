package com.example.reabar.wimc.Model;

/**
 * Created by reabar on 25.5.2016.
 */
public class User {
    private String email, userObjectId;

    public User(){}

    public User(String email) {
        this.email = email.toLowerCase();
    }

    public User(String userObjectId, String email) {
        this.email = email;
        this.userObjectId = userObjectId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email.toLowerCase();
    }

    public String getUserId() {
        return userObjectId;
    }

    public void setUserId(String userId) {
        this.userObjectId = userId;
    }
}
