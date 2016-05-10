package com.example.mystashapp.mystashappproject.webservicefactory;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.example.mystashapp.mystashappproject.Constant_util;
import com.example.mystashapp.mystashappproject.pojo.pojo_login.Users;
import com.google.gson.Gson;

public class CustomSharedPrefLogin {

    public static void setUserObject(Context _context, Users object) {
        String json = (new Gson()).toJson(object);

        Editor editor = _context.getSharedPreferences(Constant_util.PREFS_NAME, 0).edit();
        editor.putString(Constant_util.USER_OBJECT, json);
        editor.putString(Constant_util.IS_LOGIN, Constant_util.IS_LOGIN);
        editor.apply();
    }

    public static Users getUserObject(Context _context) {
        SharedPreferences pref = _context.getSharedPreferences(Constant_util.PREFS_NAME, 0);
        String json = pref.getString(Constant_util.USER_OBJECT, Constant_util.USER_OBJECT);
        return (new Gson().fromJson(json, Users.class));
    }

    public static void RemoveUserObject(Context _context) {
        SharedPreferences pref = _context.getSharedPreferences(Constant_util.PREFS_NAME, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.remove(Constant_util.USER_OBJECT);
        editor.apply();
    }
}
