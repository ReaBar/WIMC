package com.example.reabar.wimc.Model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.reabar.wimc.MyApplication;

/**
 * Created by reabar on 25.5.2016.
 */
public class ModelSql {
    private static final int VERSION = 2;

    MyDBHelper dbHelper;
    private CarSql carSql;
    private UserSql userSql;
    private  ParkingSql parkingSql;
    private  LastUpdateSql lastUpdateSql;

    public ModelSql() {
        dbHelper = new MyDBHelper(MyApplication.getAppContext());
        carSql = new CarSql();
        userSql = new UserSql();
        parkingSql = new ParkingSql();
        lastUpdateSql= new LastUpdateSql();
    }

    public void addUser(User user) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        //UserSQL.add(db, user);
    }

    public void addCar(Car car) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        carSql.addCar(db,car);
    }

    public Car getCarById(String id){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        return carSql.getCarById(db,id);
    }

    public String getCarLastUpdate(){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        return carSql.getLastUpdateDate(db);
    }


    class MyDBHelper extends SQLiteOpenHelper {
        public MyDBHelper(Context context) {
            super(context, "database.db", null, VERSION);
            dbHelper.getWritableDatabase();

        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            //create the DB schema
            carSql.create(db);
            userSql.create(db);
            parkingSql.create(db);
            lastUpdateSql.create(db);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            onCreate(db);
        }
    }
}
