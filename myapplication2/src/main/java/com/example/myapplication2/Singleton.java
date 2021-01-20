package com.example.myapplication2;

import android.content.Context;

import com.facebook.stetho.Stetho;

public class Singleton {

    public static final Singleton INSTANCE = new Singleton();

    private Singleton() {

    }

    public void stethoInitialize(Context context) {
            Stetho.initializeWithDefaults(context);
            Stetho.InitializerBuilder initializerBuilder = Stetho.newInitializerBuilder(context);
            initializerBuilder.enableWebKitInspector(Stetho.defaultInspectorModulesProvider(context));
            initializerBuilder.enableDumpapp(Stetho.defaultDumperPluginsProvider(context));
            Stetho.Initializer initializer = initializerBuilder.build();
            Stetho.initialize(initializer);
            Stetho.initializeWithDefaults(context);
    }

}
