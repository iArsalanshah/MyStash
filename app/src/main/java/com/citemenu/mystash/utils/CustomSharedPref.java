package com.citemenu.mystash.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.citemenu.mystash.constant.Constant;
import com.citemenu.mystash.pojo.pojo_login.Users;
import com.google.gson.Gson;

public class CustomSharedPref {

    public static void setUserObject(Context _context, Users object) {
        String json = (new Gson()).toJson(object);
        Editor editor = _context.getSharedPreferences(Constant.PREFS_NAME, 0).edit();
        editor.putString(Constant.USER_OBJECT, json);
        editor.putString(Constant.IS_LOGIN, Constant.IS_LOGIN);
        editor.apply();
    }

    public static Users getUserObject(Context _context) {
        SharedPreferences pref = _context.getSharedPreferences(Constant.PREFS_NAME, 0);
        String json = pref.getString(Constant.USER_OBJECT, Constant.USER_OBJECT);
        return (new Gson().fromJson(json, Users.class));
    }

    public static void setGetLoyaltyData(Context context, com.citemenu.mystash.pojo.getcardslist_pojo.Getloyalty getloyalty) {
        Editor editor = context.getSharedPreferences(Constant.PREFS_NAME, 0).edit();
        editor.putString("getLoyaltyData", new Gson().toJson(getloyalty)).apply();
    }

    public static com.citemenu.mystash.pojo.getcardslist_pojo.Getloyalty getLoyaltyData(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(Constant.PREFS_NAME, 0);
        String obj = preferences.getString("getLoyaltyData", null);
        if (obj == null) {
            return null;
        } else {
            return new Gson().fromJson(obj, com.citemenu.mystash.pojo.getcardslist_pojo.Getloyalty.class);
        }
    }

    public static void setLoyaltBardcode(Context context, String img, String barcode_number) {
        Editor editor = context.getSharedPreferences(Constant.PREFS_NAME, 0).edit();
        editor.putString("barcodeImage", img);
        editor.putString("barcodeNumber", barcode_number).apply();
    }

    public static void getLoyaltBardcode(Context context, String img, String barcode_number) {
        SharedPreferences pref = context.getSharedPreferences(Constant.PREFS_NAME, 0);
        img = pref.getString("barcodeImage", null);
        barcode_number = pref.getString("barcodeNumber", null);
    }

    public static void setLoyaltFrontCard(Context context, String img) {
        Editor editor = context.getSharedPreferences(Constant.PREFS_NAME, 0).edit();
        editor.putString("loyaltyFrontCard", img).apply();
    }

    public static String getLoyaltyFronCard(Context context) {
        SharedPreferences pref = context.getSharedPreferences(Constant.PREFS_NAME, 0);
        String img = pref.getString("loyaltyFrontCard", null);
        return img != null ? img : null;
    }

    public static void RemoveUserObject(Context context) {
        SharedPreferences pref = context.getSharedPreferences(Constant.PREFS_NAME, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.remove(Constant.USER_OBJECT);
        editor.apply();
    }
}
