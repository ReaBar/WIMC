package com.example.reabar.wimc.Model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by admin on 8/6/16.
 */
public class LastUpdateSql {

    public static void create(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " +
                Constants.LAST_UPDATE_TABLE + " (" +
                Constants.LAST_UPDATE_TABLE_NAME + " TEXT PRIMARY KEY," +
                Constants.LAST_UPDATE_TIME + " TEXT);" );
    }

    public static void drop(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + Constants.LAST_UPDATE_TABLE + ";");
    }

    public static String getLastUpdate(SQLiteDatabase db, String tableName) {
        Cursor cursor = db.query(Constants.LAST_UPDATE_TABLE, null, Constants.LAST_UPDATE_TABLE_NAME + " = ?",new String[]{tableName} , null, null, null);
        if (cursor.moveToFirst()) {
            return cursor.getString(cursor.getColumnIndex(Constants.LAST_UPDATE_TIME));
        }
        return null;
    }

    public static void setLastUpdate(SQLiteDatabase db, String table,final long currentTime) {
        ContentValues values = new ContentValues();
        values.put(Constants.LAST_UPDATE_TABLE_NAME, table);
        values.put(Constants.LAST_UPDATE_TIME, currentTime);

        db.insertWithOnConflict(Constants.LAST_UPDATE_TABLE, Constants.LAST_UPDATE_TABLE_NAME,values, SQLiteDatabase.CONFLICT_REPLACE);
    }
}
