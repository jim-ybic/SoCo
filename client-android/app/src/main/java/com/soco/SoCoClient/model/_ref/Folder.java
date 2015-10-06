package com.soco.SoCoClient.model._ref;


import android.database.Cursor;
import android.util.Log;

import com.soco.SoCoClient.control._ref.DataConfigV1;
import com.soco.SoCoClient.control._ref.GeneralConfigV1;

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
        this.ftag = GeneralConfigV1.DEFAULT_TAG;
        this.fpath = path;
    }

    public Folder(Cursor c) {
        this.fid = c.getInt(c.getColumnIndex(DataConfigV1.COLUMN_FOLDER_ID));
        this.fname = c.getString(c.getColumnIndex(DataConfigV1.COLUMN_FOLDER_NAME));
        this.fdesc = c.getString(c.getColumnIndex(DataConfigV1.COLUMN_FOLDER_DESC));
        this.ftag = c.getString(c.getColumnIndex(DataConfigV1.COLUMN_FOLDER_TAG));
        this.fpath = c.getString(c.getColumnIndex(DataConfigV1.COLUMN_FOLDER_PATH));
    }

}
