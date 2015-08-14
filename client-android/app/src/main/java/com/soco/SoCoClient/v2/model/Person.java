package com.soco.SoCoClient.v2.model;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.soco.SoCoClient.v2.control.config.DataConfig;
import com.soco.SoCoClient.v2.control.database.DbHelper;

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

    //local variables
    Context context;
    SQLiteDatabase db;

    public Person(Context c, String name, String email){
        Log.v(tag, "create new person");

        this.context = c;
        this.name = name;
        this.email = email;

        this.seq = DataConfig.ENTITIY_ID_NOT_READY;
        this.id = DataConfig.ENTITIY_ID_NOT_READY;

//        DbHelper helper = new DbHelper(c);
//        this.db = helper.getWritableDatabase();
    }

    public Person(Cursor cursor){
        Log.v(tag, "create person from cursor");
        this.seq = cursor.getInt(cursor.getColumnIndex(DataConfig.COLUMN_PERSON_SEQ));
        this.id = cursor.getInt(cursor.getColumnIndex(DataConfig.COLUMN_PERSON_ID));
        this.name = cursor.getString(cursor.getColumnIndex(DataConfig.COLUMN_PERSON_NAME));
        this.email = cursor.getString(cursor.getColumnIndex(DataConfig.COLUMN_PERSON_EMAIL));
        this.phone = cursor.getString(cursor.getColumnIndex(DataConfig.COLUMN_PERSON_PHONE));
        this.wechatid = cursor.getString(cursor.getColumnIndex(DataConfig.COLUMN_PERSON_WECHATID));
        this.facebookid = cursor.getString(cursor.getColumnIndex(DataConfig.COLUMN_PERSON_FACEBOOKID));

        Log.v(tag, "created person from cursor: " + toString());
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
                '}';
    }
}
