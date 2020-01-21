package com.penglecode.xmodule.webflux.examples.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.penglecode.xmodule.common.boot.config.AbstractSpringConfiguration;
import com.penglecode.xmodule.webflux.examples.initializer.WebFluxExampleAppStartedInitializer;

@Configuration
public class WebFluxExampleConfiguration extends AbstractSpringConfiguration {

	@Bean
	public WebFluxExampleAppStartedInitializer webFluxAppStartedInitializer() {
		return new WebFluxExampleAppStartedInitializer();
	}
	
}
