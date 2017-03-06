package com.citemenu.mystash.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Toast;

/**
 * Created by Dev.Arsalan on 11/3/2016.
 */

public class ToastUtil {
    public static void showShortMessage(@NonNull Context context, @NonNull String msg) {
        if (msg == null || context == null) {
            return;
        }
        Toast.makeText(context.getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    public static void showLongMessage(@NonNull Context context, @NonNull String msg) {
        if (msg == null || context == null) {
            return;
        }
        Toast.makeText(context.getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }
}
