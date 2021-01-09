package com.example.myapplication2;

import android.content.res.Resources;

import com.bumptech.glide.load.engine.Resource;

public class Utils {

    public static int getWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }
}
