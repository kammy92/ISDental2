package com.indiasupply.isdental.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.indiasupply.isdental.utils.AppConfigTags;
import com.indiasupply.isdental.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class DatabaseHandler extends SQLiteOpenHelper {
    // Database Version
    private static final int DATABASE_VERSION = 6;
    // Database Name
    private static final String DATABASE_NAME = "isdental";
    
    // Table Names
    private static final String TABLE_BANNERS = "tbl_banners";
    
    private static final String TABLE_EVENTS = "tbl_events";
    private static final String TABLE_COMPANIES = "tbl_companies";
    
    private static final String TABLE_FILTER = "tbl_filter";
    
    
    // Banners Table - column names
    private static final String BNNR_ID = "bnnr_id";
    private static final String BNNR_TITLE = "bnnr_title";
    private static final String BNNR_IMAGE = "bnnr_image";
    private static final String BNNR_URL = "bnnr_url";
    private static final String BNNR_TYPE = "bnnr_type";
    
    private static final String BNNR_TYPE_HOME = "HOME";
    private static final String BNNR_TYPE_BRANDS = "BRANDS";
    private static final String BNNR_TYPE_EVENTS = "EVENTS";
    
    
    private static final String EVNT_ID = "evnt_id";
    private static final String EVNT_DETAILS = "evnt_details";
    private static final String EVNT_FLOOR_PLAN = "evnt_floor_plan";
    
    private static final String CMPNY_ID = "cmpny_id";
    private static final String CMPNY_DETAILS = "cmpny_details";
    
    private static final String FILTER_CATEGORY = "filter_category";
    
    // Notes table Create Statements
    private static final String CREATE_TABLE_BANNERS = "CREATE TABLE "
            + TABLE_BANNERS + "(" +
            BNNR_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            BNNR_TITLE + " TEXT," +
            BNNR_IMAGE + " TEXT," +
            BNNR_URL + " TEXT," +
            BNNR_TYPE + " TEXT" + ")";
    
    
    // Notes table Create Statements
    private static final String CREATE_TABLE_EVENTS = "CREATE TABLE "
            + TABLE_EVENTS + "(" +
            EVNT_ID + " INTEGER," +
            EVNT_DETAILS + " TEXT," +
            EVNT_FLOOR_PLAN + " TEXT" + ")";
    
    // Notes table Create Statements
    private static final String CREATE_TABLE_COMPANIES = "CREATE TABLE "
            + TABLE_COMPANIES + "(" +
            CMPNY_ID + " INTEGER," +
            CMPNY_DETAILS + " TEXT" + ")";
    
    // Notes table Create Statements
    private static final String CREATE_TABLE_FILTERS = "CREATE TABLE "
            + TABLE_FILTER + "(" +
            FILTER_CATEGORY + " TEXT" + ")";
    
    Context mContext;
    private boolean LOG_FLAG = false;
    
    public DatabaseHandler (Context context) {
        super (context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
    }
    
    @Override
    public void onCreate (SQLiteDatabase db) {
        db.execSQL (CREATE_TABLE_BANNERS);
        db.execSQL (CREATE_TABLE_COMPANIES);
        db.execSQL (CREATE_TABLE_EVENTS);
        db.execSQL (CREATE_TABLE_FILTERS);
    }
    
    @Override
    public void onUpgrade (SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL ("DROP TABLE IF EXISTS " + TABLE_BANNERS);
        db.execSQL ("DROP TABLE IF EXISTS " + TABLE_EVENTS);
        db.execSQL ("DROP TABLE IF EXISTS " + TABLE_COMPANIES);
        db.execSQL ("DROP TABLE IF EXISTS " + TABLE_FILTER);
        onCreate (db);
    }
    
    
    public void closeDB () {
        SQLiteDatabase db = this.getReadableDatabase ();
        if (db != null && db.isOpen ())
            db.close ();
    }
    
    private String getDateTime () {
        SimpleDateFormat dateFormat = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss", Locale.getDefault ());
        Date date = new Date ();
        return dateFormat.format (date);
    }
    
    
    public long insertEvent (int event_id, String event_details) {
        SQLiteDatabase db = this.getWritableDatabase ();
        Utils.showLog (Log.DEBUG, AppConfigTags.DATABASE_LOG, "Creating Event", LOG_FLAG);
        ContentValues values = new ContentValues ();
        values.put (EVNT_ID, event_id);
        values.put (EVNT_DETAILS, event_details);
        values.put (EVNT_FLOOR_PLAN, "");
        return db.insert (TABLE_EVENTS, null, values);
    }
    
    public boolean isEventExist (int event_id) {
        String countQuery = "SELECT * FROM " + TABLE_EVENTS + " WHERE " + EVNT_ID + " = " + event_id;
        SQLiteDatabase db = this.getReadableDatabase ();
        Cursor cursor = db.rawQuery (countQuery, null);
        int count = cursor.getCount ();
        cursor.close ();
        if (count > 0) {
            return true;
        } else {
            return false;
        }
    }
    
    public int updateEventDetails (int event_id, String details) {
        SQLiteDatabase db = this.getWritableDatabase ();
        Utils.showLog (Log.DEBUG, AppConfigTags.DATABASE_LOG, "Update event details in event id = " + event_id, LOG_FLAG);
        ContentValues values = new ContentValues ();
        values.put (EVNT_DETAILS, details);
        return db.update (TABLE_EVENTS, values, EVNT_ID + " = ?", new String[] {String.valueOf (event_id)});
    }
    
    public String getEventDetails (int event_id) {
        SQLiteDatabase db = this.getReadableDatabase ();
        String selectQuery = "SELECT * FROM " + TABLE_EVENTS + " WHERE " + EVNT_ID + " = " + event_id;
        Utils.showLog (Log.DEBUG, AppConfigTags.DATABASE_LOG, "Get event details where event ID = " + event_id, LOG_FLAG);
        Cursor c = db.rawQuery (selectQuery, null);
        if (c != null)
            c.moveToFirst ();
        return c.getString (c.getColumnIndex (EVNT_DETAILS));
    }
    
    public int updateEventFloorPlan (int event_id, String floor_plan) {
        SQLiteDatabase db = this.getWritableDatabase ();
        Utils.showLog (Log.DEBUG, AppConfigTags.DATABASE_LOG, "Update event floor plan in event id = " + event_id, LOG_FLAG);
        ContentValues values = new ContentValues ();
        values.put (EVNT_FLOOR_PLAN, floor_plan);
        return db.update (TABLE_EVENTS, values, EVNT_ID + " = ?", new String[] {String.valueOf (event_id)});
    }
    
    public String getEventFloorPlan (int event_id) {
        SQLiteDatabase db = this.getReadableDatabase ();
        String selectQuery = "SELECT * FROM " + TABLE_EVENTS + " WHERE " + EVNT_ID + " = " + event_id;
        Utils.showLog (Log.DEBUG, AppConfigTags.DATABASE_LOG, "Get event floor plan where event ID = " + event_id, LOG_FLAG);
        Cursor c = db.rawQuery (selectQuery, null);
        if (c != null)
            c.moveToFirst ();
        return c.getString (c.getColumnIndex (EVNT_FLOOR_PLAN));
    }
    
    public void deleteEvent (int event_id) {
        SQLiteDatabase db = this.getWritableDatabase ();
        Utils.showLog (Log.DEBUG, AppConfigTags.DATABASE_LOG, "Delete event where id = " + event_id, LOG_FLAG);
        db.execSQL ("DELETE FROM " + TABLE_EVENTS + " WHERE " + EVNT_ID + " = " + event_id);
    }
    
    
    public long insertCompany (int company_id, String company_details) {
        SQLiteDatabase db = this.getWritableDatabase ();
        Utils.showLog (Log.DEBUG, AppConfigTags.DATABASE_LOG, "Creating Company", LOG_FLAG);
        ContentValues values = new ContentValues ();
        values.put (CMPNY_ID, company_id);
        values.put (CMPNY_DETAILS, company_details);
        return db.insert (TABLE_COMPANIES, null, values);
    }
    
    public boolean isCompanyExist (int company_id) {
        String countQuery = "SELECT * FROM " + TABLE_COMPANIES + " WHERE " + CMPNY_ID + " = " + company_id;
        SQLiteDatabase db = this.getReadableDatabase ();
        Cursor cursor = db.rawQuery (countQuery, null);
        int count = cursor.getCount ();
        cursor.close ();
        if (count > 0) {
            return true;
        } else {
            return false;
        }
    }
    
    public int updateCompanyDetails (int company_id, String details) {
        SQLiteDatabase db = this.getWritableDatabase ();
        Utils.showLog (Log.DEBUG, AppConfigTags.DATABASE_LOG, "Update company details in company id = " + company_id, LOG_FLAG);
        ContentValues values = new ContentValues ();
        values.put (CMPNY_DETAILS, details);
        return db.update (TABLE_COMPANIES, values, CMPNY_ID + " = ?", new String[] {String.valueOf (company_id)});
    }
    
    public String getCompanyDetails (int company_id) {
        SQLiteDatabase db = this.getReadableDatabase ();
        String selectQuery = "SELECT * FROM " + TABLE_COMPANIES + " WHERE " + CMPNY_ID + " = " + company_id;
        Utils.showLog (Log.DEBUG, AppConfigTags.DATABASE_LOG, "Get company details where company ID = " + company_id, LOG_FLAG);
        Cursor c = db.rawQuery (selectQuery, null);
        if (c != null)
            c.moveToFirst ();
        return c.getString (c.getColumnIndex (CMPNY_DETAILS));
    }
    
    public void deleteCompany (int company_id) {
        SQLiteDatabase db = this.getWritableDatabase ();
        Utils.showLog (Log.DEBUG, AppConfigTags.DATABASE_LOG, "Delete company where id = " + company_id, LOG_FLAG);
        db.execSQL ("DELETE FROM " + TABLE_COMPANIES + " WHERE " + CMPNY_ID + " = " + company_id);
    }
    
    
    public long insertFilter (String category) {
        SQLiteDatabase db = this.getWritableDatabase ();
        Utils.showLog (Log.DEBUG, AppConfigTags.DATABASE_LOG, "Creating filter", LOG_FLAG);
        ContentValues values = new ContentValues ();
        values.put (FILTER_CATEGORY, category);
        return db.insert (TABLE_FILTER, null, values);
    }
    
    public boolean isFilterExist (String category) {
        String countQuery = "SELECT * FROM " + TABLE_FILTER + " WHERE " + FILTER_CATEGORY + " = '" + category + "'";
        SQLiteDatabase db = this.getReadableDatabase ();
        Cursor cursor = db.rawQuery (countQuery, null);
        int count = cursor.getCount ();
        cursor.close ();
        if (count > 0) {
            return true;
        } else {
            return false;
        }
    }
    
    public ArrayList<String> getAllFilters () {
        ArrayList<String> filterList = new ArrayList<String> ();
        SQLiteDatabase db = this.getReadableDatabase ();
        String selectQuery = "SELECT  * FROM " + TABLE_FILTER;
        Utils.showLog (Log.DEBUG, AppConfigTags.DATABASE_LOG, "Get all filters", LOG_FLAG);
        Cursor c = db.rawQuery (selectQuery, null);
        if (c.moveToFirst ()) {
            do {
                filterList.add (c.getString ((c.getColumnIndex (FILTER_CATEGORY))));
            } while (c.moveToNext ());
        }
        return filterList;
    }
    
    public void deleteFilter (String category) {
        SQLiteDatabase db = this.getWritableDatabase ();
        Utils.showLog (Log.DEBUG, AppConfigTags.DATABASE_LOG, "Delete filter where filter = " + category, LOG_FLAG);
        db.execSQL ("DELETE FROM " + TABLE_FILTER + " WHERE " + FILTER_CATEGORY + " = '" + category + "'");
    }
    
    public void deleteAllFilters () {
        SQLiteDatabase db = this.getWritableDatabase ();
        Utils.showLog (Log.DEBUG, AppConfigTags.DATABASE_LOG, "Delete all filters", LOG_FLAG);
        db.execSQL ("delete from " + TABLE_FILTER);
    }
    
}