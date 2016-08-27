package com.example.reabar.wimc.Model;

import com.example.reabar.wimc.MyApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by reabar on 25.5.2016.
 */
public class Car {
    private String carId, color, model, company, userOwnerId;
    private List<String> usersList;
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

    public void setUsersList(List<String> users) {
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

/*    protected void updateThisCar() {
        Model.getInstance().updateCar(this, new Model.SyncListener() {
            @Override
            public void isSuccessful(boolean success) {
                if (success) {
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
    }*/

    public void setNewCarUser(String email){
        if(!usersList.contains(email)){
            usersList.add(email);
        }
    }

/*    public void setCarUser(final String email, final boolean newUserOrJustSQL) {
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
                    if (!usersList.contains(email) && !email.equals(Model.getInstance().getCurrentUser().getEmail())) {
                        for (User user : (List<User>) data) {
                            if (user.getEmail().equals(email)) {
                                usersList.add(email);
                                if (newUserOrJustSQL) {
                                    Toast.makeText(MyApplication.getAppActivity(), "User added To Car!",
                                            Toast.LENGTH_SHORT).show();
                                }
                                updateThisCar();
                                return;
                            }
                        }
                    } else {
                        if (newUserOrJustSQL) {
                            Toast.makeText(MyApplication.getAppContext(), "User not found or already shared with", Toast.LENGTH_SHORT).show();
                        }
                        return;
                    }
                }
                if (newUserOrJustSQL) {
                    Toast.makeText(MyApplication.getAppContext(), "User not found or already shared with", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }*/

    public void removeCarUser(final String uId) {
        if (usersList.contains(uId)) {
            usersList.remove(uId);
            //updateThisCar();
            Model.getInstance().updateCar(this);
        }
    }
}
