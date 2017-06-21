package com.indiasupply.isdental.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by actiknow on 6/19/17.
 */

public class EventDetailsPref {
    public static String EVENT_ID = "event_id";
    public static String EVENT_NAME = "event_name";
    public static String EVENT_START_DATE = "event_start_date";
    public static String EVENT_END_DATE = "event_end_date";
    public static String EVENT_FAQ  = "event_faq";
    public static String EVENT_FEES = "event_fees";
    public static String EVENT_SCHEDULE = "event_schedule";
    public static String EVENT_VENUE = "event_venue";
    public static String EVENT_CITY  = "event_city";
    public static String EVENT_PINCODE = "event_pincode";
    public static String EVENT_LATITUDE = "event_latitude";
    public static String EVENT_LONGITUDE = "event_longitude";
    public static String EVENT_INCLUSIONS = "event_inclusions";
    public static String EVENT_CONTACT_DETAILS = "event_contact_details";
    private static EventDetailsPref eventDetailsPref;
    private String EVENT_DETAILS = "EVENT_DETAILS";



    public static EventDetailsPref getInstance () {
        if (eventDetailsPref == null)
            eventDetailsPref = new EventDetailsPref ();
        return eventDetailsPref;
    }

    private SharedPreferences getPref (Context context) {
        return context.getSharedPreferences (EVENT_DETAILS, Context.MODE_PRIVATE);
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
