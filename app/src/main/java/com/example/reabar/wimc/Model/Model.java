package com.example.reabar.wimc.Model;

import java.util.List;

/**
 * Created by reabar on 28/06/2016.
 */
public class Model {
    private static Model ourInstance = new Model();

    private ModelFirebase modelFirebase;
    private ModelSql modelSql;
    private User currentUser;

    public static Model getInstance() {
        return ourInstance;
    }

    private Model() {
        modelFirebase = new ModelFirebase();
        modelSql = new ModelSql();
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public void signupUser(final User user, final String password){
        new Thread(new Runnable() {
            @Override
            public void run() {
                modelFirebase.signupUser(user, password);
            }
        }).start();
    }

    public void signInUser(User user, String password){
        modelFirebase.signInUser(user,password);
    }

    public void logoutUser(){
        modelFirebase.logoutUser();
    }

    public interface GetCurrentUserListener{
        public void onResult(User user);
        public void onCancel();
    }

    public User getCurrentUser(){
       modelFirebase.getCurrentUser();
        return currentUser;
    }

    public void resetPassword(){
        modelFirebase.resetPassword();
    }

    public void addCarToDB(final Car car){
        new Thread(new Runnable() {
            @Override
            public void run() {
                modelFirebase.addCarToDB(car);
            }
        }).start();
    }

    public void updateCar(Car car){
        modelFirebase.updateCar(car);
    }

    public List<String> getUsersList(){
        return modelFirebase.getUsersList();
    }
}
