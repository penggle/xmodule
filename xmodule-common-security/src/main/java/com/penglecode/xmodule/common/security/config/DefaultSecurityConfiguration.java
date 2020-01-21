package com.penglecode.xmodule.common.security.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.penglecode.xmodule.common.boot.config.AbstractSpringConfiguration;
import com.penglecode.xmodule.common.security.consts.SecurityApplicationConstants;

@Configuration
public class DefaultSecurityConfiguration extends AbstractSpringConfiguration {

	/**
	 * 默认的PasswordEncoder
	 * @return
	 */
	@Bean
	@ConditionalOnMissingBean
	public PasswordEncoder passwordEncoder() {
		return SecurityApplicationConstants.DEFAULT_PASSWORD_ENCODER;
	}
	
	/**
	 * JWT令牌配置
	 * @return
	 */
	@Bean
	@ConfigurationProperties(prefix="spring.security.jwt")
	public JwtTokenConfigProperties jwtTokenConfigProperties() {
		return new JwtTokenConfigProperties();
	}
	
}
