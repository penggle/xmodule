package com.penglecode.xmodule.security.oauth2.examples.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.penglecode.xmodule.common.boot.config.AbstractSpringConfiguration;

@Configuration
public class OAuth2ClientConfiguration extends AbstractSpringConfiguration {

	@Bean
	@ConfigurationProperties(prefix="spring.security.oauth2.client")
	public KeycloakOAuth2ConfigProperties keycloakOAuth2Config() {
		return new KeycloakOAuth2ConfigProperties();
	}
	
}
