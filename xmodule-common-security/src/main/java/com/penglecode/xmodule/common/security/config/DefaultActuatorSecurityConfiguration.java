package com.penglecode.xmodule.common.security.config;

import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication.Type;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

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
@ConditionalOnProperty(name=DefaultActuatorSecurityConfiguration.CONFIGURATION_ENABLED, havingValue="true", matchIfMissing=true)
public class DefaultActuatorSecurityConfiguration extends WebSecurityConfigurerAdapter {

	public static final String CONFIGURATION_ENABLED = "management.customized.enabled";
	
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/assets/**");
	}
	
	@Override
    protected void configure(HttpSecurity http) throws Exception {
		http.antMatcher("/actuator/**") //指定actuator访问安全管辖路径
			.authorizeRequests()
	        .requestMatchers(EndpointRequest.to("info", "health", "sidecarhealth")).permitAll()
	        .requestMatchers(EndpointRequest.toAnyEndpoint()).hasRole("ACTUATOR")
	    .and()
	        .httpBasic()
	    .and()
	    	.csrf().disable();
    }
	
}
