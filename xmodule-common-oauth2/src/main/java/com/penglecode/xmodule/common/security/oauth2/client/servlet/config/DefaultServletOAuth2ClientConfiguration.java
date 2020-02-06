package com.penglecode.xmodule.common.security.oauth2.client.servlet.config;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication.Type;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.InMemoryOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.OAuth2AuthorizationContext;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProvider;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProviderBuilder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.AuthenticatedPrincipalOAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.client.WebClient;

import com.penglecode.xmodule.common.boot.config.AbstractSpringConfiguration;
import com.penglecode.xmodule.common.security.oauth2.client.servlet.ServletOAuth2AuthorizedClientExchangeFilter;
import com.penglecode.xmodule.common.security.oauth2.consts.OAuth2ApplicationConstants;

/**
 * 默认的OAuth2客户端配置
 * 
 * @author 	pengpeng
 * @date 	2020年1月31日 下午3:50:13
 */
@Configuration
@ConditionalOnWebApplication(type = Type.SERVLET)
public class DefaultServletOAuth2ClientConfiguration extends AbstractSpringConfiguration {
	
	@Bean(name="defaultAuthorizedClientService")
	@ConditionalOnMissingBean(name="defaultAuthorizedClientService")
	public OAuth2AuthorizedClientService defaultAuthorizedClientService(ClientRegistrationRepository clientRegistrationRepository) {
		return new InMemoryOAuth2AuthorizedClientService(clientRegistrationRepository);
	}

	@Bean(name="defaultAuthorizedClientRepository")
	@ConditionalOnMissingBean(name="defaultAuthorizedClientRepository")
	public OAuth2AuthorizedClientRepository defaultAuthorizedClientRepository(OAuth2AuthorizedClientService authorizedClientService) {
		return new AuthenticatedPrincipalOAuth2AuthorizedClientRepository(authorizedClientService);
	}
	
	/**
	 * 默认的供各应用API之间相互调用的WebClient
	 * 
	 */
	@Bean(name="defaultApiWebClient")
	@ConditionalOnMissingBean(name="defaultApiWebClient")
	public WebClient defaultApiWebClient(WebClient.Builder webClientBuilder, OAuth2AuthorizedClientManager authorizedClientManager) {
		//默认使用当前应用的APP客户端(client_credentials模式)来进行WebClient请求OAuth2认证
		String defaultClientRegistrationId = OAuth2ApplicationConstants.DEFAULT_OAUTH2_CLIENT_CONFIG.getAppRegistrationId();
		ServletOAuth2AuthorizedClientExchangeFilter oauth2Client = new ServletOAuth2AuthorizedClientExchangeFilter(defaultClientRegistrationId, authorizedClientManager);
		return webClientBuilder.apply(oauth2Client.oauth2Configuration()).build();
	}

	@Bean(name="defaultAuthorizedClientManager")
	@ConditionalOnMissingBean(name="defaultAuthorizedClientManager")
	public OAuth2AuthorizedClientManager defaultAuthorizedClientManager(ClientRegistrationRepository clientRegistrationRepository,
															OAuth2AuthorizedClientRepository authorizedClientRepository) {
		OAuth2AuthorizedClientProvider authorizedClientProvider =
				OAuth2AuthorizedClientProviderBuilder.builder()
						.authorizationCode()
						.refreshToken(builder -> {
							builder.clock(OAuth2ApplicationConstants.DEFAULT_OAUTH2_CLIENT_CONFIG.getClock());
							builder.clockSkew(OAuth2ApplicationConstants.DEFAULT_OAUTH2_CLIENT_CONFIG.getClockSkew());
						})
						.clientCredentials(builder -> {
							builder.clock(OAuth2ApplicationConstants.DEFAULT_OAUTH2_CLIENT_CONFIG.getClock());
							builder.clockSkew(OAuth2ApplicationConstants.DEFAULT_OAUTH2_CLIENT_CONFIG.getClockSkew());
						})
						.password(builder -> {
							builder.clock(OAuth2ApplicationConstants.DEFAULT_OAUTH2_CLIENT_CONFIG.getClock());
							builder.clockSkew(OAuth2ApplicationConstants.DEFAULT_OAUTH2_CLIENT_CONFIG.getClockSkew());
						})
						.build();
		DefaultOAuth2AuthorizedClientManager authorizedClientManager = new DefaultOAuth2AuthorizedClientManager(
				clientRegistrationRepository, authorizedClientRepository);
		authorizedClientManager.setAuthorizedClientProvider(authorizedClientProvider);

		// For the `password` grant, the `username` and `password` are supplied via request parameters,
		// so map it to `OAuth2AuthorizationContext.getAttributes()`.
		authorizedClientManager.setContextAttributesMapper(contextAttributesMapper(clientRegistrationRepository));
		return authorizedClientManager;
	}

	private Function<OAuth2AuthorizeRequest, Map<String, Object>> contextAttributesMapper(final ClientRegistrationRepository clientRegistrationRepository) {
		return authorizeRequest -> {
			Map<String, Object> contextAttributes = Collections.emptyMap();
			
			ClientRegistration clientRegistration = clientRegistrationRepository.findByRegistrationId(authorizeRequest.getClientRegistrationId());
			if(AuthorizationGrantType.PASSWORD.equals(clientRegistration.getAuthorizationGrantType())) {
				HttpServletRequest servletRequest = authorizeRequest.getAttribute(HttpServletRequest.class.getName());
				String username = servletRequest.getParameter(OAuth2ParameterNames.USERNAME);
				String password = servletRequest.getParameter(OAuth2ParameterNames.PASSWORD);
				if(StringUtils.hasText(username) && StringUtils.hasText(password)) {
					contextAttributes = new HashMap<>();
					// `PasswordOAuth2AuthorizedClientProvider` requires both attributes
					contextAttributes.put(OAuth2AuthorizationContext.USERNAME_ATTRIBUTE_NAME, username);
					contextAttributes.put(OAuth2AuthorizationContext.PASSWORD_ATTRIBUTE_NAME, password);
				}
				String scope = servletRequest.getParameter(OAuth2ParameterNames.SCOPE);
				if(StringUtils.hasText(scope)) {
					contextAttributes.put(OAuth2AuthorizationContext.REQUEST_SCOPE_ATTRIBUTE_NAME,
							StringUtils.delimitedListToStringArray(scope, " "));
				}
			}
			return contextAttributes;
		};
	}
	
}