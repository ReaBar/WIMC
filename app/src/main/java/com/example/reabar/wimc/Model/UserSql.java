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

    public static void create(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " +
                Constants.USER_TABLE + " (" +
                Constants.USER_ID + " TEXT PRIMARY KEY," +
                Constants.USER_EMAIL + " TEXT);");
    }

    public static void drop(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + Constants.USER_TABLE + ";");
    }

    public static void getUsersList(SQLiteDatabase db, Model.SyncListener listener) {
        Cursor cursor = db.query(Constants.USER_TABLE, null, null, null, null, null, null);
        List<User> users = new ArrayList<>();

        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(Constants.USER_ID);
            int emailIndex = cursor.getColumnIndex(Constants.USER_EMAIL);


            do {
                String id = cursor.getString(idIndex);
                String email = cursor.getString(emailIndex);

                User us = new User(id, email);
                users.add(us);
            } while (cursor.moveToNext());
        }
        if (cursor != null)
            cursor.close();
        listener.passData(users);
    }

    public static void isUserExistsByEmail(SQLiteDatabase db, String email, Model.SyncListener listener) {
        Cursor cursor = db.rawQuery("SELECT * FROM " + Constants.USER_TABLE + " WHERE " + Constants.USER_EMAIL + " = ?", new String[]{email});

        if (cursor.moveToFirst()) {
            listener.passData(true);
        }
        listener.passData(false);
    }

    public static void addUser(SQLiteDatabase db, User us) {
        ContentValues values = new ContentValues();
        values.put(Constants.USER_ID, us.getUserId());
        values.put(Constants.USER_EMAIL, us.getEmail());

        db.insertWithOnConflict(Constants.USER_TABLE, Constants.USER_ID, values, SQLiteDatabase.CONFLICT_REPLACE);
    }
}


