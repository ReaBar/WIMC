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

    private void updateThisCar(){
        Model.getInstance().updateCar(this, new Model.SyncListener() {
            @Override
            public void isSuccessful(boolean success) {
                if(success){
                    Log.d("UpdateThisCar", "Car updated!");
                }
            }

            @Override
            public void failed(String message) {

            }

            @Override
            public void PassData(Object data) {

            }
        });
    }

    public void setNewCarUser(final String uId) {
        Model.getInstance().getUsersList(new Model.SyncListener() {
            @Override
            public void isSuccessful(boolean success) {

            }

            @Override
            public void failed(String message) {

            }

            @Override
            public void PassData(Object data) {
                if (data instanceof List) {
                    usersList = (ArrayList<String>) data;
                    if (((List<User>) data).contains(uId)) {
                        usersList.add(uId);
                    } else {
                        Toast.makeText(MyApplication.getAppContext(), "User already in the DB", Toast.LENGTH_SHORT).show();
                    }
                }
                updateThisCar();
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
