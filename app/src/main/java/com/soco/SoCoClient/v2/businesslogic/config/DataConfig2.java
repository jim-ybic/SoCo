package com.soco.SoCoClient.v2.businesslogic.config;

public class DataConfig2 {

    public static final String TABLE_TASK = "task";
    public static final String COLUMN_TASK_TASKIDLOCAL = "taskIdLocal";
    public static final String COLUMN_TASK_TASKIDSERVER = "taskIdServer";
    public static final String COLUMN_TASK_TASKNAME = "taskName";
    public static final String COLUMN_TASK_TASKPATH = "taskPath";
    public static final String COLUMN_TASK_ISTASKACTIVE = "isTaskActive";

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

    public static final String TABLE_COMMENT = "comment";
    public static final String COLUMN_COMMENT_IDLOCAL = "commentIdLocal";
    public static final String COLUMN_COMMENT_IDSERVER = "commentIdServer";
    public static final String COLUMN_COMMENT_ACTIVITYTYPE = "activityType";
    public static final String COLUMN_COMMENT_ACTIVITYID = "activityId";
    public static final String COLUMN_COMMENT_CONTENT = "commentContent";
    public static final String COLUMN_COMMENT_USERNAME = "commentUsername";
    public static final String COLUMN_COMMENT_TIMESTAMP = "commentTimestamp";

    public static final String TABLE_ATTACHMENT = "attachment";
    public static final String COLUMN_ATTACHMENT_ATTACHMENTIDLOCAL = "attachmentIdLocal";
    public static final String COLUMN_ATTACHMENT_ATTACHMENTIDSERVER = "attachmentIdServer";
    public static final String COLUMN_ATTACHMENT_ACTIVITYTYPE = "activityType";
    public static final String COLUMN_ATTACHMENT_ACTIVITYID = "activityId";
    public static final String COLUMN_ATTACHMENT_DISPLAYNAME = "attachmentDisplayName";
    public static final String COLUMN_ATTACHMENT_URI = "attachmentUri";
    public static final String COLUMN_ATTACHMENT_REMOTEPATH = "attachmentRemotePath";
    public static final String COLUMN_ATTACHMENT_LOCALPATH = "attachmentLocalPath";
    public static final String COLUMN_ATTACHMENT_USER = "attachmentUser";

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
    public static final String COLUMN_MESSAGE_MSGIDLOCAL = "msgIdLocal";
    public static final String COLUMN_MESSAGE_MSGIDSERVER = "msgIdServer";
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

    public static final int ENTITIY_ID_NOT_READY = -1;
    public static final String ENTITY_VALUE_EMPTY = "empty";

    public static final int TASK_IS_ACTIVE = 1;
    public static final int TASK_IS_INACTIVE = 0;

    public static final String ACTIVITY_TYPE_TASK = "t";
    public static final String ACTIVITY_TYPE_PROJECT = "p";

    public static final String ATTRIBUTE_NAME_DATE = "date";
    public static final String ATTRIBUTE_NAME_TIME = "time";
    public static final String ATTRIBUTE_NAME_LOCATION = "location";
    public static final String ATTRIBUTE_NAME_DESCRIPTION = "desc";

    public static final String CONTACT_SERVER_STATUS_UNKNOWN = "unknown";
    public static final String CONTACT_SERVER_STATUS_VALID = "valid";
    public static final String CONTACT_SERVER_STATUS_INVALID = "invalid";

    public static final String MESSAGE_STATUS_NEW = "new";
    public static final String MESSAGE_STATUS_SENT = "sent";
    public static final String MESSAGE_STATUS_RECEIVED = "received";

}
