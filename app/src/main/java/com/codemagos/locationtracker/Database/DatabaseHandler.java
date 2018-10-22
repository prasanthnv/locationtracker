package com.codemagos.locationtracker.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Location;
import android.util.Log;


/**
 * Created by prasanthvenugopal on 11/04/18.
 */

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "smartech.db";


    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        //3rd argument to be passed is CursorFactory instance
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        // query to create location table
        String CREATE_LOCATION_TABLE = "CREATE TABLE location(id INTEGER PRIMARY KEY,lat TEXT, lng TEXT,time TEXT)";

        // creating tables
        db.execSQL(CREATE_LOCATION_TABLE);

    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS location");

        // Create tables again
        onCreate(db);
    }


  public long addLocation(Location location,String time){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("lat", location.getLatitude());       // Latitude
        values.put("lng", location.getLongitude());       // Longitude
        values.put("time", time);       // Longitude
        // Inserting Row
        long id =  db.insert("location", null, values);
      Log.e("DB => ","Location added");
        db.close(); // Closing database connection
        return id;
    }


    public Cursor getLocation(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "Select * from location";
        String[] params = null;
        Cursor rs = db.rawQuery(query,params);
        return rs;
    }

    public void clearLocation(){
        String query = "DELETE FROM location";
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(query);
    }

}
