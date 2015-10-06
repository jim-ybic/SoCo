package com.soco.SoCoClient.common.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {

        public static String DATABASE_NAME = "socodb.v2.29";
        public static int DATABASE_VERSION = 1;

        public DbHelper(Context context) {
                super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
                db.execSQL("CREATE TABLE IF NOT EXISTS " + Config.TABLE_TASK + " ("
                        + Config.COLUMN_TASK_TASKIDLOCAL + " INTEGER PRIMARY KEY AUTOINCREMENT"
                        + ", " + Config.COLUMN_TASK_TASKIDSERVER + " INTEGER"
                        + ", " + Config.COLUMN_TASK_TASKNAME + " VARCHAR"
                        + ", " + Config.COLUMN_TASK_TASKPATH + " VARCHAR"
                        + ", " + Config.COLUMN_TASK_ISTASKACTIVE + " INTEGER"
                        + ")");

                db.execSQL("CREATE TABLE IF NOT EXISTS " + Config.TABLE_EVENT + " ("
                        + Config.COLUMN_EVENT_SEQ + " INTEGER PRIMARY KEY AUTOINCREMENT"
                        + ", " + Config.COLUMN_EVENT_ID + " INTEGER"
                        + ", " + Config.COLUMN_EVENT_NAME + " VARCHAR"
                        + ", " + Config.COLUMN_EVENT_DESC + " VARCHAR"
                        + ", " + Config.COLUMN_EVENT_DATE + " VARCHAR"
                        + ", " + Config.COLUMN_EVENT_TIME + " VARCHAR"
                        + ", " + Config.COLUMN_EVENT_LOCATION + " VARCHAR"
                        + ", " + Config.COLUMN_EVENT_ISDRAFT + " VARCHAR"
                        + ", " + Config.COLUMN_EVENT_ISDONE + " VARCHAR"
                        + ")");

                db.execSQL("CREATE TABLE IF NOT EXISTS " + Config.TABLE_PERSON + " ("
                        + Config.COLUMN_PERSON_SEQ + " INTEGER PRIMARY KEY AUTOINCREMENT"
                        + ", " + Config.COLUMN_PERSON_ID + " INTEGER"
                        + ", " + Config.COLUMN_PERSON_NAME + " VARCHAR"
                        + ", " + Config.COLUMN_PERSON_EMAIL + " VARCHAR"
                        + ", " + Config.COLUMN_PERSON_PHONE + " VARCHAR"
                        + ", " + Config.COLUMN_PERSON_WECHATID + " VARCHAR"
                        + ", " + Config.COLUMN_PERSON_FACEBOOKID + " VARCHAR"
                        + ", " + Config.COLUMN_PERSON_STATUS + " VARCHAR"
                        + ", " + Config.COLUMN_PERSON_CATEGORY + " VARCHAR"
                        + ")");

                db.execSQL("CREATE TABLE IF NOT EXISTS " + Config.TABLE_SINGLE_CONVERSATION + " ("
                        + Config.COLUMN_SINGLE_CONVERSATION_SEQ + " INTEGER PRIMARY KEY AUTOINCREMENT"
                        + ", " + Config.COLUMN_SINGLE_CONVERSATION_ID + " INTEGER"
                        + ", " + Config.COLUMN_SINGLE_CONVERSATION_TITLE + " VARCHAR"
                        + ", " + Config.COLUMN_SINGLE_CONVERSATION_LASTMSGCONTENT + " VARCHAR"
                        + ", " + Config.COLUMN_SINGLE_CONVERSATION_LASTMSGTIMESTAMP + " VARCHAR"
                        + ", " + Config.COLUMN_SINGLE_CONVERSATION_CREATEDBYUSERID + " INTEGER"
                        + ", " + Config.COLUMN_SINGLE_CONVERSATION_CREATEDTIMESTAMP + " VARCHAR"
                        + ", " + Config.COLUMN_SINGLE_CONVERSATION_COUNTERPARTYID + " INT"
                        + ", " + Config.COLUMN_SINGLE_CONVERSATION_COUNTERPARTYNAME + " VARCHAR"
                        + ")");

                db.execSQL("CREATE TABLE IF NOT EXISTS " + Config.TABLE_SINGLE_CONVERSATION_MESSAGE + " ("
                        + Config.COLUMN_SINGLE_CONVERSATION_MESSAGE_SEQ + " INTEGER PRIMARY KEY AUTOINCREMENT"
                        + ", " + Config.COLUMN_SINGLE_CONVERSATION_MESSAGE_CONSEQ + " INTEGER"
                        + ", " + Config.COLUMN_SINGLE_CONVERSATION_MESSAGE_MSGSEQ + " INTEGER"
                        + ")");

                    db.execSQL("CREATE TABLE IF NOT EXISTS " + Config.TABLE_ATTRIBUTE + " ("
                        + Config.COLUMN_ATTRIBUTE_ATTRIDLOCAL + " INTEGER PRIMARY KEY AUTOINCREMENT"
                        + ", " + Config.COLUMN_ATTRIBUTE_ATTRIDSERVER + " INTEGER"
                        + ", " + Config.COLUMN_ATTRIBUTE_ACTIVITYTYPE + " VARCHAR"
                        + ", " + Config.COLUMN_ATTRIBUTE_ACTIVITYIDLOCAL + " INTEGER"
                        + ", " + Config.COLUMN_ATTRIBUTE_ACTIVITYIDSERVER + " INTEGER"
                        + ", " + Config.COLUMN_ATTRIBUTE_ATTRNAME + " VARCHAR"
                        + ", " + Config.COLUMN_ATTRIBUTE_ATTRVALUE + " VARCHAR"
                        + ")");

                db.execSQL("CREATE TABLE IF NOT EXISTS " + Config.TABLE_CONTACT + " ("
                        + Config.COLUMN_CONTACT_CONTACTIDLOCAL + " INTEGER PRIMARY KEY AUTOINCREMENT"
                        + ", " + Config.COLUMN_CONTACT_CONTACTIDSERVER + " INTEGER"
                        + ", " + Config.COLUMN_CONTACT_CONTACTEMAIL + " VARCHAR"
                        + ", " + Config.COLUMN_CONTACT_CONTACTUSERNAME + " VARCHAR"
                        + ", " + Config.COLUMN_CONTACT_CONTACTSERVERSTATUS + " VARCHAR"
                        + ")");

                db.execSQL("CREATE TABLE IF NOT EXISTS " + Config.TABLE_MESSAGE + " ("
                        + Config.COLUMN_MESSAGE_SEQ + " INTEGER PRIMARY KEY AUTOINCREMENT"
                        + ", " + Config.COLUMN_MESSAGE_ID + " INTEGER"
                        + ", " + Config.COLUMN_MESSAGE_FROMTYPE + " INTEGER"
                        + ", " + Config.COLUMN_MESSAGE_FROMID+ " VARCHAR"
                        + ", " + Config.COLUMN_MESSAGE_TOTYPE + " INTEGER"
                        + ", " + Config.COLUMN_MESSAGE_TOID + " VARCHAR"
                        + ", " + Config.COLUMN_MESSAGE_CREATETIMESTAMP + " VARCHAR"
                        + ", " + Config.COLUMN_MESSAGE_SENDTIMESTAMP + " VARCHAR"
                        + ", " + Config.COLUMN_MESSAGE_RECEIVETIMESTAMP + " VARCHAR"
                        + ", " + Config.COLUMN_MESSAGE_FROMDEVICE + " VARCHAR"
                        + ", " + Config.COLUMN_MESSAGE_CONTENTTYPE + " INTEGER"
                        + ", " + Config.COLUMN_MESSAGE_CONTENT + " VARCHAR"
                        + ", " + Config.COLUMN_MESSAGE_STATUS + " VARCHAR"
                        + ", " + Config.COLUMN_MESSAGE_SIGNATURE + " VARCHAR"
                        + ")");

                db.execSQL("CREATE TABLE IF NOT EXISTS " + Config.TABLE_PARTYJOINACTIVITY + " ("
                        + Config.COLUMN_PARTYJOINACTIVITY_IDLOCAL + " INTEGER PRIMARY KEY AUTOINCREMENT"
                        + ", " + Config.COLUMN_PARTYJOINACTIVITY_IDSERVER + " INTEGER"
                        + ", " + Config.COLUMN_PARTYJOINACTIVITY_PARTYTYPE + " VARCHAR"
                        + ", " + Config.COLUMN_PARTYJOINACTIVITY_PARTYIDLOCAL + " INTEGER"
                        + ", " + Config.COLUMN_PARTYJOINACTIVITY_PARTYIDSERVER + " INTEGER"
                        + ", " + Config.COLUMN_PARTYJOINACTIVITY_ACTIVITYTYPE + " VARCHAR"
                        + ", " + Config.COLUMN_PARTYJOINACTIVITY_ACTIVITYIDLOCAL + " INTEGER"
                        + ", " + Config.COLUMN_PARTYJOINACTIVITY_ACTIVITYIDSERVER + " INTEGER"
                        + ", " + Config.COLUMN_PARTYJOINACTIVITY_ROLE + " VARCHAR"
                        + ", " + Config.COLUMN_PARTYJOINACTIVITY_STATUS + " VARCHAR"
                        + ", " + Config.COLUMN_PARTYJOINACTIVITY_JOINTIMESTAMP + " VARCHAR"
                        + ")");

                db.execSQL("CREATE TABLE IF NOT EXISTS " + Config.TABLE_ATTACHMENT + " ("
                        + Config.COLUMN_ATTACHMENT_ATTACHMENTIDLOCAL + " INTEGER PRIMARY KEY AUTOINCREMENT"
                        + ", " + Config.COLUMN_ATTACHMENT_ATTACHMENTIDSERVER + " INTEGER"
                        + ", " + Config.COLUMN_ATTACHMENT_ACTIVITYTYPE + " VARCHAR"
                        + ", " + Config.COLUMN_ATTACHMENT_ACTIVITYIDLOCAL + " INTEGER"
                        + ", " + Config.COLUMN_ATTACHMENT_ACTIVITYIDSERVER + " INTEGER"
                        + ", " + Config.COLUMN_ATTACHMENT_DISPLAYNAME + " VARCHAR"
                        + ", " + Config.COLUMN_ATTACHMENT_URI + " VARCHAR"
                        + ", " + Config.COLUMN_ATTACHMENT_REMOTEPATH + " VARCHAR"
                        + ", " + Config.COLUMN_ATTACHMENT_LOCALPATH + " VARCHAR"
                        + ", " + Config.COLUMN_ATTACHMENT_USER + " VARCHAR"
                        + ", " + Config.COLUMN_ATTACHMENT_CREATETIMESTAMP + " VARCHAR"
                        + ")");

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}


}
