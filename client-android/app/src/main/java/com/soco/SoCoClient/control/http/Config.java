package com.soco.SoCoClient.control.http;

public class Config {

    public static final String PROFILE_SERVER_IP = "server_ip";
    public static final String PROFILE_SERVER_PORT = "server_port";
    public static final String PROFILE_LOGIN_ACCESS_TOKEN = "access_token";

    public static final String SERVER_PATH_CREATE_TASK = "/socoserver/mobile/createActivity";
    public static final String SERVER_PATH_CREATE_CONTACT = "/socoserver/mobile/addFriend";
    public static final String SERVER_PATH_QUERY_CONTACT_DETAIL = "/socoserver/mobile/queryUserByEmailOrUsername";
    public static final String SERVER_PATH_SEND_MESSAGE = "/socoserver/exchange/sendOut";
    public static final String SERVER_PATH_RECEIVE_MESSAGE = "/socoserver/exchange/receiveMsg";
    public static final String SERVER_PATH_ACK_RECEIVE_MESSAGE = "/socoserver/exchange/ackReceivedMsg";
    public static final String SERVER_PATH_HEARTBEAT = "/socoserver/mobile/HeartBeat";
    public static final String SERVER_PATH_INVITE_CONTACT_JOIN_TASK = "/socoserver/mobile/inviteFriendShareActivity";

    public static final String SERVER_PATH_REGISTER = "/socoserver/register/register";
    public static final String SERVER_PATH_LOGIN = "/socoserver/api/login";
    public static final String SERVER_PATH_CREATE_PROJECT = "/socoserver/mobile/createActivity";
    public static final String SERVER_PATH_ARCHIVE_PROJECT = "/socoserver/mobile/archiveActivity";
    public static final String SERVER_PATH_RENAME_PROJECT = "/socoserver/mobile/updateActivity";
    public static final String SERVER_PATH_SET_PROJECT_ATTRIBUTE = "/socoserver/mobile/addAttributeByActivityID";
    public static final String SERVER_PATH_JOIN_TASK_BY_INVITE = "/socoserver/mobile/joinActivityByInvite";
    public static final String SERVER_PATH_ADD_FRIEND = "/socoserver/mobile/addFriend";
    public static final String SERVER_PATH_QUERY_USER = "/socoserver/mobile/queryUserByEmailOrUsername";
    public static final String SERVER_PATH_ADD_FILE_TO_ACTIVITY = "/socoserver/mobile/addFileToActivity";
    public static final String SERVER_PATH_GET_ACTIVITY_EVENT = "/socoserver/mobile/getActivityEvent";


    public static final String JSON_KEY_USERNAME = "username";
    public static final String JSON_KEY_USER = "user";
    public static final String JSON_KEY_EMAIL = "email";
    public static final String JSON_KEY_PASSWORD = "password";
    public static final String JSON_KEY_PASSWORD2 = "password2";
    public static final String JSON_KEY_ACCESS_TOKEN = "access_token";
    public static final String JSON_KEY_NAME = "name";
    public static final String JSON_KEY_TYPE = "type";
    public static final String JSON_KEY_TAG = "tag";
    public static final String JSON_KEY_ACTIVITY = "activity";
    public static final String JSON_KEY_ID = "id";
    public static final String JSON_KEY_INVITATION = "invitation";
    public static final String JSON_KEY_ATTRIBUTES = "attributes";
    public static final String JSON_KEY_INVITER = "inviter";
    public static final String JSON_KEY_DATE = "date";
    public static final String JSON_KEY_MESSAGE = "message";
    public static final String JSON_KEY_ACK = "ack";
    public static final String JSON_KEY_SIGNATURE = "signature";
    public static final String JSON_KEY_FILE_NAME = "file_name";
    public static final String JSON_KEY_URI = "uri";
    public static final String JSON_KEY_REMOTE_PATH = "remote_path";
    public static final String JSON_KEY_LOCAL_PATH = "local_path";
    public static final String JSON_KEY_ACTIVITY_EVENT = "activity_event";
    public static final String JSON_KEY_STATUS = "status";
    public static final String JSON_KEY_FROM_TYPE = "from_type";
    public static final String JSON_KEY_FROM_ID = "from_id";
    public static final String JSON_KEY_TO_TYPE = "to_type";
    public static final String JSON_KEY_TO_ID = "to_id";
    public static final String JSON_KEY_SEND_DATE_TIME = "send_date_time";
    public static final String JSON_KEY_FROM_DEVICE = "from_device";
    public static final String JSON_KEY_CONTENT_TYPE = "context_type";
    public static final String JSON_KEY_CONTENT = "context";
    public static final String JSON_KEY_VALUE = "value";

    public static final String JSON_VALUE_SUCCESS = "success";


    public static final String DEFAULT_SERVER_IP = "192.168.8.73";
    public static final String DEFAULT_SERVER_PORT = "8080";
}
