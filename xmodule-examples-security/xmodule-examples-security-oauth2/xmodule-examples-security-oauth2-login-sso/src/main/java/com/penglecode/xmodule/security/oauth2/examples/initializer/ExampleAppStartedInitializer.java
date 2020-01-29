package com.penglecode.xmodule.security.oauth2.examples.initializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import com.penglecode.xmodule.common.initializer.InitializationRunAt;
import com.penglecode.xmodule.common.initializer.SpringAppInitializer;

@Component
@InitializationRunAt(ApplicationStartedEvent.class)
public class ExampleAppStartedInitializer implements SpringAppInitializer {

	private static final Logger LOGGER = LoggerFactory.getLogger(ExampleAppStartedInitializer.class);
	
	@Override
	public void initialize(ConfigurableApplicationContext applicationContext) throws Exception {
		LOGGER.info(">>> Example application started! applicationContext = {}", applicationContext);
	}

}
