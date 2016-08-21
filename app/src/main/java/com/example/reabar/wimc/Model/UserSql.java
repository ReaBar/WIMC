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

    final static String USER_TABLE = "users";
    final static String USER_ID = "user_id";
    final static String USER_EMAIL = "email";


    public static void create(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " +
                USER_TABLE + " (" +
                USER_ID + " TEXT PRIMARY KEY," +
                USER_EMAIL + " TEXT);");
    }

    public static void drop(SQLiteDatabase db) {
        db.execSQL("drop table " + USER_TABLE + ";");
    }

    public static List<User> getUsersList(SQLiteDatabase db) {
        Cursor cursor = db.query(USER_TABLE, null, null , null, null, null, null);
        List<User> users = new ArrayList<>();

        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(USER_ID);
            int emailIndex = cursor.getColumnIndex(USER_EMAIL);


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

    public static boolean isUserExistsByEmail(SQLiteDatabase db, String email) {
        Cursor cursor = db.rawQuery("SELECT * FROM " + USER_TABLE + " WHERE " + USER_EMAIL + " = ?",new String[]{email});

        if (cursor.moveToFirst()) {
            return true;
        }
        return false;
    }

    public static void addUser(SQLiteDatabase db, User us) {
        ContentValues values = new ContentValues();
        values.put(USER_ID, us.getUserId());
        values.put(USER_EMAIL, us.getEmail());

        db.insertWithOnConflict(USER_TABLE, USER_ID, values, SQLiteDatabase.CONFLICT_REPLACE);
        setLastUpdateDate(db);
    }

    public static String getLastUpdateDate(SQLiteDatabase db){
        return LastUpdateSql.getLastUpdate(db,USER_TABLE);
    }
    public static void setLastUpdateDate(SQLiteDatabase db){
        LastUpdateSql.setLastUpdate(db,USER_TABLE);
    }
}


