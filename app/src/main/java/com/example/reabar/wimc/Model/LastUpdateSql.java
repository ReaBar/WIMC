package com.example.reabar.wimc.Model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by admin on 8/6/16.
 */
public class LastUpdateSql {

    final static String LAST_UPDATE_TABLE = "last_update";
    final static String LAST_UPDATE_TABLE_NAME = "table_name";
    final static String LAST_UPDATE_TIME = "last_update_time";

    public static void create(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " +
                LAST_UPDATE_TABLE + " (" +
                LAST_UPDATE_TABLE_NAME + " TEXT PRIMARY KEY," +
                LAST_UPDATE_TIME + " TEXT);" );
    }

    public static void drop(SQLiteDatabase db) {
        db.execSQL("drop table " + LAST_UPDATE_TABLE + ";");
    }

    public static String getLastUpdate(SQLiteDatabase db, String tableName) {
        Cursor cursor = db.query(LAST_UPDATE_TABLE, null, LAST_UPDATE_TABLE_NAME + " = ?",new String[]{tableName} , null, null, null);
        if (cursor.moveToFirst()) {
            return cursor.getString(cursor.getColumnIndex(LAST_UPDATE_TIME));
        }
        return null;
    }

    public static void setLastUpdate(SQLiteDatabase db, String table) {
        ContentValues values = new ContentValues();
        values.put(LAST_UPDATE_TABLE_NAME, table);
        values.put(LAST_UPDATE_TIME, System.currentTimeMillis());

        db.insertWithOnConflict(LAST_UPDATE_TABLE, LAST_UPDATE_TABLE_NAME,values, SQLiteDatabase.CONFLICT_REPLACE);
    }
}
