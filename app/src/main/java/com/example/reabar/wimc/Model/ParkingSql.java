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
    final static String PARKING_TABLE = "car";
    final static String PARKING_TABLE_ID = "_id";
    final static String PARKING_TABLE_CAR = "car";
    final static String PARKING_TABLE_STREET = "street";
    final static String PARKING_TABLE_CITY = "city";
    final static String PARKING_TABLE_PARKING_LOT_NAME = "parkingLotName";
    final static String PARKING_TABLE_PARKING_LOT_ROW_COLOR = "parkingLotRowColor";
    final static String PARKING_TABLE_STREET_NUMBER = "streetNumber";
    final static String PARKING_TABLE_PARKING_LOT_NUMBER = "parkingLotNumber";
    final static String PARKING_TABLE_PARKING_LOT_FLOOR = "parkingLotFloor";
    final static String PARKING_TABLE_LATITUDE = "latitude";
    final static String PARKING_TABLE_LONGTITUDE = "longtitude";
    final static String PARKING_TABLE_PARKING_IS_ACTIVE = "parkingIsActive";

    static public void create(SQLiteDatabase db) {
        db.execSQL("create table " + PARKING_TABLE + " (" +
                PARKING_TABLE_ID + " TEXT PRIMARY KEY," +
                PARKING_TABLE_CAR + " TEXT," +
                PARKING_TABLE_STREET + " TEXT,"+
                PARKING_TABLE_CITY + " TEXT,"+
                PARKING_TABLE_PARKING_LOT_NAME + " TEXT,"+
                PARKING_TABLE_PARKING_LOT_ROW_COLOR + " TEXT,"+
                PARKING_TABLE_STREET_NUMBER + " INTEGER,"+
                PARKING_TABLE_PARKING_LOT_NUMBER + "INTEGER,"+
                PARKING_TABLE_PARKING_LOT_FLOOR +"INTEGER,"+
                PARKING_TABLE_LATITUDE +"REAL,"+
                PARKING_TABLE_LONGTITUDE +"REAL,"+
                PARKING_TABLE_PARKING_IS_ACTIVE +"BOOLEAN");
    }

    public static void drop(SQLiteDatabase db) {
        db.execSQL("drop table " + PARKING_TABLE + ";");
    }



}
