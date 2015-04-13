package com.soco.SoCoClient.control.config;

public class DataConfig {

    // Database
//    public static String TABLE_PROGRAM = "program";
//    public static String COLUMN_PID = "pid";
//    public static String COLUMN_PNAME = "pname";
//    public static String COLUMN_PDATE = "pdate";
//    public static String COLUMN_PTIME = "ptime";
//    public static String COLUMN_PPLACE = "pplace";
//    public static String COLUMN_PCOMPLETE = "pcomplete";
//    public static String COLUMN_PDESC = "pdesc";
//    public static String COLUMN_PPHONE = "pphone";
//    public static String COLUMN_PEMAIL = "pemail";
//    public static String COLUMN_PWECHAT = "pwechat";

    //update: 20150206
    public static String TABLE_ACTIVITY = "activity";
    public static String COLUMN_ACTIVITY_ID = "id";
    public static String COLUMN_ACTIVITY_NAME = "name";
    public static String COLUMN_ACTIVITY_TAG = "tag";
    public static String COLUMN_ACTIVITY_CREATE_TIMESTAMP = "create_timestamp";
    public static String COLUMN_ACTIVITY_UPDATE_TIMESTAMP = "update_timestamp";
    public static String COLUMN_ACTIVITY_SIGNATURE = "signature";
    public static String COLUMN_ACTIVITY_ACTIVE = "active";
    //update: 20150402
    public static String COLUMN_ACTIVITY_ID_ONSERVER = "id_onserver";

    public static String VALUE_ACTIVITY_ACTIVE = "active";
    public static String VALUE_ACTIVITY_INACTIVE = "inactive";

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
//    public static String ATTRIBUTE_NAME_PLACE = "place";
    public static String ATTRIBUTE_NAME_DESC = "desc";
    public static String ATTRIBUTE_NAME_PHONE = "phone";
    public static String ATTRIBUTE_NAME_EMAIL = "email";
    public static String ATTRIBUTE_NAME_FILE_REMOTE_PATH = "file_remote_path";
    public static String ATTRIBUTE_NAME_LOCLAT = "loclat";
    public static String ATTRIBUTE_NAME_LOCLNG = "loclng";
    public static String ATTRIBUTE_NAME_LOCZOOM = "loczoom";
    public static String ATTRIBUTE_NAME_LOCNAME = "locname";
    public static String ATTRIBUTE_NAME_LOC_PREFIX = "loc";
    public static String ATTRIBUTE_NAME_TAG = "tag";


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

    public static String DEFAULT_LOCATION_LAT = "22.36757894430302";    //HONG KONG
    public static String DEFAULT_LOCATION_LNG = "114.12860479205847";
    public static String DEFAULT_LOCATION_ZOOM = "10.7580385";

    public static int INT_INDEX_1 = 1;
    public static String TYPE_STRING = "String";

    public static final String DEFAULT_PROJECT_TYPE = "activity";

}
