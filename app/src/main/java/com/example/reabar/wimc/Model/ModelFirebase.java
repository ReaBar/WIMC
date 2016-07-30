package com.example.reabar.wimc.Model;

import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

/**
 * Created by reabar on 27.6.2016.
 */
public class ModelFirebase {
    private FirebaseDatabase firebaseDatabase;
    private CarFirebase carFirebase;
    private UserFirebase userFirebase;
    private ParkingFirebase parkingFirebase;

    public ModelFirebase(){
        firebaseDatabase = FirebaseDatabase.getInstance();
        carFirebase = new CarFirebase();
        userFirebase = new UserFirebase();
        parkingFirebase = new ParkingFirebase();
    }

    // ---- User Functions ---- //
    public void signupUser(User user, String password, final Model.SyncListener listener){
        userFirebase.signupUser(firebaseDatabase,user,password,listener);
    }

    public void signInUser(User user,String password, final Model.SyncListener listener){
        userFirebase.signInUser(user, password, listener);
    }

    public void logoutUser(){
        userFirebase.logoutUser();
    }

    public void getCurrentUser(){
       userFirebase.getCurrentUser();
    }

    public void resetPassword(String email, final Model.SyncListener listener){
        userFirebase.resetPassword(email, listener);
    }

    public void updatePassword(String newPassword, final Model.SyncListener listener){
        userFirebase.updatePassword(newPassword, listener);
    }


    // ---- Car Functions ---- //
    public void addCarToDB(Car car, final Model.SyncListener listener){
        carFirebase.addCarToDB(firebaseDatabase, car, listener);
    }

    public void removeCarFromDB(Car car){
        carFirebase.removeCarFromDB(firebaseDatabase,car);
    }

    public void updateCar(Car car,Model.SyncListener listener){
        carFirebase.updateCar(firebaseDatabase, car,listener);
    }

    public List<String> getUsersList(Model.SyncListener listener){
       return userFirebase.getUsersList(firebaseDatabase, listener);
    }

    public void getOwnedCars(String uId,Model.SyncListener listener){
        carFirebase.getOwnedCars(firebaseDatabase,uId,listener);
    }

    public void getListOfSharedCars(String uId, Model.SyncListener listener){
        carFirebase.getListOfSharedCars(firebaseDatabase,uId,listener);
    }

    // ---- Parking Functions ---- //
    public void parkCar(Parking parking, Model.SyncListener listener){
        parkingFirebase.parkCar(firebaseDatabase,parking,listener);
    }

}
