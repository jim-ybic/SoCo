package com.soco.SoCoClient.db;

public class Program {
    public int pid;
    public String pname = "";
    public String pdate = "";
    public String ptime = "";
    public String pplace = "";
    public int pcomplete = 0;

//    public static String PROGRAM_PID = "PROGRAM_PID";
    public static String PROGRAM_PNAME = "PROGRAM_PNAME";
//    public static String PROGRAM_PDATE = "PROGRAM_PDATE";
//    public static String PROGRAM_PTIME = "PROGRAM_PTIME";
//    public static String PROGRAM_PPLACE = "PROGRAM_PPLACE";
//    public static String PROGRAM_PCOMPLETE = "PROGRAM_PCOMPLETE";

    public static String PROGRAM_PINFO = "PROGRAM_PINFO";

    public static int PROGRAM_COMPLETED = 1;


    public Program() {
    }

//    public Program(String pname) {
//        this.pname = pname;
//    }

//    public Program(String pname, String pdate, String ptime, String pplace) {
//        this.pname = pname;
//        this.pdate = pdate;
//        this.ptime = ptime;
//        this.pplace = pplace;
//        this.pcomplete = 0;
//    }

    public String toString() {
        return this.pname + ", " + this.pdate + ", " + this.ptime + ", " + this.pplace;
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

        return s;
    }
}
