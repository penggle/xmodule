package com.penglecode.xmodule.security.oauth2.examples.config;

import java.time.Duration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.penglecode.xmodule.common.boot.config.AbstractSpringConfiguration;

import okhttp3.OkHttpClient;
import sh.ory.hydra.ApiClient;
import sh.ory.hydra.api.AdminApi;

@Configuration
public class OAuth2LoginConfiguration extends AbstractSpringConfiguration {

	@Bean
	@ConfigurationProperties(prefix="spring.security.oauth2.client.registration.keycloak")
	public KeycloakOAuth2LoginConfigProperties keycloakOAuth2LoginConfig() {
		return new KeycloakOAuth2LoginConfigProperties();
	}
	
	@Bean
	@ConfigurationProperties(prefix="spring.security.oauth2.client.registration.hydra")
	public HydraOAuth2LoginConfigProperties hydraOAuth2LoginConfig() {
		return new HydraOAuth2LoginConfigProperties();
	}
	
	@Bean
	@ConfigurationProperties(prefix="hydra")
	public HydraConfigProperties hydraConfigProperties() {
		return new HydraConfigProperties();
	}
	
	@Bean
	public ApiClient defaultHydraApiClient(HydraConfigProperties hydraConfigProperties) {
		ApiClient defaultClient = sh.ory.hydra.Configuration.getDefaultApiClient();
		OkHttpClient httpClient = new OkHttpClient.Builder().connectTimeout(Duration.ofSeconds(6)).readTimeout(Duration.ofSeconds(60)).writeTimeout(Duration.ofSeconds(60)).build();
		defaultClient.setHttpClient(httpClient);
		defaultClient.setDebugging(true);
		defaultClient.setBasePath(hydraConfigProperties.getAdminApiUrl());
		return defaultClient;
	}
	
	@Bean
	public AdminApi hydraAdminApi(ApiClient defaultHydraApiClient) {
		return new AdminApi(defaultHydraApiClient);
	}
	
}