package com.soco.SoCoClient.datamodel;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.soco.SoCoClient.Config;

public class DBManagerSoco {
    private DBHelperSoco helper;
    private SQLiteDatabase db;

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
            Log.i("db", "Insert new program: " + program.pname + ", "
                    + program.pdate + ", " + program.ptime + ", " + program.pplace + ", "
                    + program.pdesc);
            db.execSQL("INSERT INTO " + Config.TABLE_PROGRAM + " VALUES(null, ?, ?, ?, ?, ?, ?)",
                    new Object[]{program.pname, program.pdate, program.ptime, program.pplace, 0,
                    program.pdesc});
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

//    public List<Program> loadProgram() {
//        ArrayList<Program> programs = new ArrayList<>();
//        Cursor c = queryTheCursor();
//        while (c.moveToNext()) {
//            Program program = new Program();
//            program.pid = c.getInt(c.getColumnIndex("pid"));
//            program.pname = c.getString(c.getColumnIndex("pname"));
//            program.pdate = c.getString(c.getColumnIndex("pdate"));
//            program.ptime = c.getString(c.getColumnIndex("ptime"));
//            program.pplace = c.getString(c.getColumnIndex("pplace"));
//            program.pcomplete = c.getInt(c.getColumnIndex("pcomplete"));
//            programs.add(program);
//        }
//        c.close();
//        return programs;
//    }

    public ArrayList<Program> loadPrograms(int pcompleted) {
        Log.i("db", "Load programs where pcomplete is " + pcompleted);
        ArrayList<Program> programs = new ArrayList<>();
        Cursor c = queryTheCursor(pcompleted);
        while (c.moveToNext()) {
            Program program = new Program(c);
//            program.pid = c.getInt(c.getColumnIndex(Config.COLUMN_PID));
//            program.pname = c.getString(c.getColumnIndex(Config.COLUMN_PNAME));
//            program.pdate = c.getString(c.getColumnIndex(Config.COLUMN_PDATE));
//            program.ptime = c.getString(c.getColumnIndex(Config.COLUMN_PTIME));
//            program.pplace = c.getString(c.getColumnIndex(Config.COLUMN_PPLACE));
//            program.pcomplete = c.getInt(c.getColumnIndex(Config.COLUMN_PCOMPLETE));
            programs.add(program);
        }
        c.close();
        return programs;
    }

    public Program loadProgram(String pname) {
        Log.i("db", "Load programs where pname is " + pname);
        Program program = null;
        Cursor c = queryTheCursor(pname);
        while (c.moveToNext()) {
            program = new Program(c);
//            program.pid = c.getInt(c.getColumnIndex(Config.COLUMN_PID));
//            program.pname = c.getString(c.getColumnIndex(Config.COLUMN_PNAME));
//            program.pdate = c.getString(c.getColumnIndex(Config.COLUMN_PDATE));
//            program.ptime = c.getString(c.getColumnIndex(Config.COLUMN_PTIME));
//            program.pplace = c.getString(c.getColumnIndex(Config.COLUMN_PPLACE));
//            program.pcomplete = c.getInt(c.getColumnIndex(Config.COLUMN_PCOMPLETE));
        }
        c.close();
        return program;
    }

    public Cursor queryTheCursor(String pname) {
        return db.rawQuery("SELECT * FROM " + Config.TABLE_PROGRAM +
                        " where " + Config.COLUMN_PNAME + " = ?",
                new String[] {pname});
    }

//    public Cursor queryTheCursor() {
//        return db.rawQuery("SELECT * FROM program", null);
//    }

    public Cursor queryTheCursor(int pcomplete) {
        return db.rawQuery("SELECT * FROM " + Config.TABLE_PROGRAM +
                        " where " + Config.COLUMN_PCOMPLETE + " = ?",
                new String[] {String.valueOf(pcomplete)});
    }

    public void update(Program program) {
        ContentValues cv = new ContentValues();
        cv.put(Config.COLUMN_PDATE, program.pdate);
        cv.put(Config.COLUMN_PTIME, program.ptime);
        cv.put(Config.COLUMN_PPLACE, program.pplace);
        cv.put(Config.COLUMN_PCOMPLETE, program.pcomplete);
        cv.put(Config.COLUMN_PDESC, program.pdesc);

        db.update(Config.TABLE_PROGRAM, cv, Config.COLUMN_PNAME + " = ?",
                new String[]{program.pname});
    }

//    public void closeDB() {
//        db.close();
//    }

}