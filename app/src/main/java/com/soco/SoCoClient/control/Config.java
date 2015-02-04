package com.soco.SoCoClient.control;

public class Config {

    // Program details
    public static String PROGRAM_PNAME = "PROGRAM_PNAME";
    public static String PROGRAM_PINFO = "PROGRAM_PINFO";
    public static int PROGRAM_COMPLETED = 1;
    public static int PROGRAM_ACTIVE = 0;

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

    // Local constants
    public static String LOGIN_EMAIL = "email";
    public static String LOGIN_PASSWORD = "password";

    // Config file
    public static String PROFILE_FILENAME = "SoCo.config";
    public static String PROFILE_EMAIL = "email";
    public static String PROFILE_NICKNAME = "nickname";
    public static String PROFILE_PHONE = "phone";
    public static String PROFILE_WECHAT = "wechat";
    public static String PROFILE_PNAME = "pname";
    public static String PROFILE_LOGIN_ACCESS_TOKEN = "access_token";
}
