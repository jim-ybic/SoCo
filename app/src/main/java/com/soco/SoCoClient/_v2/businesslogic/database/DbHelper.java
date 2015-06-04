package com.soco.SoCoClient._v2.businesslogic.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.soco.SoCoClient._v2.businesslogic.config.DbConfig;
import com.soco.SoCoClient.control.config.DataConfig;

public class DbHelper extends SQLiteOpenHelper {

    public static String DATABASE_NAME = "socodb.v2.1";
    public static int DATABASE_VERSION = 1;

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + DbConfig.TABLE_TASK + " ("
                + DbConfig.COLUMN_TASK_TASKIDLOCAL + " INTEGER PRIMARY KEY AUTOINCREMENT"
                + ", " + DbConfig.COLUMN_TASK_TASKIDSERVER + " INTEGER"
                + ", " + DbConfig.COLUMN_TASK_TASKNAME + " VARCHAR"
                + ", " + DbConfig.COLUMN_TASK_TASKPATH + " VARCHAR"
                + ", " + DbConfig.COLUMN_TASK_ISTASKACTIVE + " INTEGER"
                + ")");

        db.execSQL("CREATE TABLE IF NOT EXISTS " + DbConfig.TABLE_ATTRIBUTE + " ("
                + DbConfig.COLUMN_ATTRIBUTE_ATTRIDLOCAL + " INTEGER PRIMARY KEY AUTOINCREMENT"
                + ", " + DbConfig.COLUMN_ATTRIBUTE_ATTRIDSERVER + " INTEGER"
                + ", " + DbConfig.COLUMN_ATTRIBUTE_ACTIVITYTYPE + " VARCHAR"
                + ", " + DbConfig.COLUMN_ATTRIBUTE_ACTIVITYIDLOCAL + " INTEGER"
                + ", " + DbConfig.COLUMN_ATTRIBUTE_ACTIVITYIDSERVER + " INTEGER"
                + ", " + DbConfig.COLUMN_ATTRIBUTE_ATTRNAME + " VARCHAR"
                + ", " + DbConfig.COLUMN_ATTRIBUTE_ATTRVALUE + " VARCHAR"
                + ")");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}


}
