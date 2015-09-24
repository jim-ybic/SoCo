package com.soco.SoCoClient.v2.control.database.ref;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.soco.SoCoClient.v2.control.config.ref.DataConfigV1;

public class DBHelperSoco extends SQLiteOpenHelper {

    public static String DATABASE_NAME = "soco-db.0.1.25";
    public static int DATABASE_VERSION = 1;

    public DBHelperSoco(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE IF NOT EXISTS " + DataConfigV1.TABLE_ACTIVITY + " (" +
                DataConfigV1.COLUMN_ACTIVITY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DataConfigV1.COLUMN_ACTIVITY_NAME + " VARCHAR, " +
                DataConfigV1.COLUMN_ACTIVITY_TAG + " VARCHAR," +
                DataConfigV1.COLUMN_ACTIVITY_CREATE_TIMESTAMP + " VARCHAR," +
                DataConfigV1.COLUMN_ACTIVITY_UPDATE_TIMESTAMP + " VARCHAR," +
                DataConfigV1.COLUMN_ACTIVITY_SIGNATURE + " VARCHAR, " +
                DataConfigV1.COLUMN_ACTIVITY_ACTIVE + " VARCHAR, " +
                DataConfigV1.COLUMN_ACTIVITY_ID_ONSERVER +" VARCHAR," +
                DataConfigV1.COLUMN_ACTIVITY_INVITATION_STATUS +" INTEGER," +
                DataConfigV1.COLUMN_ACTIVITY_PATH +" VARCHAR" +
                ")");

        db.execSQL("CREATE TABLE IF NOT EXISTS " + DataConfigV1.TABLE_ATTRIBUTE + " (" +
                DataConfigV1.COLUMN_ATTRIBUTE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DataConfigV1.COLUMN_ATTRIBUTE_PID + " INTEGER, " +
                DataConfigV1.COLUMN_ATTRIBUTE_NAME + " VARCHAR, " +
                DataConfigV1.COLUMN_ATTRIBUTE_VALUE + " VARCHAR, " +
                DataConfigV1.COLUMN_ATTRIBUTE_USER + " VARCHAR, " +
                DataConfigV1.COLUMN_ATTRIBUTE_CREATE_TIMESTAMP + " VARCHAR, " +
                DataConfigV1.COLUMN_ATTRIBUTE_UPDATE_TIMESTAMP + " VARCHAR)");

        db.execSQL("CREATE TABLE IF NOT EXISTS " + DataConfigV1.TABLE_SHARED_FILE + " (" +
                DataConfigV1.COLUMN_SHARED_FILE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DataConfigV1.COLUMN_SHARED_FILE_PID + " INTEGER, " +
                DataConfigV1.COLUMN_SHARED_FILE_DISPLAY_NAME + " VARCHAR, " +
                DataConfigV1.COLUMN_SHARED_FILE_URI + " VARCHAR, " +
                DataConfigV1.COLUMN_SHARED_FILE_REMOTE_PATH + " VARCHAR, " +
                DataConfigV1.COLUMN_SHARED_FILE_LOCAL_PATH + " VARCHAR, " +
                DataConfigV1.COLUMN_SHARED_FILE_USER + " VARCHAR, " +
                DataConfigV1.COLUMN_SHARED_FILE_CREATE_TIMESTAMP + " VARCHAR, " +
                DataConfigV1.COLUMN_SHARED_FILE_UPDATE_TIMESTAMP + " VARCHAR)");

        db.execSQL("CREATE TABLE IF NOT EXISTS " + DataConfigV1.TABLE_ACTIVITY_MEMBER + " (" +
                DataConfigV1.COLUMN_ACTIVITY_MEMBER_AID + " VARCHAR, " +
                DataConfigV1.COLUMN_ACTIVITY_MEMBER_MEMBER_EMAIL + " VARCHAR, " +
                DataConfigV1.COLUMN_ACTIVITY_MEMBER_MEMBER_USERNAME + " VARCHAR, " +
                DataConfigV1.COLUMN_ACTIVITY_MEMBER_MEMBER_NICKNAME + " VARCHAR, " +
                DataConfigV1.COLUMN_ACTIVITY_MEMBER_MEMBER_STATUS + " VARCHAR, " +
                DataConfigV1.COLUMN_ACTIVITY_MEMBER_MEMBER_JOIN_TIMESTAMP + " VARCHAR)");

        db.execSQL("CREATE TABLE IF NOT EXISTS " + DataConfigV1.TABLE_ACTIVITY_UPDATES + " (" +
                DataConfigV1.COLUMN_ACTIVITY_UPDATES_UID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DataConfigV1.COLUMN_ACTIVITY_UPDATES_AID + " VARCHAR, " +
                DataConfigV1.COLUMN_ACTIVITY_UPDATES_COMMENT + " VARCHAR, " +
                DataConfigV1.COLUMN_ACTIVITY_UPDATES_USER + " VARCHAR, " +
                DataConfigV1.COLUMN_ACTIVITY_UPDATES_TIMESTAMP + " VARCHAR)");

        db.execSQL("CREATE TABLE IF NOT EXISTS " + DataConfigV1.TABLE_CONTACT + " (" +
                DataConfigV1.COLUMN_CONTACT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DataConfigV1.COLUMN_CONTACT_EMAIL + " VARCHAR, " +
                DataConfigV1.COLUMN_CONTACT_USERNAME + " VARCHAR, " +
                DataConfigV1.COLUMN_CONTACT_NICKNAME + " VARCHAR, " +
                DataConfigV1.COLUMN_CONTACT_PHONE + " VARCHAR, " +
                DataConfigV1.COLUMN_CONTACT_ID_ONSERVER + " INTEGER" +
        ")");

        db.execSQL("CREATE TABLE IF NOT EXISTS " + DataConfigV1.TABLE_CHAT + " (" +
                DataConfigV1.COLUMN_CHAT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DataConfigV1.COLUMN_CHAT_CONTACT_ID + " INTEGER, " +
                DataConfigV1.COLUMN_CHAT_CONTENT + " VARCHAR, " +
                DataConfigV1.COLUMN_CHAT_TIMESTAMP + " VARCHAR, " +
                DataConfigV1.COLUMN_CHAT_TYPE + " VARCHAR)");

        db.execSQL("CREATE TABLE IF NOT EXISTS " + DataConfigV1.TABLE_FOLDER + " (" +
                DataConfigV1.COLUMN_FOLDER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DataConfigV1.COLUMN_FOLDER_NAME + " VARCHAR, " +
                DataConfigV1.COLUMN_FOLDER_DESC + " VARCHAR, " +
                DataConfigV1.COLUMN_FOLDER_PATH + " VARCHAR, " +
                DataConfigV1.COLUMN_FOLDER_TAG + " VARCHAR" +
        ")");


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        db.execSQL("ALTER TABLE " + DataConfigV1.TABLE_PROGRAM + " ADD COLUMN other STRING");
    }
}

