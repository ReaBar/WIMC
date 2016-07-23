package com.example.reabar.wimc.Model;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by reabar on 28/06/2016.
 */
public class CarFirebase {

    private String TAG = "CarsFirebase";
    private String CARS_DB = "cars";


    public void addCarToDB(FirebaseDatabase db, Car car, final Model.AddNewCarListener listener){
        DatabaseReference dbRef = db.getReference(CARS_DB);
        dbRef.child(car.getCarId()).setValue(car).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Log.d(TAG, "Car Added!");
                    listener.success(true);
                }

                else{
                    Log.d(TAG,"Error to add the new car");
                    listener.failed(task.getException().getMessage());
                }
            }
        });
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
