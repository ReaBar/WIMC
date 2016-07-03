package com.example.reabar.wimc.Model;

import android.app.Activity;
import android.content.Context;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by reabar on 27.6.2016.
 */
public class ModelFirebase {
    private FirebaseDatabase firebaseDatabase;
    private CarFirebase carFirebase;
    private UserFirebase userFirebase;

    public ModelFirebase(){
        firebaseDatabase = FirebaseDatabase.getInstance();
        carFirebase = new CarFirebase();
        userFirebase = new UserFirebase();
    }

    public void signupUser(User user, String password){
        userFirebase.signupUser(firebaseDatabase,user,password);
    }

    public void signInUser(User user,String password){
        userFirebase.signInUser(user,password);
    }

    public void logoutUser(){
        userFirebase.logoutUser();
    }

    public void getCurrentUser(Model.GetCurrentUserListener listener){
       userFirebase.getCurrentUser(firebaseDatabase,listener);
    }

    public void resetPassword(){
        userFirebase.resetPassword();
    }

    public void addCarToDB(Car car){
        carFirebase.addCarToDB(firebaseDatabase, car);
    }

}
