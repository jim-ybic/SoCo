package com.soco.SoCoClient.v2.control.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.soco.SoCoClient.v2.control.config.DataConfig;

public class DbHelper extends SQLiteOpenHelper {

    public static String DATABASE_NAME = "socodb.v2.11";
    public static int DATABASE_VERSION = 1;

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + DataConfig.TABLE_TASK + " ("
                + DataConfig.COLUMN_TASK_TASKIDLOCAL + " INTEGER PRIMARY KEY AUTOINCREMENT"
                + ", " + DataConfig.COLUMN_TASK_TASKIDSERVER + " INTEGER"
                + ", " + DataConfig.COLUMN_TASK_TASKNAME + " VARCHAR"
                + ", " + DataConfig.COLUMN_TASK_TASKPATH + " VARCHAR"
                + ", " + DataConfig.COLUMN_TASK_ISTASKACTIVE + " INTEGER"
                + ")");

        db.execSQL("CREATE TABLE IF NOT EXISTS " + DataConfig.TABLE_EVENT + " ("
                + DataConfig.COLUMN_EVENT_SEQ + " INTEGER PRIMARY KEY AUTOINCREMENT"
                + ", " + DataConfig.COLUMN_EVENT_ID + " INTEGER"
                + ", " + DataConfig.COLUMN_EVENT_NAME + " VARCHAR"
                + ", " + DataConfig.COLUMN_EVENT_DESC + " VARCHAR"
                + ", " + DataConfig.COLUMN_EVENT_DATE + " VARCHAR"
                + ", " + DataConfig.COLUMN_EVENT_TIME + " VARCHAR"
                + ", " + DataConfig.COLUMN_EVENT_LOCATION + " VARCHAR"
                + ", " + DataConfig.COLUMN_EVENT_ISDRAFT + " VARCHAR"
                + ", " + DataConfig.COLUMN_EVENT_ISDONE + " VARCHAR"
                + ")");

            db.execSQL("CREATE TABLE IF NOT EXISTS " + DataConfig.TABLE_PERSON + " ("
                    + DataConfig.COLUMN_PERSON_SEQ + " INTEGER PRIMARY KEY AUTOINCREMENT"
                    + ", " + DataConfig.COLUMN_PERSON_ID + " INTEGER"
                    + ", " + DataConfig.COLUMN_PERSON_NAME + " VARCHAR"
                    + ", " + DataConfig.COLUMN_PERSON_EMAIL + " VARCHAR"
                    + ", " + DataConfig.COLUMN_PERSON_PHONE + " VARCHAR"
                    + ", " + DataConfig.COLUMN_PERSON_WECHATID + " VARCHAR"
                    + ", " + DataConfig.COLUMN_PERSON_FACEBOOKID + " VARCHAR"
                    + ")");

        db.execSQL("CREATE TABLE IF NOT EXISTS " + DataConfig.TABLE_ATTRIBUTE + " ("
                + DataConfig.COLUMN_ATTRIBUTE_ATTRIDLOCAL + " INTEGER PRIMARY KEY AUTOINCREMENT"
                + ", " + DataConfig.COLUMN_ATTRIBUTE_ATTRIDSERVER + " INTEGER"
                + ", " + DataConfig.COLUMN_ATTRIBUTE_ACTIVITYTYPE + " VARCHAR"
                + ", " + DataConfig.COLUMN_ATTRIBUTE_ACTIVITYIDLOCAL + " INTEGER"
                + ", " + DataConfig.COLUMN_ATTRIBUTE_ACTIVITYIDSERVER + " INTEGER"
                + ", " + DataConfig.COLUMN_ATTRIBUTE_ATTRNAME + " VARCHAR"
                + ", " + DataConfig.COLUMN_ATTRIBUTE_ATTRVALUE + " VARCHAR"
                + ")");

        db.execSQL("CREATE TABLE IF NOT EXISTS " + DataConfig.TABLE_CONTACT + " ("
                + DataConfig.COLUMN_CONTACT_CONTACTIDLOCAL + " INTEGER PRIMARY KEY AUTOINCREMENT"
                + ", " + DataConfig.COLUMN_CONTACT_CONTACTIDSERVER + " INTEGER"
                + ", " + DataConfig.COLUMN_CONTACT_CONTACTEMAIL + " VARCHAR"
                + ", " + DataConfig.COLUMN_CONTACT_CONTACTUSERNAME + " VARCHAR"
                + ", " + DataConfig.COLUMN_CONTACT_CONTACTSERVERSTATUS + " VARCHAR"
                + ")");

        db.execSQL("CREATE TABLE IF NOT EXISTS " + DataConfig.TABLE_MESSAGE + " ("
                + DataConfig.COLUMN_MESSAGE_MSGIDLOCAL + " INTEGER PRIMARY KEY AUTOINCREMENT"
                + ", " + DataConfig.COLUMN_MESSAGE_MSGIDSERVER + " INTEGER"
                + ", " + DataConfig.COLUMN_MESSAGE_FROMTYPE + " INTEGER"
                + ", " + DataConfig.COLUMN_MESSAGE_FROMID+ " VARCHAR"
                + ", " + DataConfig.COLUMN_MESSAGE_TOTYPE + " INTEGER"
                + ", " + DataConfig.COLUMN_MESSAGE_TOID + " VARCHAR"
                + ", " + DataConfig.COLUMN_MESSAGE_CREATETIMESTAMP + " VARCHAR"
                + ", " + DataConfig.COLUMN_MESSAGE_SENDTIMESTAMP + " VARCHAR"
                + ", " + DataConfig.COLUMN_MESSAGE_RECEIVETIMESTAMP + " VARCHAR"
                + ", " + DataConfig.COLUMN_MESSAGE_FROMDEVICE + " VARCHAR"
                + ", " + DataConfig.COLUMN_MESSAGE_CONTENTTYPE + " INTEGER"
                + ", " + DataConfig.COLUMN_MESSAGE_CONTENT + " VARCHAR"
                + ", " + DataConfig.COLUMN_MESSAGE_STATUS + " VARCHAR"
                + ", " + DataConfig.COLUMN_MESSAGE_SIGNATURE + " VARCHAR"
                + ")");

        db.execSQL("CREATE TABLE IF NOT EXISTS " + DataConfig.TABLE_PARTYJOINACTIVITY + " ("
                + DataConfig.COLUMN_PARTYJOINACTIVITY_IDLOCAL + " INTEGER PRIMARY KEY AUTOINCREMENT"
                + ", " + DataConfig.COLUMN_PARTYJOINACTIVITY_IDSERVER + " INTEGER"
                + ", " + DataConfig.COLUMN_PARTYJOINACTIVITY_PARTYTYPE + " VARCHAR"
                + ", " + DataConfig.COLUMN_PARTYJOINACTIVITY_PARTYIDLOCAL + " INTEGER"
                + ", " + DataConfig.COLUMN_PARTYJOINACTIVITY_PARTYIDSERVER + " INTEGER"
                + ", " + DataConfig.COLUMN_PARTYJOINACTIVITY_ACTIVITYTYPE + " VARCHAR"
                + ", " + DataConfig.COLUMN_PARTYJOINACTIVITY_ACTIVITYIDLOCAL + " INTEGER"
                + ", " + DataConfig.COLUMN_PARTYJOINACTIVITY_ACTIVITYIDSERVER + " INTEGER"
                + ", " + DataConfig.COLUMN_PARTYJOINACTIVITY_ROLE + " VARCHAR"
                + ", " + DataConfig.COLUMN_PARTYJOINACTIVITY_STATUS + " VARCHAR"
                + ", " + DataConfig.COLUMN_PARTYJOINACTIVITY_JOINTIMESTAMP + " VARCHAR"
                + ")");

        db.execSQL("CREATE TABLE IF NOT EXISTS " + DataConfig.TABLE_ATTACHMENT + " ("
                + DataConfig.COLUMN_ATTACHMENT_ATTACHMENTIDLOCAL + " INTEGER PRIMARY KEY AUTOINCREMENT"
                + ", " + DataConfig.COLUMN_ATTACHMENT_ATTACHMENTIDSERVER + " INTEGER"
                + ", " + DataConfig.COLUMN_ATTACHMENT_ACTIVITYTYPE + " VARCHAR"
                + ", " + DataConfig.COLUMN_ATTACHMENT_ACTIVITYIDLOCAL + " INTEGER"
                + ", " + DataConfig.COLUMN_ATTACHMENT_ACTIVITYIDSERVER + " INTEGER"
                + ", " + DataConfig.COLUMN_ATTACHMENT_DISPLAYNAME + " VARCHAR"
                + ", " + DataConfig.COLUMN_ATTACHMENT_URI + " VARCHAR"
                + ", " + DataConfig.COLUMN_ATTACHMENT_REMOTEPATH + " VARCHAR"
                + ", " + DataConfig.COLUMN_ATTACHMENT_LOCALPATH + " VARCHAR"
                + ", " + DataConfig.COLUMN_ATTACHMENT_USER + " VARCHAR"
                + ", " + DataConfig.COLUMN_ATTACHMENT_CREATETIMESTAMP + " VARCHAR"
                + ")");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}


}
