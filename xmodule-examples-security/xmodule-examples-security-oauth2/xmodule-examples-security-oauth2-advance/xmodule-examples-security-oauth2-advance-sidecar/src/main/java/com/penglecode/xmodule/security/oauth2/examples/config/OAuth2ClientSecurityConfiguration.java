package com.penglecode.xmodule.security.oauth2.examples.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.web.server.SecurityWebFilterChain;

import com.penglecode.xmodule.common.security.oauth2.client.service.RedisOAuth2AuthorizedClientService;

@Primary
@Configuration
@EnableWebFluxSecurity
public class OAuth2ClientSecurityConfiguration {

	@Bean(name="defaultAuthorizedClientService")
	public OAuth2AuthorizedClientService defaultAuthorizedClientService(ClientRegistrationRepository clientRegistrationRepository, 
			@Qualifier("defaultRedisConnectionFactory") RedisConnectionFactory defaultRedisConnectionFactory) {
		return new RedisOAuth2AuthorizedClientService(clientRegistrationRepository, defaultRedisConnectionFactory);
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
            		.authenticationEntryPoint(null)
            		.accessDeniedHandler(null);
        http.csrf().disable();
        return http.build();
    }
	
}
