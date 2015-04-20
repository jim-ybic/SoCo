package com.soco.SoCoClient.control.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.soco.SoCoClient.control.config.DataConfig;

public class DBHelperSoco extends SQLiteOpenHelper {

    public static String DATABASE_NAME = "soco.0.1.13.db";
    public static int DATABASE_VERSION = 1;

    public DBHelperSoco(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE IF NOT EXISTS " + DataConfig.TABLE_ACTIVITY + " (" +
                DataConfig.COLUMN_ACTIVITY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DataConfig.COLUMN_ACTIVITY_NAME + " VARCHAR, " +
                DataConfig.COLUMN_ACTIVITY_TAG + " VARCHAR," +
                DataConfig.COLUMN_ACTIVITY_CREATE_TIMESTAMP + " VARCHAR," +
                DataConfig.COLUMN_ACTIVITY_UPDATE_TIMESTAMP + " VARCHAR," +
                DataConfig.COLUMN_ACTIVITY_SIGNATURE + " VARCHAR, " +
                DataConfig.COLUMN_ACTIVITY_ACTIVE + " VARCHAR, " +
                DataConfig.COLUMN_ACTIVITY_ID_ONSERVER +" VARCHAR," +
                DataConfig.COLUMN_ACTIVITY_INVITATION_STATUS +" INTEGER)");

        db.execSQL("CREATE TABLE IF NOT EXISTS " + DataConfig.TABLE_ATTRIBUTE + " (" +
                DataConfig.COLUMN_ATTRIBUTE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DataConfig.COLUMN_ATTRIBUTE_PID + " INTEGER, " +
                DataConfig.COLUMN_ATTRIBUTE_NAME + " VARCHAR, " +
                DataConfig.COLUMN_ATTRIBUTE_VALUE + " VARCHAR, " +
                DataConfig.COLUMN_ATTRIBUTE_USER + " VARCHAR, " +
                DataConfig.COLUMN_ATTRIBUTE_CREATE_TIMESTAMP + " VARCHAR, " +
                DataConfig.COLUMN_ATTRIBUTE_UPDATE_TIMESTAMP + " VARCHAR)");

        db.execSQL("CREATE TABLE IF NOT EXISTS " + DataConfig.TABLE_SHARED_FILE + " (" +
                DataConfig.COLUMN_SHARED_FILE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DataConfig.COLUMN_SHARED_FILE_PID + " INTEGER, " +
                DataConfig.COLUMN_SHARED_FILE_DISPLAY_NAME + " VARCHAR, " +
                DataConfig.COLUMN_SHARED_FILE_URI + " VARCHAR, " +
                DataConfig.COLUMN_SHARED_FILE_REMOTE_PATH + " VARCHAR, " +
                DataConfig.COLUMN_SHARED_FILE_LOCAL_PATH + " VARCHAR, " +
                DataConfig.COLUMN_SHARED_FILE_USER + " VARCHAR, " +
                DataConfig.COLUMN_SHARED_FILE_CREATE_TIMESTAMP + " VARCHAR, " +
                DataConfig.COLUMN_SHARED_FILE_UPDATE_TIMESTAMP + " VARCHAR)");

        db.execSQL("CREATE TABLE IF NOT EXISTS " + DataConfig.TABLE_ACTIVITY_MEMBER + " (" +
                DataConfig.COLUMN_ACTIVITY_MEMBER_AID + " VARCHAR, " +
                DataConfig.COLUMN_ACTIVITY_MEMBER_MEMBER_EMAIL + " VARCHAR, " +
                DataConfig.COLUMN_ACTIVITY_MEMBER_MEMBER_USERNAME + " VARCHAR, " +
                DataConfig.COLUMN_ACTIVITY_MEMBER_MEMBER_STATUS + " VARCHAR, " +
                DataConfig.COLUMN_ACTIVITY_MEMBER_MEMBER_JOIN_TIMESTAMP + " VARCHAR)");

        db.execSQL("CREATE TABLE IF NOT EXISTS " + DataConfig.TABLE_ACTIVITY_UPDATES + " (" +
                DataConfig.COLUMN_ACTIVITY_UPDATES_UID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DataConfig.COLUMN_ACTIVITY_UPDATES_AID + " VARCHAR, " +
                DataConfig.COLUMN_ACTIVITY_UPDATES_COMMENT + " VARCHAR, " +
                DataConfig.COLUMN_ACTIVITY_UPDATES_USER + " VARCHAR, " +
                DataConfig.COLUMN_ACTIVITY_UPDATES_TIMESTAMP + " VARCHAR)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        db.execSQL("ALTER TABLE " + DataConfig.TABLE_PROGRAM + " ADD COLUMN other STRING");
    }
}

