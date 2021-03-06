package com.soco.SoCoClient.common.model;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.soco.SoCoClient.common.database.Config;
import com.soco.SoCoClient.common.database.DbHelper;

@Deprecated
public class Person {

    static String tag = "Person";

    //fields saved to db
    int seq;
    int id;
    String name;
    String email;
    String phone;
    String wechatid;
    String facebookid;
    String status;
    String category;

    //local variables
    Context context;
    DbHelper helper;
    SQLiteDatabase db;

    public Person(Context c, String name, String phone, String email){
        Log.v(tag, "create new person");

        this.context = c;
        this.helper = new DbHelper(context);

        this.name = name;
        this.phone = phone;
        this.email = email;

        this.seq = Config.ENTITIY_ID_NOT_READY;
        this.id = Config.ENTITIY_ID_NOT_READY;

//        DbHelper helper = new DbHelper(c);
//        this.db = helper.getWritableDatabase();
    }

    public Person(Context context, Cursor cursor){
//        Log.v(tag, "create person from cursor");
        this.context = context;
        this.helper = new DbHelper(context);

        this.seq = cursor.getInt(cursor.getColumnIndex(Config.COLUMN_PERSON_SEQ));
        this.id = cursor.getInt(cursor.getColumnIndex(Config.COLUMN_PERSON_ID));
        this.name = cursor.getString(cursor.getColumnIndex(Config.COLUMN_PERSON_NAME));
        this.email = cursor.getString(cursor.getColumnIndex(Config.COLUMN_PERSON_EMAIL));
        this.phone = cursor.getString(cursor.getColumnIndex(Config.COLUMN_PERSON_PHONE));
        this.wechatid = cursor.getString(cursor.getColumnIndex(Config.COLUMN_PERSON_WECHATID));
        this.facebookid = cursor.getString(cursor.getColumnIndex(Config.COLUMN_PERSON_FACEBOOKID));
        this.status = cursor.getString(cursor.getColumnIndex(Config.COLUMN_PERSON_STATUS));
        this.category = cursor.getString(cursor.getColumnIndex(Config.COLUMN_PERSON_CATEGORY));

//        Log.v(tag, "created person from cursor: " + toString());
    }


//    public void addContext(Context c){
//        context = c;
//        DbHelper helper = new DbHelper(c);
//        db = helper.getWritableDatabase();
//    }

    public void save(){
//        if(db == null){
//            db = helper.getWritableDatabase();
//            Log.e(tag, "db not ready, please set context before saving");
//            return;
//        }

        if (seq == Config.ENTITIY_ID_NOT_READY){
            Log.v(tag, "save new person");
            saveNew();
        }else{
            Log.v(tag, "update existing person");
            update();
        }
    }

    void saveNew(){
        Log.v(tag, "save new person to db");
        db = helper.getWritableDatabase();

        try {
            db.beginTransaction();
            ContentValues cv = new ContentValues();
            cv.put(Config.COLUMN_PERSON_ID, id);
            cv.put(Config.COLUMN_PERSON_NAME, name);
            cv.put(Config.COLUMN_PERSON_EMAIL, email);
            cv.put(Config.COLUMN_PERSON_PHONE, phone);
            cv.put(Config.COLUMN_PERSON_WECHATID, wechatid);
            cv.put(Config.COLUMN_PERSON_FACEBOOKID, facebookid);
            cv.put(Config.COLUMN_PERSON_STATUS, status);
            cv.put(Config.COLUMN_PERSON_CATEGORY, category);

            db.insert(Config.TABLE_PERSON, null, cv);
            db.setTransactionSuccessful();
            Log.d(tag, "new person added to db: " + toString());
        } finally {
            db.endTransaction();
        }

        Log.v(tag, "get seq from db");
        String query = "select max (" + Config.COLUMN_PERSON_SEQ
                + ") from " + Config.TABLE_PERSON;
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()){
            int s = cursor.getInt(0);
            Log.d(tag, "update person seq: " + s);
            seq = s;
        }
        db.close();

        //todo: save event on server
    }

    void update(){
        Log.v(tag, "update existing person to local database");
        db = helper.getWritableDatabase();

        try {
            db.beginTransaction();
            ContentValues cv = new ContentValues();
            cv.put(Config.COLUMN_PERSON_ID, id);
            cv.put(Config.COLUMN_PERSON_NAME, name);
            cv.put(Config.COLUMN_PERSON_EMAIL, email);
            cv.put(Config.COLUMN_PERSON_PHONE, phone);
            cv.put(Config.COLUMN_PERSON_WECHATID, wechatid);
            cv.put(Config.COLUMN_PERSON_FACEBOOKID, facebookid);
            cv.put(Config.COLUMN_PERSON_STATUS, status);
            cv.put(Config.COLUMN_PERSON_CATEGORY, category);

            db.update(Config.TABLE_PERSON, cv,
                    Config.COLUMN_PERSON_SEQ + " = ?",
                    new String[]{String.valueOf(seq)});
            db.setTransactionSuccessful();
            Log.d(tag, "person updated to db: " + toString());
        } finally {
            db.endTransaction();
            db.close();
        }

        //todo: update event to server
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

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getWechatid() {
        return wechatid;
    }

    public String getFacebookid() {
        return facebookid;
    }

    public String getStatus() {
        return status;
    }

    public String getCategory() {
        return category;
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

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setWechatid(String wechatid) {
        this.wechatid = wechatid;
    }

    public void setFacebookid(String facebookid) {
        this.facebookid = facebookid;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "Person{" +
                "seq=" + seq +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", wechatid='" + wechatid + '\'' +
                ", facebookid='" + facebookid + '\'' +
                ", status='" + status + '\'' +
                ", category='" + category + '\'' +
                '}';
    }
}
