package com.ngv.zns_service.util.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DebugLogger {
    private static Logger logger = LoggerFactory.getLogger(DebugLogger.class);
    public static void debug(String data){
        logger.debug(data);
    }
    public static void debug(String data, Object... objects){
        logger.debug(data, objects);
    }
    public static void info(String data){
        logger.info(data);
    }
    public static void info(String data, Object... objects){
        logger.info(data, objects);
    }
    public static void error(String data){
        logger.error(data);
    }
    public static void error(String data, Object... objects){
        logger.error(data, objects);
    }
}
