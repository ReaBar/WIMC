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

    public void registerNewUser(String email, String password){
        userFirebase.registerNewUser(email, password);
    }

    public void signInUser(String email, String password){
        userFirebase.signInUser(email,password);
    }

    public void logoutUser(){
        userFirebase.logoutUser();
    }

    public User getCurrentUser(){
       return userFirebase.getCurrentUser(firebaseDatabase);
    }

    public void addCarToDB(Car car){
        carFirebase.addCarToDB(firebaseDatabase, car);
    }


}
