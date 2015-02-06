package com.soco.SoCoClient.control.db;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.soco.SoCoClient.control.config.DatabaseConfig;
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
        db.execSQL("delete from " + DatabaseConfig.TABLE_PROGRAM);

        //update: 20150206
        db.execSQL("delete from " + DatabaseConfig.TABLE_PROJECT);
        db.execSQL("delete from " + DatabaseConfig.TABLE_ATTRIBUTE);
    }

    //todo: decommission
    public void add(Program program) {
        Log.i(tag, "Add new program: " + program.pname + ", "
                + program.pdate + ", " + program.ptime + ", " + program.pplace + ", "
                + program.pdesc + ", " + program.pphone + ", " + program.pemail + ", "
                + program.pwechat);

        try {
            db.beginTransaction();
            db.execSQL("INSERT INTO " + DatabaseConfig.TABLE_PROGRAM
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
                    + SignatureUtil.genSHA1(p) + ", " + DatabaseConfig.VALUE_PROJECT_ACTIVE);
            db.execSQL("INSERT INTO " + DatabaseConfig.TABLE_PROJECT + " VALUES(null, ?, ?, ?, ?, ?, ?)",
                    new Object[]{p.pname, "",
                            SignatureUtil.now(), SignatureUtil.now(), SignatureUtil.genSHA1(p),
                            DatabaseConfig.VALUE_PROJECT_ACTIVE});
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
//            //todo
//            db.setTransactionSuccessful();
//        } finally {
//            db.endTransaction();
//        }
//    }

    public void delete(int pid){
        Log.i(tag, "Delete programs where pid is " + pid);
        db.delete(DatabaseConfig.TABLE_PROGRAM, DatabaseConfig.COLUMN_PID + " == ?",
                new String[]{String.valueOf(pid)});
    }

    //todo: decommission
    public ArrayList<Program> loadPrograms(int pcompleted) {
        Log.i(tag, "Load programs where pcomplete is " + pcompleted);
        ArrayList<Program> programs = new ArrayList<>();
        Cursor c = queryTheCursor(pcompleted);
        while (c.moveToNext()) {
            Program program = new Program(c);
            programs.add(program);
        }
        c.close();
        return programs;
    }

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

    //todo: decommission
    public Program loadProgram(String pname) {
        Log.i(tag, "Load programs where pname is " + pname);
        Program program = null;
        Cursor c = queryTheCursor(pname);
        while (c.moveToNext()) {
            program = new Program(c);
        }
        c.close();
        return program;
    }

    public Project loadProject(String pname) {
        Log.i(tag, "Load project by name: " + pname);
        Project p = null;
        Cursor c = queryProjectByPname(pname);
        while (c.moveToNext()) {
            p = new Project(c);
        }
        c.close();
        return p;
    }

    public Project loadProjectByPid(int pid) {
        Log.i(tag, "Load project by id: " + pid);
        Project p = null;
        Cursor c = queryProjectByPid(pid);
        while (c.moveToNext()) {
            p = new Project(c);
        }
        c.close();
        return p;
    }

    public Cursor queryProjectByPid(int pid) {
        Log.d(tag, "Query project: select * from " + DatabaseConfig.TABLE_PROJECT
                + " where " + DatabaseConfig.COLUMN_PROJECT_ID + " = " + pid);
        return db.rawQuery("SELECT * FROM " + DatabaseConfig.TABLE_PROJECT +
                        " where " + DatabaseConfig.COLUMN_PROJECT_ID + " = ?",
                new String[] {String.valueOf(pid)});
    }

    public Cursor queryProjectByPname(String pname) {
        Log.d(tag, "Query project: select * from " + DatabaseConfig.TABLE_PROJECT
            + " where " + DatabaseConfig.COLUMN_PROJECT_NAME + " = " + pname);
        return db.rawQuery("SELECT * FROM " + DatabaseConfig.TABLE_PROJECT +
                        " where " + DatabaseConfig.COLUMN_PROJECT_NAME + " = ?",
                new String[] {pname});
    }

    public Cursor queryTheCursor(String pname) {
        return db.rawQuery("SELECT * FROM " + DatabaseConfig.TABLE_PROGRAM +
                        " where " + DatabaseConfig.COLUMN_PNAME + " = ?",
                new String[] {pname});
    }

    public Cursor queryTheCursor(int pcomplete) {
        return db.rawQuery("SELECT * FROM " + DatabaseConfig.TABLE_PROGRAM +
                        " where " + DatabaseConfig.COLUMN_PCOMPLETE + " = ?",
                new String[] {String.valueOf(pcomplete)});
    }

    public Cursor queryProjectByActiveness(String pactive) {
        Log.d(tag, "Query project: select * from " + DatabaseConfig.TABLE_PROJECT
                + " where " + DatabaseConfig.COLUMN_PROJECT_ACTIVE + " = " + pactive);
        return db.rawQuery("SELECT * FROM " + DatabaseConfig.TABLE_PROJECT +
                        " where " + DatabaseConfig.COLUMN_PROJECT_ACTIVE + " = ?",
                new String[] {pactive});
    }

    public void updateProject(Project p){
        Log.i(tag, "Update database for project: " + p.pname);

        ContentValues cv = new ContentValues();
        cv.put(DatabaseConfig.COLUMN_PROJECT_NAME, p.pname);
//        cv.put(DbConfig.COLUMN_PDATE, p.pdate);
//        cv.put(DbConfig.COLUMN_PTIME, p.ptime);
//        cv.put(DbConfig.COLUMN_PPLACE, p.pplace);
//        cv.put(DbConfig.COLUMN_PCOMPLETE, p.pcomplete);
//        cv.put(DbConfig.COLUMN_PDESC, p.pdesc);
//        cv.put(DbConfig.COLUMN_PPHONE, p.pphone);
//        cv.put(DbConfig.COLUMN_PEMAIL, p.pemail);
//        cv.put(DbConfig.COLUMN_PWECHAT, p.pwechat);

        db.update(DatabaseConfig.TABLE_PROJECT, cv, DatabaseConfig.COLUMN_PROJECT_ID + " = ?",
                new String[]{String.valueOf(p.pid)});
    }

    public void update(String original_pname, Program p) {
        Log.i(tag, "Update database for program: " + p.toString());

        ContentValues cv = new ContentValues();
        cv.put(DatabaseConfig.COLUMN_PNAME, p.pname);
        cv.put(DatabaseConfig.COLUMN_PDATE, p.pdate);
        cv.put(DatabaseConfig.COLUMN_PTIME, p.ptime);
        cv.put(DatabaseConfig.COLUMN_PPLACE, p.pplace);
        cv.put(DatabaseConfig.COLUMN_PCOMPLETE, p.pcomplete);
        cv.put(DatabaseConfig.COLUMN_PDESC, p.pdesc);
        cv.put(DatabaseConfig.COLUMN_PPHONE, p.pphone);
        cv.put(DatabaseConfig.COLUMN_PEMAIL, p.pemail);
        cv.put(DatabaseConfig.COLUMN_PWECHAT, p.pwechat);

        db.update(DatabaseConfig.TABLE_PROGRAM, cv, DatabaseConfig.COLUMN_PNAME + " = ?",
                new String[]{original_pname});
    }

//    public void closeDB() {
//        db.close();
//    }

}