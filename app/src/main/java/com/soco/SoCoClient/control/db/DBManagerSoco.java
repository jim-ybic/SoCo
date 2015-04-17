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
import com.soco.SoCoClient.control.util.SignatureUtil;
import com.soco.SoCoClient.model.Project;

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

    public void addMemberToProject(String userName, int pid){
        Log.d(tag, "Adding member " + userName + " into project pid " + pid);

        try {
            db.beginTransaction();
            Log.i(tag, "insert activity_user table entry: " + pid + ", " + userName);
            db.execSQL("INSERT INTO " + DataConfig.TABLE_ACTIVITY_MEMBER + " VALUES (" +
                    "?, ?, ?, ?, ?)", new Object[]{
                    pid, TEST_NOEMAIL, userName, SignatureUtil.now(), ""});    //todo: add more details
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    public ArrayList<ArrayList<String>> getUpdatesOfActivity(int aid) {
        Log.d(tag, "get comments of activity " + aid);
//        HashMap<String, String> userComment = new HashMap<>();
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

    public HashMap<String, String> getMembersOfProject(int pid){
        Log.d(tag, "get members of project " + pid);
        HashMap<String, String> userNameEmail = new HashMap<>();

        Cursor c = db.rawQuery("SELECT * FROM " + DataConfig.TABLE_ACTIVITY_MEMBER +
                        " where " + DataConfig.COLUMN_ACTIVITY_MEMBER_AID + " = ?",
                new String[] {String.valueOf(pid)});

        String name, email;
        while (c.moveToNext()) {
            name = c.getString(c.getColumnIndex(DataConfig.COLUMN_ACTIVITY_MEMBER_MEMBER_USERNAME));
            email = c.getString(c.getColumnIndex(DataConfig.COLUMN_ACTIVITY_MEMBER_MEMBER_EMAIL));
            Log.d(tag, "Found user " + name + "/" + email);
            userNameEmail.put(email, name);
        }
        c.close();
        return userNameEmail;
    }

    public int addProject(Project p){
        Log.i(tag, "Add new project: " + p.pname);
        int pid = -1;
        String userEmail = ((SocoApp)context).loginEmail;
        try {
            db.beginTransaction();

            //add into activity table
            Log.i(tag, "Insert into db: " + p.pname + ", "
                    + SignatureUtil.now() + ", " + SignatureUtil.now() + ", "
                    + SignatureUtil.genSHA1(p) + ", " + DataConfig.VALUE_ACTIVITY_ACTIVE);
            db.execSQL("INSERT INTO " + DataConfig.TABLE_ACTIVITY
                            + " VALUES(null, ?, ?, ?, ?, ?, ?, ?)",
                    new Object[]{p.pname, "",
                            SignatureUtil.now(), SignatureUtil.now(), SignatureUtil.genSHA1(p),
                            DataConfig.VALUE_ACTIVITY_ACTIVE, null});

            //get pid
            String query = "SELECT MAX(" + DataConfig.COLUMN_ACTIVITY_ID
                    + ") FROM " + DataConfig.TABLE_ACTIVITY;
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()){
                do {
                    pid = cursor.getInt(0);
                } while(cursor.moveToNext());
            }

            //add into activity_user table
            Log.i(tag, "insert activity_user table entry: " + pid + ", " + userEmail);
            db.execSQL("INSERT INTO " + DataConfig.TABLE_ACTIVITY_MEMBER + " VALUES (" +
                    "?, ?, ?, ?, ?)", new Object[]{
                    pid, userEmail, TEST_NONAME, SignatureUtil.now(), ""});    //todo: add more details

            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }

        Log.i(tag, "New project added with local pid: " + pid);
        return pid;
    }

    public void deleteProjectByPid(int pid){
        Log.i(tag, "Delete project by pid: " + pid);
        db.delete(DataConfig.TABLE_ACTIVITY, DataConfig.COLUMN_ACTIVITY_ID + " = ?",
                new String[]{String.valueOf(pid)});
        db.delete(DataConfig.TABLE_ATTRIBUTE, DataConfig.COLUMN_ATTRIBUTE_PID + " = ?",
                new String[]{String.valueOf(pid)});
    }

    public ArrayList<Project> loadProjectsByActiveness(String pactive) {
        Log.i(tag, "Load projects which are: " + pactive);
        ArrayList<Project> projects = new ArrayList<>();

        Log.d(tag, "Query project: select * from " + DataConfig.TABLE_ACTIVITY
                + " where " + DataConfig.COLUMN_ACTIVITY_ACTIVE + " = " + pactive);
        Cursor c = db.rawQuery("SELECT * FROM " + DataConfig.TABLE_ACTIVITY +
                        " where " + DataConfig.COLUMN_ACTIVITY_ACTIVE + " = ?",
                new String[]{pactive});

        while (c.moveToNext()) {
            Project p = new Project(c);
            projects.add(p);
        }
        c.close();
        return projects;
    }

    public Project loadProjectByPid(int pid) {
        Log.i(tag, "Load project for pid: " + pid);
        Project p = null;

        Log.d(tag, "Query project: select * from " + DataConfig.TABLE_ACTIVITY
                + " where " + DataConfig.COLUMN_ACTIVITY_ID + " = " + pid);
        Cursor c = db.rawQuery("SELECT * FROM " + DataConfig.TABLE_ACTIVITY +
                        " where " + DataConfig.COLUMN_ACTIVITY_ID + " = ?",
                new String[] {String.valueOf(pid)});

        while (c.moveToNext()) {
            p = new Project(c);
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

    public String findProjectIdOnserver(int pid){
        Project p = loadProjectByPid(pid);
        return p.pid_onserver;
    }

    public ArrayList<HashMap<String, String>> loadProjectAttributesByPid(int pid){
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

    public void clearProjectAttributesExceptLocation(int pid){
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

    public void updateDbProjectAttributes(int pid, HashMap<String, String> attrMap){
        clearProjectAttributesExceptLocation(pid);

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

    public void updateProjectName(int pid, String pname){
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

    public void updateProjectTag(int pid, String ptag){
        Log.i(tag, "Update database for project: " + pid);

        ContentValues cv = new ContentValues();
        cv.put(DataConfig.COLUMN_ACTIVITY_TAG, ptag);
        String now = SignatureUtil.now();
        cv.put(DataConfig.COLUMN_ACTIVITY_UPDATE_TIMESTAMP, now);

        db.update(DataConfig.TABLE_ACTIVITY, cv, DataConfig.COLUMN_ACTIVITY_ID + " = ?",
                new String[]{String.valueOf(pid)});

        Log.i(tag, "Updated project " + pid + " tag: " + ptag);
        Log.i(tag, "Updated project " + pid + " update timestamp: " + now);
    }

    public void updateProjectActiveness(int pid, String activeness) {
        Log.i(tag, "Update project " + pid + " status: " + activeness);
        ContentValues cv = new ContentValues();
        cv.put(DataConfig.COLUMN_ACTIVITY_ACTIVE, activeness);
        db.update(DataConfig.TABLE_ACTIVITY, cv, DataConfig.COLUMN_ACTIVITY_ID + " = ?",
                new String[]{String.valueOf(pid)});
    }

    public void updateProjectIdOnserver(int pid, String pid_onserver) {
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

}