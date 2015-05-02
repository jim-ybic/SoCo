package com.soco.SoCoClient.control.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import com.soco.SoCoClient.control.SocoApp;
import com.soco.SoCoClient.control.config.DataConfig;
import com.soco.SoCoClient.control.config.GeneralConfig;
import com.soco.SoCoClient.control.util.SignatureUtil;
import com.soco.SoCoClient.model.Activity;

public class DBManagerSoco {
    private DBHelperSoco helper;
    private SQLiteDatabase db;
    public Context context;

    public static String tag = "DBManagerSoco";
    public static String TEST_NONAME = "test_noname";
    public static String TEST_NOEMAIL = "test_noemail";

    public DBManagerSoco(Context context) {
        helper = new DBHelperSoco(context);
        db = helper.getWritableDatabase();
    }

    public void addMemberToActivity(String userEmail, String userName, String nickName, int aid){
        Log.d(tag, "Adding member " + userEmail + ", " + userName + ", " + nickName
                + " into project pid " + aid);

        try {
            db.beginTransaction();
            Log.i(tag, "insert activity_user table entry: " + aid + ", " + userName);

            ContentValues cv = new ContentValues();
            cv.put(DataConfig.COLUMN_ACTIVITY_MEMBER_AID, aid);
            cv.put(DataConfig.COLUMN_ACTIVITY_MEMBER_MEMBER_EMAIL, userEmail);

            if(userName != null && !userName.isEmpty())
                cv.put(DataConfig.COLUMN_ACTIVITY_MEMBER_MEMBER_USERNAME, userName);
            if(nickName != null && !nickName.isEmpty())
                cv.put(DataConfig.COLUMN_ACTIVITY_MEMBER_MEMBER_NICKNAME, nickName);

            cv.put(DataConfig.COLUMN_ACTIVITY_MEMBER_MEMBER_STATUS, "");
            cv.put(DataConfig.COLUMN_ACTIVITY_MEMBER_MEMBER_JOIN_TIMESTAMP, "");
            db.insert(DataConfig.TABLE_ACTIVITY_MEMBER, null, cv);

            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    public ArrayList<ArrayList<String>> getUpdatesOfActivity(int aid) {
        Log.d(tag, "get comments of activity " + aid);
        ArrayList<ArrayList<String>> comments = new ArrayList<>();

        Cursor c = db.rawQuery("SELECT * FROM " + DataConfig.TABLE_ACTIVITY_UPDATES +
                        " where " + DataConfig.COLUMN_ACTIVITY_UPDATES_AID + " = ?",
                new String[] {String.valueOf(aid)});

        String name, comment, timestamp;
        while (c.moveToNext()) {
            name = c.getString(c.getColumnIndex(DataConfig.COLUMN_ACTIVITY_UPDATES_USER));
            comment = c.getString(c.getColumnIndex(DataConfig.COLUMN_ACTIVITY_UPDATES_COMMENT));
            timestamp = c.getString(c.getColumnIndex(DataConfig.COLUMN_ACTIVITY_UPDATES_TIMESTAMP));
            Log.d(tag, "Found user " + name + " and comment " + comment + " at " + timestamp);
            ArrayList<String> e = new ArrayList<>();
            e.add(name);
            e.add(comment);
            e.add(timestamp);
            comments.add(e);
        }
        c.close();
        return comments;
    }

    public HashMap<String, String> getMembersOfActivity(int aid){
        Log.d(tag, "get members of project " + aid);
        HashMap<String, String> userNameEmail = new HashMap<>();

        Cursor c = db.rawQuery("SELECT * FROM " + DataConfig.TABLE_ACTIVITY_MEMBER +
                        " where " + DataConfig.COLUMN_ACTIVITY_MEMBER_AID + " = ?",
                new String[] {String.valueOf(aid)});

        String name, email;
        while (c.moveToNext()) {
//            name = c.getString(c.getColumnIndex(DataConfig.COLUMN_ACTIVITY_MEMBER_MEMBER_USERNAME));
            name = c.getString(c.getColumnIndex(DataConfig.COLUMN_ACTIVITY_MEMBER_MEMBER_NICKNAME));
            email = c.getString(c.getColumnIndex(DataConfig.COLUMN_ACTIVITY_MEMBER_MEMBER_EMAIL));
            Log.d(tag, "Found user " + name + "/" + email);
            userNameEmail.put(email, name);
        }
        c.close();
        return userNameEmail;
    }

    public int addActivity(Activity p){
        Log.i(tag, "Add new project: " + p.pname);
        int pid = -1;
        Log.i(tag, "dbmanager context: " + context);
        String userEmail = ((SocoApp)context).loginEmail;
        String userName = ((SocoApp)context).profile.getNickname(context);
        try {
            db.beginTransaction();

            Log.i(tag, "Insert into db activity: " + p.pname + ", , "
//                    + SignatureUtil.now() + ", " + SignatureUtil.now() + ", "
//                    + SignatureUtil.genSHA1(p) + ", " + DataConfig.VALUE_ACTIVITY_ACTIVE + ", "
                    + p.pid_onserver + ", " + p.invitation_status
                    + ", " + p.path);

            ContentValues cv = new ContentValues();
            cv.put(DataConfig.COLUMN_ACTIVITY_NAME, p.pname);
            cv.put(DataConfig.COLUMN_ACTIVITY_CREATE_TIMESTAMP, SignatureUtil.now());
            cv.put(DataConfig.COLUMN_ACTIVITY_UPDATE_TIMESTAMP, SignatureUtil.now());
            cv.put(DataConfig.COLUMN_ACTIVITY_SIGNATURE, SignatureUtil.genSHA1(p));
            cv.put(DataConfig.COLUMN_ACTIVITY_ACTIVE, DataConfig.VALUE_ACTIVITY_ACTIVE);
            cv.put(DataConfig.COLUMN_ACTIVITY_ID_ONSERVER, p.pid_onserver);
            cv.put(DataConfig.COLUMN_ACTIVITY_INVITATION_STATUS, p.invitation_status);
            cv.put(DataConfig.COLUMN_ACTIVITY_TAG, p.ptag);
            cv.put(DataConfig.COLUMN_ACTIVITY_PATH, p.path);

            db.insert(DataConfig.TABLE_ACTIVITY, null, cv);

            //get pid
            String query = "SELECT MAX(" + DataConfig.COLUMN_ACTIVITY_ID
                    + ") FROM " + DataConfig.TABLE_ACTIVITY;
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()){
                do {
                    pid = cursor.getInt(0);
                } while(cursor.moveToNext());
            }

//            Log.i(tag, "insert activity_user table entry: " + pid + ", " + userEmail);
//            db.execSQL("INSERT INTO " + DataConfig.TABLE_ACTIVITY_MEMBER + " VALUES (" +
//                    "?, ?, ?, ?, ?, ?)", new Object[]{
//                    pid, userEmail, userName,
//                    "",
//                    SignatureUtil.now(), ""});
            Log.i(tag, "insert into db activity members: " + pid + ", " + userEmail + ", " + userName);
            ContentValues cvMember = new ContentValues();
            cvMember.put(DataConfig.COLUMN_ACTIVITY_MEMBER_AID, pid);
            cvMember.put(DataConfig.COLUMN_ACTIVITY_MEMBER_MEMBER_EMAIL, userEmail);
            cvMember.put(DataConfig.COLUMN_ACTIVITY_MEMBER_MEMBER_USERNAME, userName);
            //todo: add nickname
            cvMember.put(DataConfig.COLUMN_ACTIVITY_MEMBER_MEMBER_JOIN_TIMESTAMP, SignatureUtil.now());
            db.insert(DataConfig.TABLE_ACTIVITY_MEMBER, null, cvMember);


            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }

        Log.i(tag, "New project added with local pid: " + pid);
        return pid;
    }

    public void deleteActivityByPid(int pid){
        Log.i(tag, "Delete activity by pid: " + pid);
        db.delete(DataConfig.TABLE_ACTIVITY, DataConfig.COLUMN_ACTIVITY_ID + " = ?",
                new String[]{String.valueOf(pid)});
        db.delete(DataConfig.TABLE_ATTRIBUTE, DataConfig.COLUMN_ATTRIBUTE_PID + " = ?",
                new String[]{String.valueOf(pid)});
    }

    public ArrayList<Activity> loadActivitiessByActiveness(String pactive) {
        Log.i(tag, "Load projects which are: " + pactive);
        ArrayList<Activity> activities = new ArrayList<>();

        Log.d(tag, "Query project: select * from " + DataConfig.TABLE_ACTIVITY
                + " where " + DataConfig.COLUMN_ACTIVITY_ACTIVE + " = " + pactive);
        Cursor c = db.rawQuery("SELECT * FROM " + DataConfig.TABLE_ACTIVITY +
                        " where " + DataConfig.COLUMN_ACTIVITY_ACTIVE + " = ?",
                new String[]{pactive});

        while (c.moveToNext()) {
            Activity p = new Activity(c);
            activities.add(p);
        }
        c.close();
        return activities;
    }

    public Activity loadActivityByAid(int pid) {
        Log.i(tag, "Load project for pid: " + pid);
        Activity p = null;

        Log.d(tag, "Query project: select * from " + DataConfig.TABLE_ACTIVITY
                + " where " + DataConfig.COLUMN_ACTIVITY_ID + " = " + pid);
        Cursor c = db.rawQuery("SELECT * FROM " + DataConfig.TABLE_ACTIVITY +
                        " where " + DataConfig.COLUMN_ACTIVITY_ID + " = ?",
                new String[] {String.valueOf(pid)});

        while (c.moveToNext()) {
            p = new Activity(c);
        }
        c.close();
        return p;
    }

    public int findLocalAidByServerAid(int aid_onserver){
        Log.d(tag, "select * from " + DataConfig.TABLE_ACTIVITY
                + " where " + DataConfig.COLUMN_ACTIVITY_ID_ONSERVER + " = " + aid_onserver);
        Cursor c = db.rawQuery("SELECT * FROM " + DataConfig.TABLE_ACTIVITY +
                        " where " + DataConfig.COLUMN_ACTIVITY_ID_ONSERVER + " = ?",
                new String[] {String.valueOf(aid_onserver)});

        int aid = -1;
        while (c.moveToNext()){
            aid = c.getInt(c.getColumnIndex(DataConfig.COLUMN_ACTIVITY_ID));
        }

        return aid;
    }

    public String findActivityIdOnserver(int pid){
        Activity p = loadActivityByAid(pid);
        return p.pid_onserver;
    }

    public ArrayList<HashMap<String, String>> loadActivityAttributesByPid(int pid){
        Log.i(tag, "Load project attributes for pid: " + pid);
        ArrayList<HashMap<String, String>> list = new ArrayList<>();

        Log.d(tag, "Query project attributes: select * from " + DataConfig.TABLE_ATTRIBUTE
                + " where " + DataConfig.COLUMN_ATTRIBUTE_PID + " = " + pid);
        Cursor c = db.rawQuery("SELECT * FROM " + DataConfig.TABLE_ATTRIBUTE +
                        " where " + DataConfig.COLUMN_ATTRIBUTE_PID + " = ?",
                new String[]{String.valueOf(pid)});

        int count = 0;
        while (c.moveToNext()){
            String attr_name = c.getString(c.getColumnIndex(DataConfig.COLUMN_ATTRIBUTE_NAME));
            String attr_value = c.getString(c.getColumnIndex(DataConfig.COLUMN_ATTRIBUTE_VALUE));
            Log.d(tag, "Found attribute: " + attr_name + ", " + attr_value);
            HashMap<String, String> attrMap = new HashMap<>();
            attrMap.put(attr_name, attr_value);
            list.add(attrMap);
            count ++;
        }
        Log.i(tag, count + " attributes loaded for pid " + pid);
        return list;
    }

    public void clearActivityAttributesExceptLocation(int pid){
        try {
            db.beginTransaction();
            Log.d(tag, "DELETE FROM " + DataConfig.TABLE_ATTRIBUTE
                    + " WHERE " + DataConfig.COLUMN_ATTRIBUTE_PID + " = " + pid
                    + " AND " + DataConfig.COLUMN_ATTRIBUTE_NAME + " NOT IN ("
                    + DataConfig.ATTRIBUTE_NAME_LOCLAT + ","
                    + DataConfig.ATTRIBUTE_NAME_LOCLNG + ","
                    + DataConfig.ATTRIBUTE_NAME_LOCZOOM + ","
//                    + DataConfig.ATTRIBUTE_NAME_LOCNAME
                    + ")");
            db.execSQL("DELETE FROM " + DataConfig.TABLE_ATTRIBUTE
                            + " WHERE " + DataConfig.COLUMN_ATTRIBUTE_PID + " = ?"
                            + " AND " + DataConfig.COLUMN_ATTRIBUTE_NAME + " NOT IN (?, ?, ?)",
                    new Object[]{pid,
                            DataConfig.ATTRIBUTE_NAME_LOCLAT,
                            DataConfig.ATTRIBUTE_NAME_LOCLNG,
                            DataConfig.ATTRIBUTE_NAME_LOCZOOM
//                            ,DataConfig.ATTRIBUTE_NAME_LOCNAME
                    });
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    public void updateDbActivityAttributes(int pid, HashMap<String, String> attrMap){
        clearActivityAttributesExceptLocation(pid);

        for(Map.Entry<String, String> entry : attrMap.entrySet()){
            String attr_name = entry.getKey();
            String attr_value = entry.getValue();
            String now = SignatureUtil.now();
            Log.i(tag, "Process project attribute: " + attr_name + ", " + attr_value);

            try {
                db.beginTransaction();
                Log.i(tag, "Add project attribute: INSERT INTO " + DataConfig.TABLE_ATTRIBUTE
                        + " VALUES(null, " + pid + ", " + attr_name + ", " + attr_value + ", "
                        + ", " + now + ", " + now);
                db.execSQL("INSERT INTO " + DataConfig.TABLE_ATTRIBUTE
                                + " VALUES(null, ?, ?, ?, ?, ?, ?)",
                        new Object[]{pid, attr_name, attr_value, "", now, now});
                db.setTransactionSuccessful();
            } finally {
                db.endTransaction();
            }
        }
    }

    public void updateActivityName(int pid, String pname){
        Log.i(tag, "Update database for project: " + pid);

        ContentValues cv = new ContentValues();
        cv.put(DataConfig.COLUMN_ACTIVITY_NAME, pname);
        String now = SignatureUtil.now();
        cv.put(DataConfig.COLUMN_ACTIVITY_UPDATE_TIMESTAMP, now);

        db.update(DataConfig.TABLE_ACTIVITY, cv, DataConfig.COLUMN_ACTIVITY_ID + " = ?",
                new String[]{String.valueOf(pid)});

        Log.i(tag, "Updated project " + pid + " name: " + pname);
        Log.i(tag, "Updated project " + pid + " update timestamp: " + now);
    }

    public void updateAcivityTag(int pid, String ptag){
        Log.i(tag, "Update project tag for pid: " + pid + ", " + ptag);

        ContentValues cv = new ContentValues();
        cv.put(DataConfig.COLUMN_ACTIVITY_TAG, ptag);
        String now = SignatureUtil.now();
        cv.put(DataConfig.COLUMN_ACTIVITY_UPDATE_TIMESTAMP, now);

        db.update(DataConfig.TABLE_ACTIVITY, cv, DataConfig.COLUMN_ACTIVITY_ID + " = ?",
                new String[]{String.valueOf(pid)});

        Log.d(tag, "Updated project " + pid + " tag: " + ptag);
    }

    public void updateActivityActiveness(int pid, String activeness) {
        Log.i(tag, "Update project " + pid + " status: " + activeness);
        ContentValues cv = new ContentValues();
        cv.put(DataConfig.COLUMN_ACTIVITY_ACTIVE, activeness);
        db.update(DataConfig.TABLE_ACTIVITY, cv, DataConfig.COLUMN_ACTIVITY_ID + " = ?",
                new String[]{String.valueOf(pid)});
    }

    public void updateActivityIdOnserver(int pid, String pid_onserver) {
        Log.i(tag, "Update project " + pid + " pid_onserver: " + pid_onserver);
        ContentValues cv = new ContentValues();
        cv.put(DataConfig.COLUMN_ACTIVITY_ID_ONSERVER, pid_onserver);
        db.update(DataConfig.TABLE_ACTIVITY, cv,
                DataConfig.COLUMN_ACTIVITY_ID + " = ?",
                new String[]{String.valueOf(pid)});
    }

    public void addSharedFile(
            int pid, String displayName, Uri uri, String remotePath, String localPath) {
        Log.i(tag, "Add shared file start: " + pid + ", " + displayName + ", "
                + uri.toString() + ", " + remotePath + ", " + localPath);

        String now = SignatureUtil.now();
        try{
            db.beginTransaction();
            Log.i(tag, "INSERT INTO " + DataConfig.TABLE_SHARED_FILE
                    + " VALUES(null, " + pid + ", " + displayName + ", "
                    + uri + ", " + remotePath + ", " + localPath
                    + ",, " + now + ", " + now);
            db.execSQL("INSERT INTO " + DataConfig.TABLE_SHARED_FILE
                            + " VALUES(null, ?, ?, ?, ?, ?, ?, ?, ?)",
                    new Object[]{pid, displayName, uri, remotePath, localPath,
                            "", now, now});
            db.setTransactionSuccessful();
        } finally{
            db.endTransaction();
        }
    }

    public ArrayList<String> getSharedFilesLocalPath(int pid) {
        Log.d(tag, "Get shared file local path for pid: " + pid);
        ArrayList<String> list = new ArrayList<>();

        Log.d(tag, "SELECT " + DataConfig.COLUMN_SHARED_FILE_LOCAL_PATH
                + " FROM " + DataConfig.TABLE_SHARED_FILE
                + " WHERE " + DataConfig.COLUMN_SHARED_FILE_PID + " = " + pid);
        Cursor c =  db.rawQuery("SELECT " + DataConfig.COLUMN_SHARED_FILE_LOCAL_PATH
                        + " FROM " + DataConfig.TABLE_SHARED_FILE
                        + " WHERE " + DataConfig.COLUMN_SHARED_FILE_PID + " = ?",
                new String[] {String.valueOf(pid)});

        int count = 0;
        while (c.moveToNext()){
            String path = c.getString(c.getColumnIndex(
                    DataConfig.COLUMN_SHARED_FILE_LOCAL_PATH));
            Log.d(tag, "Found path: " + path);
            list.add(path);
            count ++;
        }
        Log.i(tag, "Total number of shared file local path returned: " + count);
        return list;
    }

    public ArrayList<String> getSharedFilesDisplayName(int pid) {
        Log.d(tag, "Get shared file display name for pid: " + pid);
        ArrayList<String> list = new ArrayList<>();

        Log.d(tag, "SELECT " + DataConfig.COLUMN_SHARED_FILE_DISPLAY_NAME
                + " FROM " + DataConfig.TABLE_SHARED_FILE
                + " WHERE " + DataConfig.COLUMN_SHARED_FILE_PID + " = " + pid);
        Cursor c =  db.rawQuery("SELECT " + DataConfig.COLUMN_SHARED_FILE_DISPLAY_NAME
                        + " FROM " + DataConfig.TABLE_SHARED_FILE
                        + " WHERE " + DataConfig.COLUMN_SHARED_FILE_PID + " = ?",
                new String[] {String.valueOf(pid)});

        int count = 0;
        while (c.moveToNext()){
            String name = c.getString(c.getColumnIndex(
                    DataConfig.COLUMN_SHARED_FILE_DISPLAY_NAME));
            Log.d(tag, "Found display name: " + name);
            list.add(name);
            count ++;
        }
        Log.i(tag, "Total number of display names returned: " + count);
        return list;
    }

    public void setLocation(int pid, String lat, String lng, String zoom, String name) {
        Log.i(tag, "Add location start: " + pid + ", " + lat + ", " + lng + ", "
                + zoom + ", " + name);

        String now = SignatureUtil.now();
        try{
            db.beginTransaction();

            Log.i(tag, "DELETE FROM " + DataConfig.TABLE_ATTRIBUTE
                    + " WHERE " + DataConfig.COLUMN_ATTRIBUTE_PID + " = " + pid
                    + " AND " + DataConfig.COLUMN_ATTRIBUTE_NAME + " IN ("
                    + DataConfig.ATTRIBUTE_NAME_LOCLAT + ","
                    + DataConfig.ATTRIBUTE_NAME_LOCLNG + ","
                    + DataConfig.ATTRIBUTE_NAME_LOCZOOM + ","
                    + DataConfig.ATTRIBUTE_NAME_LOCNAME + ")");
            db.delete(DataConfig.TABLE_ATTRIBUTE,
                    DataConfig.COLUMN_ATTRIBUTE_PID + " = ? AND "
                            + DataConfig.COLUMN_ATTRIBUTE_NAME + " IN (?, ?, ?, ?)",
                    new String[]{String.valueOf(pid),
                            DataConfig.ATTRIBUTE_NAME_LOCLAT,
                            DataConfig.ATTRIBUTE_NAME_LOCLNG,
                            DataConfig.ATTRIBUTE_NAME_LOCZOOM,
                            DataConfig.ATTRIBUTE_NAME_LOCNAME});


            Log.i(tag, "INSERT INTO " + DataConfig.TABLE_ATTRIBUTE
                    + " VALUES(null, " + pid + ", " + DataConfig.ATTRIBUTE_NAME_LOCLAT + ", "
                    + lat + ", , " + now + ", " + now);
            db.execSQL("INSERT INTO " + DataConfig.TABLE_ATTRIBUTE
                            + " VALUES(null, ?, ?, ?, ?, ?, ?)",
                    new Object[]{pid, DataConfig.ATTRIBUTE_NAME_LOCLAT, lat, "", now, now});

            Log.i(tag, "INSERT INTO " + DataConfig.TABLE_ATTRIBUTE
                    + " VALUES(null, " + pid + ", " + DataConfig.ATTRIBUTE_NAME_LOCLNG + ", "
                    + lng + ", , " + now + ", " + now);
            db.execSQL("INSERT INTO " + DataConfig.TABLE_ATTRIBUTE
                            + " VALUES(null, ?, ?, ?, ?, ?, ?)",
                    new Object[]{pid, DataConfig.ATTRIBUTE_NAME_LOCLNG, lng, "", now, now});

            Log.i(tag, "INSERT INTO " + DataConfig.TABLE_ATTRIBUTE
                    + " VALUES(null, " + pid + ", " + DataConfig.ATTRIBUTE_NAME_LOCZOOM + ", "
                    + zoom + ", , " + now + ", " + now);
            db.execSQL("INSERT INTO " + DataConfig.TABLE_ATTRIBUTE
                            + " VALUES(null, ?, ?, ?, ?, ?, ?)",
                    new Object[]{pid, DataConfig.ATTRIBUTE_NAME_LOCZOOM, zoom, "", now, now});

            Log.i(tag, "INSERT INTO " + DataConfig.TABLE_ATTRIBUTE
                    + " VALUES(null, " + pid + ", " + DataConfig.ATTRIBUTE_NAME_LOCNAME + ", "
                    + name + ", , " + now + ", " + now);
            db.execSQL("INSERT INTO " + DataConfig.TABLE_ATTRIBUTE
                            + " VALUES(null, ?, ?, ?, ?, ?, ?)",
                    new Object[]{pid, DataConfig.ATTRIBUTE_NAME_LOCNAME, name, "", now, now});
            db.setTransactionSuccessful();
        } finally{
            db.endTransaction();
        }
    }

    public void addCommentToProject(String comment, int pid, String user) {
        Log.d(tag, "Adding comment: " + comment + ", into project pid " + pid);

        try {
            db.beginTransaction();
            Log.i(tag, "insert " + DataConfig.TABLE_ACTIVITY_UPDATES
                    + " table entry: " + pid + ", " + comment + ", " + user);
            db.execSQL("INSERT INTO " + DataConfig.TABLE_ACTIVITY_UPDATES + " VALUES (" +
                    "null, ?, ?, ?, ?)", new Object[]{
                    pid, comment, user, SignatureUtil.now()});
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    public void setInvitationStatusCompleted(int pid) {
        Log.i(tag, "Set invitation status for project: " + pid);

        ContentValues cv = new ContentValues();
        cv.put(DataConfig.COLUMN_ACTIVITY_INVITATION_STATUS,
                DataConfig.ACTIVITY_INVITATION_STATUS_COMPLETE);
        String now = SignatureUtil.now();
        cv.put(DataConfig.COLUMN_ACTIVITY_UPDATE_TIMESTAMP, now);

        db.update(DataConfig.TABLE_ACTIVITY, cv, DataConfig.COLUMN_ACTIVITY_ID + " = ?",
                new String[]{String.valueOf(pid)});

        Log.d(tag, "Updated project complete");
    }

    public HashMap<String, String> getContacts() {
        Log.d(tag, "get contacts");
        HashMap<String, String> contactEmailName = new HashMap<>();

        Cursor c = db.rawQuery("SELECT * FROM " + DataConfig.TABLE_CONTACT,
                null);

        String email, name;
        while (c.moveToNext()) {
            email = c.getString(c.getColumnIndex(DataConfig.COLUMN_CONTACT_EMAIL));
            name = c.getString(c.getColumnIndex(DataConfig.COLUMN_CONTACT_NICKNAME));
            Log.d(tag, "Found user " + name + "/" + email);
            contactEmailName.put(email, name);
        }
        c.close();
        return contactEmailName;

    }

    public void saveContact(String email, String nickname) {
        Log.d(tag, "Adding contact " + email + ", " + nickname);
        try {
            db.beginTransaction();

            Log.i(tag, "insert into contacts: " + email);
            ContentValues cv = new ContentValues();
            cv.put(DataConfig.COLUMN_CONTACT_EMAIL, email);
            cv.put(DataConfig.COLUMN_CONTACT_USERNAME, TEST_NONAME);
            cv.put(DataConfig.COLUMN_CONTACT_NICKNAME, nickname);

            db.insert(DataConfig.TABLE_CONTACT, null, cv);
        } finally {
            db.setTransactionSuccessful();
            db.endTransaction();
        }
    }

    public void updateContactName(String email, String username, String nickname) {
        Log.i(tag, "Update contact name: " + email + ", " + username + ", " + nickname);

        ContentValues cv = new ContentValues();
        cv.put(DataConfig.COLUMN_CONTACT_EMAIL, email);
        cv.put(DataConfig.COLUMN_CONTACT_USERNAME, username);
        cv.put(DataConfig.COLUMN_CONTACT_NICKNAME, nickname);

        db.update(DataConfig.TABLE_CONTACT, cv,
                DataConfig.COLUMN_CONTACT_EMAIL + " = ?",
                new String[]{email});

        Log.d(tag, "Updated contact name ");
    }

    public void updateContactNameIdOnserver(String email, String name, int contactIdOnserver) {
        Log.i(tag, "Update contact in onserver: " + email + ", " + name + ", " + contactIdOnserver);

        ContentValues cv = new ContentValues();
//        cv.put(DataConfig.COLUMN_CONTACT_EMAIL, email);
        cv.put(DataConfig.COLUMN_CONTACT_USERNAME, name);
        cv.put(DataConfig.COLUMN_CONTACT_ID_ONSERVER, contactIdOnserver);

        db.update(DataConfig.TABLE_CONTACT, cv,
                DataConfig.COLUMN_CONTACT_EMAIL + " = ?",
                new String[]{email});

//        Log.d(tag, "Updated contact id onserver " + email + ", " + name + ", " + contactIdOnserver);
    }

    public String getPhoneByContactEmail(String email) {
        Log.d(tag, "get phone of contact email " + email);
        Cursor c = db.rawQuery("SELECT * FROM " + DataConfig.TABLE_CONTACT +
                        " where " + DataConfig.COLUMN_CONTACT_EMAIL + " = ?",
                new String[] {email});
        String phone = new String();
        while (c.moveToNext()) {
            phone = c.getString(c.getColumnIndex(DataConfig.COLUMN_CONTACT_PHONE));
        }
        c.close();
        return phone;
    }

    public int getContactIdByEmail(String email) {
        Log.d(tag, "get contact id of contact email " + email);
        Cursor c = db.rawQuery("SELECT * FROM " + DataConfig.TABLE_CONTACT +
                        " where " + DataConfig.COLUMN_CONTACT_EMAIL + " = ?",
                new String[] {email});
        int id = -1;
        while (c.moveToNext()) {
            id = c.getInt(c.getColumnIndex(DataConfig.COLUMN_CONTACT_ID));
        }
        c.close();
        if(id == -1)
            Log.e(tag, "cannot find contact id for email " + email);
        else
            Log.d(tag, "find contact id on server " + id);

        return id;
    }

    public int getContactIdOnserverByEmail(String email) {
        Log.d(tag, "get contact id onserver of contact email " + email);
        Cursor c = db.rawQuery("SELECT * FROM " + DataConfig.TABLE_CONTACT +
                        " where " + DataConfig.COLUMN_CONTACT_EMAIL + " = ?",
                new String[] {email});
        int id = -1;
        while (c.moveToNext()) {
            id = c.getInt(c.getColumnIndex(DataConfig.COLUMN_CONTACT_ID_ONSERVER));
        }
        c.close();
        if(id == -1)
            Log.e(tag, "cannot find contact id on server for email " + email);
        else
            Log.d(tag, "find contact id on server " + id);

        return id;
    }

    public ArrayList<ArrayList<String>> getChatHistoryByContactId(int contactId) {
        Log.d(tag, "get chat history of contact id " + contactId);
        ArrayList<ArrayList<String>> chatHist = new ArrayList<>();

        Cursor c = db.rawQuery("SELECT * FROM " + DataConfig.TABLE_CHAT +
                        " where " + DataConfig.COLUMN_CHAT_CONTACT_ID + " = ?",
                new String[] {String.valueOf(contactId)});

        String content, timestamp, type;
        while (c.moveToNext()) {
            content = c.getString(c.getColumnIndex(DataConfig.COLUMN_CHAT_CONTENT));
            timestamp = c.getString(c.getColumnIndex(DataConfig.COLUMN_CHAT_TIMESTAMP));
            type = c.getString(c.getColumnIndex(DataConfig.COLUMN_CHAT_TYPE));
            Log.d(tag, "Found chat: " + content + ", " + type + " at " + timestamp);

            ArrayList<String> e = new ArrayList<>();
            e.add(content);
            e.add(timestamp);
            e.add(type);
            chatHist.add(e);
        }
        c.close();
        return chatHist;
    }

    public void addMessage(int contactId, String message, int chat_type) {
        Log.d(tag, "Adding message: " + message + ", into contact pid " + contactId
                + ", of type " + chat_type);

        try {
            db.beginTransaction();
            ContentValues cv = new ContentValues();
            cv.put(DataConfig.COLUMN_CHAT_CONTACT_ID, contactId);
            cv.put(DataConfig.COLUMN_CHAT_CONTENT, message);
            cv.put(DataConfig.COLUMN_CHAT_TIMESTAMP, SignatureUtil.now());
            cv.put(DataConfig.COLUMN_CHAT_TYPE, chat_type);
            db.insert(DataConfig.TABLE_CHAT, null, cv);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }

    }

    public int addFolder(String name, String desc, String path) {
        Log.d(tag, "Adding folder into database: " + name + ", " + desc + ", " + path);

        int fid = -1;
        try {
            db.beginTransaction();

            ContentValues cv = new ContentValues();
            cv.put(DataConfig.COLUMN_FOLDER_NAME, name);
            cv.put(DataConfig.COLUMN_FOLDER_DESC, desc);
            cv.put(DataConfig.COLUMN_FOLDER_PATH, path);

            db.insert(DataConfig.TABLE_FOLDER, null, cv);

            Log.d(tag, "get fid of new folder");
            String query = "SELECT MAX(" + DataConfig.COLUMN_FOLDER_ID
                    + ") FROM " + DataConfig.TABLE_FOLDER;
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()){
                do {
                    fid = cursor.getInt(0);
                } while(cursor.moveToNext());
            }

            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }

        return fid;
    }

    public HashMap<String, String> loadFolders(String currentPath) {
        Log.d(tag, "load folders at path: " + currentPath);

        HashMap<String, String> folders = new HashMap<>();
        Cursor c = db.rawQuery("SELECT * FROM " + DataConfig.TABLE_FOLDER
                    + " WHERE " + DataConfig.COLUMN_FOLDER_PATH + " = ?",
                new String[]{currentPath});

        String name, desc;
        while (c.moveToNext()) {
            name = c.getString(c.getColumnIndex(DataConfig.COLUMN_FOLDER_NAME));
            desc = c.getString(c.getColumnIndex(DataConfig.COLUMN_FOLDER_DESC));
            Log.d(tag, "found folder: " + name + ", " + desc);
            folders.put(name, desc);
        }
        c.close();

        return folders;
    }


    public ArrayList<Activity> loadActiveActivitiesByPath(String currentPath) {
        Log.i(tag, "Load active activities on path: " + currentPath);
        ArrayList<Activity> activities = new ArrayList<>();
        Cursor c = db.rawQuery("SELECT * FROM " + DataConfig.TABLE_ACTIVITY +
                        " where " + DataConfig.COLUMN_ACTIVITY_ACTIVE + " = ? " +
                        " and " + DataConfig.COLUMN_ACTIVITY_PATH + " = ? ",
                new String[]{DataConfig.VALUE_ACTIVITY_ACTIVE, currentPath});

        while (c.moveToNext()) {
            Activity p = new Activity(c);
            Log.d(tag, "found activity: " + p.pname);
            activities.add(p);
        }
        c.close();
        return activities;

    }
}