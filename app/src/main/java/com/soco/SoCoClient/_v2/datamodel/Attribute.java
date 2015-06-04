package com.soco.SoCoClient._v2.datamodel;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.soco.SoCoClient._v2.businesslogic.config.DbConfig;
import com.soco.SoCoClient._v2.businesslogic.database.DbHelper;

public class Attribute {

    //fields saved to db
    int attrIdLocal;
    int attrIdServer;
    String activityType;
    int activityIdLocal;
    int activityIdServer;
    String attrName;
    String attrValue;
    String attrLastUpdateUser;
    String attrLastUpdateTimestamp;

    //fields saved to db
    SQLiteDatabase db;
    boolean isUpdated;
    String action;      //new, update, delete

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

    public void save(){}



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

    public String getAttrLastUpdateUser() {
        return attrLastUpdateUser;
    }

    public void setAttrLastUpdateUser(String attrLastUpdateUser) {
        this.attrLastUpdateUser = attrLastUpdateUser;
    }

    public String getAttrLastUpdateTimestamp() {
        return attrLastUpdateTimestamp;
    }

    public void setAttrLastUpdateTimestamp(String attrLastUpdateTimestamp) {
        this.attrLastUpdateTimestamp = attrLastUpdateTimestamp;
    }
}
