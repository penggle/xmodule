package com.penglecode.xmodule.security.oauth2.examples.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;

public class KeycloakOAuth2ConfigProperties {

	private final String realm = "spring-security-oauth2";
	
	@Value("http://127.0.0.1:${server.port:8080}")
	private String baseUrl;
	
	private KeycloakUserStorageConfigProperties userStorageConfig = new KeycloakUserStorageConfigProperties("upms", "upms", "java:jboss/datasources/UpmsStorageDS");
	
	private final Map<String, KeycloakOAuth2ClientProperties> registration = new HashMap<>();
	
	public String getRealm() {
		return realm;
	}

	public String getBaseUrl() {
		return baseUrl;
	}
	
	public Map<String, KeycloakOAuth2ClientProperties> getRegistration() {
		return registration;
	}

	public KeycloakUserStorageConfigProperties getUserStorageConfig() {
		return userStorageConfig;
	}

}
