package com.soco.SoCoClient.control.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.soco.SoCoClient.control.config.DataConfig;

public class DBHelperSoco extends SQLiteOpenHelper {

    public static String DATABASE_NAME = "soco.0.1.2.db";
    public static int DATABASE_VERSION = 1;

    public DBHelperSoco(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        //todo: decommission
        db.execSQL("CREATE TABLE IF NOT EXISTS " + DataConfig.TABLE_PROGRAM + " (" +
                DataConfig.COLUMN_PID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DataConfig.COLUMN_PNAME + " VARCHAR, " +
                DataConfig.COLUMN_PDATE + " VARCHAR," +
                DataConfig.COLUMN_PTIME + " VARCHAR," +
                DataConfig.COLUMN_PPLACE + " VARCHAR," +
                DataConfig.COLUMN_PCOMPLETE + " INTEGER," +
                DataConfig.COLUMN_PDESC + " VARCHAR," +
                DataConfig.COLUMN_PPHONE + " VARCHAR, " +
                DataConfig.COLUMN_PEMAIL + " VARCHAR, " +
                DataConfig.COLUMN_PWECHAT + " VARCHAR)");

        //update: 20150206
        db.execSQL("CREATE TABLE IF NOT EXISTS " + DataConfig.TABLE_PROJECT + " (" +
                DataConfig.COLUMN_PROJECT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DataConfig.COLUMN_PROJECT_NAME + " VARCHAR, " +
                DataConfig.COLUMN_PROJECT_TAG + " VARCHAR," +
                DataConfig.COLUMN_PROJECT_CREATE_TIMESTAMP + " VARCHAR," +
                DataConfig.COLUMN_PROJECT_UPDATE_TIMESTAMP + " VARCHAR," +
                DataConfig.COLUMN_PROJECT_SIGNATURE + " VARCHAR, " +
                DataConfig.COLUMN_PROJECT_ACTIVE + " VARCHAR)");

        db.execSQL("CREATE TABLE IF NOT EXISTS " + DataConfig.TABLE_ATTRIBUTE + " (" +
                DataConfig.COLUMN_ATTRIBUTE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DataConfig.COLUMN_ATTRIBUTE_PID + " INTEGER, " +
                DataConfig.COLUMN_ATTRIBUTE_NAME + " VARCHAR, " +
                DataConfig.COLUMN_ATTRIBUTE_VALUE + " VARCHAR, " +
                DataConfig.COLUMN_ATTRIBUTE_USER + " VARCHAR, " +
                DataConfig.COLUMN_ATTRIBUTE_CREATE_TIMESTAMP + " VARCHAR, " +
                DataConfig.COLUMN_ATTRIBUTE_UPDATE_TIMESTAMP + " VARCHAR)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("ALTER TABLE " + DataConfig.TABLE_PROGRAM + " ADD COLUMN other STRING");
    }
}

