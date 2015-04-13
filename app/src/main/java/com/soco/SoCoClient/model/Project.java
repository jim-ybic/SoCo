package com.soco.SoCoClient.model;


import android.database.Cursor;

import com.soco.SoCoClient.control.config.DataConfig;
import com.soco.SoCoClient.control.util.SignatureUtil;

public class Project {
    public int pid;
    public String pname;
    public String ptag;
    public String pcreate_timestamp;
    public String pupdate_timestamp;
    public String psignature;
    public String pactive;
    public String pid_onserver;

    static String DEFAULT_PROJECT_TAG = "default";
    static String tag = "Project";

    public Project(String name) {
        this.pname = name;

        this.ptag = DEFAULT_PROJECT_TAG;
        this.pupdate_timestamp = SignatureUtil.now();
    }

    public Project(Cursor c) {
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

        this.ptag = DEFAULT_PROJECT_TAG;
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
