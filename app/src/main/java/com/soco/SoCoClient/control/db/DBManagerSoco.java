package com.soco.SoCoClient.control.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.soco.SoCoClient.control.config.DataConfig;
import com.soco.SoCoClient.control.util.SignatureUtil;
import com.soco.SoCoClient.model.Program;
import com.soco.SoCoClient.model.Project;

public class DBManagerSoco {
    private DBHelperSoco helper;
    private SQLiteDatabase db;

    public static String tag = "DBManagerSoco";

    public DBManagerSoco(Context context) {
        helper = new DBHelperSoco(context);
        db = helper.getWritableDatabase();
    }

    public void cleanDB() {
        Log.i(tag, "Clean up database.");

        //todo: decommission
        db.execSQL("delete from " + DataConfig.TABLE_PROGRAM);

        //update: 20150206
        db.execSQL("delete from " + DataConfig.TABLE_PROJECT);
        db.execSQL("delete from " + DataConfig.TABLE_ATTRIBUTE);
    }

    public void add(Program program) {
        Log.i(tag, "Add new program: " + program.pname + ", "
                + program.pdate + ", " + program.ptime + ", " + program.pplace + ", "
                + program.pdesc + ", " + program.pphone + ", " + program.pemail + ", "
                + program.pwechat);

        try {
            db.beginTransaction();
            db.execSQL("INSERT INTO " + DataConfig.TABLE_PROGRAM
                            + " VALUES(null, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                    new Object[]{program.pname, program.pdate, program.ptime, program.pplace, 0,
                    program.pdesc, program.pphone, program.pemail, program.pwechat});
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    public void addProject(Project p){
        Log.i(tag, "Add new project (update 20150206): " + p.pname);
        try {
            db.beginTransaction();
            Log.i(tag, "Insert into db: " + p.pname + ", "
                    + SignatureUtil.now() + ", " + SignatureUtil.now() + ", "
                    + SignatureUtil.genSHA1(p) + ", " + DataConfig.VALUE_PROJECT_ACTIVE);
            db.execSQL("INSERT INTO " + DataConfig.TABLE_PROJECT + " VALUES(null, ?, ?, ?, ?, ?, ?)",
                    new Object[]{p.pname, "",
                            SignatureUtil.now(), SignatureUtil.now(), SignatureUtil.genSHA1(p),
                            DataConfig.VALUE_PROJECT_ACTIVE});
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

//    public void addAttributeFromProject(Project p){
//        Log.i(tag, "Add attribute from project (update 20150206): " + p.pname);
//        int pid = p.pid;
//
//        //collect attributes
//        HashMap<String,String> attrMap = new HashMap<String, String>();
//        if(p.)
//        //save to db
//        try {
//            db.beginTransaction();
//            for (Map.Entry<String, String> entry:attrMap.entrySet()){
//                String aname = entry.getKey();
//                String avalue = entry.getValue();
//            }
//            db.setTransactionSuccessful();
//        } finally {
//            db.endTransaction();
//        }
//    }

//    public void delete(int pid){
//        Log.i(tag, "Delete programs where pid is " + pid);
//        db.delete(DataConfig.TABLE_PROGRAM, DataConfig.COLUMN_PID + " == ?",
//                new String[]{String.valueOf(pid)});
//    }

    public void deleteProjectByPid(int pid){
        Log.i(tag, "Delete project by pid: " + pid);
        db.delete(DataConfig.TABLE_PROJECT, DataConfig.COLUMN_PROJECT_ID + " = ?",
                new String[]{String.valueOf(pid)});
        db.delete(DataConfig.TABLE_ATTRIBUTE, DataConfig.COLUMN_ATTRIBUTE_PID + " = ?",
                new String[]{String.valueOf(pid)});
    }

//    public ArrayList<Program> loadPrograms(int pcompleted) {
//        Log.i(tag, "Load programs where pcomplete is " + pcompleted);
//        ArrayList<Program> programs = new ArrayList<>();
//        Cursor c = queryTheCursor(pcompleted);
//        while (c.moveToNext()) {
//            Program program = new Program(c);
//            programs.add(program);
//        }
//        c.close();
//        return programs;
//    }

    public ArrayList<Project> loadProjectsByActiveness(String pactive) {
        Log.i(tag, "Load projects which are: " + pactive);
        ArrayList<Project> projects = new ArrayList<>();
        Cursor c = queryProjectByActiveness(pactive);
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
        Cursor c = queryProjectByPid(pid);
        while (c.moveToNext()) {
            p = new Project(c);
        }
        c.close();
        return p;
    }

    public ArrayList<HashMap<String, String>> loadProjectAttributesByPid(int pid){
        Log.i(tag, "Load project attributes for pid: " + pid);
        ArrayList<HashMap<String, String>> list = new ArrayList<>();
        Cursor c = queryProjectAttributesByPid(pid);
        int count = 0;
        while (c.moveToNext()){
            String attr_name = c.getString(c.getColumnIndex(DataConfig.COLUMN_ATTRIBUTE_NAME));
            String attr_value = c.getString(c.getColumnIndex(DataConfig.COLUMN_ATTRIBUTE_VALUE));
            Log.i(tag, "Found attribute: " + attr_name + ", " + attr_value);
            HashMap<String, String> attrMap = new HashMap<>();
            attrMap.put(attr_name, attr_value);
            list.add(attrMap);
            count ++;
        }
        Log.i(tag, count + " attributes loaded for pid " + pid);
        return list;
    }

    public Cursor queryProjectAttributesByPid(int pid) {
        Log.d(tag, "Query project attributes: select * from " + DataConfig.TABLE_ATTRIBUTE
                + " where " + DataConfig.COLUMN_PROJECT_ID + " = " + pid);
        return db.rawQuery("SELECT * FROM " + DataConfig.TABLE_ATTRIBUTE +
                        " where " + DataConfig.COLUMN_PROJECT_ID + " = ?",
                new String[] {String.valueOf(pid)});
    }

    public Cursor queryProjectByPid(int pid) {
        Log.d(tag, "Query project: select * from " + DataConfig.TABLE_PROJECT
                + " where " + DataConfig.COLUMN_PROJECT_ID + " = " + pid);
        return db.rawQuery("SELECT * FROM " + DataConfig.TABLE_PROJECT +
                        " where " + DataConfig.COLUMN_PROJECT_ID + " = ?",
                new String[] {String.valueOf(pid)});
    }

//    public Cursor queryProjectByPname(String pname) {
//        Log.d(tag, "Query project: select * from " + DataConfig.TABLE_PROJECT
//            + " where " + DataConfig.COLUMN_PROJECT_NAME + " = " + pname);
//        return db.rawQuery("SELECT * FROM " + DataConfig.TABLE_PROJECT +
//                        " where " + DataConfig.COLUMN_PROJECT_NAME + " = ?",
//                new String[] {pname});
//    }
//
//    public Cursor queryTheCursor(String pname) {
//        return db.rawQuery("SELECT * FROM " + DataConfig.TABLE_PROGRAM +
//                        " where " + DataConfig.COLUMN_PNAME + " = ?",
//                new String[] {pname});
//    }

    public Cursor queryTheCursor(int pcomplete) {
        return db.rawQuery("SELECT * FROM " + DataConfig.TABLE_PROGRAM +
                        " where " + DataConfig.COLUMN_PCOMPLETE + " = ?",
                new String[] {String.valueOf(pcomplete)});
    }

    public Cursor queryProjectByActiveness(String pactive) {
        Log.d(tag, "Query project: select * from " + DataConfig.TABLE_PROJECT
                + " where " + DataConfig.COLUMN_PROJECT_ACTIVE + " = " + pactive);
        return db.rawQuery("SELECT * FROM " + DataConfig.TABLE_PROJECT +
                        " where " + DataConfig.COLUMN_PROJECT_ACTIVE + " = ?",
                new String[] {pactive});
    }

    public void clearProjectAttributes(int pid){

        //todo: rawQuery seems not working for delete
//        return db.rawQuery("DELETE FROM " + DbConfig.TABLE_ATTRIBUTE +
//                        " where " + DbConfig.COLUMN_ATTRIBUTE_PID + " = ?",
//                new String[] {String.valueOf(pid)});

        try {
            db.beginTransaction();
            Log.d(tag, "Clear project attributes: DELETE FROM " + DataConfig.TABLE_ATTRIBUTE
                    + " where " + DataConfig.COLUMN_ATTRIBUTE_PID + " = " + pid);
            db.execSQL("DELETE FROM " + DataConfig.TABLE_ATTRIBUTE
                            + " WHERE " + DataConfig.COLUMN_ATTRIBUTE_PID + " = ?",
                    new Object[]{pid});
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }

    }

    public void updateDbProjectAttributes(int pid, HashMap<String, String> attrMap){
        clearProjectAttributes(pid);

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

    public void addSharedFileProjectAttribute(int pid, String remotePath){
        String now = SignatureUtil.now();
        try{
            db.beginTransaction();
            Log.i(tag, "Add shared file project attribute: INSERT INTO "
                    + DataConfig.TABLE_ATTRIBUTE + " VALUES(null, " + pid + ", "
                    + DataConfig.ATTRIBUTE_NAME_FILE_REMOTE_PATH + ", "
                    + remotePath);
            db.execSQL("INSERT INTO " + DataConfig.TABLE_ATTRIBUTE
                        + " VALUES(null, ?, ?, ?, ?, ?, ?)",
                        new Object[]{pid, DataConfig.ATTRIBUTE_NAME_FILE_REMOTE_PATH, remotePath,
                                        "", now, now});
            db.setTransactionSuccessful();
        } finally{
            db.endTransaction();
        }
    }

    public void updateProjectName(int pid, String pname){
        Log.i(tag, "Update database for project: " + pid);

        ContentValues cv = new ContentValues();
        cv.put(DataConfig.COLUMN_PROJECT_NAME, pname);
        String now = SignatureUtil.now();
        cv.put(DataConfig.COLUMN_PROJECT_UPDATE_TIMESTAMP, now);
//        cv.put(DbConfig.COLUMN_PDATE, p.pdate);
//        cv.put(DbConfig.COLUMN_PTIME, p.ptime);
//        cv.put(DbConfig.COLUMN_PPLACE, p.pplace);
//        cv.put(DbConfig.COLUMN_PCOMPLETE, p.pcomplete);
//        cv.put(DbConfig.COLUMN_PDESC, p.pdesc);
//        cv.put(DbConfig.COLUMN_PPHONE, p.pphone);
//        cv.put(DbConfig.COLUMN_PEMAIL, p.pemail);
//        cv.put(DbConfig.COLUMN_PWECHAT, p.pwechat);

        db.update(DataConfig.TABLE_PROJECT, cv, DataConfig.COLUMN_PROJECT_ID + " = ?",
                new String[]{String.valueOf(pid)});

        Log.i(tag, "Updated project " + pid + " name: " + pname);
        Log.i(tag, "Updated project " + pid + " update timestamp: " + now);
    }

    public void update(String original_pname, Program p) {
        Log.i(tag, "Update database for program: " + p.toString());

        ContentValues cv = new ContentValues();
        cv.put(DataConfig.COLUMN_PNAME, p.pname);
        cv.put(DataConfig.COLUMN_PDATE, p.pdate);
        cv.put(DataConfig.COLUMN_PTIME, p.ptime);
        cv.put(DataConfig.COLUMN_PPLACE, p.pplace);
        cv.put(DataConfig.COLUMN_PCOMPLETE, p.pcomplete);
        cv.put(DataConfig.COLUMN_PDESC, p.pdesc);
        cv.put(DataConfig.COLUMN_PPHONE, p.pphone);
        cv.put(DataConfig.COLUMN_PEMAIL, p.pemail);
        cv.put(DataConfig.COLUMN_PWECHAT, p.pwechat);

        db.update(DataConfig.TABLE_PROGRAM, cv, DataConfig.COLUMN_PNAME + " = ?",
                new String[]{original_pname});
    }

    public void updateProjectActiveness(int pid, String activeness) {
        Log.i(tag, "Update project " + pid + " status: " + activeness);
        ContentValues cv = new ContentValues();
        cv.put(DataConfig.COLUMN_PROJECT_ACTIVE, activeness);
        db.update(DataConfig.TABLE_PROJECT, cv, DataConfig.COLUMN_PROJECT_ID + " = ?",
                new String[]{String.valueOf(pid)});
    }

//    public void closeDB() {
//        db.close();
//    }

}