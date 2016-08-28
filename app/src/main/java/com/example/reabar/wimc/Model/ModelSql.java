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

    public ModelSql() {
        dbHelper = new MyDBHelper(MyApplication.getAppContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        CarSql.drop(db);
        ParkingSql.drop(db);
        UserSql.drop(db);
        LastUpdateSql.drop(db);
        CarSql.create(db);
        ParkingSql.create(db);
        UserSql.create(db);
        LastUpdateSql.create(db);
    }

    public void dropCarDb(){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        CarSql.drop(db);
        LastUpdateSql.setLastUpdate(db,Constants.CAR_TABLE,0);
        CarSql.create(db);
    }

    public void dropParkingDb(){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ParkingSql.drop(db);
        LastUpdateSql.setLastUpdate(db,Constants.PARKING_TABLE,0);
        ParkingSql.create(db);
    }

    public void addUser(User user) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        UserSql.addUser(db, user);
    }

    public void getUsersList(Model.SyncListener listener) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        UserSql.getUsersList(db,listener);
    }

    public List<User> getUsersList() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        return UserSql.getUsersList(db);
    }

    public String getUsersLastUpdateTime(){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        return LastUpdateSql.getLastUpdate(db,Constants.USER_TABLE);
    }

    public void isUserExistsByEmail(String email,Model.SyncListener listener) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        UserSql.isUserExistsByEmail(db, email,listener);
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

    public void getAllCars(Model.SyncListener listener) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        CarSql.getAllCars(db,listener);
    }

    public String getCarLastUpdate() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        return LastUpdateSql.getLastUpdate(db,Constants.CAR_TABLE);
    }

    public void getOwnedCars(String uId, Model.SyncListener listener){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        CarSql.getOwnedCars(db,uId,listener);
    }

    public void getListOfSharedCars(String uId, Model.SyncListener listener){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        CarSql.getListOfSharedCars(db,uId,listener);
    }

    public void updateCar(Car car) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        CarSql.updateCar(db, car);
    }

    public void parkCar(Parking parkingLocation) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ParkingSql.parkCar(db, parkingLocation);
    }

    public void getMyUnparkedCars(Model.SyncListener listener) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        ParkingSql.getMyUnparkedCars(db,listener);
    }

    public void getMyParkedCars(Model.SyncListener listener){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        ParkingSql.getMyParkedCars(db,listener);
    }

    public void getMyParkingSpots(Model.SyncListener listener){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        ParkingSql.getMyParkingSpots(db,listener);
    }

    public void stopParking(Parking parking){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ParkingSql.stopParking(db,parking);
    }

    public void stopParking(Car car){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ParkingSql.stopParking(db,car);
    }

    public String getParkingLastUpdate(){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        return LastUpdateSql.getLastUpdate(db,Constants.PARKING_TABLE);
    }

    public void updateUsersDbTime(long currentTime){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        LastUpdateSql.setLastUpdate(db,Constants.USER_TABLE,currentTime);
    }

    public void updateParkingDbTime(long currentTime){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        LastUpdateSql.setLastUpdate(db,Constants.PARKING_TABLE,currentTime);
    }

    public void updateCarsDbTime(long currentTime){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        LastUpdateSql.setLastUpdate(db,Constants.CAR_TABLE,currentTime);
    }

    class MyDBHelper extends SQLiteOpenHelper {
        public MyDBHelper(Context context) {
            super(context,"database.db", null, VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            //create the DB schema
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
