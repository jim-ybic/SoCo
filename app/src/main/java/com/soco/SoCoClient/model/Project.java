package com.soco.SoCoClient.model;


import android.database.Cursor;

import com.soco.SoCoClient.control.config.DataConfig;

public class Project {
    public int pid;
    public String pname;
    public String ptag;
    public String pcreate_timestamp;
    public String pupdate_timestamp;
    public String psignature;
    public String pactive;

    public Project(String name) {
        this.pname = name;
    }

    public Project(Cursor c) {
        this.pid = c.getInt(c.getColumnIndex(DataConfig.COLUMN_PROJECT_ID));
        this.pname = c.getString(c.getColumnIndex(DataConfig.COLUMN_PROJECT_NAME));
        this.ptag = c.getString(c.getColumnIndex(DataConfig.COLUMN_PROJECT_TAG));
        this.pcreate_timestamp = c.getString(c.getColumnIndex(
                DataConfig.COLUMN_PROJECT_CREATE_TIMESTAMP));
        this.pupdate_timestamp = c.getString(c.getColumnIndex(
                DataConfig.COLUMN_PROJECT_UPDATE_TIMESTAMP));
        this.psignature = c.getString(c.getColumnIndex(DataConfig.COLUMN_PROJECT_SIGNATURE));
        this.pactive = c.getString(c.getColumnIndex(DataConfig.COLUMN_PROJECT_ACTIVE));
    }

    public String getMoreInfo() {
        return "More info coming soon";
    }

}
