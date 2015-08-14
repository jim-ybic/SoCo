package com.soco.SoCoClient.v2.model;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.soco.SoCoClient.v2.control.config.DataConfig;
import com.soco.SoCoClient.v2.control.database.DbHelper;
import com.soco.SoCoClient.v2.control.http.task.CreateTaskOnServerJob;

import java.sql.SQLClientInfoException;

public class Event {

    String tag = "Event";

    //fields saved to db
    int seq;
    int id;
    String name;
    int isDraft;
    int isDone;

     //local variables
    Context context;
    SQLiteDatabase db;

    public Event(Context c){
        Log.v(tag, "create new event");

        this.context = c;

        this.seq = DataConfig.ENTITIY_ID_NOT_READY;
        this.id = DataConfig.ENTITIY_ID_NOT_READY;
        this.isDraft = 1;
        this.isDone = 0;

        DbHelper helper = new DbHelper(c);
        this.db = helper.getWritableDatabase();
    }

    public Event(Cursor cursor){
        Log.v(tag, "create event from cursor");
        this.seq = cursor.getInt(cursor.getColumnIndex(DataConfig.COLUMN_EVENT_SEQ));
        this.id = cursor.getInt(cursor.getColumnIndex(DataConfig.COLUMN_EVENT_ID));
        this.name = cursor.getString(cursor.getColumnIndex(DataConfig.COLUMN_EVENT_NAME));
        this.isDraft = cursor.getInt(cursor.getColumnIndex(DataConfig.COLUMN_EVENT_ISDRAFT));
        this.isDone = cursor.getInt(cursor.getColumnIndex(DataConfig.COLUMN_EVENT_ISDONE));
        Log.v(tag, "created event from cursor: " + toString());
    }

    public void save(){
        if (seq == DataConfig.ENTITIY_ID_NOT_READY){
            Log.v(tag, "save new event");
            saveNew();
        }else{
            Log.v(tag, "update existing event");
            update();
        }
    }

    void saveNew(){
        Log.v(tag, "save new event to db");
        try {
            db.beginTransaction();
            ContentValues cv = new ContentValues();
            cv.put(DataConfig.COLUMN_EVENT_NAME, name);
            cv.put(DataConfig.COLUMN_EVENT_ISDRAFT, isDraft);
            cv.put(DataConfig.COLUMN_EVENT_ISDONE, isDone);

            db.insert(DataConfig.TABLE_EVENT, null, cv);
            db.setTransactionSuccessful();
            Log.d(tag, "new event added to db: " + toString());
        } finally {
            db.endTransaction();
        }

        Log.v(tag, "get seq from db");
        String query = "select max (" + DataConfig.COLUMN_EVENT_SEQ
                + ") from " + DataConfig.TABLE_EVENT;
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()){
            int s = cursor.getInt(0);
            Log.d(tag, "update event seq: " + s);
            seq = s;
        }

        //todo: save event on server
    }

    void update(){
        Log.v(tag, "update existing event to local database");
        try {
            db.beginTransaction();
            ContentValues cv = new ContentValues();
            cv.put(DataConfig.COLUMN_EVENT_ID, id);
            cv.put(DataConfig.COLUMN_EVENT_NAME, name);
            cv.put(DataConfig.COLUMN_EVENT_ISDRAFT, isDraft);
            cv.put(DataConfig.COLUMN_EVENT_ISDONE, isDone);

            db.update(DataConfig.TABLE_EVENT, cv,
                    DataConfig.COLUMN_EVENT_SEQ + " = ?",
                    new String[]{String.valueOf(seq)});
            db.setTransactionSuccessful();
            Log.d(tag, "event updated to db: " + toString());
        } finally {
            db.endTransaction();
        }

        //todo: update event to server
    }

    public String getInfo(){
        return "More info coming soon...";
    }

    public String toString() {
        return "Event{" +
                "seq=" + seq +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", isDraft='" + isDraft + '\'' +
                ", isDone='" + isDone + '\'' +
                '}';
    }

    public int getSeq() {
        return seq;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int isDraft() {
        return isDraft;
    }

    public int isDone() {
        return isDone;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIsDraft(int isDraft) {
        this.isDraft = isDraft;
    }

    public void setIsDone(int isDone) {
        this.isDone = isDone;
    }
}