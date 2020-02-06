package com.penglecode.xmodule.security.oauth2.examples.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;

import com.penglecode.xmodule.common.security.oauth2.client.servlet.service.RedisServletOAuth2AuthorizedClientService;
import com.penglecode.xmodule.common.security.oauth2.resource.servlet.support.OAuth2BearerTokenAccessDeniedHandler;
import com.penglecode.xmodule.common.security.oauth2.resource.servlet.support.OAuth2BearerTokenAuthenticationEntryPoint;

@Primary
@Configuration
@EnableWebSecurity
public class OAuth2ClientSecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Bean(name="defaultAuthorizedClientService")
	public OAuth2AuthorizedClientService defaultAuthorizedClientService(ClientRegistrationRepository clientRegistrationRepository, 
			@Qualifier("defaultRedisConnectionFactory") RedisConnectionFactory defaultRedisConnectionFactory) {
		return new RedisServletOAuth2AuthorizedClientService(clientRegistrationRepository, defaultRedisConnectionFactory);
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and()
				.antMatcher("/**") //指定API资源访问安全管辖路径
				.authorizeRequests()
				.antMatchers("/api/server/info").permitAll()
				.antMatchers("/api/**").access("hasAnyAuthority('SCOPE_user', 'SCOPE_app')")
				.anyRequest().authenticated()
			.and()
				.oauth2Client()
			.and()
				.oauth2ResourceServer()
					.authenticationEntryPoint(new OAuth2BearerTokenAuthenticationEntryPoint())
					.accessDeniedHandler(new OAuth2BearerTokenAccessDeniedHandler())
					.jwt();
		http.csrf().disable();
	}
	
}
