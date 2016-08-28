package com.example.reabar.wimc.Model;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by reabar on 30.7.2016.
 */
public class ParkingFirebase {

    private String TAG = "ParkingFirebase";

    public void parkCar(final FirebaseDatabase db, final Parking parkingLocation, final Model.SyncListener listener) {
        DatabaseReference dbRef = db.getReference(Constants.PARKING_TABLE);
        dbRef.child(parkingLocation.getCarId()).setValue(parkingLocation).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "Parking was saved!");
                    listener.isSuccessful(true);
                    CarFirebase.getListOfAllCarsInDB(db, new Model.SyncListener() {
                        @Override
                        public void isSuccessful(boolean success) {

                        }

                        @Override
                        public void failed(String message) {

                        }

                        @Override
                        public void passData(Object data) {
                            if (data instanceof ArrayList) {
                                for (Car car : (ArrayList<Car>) data) {
                                    if (car.getCarId().equals(parkingLocation.getCarId())) {
                                        car.setParkingIsActive(true);
                                        //car.updateThisCar();
                                        Model.getInstance().updateParkingDbTime();
                                    }
                                }
                            }
                        }
                    });
                } else {
                    Log.d(TAG, "Error to addUser the new car");
                    listener.failed(task.getException().getMessage());
                }
            }
        });
    }

    public void getMyUnparkedCars(final FirebaseDatabase db, final String uId, final Model.SyncListener listener) {
        final DatabaseReference carDbRef = db.getReference(Constants.CAR_TABLE);
        final ArrayList<Car> carsList = new ArrayList<>();

        carDbRef.orderByChild("userOwnerId").equalTo(uId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                while (children.iterator().hasNext()) {
                    Car tempCar = children.iterator().next().getValue(Car.class);
                    carsList.add(tempCar);
                }

                carDbRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                        while (children.iterator().hasNext()) {
                            Car tempCar = children.iterator().next().getValue(Car.class);
                            if (tempCar.getUsersList().contains(uId)) {
                                carsList.add(tempCar);
                            }
                        }
                        DatabaseReference parkingDbRef = db.getReference(Constants.PARKING_TABLE);

                        parkingDbRef.orderByChild(Constants.CAR_ID).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                                while (children.iterator().hasNext()) {
                                    Parking tempCar = children.iterator().next().getValue(Parking.class);
                                    for (int i = 0; i < carsList.size(); i++) {
                                        if (carsList.get(i).getCarId().equals(tempCar.getCarId())) {
                                            carsList.remove(i);
                                        }
                                    }
                                }
                                listener.isSuccessful(true);
                                listener.passData(carsList);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                listener.isSuccessful(false);
                                listener.failed(databaseError.getMessage());
                            }
                        });
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public void getMyParkedCars(FirebaseDatabase db, final Model.SyncListener listener) {
        final ArrayList<Car> carsList = new ArrayList<>();
        CarFirebase.getListOfAllCarsInDB(db, new Model.SyncListener() {
            @Override
            public void isSuccessful(boolean success) {

            }

            @Override
            public void failed(String message) {

            }

            @Override
            public void passData(Object data) {
                if (data instanceof ArrayList) {
                    for (Car car : (ArrayList<Car>) data) {
                        if ((car.getUserOwnerId().equals(Model.getInstance().getCurrentUser().getEmail()) || car.getUsersList().contains(Model.getInstance().getCurrentUser().getEmail()))) {
                            carsList.add(car);
                        }
                    }
                    listener.passData(carsList);
                    listener.isSuccessful(true);
                }
            }
        });
    }

    public void getAllMyParkingSpots(final FirebaseDatabase db, final Model.SyncListener listener) {
        final ArrayList<Parking> parkingSpots = new ArrayList<>();
        final ArrayList<Parking> finalParkingList = new ArrayList<>();
        DatabaseReference dbRef = db.getReference(Constants.PARKING_TABLE);
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                while (children.iterator().hasNext()) {
                    parkingSpots.add(children.iterator().next().getValue(Parking.class));
                }
                getMyParkedCars(db, new Model.SyncListener() {
                    @Override
                    public void isSuccessful(boolean success) {

                    }

                    @Override
                    public void failed(String message) {

                    }

                    @Override
                    public void passData(Object data) {
                        for (Car car : (List<Car>) data) {
                            for (Parking parking : parkingSpots) {
                                if (car.getCarId().equals(parking.getCarId())) {
                                    finalParkingList.add(parking);
                                }
                            }
                        }
                        listener.passData(finalParkingList);
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.isSuccessful(false);
                listener.failed(databaseError.getMessage());
            }
        });
    }

    public void stopParking(FirebaseDatabase db, final Parking parking) {
        DatabaseReference dbRef = db.getReference(Constants.PARKING_TABLE);
        Model.getInstance().getMyParkedCars(new Model.SyncListener() {
            @Override
            public void isSuccessful(boolean success) {

            }

            @Override
            public void failed(String message) {

            }

            @Override
            public void passData(Object data) {
                if (data instanceof ArrayList) {
                    for (Car car : (ArrayList<Car>) data) {
                        if (car.getCarId().equals(parking.getCarId())) {
                            car.setParkingIsActive(false);
                            //car.updateThisCar();
                            return;
                        }
                    }
                }
            }
        });
        dbRef.child(parking.getCarId()).removeValue();
    }

    public void stopParking(FirebaseDatabase db, final Car car) {
        DatabaseReference dbRef = db.getReference(Constants.PARKING_TABLE);
        Model.getInstance().getMyParkedCars(new Model.SyncListener() {
            @Override
            public void isSuccessful(boolean success) {

            }

            @Override
            public void failed(String message) {

            }

            @Override
            public void passData(Object data) {
                if (data instanceof ArrayList) {
                    for (Car car : (ArrayList<Car>) data) {
                        if (car.getCarId().equals(car.getCarId())) {
                            car.setParkingIsActive(false);
                            //car.updateThisCar();
                            return;
                        }
                    }
                }
            }
        });
        dbRef.child(car.getCarId()).removeValue();
    }
}
