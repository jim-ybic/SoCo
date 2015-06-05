package com.soco.SoCoClient.v2.datamodel;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.soco.SoCoClient.v2.businesslogic.config.DataConfig2;
import com.soco.SoCoClient.v2.businesslogic.database.DbHelper;
import com.soco.SoCoClient.v2.businesslogic.http.task.CreateContactOnServerJob;
import com.soco.SoCoClient.v2.businesslogic.util.TimeUtil;

public class Message {

    String tag = "Message";

    //fields saved to db
    int msgIdLocal;
    int msgIdServer;
    int fromType;
    String fromId;
    int toType;
    String toId;
    String createTimestamp;
    String sendTimestamp;
    String receiveTimestamp;
    String fromDevice;
    int contentType;
    String content;
    String status;

    //fields not saved to db
    Context context;
    SQLiteDatabase db;

    public Message(Context context){
        Log.v(tag, "create new message");

        this.context = context;

        this.msgIdLocal = DataConfig2.ENTITIY_ID_NOT_READY;
        this.msgIdServer = DataConfig2.ENTITIY_ID_NOT_READY;
        this.createTimestamp = TimeUtil.now();
        this.fromDevice = DataConfig2.ENTITY_VALUE_EMPTY;
        this.status = DataConfig2.MESSAGE_STATUS_NEW;

        DbHelper dbHelper = new DbHelper(context);
        this.db = dbHelper.getWritableDatabase();
    }

    public Message(Cursor cursor){
        Log.v(tag, "create message from cursor");
        this.msgIdLocal = cursor.getInt(cursor.getColumnIndex(DataConfig2.COLUMN_MESSAGE_MSGIDLOCAL));
        this.msgIdServer = cursor.getInt(cursor.getColumnIndex(DataConfig2.COLUMN_MESSAGE_MSGIDSERVER));
        this.fromType = cursor.getInt(cursor.getColumnIndex(DataConfig2.COLUMN_MESSAGE_FROMTYPE));
        this.fromId = cursor.getString(cursor.getColumnIndex(DataConfig2.COLUMN_MESSAGE_FROMID));
        this.toType = cursor.getInt(cursor.getColumnIndex(DataConfig2.COLUMN_MESSAGE_TOTYPE));
        this.toId = cursor.getString(cursor.getColumnIndex(DataConfig2.COLUMN_MESSAGE_TOID));
        this.createTimestamp = cursor.getString(cursor.getColumnIndex(DataConfig2.COLUMN_MESSAGE_CREATETIMESTAMP));
        this.sendTimestamp = cursor.getString(cursor.getColumnIndex(DataConfig2.COLUMN_MESSAGE_SENDTIMESTAMP));
        this.receiveTimestamp = cursor.getString(cursor.getColumnIndex(DataConfig2.COLUMN_MESSAGE_RECEIVETIMESTAMP));
        this.fromDevice = cursor.getString(cursor.getColumnIndex(DataConfig2.COLUMN_MESSAGE_FROMDEVICE));
        this.contentType = cursor.getInt(cursor.getColumnIndex(DataConfig2.COLUMN_MESSAGE_CONTENTTYPE));
        this.content = cursor.getString(cursor.getColumnIndex(DataConfig2.COLUMN_MESSAGE_CONTENT));
        this.status = cursor.getString(cursor.getColumnIndex(DataConfig2.COLUMN_MESSAGE_STATUS));
        Log.v(tag, "created message from cursor: " + toString());
    }

    public void save(){
        if(msgIdLocal == DataConfig2.ENTITIY_ID_NOT_READY){
            Log.v(tag, "save new message");
            saveNew();
        }else{
            Log.v(tag, "update existing message");
            update();
        }
    }

    void saveNew(){
        Log.v(tag, "save new message to database");
        try{
            db.beginTransaction();
            ContentValues cv = new ContentValues();
            cv.put(DataConfig2.COLUMN_MESSAGE_MSGIDSERVER, msgIdServer);
            cv.put(DataConfig2.COLUMN_MESSAGE_FROMTYPE, fromType);
            cv.put(DataConfig2.COLUMN_MESSAGE_FROMID, fromId);
            cv.put(DataConfig2.COLUMN_MESSAGE_TOTYPE, toId);
            cv.put(DataConfig2.COLUMN_MESSAGE_CREATETIMESTAMP, createTimestamp);
            cv.put(DataConfig2.COLUMN_MESSAGE_SENDTIMESTAMP, sendTimestamp);
            cv.put(DataConfig2.COLUMN_MESSAGE_RECEIVETIMESTAMP, receiveTimestamp);
            cv.put(DataConfig2.COLUMN_MESSAGE_FROMDEVICE, fromDevice);
            cv.put(DataConfig2.COLUMN_MESSAGE_CONTENTTYPE, contentType);
            cv.put(DataConfig2.COLUMN_MESSAGE_CONTENT, content);
            cv.put(DataConfig2.COLUMN_MESSAGE_STATUS, status);
            db.insert(DataConfig2.TABLE_MESSAGE, null, cv);
            db.setTransactionSuccessful();
            Log.d(tag, "new message inserted to database: " + toString());
        } finally {
            db.endTransaction();
        }

        Log.v(tag, "get message id local from database");
        int midLocal = -1;
        String query = "select max (" + DataConfig2.COLUMN_MESSAGE_MSGIDLOCAL
                + ") from " + DataConfig2.TABLE_MESSAGE;
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()){
            midLocal = cursor.getInt(0);
            Log.d(tag, "update message id local: " + midLocal);
            msgIdLocal = midLocal;
        }

        //todo: send message to server, update field

    }

    void update(){
        Log.v(tag, "update existing message to local database");
        try {
            db.beginTransaction();
            ContentValues cv = new ContentValues();
            cv.put(DataConfig2.COLUMN_MESSAGE_MSGIDLOCAL, msgIdLocal);
            cv.put(DataConfig2.COLUMN_MESSAGE_MSGIDSERVER, msgIdServer);
            cv.put(DataConfig2.COLUMN_MESSAGE_FROMTYPE, fromType);
            cv.put(DataConfig2.COLUMN_MESSAGE_FROMID, fromId);
            cv.put(DataConfig2.COLUMN_MESSAGE_TOTYPE, toId);
            cv.put(DataConfig2.COLUMN_MESSAGE_CREATETIMESTAMP, createTimestamp);
            cv.put(DataConfig2.COLUMN_MESSAGE_SENDTIMESTAMP, sendTimestamp);
            cv.put(DataConfig2.COLUMN_MESSAGE_RECEIVETIMESTAMP, receiveTimestamp);
            cv.put(DataConfig2.COLUMN_MESSAGE_FROMDEVICE, fromDevice);
            cv.put(DataConfig2.COLUMN_MESSAGE_CONTENTTYPE, contentType);
            cv.put(DataConfig2.COLUMN_MESSAGE_CONTENT, content);
            cv.put(DataConfig2.COLUMN_MESSAGE_STATUS, status);
            db.update(DataConfig2.TABLE_MESSAGE, cv,
                    DataConfig2.COLUMN_MESSAGE_MSGIDLOCAL + " = ?",
                    new String[]{String.valueOf(msgIdLocal)});
            db.setTransactionSuccessful();
            Log.d(tag, "message updated into database: " + toString());
        } finally {
            db.endTransaction();
        }

        //todo: update message to server
    }

    public void delete(){
        Log.v(tag, "delete existing message");
        if(msgIdLocal == DataConfig2.ENTITIY_ID_NOT_READY){
            Log.e(tag, "cannot delete a non-existing message");
        } else {
            db.delete(DataConfig2.TABLE_MESSAGE,
                    DataConfig2.COLUMN_MESSAGE_MSGIDLOCAL + " = ?",
                    new String[]{String.valueOf(msgIdLocal)});
            Log.d(tag, "message deleted from database: " + toString());
        }

        //todo: delete message from server
    }

    public String toString() {
        return "Message{" +
                "msgIdLocal=" + msgIdLocal +
                ", msgIdServer=" + msgIdServer +
                ", fromType=" + fromType +
                ", fromId='" + fromId + '\'' +
                ", toType=" + toType +
                ", toId='" + toId + '\'' +
                ", createTimestamp='" + createTimestamp + '\'' +
                ", sendTimestamp='" + sendTimestamp + '\'' +
                ", receiveTimestamp='" + receiveTimestamp + '\'' +
                ", fromDevice='" + fromDevice + '\'' +
                ", contentType=" + contentType +
                ", content='" + content + '\'' +
                ", status='" + status + '\'' +
                '}';
    }

    public int getMsgIdLocal() {
        return msgIdLocal;
    }

    public void setMsgIdLocal(int msgIdLocal) {
        this.msgIdLocal = msgIdLocal;
    }

    public int getMsgIdServer() {
        return msgIdServer;
    }

    public void setMsgIdServer(int msgIdServer) {
        this.msgIdServer = msgIdServer;
    }

    public int getFromType() {
        return fromType;
    }

    public void setFromType(int fromType) {
        this.fromType = fromType;
    }

    public String getFromId() {
        return fromId;
    }

    public void setFromId(String fromId) {
        this.fromId = fromId;
    }

    public int getToType() {
        return toType;
    }

    public void setToType(int toType) {
        this.toType = toType;
    }

    public String getToId() {
        return toId;
    }

    public void setToId(String toId) {
        this.toId = toId;
    }

    public String getCreateTimestamp() {
        return createTimestamp;
    }

    public void setCreateTimestamp(String createTimestamp) {
        this.createTimestamp = createTimestamp;
    }

    public String getSendTimestamp() {
        return sendTimestamp;
    }

    public void setSendTimestamp(String sendTimestamp) {
        this.sendTimestamp = sendTimestamp;
    }

    public String getReceiveTimestamp() {
        return receiveTimestamp;
    }

    public void setReceiveTimestamp(String receiveTimestamp) {
        this.receiveTimestamp = receiveTimestamp;
    }

    public String getFromDevice() {
        return fromDevice;
    }

    public void setFromDevice(String fromDevice) {
        this.fromDevice = fromDevice;
    }

    public int getContentType() {
        return contentType;
    }

    public void setContentType(int contentType) {
        this.contentType = contentType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
