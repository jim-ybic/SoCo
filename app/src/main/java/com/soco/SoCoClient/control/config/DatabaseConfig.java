package com.soco.SoCoClient.control.config;

public class DatabaseConfig {

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

    public static String TABLE_ATTRIBUTE = "attribute";
    public static String COLUMN_ATTRIBUTE_ID = "aid";
    public static String COLUMN_ATTRIBUTE_PID = "pid";
    public static String COLUMN_ATTRIBUTE_NAME = "aname";
    public static String COLUMN_ATTRIBUTE_VALUE = "avalue";
    public static String COLUMN_ATTRIBUTE_USER = "auser";
    public static String COLUMN_ATTRIBUTE_CREATE_TIMESTAMP = "acreate_timestamp";
    public static String COLUMN_ATTRIBUTE_UPDATE_TIMESTAMP = "aupdate_timestamp";
}
