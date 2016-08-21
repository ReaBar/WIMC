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
    final static String CAR_TABLE = "car";
    final static String CAR_ID = "car_id";
    final static String CAR_COLOR = "color";
    final static String CAR_MODEL = "model";
    final static String CAR_COMPANY = "company";
    final static String CAR_USER_OWNER_ID = "user_owner_id";
    final static String CAR_USERS_LIST = "users_list";
    private static String LIST_SEPARATOR = "__,__";


    public static void create(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " +
                CAR_TABLE + " (" +
                CAR_ID + " TEXT PRIMARY KEY," +
                CAR_COLOR + " TEXT," +
                CAR_MODEL + " TEXT," +
                CAR_COMPANY + " TEXT," +
                CAR_USER_OWNER_ID + " TEXT, " +
                CAR_USERS_LIST + " TEXT);");
    }

    public static void drop(SQLiteDatabase db) {
        db.execSQL("drop table " + CAR_TABLE + ";");
    }

    public static List<Car> getAllCars(SQLiteDatabase db) {
        Cursor cursor = db.query(CAR_TABLE, null, null, null, null, null, null);
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
                List<String> usersList = convertStringToList(cursor.getString(userListIndex));
                //0 false / 1 true
                Car car = new Car(id, color, model, company, userOwnerId);
                for (String user:usersList) {
                    car.setNewCarUser(user);
                }
                cars.add(car);
            } while (cursor.moveToNext());
        }
        return cars;
    }

    public static Car getCarById(SQLiteDatabase db, String id) {
        String[] params = new String[1];
        params[0] = id;
        Cursor cursor = db.query(CAR_TABLE, null, CAR_ID + " = ?", params, null, null, null);

        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(CAR_ID);
            int colorIndex = cursor.getColumnIndex(CAR_COLOR);
            int modelIndex = cursor.getColumnIndex(CAR_MODEL);
            int companyIndex = cursor.getColumnIndex(CAR_COMPANY);
            int userOwnerIdIndex = cursor.getColumnIndex(CAR_USER_OWNER_ID);

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
        values.put(CAR_ID, car.getCarId());
        values.put(CAR_COLOR, car.getColor());
        values.put(CAR_MODEL, car.getModel());
        values.put(CAR_COMPANY, car.getCompany());
        values.put(CAR_USER_OWNER_ID, car.getUserOwnerId());
        values.put(CAR_USERS_LIST,convertListToString(car.getUsersList()));

        db.insertWithOnConflict(CAR_TABLE, CAR_ID, values, SQLiteDatabase.CONFLICT_REPLACE);
        setLastUpdateDate(db);
    }

    public static void removeCarFromDb(SQLiteDatabase db,Car car){
        db.delete(CAR_TABLE, CAR_ID + " = " + car.getCarId(),null);
        setLastUpdateDate(db);
    }

    public static void updateCar(SQLiteDatabase db,Car car){
        ContentValues values = new ContentValues();
        values.put(CAR_ID, car.getCarId());
        values.put(CAR_COLOR,car.getColor());
        values.put(CAR_MODEL,car.getModel());
        values.put(CAR_COMPANY,car.getCompany());
        values.put(CAR_USER_OWNER_ID,car.getUserOwnerId());
        values.put(CAR_USERS_LIST,convertListToString(car.getUsersList()));

        int update = db.update(CAR_TABLE, values, CAR_ID + " = ?", new String[]{car.getCarId()});
        if(update < 0){
            db.insert(CAR_TABLE, CAR_ID, values);
        }
        setLastUpdateDate(db);
    }

    public static List<Car> getOwnedCars(SQLiteDatabase db, String uId){
        Cursor cursor = db.rawQuery("SELECT * FROM " + CAR_TABLE +" WHERE " + CAR_USER_OWNER_ID + " = ?",new String[]{uId});

        List<Car> carOwnedList = new ArrayList<>();
        if(cursor.moveToFirst()){
            int carIdIndex = cursor.getColumnIndex(CAR_ID);
            int colorIndex = cursor.getColumnIndex(CAR_COLOR);
            int modelIndex = cursor.getColumnIndex(CAR_MODEL);
            int companyIndex = cursor.getColumnIndex(CAR_COMPANY);
            int userOwnerIndex = cursor.getColumnIndex(CAR_USER_OWNER_ID);
            int usersListIndex = cursor.getColumnIndex(CAR_USERS_LIST);

            do{
                String id = cursor.getString(carIdIndex);
                String company = cursor.getString(companyIndex);
                String model = cursor.getString(modelIndex);
                String userOwner = cursor.getString(userOwnerIndex);
                String color = cursor.getString(colorIndex);
                String usersList = cursor.getString(usersListIndex);

                Car car = new Car(id,color,model,company,userOwner);
                List<String> carUsers = convertStringToList(usersList);
                for (String user:carUsers) {
                    car.setNewCarUser(user);
                }
                carOwnedList.add(car);
            }while (cursor.moveToNext());
        }

        return carOwnedList;
    }

    public static List<Car> getListOfSharedCars(SQLiteDatabase db, String uId){
        Cursor cursor = db.rawQuery("SELECT * FROM " + CAR_TABLE +" WHERE " + CAR_USERS_LIST + " = ?",new String[]{"%" + uId + "%"});

        List<Car> sharedCars = new ArrayList<>();
        if(cursor.moveToFirst()){
            int carIdIndex = cursor.getColumnIndex(CAR_ID);
            int colorIndex = cursor.getColumnIndex(CAR_COLOR);
            int modelIndex = cursor.getColumnIndex(CAR_MODEL);
            int companyIndex = cursor.getColumnIndex(CAR_COMPANY);
            int userOwnerIndex = cursor.getColumnIndex(CAR_USER_OWNER_ID);
            int usersListIndex = cursor.getColumnIndex(CAR_USERS_LIST);

            do{
                String id = cursor.getString(carIdIndex);
                String company = cursor.getString(companyIndex);
                String model = cursor.getString(modelIndex);
                String userOwner = cursor.getString(userOwnerIndex);
                String color = cursor.getString(colorIndex);
                String usersList = cursor.getString(usersListIndex);

                Car car = new Car(id,color,model,company,userOwner);
                List<String> carUsers = convertStringToList(usersList);
                for (String user:carUsers) {
                    car.setNewCarUser(user);
                }
                sharedCars.add(car);
            }while (cursor.moveToNext());
        }

        return sharedCars;
    }

    public static List<Car> getListOfAllCarsInDB(SQLiteDatabase db){
        //Cursor cursor = db.rawQuery("SELECT * FROM " + CAR_TABLE +" WHERE type = 'table'",null);
        Cursor cursor = db.query(CAR_TABLE,null,null,null,null,null,null);

        List<Car> allCars = new ArrayList<>();
        if(cursor.moveToFirst()){
            int carIdIndex = cursor.getColumnIndex(CAR_ID);
            int colorIndex = cursor.getColumnIndex(CAR_COLOR);
            int modelIndex = cursor.getColumnIndex(CAR_MODEL);
            int companyIndex = cursor.getColumnIndex(CAR_COMPANY);
            int userOwnerIndex = cursor.getColumnIndex(CAR_USER_OWNER_ID);
            int usersListIndex = cursor.getColumnIndex(CAR_USERS_LIST);

            do{
                String id = cursor.getString(carIdIndex);
                String company = cursor.getString(companyIndex);
                String model = cursor.getString(modelIndex);
                String userOwner = cursor.getString(userOwnerIndex);
                String color = cursor.getString(colorIndex);
                String usersList = cursor.getString(usersListIndex);

                Car car = new Car(id,color,model,company,userOwner);
                List<String> carUsers = convertStringToList(usersList);
                for (String user:carUsers) {
                    car.setNewCarUser(user);
                }
                allCars.add(car);
            }while (cursor.moveToNext());
        }

        return allCars;
    }

    public static String convertListToString(List<String> stringList) {
        StringBuffer stringBuffer = new StringBuffer();
        for (String str : stringList) {
            stringBuffer.append(str).append(LIST_SEPARATOR);
        }

        // Remove last separator
        if(stringBuffer.length() > 0){
            int lastIndex = stringBuffer.lastIndexOf(LIST_SEPARATOR);
            stringBuffer.delete(lastIndex, lastIndex + LIST_SEPARATOR.length() + 1);
        }

        return stringBuffer.toString();
    }

    public static List<String> convertStringToList(String str) {
        return Arrays.asList(str.split(LIST_SEPARATOR));
    }

    public static String getLastUpdateDate(SQLiteDatabase db) {
        return LastUpdateSql.getLastUpdate(db, CAR_TABLE);
    }

    public static void setLastUpdateDate(SQLiteDatabase db) {
        LastUpdateSql.setLastUpdate(db, CAR_TABLE);
    }
}

