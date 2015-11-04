package com.soco.SoCoClient.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class TimeUtil {

    public static String now() {
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String s = f.format(new Date());
        return s;
    }
    public static Date getDate(String dateString){
        String format = "yyyy-MM-dd";
        return getDate(dateString,format);
    }
    public static Date getDate(String dateString, String dateInputFormat){
        SimpleDateFormat f = new SimpleDateFormat(dateInputFormat);
        try {
            return  f.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static String getDayOfWeek(Date date){
        SimpleDateFormat simpleDateformat = new SimpleDateFormat("E"); // the day of the week abbreviated
        return simpleDateformat.format(date);
    }
    public static String getDateToString(Date date, String outputFormat){
        SimpleDateFormat f = new SimpleDateFormat(outputFormat);
        return f.format(date);
    }
}
