package com.example.reabar.wimc.Model;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by reabar on 30.7.2016.
 */
public class ParkingFirebase {

    private String TAG = "ParkingFirebase";
    private String PARKING_DB = "parking";

    public void parkCar(final FirebaseDatabase db, final Parking parkingLocation, final Model.SyncListener listener) {
        DatabaseReference dbRef = db.getReference(PARKING_DB);
        dbRef.child(parkingLocation.getCarId()).setValue(parkingLocation).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "Parking was saved!");
                    listener.isSuccessful(true);
                } else {
                    Log.d(TAG, "Error to add the new car");
                    listener.failed(task.getException().getMessage());
                }
            }
        });

    }
}
