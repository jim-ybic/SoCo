package com.soco.SoCoClient.common.util;

import com.soco.SoCoClient.events.model.Event;

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
    public static String getDayOfStartDate(String dateString){
        Date d =  TimeUtil.getDate(dateString);
        if(d==null){
            return "";
        }
        return TimeUtil.getDayOfWeek(d);
    }
    public static String getTextDate(String dateString,String format){
        if("yyyy-MM-dd".equalsIgnoreCase(format)){
            return dateString;
        }
        Date date = TimeUtil.getDate(dateString);
        return TimeUtil.getDateToString(date, format);
    }
    public static String getTextStartEndTime(Event event){
        StringBuffer sb = new StringBuffer();
        if(!StringUtil.isEmptyString(event.getStart_time())){
            sb.append(event.getStart_time());
        }
        if(!StringUtil.isEmptyString(event.getEnd_time())){
            sb.append("~");
            sb.append(event.getEnd_time());
        }
        return sb.toString();
    }
}
