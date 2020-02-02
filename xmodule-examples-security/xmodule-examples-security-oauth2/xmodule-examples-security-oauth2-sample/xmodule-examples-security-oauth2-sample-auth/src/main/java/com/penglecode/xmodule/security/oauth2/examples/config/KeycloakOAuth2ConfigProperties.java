package com.penglecode.xmodule.security.oauth2.examples.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KeycloakOAuth2ConfigProperties {

	private final String realm = "spring-security-oauth2";
	
	private final List<KcUser> users = Arrays.asList(new KcUser("tiger", "123456"), new KcUser("monkey", "123456"));
	
	@Autowired
	@Qualifier("refreshClient")
	private KeycloakOAuth2ClientProperties refreshClient;
	
	@Autowired
	@Qualifier("passwordClient")
	private KeycloakOAuth2ClientProperties passwordClient;
	
	@Autowired
	@Qualifier("clientcredsClient")
	private KeycloakOAuth2ClientProperties clientcredsClient;

	@Value("http://127.0.0.1:${server.port:8080}")
	private String baseUrl;
	
	@Bean
	@ConfigurationProperties(prefix="spring.security.oauth2.client.registration.refresh-client")
	public KeycloakOAuth2ClientProperties refreshClient() {
		return new KeycloakOAuth2ClientProperties();
	}
	
	@Bean
	@ConfigurationProperties(prefix="spring.security.oauth2.client.registration.password-client")
	public KeycloakOAuth2ClientProperties passwordClient() {
		return new KeycloakOAuth2ClientProperties();
	}
	
	@Bean
	@ConfigurationProperties(prefix="spring.security.oauth2.client.registration.clientcreds-client")
	public KeycloakOAuth2ClientProperties clientcredsClient() {
		return new KeycloakOAuth2ClientProperties();
	}
	
	public KeycloakOAuth2ClientProperties getRefreshClient() {
		return refreshClient;
	}

	public KeycloakOAuth2ClientProperties getPasswordClient() {
		return passwordClient;
	}

	public KeycloakOAuth2ClientProperties getClientcredsClient() {
		return clientcredsClient;
	}

	public String getRealm() {
		return realm;
	}

	public String getBaseUrl() {
		return baseUrl;
	}
	
	public List<KcUser> getUsers() {
		return users;
	}

	public static class KcUser {
		
		private String username;
		
		private String password;

		public KcUser() {
			super();
		}

		public KcUser(String username, String password) {
			super();
			this.username = username;
			this.password = password;
		}

		public String getUsername() {
			return username;
		}

		public void setUsername(String username) {
			this.username = username;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}
		
	}
	
}
