package com.example.mystashapp.mystashappproject.webservicefactory;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.example.mystashapp.mystashappproject.Constant_util;
import com.example.mystashapp.mystashappproject.pojo.getcardslist_pojo.Getloyalty;
import com.example.mystashapp.mystashappproject.pojo.pojo_login.Users;
import com.google.gson.Gson;

public class CustomSharedPref {

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

    public static void setGetLoyaltyData(Context context, Getloyalty getloyalty) {
        Editor editor = context.getSharedPreferences(Constant_util.PREFS_NAME, 0).edit();
        editor.putString("getLoyaltyData", new Gson().toJson(getloyalty)).apply();
    }

    public static Getloyalty getLoyaltyData(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(Constant_util.PREFS_NAME, 0);
        String obj = preferences.getString("getLoyaltyData", null);
        if (obj == null) {
            return null;
        } else {
            return new Gson().fromJson(obj, Getloyalty.class);
        }
    }

    public static void setLoyaltBardcode(Context context, String img, String barcode_number) {
        Editor editor = context.getSharedPreferences(Constant_util.PREFS_NAME, 0).edit();
        editor.putString("barcodeImage", img);
        editor.putString("barcodeNumber", barcode_number).apply();
    }

    public static void getLoyaltBardcode(Context context, String img, String barcode_number) {
        SharedPreferences pref = context.getSharedPreferences(Constant_util.PREFS_NAME, 0);
        img = pref.getString("barcodeImage", null);
        barcode_number = pref.getString("barcodeNumber", null);
    }

    public static void setLoyaltFrontCard(Context context, String img) {
        Editor editor = context.getSharedPreferences(Constant_util.PREFS_NAME, 0).edit();
        editor.putString("loyaltyFrontCard", img).apply();
    }

    public static String getLoyaltyFronCard(Context context) {
        SharedPreferences pref = context.getSharedPreferences(Constant_util.PREFS_NAME, 0);
        String img = pref.getString("loyaltyFrontCard", null);
        return img != null ? img : null;
    }

    public static void RemoveUserObject(Context context) {
        SharedPreferences pref = context.getSharedPreferences(Constant_util.PREFS_NAME, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.remove(Constant_util.USER_OBJECT);
        editor.apply();
    }
}
