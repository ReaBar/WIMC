package com.example.reabar.wimc.Model;

import java.util.List;

/**
 * Created by reabar on 25.5.2016.
 */
public class Car {
    private String carId, color, model, company, userOwnerId;
    private List<String> usersList;

    public Car(String carId, String color, String model, String company, String userOwnerId) {
        this.carId = carId;
        this.color = color;
        this.model = model;
        this.company = company;
        this.userOwnerId = userOwnerId;
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

    public List<String> getUsersList() {
        return usersList;
    }

    public void setUsersList(String user) {
        this.usersList.add(user);
    }
}
