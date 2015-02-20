package com.soco.SoCoClient.control.config;

public class DataConfig {

    // Database
    public static String TABLE_PROGRAM = "program";
    public static String COLUMN_PID = "pid";
    public static String COLUMN_PNAME = "pname";
    public static String COLUMN_PDATE = "pdate";
    public static String COLUMN_PTIME = "ptime";
    public static String COLUMN_PPLACE = "pplace";
    public static String COLUMN_PCOMPLETE = "pcomplete";
    public static String COLUMN_PDESC = "pdesc";
    public static String COLUMN_PPHONE = "pphone";
    public static String COLUMN_PEMAIL = "pemail";
    public static String COLUMN_PWECHAT = "pwechat";

    //update: 20150206
    public static String TABLE_PROJECT = "project";
    public static String COLUMN_PROJECT_ID = "pid";
    public static String COLUMN_PROJECT_NAME = "pname";
    public static String COLUMN_PROJECT_TAG = "ptag";
    public static String COLUMN_PROJECT_CREATE_TIMESTAMP = "pcreate_timestamp";
    public static String COLUMN_PROJECT_UPDATE_TIMESTAMP = "pupdate_timestamp";
    public static String COLUMN_PROJECT_SIGNATURE = "psignature";
    public static String COLUMN_PROJECT_ACTIVE = "pactive";

    public static String VALUE_PROJECT_ACTIVE = "active";
    public static String VALUE_PROJECT_INACTIVE = "inactive";

    public static String TABLE_ATTRIBUTE = "attribute";
    public static String COLUMN_ATTRIBUTE_ID = "aid";
    public static String COLUMN_ATTRIBUTE_PID = "pid";
    public static String COLUMN_ATTRIBUTE_NAME = "aname";
    public static String COLUMN_ATTRIBUTE_VALUE = "avalue";
    public static String COLUMN_ATTRIBUTE_USER = "auser";
    public static String COLUMN_ATTRIBUTE_CREATE_TIMESTAMP = "acreate_timestamp";
    public static String COLUMN_ATTRIBUTE_UPDATE_TIMESTAMP = "aupdate_timestamp";

    public static String ATTRIBUTE_NAME_DATE = "date";
    public static String ATTRIBUTE_NAME_TIME = "time";
    public static String ATTRIBUTE_NAME_PLACE = "place";
    public static String ATTRIBUTE_NAME_DESC = "desc";
    public static String ATTRIBUTE_NAME_PHONE = "phone";
    public static String ATTRIBUTE_NAME_EMAIL = "email";
    public static String ATTRIBUTE_NAME_FILE_REMOTE_PATH = "file_remote_path";

    public static String TABLE_SHARED_FILE = "shared_files";
    public static String COLUMN_SHARED_FILE_ID = "fid";
    public static String COLUMN_SHARED_FILE_PID = "pid";
    public static String COLUMN_SHARED_FILE_DISPLAY_NAME = "fdisplay_name";
    public static String COLUMN_SHARED_FILE_URI = "furi";
    public static String COLUMN_SHARED_FILE_REMOTE_PATH = "fremote_path";
    public static String COLUMN_SHARED_FILE_LOCAL_PATH = "flocal_path";
    public static String COLUMN_SHARED_FILE_USER = "fuser";
    public static String COLUMN_SHARED_FILE_CREATE_TIMESTAMP = "fcreate_timestamp";
    public static String COLUMN_SHARED_FILE_UPDATE_TIMESTAMP = "fupdate_timestamp";
}
