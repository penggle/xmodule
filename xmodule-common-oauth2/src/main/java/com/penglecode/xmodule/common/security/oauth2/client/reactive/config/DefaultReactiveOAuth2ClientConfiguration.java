package com.penglecode.xmodule.common.security.oauth2.client.reactive.config;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication.Type;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.InMemoryReactiveOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.OAuth2AuthorizationContext;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientProvider;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientProviderBuilder;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultReactiveOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.server.AuthenticatedPrincipalServerOAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.client.web.server.ServerOAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;

import com.penglecode.xmodule.common.boot.config.AbstractSpringConfiguration;
import com.penglecode.xmodule.common.security.oauth2.client.reactive.ServerOAuth2AuthorizedClientExchangeFilter;
import com.penglecode.xmodule.common.security.oauth2.consts.OAuth2ApplicationConstants;

import reactor.core.publisher.Mono;

/**
 * 默认的OAuth2客户端配置
 * 
 * @author 	pengpeng
 * @date 	2020年1月31日 下午3:50:13
 */
@Configuration
@ConditionalOnWebApplication(type = Type.REACTIVE)
public class DefaultReactiveOAuth2ClientConfiguration extends AbstractSpringConfiguration {

	@Bean(name="defaultAuthorizedClientService")
	@ConditionalOnMissingBean(name="defaultAuthorizedClientService")
	public ReactiveOAuth2AuthorizedClientService defaultAuthorizedClientService(ReactiveClientRegistrationRepository clientRegistrationRepository) {
		return new InMemoryReactiveOAuth2AuthorizedClientService(clientRegistrationRepository);
	}

	@Bean(name="defaultAuthorizedClientRepository")
	@ConditionalOnMissingBean(name="defaultAuthorizedClientRepository")
	public ServerOAuth2AuthorizedClientRepository defaultAuthorizedClientRepository(ReactiveOAuth2AuthorizedClientService authorizedClientService) {
		return new AuthenticatedPrincipalServerOAuth2AuthorizedClientRepository(authorizedClientService);
	}
	
	/**
	 * 默认的供各应用API之间相互调用的WebClient
	 * 
	 */
	@Bean(name="defaultApiWebClient")
	@ConditionalOnMissingBean(name="defaultApiWebClient")
	public WebClient defaultApiWebClient(WebClient.Builder webClientBuilder, ReactiveOAuth2AuthorizedClientManager authorizedClientManager) {
		//默认使用当前应用的APP客户端(client_credentials模式)来进行WebClient请求OAuth2认证
		String defaultClientRegistrationId = OAuth2ApplicationConstants.DEFAULT_OAUTH2_CLIENT_CONFIG.getAppRegistrationId();
		ServerOAuth2AuthorizedClientExchangeFilter oauth2Client = new ServerOAuth2AuthorizedClientExchangeFilter(defaultClientRegistrationId, authorizedClientManager);
		return webClientBuilder.apply(oauth2Client.oauth2Configuration()).build();
	}

	@Bean(name="defaultAuthorizedClientManager")
	@ConditionalOnMissingBean(name="defaultAuthorizedClientManager")
	public ReactiveOAuth2AuthorizedClientManager defaultAuthorizedClientManager(ReactiveClientRegistrationRepository clientRegistrationRepository,
															ServerOAuth2AuthorizedClientRepository authorizedClientRepository) {
		ReactiveOAuth2AuthorizedClientProvider authorizedClientProvider =
				ReactiveOAuth2AuthorizedClientProviderBuilder.builder()
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
		DefaultReactiveOAuth2AuthorizedClientManager authorizedClientManager = new DefaultReactiveOAuth2AuthorizedClientManager(
				clientRegistrationRepository, authorizedClientRepository);
		authorizedClientManager.setAuthorizedClientProvider(authorizedClientProvider);

		// For the `password` grant, the `username` and `password` are supplied via request parameters,
		// so map it to `OAuth2AuthorizationContext.getAttributes()`.
		authorizedClientManager.setContextAttributesMapper(contextAttributesMapper(clientRegistrationRepository));
		return authorizedClientManager;
	}

	private Function<OAuth2AuthorizeRequest, Mono<Map<String, Object>>> contextAttributesMapper(final ReactiveClientRegistrationRepository clientRegistrationRepository) {
		return authorizeRequest -> clientRegistrationRepository.findByRegistrationId(authorizeRequest.getClientRegistrationId()).flatMap(registration -> {
			ServerWebExchange exchange = authorizeRequest.getAttribute(ServerWebExchange.class.getName());
			Map<String,Object> contextAttributes = new HashMap<String,Object>();
			return Mono.just(contextAttributes).flatMap(attrs -> { //默认所有grantType均带上scope
				String scope = exchange.getRequest().getQueryParams().getFirst(OAuth2ParameterNames.SCOPE);
				if(StringUtils.hasText(scope)) {
					attrs.put(OAuth2AuthorizationContext.REQUEST_SCOPE_ATTRIBUTE_NAME,
							StringUtils.delimitedListToStringArray(scope, " "));
				}
				return Mono.just(attrs);
			}).filter(attrs -> registration.getAuthorizationGrantType().equals(AuthorizationGrantType.PASSWORD)).flatMap(attrs -> { //password模式带上username/password
				return exchange.getFormData().flatMap(form -> {
					String username = form.getFirst(OAuth2ParameterNames.USERNAME);
					String password = form.getFirst(OAuth2ParameterNames.PASSWORD);
					if(StringUtils.hasText(username) && StringUtils.hasText(password)) {
						// `PasswordOAuth2AuthorizedClientProvider` requires both attributes
						attrs.put(OAuth2AuthorizationContext.USERNAME_ATTRIBUTE_NAME, username);
						attrs.put(OAuth2AuthorizationContext.PASSWORD_ATTRIBUTE_NAME, password);
					}
					return Mono.just(attrs);
				}).switchIfEmpty(Mono.defer(() -> Mono.just(attrs)));
			}).switchIfEmpty(Mono.defer(() -> Mono.just(contextAttributes)));
		});
	}
	
}