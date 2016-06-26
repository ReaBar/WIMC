package com.example.reabar.wimc;

import android.app.Application;
import android.content.Context;

/**
 * Created by reabar on 25.5.2016.
 */
public class MyApplication extends Application {
    private static Context context;
    public MyApplication(Context context){
        this.onCreate(context);
    }
    public void onCreate(Context context) {
        super.onCreate();
        MyApplication.context = context;
    }
    public static Context getAppContext() {
        return MyApplication.context;
    }
}
