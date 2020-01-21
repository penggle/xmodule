package com.penglecode.xmodule.webflux.examples.initializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ConfigurableApplicationContext;

import com.penglecode.xmodule.common.initializer.InitializationRunAt;
import com.penglecode.xmodule.common.initializer.SpringAppInitializer;

@InitializationRunAt(ApplicationStartedEvent.class)
public class WebFluxExampleAppStartedInitializer implements SpringAppInitializer {

	private static final Logger LOGGER = LoggerFactory.getLogger(WebFluxExampleAppStartedInitializer.class);
	
	@Override
	public void initialize(ConfigurableApplicationContext applicationContext) throws Exception {
		LOGGER.info(">>> Webflux example application started! applicationContext = {}", applicationContext);
	}

}
