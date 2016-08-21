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

    final static String PARKING_TABLE = "parking";
    final static String PARKING_CAR_ID = "car_id";
    final static String PARKING_STREET = "street";
    final static String PARKING_CITY = "city";
    final static String PARKING_PARKING_LOT_NAME = "parkingLotName";
    final static String PARKING_LOT_ROW_COLOR = "parkingLotRowColor";
    final static String PARKING_STREET_NUMBER = "streetNumber";
    final static String PARKING_LOT_FLOOR = "parkingLotFloor";
    final static String PARKING_LATITUDE = "latitude";
    final static String PARKING_LONGITUDE = "longitude";
    final static String PARKING_IS_ACTIVE = "parkingIsActive";

    final static String CAR_TABLE = "car";
    final static String CAR_ID = "car_id";
    final static String CAR_COLOR = "color";
    final static String CAR_MODEL = "model";
    final static String CAR_COMPANY = "company";
    final static String CAR_USER_OWNER_ID = "user_owner_id";
    final static String CAR_USERS_LIST = "users_list";

    static public void create(SQLiteDatabase db) {
        //TODO startParking is not saved we need to solve this because there is no such field as Date
        db.execSQL("CREATE TABLE IF NOT EXISTS " +
                PARKING_TABLE + " (" +
                PARKING_CAR_ID + " TEXT PRIMARY KEY," +
                PARKING_STREET + " TEXT," +
                PARKING_CITY + " TEXT," +
                PARKING_PARKING_LOT_NAME + " TEXT," +
                PARKING_LOT_ROW_COLOR + " TEXT," +
                PARKING_STREET_NUMBER + " TEXT," +
                PARKING_LOT_FLOOR + " TEXT," +
                PARKING_LATITUDE + " REAL," +
                PARKING_LONGITUDE + " REAL," +
                PARKING_IS_ACTIVE + " BOOLEAN);");
    }

    public static void parkCar(SQLiteDatabase db, Parking parkingLocation) {
        ContentValues values = new ContentValues();
        values.put(PARKING_CAR_ID, parkingLocation.getCarId());
        values.put(PARKING_STREET, parkingLocation.getStreet());
        values.put(PARKING_CITY, parkingLocation.getCity());
        values.put(PARKING_PARKING_LOT_NAME, parkingLocation.getParkingLotName());
        values.put(PARKING_LOT_ROW_COLOR, parkingLocation.getParkingLotRowColor());
        values.put(PARKING_STREET_NUMBER, parkingLocation.getStreetNumber());
        values.put(PARKING_LOT_FLOOR, parkingLocation.getParkingLotFloor());
        values.put(PARKING_LATITUDE, parkingLocation.getLatitude());
        values.put(PARKING_LONGITUDE, parkingLocation.getLongitude());
        values.put(PARKING_IS_ACTIVE, parkingLocation.isParkingActive());

        db.insertWithOnConflict(PARKING_TABLE, PARKING_CAR_ID, values, SQLiteDatabase.CONFLICT_REPLACE);
        setLastUpdateDate(db);
    }

    public static List<Car> getMyUnparkedCars(SQLiteDatabase db) {
        String currentUser = "%" + Model.getInstance().getCurrentUser().getEmail() + "%";
        Cursor cursor = db.rawQuery("SELECT * FROM " + CAR_TABLE + " WHERE " + CAR_ID + " NOT IN (SELECT " + PARKING_CAR_ID + " FROM " + PARKING_TABLE + ") AND (" + CAR_USER_OWNER_ID + " = ? OR " + CAR_USERS_LIST + " LIKE ?)" , new String[]{currentUser,currentUser});
        List<Car> cars = new ArrayList<>();

        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(CAR_ID);
            int colorIndex = cursor.getColumnIndex(CAR_COLOR);
            int modelIndex = cursor.getColumnIndex(CAR_MODEL);
            int companyIndex = cursor.getColumnIndex(CAR_COMPANY);
            int userOwnerIdIndex = cursor.getColumnIndex(CAR_USER_OWNER_ID);
            int userListIndex = cursor.getColumnIndex(CAR_USERS_LIST);

            do {
                String id = cursor.getString(idIndex);
                String color = cursor.getString(colorIndex);
                String model = cursor.getString(modelIndex);
                String company = cursor.getString(companyIndex);
                String userOwnerId = cursor.getString(userOwnerIdIndex);
                List<String> usersList = CarSql.convertStringToList(cursor.getString(userListIndex));

                Car car = new Car(id, color, model, company, userOwnerId);
                for (String user : usersList) {
                    car.setNewCarUser(user);
                    cars.add(car);
                }
            } while (cursor.moveToNext());
        }
        return cars;
    }

    public static List<Car> getMyParkedCars(SQLiteDatabase db) {
        String currentUser = "%" + Model.getInstance().getCurrentUser().getEmail() + "%";
        Cursor cursor = db.rawQuery("SELECT * FROM " + CAR_TABLE + " WHERE " + CAR_ID + " IN (SELECT " + PARKING_CAR_ID + " FROM " + PARKING_TABLE + ") AND (" + CAR_USER_OWNER_ID + " = ? OR " + CAR_USERS_LIST + " LIKE ?)" , new String[]{currentUser,currentUser});
        List<Car> cars = new ArrayList<>();

        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(CAR_ID);
            int colorIndex = cursor.getColumnIndex(CAR_COLOR);
            int modelIndex = cursor.getColumnIndex(CAR_MODEL);
            int companyIndex = cursor.getColumnIndex(CAR_COMPANY);
            int userOwnerIdIndex = cursor.getColumnIndex(CAR_USER_OWNER_ID);
            int userListIndex = cursor.getColumnIndex(CAR_USERS_LIST);

            do {
                String id = cursor.getString(idIndex);
                String color = cursor.getString(colorIndex);
                String model = cursor.getString(modelIndex);
                String company = cursor.getString(companyIndex);
                String userOwnerId = cursor.getString(userOwnerIdIndex);
                List<String> usersList = CarSql.convertStringToList(cursor.getString(userListIndex));

                Car car = new Car(id, color, model, company, userOwnerId);
                for (String user : usersList) {
                    car.setNewCarUser(user);
                    cars.add(car);
                }
            } while (cursor.moveToNext());
        }
        return cars;
    }

    public static List<Parking> getMyParkingSpots(SQLiteDatabase db) {
        String currentUser = "%" + Model.getInstance().getCurrentUser().getEmail() + "%";
        Cursor cursor = db.rawQuery("SELECT * FROM " + PARKING_TABLE + " WHERE " + PARKING_CAR_ID + " IN (SELECT " + CAR_TABLE + " FROM " + CAR_ID + ") AND (" + CAR_USER_OWNER_ID + " = ? OR " + CAR_USERS_LIST + " LIKE ?)" , new String[]{currentUser,currentUser});
        List<Parking> parkingSpots = new ArrayList<>();

        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(PARKING_CAR_ID);
            int streetIndex = cursor.getColumnIndex(PARKING_STREET);
            int cityIndex = cursor.getColumnIndex(PARKING_CITY);
            int lotNameIndex = cursor.getColumnIndex(PARKING_PARKING_LOT_NAME);
            int lotRowColorIndex = cursor.getColumnIndex(PARKING_LOT_ROW_COLOR);
            int streetNumberIndex = cursor.getColumnIndex(PARKING_STREET_NUMBER);
            int lotFloorIndex = cursor.getColumnIndex(PARKING_LOT_FLOOR);
            int latitudeIndex = cursor.getColumnIndex(PARKING_LATITUDE);
            int longitudeIndex = cursor.getColumnIndex(PARKING_LONGITUDE);
            int isParkingActiveIndex = cursor.getColumnIndex(PARKING_IS_ACTIVE);

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

                Parking parking = new Parking.ParkingBuilder(id).street(street).city(city).parkingLotName(lotName).parkingLotRowColor(lotRowColor).streetNumber(streetNumber).parkingLotFloor(lotFloor).parkingLatitude(latitude)
                        .parkingLonitude(longitude).isParkingActive(isParkingActive).build();
                parkingSpots.add(parking);
            } while (cursor.moveToNext());
        }
        return parkingSpots;
    }

    public static void stopParking(SQLiteDatabase db, Parking parking){
        db.delete(PARKING_TABLE, PARKING_CAR_ID + " = " + parking.getCarId(),null);
        setLastUpdateDate(db);
    }

    public static void stopParking(SQLiteDatabase db, Car car){
        db.delete(PARKING_TABLE, PARKING_CAR_ID + " = " + car.getCarId(),null);
        setLastUpdateDate(db);
    }

    public static void drop(SQLiteDatabase db) {
        db.execSQL("drop table " + PARKING_TABLE + ";");
    }

    public static String getLastUpdateDate(SQLiteDatabase db) {
        return LastUpdateSql.getLastUpdate(db, PARKING_TABLE);
    }

    public static void setLastUpdateDate(SQLiteDatabase db) {
        LastUpdateSql.setLastUpdate(db, PARKING_TABLE);
    }


}
