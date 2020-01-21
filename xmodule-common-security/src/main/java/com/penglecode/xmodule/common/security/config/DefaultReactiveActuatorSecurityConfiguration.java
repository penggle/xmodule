package com.penglecode.xmodule.common.security.config;

import org.springframework.boot.actuate.autoconfigure.security.reactive.EndpointRequest;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication.Type;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

/**
 * spring-boot-starter-actuator启用安全认证 (针对Reactive应用)
 * 
 * @author 	pengpeng
 * @date	2018年9月6日 下午4:02:37
 */
@Configuration
@EnableWebFluxSecurity
@ConditionalOnWebApplication(type = Type.REACTIVE)
@ConditionalOnProperty(name=DefaultReactiveActuatorSecurityConfiguration.CONFIGURATION_ENABLED, havingValue="true", matchIfMissing=true)
public class DefaultReactiveActuatorSecurityConfiguration {

	public static final String CONFIGURATION_ENABLED = "management.customized.enabled";
	
	@Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
		http.authorizeExchange()
			.matchers(EndpointRequest.to("info", "health", "sidecarhealth")).permitAll()
			.matchers(EndpointRequest.toAnyEndpoint()).hasRole("ACTUATOR")
			.anyExchange().permitAll()
		.and().cors()
		.and().httpBasic()
		.and().csrf().disable();
		return http.build();
	}
	
}
