package com.example.reabar.wimc.Model;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by reabar on 28/06/2016.
 */
public class Model {
    private static Model ourInstance = new Model();

    private ModelFirebase modelFirebase;
    private ModelSql modelSql;

    public static Model getInstance() {
        return ourInstance;
    }

    private Model() {
        modelFirebase = new ModelFirebase();
        modelSql = new ModelSql();
    }

    public void registerNewUser(User user){
        modelFirebase.registerNewUser(user.getEmail(),user.getPassword());
    }

    public void signInUser(User user){
        modelFirebase.signInUser(user.getEmail(),user.getPassword());
    }

    public void logoutUser(){
        modelFirebase.logoutUser();
    }

    public User getCurrentUser(){
        return modelFirebase.getCurrentUser();
    }

    public void addCarToDB(final Car car){
        new Thread(new Runnable() {
            @Override
            public void run() {
                modelFirebase.addCarToDB(car);
            }
        }).start();

    }
}
