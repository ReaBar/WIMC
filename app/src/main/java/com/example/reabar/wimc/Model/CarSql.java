package com.example.reabar.wimc.Model;

import android.content.ContentValues;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import java.util.ArrayList;

import java.util.List;

/**
 * Created by admin on 8/6/16.
 */


public class CarSql {
    final static String CAR_TABLE = "car";
    final static String CAR_TABLE_ID = "_id";
    final static String CAR_TABLE_COLOR = "color";
    final static String CAR_TABLE_MODEL = "model";
    final static String CAR_TABLE_COMPANY = "company";
    final static String CAR_TABLE_USER_OWNER_ID = "user_owner_id";

    public void create(SQLiteDatabase db) {
        db.execSQL("create table " + CAR_TABLE + " (" +
                CAR_TABLE_ID + " TEXT PRIMARY KEY," +
                CAR_TABLE_COLOR + " TEXT," +
                CAR_TABLE_MODEL + " TEXT," +
                CAR_TABLE_COMPANY + " TEXT," +
                CAR_TABLE_USER_OWNER_ID + " TEXT);");
    }

    public void drop(SQLiteDatabase db) {
        db.execSQL("drop table " + CAR_TABLE + ";");
    }

    public List<Car> getAllCars(SQLiteDatabase db) {
        Cursor cursor = db.query(CAR_TABLE, null, null, null, null, null, null);
        List<Car> cars = new ArrayList<Car>();

        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(CAR_TABLE_ID);
            int colorIndex = cursor.getColumnIndex(CAR_TABLE_COLOR);
            int modelIndex = cursor.getColumnIndex(CAR_TABLE_MODEL);
            int companyIndex = cursor.getColumnIndex(CAR_TABLE_COMPANY);
            int userOwnerIdIndex = cursor.getColumnIndex(CAR_TABLE_USER_OWNER_ID);

            do {
                String id = cursor.getString(idIndex);
                String color = cursor.getString(colorIndex);
                String model = cursor.getString(modelIndex);
                String company = cursor.getString(companyIndex);
                String userOwnerId = cursor.getString(userOwnerIdIndex);
                //0 false / 1 true
                Car cr = new Car(id, color, model, company, userOwnerId);
                cars.add(cr);
            } while (cursor.moveToNext());
        }
        return cars;
    }

    public Car getCarById(SQLiteDatabase db, String id) {
        String[] params = new String[1];
        params[0] = id;
        Cursor cursor = db.query(CAR_TABLE, null, CAR_TABLE_ID, params, null, null, null);

        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(CAR_TABLE_ID);
            int colorIndex = cursor.getColumnIndex(CAR_TABLE_COLOR);
            int modelIndex = cursor.getColumnIndex(CAR_TABLE_MODEL);
            int companyIndex = cursor.getColumnIndex(CAR_TABLE_COMPANY);
            int userOwnerIdIndex = cursor.getColumnIndex(CAR_TABLE_USER_OWNER_ID);

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

    public void addCar(SQLiteDatabase db, Car car) {
        ContentValues values = new ContentValues();
        values.put(CAR_TABLE_ID, car.getCarId());
        values.put(CAR_TABLE_COLOR, car.getColor());
        values.put(CAR_TABLE_MODEL, car.getModel());
        values.put(CAR_TABLE_COMPANY, car.getCompany());
        values.put(CAR_TABLE_USER_OWNER_ID, car.getUserOwnerId());


        db.insertWithOnConflict(CAR_TABLE, CAR_TABLE_ID, values, SQLiteDatabase.CONFLICT_REPLACE);
    }

    public String getLastUpdateDate(SQLiteDatabase db) {
        return LastUpdateSql.getLastUpdate(db, CAR_TABLE);
    }

    public void setLastUpdateDate(SQLiteDatabase db, String date) {
        LastUpdateSql.setLastUpdate(db, CAR_TABLE, date);
    }
}

