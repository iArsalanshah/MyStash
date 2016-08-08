package com.citemenu.mystash.helper;

import android.util.Log;

/**
 * Created by dev.arsalan on 6/29/2016.
 */
public class L {
    private static final String TAG = "ImageZoomCrop";

    public static void e(Throwable e) {
        Log.e(TAG, e.getMessage(), e);
    }

    public static void e(String msg) {
        Log.e(TAG, msg);
    }
}
