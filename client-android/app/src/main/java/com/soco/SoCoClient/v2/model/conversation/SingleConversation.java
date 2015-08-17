package com.soco.SoCoClient.v2.model.conversation;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.soco.SoCoClient.v2.control.config.DataConfig;
import com.soco.SoCoClient.v2.control.database.DbHelper;
import com.soco.SoCoClient.v2.model.Message;

public class SingleConversation {

    static String tag = "SingleConversation";

    //fields saved to db
    int seq;
    int id;
    String lastMsgContent;
    String lastMsgTimestamp;
    int createdByUserId;
    String createdTimestamp;
    int counterpartyId;

    //fields not saved to db
    Context context;
    DbHelper helper;
    SQLiteDatabase db;

    public SingleConversation(Context c){
        Log.v(tag, "create new conversation");

        this.context = c;
        this.helper = new DbHelper(context);

        this.seq = DataConfig.ENTITIY_ID_NOT_READY;
        this.id = DataConfig.ENTITIY_ID_NOT_READY;
    }

    public SingleConversation(Context context, Cursor cursor){
        Log.v(tag, "create conversation from cursor");
        this.context = context;
        this.helper = new DbHelper(context);

        this.seq = cursor.getInt(cursor.getColumnIndex(DataConfig.COLUMN_SINGLE_CONVERSATION_SEQ));
        this.id = cursor.getInt(cursor.getColumnIndex(DataConfig.COLUMN_SINGLE_CONVERSATION_ID));
        this.lastMsgContent = cursor.getString(cursor.getColumnIndex(DataConfig.COLUMN_SINGLE_CONVERSATION_LASTMSGCONTENT));
        this.lastMsgTimestamp = cursor.getString(cursor.getColumnIndex(DataConfig.COLUMN_SINGLE_CONVERSATION_LASTMSGTIMESTAMP));
        this.createdByUserId = cursor.getInt(cursor.getColumnIndex(DataConfig.COLUMN_SINGLE_CONVERSATION_CREATEDBYUSERID));
        this.createdTimestamp = cursor.getString(cursor.getColumnIndex(DataConfig.COLUMN_SINGLE_CONVERSATION_CREATEDTIMESTAMP));
        this.counterpartyId = cursor.getInt(cursor.getColumnIndex(DataConfig.COLUMN_SINGLE_CONVERSATION_COUNTERPARTYID));
        Log.v(tag, "created conversation from cursor: " + toString());
    }

//    public void addContext(Context c){
//        context = c;
//        DbHelper helper = new DbHelper(c);
//        db = helper.getWritableDatabase();
//    }

    public void save(){
//        if(db == null){
//            Log.e(tag, "db not ready, please set context before saving");
//            db = helper.getWritableDatabase();
//            return;
//        }

        if (seq == DataConfig.ENTITIY_ID_NOT_READY){
            Log.v(tag, "save new conversation");
            saveNew();
        }else{
            Log.v(tag, "update existing conversation");
            update();
        }
    }

    void saveNew(){
        Log.v(tag, "save new conversation to db");
        db = helper.getWritableDatabase();

        try {
            db.beginTransaction();
            ContentValues cv = new ContentValues();
            cv.put(DataConfig.COLUMN_SINGLE_CONVERSATION_LASTMSGCONTENT, lastMsgContent);
            cv.put(DataConfig.COLUMN_SINGLE_CONVERSATION_LASTMSGTIMESTAMP, lastMsgTimestamp);
            cv.put(DataConfig.COLUMN_SINGLE_CONVERSATION_CREATEDBYUSERID, createdByUserId);
            cv.put(DataConfig.COLUMN_SINGLE_CONVERSATION_CREATEDTIMESTAMP, createdTimestamp);
            cv.put(DataConfig.COLUMN_SINGLE_CONVERSATION_COUNTERPARTYID, counterpartyId);

            db.insert(DataConfig.TABLE_SINGLE_CONVERSATION, null, cv);
            db.setTransactionSuccessful();
            Log.d(tag, "new conversation added to db: " + toString());
        } finally {
            db.endTransaction();
        }

        Log.v(tag, "get seq from db");
        String query = "select max (" + DataConfig.COLUMN_SINGLE_CONVERSATION_SEQ
                + ") from " + DataConfig.TABLE_SINGLE_CONVERSATION;
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()){
            int s = cursor.getInt(0);
            Log.d(tag, "update conversation seq: " + s);
            seq = s;
        }
        db.close();

        //todo: save conversation on server
    }

    void update(){
        Log.v(tag, "update existing conversation to db");
        db = helper.getWritableDatabase();

        try {
            db.beginTransaction();
            ContentValues cv = new ContentValues();
            cv.put(DataConfig.COLUMN_SINGLE_CONVERSATION_ID, id);
            cv.put(DataConfig.COLUMN_SINGLE_CONVERSATION_LASTMSGCONTENT, lastMsgContent);
            cv.put(DataConfig.COLUMN_SINGLE_CONVERSATION_LASTMSGTIMESTAMP, lastMsgTimestamp);
            cv.put(DataConfig.COLUMN_SINGLE_CONVERSATION_CREATEDBYUSERID, createdByUserId);
            cv.put(DataConfig.COLUMN_SINGLE_CONVERSATION_CREATEDTIMESTAMP, createdTimestamp);
            cv.put(DataConfig.COLUMN_SINGLE_CONVERSATION_COUNTERPARTYID, counterpartyId);

            db.update(DataConfig.TABLE_SINGLE_CONVERSATION, cv,
                    DataConfig.COLUMN_SINGLE_CONVERSATION_SEQ + " = ?",
                    new String[]{String.valueOf(seq)});
            db.setTransactionSuccessful();
            Log.d(tag, "conversation updated to db: " + toString());
        } finally {
            db.endTransaction();
            db.close();
        }

        //todo: update conversation to server
    }

    public void addMessage(Message m){
//        if(db == null){
//            db = helper.getWritableDatabase();
//            Log.e(tag, "db not ready, please set context before saving");
//            return;
//        }

        Log.v(tag, "adding message to conversation");
        db = helper.getWritableDatabase();

        try {
            db.beginTransaction();
            ContentValues cv = new ContentValues();
            cv.put(DataConfig.COLUMN_SINGLE_CONVERSATION_MESSAGE_CONSEQ, seq);
            cv.put(DataConfig.COLUMN_SINGLE_CONVERSATION_MESSAGE_MSGSEQ, m.getSeq());

            db.insert(DataConfig.TABLE_SINGLE_CONVERSATION_MESSAGE, null, cv);
            db.setTransactionSuccessful();
            Log.d(tag, "new message added to conversation: " + m.toString());
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    public int getSeq() {
        return seq;
    }

    public int getId() {
        return id;
    }

    public String getLastMsgContent() {
        return lastMsgContent;
    }

    public String getLastMsgTimestamp() {
        return lastMsgTimestamp;
    }

    public int getCreatedByUserId() {
        return createdByUserId;
    }

    public String getCreatedTimestamp() {
        return createdTimestamp;
    }

    public int getCounterpartyId() {
        return counterpartyId;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setLastMsgContent(String lastMsgContent) {
        this.lastMsgContent = lastMsgContent;
    }

    public void setLastMsgTimestamp(String lastMsgTimestamp) {
        this.lastMsgTimestamp = lastMsgTimestamp;
    }

    public void setCreatedByUserId(int createdByUserId) {
        this.createdByUserId = createdByUserId;
    }

    public void setCreatedTimestamp(String createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
    }

    public void setCounterpartyId(int counterpartyId) {
        this.counterpartyId = counterpartyId;
    }

    @Override
    public String toString() {
        return "SingleConversation{" +
                "seq=" + seq +
                ", id=" + id +
                ", lastMsgContent='" + lastMsgContent + '\'' +
                ", lastMsgTimestamp='" + lastMsgTimestamp + '\'' +
                ", createdByUserId=" + createdByUserId +
                ", createdTimestamp='" + createdTimestamp + '\'' +
                ", counterpartyId=" + counterpartyId +
                '}';
    }
}
