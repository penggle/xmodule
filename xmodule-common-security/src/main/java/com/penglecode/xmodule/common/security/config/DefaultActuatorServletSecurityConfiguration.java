package com.penglecode.xmodule.common.security.config;

import java.util.List;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication.Type;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.util.StringUtils;

/**
 * 默认的spring-boot-starter-actuator安全配置
 * 
 * @author 	pengpeng
 * @date	2018年9月6日 下午4:02:37
 */

@Configuration
@EnableWebSecurity
@Order(Ordered.HIGHEST_PRECEDENCE)
@ConditionalOnWebApplication(type = Type.SERVLET)
@ConditionalOnProperty(name=DefaultActuatorServletSecurityConfiguration.CONFIGURATION_ENABLED, havingValue="true", matchIfMissing=true)
public class DefaultActuatorServletSecurityConfiguration extends WebSecurityConfigurerAdapter {

	public static final String CONFIGURATION_ENABLED = "management.customized.enabled";
	
	private final SecurityProperties securityProperties;
	
	private final PasswordEncoder passwordEncoder;
	
	public DefaultActuatorServletSecurityConfiguration(SecurityProperties securityProperties, ObjectProvider<PasswordEncoder> passwordEncoder) {
		this.securityProperties = securityProperties;
		this.passwordEncoder = passwordEncoder.getIfAvailable();
	}
	
	@Override
    protected void configure(HttpSecurity http) throws Exception {
		http.userDetailsService(inMemoryUserDetailsManager())
			.antMatcher("/actuator/**") //指定actuator访问安全管辖路径
			.authorizeRequests()
	        .requestMatchers(EndpointRequest.to("info", "health", "sidecarhealth")).permitAll()
	        .requestMatchers(EndpointRequest.toAnyEndpoint()).hasRole("ACTUATOR")
	    .and()
	        .httpBasic()
	    .and()
	    	.csrf().disable();
    }
	
	private InMemoryUserDetailsManager inMemoryUserDetailsManager() {
		SecurityProperties.User user = securityProperties.getUser();
		List<String> roles = user.getRoles();
		return new InMemoryUserDetailsManager(
				User.withUsername(user.getName()).password(getEncodedPassword(user))
						.roles(StringUtils.toStringArray(roles)).build());
	}
	
	private String getEncodedPassword(SecurityProperties.User user) {
		String password = user.getPassword();
		if(passwordEncoder != null) {
			password = passwordEncoder.encode(password);
		}
		return password;
	}
	
}
