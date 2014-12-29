package com.soco.SoCoClient.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DBManagerSoco {
    private DBHelperSoco helper;
    private SQLiteDatabase db;

    public DBManagerSoco(Context context) {
        helper = new DBHelperSoco(context);
        db = helper.getWritableDatabase();
    }

    public void cleanDB() {
        db.execSQL("delete from program");
    }

    public void add(Program program) {
        db.beginTransaction();
        try {
            String pname = program.pname;
            String pdate = program.pdate;
            String ptime = program.ptime;
            String pplace = program.pplace;
            int pcomplete = 0;
            db.execSQL("INSERT INTO program VALUES(null, ?, ?, ?, ?, ?)",
                    new Object[]{pname, pdate, ptime, pplace, pcomplete});
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    public List<Program> loadProgram() {
        ArrayList<Program> programs = new ArrayList<>();
        Cursor c = queryTheCursor();
        while (c.moveToNext()) {
            Program program = new Program();
            program.pid = c.getInt(c.getColumnIndex("pid"));
            program.pname = c.getString(c.getColumnIndex("pname"));
            program.pdate = c.getString(c.getColumnIndex("pdate"));
            program.ptime = c.getString(c.getColumnIndex("ptime"));
            program.pplace = c.getString(c.getColumnIndex("pplace"));
            program.pcomplete = c.getInt(c.getColumnIndex("pcomplete"));
            programs.add(program);
        }
        c.close();
        return programs;
    }

    public Program loadProgram(String pname) {
        Program program = null;
        Cursor c = queryTheCursor(pname);
        while (c.moveToNext()) {
            program = new Program();
            program.pid = c.getInt(c.getColumnIndex("pid"));
            program.pname = c.getString(c.getColumnIndex("pname"));
            program.pdate = c.getString(c.getColumnIndex("pdate"));
            program.ptime = c.getString(c.getColumnIndex("ptime"));
            program.pplace = c.getString(c.getColumnIndex("pplace"));
            program.pcomplete = c.getInt(c.getColumnIndex("pcomplete"));
        }
        c.close();
        return program;
    }

    public Cursor queryTheCursor(String pname) {
        return db.rawQuery("SELECT * FROM program where pname = ?",
                new String[] {pname});
    }

    public Cursor queryTheCursor() {
        return db.rawQuery("SELECT * FROM program", null);
    }

    // Given a pname, update other fields (pdate, ptime, and pplace)
    public void update(Program program) {
        ContentValues cv = new ContentValues();
        cv.put("pdate", program.pdate);
        cv.put("ptime", program.ptime);
        cv.put("pplace", program.pplace);
        cv.put("pcomplete", program.pcomplete);
        db.update("program", cv, "pname = ?", new String[]{program.pname});
    }

//    public void closeDB() {
//        db.close();
//    }

}