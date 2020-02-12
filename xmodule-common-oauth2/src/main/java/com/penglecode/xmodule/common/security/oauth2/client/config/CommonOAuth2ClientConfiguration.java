package com.penglecode.xmodule.common.security.oauth2.client.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication.Type;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;

import com.penglecode.xmodule.common.boot.config.AbstractSpringConfiguration;
import com.penglecode.xmodule.common.initializer.SpringWebAppStartupInitializer;
import com.penglecode.xmodule.common.security.oauth2.client.reactive.support.ReactiveClientRegistrationRepositoryAdapter;
import com.penglecode.xmodule.common.security.oauth2.client.servlet.support.ServletClientRegistrationRepositoryAdapter;
import com.penglecode.xmodule.common.security.oauth2.client.support.ClientRegistrationRepositoryAdapter;
import com.penglecode.xmodule.common.security.oauth2.client.support.OAuth2ClientConfigProperties;
import com.penglecode.xmodule.common.security.oauth2.consts.OAuth2ApplicationConstants;

@Configuration
public class CommonOAuth2ClientConfiguration extends AbstractSpringConfiguration {

	@Bean
	@ConfigurationProperties(prefix="spring.security.oauth2.client")
	public OAuth2ClientConfigProperties oauth2ClientConfigProperties() {
		return OAuth2ApplicationConstants.DEFAULT_OAUTH2_CLIENT_CONFIG;
	}
	
	@Bean
	public OAuth2ClientConfigBeanPreInitializer oauth2ClientConfigBeanPreInitializer() {
		return new OAuth2ClientConfigBeanPreInitializer();
	}
	
	@Order(Ordered.HIGHEST_PRECEDENCE)
	public static class OAuth2ClientConfigBeanPreInitializer implements SpringWebAppStartupInitializer {

		@Override
		public void initialize(ConfigurableApplicationContext applicationContext) throws Exception {
			applicationContext.getBean(OAuth2ClientConfigProperties.class); //尽快初始化OAuth2ClientConfigProperties的bean实例
		}
		
	}
	
	@Configuration
	@ConditionalOnWebApplication(type=Type.SERVLET)
	public static class ServletClientRegistrationRepositoryAdapterConfiguration {
		
		@Bean
		public ClientRegistrationRepositoryAdapter clientRegistrationRepositoryAdapter(ClientRegistrationRepository clientRegistrationRepository) {
			return new ServletClientRegistrationRepositoryAdapter(clientRegistrationRepository);
		}
		
	}
	
	@Configuration
	@ConditionalOnWebApplication(type=Type.REACTIVE)
	public static class ReactiveClientRegistrationRepositoryAdapterConfiguration {
		
		@Bean
		public ClientRegistrationRepositoryAdapter clientRegistrationRepositoryAdapter(ReactiveClientRegistrationRepository clientRegistrationRepository) {
			return new ReactiveClientRegistrationRepositoryAdapter(clientRegistrationRepository);
		}
		
	}
	
}
