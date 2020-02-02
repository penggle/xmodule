package com.penglecode.xmodule.webflux.examples.initializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class WebFluxExampleAppStartedInitializer {

	private static final Logger LOGGER = LoggerFactory.getLogger(WebFluxExampleAppStartedInitializer.class);
	
	@EventListener(ApplicationStartedEvent.class)
	public void initialize(ApplicationStartedEvent event) throws Exception {
		LOGGER.info(">>> Webflux example application started! event = {}", event);
	}

}
