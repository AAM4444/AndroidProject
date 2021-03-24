package com.example.myapplication2;

//import
import android.app.Application;

public class MyApplication extends Application {

    private static MyApplication instance;

    public void onCreate() {
        super.onCreate();
        instance = this;
        Singleton.INSTANCE.CreateDatabase(getApplicationContext());
    }

    public static MyApplication getInstance() {
        return instance;
    }

    public UsersDatabase getDatabase() {
        return Singleton.INSTANCE.CreateDatabase(getApplicationContext());
    }
}