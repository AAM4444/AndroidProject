package com.example.myapplication2;

//import
import okhttp3.logging.HttpLoggingInterceptor;

public class OkHttpInitialize {

    public void okHttp() {
        HttpLoggingInterceptor logger = new HttpLoggingInterceptor();
        logger.setLevel(HttpLoggingInterceptor.Level.BODY);
    }

}
