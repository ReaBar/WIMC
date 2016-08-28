package com.example.reabar.wimc.Model;

import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.example.reabar.wimc.FilesManagerHelper;
import com.example.reabar.wimc.MyApplication;
import com.google.firebase.auth.FirebaseAuth;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by reabar on 28/06/2016.
 */
public class Model {
    private static Model ourInstance = new Model();
    private Bitmap image;
    private ModelFirebase modelFirebase;
    private ModelCloudinary modelCloudinary;
    private ModelSql modelSql;
    private User currentUser;
    private static boolean imageBool;
    private FilesManagerHelper fileManager;

    public static Model getInstance() {
        if (ourInstance == null) {
            ourInstance = new Model();
        }
        return ourInstance;
    }

    private Model() {
        fileManager = new FilesManagerHelper(MyApplication.getAppContext());
        modelFirebase = new ModelFirebase();
        modelSql = new ModelSql();
        modelCloudinary = new ModelCloudinary(MyApplication.getAppContext());
        getAPIVersion();
        User tempUser = currentUser;
        if (tempUser != null) {
            modelFirebase.getOwnedCars(currentUser.getEmail(), new SyncListener() {
                @Override
                public void isSuccessful(boolean success) {

                }

                @Override
                public void failed(String message) {

                }

                @Override
                public void passData(Object data) {
                    for (Car car : (List<Car>) data) {
                        modelSql.addCar(car);
                    }
                    modelSql.updateCarsDbTime(System.currentTimeMillis());
                }
            });
        } else {
            modelFirebase.getListOfAllCarsInDB(new SyncListener() {
                @Override
                public void isSuccessful(boolean success) {

                }

                @Override
                public void failed(String message) {

                }

                @Override
                public void passData(Object data) {
                    for (Car car : (List<Car>) data) {
                        modelSql.addCar(car);
                    }
                    modelSql.updateCarsDbTime(System.currentTimeMillis());
                }
            });
        }

        modelFirebase.getMyParkingSpots(new SyncListener() {
            @Override
            public void isSuccessful(boolean success) {

            }

            @Override
            public void failed(String message) {

            }

            @Override
            public void passData(Object data) {
                for (Parking parking : (List<Parking>) data) {
                    modelSql.parkCar(parking);
                }
                modelSql.updateParkingDbTime(System.currentTimeMillis());
            }
        });

        modelFirebase.getUsersList(new SyncListener() {
            @Override
            public void isSuccessful(boolean success) {

            }

            @Override
            public void failed(String message) {

            }

            @Override
            public void passData(Object data) {
                for (User user : (List<User>) data) {
                    modelSql.addUser(user);
                }
                modelSql.updateUsersDbTime(System.currentTimeMillis());
            }
        });
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public void signupUser(final User user, final String password, final SyncListener listener) {
        modelFirebase.signupUser(user, password, listener);
        modelSql.addUser(user);
        updateUsersDbTime();
    }

    public void signInUser(User user, String password, final SyncListener listener) {
        currentUser = user;
        modelFirebase.signInUser(user, password, listener);
    }

    public void logoutUser() {
        modelFirebase.logoutUser();
        currentUser = null;
        ourInstance = null;
    }

    public User getCurrentUser() {
        modelFirebase.getCurrentUser();
        return currentUser;
    }

    public String getCurrentUserID() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    public void resetPassword(String email, final SyncListener listener) {
        modelFirebase.resetPassword(email, listener);
    }

    public void updatePassword(String newPassword, final SyncListener listener) {
        modelFirebase.updatePassword(newPassword, listener);
    }

    public void getUsersList(final Model.SyncListener listener) {
        final String lastUpdateDate = modelSql.getUsersLastUpdateTime();
        final List<User> usersList = new ArrayList<>();
        modelFirebase.getUsersDbTime(new SyncListener() {
            @Override
            public void isSuccessful(boolean success) {

            }

            @Override
            public void failed(String message) {

            }

            @Override
            public void passData(Object data) {
                if (data == null || lastUpdateDate == null || data.toString().compareTo(lastUpdateDate) > 0) {
                    modelFirebase.getUsersList(new SyncListener() {
                        @Override
                        public void isSuccessful(boolean success) {

                        }

                        @Override
                        public void failed(String message) {

                        }

                        @Override
                        public void passData(Object data) {
                            if (data != null) {
                                for (User user : (List<User>) data) {
                                    modelSql.addUser(user);
                                }
                                modelSql.updateUsersDbTime(System.currentTimeMillis());

                                listener.passData(data);
                            }
                        }
                    });
                    if (data == null) {
                        modelFirebase.updateUsersDbTime(System.currentTimeMillis());
                    }
                } else {
                    modelSql.getUsersList(listener);
                }
            }
        });
    }

    public void getOwnedCars(final String uId, final SyncListener listener) {
        final String lastUpdateDate = modelSql.getCarLastUpdate();
        final List<Car> carsList = new ArrayList<>();
        modelFirebase.getCarDbTime(new SyncListener() {
            @Override
            public void isSuccessful(boolean success) {

            }

            @Override
            public void failed(String message) {

            }

            @Override
            public void passData(Object data) {
                if (data == null) {
                    listener.passData(carsList);
                } else if (lastUpdateDate == null || data.toString().compareTo(lastUpdateDate) > 0) {
                    modelFirebase.getOwnedCars(uId, new SyncListener() {
                        @Override
                        public void isSuccessful(boolean success) {

                        }

                        @Override
                        public void failed(String message) {

                        }

                        @Override
                        public void passData(Object data) {
                            if (data != null) {
                                for (Car car : (List<Car>) data) {
                                    if (modelSql.getCarById(car.getCarId()) == null) {
                                        modelSql.addCar(car);
                                    }
                                }
                                modelSql.updateCarsDbTime(System.currentTimeMillis());
                                listener.passData(data);
                            }
                        }
                    });
                } else {
                    modelSql.getOwnedCars(uId, listener);
                }
            }
        });
    }

    public void getListOfSharedCars(final String uId, final SyncListener listener) {
        final String lastUpdateDate = modelSql.getCarLastUpdate();
        final List<Car> carsList = new ArrayList<>();
        modelFirebase.getCarDbTime(new SyncListener() {
            @Override
            public void isSuccessful(boolean success) {

            }

            @Override
            public void failed(String message) {

            }

            @Override
            public void passData(Object data) {
                if (data == null) {
                    listener.passData(carsList);
                } else if (lastUpdateDate == null || data.toString().compareTo(lastUpdateDate) > 0) {
                    modelFirebase.getListOfSharedCars(uId, new SyncListener() {
                        @Override
                        public void isSuccessful(boolean success) {

                        }

                        @Override
                        public void failed(String message) {

                        }

                        @Override
                        public void passData(Object data) {
                            if (data != null) {
                                for (Car car : (List<Car>) data) {
                                    if (modelSql.getCarById(car.getCarId()) == null) {
                                        modelSql.addCar(car);
                                    }
                                }
                                modelSql.updateCarsDbTime(System.currentTimeMillis());
                                listener.passData(data);
                            }
                        }
                    });
                } else {
                    modelSql.getListOfSharedCars(uId, listener);
                }
            }
        });
    }

    public void addCarToDB(final Car car, final SyncListener listener) {
        modelFirebase.addCarToDB(car, listener);
        modelSql.addCar(car);
        updateCarDbTime();
    }

    public void setCarUser(final Car car, final String userEmail) {
        //new
        List<User> allUsers = modelSql.getUsersList();
        final List<String> userEmails = new ArrayList<>();

        if (allUsers.size() == 0) {
            modelFirebase.getUsersList(new SyncListener() {
                @Override
                public void isSuccessful(boolean success) {

                }

                @Override
                public void failed(String message) {

                }

                @Override
                public void passData(Object data) {
                    if (data != null) {
                        for (User user : (List<User>) data) {
                            userEmails.add(user.getEmail());
                        }
                        if (userEmails.contains(userEmail) && !car.getUsersList().contains(userEmail) && !userEmail.equals(Model.getInstance().getCurrentUser().getEmail())) {
                            car.setNewCarUser(userEmail);
                            Toast.makeText(MyApplication.getAppActivity(), "User added To Car!",
                                    Toast.LENGTH_SHORT).show();
                            updateCar(car);
                        } else {
                            Toast.makeText(MyApplication.getAppContext(), "User not found or already shared with", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        } else {
            for (User user : allUsers) {
                if (user.getEmail().equals(userEmail) && !car.getUsersList().contains(userEmail) && !userEmail.equals(Model.getInstance().getCurrentUser().getEmail())) {
                    car.setNewCarUser(userEmail);
                    Toast.makeText(MyApplication.getAppActivity(), "User added To Car!",
                            Toast.LENGTH_SHORT).show();
                    updateCar(car);
                    return;
                }
            }
            Toast.makeText(MyApplication.getAppContext(), "User not found or already shared with", Toast.LENGTH_SHORT).show();
        }
    }

    public void setCarUsersList(final Car car, final List<String> usersList) {
        //new

        List<User> allUsers = modelSql.getUsersList();
        final List<String> userEmails = new ArrayList<>();

        if (allUsers.size() == 0) {
            modelFirebase.getUsersList(new SyncListener() {
                @Override
                public void isSuccessful(boolean success) {

                }

                @Override
                public void failed(String message) {

                }

                @Override
                public void passData(Object data) {
                    if (data != null) {
                        for (User user : (List<User>) data) {
                            userEmails.add(user.getEmail());
                        }
                        for (int i = 0; i < usersList.size(); i++) {
                            if (!userEmails.contains(usersList.get(i))) {
                                usersList.remove(usersList.get(i));
                            }
                        }
                        car.setUsersList(usersList);
                        //updateCar(car);
                    }
                }
            });
        } else {
            for (User user : allUsers) {
                userEmails.add(user.getEmail());
            }

            for (int i = 0; i < usersList.size(); i++) {
                if (!userEmails.contains(usersList.get(i))) {
                    usersList.remove(usersList.get(i));
                }
            }
            car.setUsersList(usersList);
            //updateCar(car);
        }

//        for (String carUser:usersList) {
//            if(!userEmails.contains(carUser)){
//                usersList.remove(carUser);
//            }
//        }
    }


    public void updateCar(final Car car) {
        //new
        modelSql.updateCar(car);
        modelFirebase.updateCar(car);
        updateCarDbTime();
    }

//    public void updateCar(final Car car, Model.SyncListener listener) {
//        modelSql.updateCar(car);
//        modelFirebase.updateCar(car, listener);
//        updateCarDbTime();
//    }

    public void getAllCars(final SyncListener listener) {
        final String lastUpdateDate = modelSql.getCarLastUpdate();
        final ArrayList<Car> carList = new ArrayList<>();
        modelFirebase.getCarDbTime(new SyncListener() {
            @Override
            public void isSuccessful(boolean success) {

            }

            @Override
            public void failed(String message) {

            }

            @Override
            public void passData(Object data) {
                if (data == null) {
                    listener.passData(carList);
                } else if (lastUpdateDate == null || data.toString().compareTo(lastUpdateDate) > 0) {
                    modelFirebase.getListOfAllCarsInDB(new SyncListener() {
                        @Override
                        public void isSuccessful(boolean success) {

                        }

                        @Override
                        public void failed(String message) {

                        }

                        @Override
                        public void passData(Object data) {
                            for (Car car : (ArrayList<Car>) data) {
                                if (modelSql.getCarById(car.getCarId()) == null) {
                                    modelSql.addCar(car);
                                }
                            }
                            modelSql.updateCarsDbTime(System.currentTimeMillis());
                            listener.passData(data);
                        }
                    });
                } else {
                    modelSql.getAllCars(listener);
                }
            }
        });
    }

//    public Car getCarById(String carId) {
//        return modelSql.getCarById(carId);
//    }

    public void parkCar(Parking parking, SyncListener listener) {
        modelFirebase.parkCar(parking, listener);
        modelSql.parkCar(parking);
        updateParkingDbTime();
    }

    public void getMyUnparkedCars(final String uId, final SyncListener listener) {
        final String parkingLastUpdateDate = modelSql.getParkingLastUpdate();
        final String carLastUpdateDate = modelSql.getCarLastUpdate();
        modelFirebase.getParkingDbTime(new SyncListener() {
            @Override
            public void isSuccessful(boolean success) {

            }

            @Override
            public void failed(String message) {

            }

            @Override
            public void passData(Object data) {
                if (data == null || parkingLastUpdateDate == null || data.toString().compareTo(parkingLastUpdateDate) > 0) {
                    modelSql.dropCarDb();
                    modelSql.dropParkingDb();
                    modelFirebase.getMyUnparkedCars(uId, new SyncListener() {
                        @Override
                        public void isSuccessful(boolean success) {

                        }

                        @Override
                        public void failed(String message) {

                        }

                        @Override
                        public void passData(Object data) {
                            modelFirebase.getListOfAllCarsInDB(new SyncListener() {
                                @Override
                                public void isSuccessful(boolean success) {

                                }

                                @Override
                                public void failed(String message) {

                                }

                                @Override
                                public void passData(Object data) {
                                    if (data != null) {
                                        for (Car car : (List<Car>) data) {
                                            if (modelSql.getCarById(car.getCarId()) == null) {
                                                modelSql.addCar(car);
                                            }
                                        }
                                        modelSql.updateCarsDbTime(System.currentTimeMillis());
                                    }
                                }
                            });

                            modelFirebase.getMyParkingSpots(new SyncListener() {
                                @Override
                                public void isSuccessful(boolean success) {

                                }

                                @Override
                                public void failed(String message) {

                                }

                                @Override
                                public void passData(Object data) {
                                    if (data != null) {
                                        for (Parking parking : (List<Parking>) data) {
                                            modelSql.parkCar(parking);
                                        }
                                        modelSql.updateParkingDbTime(System.currentTimeMillis());
                                    }
                                }
                            });
/*                            if (data == null) {
                                modelFirebase.updateParkingDbTime(System.currentTimeMillis());
                            }*/
                            listener.passData(data);
                        }
                    });
                } else if (data.toString().compareTo(parkingLastUpdateDate) <= 0) {
                    modelFirebase.getCarDbTime(new SyncListener() {
                        @Override
                        public void isSuccessful(boolean success) {

                        }

                        @Override
                        public void failed(String message) {

                        }

                        @Override
                        public void passData(Object data) {
                            if(carLastUpdateDate == null || data.toString().compareTo(carLastUpdateDate) > 0){
                                modelSql.dropCarDb();
                                modelSql.dropParkingDb();
                                modelFirebase.getMyUnparkedCars(uId, new SyncListener() {
                                    @Override
                                    public void isSuccessful(boolean success) {

                                    }

                                    @Override
                                    public void failed(String message) {

                                    }

                                    @Override
                                    public void passData(Object data) {
                                        modelFirebase.getListOfAllCarsInDB(new SyncListener() {
                                            @Override
                                            public void isSuccessful(boolean success) {

                                            }

                                            @Override
                                            public void failed(String message) {

                                            }

                                            @Override
                                            public void passData(Object data) {
                                                if (data != null) {
                                                    for (Car car : (List<Car>) data) {
                                                        if (modelSql.getCarById(car.getCarId()) == null) {
                                                            modelSql.addCar(car);
                                                        }
                                                    }
                                                    modelSql.updateCarsDbTime(System.currentTimeMillis());
                                                }
                                            }
                                        });

                                        modelFirebase.getMyParkingSpots(new SyncListener() {
                                            @Override
                                            public void isSuccessful(boolean success) {

                                            }

                                            @Override
                                            public void failed(String message) {

                                            }

                                            @Override
                                            public void passData(Object data) {
                                                if (data != null) {
                                                    for (Parking parking : (List<Parking>) data) {
                                                        modelSql.parkCar(parking);
                                                    }
                                                    modelSql.updateParkingDbTime(System.currentTimeMillis());
                                                }
                                            }
                                        });
/*                                        if (data == null) {
                                            modelFirebase.updateCarDbTime(System.currentTimeMillis());
                                        }*/
                                        listener.passData(data);
                                    }
                                });
                            }
                            else{
                                modelSql.getMyUnparkedCars(listener);
                            }
                        }
                    });
                } else {
                    modelSql.getMyUnparkedCars(listener);
                }
            }
        });
    }

    public void getMyParkedCars(final SyncListener listener) {
        final String lastUpdateDate = modelSql.getParkingLastUpdate();
        final ArrayList<Car> parkedCars = new ArrayList<>();
        modelFirebase.getParkingDbTime(new SyncListener() {
            @Override
            public void isSuccessful(boolean success) {

            }

            @Override
            public void failed(String message) {

            }

            @Override
            public void passData(Object data) {
                if (data == null) {
                    listener.passData(parkedCars);
                } else if (lastUpdateDate == null || data.toString().compareTo(lastUpdateDate) > 0) {
                    modelSql.dropCarDb();
                    modelSql.dropParkingDb();
                    modelFirebase.getMyParkedCars(new SyncListener() {
                        @Override
                        public void isSuccessful(boolean success) {

                        }

                        @Override
                        public void failed(String message) {

                        }

                        @Override
                        public void passData(Object data) {
                            modelFirebase.getListOfAllCarsInDB(new SyncListener() {
                                @Override
                                public void isSuccessful(boolean success) {

                                }

                                @Override
                                public void failed(String message) {

                                }

                                @Override
                                public void passData(Object data) {
                                    if (data != null) {
                                        for (Car car : (List<Car>) data) {
                                            if (modelSql.getCarById(car.getCarId()) == null) {
                                                modelSql.addCar(car);
                                            }
                                        }
                                        modelSql.updateCarsDbTime(System.currentTimeMillis());
                                    }
                                }
                            });

                            modelFirebase.getMyParkingSpots(new SyncListener() {
                                @Override
                                public void isSuccessful(boolean success) {

                                }

                                @Override
                                public void failed(String message) {

                                }

                                @Override
                                public void passData(Object data) {
                                    if (data != null) {
                                        for (Parking parking : (List<Parking>) data) {
                                            modelSql.parkCar(parking);
                                        }
                                        modelSql.updateParkingDbTime(System.currentTimeMillis());
                                    }
                                }
                            });
                            listener.passData(data);
                        }
                    });
                } else {
                    modelSql.getMyParkedCars(listener);
                }
            }
        });
    }

    public void getMyParkingSpots(final SyncListener listener) {
        final String parkingLastUpdateDate = modelSql.getParkingLastUpdate();
        final String carLastUpdateDate = modelSql.getCarLastUpdate();
        final ArrayList<Parking> parkingSpots = new ArrayList<>();
        modelFirebase.getParkingDbTime(new SyncListener() {
            @Override
            public void isSuccessful(boolean success) {

            }

            @Override
            public void failed(String message) {

            }

            @Override
            public void passData(Object data) {
                if (data == null || parkingLastUpdateDate == null || data.toString().compareTo(parkingLastUpdateDate) > 0) {
                    modelSql.dropCarDb();
                    modelSql.dropParkingDb();
                    modelFirebase.getMyParkingSpots(new SyncListener() {
                        @Override
                        public void isSuccessful(boolean success) {

                        }

                        @Override
                        public void failed(String message) {

                        }

                        @Override
                        public void passData(Object data) {
                            modelFirebase.getListOfAllCarsInDB(new SyncListener() {
                                @Override
                                public void isSuccessful(boolean success) {

                                }

                                @Override
                                public void failed(String message) {

                                }

                                @Override
                                public void passData(Object data) {
                                    if (data != null) {
                                        for (Car car : (List<Car>) data) {
                                            if (modelSql.getCarById(car.getCarId()) == null) {
                                                modelSql.addCar(car);
                                            }
                                        }
                                        modelSql.updateCarsDbTime(System.currentTimeMillis());
                                    }
                                }
                            });

/*                            modelFirebase.getMyParkingSpots(new SyncListener() {
                                @Override
                                public void isSuccessful(boolean success) {

                                }

                                @Override
                                public void failed(String message) {

                                }

                                @Override
                                public void passData(Object data) {
                                    if (data != null) {
                                        for (Parking parking : (List<Parking>) data) {
                                            modelSql.parkCar(parking);
                                        }
                                        modelSql.updateParkingDbTime(System.currentTimeMillis());
                                    }
                                }
                            });*/
                            if (data != null) {
                                for (Parking parking : (List<Parking>) data) {
                                    modelSql.parkCar(parking);
                                }
                                modelSql.updateParkingDbTime(System.currentTimeMillis());
                            }
                            listener.passData(data);
                        }
                    });
                } else if (data.toString().compareTo(parkingLastUpdateDate) <= 0) {
                    modelFirebase.getCarDbTime(new SyncListener() {
                        @Override
                        public void isSuccessful(boolean success) {

                        }

                        @Override
                        public void failed(String message) {

                        }

                        @Override
                        public void passData(Object data) {
                            if(carLastUpdateDate == null || data.toString().compareTo(carLastUpdateDate) > 0){
                                modelSql.dropCarDb();
                                modelSql.dropParkingDb();
                                modelFirebase.getMyParkingSpots(new SyncListener() {
                                    @Override
                                    public void isSuccessful(boolean success) {

                                    }

                                    @Override
                                    public void failed(String message) {

                                    }

                                    @Override
                                    public void passData(Object data) {
                                        modelFirebase.getListOfAllCarsInDB(new SyncListener() {
                                            @Override
                                            public void isSuccessful(boolean success) {

                                            }

                                            @Override
                                            public void failed(String message) {

                                            }

                                            @Override
                                            public void passData(Object data) {
                                                if (data != null) {
                                                    for (Car car : (List<Car>) data) {
                                                        if (modelSql.getCarById(car.getCarId()) == null) {
                                                            modelSql.addCar(car);
                                                        }
                                                    }
                                                    modelSql.updateCarsDbTime(System.currentTimeMillis());
                                                }
                                            }
                                        });

/*                                        modelFirebase.getMyParkingSpots(new SyncListener() {
                                            @Override
                                            public void isSuccessful(boolean success) {

                                            }

                                            @Override
                                            public void failed(String message) {

                                            }

                                            @Override
                                            public void passData(Object data) {
                                                if (data != null) {
                                                    for (Parking parking : (List<Parking>) data) {
                                                        modelSql.parkCar(parking);
                                                    }
                                                    modelSql.updateParkingDbTime(System.currentTimeMillis());
                                                }
                                            }
                                        });*/

                                        if (data != null) {
                                            for (Parking parking : (List<Parking>) data) {
                                                modelSql.parkCar(parking);
                                            }
                                            modelSql.updateParkingDbTime(System.currentTimeMillis());
                                        }
/*                                        if (data == null) {
                                            modelFirebase.updateCarDbTime(System.currentTimeMillis());
                                        }*/
                                        listener.passData(data);
                                    }
                                });
                            }
                            else{
                                modelSql.getMyParkingSpots(listener);
                            }
                        }
                    });
                } else {
                    modelSql.getMyParkingSpots(listener);
                }
            }
        });
    }

    public void stopParking(Parking parking) {
        modelFirebase.stopParking(parking);
        modelSql.stopParking(parking);
        updateParkingDbTime();
    }

/*    public void stopParking(Car car) {
        modelFirebase.stopParking(car);
        modelSql.stopParking(car);
        updateParkingDbTime();
    }*/

    public void updateCarDbTime() {
        long currentTime = System.currentTimeMillis();
        modelFirebase.updateCarDbTime(currentTime);
        modelSql.updateCarsDbTime(currentTime);
    }

    public void updateParkingDbTime() {
        long currentTime = System.currentTimeMillis();
        modelFirebase.updateParkingDbTime(currentTime);
        modelSql.updateParkingDbTime(currentTime);
    }

    public void updateUsersDbTime() {
        long currentTime = System.currentTimeMillis();
        modelFirebase.updateUsersDbTime(currentTime);
        modelSql.updateUsersDbTime(currentTime);
    }

    public float getAPIVersion() {

        float f = 1f;
        try {
            StringBuilder strBuild = new StringBuilder();
            strBuild.append(android.os.Build.VERSION.RELEASE.substring(0, 2));
            f = Float.valueOf(strBuild.toString());
            Log.d("deviceVersion", "device OS version is: " + f);
        } catch (NumberFormatException e) {
            Log.e("deviceVersion", "error retrieving api version" + e.getMessage());
        }

        return f;
    }

    public List<Address> getLatandLong(String locationName) {
        List<Address> result = new ArrayList<>();
        try {
            result = new Geocoder(MyApplication.getAppContext()).getFromLocationName(locationName, 1);
            return result;
        } catch (IOException e) {
            Log.e("location", e.getMessage());
        }
        return null;

    }

    //--- Listeners ---- //
    public interface SyncListener {
        void isSuccessful(boolean success);

        void failed(String message);

        void passData(Object data);
    }


    public void loadImage(final String imageName, final LoadImageListener listener) {
        AsyncTask<String, String, Bitmap> task = new AsyncTask<String, String, Bitmap>() {
            @Override
            protected Bitmap doInBackground(String... params) {
                //Bitmap bmp = modelCloudinary.loadImage(imageName);
                //first try to fin the image on the device
                Bitmap bmp = fileManager.loadImageFromFile(imageName);

                if (bmp == null) {
                    bmp = modelCloudinary.loadImage(imageName);
                    //save the image locally for next time
                    if (bmp != null)
                        fileManager.saveImageToFile(bmp, imageName);
                }
                return bmp;
            }

            @Override
            protected void onPostExecute(Bitmap result) {
                listener.onResult(result);
            }
        };
        task.execute();
    }


    public interface LoadImageListener {
        public void onResult(Bitmap imageBmp);
    }


    public void saveImage(final Bitmap imageBitmap, final String imageName) {
        saveImageToFile(imageBitmap, imageName); // synchronously save image locally
        Thread d = new Thread(new Runnable() {  // asynchronously save image to parse
            @Override
            public void run() {
                modelCloudinary.uploadImage(imageName, imageBitmap);
            }
        });
        d.start();
    }

    private void saveImageToFile(Bitmap imageBitmap, String imageFileName) {
        FileOutputStream fos;
        OutputStream out = null;
        try {
            File dir = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES);
            if (!dir.exists()) {
                dir.mkdir();
            }
            File imageFile = new File(dir, imageFileName);
            imageFile.createNewFile();

            out = new FileOutputStream(imageFile);
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.close();

            //add the picture to the gallery so we dont need to manage the cache size
            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri contentUri = Uri.fromFile(imageFile);
            mediaScanIntent.setData(contentUri);
            MyApplication.getAppContext().sendBroadcast(mediaScanIntent);
            Log.d("tag", "add image to cache: " + imageFileName);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
