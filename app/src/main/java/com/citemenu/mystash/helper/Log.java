package com.citemenu.mystash.helper;

/**
 * Created by dev.arsalan on 8/10/2016.
 */
public class Log {
    private static final String TAG = "MyStash: ";

    public static void d(String msg) {
        d(msg, null);
    }

    public static void d(String msg, Throwable e) {
        android.util.Log.d(TAG, Thread.currentThread().getName() + "| " + msg, e);
    }

    public static void i(String msg) {
        i(msg, null);
    }

    public static void i(String msg, Throwable e) {
        android.util.Log.i(TAG, Thread.currentThread().getName() + "| " + msg, e);
    }

    public static void e(String msg) {
        e(msg, null);
    }

    public static void e(String msg, Throwable e) {
        android.util.Log.e(TAG, Thread.currentThread().getName() + "| " + msg, e);
    }

    public static void v(String msg) {
        android.util.Log.v(TAG, Thread.currentThread().getName() + "| " + msg);
    }

    public static void w(String msg) {
        android.util.Log.w(TAG, Thread.currentThread().getName() + "| " + msg);
    }

}
