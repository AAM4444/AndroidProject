package com.example.myapplication2;

import android.content.res.Resources;

public class Utils {

    public static float getWidth() {
        return (float)Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static float getDpi() {
        return (float) Resources.getSystem().getDisplayMetrics().densityDpi;
    }

    public static float getHeight() {
        return (float) (50*(getDpi()/160));
    }
}
