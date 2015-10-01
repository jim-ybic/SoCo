package com.soco.log;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Log {
    
	private static final Logger log = LogManager.getRootLogger();

    public static void infor(String msg){
        log.info(msg);
    }
    
    public static void debug(String msg){
        log.debug(msg);
    }
    
    public static void error(String msg){
        log.error(msg);
    }
}
