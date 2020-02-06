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
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.util.matcher.PathPatternParserServerWebExchangeMatcher;
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
	
	private final SecurityProperties securityProperties;
	
	private final PasswordEncoder passwordEncoder;
	
	public DefaultActuatorReactiveSecurityConfiguration(SecurityProperties securityProperties, ObjectProvider<PasswordEncoder> passwordEncoder) {
		this.securityProperties = securityProperties;
		this.passwordEncoder = passwordEncoder.getIfAvailable();
	}
	
	private ReactiveAuthenticationManager reactiveAuthenticationManager() {
		UserDetailsRepositoryReactiveAuthenticationManager reactiveAuthenticationManager = new UserDetailsRepositoryReactiveAuthenticationManager(reactiveUserDetailsService());
		reactiveAuthenticationManager.setPasswordEncoder(passwordEncoder);
		return reactiveAuthenticationManager;
	}
	
	private MapReactiveUserDetailsService reactiveUserDetailsService() {
		SecurityProperties.User user = securityProperties.getUser();
		UserDetails userDetails = getUserDetails(user, getEncodedPassword(user));
		return new MapReactiveUserDetailsService(userDetails);
	}

	private UserDetails getUserDetails(SecurityProperties.User user, String password) {
		List<String> roles = user.getRoles();
		return User.withUsername(user.getName()).password(password).roles(StringUtils.toStringArray(roles)).build();
	}
	
	private String getEncodedPassword(SecurityProperties.User user) {
		String password = user.getPassword();
		if(passwordEncoder != null) {
			password = passwordEncoder.encode(password);
		}
		return password;
	}
	
	@Bean
	@Order(Ordered.HIGHEST_PRECEDENCE)
    public SecurityWebFilterChain actuatorSpringSecurityFilterChain(ServerHttpSecurity http) {
		http.securityMatcher(new PathPatternParserServerWebExchangeMatcher("/actuator/**"))
			.authenticationManager(reactiveAuthenticationManager())
			.authorizeExchange()
				.matchers(EndpointRequest.to("info", "health", "sidecarhealth")).permitAll()
				.matchers(EndpointRequest.toAnyEndpoint()).hasRole("ACTUATOR")
				.anyExchange().permitAll()
			.and().httpBasic()
			.and().cors()
			.and().csrf().disable();
		return http.build();
	}
	
}
