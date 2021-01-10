package com.example.myapplication2;

import android.content.res.Resources;
import android.util.DisplayMetrics;

import com.bumptech.glide.load.engine.Resource;

public class Utils {

    public static int getWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static float getDpi() {
        return Resources.getSystem().getDisplayMetrics().xdpi;
    }

    public static float getWidthDp() {
        return (getWidth()*160/getDpi());
    }
}
