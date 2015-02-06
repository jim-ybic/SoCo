package com.soco.SoCoClient.model;

import android.database.Cursor;

import com.soco.SoCoClient.control.config.DatabaseConfig;

public class Program {

    public int pid;
    public String pname = "";
    public String pdate = "";
    public String ptime = "";
    public String pplace = "";
    public int pcomplete = 0;
    public String pdesc = "";
    public String pphone = "";
    public String pemail = "";
    public String pwechat = "";

    public Program(String name) {
        this.pname = name;
    }

    public Program(Cursor c) {
        this.pid = c.getInt(c.getColumnIndex(DatabaseConfig.COLUMN_PID));
        this.pname = c.getString(c.getColumnIndex(DatabaseConfig.COLUMN_PNAME));
        this.pdate = c.getString(c.getColumnIndex(DatabaseConfig.COLUMN_PDATE));
        this.ptime = c.getString(c.getColumnIndex(DatabaseConfig.COLUMN_PTIME));
        this.pplace = c.getString(c.getColumnIndex(DatabaseConfig.COLUMN_PPLACE));
        this.pcomplete = c.getInt(c.getColumnIndex(DatabaseConfig.COLUMN_PCOMPLETE));
        this.pdesc = c.getString(c.getColumnIndex(DatabaseConfig.COLUMN_PDESC));
        this.pphone = c.getString(c.getColumnIndex(DatabaseConfig.COLUMN_PPHONE));
        this.pemail = c.getString(c.getColumnIndex(DatabaseConfig.COLUMN_PEMAIL));
        this.pwechat = c.getString(c.getColumnIndex(DatabaseConfig.COLUMN_PWECHAT));
    }

    public String toString() {
        return this.pname + ", " + this.pdate + ", " + this.ptime + ", " + this.pplace + ", "
                + this.pdesc + ", " + this.pphone + ", " + this.pemail + ", " + this.pwechat;
    }

    public String getMoreInfo() {
        String s = new String(), t;
        if (!this.pdate.isEmpty()) {
            t = s.isEmpty() ? this.pdate : ", " + this.pdate;
            s += t;
        }
        if (!this.ptime.isEmpty()) {
            t = s.isEmpty() ? this.ptime : ", " + this.ptime;
            s += t;
        }
        if (!this.pplace.isEmpty()){
            t = s.isEmpty() ? this.pplace : ", " + this.pplace;
            s += t;
        }
        if (s.isEmpty())
            s = "No more detail";
        return s;
    }
}
