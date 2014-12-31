package com.soco.SoCoClient.datamodel;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.soco.SoCoClient.Config;

public class DBHelperSoco extends SQLiteOpenHelper {

    public static String DATABASE_NAME = "soco.0.1.db";
    public static int DATABASE_VERSION = 1;

    public DBHelperSoco(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + Config.TABLE_PROGRAM + " (" +
                Config.COLUMN_PID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Config.COLUMN_PNAME + " VARCHAR, " +
                Config.COLUMN_PDATE + " VARCHAR," +
                Config.COLUMN_PTIME + " VARCHAR," +
                Config.COLUMN_PPLACE + " VARCHAR," +
                Config.COLUMN_PCOMPLETE + " INTEGER," +
                Config.COLUMN_PDESC + " VARCHAR," +
                Config.COLUMN_PPHONE + " VARCHAR, " +
                Config.COLUMN_PEMAIL + " VARCHAR, " +
                Config.COLUMN_PWECHAT + " VARCHAR)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("ALTER TABLE " + Config.TABLE_PROGRAM + " ADD COLUMN other STRING");
    }
}

