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

/**
 * Created by reabar on 20.8.2016.
 */
public class LastUpdateFirebase {
    private String TAG = "LastUpdateFirebase";
    //private String LAST_UPDATE_DB = "lastUpdate";

    public void updateParkingDbTime(FirebaseDatabase db,final long currentTime) {
        DatabaseReference dbRef = db.getReference(Constants.LAST_UPDATE_TABLE);
        dbRef.child(Constants.PARKING_TABLE).setValue(currentTime).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "Parking last update time is: " + currentTime);
                } else {
                    Log.d(TAG, "Error updating last update time");
                }
            }
        });
    }

    public void getParkingDbTime(FirebaseDatabase db, final Model.SyncListener listener) {
        DatabaseReference dbRef = db.getReference(Constants.LAST_UPDATE_TABLE);
        dbRef.child(Constants.PARKING_TABLE).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Object time = dataSnapshot.getValue();
                Log.d(TAG, "Last update parking time is: " + time.toString());
                listener.passData(time);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.isSuccessful(false);
                listener.failed(databaseError.getMessage());
            }
        });
    }

    public void updateCarDbTime(FirebaseDatabase db,final long currentTime) {
        DatabaseReference dbRef = db.getReference(Constants.LAST_UPDATE_TABLE);
        dbRef.child(Constants.CAR_TABLE).setValue(currentTime).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "Car last update time is: " + currentTime);
                } else {
                    Log.d(TAG, "Error updating last update time");
                }
            }
        });
    }

    public void getCarDbTime(FirebaseDatabase db, final Model.SyncListener listener) {
        DatabaseReference dbRef = db.getReference(Constants.LAST_UPDATE_TABLE);
        dbRef.child(Constants.CAR_TABLE).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Object time = dataSnapshot.getValue();
                if(time != null){
                    Log.d(TAG, "Last update car time is: " + time.toString());
                }
                listener.passData(time);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.isSuccessful(false);
                listener.failed(databaseError.getMessage());
            }
        });
    }

    public void updateUsersDbTime(FirebaseDatabase db,final long currentTime) {
        DatabaseReference dbRef = db.getReference(Constants.LAST_UPDATE_TABLE);
        dbRef.child(Constants.USER_TABLE).setValue(currentTime).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "Users last update time is: " + currentTime);
                } else {
                    Log.d(TAG, "Error updating last update time");
                }
            }
        });
    }

    public void getUsersDbTime(FirebaseDatabase db, final Model.SyncListener listener) {
        DatabaseReference dbRef = db.getReference(Constants.LAST_UPDATE_TABLE);
        dbRef.child(Constants.USER_TABLE).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Object time = dataSnapshot.getValue();
                Log.d(TAG, "Last update users time is: " + time.toString());
                listener.passData(time);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.isSuccessful(false);
                listener.failed(databaseError.getMessage());
            }
        });
    }
}
