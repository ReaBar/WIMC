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

    public ModelFirebase(){
        firebaseDatabase = FirebaseDatabase.getInstance();
        carFirebase = new CarFirebase();
        userFirebase = new UserFirebase();
    }

    // ---- User Functions ---- //
    public void signupUser(User user, String password, final Model.SignUpListener listener){
        userFirebase.signupUser(firebaseDatabase,user,password,listener);
    }

    public void signInUser(User user,String password, final Model.LoginListener listener){
        userFirebase.signInUser(user, password, listener);
    }

    public void logoutUser(){
        userFirebase.logoutUser();
    }

    public void getCurrentUser(){
       userFirebase.getCurrentUser();
    }

    public void resetPassword(String email, final Model.ResetPasswordListener listener){
        userFirebase.resetPassword(email, listener);
    }

    public void updatePassword(String newPassword, final Model.UpdatePasswordListener listener){
        userFirebase.updatePassword(newPassword, listener);
    }


    // ---- Car Functions ---- //
    public void addCarToDB(Car car, final Model.AddNewCarListener listener){
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

}
