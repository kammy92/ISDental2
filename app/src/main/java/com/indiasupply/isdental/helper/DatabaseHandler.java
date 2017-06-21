package com.indiasupply.isdental.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.indiasupply.isdental.model.Banner;
import com.indiasupply.isdental.utils.AppConfigTags;
import com.indiasupply.isdental.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class DatabaseHandler extends SQLiteOpenHelper {
    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "isdental";
    
    // Table Names
    private static final String TABLE_BANNERS = "tbl_banners";
    
    // Banners Table - column names
    private static final String BNNR_ID = "bnnr_id";
    private static final String BNNR_TITLE = "bnnr_title";
    private static final String BNNR_IMAGE = "bnnr_image";
    private static final String BNNR_URL = "bnnr_url";
    private static final String BNNR_TYPE = "bnnr_type";
    
    private static final String BNNR_TYPE_HOME = "HOME";
    private static final String BNNR_TYPE_BRANDS = "BRANDS";
    private static final String BNNR_TYPE_EVENTS = "EVENTS";
    
    
    // Notes table Create Statements
    private static final String CREATE_TABLE_BANNERS = "CREATE TABLE "
            + TABLE_BANNERS + "(" +
            BNNR_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            BNNR_TITLE + " TEXT," +
            BNNR_IMAGE + " TEXT," +
            BNNR_URL + " TEXT," +
            BNNR_TYPE + " TEXT" + ")";
    
    Context mContext;
    private boolean LOG_FLAG = false;
    
    public DatabaseHandler (Context context) {
        super (context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
    }
    
    @Override
    public void onCreate (SQLiteDatabase db) {
        db.execSQL (CREATE_TABLE_BANNERS);
    }
    
    @Override
    public void onUpgrade (SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL ("DROP TABLE IF EXISTS " + TABLE_BANNERS);
        onCreate (db);
    }
    
    //BANNERS
    
    public long createBanner (Banner banner) {
        SQLiteDatabase db = this.getWritableDatabase ();
        Utils.showLog (Log.DEBUG, AppConfigTags.DATABASE_LOG, "Creating Banner", LOG_FLAG);
        ContentValues values = new ContentValues ();
        values.put (BNNR_TITLE, banner.getTitle ());
        values.put (BNNR_IMAGE, banner.getImage ());
        values.put (BNNR_URL, banner.getUrl ());
        values.put (BNNR_TYPE, banner.getType ());
        long banner_id = db.insert (TABLE_BANNERS, null, values);
        return banner_id;
    }
    
    public void insertAllBanners (ArrayList<Banner> bannerList) {
        SQLiteDatabase db = this.getWritableDatabase ();
        Utils.showLog (Log.DEBUG, AppConfigTags.DATABASE_LOG, "Inserting all banners", LOG_FLAG);
        db.beginTransaction ();
        try {
            ContentValues values = new ContentValues ();
            for (Banner banner : bannerList) {
                values.put (BNNR_TITLE, banner.getTitle ());
                values.put (BNNR_IMAGE, banner.getImage ());
                values.put (BNNR_URL, banner.getUrl ());
                values.put (BNNR_TYPE, banner.getType ());
                db.insert (TABLE_BANNERS, null, values);
            }
            db.setTransactionSuccessful ();
        } finally {
            db.endTransaction ();
        }
    }
    
    public ArrayList<Banner> getAllHomeBanners () {
        ArrayList<Banner> bannerList = new ArrayList<Banner> ();
        SQLiteDatabase db = this.getReadableDatabase ();
        String selectQuery = "SELECT  * FROM " + TABLE_BANNERS + " WHERE " + BNNR_TYPE + " = '" + BNNR_TYPE_HOME + "'";
        Utils.showLog (Log.DEBUG, AppConfigTags.DATABASE_LOG, "Get banner where banner type = " + BNNR_TYPE_HOME, LOG_FLAG);
        Cursor c = db.rawQuery (selectQuery, null);
        if (c.moveToFirst ()) {
            do {
                Banner banner = new Banner (
                        c.getInt ((c.getColumnIndex (BNNR_ID))),
                        c.getString ((c.getColumnIndex (BNNR_TITLE))),
                        c.getString ((c.getColumnIndex (BNNR_IMAGE))),
                        c.getString ((c.getColumnIndex (BNNR_URL))),
                        c.getString ((c.getColumnIndex (BNNR_TYPE)))
                );
                bannerList.add (banner);
            } while (c.moveToNext ());
        }
        return bannerList;
    }
    
    public ArrayList<Banner> getAllBrandsBanners () {
        ArrayList<Banner> bannerList = new ArrayList<Banner> ();
        SQLiteDatabase db = this.getReadableDatabase ();
        String selectQuery = "SELECT  * FROM " + TABLE_BANNERS + " WHERE " + BNNR_TYPE + " = '" + BNNR_TYPE_BRANDS + "'";
        Utils.showLog (Log.DEBUG, AppConfigTags.DATABASE_LOG, "Get banner where banner type = " + BNNR_TYPE_BRANDS, LOG_FLAG);
        Cursor c = db.rawQuery (selectQuery, null);
        if (c.moveToFirst ()) {
            do {
                Banner banner = new Banner (
                        c.getInt ((c.getColumnIndex (BNNR_ID))),
                        c.getString ((c.getColumnIndex (BNNR_TITLE))),
                        c.getString ((c.getColumnIndex (BNNR_IMAGE))),
                        c.getString ((c.getColumnIndex (BNNR_URL))),
                        c.getString ((c.getColumnIndex (BNNR_TYPE)))
                );
                bannerList.add (banner);
            } while (c.moveToNext ());
        }
        return bannerList;
    }
    
    public ArrayList<Banner> getAllEventsBanners () {
        ArrayList<Banner> bannerList = new ArrayList<Banner> ();
        SQLiteDatabase db = this.getReadableDatabase ();
        String selectQuery = "SELECT  * FROM " + TABLE_BANNERS + " WHERE " + BNNR_TYPE + " = '" + BNNR_TYPE_EVENTS + "'";
        Utils.showLog (Log.DEBUG, AppConfigTags.DATABASE_LOG, "Get banner where banner type = " + BNNR_TYPE_EVENTS, LOG_FLAG);
        Cursor c = db.rawQuery (selectQuery, null);
        if (c.moveToFirst ()) {
            do {
                Banner banner = new Banner (
                        c.getInt ((c.getColumnIndex (BNNR_ID))),
                        c.getString ((c.getColumnIndex (BNNR_TITLE))),
                        c.getString ((c.getColumnIndex (BNNR_IMAGE))),
                        c.getString ((c.getColumnIndex (BNNR_URL))),
                        c.getString ((c.getColumnIndex (BNNR_TYPE)))
                );
                bannerList.add (banner);
            } while (c.moveToNext ());
        }
        return bannerList;
    }
    
    public void deleteAllBanners () {
        SQLiteDatabase db = this.getWritableDatabase ();
        Utils.showLog (Log.DEBUG, AppConfigTags.DATABASE_LOG, "Delete all banners", LOG_FLAG);
        db.execSQL ("delete from " + TABLE_BANNERS);
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
}