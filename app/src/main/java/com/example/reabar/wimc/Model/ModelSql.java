package com.example.reabar.wimc.Model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.reabar.wimc.MyApplication;

import java.util.List;

/**
 * Created by reabar on 25.5.2016.
 */
public class ModelSql {
    private static final int VERSION = 2;

    MyDBHelper dbHelper;
    private final String USERS_DB = "users";
    private final String CAR_DB = "car";
    private final String PARKING_DB = "parking";
    private final String LAST_UPDATE_DB = "last_update";

    public ModelSql() {
        dbHelper = new MyDBHelper(MyApplication.getAppContext());
    }

    public void addUser(User user) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        UserSql.addUser(db, user);
    }

    public List<User> getUsersList() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        return UserSql.getUsersList(db);
    }

    public boolean isUserExistsByEmail(String email) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        return UserSql.isUserExistsByEmail(db, email);
    }

    public void addCar(Car car) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        CarSql.addCarToDB(db, car);
    }

    public void removeCar(Car car) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        CarSql.removeCarFromDb(db, car);
    }

    public Car getCarById(String id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        return CarSql.getCarById(db, id);
    }

    public List<Car> getAllCars() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        return CarSql.getAllCars(db);
    }

    public String getCarLastUpdate() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        return CarSql.getLastUpdateDate(db);
    }

    public void updateCar(Car car) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        CarSql.updateCar(db, car);
    }

    public void parkCar(Parking parkingLocation) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ParkingSql.parkCar(db, parkingLocation);
    }

    public List<Car> getMyUnparkedCars() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        return ParkingSql.getMyUnparkedCars(db);
    }

    public List<Car> getMyParkedCars(){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        return ParkingSql.getMyParkedCars(db);
    }

    public List<Parking> getMyParkingSpots(){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        return ParkingSql.getMyParkingSpots(db);
    }

    public void stopParking(Parking parking){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ParkingSql.stopParking(db,parking);
    }

    public void stopParking(Car car){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ParkingSql.stopParking(db,car);
    }


    class MyDBHelper extends SQLiteOpenHelper {
        public MyDBHelper(Context context) {
            super(context, "database.db", null, VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            //create the DB schema
            //dbHelper.getWritableDatabase();
            CarSql.create(db);
            UserSql.create(db);
            ParkingSql.create(db);
            LastUpdateSql.create(db);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            CarSql.drop(db);
            UserSql.drop(db);
            onCreate(db);
        }
    }
}
