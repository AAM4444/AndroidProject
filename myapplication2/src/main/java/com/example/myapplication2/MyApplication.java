package com.example.myapplication2;

import android.app.Application;

public class MyApplication extends Application {

    public static MyApplication instance;
    private UsersDatabase database;

    public void onCreate() {
        super.onCreate();
        instance = this;
//        Singleton.INSTANCE.stethoInitialize(this);
        Singleton.INSTANCE.CreateDatabase(getApplicationContext());
    }

    public static MyApplication getInstance() {
        return instance;
    }

    public UsersDatabase getDatabase() {
        return Singleton.INSTANCE.CreateDatabase(getApplicationContext());
    }
}