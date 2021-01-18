package com.example.myapplication2;

import com.facebook.stetho.Stetho;

public class MyApplication extends com.activeandroid.app.Application {

    public void onCreate() {
        super.onCreate();
        stethoInitialize();
    }

    public void stethoInitialize() {
        Stetho.initializeWithDefaults(this);
        Stetho.InitializerBuilder initializerBuilder = Stetho.newInitializerBuilder(this);
        initializerBuilder.enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this));
        initializerBuilder.enableDumpapp(Stetho.defaultDumperPluginsProvider(this));
        Stetho.Initializer initializer = initializerBuilder.build();
        Stetho.initialize(initializer);
        Stetho.initializeWithDefaults(this);
    }

}
