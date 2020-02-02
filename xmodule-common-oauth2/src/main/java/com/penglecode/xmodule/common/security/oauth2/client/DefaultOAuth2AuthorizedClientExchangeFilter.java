package com.penglecode.xmodule.common.security.oauth2.client;

import java.util.Map;
import java.util.function.Consumer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.util.Assert;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import org.springframework.web.reactive.function.client.WebClient;

import com.penglecode.xmodule.common.security.oauth2.client.util.OAuth2ClientUtils;

import reactor.core.publisher.Mono;

/**
 * 默认的自定义的OAuth2客户端WebClient请求过滤器
 * 
 * 增加了AccessToken使用日志打印功能
 * 
 * @see #ServletOAuth2AuthorizedClientExchangeFilterFunction
 * 
 * @author 	pengpeng
 * @date	2019年12月16日 上午11:17:30
 */
public class DefaultOAuth2AuthorizedClientExchangeFilter implements ExchangeFilterFunction {

	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultOAuth2AuthorizedClientExchangeFilter.class);
	
	/**
	 * The request attribute name used to locate the {@link OAuth2AuthorizedClient}.
	 */
	public static final String OAUTH2_AUTHORIZED_CLIENT_ATTR_NAME = OAuth2AuthorizedClient.class.getName();
	public static final String CLIENT_REGISTRATION_ID_ATTR_NAME = OAuth2AuthorizedClient.class.getName().concat(".CLIENT_REGISTRATION_ID");
	public static final String AUTHENTICATION_ATTR_NAME = Authentication.class.getName();
	public static final String HTTP_SERVLET_REQUEST_ATTR_NAME = HttpServletRequest.class.getName();
	public static final String HTTP_SERVLET_RESPONSE_ATTR_NAME = HttpServletResponse.class.getName();
	
	public static final Authentication ANONYMOUS_AUTHENTICATION = new AnonymousAuthenticationToken(
			"anonymous", "anonymousUser", AuthorityUtils.createAuthorityList("ROLE_ANONYMOUS"));

	private final ServletOAuth2AuthorizedClientExchangeFilterFunction delegatingOAuth2ClientExchangeFilter;
	
	public DefaultOAuth2AuthorizedClientExchangeFilter(OAuth2AuthorizedClientManager authorizedClientManager) {
		Assert.notNull(authorizedClientManager, "Parameter 'authorizedClientManager' must be required!");
		this.delegatingOAuth2ClientExchangeFilter = new ServletOAuth2AuthorizedClientExchangeFilterFunction(authorizedClientManager);
	}
	
	public Consumer<WebClient.Builder> oauth2Configuration() {
		return builder -> {
			builder.defaultRequest(delegatingOAuth2ClientExchangeFilter.defaultRequest())
				   .filter(delegatingOAuth2ClientExchangeFilter.andThen(this));
		};
	}
	
	/**
	 * Modifies the {@link ClientRequest#attributes()} to include the {@link OAuth2AuthorizedClient} to be used for
	 * providing the Bearer Token.
	 *
	 * @param authorizedClient the {@link OAuth2AuthorizedClient} to use.
	 * @return the {@link Consumer} to populate the attributes
	 */
	public static Consumer<Map<String, Object>> oauth2AuthorizedClient(OAuth2AuthorizedClient authorizedClient) {
		return attributes -> {
			if (authorizedClient == null) {
				attributes.remove(OAUTH2_AUTHORIZED_CLIENT_ATTR_NAME);
			} else {
				attributes.put(OAUTH2_AUTHORIZED_CLIENT_ATTR_NAME, authorizedClient);
			}
		};
	}

	/**
	 * Modifies the {@link ClientRequest#attributes()} to include the {@link ClientRegistration#getRegistrationId()} to
	 * be used to look up the {@link OAuth2AuthorizedClient}.
	 *
	 * @param clientRegistrationId the {@link ClientRegistration#getRegistrationId()} to
	 * be used to look up the {@link OAuth2AuthorizedClient}.
	 * @return the {@link Consumer} to populate the attributes
	 */
	public static Consumer<Map<String, Object>> clientRegistrationId(String clientRegistrationId) {
		return attributes -> attributes.put(CLIENT_REGISTRATION_ID_ATTR_NAME, clientRegistrationId);
	}

	/**
	 * Modifies the {@link ClientRequest#attributes()} to include the {@link Authentication} used to
	 * look up and save the {@link OAuth2AuthorizedClient}. The value is defaulted in
	 * {@link ServletOAuth2AuthorizedClientExchangeFilterFunction#defaultRequest()}
	 *
	 * @param authentication the {@link Authentication} to use.
	 * @return the {@link Consumer} to populate the attributes
	 */
	public static Consumer<Map<String, Object>> authentication(Authentication authentication) {
		return attributes -> attributes.put(AUTHENTICATION_ATTR_NAME, authentication);
	}

	/**
	 * Modifies the {@link ClientRequest#attributes()} to include the {@link HttpServletRequest} used to
	 * look up and save the {@link OAuth2AuthorizedClient}. The value is defaulted in
	 * {@link ServletOAuth2AuthorizedClientExchangeFilterFunction#defaultRequest()}
	 *
	 * @param request the {@link HttpServletRequest} to use.
	 * @return the {@link Consumer} to populate the attributes
	 */
	public static Consumer<Map<String, Object>> httpServletRequest(HttpServletRequest request) {
		return attributes -> attributes.put(HTTP_SERVLET_REQUEST_ATTR_NAME, request);
	}

	/**
	 * Modifies the {@link ClientRequest#attributes()} to include the {@link HttpServletResponse} used to
	 * save the {@link OAuth2AuthorizedClient}. The value is defaulted in
	 * {@link ServletOAuth2AuthorizedClientExchangeFilterFunction#defaultRequest()}
	 *
	 * @param response the {@link HttpServletResponse} to use.
	 * @return the {@link Consumer} to populate the attributes
	 */
	public static Consumer<Map<String, Object>> httpServletResponse(HttpServletResponse response) {
		return attributes -> attributes.put(HTTP_SERVLET_RESPONSE_ATTR_NAME, response);
	}
	
	@Override
	public Mono<ClientResponse> filter(ClientRequest request, ExchangeFunction next) {
		String clientRegistrationId = request.attribute(CLIENT_REGISTRATION_ID_ATTR_NAME).get().toString();
		Authentication principal = (Authentication) request.attribute(AUTHENTICATION_ATTR_NAME).orElse(ANONYMOUS_AUTHENTICATION);
		HttpServletRequest httpRequest = (HttpServletRequest) request.attribute(HTTP_SERVLET_REQUEST_ATTR_NAME).get();
		OAuth2AuthorizedClient oauth2AuthorizedClient = OAuth2ClientUtils.getOAuth2AuthorizedClient(clientRegistrationId, principal, httpRequest);
		if(oauth2AuthorizedClient != null) {
			OAuth2AccessToken oauth2AccessToken = oauth2AuthorizedClient.getAccessToken();
			LOGGER.debug("{} - Apply OAuth2AccessToken[value = {}, issuedAt = {}, expiresAt = {}, scopes = {}] for Client[registrationId = {}, principalName = {}] ", request.logPrefix(), oauth2AccessToken.getTokenValue(), oauth2AccessToken.getIssuedAt(), oauth2AccessToken.getExpiresAt(), oauth2AccessToken.getScopes(), clientRegistrationId, principal.getName());
		}
		return next.exchange(request);
	}
	
}
