package com.example.myapplication2;

public class MyApplication extends com.activeandroid.app.Application {

    public void onCreate() {
        super.onCreate();
        Singleton.INSTANCE.stethoInitialize(this);
    }
}


