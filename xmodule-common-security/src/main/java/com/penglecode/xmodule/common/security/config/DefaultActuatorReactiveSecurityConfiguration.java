package com.penglecode.xmodule.common.security.config;

import java.util.List;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.actuate.autoconfigure.security.reactive.EndpointRequest;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication.Type;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.util.StringUtils;

/**
 * spring-boot-starter-actuator启用安全认证 (针对Reactive应用)
 * 
 * @author 	pengpeng
 * @date	2018年9月6日 下午4:02:37
 */
@Configuration
@EnableWebFluxSecurity
@ConditionalOnWebApplication(type = Type.REACTIVE)
@ConditionalOnProperty(name=DefaultActuatorReactiveSecurityConfiguration.CONFIGURATION_ENABLED, havingValue="true", matchIfMissing=true)
public class DefaultActuatorReactiveSecurityConfiguration {

	public static final String CONFIGURATION_ENABLED = "management.customized.enabled";
	
	@Bean
	public MapReactiveUserDetailsService reactiveUserDetailsService(SecurityProperties properties,
			ObjectProvider<PasswordEncoder> passwordEncoder) {
		SecurityProperties.User user = properties.getUser();
		UserDetails userDetails = getUserDetails(user, getEncodedPassword(user, passwordEncoder.getIfAvailable()));
		return new MapReactiveUserDetailsService(userDetails);
	}

	private UserDetails getUserDetails(SecurityProperties.User user, String password) {
		List<String> roles = user.getRoles();
		return User.withUsername(user.getName()).password(password).roles(StringUtils.toStringArray(roles)).build();
	}
	
	private String getEncodedPassword(SecurityProperties.User user, PasswordEncoder encoder) {
		String password = user.getPassword();
		if(encoder != null) {
			password = encoder.encode(password);
		}
		return password;
	}
	
	@Bean
    public SecurityWebFilterChain actuatorSpringSecurityFilterChain(ServerHttpSecurity http) {
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
