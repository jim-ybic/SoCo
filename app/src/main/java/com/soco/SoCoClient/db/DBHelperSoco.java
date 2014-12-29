package com.soco.SoCoClient.db;

/**
 * Created by jenny on 28/12/14.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelperSoco extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "soco3.db";
    private static final int DATABASE_VERSION = 1;

    public DBHelperSoco(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS program (" +
                "pid INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "pname VARCHAR, " +
                "pdate VARCHAR," +
                "ptime VARCHAR," +
                "pplace VARCHAR," +
                "pcomplete INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("ALTER TABLE program ADD COLUMN other STRING");
    }
}

