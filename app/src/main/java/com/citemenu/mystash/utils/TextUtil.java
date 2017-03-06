package com.citemenu.mystash.utils;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.TextView;

import static com.citemenu.mystash.utils.Utils.isNotNullEmpty;

/**
 * Created by Dev.Arsalan on 2/1/2017.
 */

public class TextUtil {
    /**
     * @param textView widget in which the the to be set
     * @param string   value to set in textView
     * @action if string is null/empty
     * then textView would set null
     * as text else the value of string
     */
    public static void setText(@NonNull TextView textView, @Nullable String string) {
        if (isNotNullEmpty(string)) textView.setText(string);
        else textView.setText("");
    }
}
