package com.soco.SoCoClient.common.model._ref;


import android.database.Cursor;
import android.util.Log;

import com.soco.SoCoClient._ref.DataConfigV1;
import com.soco.SoCoClient._ref.GeneralConfigV1;
import com.soco.SoCoClient.common.util.SignatureUtil;

@Deprecated
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
                + ", tag: " + GeneralConfigV1.DEFAULT_TAG
                + ", path: " + path);

        this.pname = name;
        this.ptag = GeneralConfigV1.DEFAULT_TAG;
        this.path = path;
        this.pupdate_timestamp = SignatureUtil.now();
        this.invitation_status = 1;
    }

    public Activity(Cursor c) {
        this.pid = c.getInt(c.getColumnIndex(DataConfigV1.COLUMN_ACTIVITY_ID));
        this.pname = c.getString(c.getColumnIndex(DataConfigV1.COLUMN_ACTIVITY_NAME));
        this.ptag = c.getString(c.getColumnIndex(DataConfigV1.COLUMN_ACTIVITY_TAG));
        this.pcreate_timestamp = c.getString(c.getColumnIndex(
                DataConfigV1.COLUMN_ACTIVITY_CREATE_TIMESTAMP));
        this.pupdate_timestamp = c.getString(c.getColumnIndex(
                DataConfigV1.COLUMN_ACTIVITY_UPDATE_TIMESTAMP));
        this.psignature = c.getString(c.getColumnIndex(DataConfigV1.COLUMN_ACTIVITY_SIGNATURE));
        this.pactive = c.getString(c.getColumnIndex(DataConfigV1.COLUMN_ACTIVITY_ACTIVE));
        this.pid_onserver = c.getString(c.getColumnIndex(DataConfigV1.COLUMN_ACTIVITY_ID_ONSERVER));
        this.invitation_status = c.getInt(c.getColumnIndex(DataConfigV1.COLUMN_ACTIVITY_INVITATION_STATUS));
        this.ptag = c.getString(c.getColumnIndex(DataConfigV1.COLUMN_ACTIVITY_TAG));;
        this.path = c.getString(c.getColumnIndex(DataConfigV1.COLUMN_ACTIVITY_PATH));;
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
        return DataConfigV1.DEFAULT_PROJECT_TYPE;
    }
}
