package com.soco.SoCoClient.v1.model;


import android.database.Cursor;
import android.util.Log;

import com.soco.SoCoClient.v1.control.config.DataConfig;
import com.soco.SoCoClient.v1.control.config.GeneralConfig;
import com.soco.SoCoClient.v1.control.util.SignatureUtil;

public class Activity {
    public int pid;
    public String pname;
    public String ptag;
    public String pcreate_timestamp;
    public String pupdate_timestamp;
    public String psignature;
    public String pactive;
    public String pid_onserver;
    public int invitation_status;
    public String path;

    static String tag = "Project";

    public Activity(String name, String path) {
        Log.d(tag, "create new project: " + name
                + ", tag: " + GeneralConfig.DEFAULT_TAG
                + ", path: " + path);

        this.pname = name;
        this.ptag = GeneralConfig.DEFAULT_TAG;
        this.path = path;
        this.pupdate_timestamp = SignatureUtil.now();
        this.invitation_status = 1;
    }

    public Activity(Cursor c) {
        this.pid = c.getInt(c.getColumnIndex(DataConfig.COLUMN_ACTIVITY_ID));
        this.pname = c.getString(c.getColumnIndex(DataConfig.COLUMN_ACTIVITY_NAME));
        this.ptag = c.getString(c.getColumnIndex(DataConfig.COLUMN_ACTIVITY_TAG));
        this.pcreate_timestamp = c.getString(c.getColumnIndex(
                DataConfig.COLUMN_ACTIVITY_CREATE_TIMESTAMP));
        this.pupdate_timestamp = c.getString(c.getColumnIndex(
                DataConfig.COLUMN_ACTIVITY_UPDATE_TIMESTAMP));
        this.psignature = c.getString(c.getColumnIndex(DataConfig.COLUMN_ACTIVITY_SIGNATURE));
        this.pactive = c.getString(c.getColumnIndex(DataConfig.COLUMN_ACTIVITY_ACTIVE));
        this.pid_onserver = c.getString(c.getColumnIndex(DataConfig.COLUMN_ACTIVITY_ID_ONSERVER));
        this.invitation_status = c.getInt(c.getColumnIndex(DataConfig.COLUMN_ACTIVITY_INVITATION_STATUS));
        this.ptag = c.getString(c.getColumnIndex(DataConfig.COLUMN_ACTIVITY_TAG));;
        this.path = c.getString(c.getColumnIndex(DataConfig.COLUMN_ACTIVITY_PATH));;
    }

    public String getMoreInfo() {
        return "";
    }

    public String getSignature(){
        return pupdate_timestamp;
    }

    public String getTag(){
        return ptag;
    }

    public String getType(){
        return DataConfig.DEFAULT_PROJECT_TYPE;
    }
}