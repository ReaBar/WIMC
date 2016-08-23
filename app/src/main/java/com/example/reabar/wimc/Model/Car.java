package com.example.reabar.wimc.Model;

import android.util.Log;
import android.widget.Toast;
import com.example.reabar.wimc.MyApplication;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by reabar on 25.5.2016.
 */
public class Car {
    private String carId, color, model, company, userOwnerId;
    private ArrayList<String> usersList;
    private Boolean parkingIsActive;
    private String TAG = "CarClass";

    public Car() {
        usersList = new ArrayList<>();
    }

    public Car(String carId, String color, String model, String company, String userOwnerId) {
        this.carId = carId;
        this.color = color;
        this.model = model;
        this.company = company;
        this.userOwnerId = userOwnerId;
        usersList = new ArrayList<>();
        parkingIsActive = false;
    }

    public String getCarId() {
        return carId;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getUserOwnerId() {
        return userOwnerId;
    }

    public void setUserOwnerId(String userOwnerId) {
        this.userOwnerId = userOwnerId;
    }

    public void setUsersList(ArrayList<String> users){
        usersList = users;
    }

    public List<String> getUsersList() {
        return usersList;
    }

    public Boolean getParkingIsActive() {
        return parkingIsActive;
    }

    public void setParkingIsActive(Boolean parkingIsActive) {
        this.parkingIsActive = parkingIsActive;
    }

    protected void updateThisCar(){
        Model.getInstance().updateCar(this, new Model.SyncListener() {
            @Override
            public void isSuccessful(boolean success) {
                if(success){
                    Log.d(TAG, "Car updated!");
                }
            }

            @Override
            public void failed(String message) {

            }

            @Override
            public void passData(Object data) {

            }
        });
    }

    public void setNewCarUser(final String email) {
        Model.getInstance().getUsersList(new Model.SyncListener() {
            @Override
            public void isSuccessful(boolean success) {

            }

            @Override
            public void failed(String message) {

            }

            @Override
            public void passData(Object data) {
                if (data instanceof List) {
                    if (!usersList.contains(email)) {
                        for (User user: (List<User>)data) {
                            if(user.getEmail().equals(email)){
                                usersList.add(email);
                                Toast.makeText(MyApplication.getAppActivity(), "User added To Car!",
                                        Toast.LENGTH_SHORT).show();
                                updateThisCar();
                                return;
                            }
                            else{
                                Toast.makeText(MyApplication.getAppContext(), "User not found or already shared with", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else {
                        Toast.makeText(MyApplication.getAppContext(), "User not found or already shared with", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    public void removeCarUser(final String uId){
        if(usersList.contains(uId)){
            usersList.remove(uId);
            updateThisCar();
        }
    }



}
