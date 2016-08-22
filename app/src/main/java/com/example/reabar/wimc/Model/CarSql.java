package com.example.reabar.wimc.Model;

import android.content.ContentValues;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import java.util.ArrayList;

import java.util.Arrays;
import java.util.List;

/**
 * Created by admin on 8/6/16.
 */


public class CarSql {

    public static void create(SQLiteDatabase db) {
        //TODO add isParkingActive
        db.execSQL("CREATE TABLE IF NOT EXISTS " +
                Constants.CAR_TABLE + " (" +
                Constants.CAR_ID + " TEXT PRIMARY KEY," +
                Constants.CAR_COLOR + " TEXT," +
                Constants.CAR_MODEL + " TEXT," +
                Constants.CAR_COMPANY + " TEXT," +
                Constants.CAR_USER_OWNER_ID + " TEXT, " +
                Constants.CAR_USERS_LIST + " TEXT);");
    }

    public static void drop(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + Constants.CAR_TABLE + ";");
    }

    public static void getAllCars(SQLiteDatabase db, Model.SyncListener listener) {
        Cursor cursor = db.query(Constants.CAR_TABLE, null, null, null, null, null, null);
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
                List<String> usersList = convertStringToList(cursor.getString(userListIndex));
                //0 false / 1 true
                Car car = new Car(id, color, model, company, userOwnerId);
                if(usersList != null){
                    for (String user : usersList) {
                        car.setNewCarUser(user);
                    }
                }
                cars.add(car);
            } while (cursor.moveToNext());
        }
        listener.passData(cars);
    }

    public static Car getCarById(SQLiteDatabase db, String id) {
        String[] params = new String[1];
        params[0] = id;
        Cursor cursor = db.query(Constants.CAR_TABLE, null, Constants.CAR_ID + " = ?", params, null, null, null);

        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(Constants.CAR_ID);
            int colorIndex = cursor.getColumnIndex(Constants.CAR_COLOR);
            int modelIndex = cursor.getColumnIndex(Constants.CAR_MODEL);
            int companyIndex = cursor.getColumnIndex(Constants.CAR_COMPANY);
            int userOwnerIdIndex = cursor.getColumnIndex(Constants.CAR_USER_OWNER_ID);

            String objectId = cursor.getString(idIndex);
            String color = cursor.getString(colorIndex);
            String model = cursor.getString(modelIndex);
            String company = cursor.getString(companyIndex);
            String userOwnerId = cursor.getString(userOwnerIdIndex);
            if (cursor != null) {
                cursor.close();
            }

            //0 false / 1 true
            Car car = new Car(objectId, color, model, company, userOwnerId);
            return car;
        }
        return null;
    }

    public static void addCarToDB(SQLiteDatabase db, Car car) {
        ContentValues values = new ContentValues();
        values.put(Constants.CAR_ID, car.getCarId());
        values.put(Constants.CAR_COLOR, car.getColor());
        values.put(Constants.CAR_MODEL, car.getModel());
        values.put(Constants.CAR_COMPANY, car.getCompany());
        values.put(Constants.CAR_USER_OWNER_ID, car.getUserOwnerId());
        values.put(Constants.CAR_USERS_LIST, convertListToString(car.getUsersList()));

        db.insertWithOnConflict(Constants.CAR_TABLE, Constants.CAR_ID, values, SQLiteDatabase.CONFLICT_REPLACE);
    }

    public static void removeCarFromDb(SQLiteDatabase db, Car car) {
        db.delete(Constants.CAR_TABLE, Constants.CAR_ID + " = " + car.getCarId(), null);
    }

    public static void updateCar(SQLiteDatabase db, Car car) {
        ContentValues values = new ContentValues();
        values.put(Constants.CAR_ID, car.getCarId());
        values.put(Constants.CAR_COLOR, car.getColor());
        values.put(Constants.CAR_MODEL, car.getModel());
        values.put(Constants.CAR_COMPANY, car.getCompany());
        values.put(Constants.CAR_USER_OWNER_ID, car.getUserOwnerId());
        values.put(Constants.CAR_USERS_LIST, convertListToString(car.getUsersList()));

        int update = db.update(Constants.CAR_TABLE, values, Constants.CAR_ID + " = ?", new String[]{car.getCarId()});
        if (update < 0) {
            db.insert(Constants.CAR_TABLE, Constants.CAR_ID, values);
        }
    }

    public static void getOwnedCars(SQLiteDatabase db, String uId, Model.SyncListener listener) {
        Cursor cursor = db.rawQuery("SELECT * FROM " + Constants.CAR_TABLE + " WHERE " + Constants.CAR_USER_OWNER_ID + " = ?", new String[]{uId});

        List<Car> carOwnedList = new ArrayList<>();
        if (cursor.moveToFirst()) {
            int carIdIndex = cursor.getColumnIndex(Constants.CAR_ID);
            int colorIndex = cursor.getColumnIndex(Constants.CAR_COLOR);
            int modelIndex = cursor.getColumnIndex(Constants.CAR_MODEL);
            int companyIndex = cursor.getColumnIndex(Constants.CAR_COMPANY);
            int userOwnerIndex = cursor.getColumnIndex(Constants.CAR_USER_OWNER_ID);
            int usersListIndex = cursor.getColumnIndex(Constants.CAR_USERS_LIST);

            do {
                String id = cursor.getString(carIdIndex);
                String company = cursor.getString(companyIndex);
                String model = cursor.getString(modelIndex);
                String userOwner = cursor.getString(userOwnerIndex);
                String color = cursor.getString(colorIndex);
                String usersList = cursor.getString(usersListIndex);

                Car car = new Car(id, color, model, company, userOwner);
                List<String> carUsers = convertStringToList(usersList);
                for (String user : carUsers) {
                    car.setNewCarUser(user);
                }
                carOwnedList.add(car);
            } while (cursor.moveToNext());
        }

        listener.passData(carOwnedList);
    }

    public static void getListOfSharedCars(SQLiteDatabase db, String uId, Model.SyncListener listener) {
        Cursor cursor = db.rawQuery("SELECT * FROM " + Constants.CAR_TABLE + " WHERE " + Constants.CAR_USERS_LIST + " = ?", new String[]{"%" + uId + "%"});

        List<Car> sharedCars = new ArrayList<>();
        if (cursor.moveToFirst()) {
            int carIdIndex = cursor.getColumnIndex(Constants.CAR_ID);
            int colorIndex = cursor.getColumnIndex(Constants.CAR_COLOR);
            int modelIndex = cursor.getColumnIndex(Constants.CAR_MODEL);
            int companyIndex = cursor.getColumnIndex(Constants.CAR_COMPANY);
            int userOwnerIndex = cursor.getColumnIndex(Constants.CAR_USER_OWNER_ID);
            int usersListIndex = cursor.getColumnIndex(Constants.CAR_USERS_LIST);

            do {
                String id = cursor.getString(carIdIndex);
                String company = cursor.getString(companyIndex);
                String model = cursor.getString(modelIndex);
                String userOwner = cursor.getString(userOwnerIndex);
                String color = cursor.getString(colorIndex);
                String usersList = cursor.getString(usersListIndex);

                Car car = new Car(id, color, model, company, userOwner);
                List<String> carUsers = convertStringToList(usersList);
                for (String user : carUsers) {
                    car.setNewCarUser(user);
                }
                sharedCars.add(car);
            } while (cursor.moveToNext());
        }

        listener.passData(sharedCars);
    }

    public static void getListOfAllCarsInDB(SQLiteDatabase db, Model.SyncListener listener) {
        //Cursor cursor = db.rawQuery("SELECT * FROM " + CAR_TABLE +" WHERE type = 'table'",null);
        Cursor cursor = db.query(Constants.CAR_TABLE, null, null, null, null, null, null);

        List<Car> allCars = new ArrayList<>();
        if (cursor.moveToFirst()) {
            int carIdIndex = cursor.getColumnIndex(Constants.CAR_ID);
            int colorIndex = cursor.getColumnIndex(Constants.CAR_COLOR);
            int modelIndex = cursor.getColumnIndex(Constants.CAR_MODEL);
            int companyIndex = cursor.getColumnIndex(Constants.CAR_COMPANY);
            int userOwnerIndex = cursor.getColumnIndex(Constants.CAR_USER_OWNER_ID);
            int usersListIndex = cursor.getColumnIndex(Constants.CAR_USERS_LIST);

            do {
                String id = cursor.getString(carIdIndex);
                String company = cursor.getString(companyIndex);
                String model = cursor.getString(modelIndex);
                String userOwner = cursor.getString(userOwnerIndex);
                String color = cursor.getString(colorIndex);
                String usersList = cursor.getString(usersListIndex);

                Car car = new Car(id, color, model, company, userOwner);
                List<String> carUsers = convertStringToList(usersList);
                for (String user : carUsers) {
                    car.setNewCarUser(user);
                }
                allCars.add(car);
            } while (cursor.moveToNext());
        }

        listener.passData(allCars);
    }

    public static String convertListToString(List<String> stringList) {
        StringBuffer stringBuffer = new StringBuffer();
        for (String str : stringList) {
            stringBuffer.append(str).append(Constants.LIST_SEPARATOR);
        }

        // Remove last separator
        if (stringBuffer.length() > 0) {
            int lastIndex = stringBuffer.lastIndexOf(Constants.LIST_SEPARATOR);
            stringBuffer.delete(lastIndex, lastIndex + Constants.LIST_SEPARATOR.length() + 1);
            return stringBuffer.toString();
        }
        return null;
    }

    public static List<String> convertStringToList(String str) {
        if(str == null){
            return null;
        }
        return Arrays.asList(str.split(Constants.LIST_SEPARATOR));
    }

    public static String getLastUpdateDate(SQLiteDatabase db) {
        return LastUpdateSql.getLastUpdate(db, Constants.CAR_TABLE);
    }

    public static void setLastUpdateDate(SQLiteDatabase db,long currentTime) {
        LastUpdateSql.setLastUpdate(db, Constants.CAR_TABLE,currentTime);
    }
}

