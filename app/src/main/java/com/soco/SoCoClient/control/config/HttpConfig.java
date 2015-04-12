package com.soco.SoCoClient.control.config;

public class HttpConfig {

    public static String HTTP_TYPE_LOGIN = "login";
    public static String HTTP_TYPE_REGISTER = "register";
    public static String HTTP_TYPE_CREATE_PROJECT = "create_project";
    public static String HTTP_TYPE_ARCHIVE_PROJECT = "archive_project";
    public static String HTTP_TYPE_UPDATE_PROJECT_NAME = "update_project_name";
    public static String HTTP_TYPE_SET_PROJECT_ATTRIBUTE = "set_project_attribute";
    public static String HTTP_TYPE_HEART_BEAT = "heartbeat";
    public static String HTTP_TYPE_INVITE_PROJECT_MEMBER = "invite_project_member";

    public static String JSON_KEY_USERNAME = "username";
    public static String JSON_KEY_EMAIL = "email";
    public static String JSON_KEY_PASSWORD = "password";
    public static String JSON_KEY_PASSWORD2 = "password2";
    public static String JSON_KEY_ACCESS_TOKEN = "access_token";
    public static String JSON_KEY_PROJECT_NAME = "name";
    public static String JSON_KEY_PROJECT_TYPE = "type";
    public static String JSON_KEY_PROJECT_TAG = "tag";
    public static String JSON_KEY_PROJECT_SIGNATURE = "signature";
    public static String JSON_KEY_PROJECT_ID = "activity";
    public static String JSON_KEY_PROJECT_ID_ONSERVER = "id";

    public static String JSON_KEY_ATTRIBUTE_NAME = "name";
    public static String JSON_KEY_ATTRIBUTE_INDEX = "index";
    public static String JSON_KEY_ATTRIBUTE_TYPE = "type";
    public static String JSON_KEY_ATTRIBUTE_VALUE = "value";

    public static String JSON_KEY_RESPONSE_STATUS = "status";
    public static String JSON_VALUE_RESPONSE_STATUS_SUCCESS = "success";

    public static String SERVER_PATH_REGISTER = "/socoserver/register/register";
    public static String SERVER_PATH_LOGIN = "/socoserver/api/login";
    public static String SERVER_PATH_CREATE_PROJECT = "/socoserver/mobile/createActivity?";
    public static String SERVER_PATH_ARCHIVE_PROJECT = "/socoserver/mobile/archieveActivity?";
    public static String SERVER_PATH_RENAME_PROJECT = "/socoserver/mobile/updateActivity?";
    public static String SERVER_PATH_SET_PROJECT_ATTRIBUTE =
            "/socoserver/mobile/addUpdateAttributeByActivityID?";
    public static String SERVER_PATH_HEARTBEAT = "/socoserver/mobile/HeartBeat?";
    public static String SERVER_PATH_INVITE_PROJECT_MEMBER =
            "/socoserver/mobile/inviteFriendShareActivity?";


    public static String HTTP_TOKEN_TYPE = "access_token";
    public static String KEYWORD_REGISTRATION_SUBMITTED = "Your account registration email";
}
