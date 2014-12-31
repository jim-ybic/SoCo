package com.soco.SoCoClient.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.soco.SoCoClient.Config;

public class DBHelperSoco extends SQLiteOpenHelper {


    public DBHelperSoco(Context context) {
        super(context, Config.DATABASE_NAME, null, Config.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + Config.TABLE_PROGRAM + " (" +
                Config.TABLE_COLUMN_PID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Config.TABLE_COLUMN_PNAME + " VARCHAR, " +
                Config.TABLE_COLUMN_PDATE + " VARCHAR," +
                Config.TABLE_COLUMN_PTIME + " VARCHAR," +
                Config.TABLE_COLUMN_PPLACE + " VARCHAR," +
                Config.TABLE_COLUMN_PCOMPLETE + " INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("ALTER TABLE " + Config.TABLE_PROGRAM + " ADD COLUMN other STRING");
    }
}

