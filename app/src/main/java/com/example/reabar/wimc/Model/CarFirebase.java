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

/**
 * Created by reabar on 28/06/2016.
 */
public class CarFirebase {

    private String TAG = "CarsFirebase";
    //private String CARS_DB = "cars";

    public void addCarToDB(FirebaseDatabase db, Car car, final Model.SyncListener listener) {
        DatabaseReference dbRef = db.getReference(Constants.CAR_TABLE);
        dbRef.child(car.getCarId()).setValue(car).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "Car Added!");
                    listener.isSuccessful(true);
                } else {
                    Log.d(TAG, "Error to addUser the new car");
                    listener.failed(task.getException().getMessage());
                }
            }
        });
    }

    public void removeCarFromDB(FirebaseDatabase db, Car car) {
        DatabaseReference dbRef = db.getReference(Constants.CAR_TABLE);
        dbRef.child(car.getCarId()).removeValue();
    }

    public void updateCar(FirebaseDatabase db, Car car, final Model.SyncListener listener) {
        DatabaseReference dbRef = db.getReference(Constants.CAR_TABLE);
        dbRef.child(car.getCarId()).setValue(car).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    listener.isSuccessful(true);
                } else {
                    Log.d(TAG, "Error to update car");
                    listener.failed(task.getException().getMessage());
                }
            }
        });
    }

    public void getOwnedCars(FirebaseDatabase db, final String uId, final Model.SyncListener listener) {
        DatabaseReference dbRef = db.getReference(Constants.CAR_TABLE);
        final ArrayList<Car> ownedCars = new ArrayList<>();
        dbRef.orderByChild("userOwnerId").equalTo(uId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                while (children.iterator().hasNext()) {
                    Car tempCar = children.iterator().next().getValue(Car.class);
                    ownedCars.add(tempCar);
                }
                listener.isSuccessful(true);
                listener.passData(ownedCars);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.isSuccessful(false);
                listener.failed(databaseError.getMessage());
            }
        });
    }

    public void getListOfSharedCars(FirebaseDatabase db, final String uId, final Model.SyncListener listener) {
        DatabaseReference dbRef = db.getReference(Constants.CAR_TABLE);
        final ArrayList<Car> sharedCars = new ArrayList<>();
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                while (children.iterator().hasNext()) {
                    Car tempCar = children.iterator().next().getValue(Car.class);
                    if (tempCar.getUsersList().contains(uId)) {
                        sharedCars.add(tempCar);
                    }
                }
                listener.isSuccessful(true);
                listener.passData(sharedCars);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.isSuccessful(false);
                listener.failed(databaseError.getMessage());
            }
        });
    }

    public static void getListOfAllCarsInDB(FirebaseDatabase db, final Model.SyncListener listener) {
        DatabaseReference dbRef = db.getReference(Constants.CAR_TABLE);
        final ArrayList<Car> carsList = new ArrayList<>();
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                while (children.iterator().hasNext()) {
                    Car tempCar = children.iterator().next().getValue(Car.class);
                    carsList.add(tempCar);
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
}
