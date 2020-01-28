package com.penglecode.xmodule.security.oauth2.examples.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.penglecode.xmodule.common.boot.config.AbstractSpringConfiguration;

@Configuration
public class OAuth2LoginConfiguration extends AbstractSpringConfiguration {

	@Bean
	@ConfigurationProperties(prefix="spring.security.oauth2.client.registration.keycloak")
	public KeycloakOAuth2LoginConfigProperties keycloakOAuth2LoginConfig() {
		return new KeycloakOAuth2LoginConfigProperties();
	}
	
}
