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
    private LastUpdateFirebase lastUpdateFirebase;

    public ModelFirebase(){
        firebaseDatabase = FirebaseDatabase.getInstance();
        carFirebase = new CarFirebase();
        userFirebase = new UserFirebase();
        parkingFirebase = new ParkingFirebase();
        lastUpdateFirebase = new LastUpdateFirebase();
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

    public void getListOfAllCarsInDB(Model.SyncListener listener){
        carFirebase.getListOfAllCarsInDB(firebaseDatabase,listener);
    }

    // ---- Parking Functions ---- //
    public void parkCar(Parking parking, Model.SyncListener listener){
        parkingFirebase.parkCar(firebaseDatabase,parking,listener);
    }

    public void getMyUnparkedCars(String uid, Model.SyncListener listener){
        parkingFirebase.getMyUnparkedCars(firebaseDatabase,uid,listener);
    }

    public void getAllMyParkedCars(Model.SyncListener listener){
        parkingFirebase.getAllMyParkedCars(listener);
    }

    public void getAllMyParkingSpots(Model.SyncListener listener){
        parkingFirebase.getAllMyParkingSpots(firebaseDatabase,listener);
    }

    public void stopParking(Parking parking){
        parkingFirebase.stopParking(firebaseDatabase,parking);
    }

    public void stopParking(Car car){
        parkingFirebase.stopParking(firebaseDatabase,car);
    }

    // ---- Last Update Functions --- //

    public void updateParkingDbTime(long currentTime){
        lastUpdateFirebase.updateParkingDbTime(firebaseDatabase,currentTime);
    }

    public void getParkingDbTime(Model.SyncListener listener){
        lastUpdateFirebase.getParkingDbTime(firebaseDatabase,listener);
    }

    public void updateCarDbTime(long currentTime){
        lastUpdateFirebase.updateCarDbTime(firebaseDatabase,currentTime);
    }

    public void getCarDbTime(Model.SyncListener listener){
        lastUpdateFirebase.getCarDbTime(firebaseDatabase,listener);
    }

    public void updateUsersDbTime(long currentTime){
        lastUpdateFirebase.updateUsersDbTime(firebaseDatabase,currentTime);
    }

    public void getUsersDbTime(Model.SyncListener listener){
        lastUpdateFirebase.getUsersDbTime(firebaseDatabase,listener);
    }

}
