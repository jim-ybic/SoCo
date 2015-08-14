package com.soco.SoCoClient.obsolete.v1.control.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.soco.SoCoClient.obsolete.v1.control.config.DataConfigObs;

public class DBHelperSocoObs extends SQLiteOpenHelper {

    public static String DATABASE_NAME = "soco-db.0.1.25";
    public static int DATABASE_VERSION = 1;

    public DBHelperSocoObs(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE IF NOT EXISTS " + DataConfigObs.TABLE_ACTIVITY + " (" +
                DataConfigObs.COLUMN_ACTIVITY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DataConfigObs.COLUMN_ACTIVITY_NAME + " VARCHAR, " +
                DataConfigObs.COLUMN_ACTIVITY_TAG + " VARCHAR," +
                DataConfigObs.COLUMN_ACTIVITY_CREATE_TIMESTAMP + " VARCHAR," +
                DataConfigObs.COLUMN_ACTIVITY_UPDATE_TIMESTAMP + " VARCHAR," +
                DataConfigObs.COLUMN_ACTIVITY_SIGNATURE + " VARCHAR, " +
                DataConfigObs.COLUMN_ACTIVITY_ACTIVE + " VARCHAR, " +
                DataConfigObs.COLUMN_ACTIVITY_ID_ONSERVER +" VARCHAR," +
                DataConfigObs.COLUMN_ACTIVITY_INVITATION_STATUS +" INTEGER," +
                DataConfigObs.COLUMN_ACTIVITY_PATH +" VARCHAR" +
                ")");

        db.execSQL("CREATE TABLE IF NOT EXISTS " + DataConfigObs.TABLE_ATTRIBUTE + " (" +
                DataConfigObs.COLUMN_ATTRIBUTE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DataConfigObs.COLUMN_ATTRIBUTE_PID + " INTEGER, " +
                DataConfigObs.COLUMN_ATTRIBUTE_NAME + " VARCHAR, " +
                DataConfigObs.COLUMN_ATTRIBUTE_VALUE + " VARCHAR, " +
                DataConfigObs.COLUMN_ATTRIBUTE_USER + " VARCHAR, " +
                DataConfigObs.COLUMN_ATTRIBUTE_CREATE_TIMESTAMP + " VARCHAR, " +
                DataConfigObs.COLUMN_ATTRIBUTE_UPDATE_TIMESTAMP + " VARCHAR)");

        db.execSQL("CREATE TABLE IF NOT EXISTS " + DataConfigObs.TABLE_SHARED_FILE + " (" +
                DataConfigObs.COLUMN_SHARED_FILE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DataConfigObs.COLUMN_SHARED_FILE_PID + " INTEGER, " +
                DataConfigObs.COLUMN_SHARED_FILE_DISPLAY_NAME + " VARCHAR, " +
                DataConfigObs.COLUMN_SHARED_FILE_URI + " VARCHAR, " +
                DataConfigObs.COLUMN_SHARED_FILE_REMOTE_PATH + " VARCHAR, " +
                DataConfigObs.COLUMN_SHARED_FILE_LOCAL_PATH + " VARCHAR, " +
                DataConfigObs.COLUMN_SHARED_FILE_USER + " VARCHAR, " +
                DataConfigObs.COLUMN_SHARED_FILE_CREATE_TIMESTAMP + " VARCHAR, " +
                DataConfigObs.COLUMN_SHARED_FILE_UPDATE_TIMESTAMP + " VARCHAR)");

        db.execSQL("CREATE TABLE IF NOT EXISTS " + DataConfigObs.TABLE_ACTIVITY_MEMBER + " (" +
                DataConfigObs.COLUMN_ACTIVITY_MEMBER_AID + " VARCHAR, " +
                DataConfigObs.COLUMN_ACTIVITY_MEMBER_MEMBER_EMAIL + " VARCHAR, " +
                DataConfigObs.COLUMN_ACTIVITY_MEMBER_MEMBER_USERNAME + " VARCHAR, " +
                DataConfigObs.COLUMN_ACTIVITY_MEMBER_MEMBER_NICKNAME + " VARCHAR, " +
                DataConfigObs.COLUMN_ACTIVITY_MEMBER_MEMBER_STATUS + " VARCHAR, " +
                DataConfigObs.COLUMN_ACTIVITY_MEMBER_MEMBER_JOIN_TIMESTAMP + " VARCHAR)");

        db.execSQL("CREATE TABLE IF NOT EXISTS " + DataConfigObs.TABLE_ACTIVITY_UPDATES + " (" +
                DataConfigObs.COLUMN_ACTIVITY_UPDATES_UID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DataConfigObs.COLUMN_ACTIVITY_UPDATES_AID + " VARCHAR, " +
                DataConfigObs.COLUMN_ACTIVITY_UPDATES_COMMENT + " VARCHAR, " +
                DataConfigObs.COLUMN_ACTIVITY_UPDATES_USER + " VARCHAR, " +
                DataConfigObs.COLUMN_ACTIVITY_UPDATES_TIMESTAMP + " VARCHAR)");

        db.execSQL("CREATE TABLE IF NOT EXISTS " + DataConfigObs.TABLE_CONTACT + " (" +
                DataConfigObs.COLUMN_CONTACT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DataConfigObs.COLUMN_CONTACT_EMAIL + " VARCHAR, " +
                DataConfigObs.COLUMN_CONTACT_USERNAME + " VARCHAR, " +
                DataConfigObs.COLUMN_CONTACT_NICKNAME + " VARCHAR, " +
                DataConfigObs.COLUMN_CONTACT_PHONE + " VARCHAR, " +
                DataConfigObs.COLUMN_CONTACT_ID_ONSERVER + " INTEGER" +
        ")");

        db.execSQL("CREATE TABLE IF NOT EXISTS " + DataConfigObs.TABLE_CHAT + " (" +
                DataConfigObs.COLUMN_CHAT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DataConfigObs.COLUMN_CHAT_CONTACT_ID + " INTEGER, " +
                DataConfigObs.COLUMN_CHAT_CONTENT + " VARCHAR, " +
                DataConfigObs.COLUMN_CHAT_TIMESTAMP + " VARCHAR, " +
                DataConfigObs.COLUMN_CHAT_TYPE + " VARCHAR)");

        db.execSQL("CREATE TABLE IF NOT EXISTS " + DataConfigObs.TABLE_FOLDER + " (" +
                DataConfigObs.COLUMN_FOLDER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DataConfigObs.COLUMN_FOLDER_NAME + " VARCHAR, " +
                DataConfigObs.COLUMN_FOLDER_DESC + " VARCHAR, " +
                DataConfigObs.COLUMN_FOLDER_PATH + " VARCHAR, " +
                DataConfigObs.COLUMN_FOLDER_TAG + " VARCHAR" +
        ")");


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        db.execSQL("ALTER TABLE " + DataConfigObs.TABLE_PROGRAM + " ADD COLUMN other STRING");
    }
}

