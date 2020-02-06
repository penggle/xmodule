package com.penglecode.xmodule.security.oauth2.examples.initializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import com.penglecode.xmodule.common.initializer.SpringWebAppStartupInitializer;

@Component
public class ExampleWebAppStartupInitializer implements SpringWebAppStartupInitializer {

	private static final Logger LOGGER = LoggerFactory.getLogger(ExampleWebAppStartupInitializer.class);
	
	@Override
	public void initialize(ConfigurableApplicationContext applicationContext) throws Exception {
		LOGGER.info(">>> Example web application started! applicationContext = {}", applicationContext);
	}

}
