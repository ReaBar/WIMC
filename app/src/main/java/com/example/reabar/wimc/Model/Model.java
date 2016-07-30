package com.example.reabar.wimc.Model;

import com.google.firebase.auth.FirebaseAuth;

import java.util.LinkedList;
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

    public void signupUser(final User user, final String password, final SyncListener listener){
        modelFirebase.signupUser(user, password, listener);
    }

    public void signInUser(User user, String password, final SyncListener listener){
        modelFirebase.signInUser(user, password, listener);
    }

    public void logoutUser(){
        modelFirebase.logoutUser();
    }

    public User getCurrentUser(){
       modelFirebase.getCurrentUser();
        return currentUser;
    }

    public String getCurrentUserID(){
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    public void resetPassword(String email, final SyncListener listener){
        modelFirebase.resetPassword(email, listener);
    }

    public void updatePassword(String newPassword, final SyncListener listener){
        modelFirebase.updatePassword(newPassword, listener);
    }

    public List<String> getUsersList(Model.SyncListener listener){
        return modelFirebase.getUsersList(listener);
    }

    public void getOwnedCars(String uId,SyncListener listener){
        modelFirebase.getOwnedCars(uId,listener);
    }

    public void getListOfSharedCars(String uId, SyncListener listener){
        modelFirebase.getListOfSharedCars(uId,listener);
    }

    public void addCarToDB(final Car car, final SyncListener listener){
        modelFirebase.addCarToDB(car, listener);
    }

    public void updateCar(Car car,Model.SyncListener listener){
        modelFirebase.updateCar(car,listener);
    }

    private List<Car> data = new LinkedList<Car>();
    public List<Car> getAllCars(){
        return data;
    }

    public void parkCar(Parking parking, SyncListener listener){
        modelFirebase.parkCar(parking,listener);
    }

    public void getMyUnparkedCars(String uid, SyncListener listener){
        modelFirebase.getMyUnparkedCars(uid, listener);
    }

    //--- Listeners ---- //
    public interface SyncListener{
        void isSuccessful(boolean success);
        void failed(String message);
        void PassData(Object data);
    }
}
