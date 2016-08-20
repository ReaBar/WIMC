package com.example.reabar.wimc;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.example.reabar.wimc.Model.Model;
/**
 * Created by reabar on 25.5.2016.
 */
public class MyApplication extends Application {
    private static Context context;
    private static Activity activity;
    public MyApplication(Context context, Activity activity){
        this.onCreate(context,activity);
    }
    public void onCreate(Context context, Activity activity) {
        super.onCreate();
        MyApplication.context = context;
        MyApplication.activity = activity;

        //initialize model
        Model.getInstance();


        //check for default image
      /*  Bitmap defaultImage = BitmapFactory.decodeResource(getResources(), R.drawable.image);
        if(defaultImage == null)
            Log.d("TAG", "null default image");
        //Model.getInstance().SetLocalBitmap(defaultImage);
        */
    }




    public static Context getAppContext() {
        return MyApplication.context;
    }

    public static Activity getAppActivity(){
        return MyApplication.activity;
    }
}
