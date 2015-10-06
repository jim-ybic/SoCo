package com.soco.SoCoClient.events.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.soco.SoCoClient.common.database.Config;
import com.soco.SoCoClient.common.database.DbHelper;
import com.soco.SoCoClient.common.util.TimeUtil;

public class Attachment {

    String tag = "Attachment";

    //fields saved to db
    int attachmentIdLocal;
    int attachmentIdServer;
    String activityType;
    int activityIdLocal;
    int activityIdServer;
    String displayName;
    String uri;
    String remotePath;
    String localPath;
    String user;
    String createTimestamp;

    //fields not saved to db
    Context context;
    SQLiteDatabase db;

    public Attachment(Context context,
                      String activityType, int activityIdLocal, int activityIdServer){
        Log.v(tag, "create new attachment object");

        this.context = context;
        this.activityType = activityType;
        this.activityIdLocal = activityIdLocal;
        this.activityIdServer = activityIdServer;

        this.attachmentIdLocal = Config.ENTITIY_ID_NOT_READY;
        this.attachmentIdServer = Config.ENTITIY_ID_NOT_READY;
        this.createTimestamp = TimeUtil.now();

        DbHelper dbHelper = new DbHelper(context);
        this.db = dbHelper.getWritableDatabase();
    }

    public Attachment(Cursor cursor){
        Log.v(tag, "create attribute from cursor");
        this.attachmentIdLocal = cursor.getInt(cursor.getColumnIndex(Config.COLUMN_ATTACHMENT_ATTACHMENTIDLOCAL));
        this.attachmentIdServer = cursor.getInt(cursor.getColumnIndex(Config.COLUMN_ATTACHMENT_ATTACHMENTIDSERVER));
        this.activityType = cursor.getString(cursor.getColumnIndex(Config.COLUMN_ATTACHMENT_ACTIVITYTYPE));
        this.activityIdLocal = cursor.getInt(cursor.getColumnIndex(Config.COLUMN_ATTACHMENT_ACTIVITYIDLOCAL));
        this.activityIdServer = cursor.getInt(cursor.getColumnIndex(Config.COLUMN_ATTACHMENT_ACTIVITYIDSERVER));
        this.displayName = cursor.getString(cursor.getColumnIndex(Config.COLUMN_ATTACHMENT_DISPLAYNAME));
        this.uri = cursor.getString(cursor.getColumnIndex(Config.COLUMN_ATTACHMENT_URI));
        this.remotePath = cursor.getString(cursor.getColumnIndex(Config.COLUMN_ATTACHMENT_REMOTEPATH));
        this.localPath = cursor.getString(cursor.getColumnIndex(Config.COLUMN_ATTACHMENT_LOCALPATH));
        this.user = cursor.getString(cursor.getColumnIndex(Config.COLUMN_ATTACHMENT_USER));
        this.createTimestamp = cursor.getString(cursor.getColumnIndex(Config.COLUMN_ATTACHMENT_CREATETIMESTAMP));
        Log.d(tag, "created attachment from cursor: " + toString());
    }

    public void save(){
        if(attachmentIdLocal == Config.ENTITIY_ID_NOT_READY){
            Log.v(tag, "save new attachment");
            saveNew();
        }else{
            Log.v(tag, "update existing attachment");
            update();
        }
    }

    void saveNew(){
        Log.v(tag, "save new attachment into database");
        try{
            db.beginTransaction();
            ContentValues cv = new ContentValues();
            cv.put(Config.COLUMN_ATTACHMENT_ATTACHMENTIDSERVER, attachmentIdServer);
            cv.put(Config.COLUMN_ATTACHMENT_ACTIVITYTYPE, activityType);
            cv.put(Config.COLUMN_ATTACHMENT_ACTIVITYIDLOCAL, activityIdLocal);
            cv.put(Config.COLUMN_ATTACHMENT_ACTIVITYIDSERVER, activityIdServer);
            cv.put(Config.COLUMN_ATTACHMENT_DISPLAYNAME, displayName);
            cv.put(Config.COLUMN_ATTACHMENT_URI, uri);
            cv.put(Config.COLUMN_ATTACHMENT_REMOTEPATH, remotePath);
            cv.put(Config.COLUMN_ATTACHMENT_LOCALPATH, localPath);
            cv.put(Config.COLUMN_ATTACHMENT_USER, user);
            cv.put(Config.COLUMN_ATTACHMENT_CREATETIMESTAMP, createTimestamp);
            db.insert(Config.TABLE_ATTACHMENT, null, cv);
            db.setTransactionSuccessful();
            Log.d(tag, "new attachment inserted into database: " + toString());
        }finally {
            db.endTransaction();
        }

        Log.v(tag, "attachment id local from database");
        int aidLocal = -1;
        String query = "select max (" + Config.COLUMN_ATTACHMENT_ATTACHMENTIDLOCAL
                + ") from " + Config.TABLE_ATTACHMENT;
        Cursor cursor = db.rawQuery(query, null);
        if(cursor.moveToFirst()) {
            aidLocal = cursor.getInt(0);
            Log.d(tag, "update attachment id local: " + aidLocal);
            attachmentIdLocal = aidLocal;
        }

        //todo: save new attachment to server
    }

    void update(){
        Log.v(tag, "update existing attachment to local database");
        try{
            db.beginTransaction();
            ContentValues cv = new ContentValues();
            cv.put(Config.COLUMN_ATTACHMENT_ATTACHMENTIDLOCAL, attachmentIdLocal);
            cv.put(Config.COLUMN_ATTACHMENT_ATTACHMENTIDSERVER, attachmentIdServer);
            cv.put(Config.COLUMN_ATTACHMENT_ACTIVITYTYPE, activityType);
            cv.put(Config.COLUMN_ATTACHMENT_ACTIVITYIDLOCAL, activityIdLocal);
            cv.put(Config.COLUMN_ATTACHMENT_ACTIVITYIDSERVER, activityIdServer);
            cv.put(Config.COLUMN_ATTACHMENT_DISPLAYNAME, displayName);
            cv.put(Config.COLUMN_ATTACHMENT_URI, uri);
            cv.put(Config.COLUMN_ATTACHMENT_REMOTEPATH, remotePath);
            cv.put(Config.COLUMN_ATTACHMENT_LOCALPATH, localPath);
            cv.put(Config.COLUMN_ATTACHMENT_USER, user);
            cv.put(Config.COLUMN_ATTACHMENT_CREATETIMESTAMP, createTimestamp);
            db.update(Config.TABLE_ATTACHMENT, cv,
                    Config.COLUMN_ATTACHMENT_ATTACHMENTIDLOCAL + " = ?",
                    new String[]{String.valueOf(attachmentIdLocal)});
            db.setTransactionSuccessful();
            Log.d(tag, "attachment updated into database: " + toString());
        }finally {
            db.endTransaction();
        }

        //todo: update attachment to server
    }

    public void delete(){
        Log.v(tag, "delete existing attachment");
        if(attachmentIdLocal == Config.ENTITIY_ID_NOT_READY){
            Log.e(tag, "cannot delete a non-existing attachment");
        }else{
            db.delete(Config.TABLE_ATTACHMENT,
                    Config.COLUMN_ATTACHMENT_ATTACHMENTIDLOCAL + " = ?",
                    new String[]{String.valueOf(attachmentIdLocal)});
            Log.d(tag, "attachment deleted from database: " + toString());
        }
    }

    public String toString() {
        return "Attachment{" +
                "attachmentIdLocal=" + attachmentIdLocal +
                ", attachmentIdServer=" + attachmentIdServer +
                ", activityType='" + activityType + '\'' +
                ", activityIdLocal=" + activityIdLocal +
                ", activityIdServer=" + activityIdServer +
                ", displayName='" + displayName + '\'' +
                ", uri='" + uri + '\'' +
                ", remotePath='" + remotePath + '\'' +
                ", localPath='" + localPath + '\'' +
                ", user='" + user + '\'' +
                ", createTimestamp='" + createTimestamp + '\'' +
                '}';
    }

    public int getAttachmentIdLocal() {
        return attachmentIdLocal;
    }

    public void setAttachmentIdLocal(int attachmentIdLocal) {
        this.attachmentIdLocal = attachmentIdLocal;
    }

    public int getAttachmentIdServer() {
        return attachmentIdServer;
    }

    public void setAttachmentIdServer(int attachmentIdServer) {
        this.attachmentIdServer = attachmentIdServer;
    }

    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

    public int getActivityIdLocal() {
        return activityIdLocal;
    }

    public void setActivityIdLocal(int activityIdLocal) {
        this.activityIdLocal = activityIdLocal;
    }

    public int getActivityIdServer() {
        return activityIdServer;
    }

    public void setActivityIdServer(int activityIdServer) {
        this.activityIdServer = activityIdServer;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getRemotePath() {
        return remotePath;
    }

    public void setRemotePath(String remotePath) {
        this.remotePath = remotePath;
    }

    public String getLocalPath() {
        return localPath;
    }

    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getCreateTimestamp() {
        return createTimestamp;
    }

    public void setCreateTimestamp(String createTimestamp) {
        this.createTimestamp = createTimestamp;
    }

}
