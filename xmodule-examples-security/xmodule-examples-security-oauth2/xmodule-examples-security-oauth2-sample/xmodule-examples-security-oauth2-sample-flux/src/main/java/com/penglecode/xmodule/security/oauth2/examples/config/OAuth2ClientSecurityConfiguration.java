package com.penglecode.xmodule.security.oauth2.examples.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.web.server.SecurityWebFilterChain;

import com.penglecode.xmodule.common.security.oauth2.client.reactive.service.RedisReactiveOAuth2AuthorizedClientService;
import com.penglecode.xmodule.common.security.oauth2.resource.reactive.support.OAuth2BearerTokenAccessDeniedHandler;
import com.penglecode.xmodule.common.security.oauth2.resource.reactive.support.OAuth2BearerTokenAuthenticationEntryPoint;

@Primary
@Configuration
@EnableWebFluxSecurity
public class OAuth2ClientSecurityConfiguration {

	@Bean(name="defaultAuthorizedClientService")
	public ReactiveOAuth2AuthorizedClientService defaultAuthorizedClientService(ReactiveClientRegistrationRepository clientRegistrationRepository, 
			@Qualifier("defaultRedisConnectionFactory") RedisConnectionFactory defaultRedisConnectionFactory) {
		return new RedisReactiveOAuth2AuthorizedClientService(clientRegistrationRepository, defaultRedisConnectionFactory);
	}
	
	@Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http.authorizeExchange(exchanges ->
                exchanges
                	.pathMatchers("/api/server/info").permitAll()
                	.pathMatchers("/api/**").hasAnyAuthority("SCOPE_user", "SCOPE_app")
                    .anyExchange().authenticated()
            )
            	.oauth2Client()
            .and()
            	.oauth2ResourceServer()
            		.authenticationEntryPoint(new OAuth2BearerTokenAuthenticationEntryPoint())
            		.accessDeniedHandler(new OAuth2BearerTokenAccessDeniedHandler())
            		.jwt();
        http.csrf().disable();
        return http.build();
    }
	
}
