package com.soco.SoCoClient._v2.businesslogic.config;

public class DbConfig {

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
    public static final String COLUMN_ATTRIBUTE_ACTIVITYID = "activityId";
    public static final String COLUMN_ATTRIBUTE_ATTRNAME = "attrName";
    public static final String COLUMN_ATTRIBUTE_ATTRVALUE = "attrValue";
    public static final String COLUMN_ATTRIBUTE_ATTRLASTUPDATEUSER = "attrLastUpdateUser";
    public static final String COLUMN_ATTRIBUTE_ATTRLASTUPDATETIMESTAMP = "attrLastUpdateTimestamp";

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

    public static final String TABLE_CONTACTGROUP = "contactGroup";
    public static final String COLUMN_CONTACTGROUP_CONTACTGROUPIDLOCAL = "contactGroupIdLocal";
    public static final String COLUMN_CONTACTGROUP_CONTACTGROUPIDSERVER = "contactGroupIdServer";
    public static final String COLUMN_CONTACTGROUP_CONTACTGROUPNAME = "contactGroupName";

    public static final int ENTITIY_ID_NOT_READY = -1;


}
