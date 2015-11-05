package com.soco.SoCoClient.common.util;

/**
 * Created by David_WANG on 11/04/2015.
 */
public class StringUtil {
    public static boolean isEmptyString(String s){
        if(s!=null&&!"".equals(s)){
            return false;
        }else{
            return true;
        }
    }
}
