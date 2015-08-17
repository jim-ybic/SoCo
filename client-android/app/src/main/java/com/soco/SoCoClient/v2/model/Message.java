package com.soco.SoCoClient.v2.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.soco.SoCoClient.v2.control.config.DataConfig;
import com.soco.SoCoClient.v2.control.database.DbHelper;
import com.soco.SoCoClient.v2.control.util.TimeUtil;

public class Message {

    String tag = "Message";

    //fields saved to db
    int seq;
    int id;
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
    String signature;

    //fields not saved to db
    Context context;
    DbHelper helper;
    SQLiteDatabase db;

    public Message(Context context){
        Log.v(tag, "create new message");

        this.context = context;
        this.helper = new DbHelper(context);

        this.seq = DataConfig.ENTITIY_ID_NOT_READY;
        this.id = DataConfig.ENTITIY_ID_NOT_READY;
        this.createTimestamp = TimeUtil.now();
        this.fromDevice = DataConfig.ENTITY_VALUE_EMPTY;
        this.status = DataConfig.MESSAGE_STATUS_NEW;

//        this.db = dbHelper.getWritableDatabase();
    }

    public Message(Context context, Cursor cursor){
        Log.v(tag, "create message from cursor");
        this.context = context;
        this.helper = new DbHelper(context);

        this.seq = cursor.getInt(cursor.getColumnIndex(DataConfig.COLUMN_MESSAGE_SEQ));
        this.id = cursor.getInt(cursor.getColumnIndex(DataConfig.COLUMN_MESSAGE_ID));
        this.fromType = cursor.getInt(cursor.getColumnIndex(DataConfig.COLUMN_MESSAGE_FROMTYPE));
        this.fromId = cursor.getString(cursor.getColumnIndex(DataConfig.COLUMN_MESSAGE_FROMID));
        this.toType = cursor.getInt(cursor.getColumnIndex(DataConfig.COLUMN_MESSAGE_TOTYPE));
        this.toId = cursor.getString(cursor.getColumnIndex(DataConfig.COLUMN_MESSAGE_TOID));
        this.createTimestamp = cursor.getString(cursor.getColumnIndex(DataConfig.COLUMN_MESSAGE_CREATETIMESTAMP));
        this.sendTimestamp = cursor.getString(cursor.getColumnIndex(DataConfig.COLUMN_MESSAGE_SENDTIMESTAMP));
        this.receiveTimestamp = cursor.getString(cursor.getColumnIndex(DataConfig.COLUMN_MESSAGE_RECEIVETIMESTAMP));
        this.fromDevice = cursor.getString(cursor.getColumnIndex(DataConfig.COLUMN_MESSAGE_FROMDEVICE));
        this.contentType = cursor.getInt(cursor.getColumnIndex(DataConfig.COLUMN_MESSAGE_CONTENTTYPE));
        this.content = cursor.getString(cursor.getColumnIndex(DataConfig.COLUMN_MESSAGE_CONTENT));
        this.status = cursor.getString(cursor.getColumnIndex(DataConfig.COLUMN_MESSAGE_STATUS));
        this.signature = cursor.getString(cursor.getColumnIndex(DataConfig.COLUMN_MESSAGE_SIGNATURE));
        Log.v(tag, "created message from cursor: " + toString());
    }

    public void save(){
//        if(db == null){
//            this.db = helper.getWritableDatabase();
//            return;
//        }

        if(seq == DataConfig.ENTITIY_ID_NOT_READY){
            Log.v(tag, "save new message");
            saveNew();
        }else{
            Log.v(tag, "update existing message");
            update();
        }
    }

    void saveNew(){
        Log.v(tag, "save new message to database");
        this.db = helper.getWritableDatabase();

        try{
            db.beginTransaction();
            ContentValues cv = new ContentValues();
            cv.put(DataConfig.COLUMN_MESSAGE_ID, id);
            cv.put(DataConfig.COLUMN_MESSAGE_FROMTYPE, fromType);
            cv.put(DataConfig.COLUMN_MESSAGE_FROMID, fromId);
            cv.put(DataConfig.COLUMN_MESSAGE_TOTYPE, toType);
            cv.put(DataConfig.COLUMN_MESSAGE_TOID, toId);
            cv.put(DataConfig.COLUMN_MESSAGE_CREATETIMESTAMP, createTimestamp);
            cv.put(DataConfig.COLUMN_MESSAGE_SENDTIMESTAMP, sendTimestamp);
            cv.put(DataConfig.COLUMN_MESSAGE_RECEIVETIMESTAMP, receiveTimestamp);
            cv.put(DataConfig.COLUMN_MESSAGE_FROMDEVICE, fromDevice);
            cv.put(DataConfig.COLUMN_MESSAGE_CONTENTTYPE, contentType);
            cv.put(DataConfig.COLUMN_MESSAGE_CONTENT, content);
            cv.put(DataConfig.COLUMN_MESSAGE_STATUS, status);
            cv.put(DataConfig.COLUMN_MESSAGE_SIGNATURE, signature);
            db.insert(DataConfig.TABLE_MESSAGE, null, cv);
            db.setTransactionSuccessful();
            Log.d(tag, "new message added to database: " + toString());
        } finally {
            db.endTransaction();
        }

        Log.v(tag, "get message id local from database");
        int midLocal = -1;
        String query = "select max (" + DataConfig.COLUMN_MESSAGE_SEQ
                + ") from " + DataConfig.TABLE_MESSAGE;
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()){
            midLocal = cursor.getInt(0);
            Log.d(tag, "update message id local: " + midLocal);
            seq = midLocal;
        }
        db.close();

        //todo: send message to server, update field

    }

    void update(){
        Log.v(tag, "update existing message to local database");
        try {
            db.beginTransaction();
            ContentValues cv = new ContentValues();
            cv.put(DataConfig.COLUMN_MESSAGE_SEQ, seq);
            cv.put(DataConfig.COLUMN_MESSAGE_ID, id);
            cv.put(DataConfig.COLUMN_MESSAGE_FROMTYPE, fromType);
            cv.put(DataConfig.COLUMN_MESSAGE_FROMID, fromId);
            cv.put(DataConfig.COLUMN_MESSAGE_TOTYPE, toType);
            cv.put(DataConfig.COLUMN_MESSAGE_TOID, toId);
            cv.put(DataConfig.COLUMN_MESSAGE_CREATETIMESTAMP, createTimestamp);
            cv.put(DataConfig.COLUMN_MESSAGE_SENDTIMESTAMP, sendTimestamp);
            cv.put(DataConfig.COLUMN_MESSAGE_RECEIVETIMESTAMP, receiveTimestamp);
            cv.put(DataConfig.COLUMN_MESSAGE_FROMDEVICE, fromDevice);
            cv.put(DataConfig.COLUMN_MESSAGE_CONTENTTYPE, contentType);
            cv.put(DataConfig.COLUMN_MESSAGE_CONTENT, content);
            cv.put(DataConfig.COLUMN_MESSAGE_STATUS, status);
            cv.put(DataConfig.COLUMN_MESSAGE_SIGNATURE, signature);
            db.update(DataConfig.TABLE_MESSAGE, cv,
                    DataConfig.COLUMN_MESSAGE_SEQ + " = ?",
                    new String[]{String.valueOf(seq)});
            db.setTransactionSuccessful();
            Log.d(tag, "message updated into database: " + toString());
        } finally {
            db.endTransaction();
            db.close();
        }

        //todo: update message to server
    }

    public void delete(){
        Log.v(tag, "delete existing message");
        if(seq == DataConfig.ENTITIY_ID_NOT_READY){
            Log.e(tag, "cannot delete a non-existing message");
        } else {
            db.delete(DataConfig.TABLE_MESSAGE,
                    DataConfig.COLUMN_MESSAGE_SEQ + " = ?",
                    new String[]{String.valueOf(seq)});
            Log.d(tag, "message deleted from database: " + toString());
            db.close();
        }

        //todo: delete message from server
    }

    public String toString() {
        return "Message{" +
                "seq=" + seq +
                ", id=" + id +
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
                ", signature='" + signature + '\'' +
                '}';
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }
}
