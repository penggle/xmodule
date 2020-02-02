package com.penglecode.xmodule.security.oauth2.examples.initializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ConfigurableWebApplicationContext;

import com.penglecode.xmodule.common.initializer.SpringWebAppStartupInitializer;

@Component
public class ExampleWebAppStartupInitializer implements SpringWebAppStartupInitializer {

	private static final Logger LOGGER = LoggerFactory.getLogger(ExampleWebAppStartupInitializer.class);
	
	@Override
	public void initialize(ConfigurableWebApplicationContext applicationContext) throws Exception {
		LOGGER.info(">>> Example web application started! applicationContext = {}", applicationContext);
	}

}
