package com.citemenu.mystash.utils;

import android.support.annotation.NonNull;
import android.util.Log;

/**
 * Created by Dev.Arsalan on 12/20/2016.
 */

public class LogUtil {
    private static boolean isDebugModeEnabled = true; //todo set it to false before release apk

    /**
     * @param TAG set tag for log
     * @param msg set message to display in log
     */
    public static void d(@NonNull String TAG, @NonNull String msg) {
        if (isDebugModeEnabled) {
            if (TAG == null) {
                TAG = "TAG NULL FOUND";
            }
            if (msg == null) {
                msg = "MSG NULL FOUND";
            }
            Log.d(TAG, msg);
        }
    }

    /**
     * @param TAG set tag for log
     * @param msg set message to display in log
     */
    public static void e(@NonNull String TAG, @NonNull String msg) {
        if (isDebugModeEnabled) {
            if (TAG == null) {
                TAG = "TAG NULL FOUND";
            }
            if (msg == null) {
                msg = "MSG NULL FOUND";
            }
            Log.e(TAG, msg);
        }
    }
}