package com.indiasupply.isdental.utils;


import android.content.Context;
import android.content.SharedPreferences;

public class ApplicationDetailsPref {
    public static String SWIGGY_BRANDS = "brands";
    public static String SWIGGY_CATEGORIES = "categories";
    private static ApplicationDetailsPref applicationDetailsPref;
    private String APPLICATION_DETAILS = "APPLICATION_DETAILS";
    
    public static ApplicationDetailsPref getInstance () {
        if (applicationDetailsPref == null)
            applicationDetailsPref = new ApplicationDetailsPref ();
        return applicationDetailsPref;
    }
    
    private SharedPreferences getPref (Context context) {
        return context.getSharedPreferences (APPLICATION_DETAILS, Context.MODE_PRIVATE);
    }
    
    public String getStringPref (Context context, String key) {
        return getPref (context).getString (key, "");
    }
    
    public int getIntPref (Context context, String key) {
        return getPref (context).getInt (key, 0);
    }
    
    public boolean getBooleanPref (Context context, String key) {
        return getPref (context).getBoolean (key, false);
    }
    
    public void putBooleanPref (Context context, String key, boolean value) {
        SharedPreferences.Editor editor = getPref (context).edit ();
        editor.putBoolean (key, value);
        editor.apply ();
    }
    
    public void putStringPref (Context context, String key, String value) {
        SharedPreferences.Editor editor = getPref (context).edit ();
        editor.putString (key, value);
        editor.apply ();
    }
    
    public void putIntPref (Context context, String key, int value) {
        SharedPreferences.Editor editor = getPref (context).edit ();
        editor.putInt (key, value);
        editor.apply ();
    }
}
