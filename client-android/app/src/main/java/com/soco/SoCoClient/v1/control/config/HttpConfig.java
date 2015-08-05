package com.soco.SoCoClient.v1.control.config;

public class HttpConfig {

    public static String TEST_NO_INVITER_EMAIL = "NO EMAIL RECEIVED FROM SERVER";
    public static String TEST_UNKNOWN_USERNAME = "UNKNOWN USERNAME";

    public static String JSON_KEY_USERNAME = "username";
    public static String JSON_KEY_EMAIL = "email";
    public static String JSON_KEY_PASSWORD = "password";
    public static String JSON_KEY_PASSWORD2 = "password2";
    public static String JSON_KEY_ACCESS_TOKEN = "access_token";
    public static String JSON_KEY_NAME = "name";
    public static String JSON_KEY_PROJECT_TYPE = "type";
    public static String JSON_KEY_PROJECT_TAG = "tag";
    public static String JSON_KEY_PROJECT_SIGNATURE = "signature";
    public static String JSON_KEY_PROJECT_ID = "activity";
    public static String JSON_KEY_PROJECT_ID_ONSERVER = "id";
    public static String JSON_KEY_INVITATION = "invitation";
    public static String JSON_KEY_ATTRIBUTES = "attributes";
    public static String JSON_KEY_PROJECT = "activity";
    public static String JSON_KEY_INVITER = "inviter";
    public static String JSON_KEY_DATE = "date";
    public static String JSON_KEY_MESSAGE = "message";
    public static String JSON_KEY_ACK = "ack";
    public static String JSON_KEY_SIGNATURE = "signature";
    public static String JSON_KEY_FILE_NAME = "file_name";
    public static String JSON_KEY_URI = "uri";
    public static String JSON_KEY_REMOTE_PATH = "remote_path";
    public static String JSON_KEY_LOCAL_PATH = "local_path";
    public static final String JSON_KEY_ACTIVITY_EVENT = "activity_event";

    public static String JSON_KEY_ATTRIBUTE_NAME = "name";
//    public static String JSON_KEY_ATTRIBUTE_INDEX = "index";
    public static String JSON_KEY_ATTRIBUTE_TYPE = "type";
    public static String JSON_KEY_ATTRIBUTE_VALUE = "value";

    public static String JSON_KEY_FROM_TYPE = "from_type";
    public static String JSON_KEY_FROM_ID = "from_id";
    public static String JSON_KEY_TO_TYPE = "to_type";
    public static String JSON_KEY_TO_ID = "to_id";
    public static String JSON_KEY_SEND_DATE_TIME = "send_date_time";
    public static String JSON_KEY_FROM_DEVICE = "from_device";
    public static String JSON_KEY_CONTENT_TYPE = "context_type";
    public static String JSON_KEY_CONTENT = "context";
    public static String JSON_KEY_USER = "user";
    public static String JSON_KEY_ID = "id";

    public static String JSON_KEY_RESPONSE_STATUS = "status";
    public static String JSON_VALUE_RESPONSE_STATUS_SUCCESS = "success";

    public static String SERVER_PATH_REGISTER = "/socoserver/register/register";
    public static String SERVER_PATH_LOGIN = "/socoserver/api/login";
    public static String SERVER_PATH_CREATE_PROJECT = "/socoserver/mobile/createActivity?";
    public static String SERVER_PATH_ARCHIVE_PROJECT = "/socoserver/mobile/archiveActivity?";
    public static String SERVER_PATH_RENAME_PROJECT = "/socoserver/mobile/updateActivity?";
    public static String SERVER_PATH_SET_PROJECT_ATTRIBUTE =
            "/socoserver/mobile/addAttributeByActivityID?";
    public static String SERVER_PATH_HEARTBEAT = "/socoserver/mobile/HeartBeat?";
    public static String SERVER_PATH_INVITE_PROJECT_MEMBER =
            "/socoserver/mobile/inviteFriendShareActivity?";
    public static String SERVER_PATH_JOIN_PROJECT_BY_INVITE =
            "/socoserver/mobile/joinActivityByInvite?";
    public static String SERVER_PATH_SEND_MESSAGE = "/socoserver/exchange/sendOut?";
    public static String SERVER_PATH_RECEIVE_MESSAGE = "/socoserver/exchange/receiveMsg?";
    public static String SERVER_PATH_ACK_RECEIVE_MESSAGE = "/socoserver/exchange/ackReceivedMsg?";
    public static String SERVER_PATH_ADD_FRIEND = "/socoserver/mobile/addFriend?";
    public static String SERVER_PATH_QUERY_USER = "/socoserver/mobile/queryUserByEmailOrUsername?";
    public static final String SERVER_PATH_ADD_FILE_TO_ACTIVITY = "/socoserver/mobile/addFileToActivity?";
    public static final String SERVER_PATH_GET_ACTIVITY_EVENT = "/socoserver/mobile/getActivityEvent?";

    public static String HTTP_TOKEN_TYPE = "access_token";
    public static String KEYWORD_REGISTRATION_SUBMITTED = "Your account registration email";

    public static int MESSAGE_CONTENT_TYPE_1 = 1;
    public static int MESSAGE_FROM_TYPE_1 = 1;
    public static int MESSAGE_FROM_TYPE_2 = 2;
    public static int MESSAGE_TO_TYPE_1 = 1;
    public static int MESSAGE_TO_TYPE_2 = 2;
}
