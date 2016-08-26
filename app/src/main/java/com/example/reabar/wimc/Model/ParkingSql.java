package com.example.reabar.wimc.Model;

/**
 * Created by admin on 8/17/16.
 */

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;


public class ParkingSql {

    static public void create(SQLiteDatabase db) {
        //TODO startParking is not saved we need to solve this because there is no such field as Date
        db.execSQL("CREATE TABLE IF NOT EXISTS " +
                Constants.PARKING_TABLE + " (" +
                Constants.PARKING_CAR_ID + " TEXT PRIMARY KEY," +
                Constants.PARKING_STREET + " TEXT," +
                Constants.PARKING_CITY + " TEXT," +
                Constants.PARKING_PARKING_LOT_NAME + " TEXT," +
                Constants.PARKING_LOT_ROW_COLOR + " TEXT," +
                Constants.PARKING_STREET_NUMBER + " TEXT," +
                Constants.PARKING_LOT_FLOOR + " TEXT," +
                Constants.PARKING_LATITUDE + " REAL," +
                Constants.PARKING_LONGITUDE + " REAL," +
                Constants.PARKING_IMAGE_NAME + " TEXT," +
                Constants.PARKING_IS_ACTIVE + " BOOLEAN);");
    }

    public static void parkCar(SQLiteDatabase db, Parking parkingLocation) {
        ContentValues values = new ContentValues();
        values.put(Constants.PARKING_CAR_ID, parkingLocation.getCarId());
        values.put(Constants.PARKING_STREET, parkingLocation.getStreet());
        values.put(Constants.PARKING_CITY, parkingLocation.getCity());
        values.put(Constants.PARKING_PARKING_LOT_NAME, parkingLocation.getParkingLotName());
        values.put(Constants.PARKING_LOT_ROW_COLOR, parkingLocation.getParkingLotRowColor());
        values.put(Constants.PARKING_STREET_NUMBER, parkingLocation.getStreetNumber());
        values.put(Constants.PARKING_LOT_FLOOR, parkingLocation.getParkingLotFloor());
        values.put(Constants.PARKING_LATITUDE, parkingLocation.getLatitude());
        values.put(Constants.PARKING_LONGITUDE, parkingLocation.getLongitude());
        values.put(Constants.PARKING_IS_ACTIVE, parkingLocation.isParkingActive());
        values.put(Constants.PARKING_IMAGE_NAME, parkingLocation.getImageName());
        db.insertWithOnConflict(Constants.PARKING_TABLE, Constants.PARKING_CAR_ID, values, SQLiteDatabase.CONFLICT_REPLACE);
    }

    public static void getMyUnparkedCars(SQLiteDatabase db,Model.SyncListener listener) {
        String currentUser = "%" + Model.getInstance().getCurrentUser().getEmail() + "%";
        String userEmail = Model.getInstance().getCurrentUser().getEmail();
        Cursor cursor = db.rawQuery("SELECT * FROM " + Constants.CAR_TABLE + " WHERE " + Constants.CAR_ID + " NOT IN (SELECT " + Constants.PARKING_CAR_ID + " FROM " + Constants.PARKING_TABLE + ") AND (" + Constants.CAR_USER_OWNER_ID + " = ? OR " + Constants.CAR_USERS_LIST + " LIKE ?)" , new String[]{userEmail,currentUser});
        List<Car> cars = new ArrayList<>();

        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(Constants.CAR_ID);
            int colorIndex = cursor.getColumnIndex(Constants.CAR_COLOR);
            int modelIndex = cursor.getColumnIndex(Constants.CAR_MODEL);
            int companyIndex = cursor.getColumnIndex(Constants.CAR_COMPANY);
            int userOwnerIdIndex = cursor.getColumnIndex(Constants.CAR_USER_OWNER_ID);
            int userListIndex = cursor.getColumnIndex(Constants.CAR_USERS_LIST);

            do {
                String id = cursor.getString(idIndex);
                String color = cursor.getString(colorIndex);
                String model = cursor.getString(modelIndex);
                String company = cursor.getString(companyIndex);
                String userOwnerId = cursor.getString(userOwnerIdIndex);
                List<String> usersList = CarSql.convertStringToList(cursor.getString(userListIndex));

                Car car = new Car(id, color, model, company, userOwnerId);
                if(usersList != null){
                    for (String user : usersList) {
                        car.setNewCarUser(user,false);
                    }
                }
                cars.add(car);
            } while (cursor.moveToNext());
        }
        listener.passData(cars);
    }

    public static void getMyParkedCars(SQLiteDatabase db,Model.SyncListener listener) {
        String currentUser = "%" + Model.getInstance().getCurrentUser().getEmail() + "%";
        String userEmail = Model.getInstance().getCurrentUser().getEmail();
        Cursor cursor = db.rawQuery("SELECT * FROM " + Constants.CAR_TABLE + " WHERE " + Constants.CAR_ID + " IN (SELECT " + Constants.PARKING_CAR_ID + " FROM " + Constants.PARKING_TABLE + ") AND (" + Constants.CAR_USER_OWNER_ID + " = ? OR " + Constants.CAR_USERS_LIST + " LIKE ?)" , new String[]{userEmail,currentUser});
        List<Car> cars = new ArrayList<>();

        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(Constants.CAR_ID);
            int colorIndex = cursor.getColumnIndex(Constants.CAR_COLOR);
            int modelIndex = cursor.getColumnIndex(Constants.CAR_MODEL);
            int companyIndex = cursor.getColumnIndex(Constants.CAR_COMPANY);
            int userOwnerIdIndex = cursor.getColumnIndex(Constants.CAR_USER_OWNER_ID);
            int userListIndex = cursor.getColumnIndex(Constants.CAR_USERS_LIST);

            do {
                String id = cursor.getString(idIndex);
                String color = cursor.getString(colorIndex);
                String model = cursor.getString(modelIndex);
                String company = cursor.getString(companyIndex);
                String userOwnerId = cursor.getString(userOwnerIdIndex);
                List<String> usersList = CarSql.convertStringToList(cursor.getString(userListIndex));

                Car car = new Car(id, color, model, company, userOwnerId);
                if(usersList != null){
                    for (String user : usersList) {
                        car.setNewCarUser(user,false);
                    }
                }
                cars.add(car);
            } while (cursor.moveToNext());
        }
        listener.passData(cars);
    }

    public static void getMyParkingSpots(SQLiteDatabase db,Model.SyncListener listener) {
        String currentUser = "%" + Model.getInstance().getCurrentUser().getEmail() + "%";
        String userEmail = Model.getInstance().getCurrentUser().getEmail();
        Cursor cursor = db.rawQuery("SELECT * FROM " + Constants.PARKING_TABLE + " WHERE " + Constants.PARKING_CAR_ID + " IN (SELECT " + Constants.CAR_ID + " FROM " + Constants.CAR_TABLE + " WHERE " + Constants.CAR_USER_OWNER_ID + " = ? OR " + Constants.CAR_USERS_LIST + " LIKE ?)" , new String[]{userEmail,currentUser});
        List<Parking> parkingSpots = new ArrayList<>();

        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(Constants.PARKING_CAR_ID);
            int streetIndex = cursor.getColumnIndex(Constants.PARKING_STREET);
            int cityIndex = cursor.getColumnIndex(Constants.PARKING_CITY);
            int lotNameIndex = cursor.getColumnIndex(Constants.PARKING_PARKING_LOT_NAME);
            int lotRowColorIndex = cursor.getColumnIndex(Constants.PARKING_LOT_ROW_COLOR);
            int streetNumberIndex = cursor.getColumnIndex(Constants.PARKING_STREET_NUMBER);
            int lotFloorIndex = cursor.getColumnIndex(Constants.PARKING_LOT_FLOOR);
            int latitudeIndex = cursor.getColumnIndex(Constants.PARKING_LATITUDE);
            int longitudeIndex = cursor.getColumnIndex(Constants.PARKING_LONGITUDE);
            int isParkingActiveIndex = cursor.getColumnIndex(Constants.PARKING_IS_ACTIVE);
            int imageNameIndex = cursor.getColumnIndex(Constants.PARKING_IMAGE_NAME);

            do {
                String id = cursor.getString(idIndex);
                String street = cursor.getString(streetIndex);
                String city = cursor.getString(cityIndex);
                String lotName = cursor.getString(lotNameIndex);
                String lotRowColor = cursor.getString(lotRowColorIndex);
                String streetNumber = cursor.getString(streetNumberIndex);
                String lotFloor = cursor.getString(lotFloorIndex);
                long latitude = cursor.getLong(latitudeIndex);
                long longitude = cursor.getLong(longitudeIndex);
                boolean isParkingActive = cursor.getInt(isParkingActiveIndex) != 0;
                String imageName = cursor.getString(imageNameIndex);

                Parking parking = new Parking.ParkingBuilder(id).street(street).city(city).parkingLotName(lotName).parkingLotRowColor(lotRowColor).streetNumber(streetNumber).parkingLotFloor(lotFloor).parkingLatitude(latitude)
                        .parkingLonitude(longitude).isParkingActive(isParkingActive).imageName(imageName).build();
                parkingSpots.add(parking);
            } while (cursor.moveToNext());
        }
        listener.passData(parkingSpots);
    }

    public static void stopParking(SQLiteDatabase db,final Parking parking){
        CarSql.getAllCars(db, new Model.SyncListener() {
            @Override
            public void isSuccessful(boolean success) {

            }

            @Override
            public void failed(String message) {

            }

            @Override
            public void passData(Object data) {
                List<Car> cars = (List<Car>)data;
                for (Car car: cars) {
                    if(car.getCarId().equals(parking.getCarId())){
                        car.setParkingIsActive(false);
                        car.updateThisCar();
                    }
                }
            }
        });

        db.delete(Constants.PARKING_TABLE, Constants.PARKING_CAR_ID + " = " + parking.getCarId(),null);
    }

    public static void stopParking(SQLiteDatabase db, Car car){
        car.setParkingIsActive(false);
        car.updateThisCar();
        db.delete(Constants.PARKING_TABLE, Constants.PARKING_CAR_ID + " = " + car.getCarId(),null);
    }

    public static void drop(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + Constants.PARKING_TABLE + ";");
    }
}
