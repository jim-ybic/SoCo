package com.soco.SoCoClient.obsolete.v1.model;


import android.database.Cursor;
import android.util.Log;

import com.soco.SoCoClient.obsolete.v1.control.config.DataConfigObs;
import com.soco.SoCoClient.obsolete.v1.control.config.GeneralConfig;

public class Folder {
    public int fid;
    public String fname;
    public String fdesc;
    public String ftag;
    public String fpath;

    static String tag = "Folder";

    public Folder(String name, String desc, String path) {
        Log.d(tag, "create new folder: " + name + ", desc: " + desc + ", path: " + path);
        this.fname = name;
        this.fdesc = desc;
        this.ftag = GeneralConfig.DEFAULT_TAG;
        this.fpath = path;
    }

    public Folder(Cursor c) {
        this.fid = c.getInt(c.getColumnIndex(DataConfigObs.COLUMN_FOLDER_ID));
        this.fname = c.getString(c.getColumnIndex(DataConfigObs.COLUMN_FOLDER_NAME));
        this.fdesc = c.getString(c.getColumnIndex(DataConfigObs.COLUMN_FOLDER_DESC));
        this.ftag = c.getString(c.getColumnIndex(DataConfigObs.COLUMN_FOLDER_TAG));
        this.fpath = c.getString(c.getColumnIndex(DataConfigObs.COLUMN_FOLDER_PATH));
    }

}
