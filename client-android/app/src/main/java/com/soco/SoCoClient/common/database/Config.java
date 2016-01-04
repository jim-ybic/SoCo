package com.soco.SoCoClient.common.database;

public class Config {

    //database config
    public static final String TABLE_TASK = "task";
    public static final String COLUMN_TASK_TASKIDLOCAL = "taskIdLocal";
    public static final String COLUMN_TASK_TASKIDSERVER = "taskIdServer";
    public static final String COLUMN_TASK_TASKNAME = "taskName";
    public static final String COLUMN_TASK_TASKPATH = "taskPath";
    public static final String COLUMN_TASK_ISTASKACTIVE = "isTaskActive";

    public static final String TABLE_EVENT = "event";
    public static final String COLUMN_EVENT_SEQ = "seq";
    public static final String COLUMN_EVENT_ID = "id";    //global id
    public static final String COLUMN_EVENT_NAME = "name";
    public static final String COLUMN_EVENT_DESC = "desc";
    public static final String COLUMN_EVENT_DATE = "date";
    public static final String COLUMN_EVENT_TIME = "time";
    public static final String COLUMN_EVENT_LOCATION = "location";
    public static final String COLUMN_EVENT_ISDRAFT = "isdraft";
    public static final String COLUMN_EVENT_ISDONE = "isdone";

    public static final String TABLE_PERSON = "person";
    public static final String COLUMN_PERSON_SEQ = "seq";
    public static final String COLUMN_PERSON_ID = "id";
    public static final String COLUMN_PERSON_NAME = "name";
    public static final String COLUMN_PERSON_EMAIL = "email";
    public static final String COLUMN_PERSON_PHONE = "phone";
    public static final String COLUMN_PERSON_WECHATID = "wechatid";
    public static final String COLUMN_PERSON_FACEBOOKID = "facebookid";
    public static final String COLUMN_PERSON_STATUS = "status"; //invited, accepted, rejected
    public static final String COLUMN_PERSON_CATEGORY = "category"; //friend, phone contact

    public static final String TABLE_SINGLE_CONVERSATION = "single_conversation";
    public static final String COLUMN_SINGLE_CONVERSATION_SEQ = "seq";
    public static final String COLUMN_SINGLE_CONVERSATION_ID = "id";
    public static final String COLUMN_SINGLE_CONVERSATION_TITLE = "title";
    public static final String COLUMN_SINGLE_CONVERSATION_LASTMSGCONTENT = "lastMsgContent";
    public static final String COLUMN_SINGLE_CONVERSATION_LASTMSGTIMESTAMP = "lastMsgTimestamp";
    public static final String COLUMN_SINGLE_CONVERSATION_CREATEDBYUSERID = "createdByUserId";
    public static final String COLUMN_SINGLE_CONVERSATION_CREATEDTIMESTAMP = "createdTimestamp";
    public static final String COLUMN_SINGLE_CONVERSATION_COUNTERPARTYID = "counterpartyId";
    public static final String COLUMN_SINGLE_CONVERSATION_COUNTERPARTYNAME = "counterpartyName";

    public static final String TABLE_SINGLE_CONVERSATION_MESSAGE = "single_conversation_message";
    public static final String COLUMN_SINGLE_CONVERSATION_MESSAGE_SEQ = "seq";
    public static final String COLUMN_SINGLE_CONVERSATION_MESSAGE_CONSEQ = "conSeq";
    public static final String COLUMN_SINGLE_CONVERSATION_MESSAGE_MSGSEQ = "msgSeq";

    public static final String TABLE_PROJECT = "project";
    public static final String COLUMN_PROJECT_PROJECTIDLOCAL = "projectIdLocal";
    public static final String COLUMN_PROJECT_PROJECTIDSERVER = "projectIdServer";
    public static final String COLUMN_PROJECT_PROJECTNAME = "projectName";
    public static final String COLUMN_PROJECT_PROJECTPATH = "projectPath";
    public static final String COLUMN_PROJECT_ISPROJECTACTIVE = "isProjectActive";

    public static final String TABLE_ATTRIBUTE = "attribute";
    public static final String COLUMN_ATTRIBUTE_ATTRIDLOCAL = "attrIdLocal";
    public static final String COLUMN_ATTRIBUTE_ATTRIDSERVER = "attrIdServer";
    public static final String COLUMN_ATTRIBUTE_ACTIVITYTYPE = "activityType";
    public static final String COLUMN_ATTRIBUTE_ACTIVITYIDLOCAL = "activityIdLocal";
    public static final String COLUMN_ATTRIBUTE_ACTIVITYIDSERVER = "activityIdServer";
    public static final String COLUMN_ATTRIBUTE_ATTRNAME = "attrName";
    public static final String COLUMN_ATTRIBUTE_ATTRVALUE = "attrValue";

    public static final String TABLE_ATTACHMENT = "attachment";
    public static final String COLUMN_ATTACHMENT_ATTACHMENTIDLOCAL = "attachmentIdLocal";
    public static final String COLUMN_ATTACHMENT_ATTACHMENTIDSERVER = "attachmentIdServer";
    public static final String COLUMN_ATTACHMENT_ACTIVITYTYPE = "activityType";
    public static final String COLUMN_ATTACHMENT_ACTIVITYIDLOCAL = "activityIdLocal";
    public static final String COLUMN_ATTACHMENT_ACTIVITYIDSERVER = "activityIdServer";
    public static final String COLUMN_ATTACHMENT_DISPLAYNAME = "attachmentDisplayName";
    public static final String COLUMN_ATTACHMENT_URI = "attachmentUri";
    public static final String COLUMN_ATTACHMENT_REMOTEPATH = "attachmentRemotePath";
    public static final String COLUMN_ATTACHMENT_LOCALPATH = "attachmentLocalPath";
    public static final String COLUMN_ATTACHMENT_USER = "attachmentUser";
    public static final String COLUMN_ATTACHMENT_CREATETIMESTAMP = "attachmentCreateTimestamp";

    public static final String TABLE_CONTACT = "contact";
    public static final String COLUMN_CONTACT_CONTACTIDLOCAL = "contactIdLocal";
    public static final String COLUMN_CONTACT_CONTACTIDSERVER = "contactIdServer";
    public static final String COLUMN_CONTACT_CONTACTEMAIL = "contactEmail";
    public static final String COLUMN_CONTACT_CONTACTUSERNAME = "contactUser";
    public static final String COLUMN_CONTACT_CONTACTSERVERSTATUS = "contactServerStatus";

    public static final String TABLE_CONTACTGROUP = "contactGroup";
    public static final String COLUMN_CONTACTGROUP_CONTACTGROUPIDLOCAL = "contactGroupIdLocal";
    public static final String COLUMN_CONTACTGROUP_CONTACTGROUPIDSERVER = "contactGroupIdServer";
    public static final String COLUMN_CONTACTGROUP_CONTACTGROUPNAME = "contactGroupName";

    public static final String TABLE_MESSAGE = "message";
    public static final String COLUMN_MESSAGE_SEQ = "seq";
    public static final String COLUMN_MESSAGE_ID = "id";
    public static final String COLUMN_MESSAGE_FROMTYPE = "fromType";
    public static final String COLUMN_MESSAGE_FROMID = "fromId";
    public static final String COLUMN_MESSAGE_TOTYPE = "toType";
    public static final String COLUMN_MESSAGE_TOID = "toId";
    public static final String COLUMN_MESSAGE_CREATETIMESTAMP = "createTimestamp";
    public static final String COLUMN_MESSAGE_SENDTIMESTAMP = "sendTimestamp";
    public static final String COLUMN_MESSAGE_RECEIVETIMESTAMP = "receiveTimestamp";
    public static final String COLUMN_MESSAGE_FROMDEVICE = "fromDevice";
    public static final String COLUMN_MESSAGE_CONTENTTYPE = "contentType";
    public static final String COLUMN_MESSAGE_CONTENT = "content";
    public static final String COLUMN_MESSAGE_STATUS = "status";
    public static final String COLUMN_MESSAGE_SIGNATURE = "signature";

    public static final String TABLE_PARTYJOINACTIVITY = "partyJoinActivity";
    public static final String COLUMN_PARTYJOINACTIVITY_IDLOCAL = "idLocal";
    public static final String COLUMN_PARTYJOINACTIVITY_IDSERVER = "idServer";
    public static final String COLUMN_PARTYJOINACTIVITY_PARTYTYPE = "partyType";
    public static final String COLUMN_PARTYJOINACTIVITY_PARTYIDLOCAL = "partyIdLocal";
    public static final String COLUMN_PARTYJOINACTIVITY_PARTYIDSERVER = "partyIdServer";
    public static final String COLUMN_PARTYJOINACTIVITY_ACTIVITYTYPE = "activityType";
    public static final String COLUMN_PARTYJOINACTIVITY_ACTIVITYIDLOCAL = "activityIdLocal";
    public static final String COLUMN_PARTYJOINACTIVITY_ACTIVITYIDSERVER = "activityIdServer";
    public static final String COLUMN_PARTYJOINACTIVITY_ROLE = "role";
    public static final String COLUMN_PARTYJOINACTIVITY_STATUS = "status";
    public static final String COLUMN_PARTYJOINACTIVITY_JOINTIMESTAMP = "joinTimestamp";

    public static final int ENTITIY_ID_NOT_READY = -1;
    public static final String ENTITY_VALUE_EMPTY = "empty";

    public static final int TASK_IS_ACTIVE = 1;
    public static final int TASK_IS_INACTIVE = 0;

    public static final String ACTIVITY_TYPE_TASK = "t";        //task
    public static final String ACTIVITY_TYPE_PROJECT = "p";     //project
    public static final String PARTY_TYPE_INDIVIDUAL = "i";     //individual
    public static final String PARTY_TYPE_GROUP = "g";          //group

    public static final String ATTRIBUTE_NAME_DATE = "date";
    public static final String ATTRIBUTE_NAME_TIME = "time";
    public static final String ATTRIBUTE_NAME_LOCATION = "location";
    public static final String ATTRIBUTE_NAME_DESCRIPTION = "desc";

    public static final String CONTACT_SERVER_STATUS_UNKNOWN = "unknown";
    public static final String CONTACT_SERVER_STATUS_VALID = "valid";
    public static final String CONTACT_SERVER_STATUS_INVALID = "invalid";

    public static final String MESSAGE_STATUS_NEW = "n";        //new
    public static final String MESSAGE_STATUS_SENT = "s";       //sent
    public static final String MESSAGE_STATUS_RECEIVED = "r";   //received

    public static final String PARTY_JOIN_ACTIVITY_STATUS_INVITED = "i";
    public static final String PARTY_JOIN_ACTIVITY_STATUS_ACCEPTED = "a";
    public static final String PARTY_JOIN_ACTIVITY_STATUS_REJECTED = "r";

    public static final String PARTY_JOIN_ACTIVITY_ROLE_OWNER = "o";
    public static final String PARTY_JOIN_ACTIVITY_ROLE_MEMBER = "m";
    public static final String PARTY_JOIN_ACTIVITY_ROLE_FOLLOWER = "f";

    public static final int DASHBOARD_TAB_COUNT = 3;
//    public static final int DASHBOARD_TAB_INDEX_STREAM = 2;
    public static final int DASHBOARD_TAB_INDEX_EVENTS = 0;
//    public static final int DASHBOARD_TAB_INDEX_FRIENDS = 1;
//    public static final int DASHBOARD_TAB_INDEX_MESSAGES = 2;
    public static final int DASHBOARD_TAB_INDEX_TOPICS = 1;
    public static final int DASHBOARD_TAB_INDEX_POSTS = 2;

    public static final int EVENT_GROUPS_BUDDIES_TAB_COUNT = 2;
    public static final int EVENT_GROUPS_BUDDIES_TAB_INDEX_GROUPS = 0;
    public static final int EVENT_GROUPS_BUDDIES_TAB_INDEX_BUDDIES = 1;

    public static final int USER_PROFILE_TAB_COUNT = 3;
    public static final int USER_PROFILE_TAB_INDEX_PROFILE = 0;
    public static final int USER_PROFILE_TAB_INDEX_GROUPS = 1;
    public static final int USER_PROFILE_TAB_INDEX_EVENTS = 2;
    public static final String USER_PROFILE_TAB_INDEX = "user_profile_tab_index";

    public static final int GROUP_DETAILS_TAB_COUNT = 2;
    public static final int GROUP_DETAILS_TAB_INDEX_UPCOMINGEVENTS = 0;
    public static final int GROUP_DETAILS_TAB_INDEX_PASTEVENTS = 1;

    public static final int ALL_BUDDIES_TAB_COUNT = 2;
    public static final int ALL_BUDDIES_TAB_INDEX_MYMATCH = 0;
    public static final int ALL_BUDDIES_TAB_INDEX_MYBUDDIES = 1;

    public static int ACTIVITY_INVITATION_STATUS_COMPLETE = 1;
    public static int ACTIVITY_INVITATION_STATUS_INCOMPLETE = 0;

    public static final int INTENT_SHOW_EVENT_DETAIL = 101;
    public static final String EXTRA_EVENT_SEQ = "extra_event_seq";
    public static final String EXTRA_CONVERSATION_SEQ = "extra_conversation_seq";

    public static final String PERSON_STATUS_NOTCONNECTED = "not connected";
    public static final String PERSON_STATUS_INVITED = "invited";
    public static final String PERSON_STATUS_ACCEPTED = "accepted";
    public static final String PERSON_STATUS_REJECTED = "rejected";

    public static final String CONTACT_LIST_SECTION_MYFRIENDS = "MY FRIENDS";
    public static final String CONTACT_LIST_SECTION_MYPHONECONTACTS = "MY PHONE CONTACTS";


}
