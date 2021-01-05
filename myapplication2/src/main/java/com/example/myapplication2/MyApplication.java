package com.example.myapplication2;

import com.facebook.stetho.Stetho;

public class MyApplication extends com.activeandroid.app.Application {
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
    }
}
