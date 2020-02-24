package com.penglecode.xmodule.hollis.examples.java.util.logger;

import org.slf4j.Logger;

public class LoggerFactory {

	static {
		String julLoggingConfigFilePath = LoggerFactory.class.getClassLoader().getResource("jul-logging.properties").getFile();
		System.setProperty("java.util.logging.config.file", julLoggingConfigFilePath);
	}
	
	public static Logger getLogger(Class<?> clazz) {
		return org.slf4j.LoggerFactory.getLogger(clazz);
	}
	
	public static Logger getLogger(String name) {
		return org.slf4j.LoggerFactory.getLogger(name);
	}
	
}
