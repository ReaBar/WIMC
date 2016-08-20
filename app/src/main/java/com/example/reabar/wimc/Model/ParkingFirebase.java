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

import java.lang.reflect.Array;
import java.util.ArrayList;

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
                     Model.getInstance().getAllCars(new Model.SyncListener() {
                         @Override
                         public void isSuccessful(boolean success) {

                         }

                         @Override
                         public void failed(String message) {

                         }

                         @Override
                         public void PassData(Object data) {
                            if(data instanceof ArrayList){
                                for (Car car: (ArrayList<Car>)data) {
                                    if(car.getCarId().equals(parkingLocation.getCarId())){
                                        car.setParkingIsActive(true);
                                        car.updateThisCar();
                                    }
                                }
                            }
                         }
                     });
                } else {
                    Log.d(TAG, "Error to add the new car");
                    listener.failed(task.getException().getMessage());
                }
            }
        });
    }

    public void getMyUnparkedCars(final FirebaseDatabase db, final String uId, final Model.SyncListener listener) {
        final DatabaseReference carDbRef = db.getReference("cars");
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
                        DatabaseReference parkingDbRef = db.getReference(PARKING_DB);

                        parkingDbRef.orderByChild("carId").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                                while (children.iterator().hasNext()) {
                                    Parking tempCar = children.iterator().next().getValue(Parking.class);
                                    for (int i = 0; i < carsList.size(); i++) {
                                        if (carsList.get(i).getCarId().equals(tempCar.getCarId())) {
                                            Log.d("TESTTEST", "Removed " + carsList.get(i).getCarId());
                                            carsList.remove(i);
                                        }
                                    }
                                }
                                listener.isSuccessful(true);
                                listener.PassData(carsList);
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

    public void getAllMyParkedCars(final Model.SyncListener listener) {
        final ArrayList<Car> carsList = new ArrayList<>();
        Model.getInstance().getAllCars(new Model.SyncListener() {
            @Override
            public void isSuccessful(boolean success) {

            }

            @Override
            public void failed(String message) {

            }

            @Override
            public void PassData(Object data) {
                if(data instanceof ArrayList){
                    for (Car car: (ArrayList<Car>)data) {
                        if((car.getUserOwnerId().equals(Model.getInstance().getCurrentUser().getEmail()) || car.getUsersList().contains(Model.getInstance().getCurrentUser().getEmail())) && car.getParkingIsActive()){
                            carsList.add(car);
                        }
                    }
                    listener.PassData(carsList);
                    listener.isSuccessful(true);
                }
            }
        });
    }

    public void getAllMyParkingSpots(final FirebaseDatabase db,final Model.SyncListener listener) {
        final ArrayList<Parking> parkingSpots = new ArrayList<>();
        final ArrayList<Parking> finalParkingList = new ArrayList<>();
        DatabaseReference dbRef = db.getReference(PARKING_DB);
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                while (children.iterator().hasNext()) {
                    parkingSpots.add(children.iterator().next().getValue(Parking.class));
                }
                Model.getInstance().getAllMyParkedCars(new Model.SyncListener() {
                    @Override
                    public void isSuccessful(boolean success) {

                    }

                    @Override
                    public void failed(String message) {

                    }

                    @Override
                    public void PassData(Object data) {
                        if(data instanceof ArrayList){
                            for (Car car: (ArrayList<Car>)data) {
                                for (Parking parking: parkingSpots) {
                                    if(parking.getCarId().equals(car.getCarId())){
                                        finalParkingList.add(parking);
                                    }
                                }
                            }
                            listener.PassData(finalParkingList);
                            listener.isSuccessful(true);
                        }
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
        DatabaseReference dbRef = db.getReference(PARKING_DB);
        Model.getInstance().getAllMyParkedCars(new Model.SyncListener() {
            @Override
            public void isSuccessful(boolean success) {

            }

            @Override
            public void failed(String message) {

            }

            @Override
            public void PassData(Object data) {
                if(data instanceof ArrayList){
                    for (Car car: (ArrayList<Car>)data) {
                        if(car.getCarId().equals(parking.getCarId())){
                            car.setParkingIsActive(false);
                            car.updateThisCar();
                            return;
                        }
                    }
                }
            }
        });
        dbRef.child(parking.getCarId()).removeValue();
    }

    public void stopParking(FirebaseDatabase db, final Car car) {
        DatabaseReference dbRef = db.getReference(PARKING_DB);
        Model.getInstance().getAllMyParkedCars(new Model.SyncListener() {
            @Override
            public void isSuccessful(boolean success) {

            }

            @Override
            public void failed(String message) {

            }

            @Override
            public void PassData(Object data) {
                if(data instanceof ArrayList){
                    for (Car car: (ArrayList<Car>)data) {
                        if(car.getCarId().equals(car.getCarId())){
                            car.setParkingIsActive(false);
                            car.updateThisCar();
                            return;
                        }
                    }
                }
            }
        });
        dbRef.child(car.getCarId()).removeValue();
    }
}
