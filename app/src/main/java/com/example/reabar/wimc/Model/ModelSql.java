package com.example.reabar.wimc.Model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.reabar.wimc.MyApplication;

import java.util.ArrayList;

/**
 * Created by reabar on 25.5.2016.
 */
public class ModelSql {
    private static final int VERSION = 2;

    MyDBHelper dbHelper;

    public ModelSql(){ dbHelper = new MyDBHelper(MyApplication.getAppContext());}

    public void addUser(User user){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        //UserSQL.add(db, user);
    }


    class MyDBHelper extends SQLiteOpenHelper {
        public MyDBHelper(Context context) {
            super(context, "database.db", null, VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            //create the DB schema

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            onCreate(db);
        }
    }
}
