package com.penglecode.xmodule.master4j.spring.common.logger;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import org.slf4j.LoggerFactory;

/**
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/22 9:52
 */
public class LoggerUtils {

    public static void setLoggerLevel(String loggerName, String loggerLevel) {
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        Logger logger = loggerContext.getLogger(loggerName);
        logger.setLevel(Level.toLevel(loggerLevel));
    }

    public static void setLoggerLevel(Class<?> loggerName, String loggerLevel) {
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        Logger logger = loggerContext.getLogger(loggerName);
        logger.setLevel(Level.toLevel(loggerLevel));
    }

}
