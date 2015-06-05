package com.soco.SoCoClient.v2.businesslogic.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.soco.SoCoClient.v2.businesslogic.config.DataConfig2;

public class DbHelper extends SQLiteOpenHelper {

    public static String DATABASE_NAME = "socodb.v2.4";
    public static int DATABASE_VERSION = 1;

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + DataConfig2.TABLE_TASK + " ("
                + DataConfig2.COLUMN_TASK_TASKIDLOCAL + " INTEGER PRIMARY KEY AUTOINCREMENT"
                + ", " + DataConfig2.COLUMN_TASK_TASKIDSERVER + " INTEGER"
                + ", " + DataConfig2.COLUMN_TASK_TASKNAME + " VARCHAR"
                + ", " + DataConfig2.COLUMN_TASK_TASKPATH + " VARCHAR"
                + ", " + DataConfig2.COLUMN_TASK_ISTASKACTIVE + " INTEGER"
                + ")");

        db.execSQL("CREATE TABLE IF NOT EXISTS " + DataConfig2.TABLE_ATTRIBUTE + " ("
                + DataConfig2.COLUMN_ATTRIBUTE_ATTRIDLOCAL + " INTEGER PRIMARY KEY AUTOINCREMENT"
                + ", " + DataConfig2.COLUMN_ATTRIBUTE_ATTRIDSERVER + " INTEGER"
                + ", " + DataConfig2.COLUMN_ATTRIBUTE_ACTIVITYTYPE + " VARCHAR"
                + ", " + DataConfig2.COLUMN_ATTRIBUTE_ACTIVITYIDLOCAL + " INTEGER"
                + ", " + DataConfig2.COLUMN_ATTRIBUTE_ACTIVITYIDSERVER + " INTEGER"
                + ", " + DataConfig2.COLUMN_ATTRIBUTE_ATTRNAME + " VARCHAR"
                + ", " + DataConfig2.COLUMN_ATTRIBUTE_ATTRVALUE + " VARCHAR"
                + ")");

        db.execSQL("CREATE TABLE IF NOT EXISTS " + DataConfig2.TABLE_CONTACT + " ("
                + DataConfig2.COLUMN_CONTACT_CONTACTIDLOCAL + " INTEGER PRIMARY KEY AUTOINCREMENT"
                + ", " + DataConfig2.COLUMN_CONTACT_CONTACTIDSERVER + " INTEGER"
                + ", " + DataConfig2.COLUMN_CONTACT_CONTACTEMAIL + " VARCHAR"
                + ", " + DataConfig2.COLUMN_CONTACT_CONTACTUSERNAME + " VARCHAR"
                + ", " + DataConfig2.COLUMN_CONTACT_CONTACTSERVERSTATUS + " VARCHAR"
                + ")");

        db.execSQL("CREATE TABLE IF NOT EXISTS " + DataConfig2.TABLE_MESSAGE + " ("
                + DataConfig2.COLUMN_MESSAGE_MSGIDLOCAL + " INTEGER PRIMARY KEY AUTOINCREMENT"
                + ", " + DataConfig2.COLUMN_MESSAGE_MSGIDSERVER + " INTEGER"
                + ", " + DataConfig2.COLUMN_MESSAGE_FROMTYPE + " INTEGER"
                + ", " + DataConfig2.COLUMN_MESSAGE_FROMID+ " VARCHAR"
                + ", " + DataConfig2.COLUMN_MESSAGE_TOTYPE + " INTEGER"
                + ", " + DataConfig2.COLUMN_MESSAGE_TOID + " VARCHAR"
                + ", " + DataConfig2.COLUMN_MESSAGE_CREATETIMESTAMP + " VARCHAR"
                + ", " + DataConfig2.COLUMN_MESSAGE_SENDTIMESTAMP + " VARCHAR"
                + ", " + DataConfig2.COLUMN_MESSAGE_RECEIVETIMESTAMP + " VARCHAR"
                + ", " + DataConfig2.COLUMN_MESSAGE_FROMDEVICE + " VARCHAR"
                + ", " + DataConfig2.COLUMN_MESSAGE_CONTENTTYPE + " INTEGER"
                + ", " + DataConfig2.COLUMN_MESSAGE_CONTENT + " VARCHAR"
                + ", " + DataConfig2.COLUMN_MESSAGE_STATUS + " VARCHAR"
                + ")");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}


}
