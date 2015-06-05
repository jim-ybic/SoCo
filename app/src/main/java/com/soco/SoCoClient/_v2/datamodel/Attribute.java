package com.soco.SoCoClient._v2.datamodel;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.soco.SoCoClient._v2.businesslogic.config.DbConfig;
import com.soco.SoCoClient._v2.businesslogic.database.DbHelper;

public class Attribute {

    String tag = "Attribute";

    //fields saved to db
    int attrIdLocal;
    int attrIdServer;
    String activityType;
    int activityIdLocal;
    int activityIdServer;
    String attrName;
    String attrValue;

    //fields saved to db
    SQLiteDatabase db;

    public Attribute(Context context,
                     String activityType, int activityIdLocal, int activityIdServer){
        this.activityType = activityType;
        this.activityIdLocal = activityIdLocal;
        this.activityIdServer = activityIdServer;

        this.attrIdLocal = DbConfig.ENTITIY_ID_NOT_READY;
        this.attrIdServer = DbConfig.ENTITIY_ID_NOT_READY;

        DbHelper dbHelper= new DbHelper(context);
        this.db = dbHelper.getWritableDatabase();
    }

    public Attribute(Cursor cursor){
        Log.v(tag, "create attribute from cursor");
        this.attrIdLocal = cursor.getInt(cursor.getColumnIndex(DbConfig.COLUMN_ATTRIBUTE_ATTRIDLOCAL));
        this.attrIdServer = cursor.getInt(cursor.getColumnIndex(DbConfig.COLUMN_ATTRIBUTE_ATTRIDSERVER));
        this.activityType = cursor.getString(cursor.getColumnIndex(DbConfig.COLUMN_ATTRIBUTE_ACTIVITYTYPE));
        this.activityIdLocal = cursor.getInt(cursor.getColumnIndex(DbConfig.COLUMN_ATTRIBUTE_ACTIVITYIDLOCAL));
        this.activityIdServer = cursor.getInt(cursor.getColumnIndex(DbConfig.COLUMN_ATTRIBUTE_ACTIVITYIDSERVER));
        this.attrName = cursor.getString(cursor.getColumnIndex(DbConfig.COLUMN_ATTRIBUTE_ATTRNAME));
        this.attrValue = cursor.getString(cursor.getColumnIndex(DbConfig.COLUMN_ATTRIBUTE_ATTRVALUE));
        Log.d(tag, "created attribute from cursor: " + toString());
    }

    public void save(){
        if(attrIdLocal == DbConfig.ENTITIY_ID_NOT_READY){
            Log.v(tag, "save new attribute");
            saveNew();
        }else{
            Log.v(tag, "update existing attribute");
            update();
        }
    }

    void saveNew(){
        Log.v(tag, "save new attribute into database");
        try{
            db.beginTransaction();
            ContentValues cv = new ContentValues();
            cv.put(DbConfig.COLUMN_ATTRIBUTE_ATTRIDSERVER, attrIdServer);
            cv.put(DbConfig.COLUMN_ATTRIBUTE_ACTIVITYTYPE, activityType);
            cv.put(DbConfig.COLUMN_ATTRIBUTE_ACTIVITYIDLOCAL, activityIdLocal);
            cv.put(DbConfig.COLUMN_ATTRIBUTE_ACTIVITYIDSERVER, activityIdServer);
            cv.put(DbConfig.COLUMN_ATTRIBUTE_ATTRNAME, attrName);
            cv.put(DbConfig.COLUMN_ATTRIBUTE_ATTRVALUE, attrValue);
            db.insert(DbConfig.TABLE_ATTRIBUTE, null, cv);
            db.setTransactionSuccessful();
            Log.d(tag, "new attribute inserted into database: " + toString());
        }finally {
            db.endTransaction();
        }

        Log.v(tag, "task attribute id local from database");
        int aidLocal = -1;
        String query = "select max (" + DbConfig.COLUMN_ATTRIBUTE_ACTIVITYIDLOCAL
                + ") from " + DbConfig.TABLE_ATTRIBUTE;
        Cursor cursor = db.rawQuery(query, null);
        if(cursor.moveToFirst()) {
            aidLocal = cursor.getInt(0);
            Log.d(tag, "update attribute id local: " + aidLocal);
            attrIdLocal = aidLocal;
        }

        //todo: save new attribute to server
    }

    void update(){
        Log.v(tag, "update existing attribute to local database");
        try{
            db.beginTransaction();
            ContentValues cv = new ContentValues();
            cv.put(DbConfig.COLUMN_ATTRIBUTE_ATTRIDLOCAL, attrIdLocal);
            cv.put(DbConfig.COLUMN_ATTRIBUTE_ATTRIDSERVER, attrIdServer);
            cv.put(DbConfig.COLUMN_ATTRIBUTE_ACTIVITYTYPE, activityType);
            cv.put(DbConfig.COLUMN_ATTRIBUTE_ACTIVITYIDLOCAL, activityIdLocal);
            cv.put(DbConfig.COLUMN_ATTRIBUTE_ACTIVITYIDSERVER, activityIdServer);
            cv.put(DbConfig.COLUMN_ATTRIBUTE_ATTRNAME, attrName);
            cv.put(DbConfig.COLUMN_ATTRIBUTE_ATTRVALUE, attrValue);
            db.update(DbConfig.TABLE_ATTRIBUTE, cv,
                    DbConfig.COLUMN_ATTRIBUTE_ATTRIDLOCAL + " = ?",
                    new String[]{String.valueOf(attrIdLocal)});
            db.setTransactionSuccessful();
            Log.d(tag, "attribute updated into database: " + toString());
        }finally {
            db.endTransaction();
        }

        //todo: update attribute to server
    }

    public void delete(){
        Log.v(tag, "delete existing task");
        if(attrIdLocal == DbConfig.ENTITIY_ID_NOT_READY){
            Log.e(tag, "cannot delete a non-existing attribute");
        }else{
            db.delete(DbConfig.TABLE_ATTRIBUTE,
                    DbConfig.COLUMN_ATTRIBUTE_ACTIVITYIDLOCAL + " = ?",
                    new String[]{String.valueOf(attrIdLocal)});
            Log.d(tag, "attribute deleted from database: " + toString());
        }
    }

    public String toString() {
        return "Attribute{" +
                "attrIdLocal=" + attrIdLocal +
                ", attrIdServer=" + attrIdServer +
                ", activityType='" + activityType + '\'' +
                ", activityIdLocal=" + activityIdLocal +
                ", activityIdServer=" + activityIdServer +
                ", attrName='" + attrName + '\'' +
                ", attrValue='" + attrValue + '\'' +
                '}';
    }


    public int getAttrIdLocal() {
        return attrIdLocal;
    }

    public void setAttrIdLocal(int attrIdLocal) {
        this.attrIdLocal = attrIdLocal;
    }

    public int getAttrIdServer() {
        return attrIdServer;
    }

    public void setAttrIdServer(int attrIdServer) {
        this.attrIdServer = attrIdServer;
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

    public String getAttrName() {
        return attrName;
    }

    public void setAttrName(String attrName) {
        this.attrName = attrName;
    }

    public String getAttrValue() {
        return attrValue;
    }

    public void setAttrValue(String attrValue) {
        this.attrValue = attrValue;
    }


}
