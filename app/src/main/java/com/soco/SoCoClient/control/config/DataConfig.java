package com.soco.SoCoClient.control.config;

public class DataConfig {

    public static String TEST_NO_NAME = "TEST NO NAME";

    public static String TABLE_ACTIVITY = "activity";
    public static String COLUMN_ACTIVITY_ID = "aid";
    public static String COLUMN_ACTIVITY_NAME = "aname";
    public static String COLUMN_ACTIVITY_TAG = "tag";
    public static String COLUMN_ACTIVITY_CREATE_TIMESTAMP = "create_timestamp";
    public static String COLUMN_ACTIVITY_UPDATE_TIMESTAMP = "update_timestamp";
    public static String COLUMN_ACTIVITY_SIGNATURE = "signature";
    public static String COLUMN_ACTIVITY_ACTIVE = "active";
    public static String COLUMN_ACTIVITY_ID_ONSERVER = "id_onserver";
    public static String COLUMN_ACTIVITY_INVITATION_STATUS = "invitation_status";
    public static String COLUMN_ACTIVITY_PATH = "activity_path";

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
    public static String ATTRIBUTE_NAME_DESC = "desc";
    public static String ATTRIBUTE_NAME_PHONE = "phone";
    public static String ATTRIBUTE_NAME_EMAIL = "email";
    public static String ATTRIBUTE_NAME_FILE_REMOTE_PATH = "file_remote_path";
    public static String ATTRIBUTE_NAME_LOCLAT = "loclat";
    public static String ATTRIBUTE_NAME_LOCLNG = "loclng";
    public static String ATTRIBUTE_NAME_LOCZOOM = "loczoom";
    public static String ATTRIBUTE_NAME_LOCNAME = "locname";

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

    public static String TYPE_STRING = "String";

    public static String DEFAULT_PROJECT_TYPE = "activity";

    public static String TABLE_ACTIVITY_MEMBER = "activity_member";
    public static String COLUMN_ACTIVITY_MEMBER_AID = "aid";
    public static String COLUMN_ACTIVITY_MEMBER_MEMBER_EMAIL = "email";
    public static String COLUMN_ACTIVITY_MEMBER_MEMBER_USERNAME = "username";
    public static String COLUMN_ACTIVITY_MEMBER_MEMBER_NICKNAME = "nickname";
    public static String COLUMN_ACTIVITY_MEMBER_MEMBER_STATUS = "status";
    public static String COLUMN_ACTIVITY_MEMBER_MEMBER_JOIN_TIMESTAMP = "join_timestamp";

    public static String TABLE_ACTIVITY_UPDATES = "activity_update";
    public static String COLUMN_ACTIVITY_UPDATES_UID = "uid";
    public static String COLUMN_ACTIVITY_UPDATES_AID = "aid";
    public static String COLUMN_ACTIVITY_UPDATES_COMMENT = "content";
    public static String COLUMN_ACTIVITY_UPDATES_USER = "user";
    public static String COLUMN_ACTIVITY_UPDATES_TIMESTAMP = "timestamp";

    public static int UPDATE_INDEX_NAME = 0;
    public static int UPDATE_INDEX_COMMENT = 1;
    public static int UPDATE_INDEX_TIMESTAMP = 2;

    public static int CHAT_INDEX_CONTENT = 0;
    public static int CHAT_INDEX_TIMESTAMP = 1;
    public static int CHAT_INDEX_TYPE = 2;

    public static int CHAT_TYPE_SEND = 0;
    public static int CHAT_TYPE_RECEIVE = 1;

    public static int ACTIVITY_INVITATION_STATUS_COMPLETE = 1;
    public static int ACTIVITY_INVITATION_STATUS_INCOMPLETE = 0;

    public static String TABLE_CONTACT = "contact";
    public static String COLUMN_CONTACT_ID = "contact_id";
    public static String COLUMN_CONTACT_EMAIL = "contact_email";
    public static String COLUMN_CONTACT_USERNAME = "contact_username";
    public static String COLUMN_CONTACT_NICKNAME = "contact_nickname";
    public static String COLUMN_CONTACT_PHONE = "contact_phone";
    public static String COLUMN_CONTACT_ID_ONSERVER = "contact_id_onserver";

    public static String TABLE_CHAT = "chat";
    public static String COLUMN_CHAT_ID = "chat_id";
    public static String COLUMN_CHAT_CONTACT_ID = "chat_contact_id";
    public static String COLUMN_CHAT_CONTENT = "chat_content";
    public static String COLUMN_CHAT_TIMESTAMP = "chat_timestamp";
    public static String COLUMN_CHAT_TYPE = "chat_type";

    public static String TABLE_FOLDER = "folder";
    public static String COLUMN_FOLDER_ID = "folder_id";
    public static String COLUMN_FOLDER_NAME = "folder_name";
    public static String COLUMN_FOLDER_DESC = "folder_desc";
    public static String COLUMN_FOLDER_PATH = "folder_path";


}
