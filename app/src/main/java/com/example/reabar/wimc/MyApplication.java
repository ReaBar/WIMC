package com.example.reabar.wimc;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

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
    }
    public static Context getAppContext() {
        return MyApplication.context;
    }

    public static Activity getAppActivity(){
        return MyApplication.activity;
    }
}
