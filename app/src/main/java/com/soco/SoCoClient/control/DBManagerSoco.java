package com.soco.SoCoClient.control;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.soco.SoCoClient.control.Config;
import com.soco.SoCoClient.control.DBHelperSoco;
import com.soco.SoCoClient.model.Program;

public class DBManagerSoco {
    private DBHelperSoco helper;
    private SQLiteDatabase db;

    public static String tag = "DBManagerSoco";

    public DBManagerSoco(Context context) {
        helper = new DBHelperSoco(context);
        db = helper.getWritableDatabase();
    }

    public void cleanDB() {
        Log.i("db", "Clean up database.");
        db.execSQL("delete from " + Config.TABLE_PROGRAM);
    }

    public void add(Program program) {
        db.beginTransaction();
        try {
            Log.i("db", "Add new program: " + program.pname + ", "
                    + program.pdate + ", " + program.ptime + ", " + program.pplace + ", "
                    + program.pdesc + ", " + program.pphone + ", " + program.pemail + ", "
                    + program.pwechat);
            db.execSQL("INSERT INTO " + Config.TABLE_PROGRAM
                            + " VALUES(null, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                    new Object[]{program.pname, program.pdate, program.ptime, program.pplace, 0,
                    program.pdesc, program.pphone, program.pemail, program.pwechat});
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    public void delete(int pid){
        Log.i("db", "Delete programs where pid is " + pid);
        db.delete(Config.TABLE_PROGRAM, Config.COLUMN_PID + " == ?",
                new String[]{String.valueOf(pid)});
    }

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

    public Cursor queryTheCursor(String pname) {
        return db.rawQuery("SELECT * FROM " + Config.TABLE_PROGRAM +
                        " where " + Config.COLUMN_PNAME + " = ?",
                new String[] {pname});
    }

    public Cursor queryTheCursor(int pcomplete) {
        return db.rawQuery("SELECT * FROM " + Config.TABLE_PROGRAM +
                        " where " + Config.COLUMN_PCOMPLETE + " = ?",
                new String[] {String.valueOf(pcomplete)});
    }

    public void update(String original_pname, Program p) {
        Log.i("db", "Update database for program: " + p.toString());

        ContentValues cv = new ContentValues();
        cv.put(Config.COLUMN_PNAME, p.pname);
        cv.put(Config.COLUMN_PDATE, p.pdate);
        cv.put(Config.COLUMN_PTIME, p.ptime);
        cv.put(Config.COLUMN_PPLACE, p.pplace);
        cv.put(Config.COLUMN_PCOMPLETE, p.pcomplete);
        cv.put(Config.COLUMN_PDESC, p.pdesc);
        cv.put(Config.COLUMN_PPHONE, p.pphone);
        cv.put(Config.COLUMN_PEMAIL, p.pemail);
        cv.put(Config.COLUMN_PWECHAT, p.pwechat);

        db.update(Config.TABLE_PROGRAM, cv, Config.COLUMN_PNAME + " = ?",
                new String[]{original_pname});
    }

//    public void closeDB() {
//        db.close();
//    }

}