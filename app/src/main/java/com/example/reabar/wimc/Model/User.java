package com.example.reabar.wimc.Model;

/**
 * Created by reabar on 25.5.2016.
 */
public class User {
    private String email, userObjectId, password;

    public User(){

    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
