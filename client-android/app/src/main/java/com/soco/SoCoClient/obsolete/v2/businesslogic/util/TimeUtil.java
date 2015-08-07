package com.soco.SoCoClient.obsolete.v2.businesslogic.util;

import java.text.SimpleDateFormat;
import java.util.Date;


public class TimeUtil {

    public static String now() {
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String s = f.format(new Date());
        return s;
    }

}
