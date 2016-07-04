package com.example.reabar.wimc.Model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

/**
 * Created by reabar on 28/06/2016.
 */
public class CarFirebase {

    private String TAG = "CarsFirebase";
    private String CARS_DB = "cars";


    public void addCarToDB(FirebaseDatabase db, Car car){
        DatabaseReference dbRef = db.getReference(CARS_DB);
        dbRef.child(car.getCarId()).setValue(car);
    }

    public void removeCarFromDB(FirebaseDatabase db, Car car){
        DatabaseReference dbRef = db.getReference(CARS_DB);
        dbRef.child(car.getCarId()).removeValue();
    }

    public void updateCar(FirebaseDatabase db, Car car){
        DatabaseReference dbRef = db.getReference(CARS_DB);
        dbRef.child(car.getCarId()).setValue(car);
    }
}
