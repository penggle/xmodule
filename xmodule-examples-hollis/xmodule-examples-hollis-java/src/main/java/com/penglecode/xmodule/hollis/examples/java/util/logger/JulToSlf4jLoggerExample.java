package com.penglecode.xmodule.hollis.examples.java.util.logger;

import java.time.LocalDateTime;

import org.slf4j.Logger;

public class JulToSlf4jLoggerExample {

	private static final Logger LOGGER = LoggerFactory.getLogger(JulToSlf4jLoggerExample.class);
	
	public static void main(String[] args) {
		LOGGER.debug(">>> program startup ...");
		LOGGER.info(">>> now time is: {}", LocalDateTime.now());
		LOGGER.debug("<<< program shutdown ...");
	}

}
