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

    public void registerNewUser(User user){
        userFirebase.registerNewUser(firebaseDatabase,user);
    }

    public void signInUser(User user){
        userFirebase.signInUser(user);
    }

    public void logoutUser(){
        userFirebase.logoutUser();
    }

    public void getCurrentUser(Model.GetCurrentUserListener listener){
       userFirebase.getCurrentUser(firebaseDatabase,listener);
    }

    public void addCarToDB(Car car){
        carFirebase.addCarToDB(firebaseDatabase, car);
    }

}
