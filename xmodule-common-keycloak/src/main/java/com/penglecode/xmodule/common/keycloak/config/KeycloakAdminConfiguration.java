package com.penglecode.xmodule.common.keycloak.config;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Keycloak Admin Client配置
 * 
 * @author 	pengpeng
 * @date	2019年12月12日 下午4:56:41
 */
@Configuration
@ConditionalOnProperty(prefix=KeycloakAdminProperties.DEFAULT_PROPERTIES_CONFIG_PREFIX, name={"server-url", "username", "password"})
public class KeycloakAdminConfiguration {

	@Bean
	@ConfigurationProperties(prefix=KeycloakAdminProperties.DEFAULT_PROPERTIES_CONFIG_PREFIX)
	public KeycloakAdminProperties keycloakAdminProperties() {
		return new KeycloakAdminProperties();
	}
	
	@Bean
	public Keycloak keycloak(KeycloakAdminProperties keycloakAdminProperties) {
		return KeycloakBuilder.builder()
				.serverUrl(keycloakAdminProperties.getServerUrl())
				.realm(keycloakAdminProperties.getRealm())
				.username(keycloakAdminProperties.getUsername())
				.password(keycloakAdminProperties.getPassword())
				.clientId(keycloakAdminProperties.getClientId())
				.build();
	}
	
}
