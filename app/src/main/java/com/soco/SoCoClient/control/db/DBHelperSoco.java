package com.soco.SoCoClient.control.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.soco.SoCoClient.control.config.DatabaseConfig;

public class DBHelperSoco extends SQLiteOpenHelper {

    public static String DATABASE_NAME = "soco.0.1.1.db";
    public static int DATABASE_VERSION = 1;

    public DBHelperSoco(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        //todo: decommission
        db.execSQL("CREATE TABLE IF NOT EXISTS " + DatabaseConfig.TABLE_PROGRAM + " (" +
                DatabaseConfig.COLUMN_PID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DatabaseConfig.COLUMN_PNAME + " VARCHAR, " +
                DatabaseConfig.COLUMN_PDATE + " VARCHAR," +
                DatabaseConfig.COLUMN_PTIME + " VARCHAR," +
                DatabaseConfig.COLUMN_PPLACE + " VARCHAR," +
                DatabaseConfig.COLUMN_PCOMPLETE + " INTEGER," +
                DatabaseConfig.COLUMN_PDESC + " VARCHAR," +
                DatabaseConfig.COLUMN_PPHONE + " VARCHAR, " +
                DatabaseConfig.COLUMN_PEMAIL + " VARCHAR, " +
                DatabaseConfig.COLUMN_PWECHAT + " VARCHAR)");

        //update: 20150206
        db.execSQL("CREATE TABLE IF NOT EXISTS " + DatabaseConfig.TABLE_PROJECT + " (" +
                DatabaseConfig.COLUMN_PROJECT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DatabaseConfig.COLUMN_PROJECT_NAME + " VARCHAR, " +
                DatabaseConfig.COLUMN_PROJECT_TAG + " VARCHAR," +
                DatabaseConfig.COLUMN_PROJECT_CREATE_TIMESTAMP + " VARCHAR," +
                DatabaseConfig.COLUMN_PROJECT_UPDATE_TIMESTAMP + " VARCHAR," +
                DatabaseConfig.COLUMN_PROJECT_SIGNATURE + " VARCHAR, " +
                DatabaseConfig.COLUMN_PROJECT_ACTIVE + " VARCHAR)");

        db.execSQL("CREATE TABLE IF NOT EXISTS " + DatabaseConfig.TABLE_ATTRIBUTE + " (" +
                DatabaseConfig.COLUMN_ATTRIBUTE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DatabaseConfig.COLUMN_ATTRIBUTE_PID + " INTEGER, " +
                DatabaseConfig.COLUMN_ATTRIBUTE_NAME + " VARCHAR, " +
                DatabaseConfig.COLUMN_ATTRIBUTE_VALUE + " VARCHAR, " +
                DatabaseConfig.COLUMN_ATTRIBUTE_USER + " VARCHAR, " +
                DatabaseConfig.COLUMN_ATTRIBUTE_CREATE_TIMESTAMP + " VARCHAR, " +
                DatabaseConfig.COLUMN_ATTRIBUTE_UPDATE_TIMESTAMP + " VARCHAR)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("ALTER TABLE " + DatabaseConfig.TABLE_PROGRAM + " ADD COLUMN other STRING");
    }
}

