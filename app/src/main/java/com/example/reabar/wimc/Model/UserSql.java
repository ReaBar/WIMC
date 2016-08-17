package com.example.reabar.wimc.Model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 8/6/16.
 */
public class UserSql {

    final static String USER_TABLE = "car";
    final static String USER_TABLE_ID = "_id";
    final static String USER_TABLE_EMAIL = "email";


    static public void create(SQLiteDatabase db) {
        db.execSQL("create table " + USER_TABLE + " (" +
                USER_TABLE_ID + " TEXT PRIMARY KEY," +
                USER_TABLE_EMAIL + " TEXT);");
    }

    public static void drop(SQLiteDatabase db) {
        db.execSQL("drop table " + USER_TABLE + ";");
    }

    public static List<User> getAllUsers(SQLiteDatabase db) {
        Cursor cursor = db.query(USER_TABLE, null, null , null, null, null, null);
        List<User> users = new ArrayList<User>();

        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(USER_TABLE_ID);
            int emailIndex = cursor.getColumnIndex(USER_TABLE_EMAIL);


            do {
                String id = cursor.getString(idIndex);
                String email = cursor.getString(emailIndex);

                User us = new User(id,email);
                users.add(us);
            } while (cursor.moveToNext());
        }
        if(cursor != null)
            cursor.close();
        return users;
    }

    public static User getUserById(SQLiteDatabase db, String id) {
        String[] params = new String[1];
        params[0] = id;
        Cursor cursor = db.query(USER_TABLE, null, USER_TABLE_ID, params, null, null, null);

        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(USER_TABLE_ID);
            int emailIndex = cursor.getColumnIndex(USER_TABLE_EMAIL);

            String objectId = cursor.getString(idIndex);
            String email = cursor.getString(emailIndex);

            if(cursor != null)
                cursor.close();


            //0 false / 1 true
            User us = new User(objectId,email);
            return us;
        }
        return null;
    }

    public static void add(SQLiteDatabase db, User us) {
        ContentValues values = new ContentValues();
        values.put(USER_TABLE_ID, us.getUserId());
        values.put(USER_TABLE_EMAIL, us.getEmail());


        db.insertWithOnConflict(USER_TABLE, USER_TABLE_ID, values, SQLiteDatabase.CONFLICT_REPLACE);
    }

    public static String getLastUpdateDate(SQLiteDatabase db){
        return LastUpdateSql.getLastUpdate(db,USER_TABLE);
    }
    public static void setLastUpdateDate(SQLiteDatabase db, String date){
        LastUpdateSql.setLastUpdate(db,USER_TABLE, date);
    }
}


