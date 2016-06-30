package com.example.reabar.wimc.Model;

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

    public void registerNewUser(final User user){
        new Thread(new Runnable() {
            @Override
            public void run() {
                modelFirebase.registerNewUser(user);
            }
        }).start();
    }

    public void signInUser(User user){
        modelFirebase.signInUser(user);
    }

    public void logoutUser(){
        modelFirebase.logoutUser();
    }

    public interface GetCurrentUserListener{
        public void onResult(User user);
        public void onCancel();
    }

    public void getCurrentUser(GetCurrentUserListener listener){
       modelFirebase.getCurrentUser(listener);
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
