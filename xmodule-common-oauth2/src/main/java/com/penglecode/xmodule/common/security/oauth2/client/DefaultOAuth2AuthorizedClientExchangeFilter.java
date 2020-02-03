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
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.util.Assert;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.RequestHeadersSpec;

import com.penglecode.xmodule.common.security.oauth2.client.util.OAuth2ClientUtils;
import com.penglecode.xmodule.common.security.servlet.util.SpringSecurityUtils;

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
		return oauth2Configuration(null);
	}
	
	/**
	 * @param overrideDefaultRequest - 临时覆盖默认全局请求设置的钩子
	 * @return
	 */
	public Consumer<WebClient.Builder> oauth2Configuration(Consumer<RequestHeadersSpec<?>> overrideDefaultRequest) {
		return builder -> {
			Consumer<RequestHeadersSpec<?>> defaultRequest = delegatingOAuth2ClientExchangeFilter.defaultRequest();
			if(overrideDefaultRequest != null) {
				defaultRequest = defaultRequest.andThen(overrideDefaultRequest);
			}
			builder.defaultRequest(defaultRequest) //WebClient的全局默认请求设置
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
	 * 修改本次WebClient请求所使用的的OAuth2AuthorizedClient
	 * 例如使用当前登录者的OAuth2AuthorizedClient(基于Password模式)，或者使用应用API之间相互调用的OAuth2AuthorizedClient(基于client_credentials模式)
	 * @param authorizationGrantType
	 * @return
	 */
	public static Consumer<Map<String, Object>> withClientRegistration(String clientRegistrationId) {
		ClientRegistration clientRegistration = OAuth2ClientUtils.getClientRegistrationById(clientRegistrationId);
		Assert.notNull(clientRegistration, "No ClientRegistration found for clientRegistrationId: " + clientRegistrationId);
		if(AuthorizationGrantType.CLIENT_CREDENTIALS.equals(clientRegistration.getAuthorizationGrantType())) {
			/**
			 * AccessToken中的sub字段值与#OAuth2PrincipalNameAuthentication.name及#JwtAuthenticationToken.name的值保持一致(至于为什么要保持一致，究其原因是#JwtAuthenticationToken.name硬编码取自令牌的sub字段),
			 * 这样才能保证#OAuth2AuthorizedClientRepository.loadAuthorizedClient()方法在查找OAuth2AuthorizedClient时才能获取正确的结果
			 * (因为OAuth2AuthorizedClientRepository.loadAuthorizedClient()方法查找逻辑是根据key(clientRegistrationId + principal.name)作为唯一键来查找的)
			 */
			String principalName = clientRegistration.getClientId(); //此处不能轻易改动，必须约定好了
			Authentication authentication = OAuth2ClientUtils.prepareClientCredentialsOAuth2Authentication(clientRegistration, null, principalName);
			return attributes -> {
				attributes.put(CLIENT_REGISTRATION_ID_ATTR_NAME, clientRegistration.getRegistrationId());
				attributes.put(AUTHENTICATION_ATTR_NAME, authentication);
			};
		} else if (AuthorizationGrantType.PASSWORD.equals(clientRegistration.getAuthorizationGrantType()) || AuthorizationGrantType.AUTHORIZATION_CODE.equals(clientRegistration.getAuthorizationGrantType()) || AuthorizationGrantType.IMPLICIT.equals(clientRegistration.getAuthorizationGrantType())) {
			Authentication authentication = SpringSecurityUtils.getAuthentication(); //当前已登录用户
			return attributes -> {
				attributes.put(CLIENT_REGISTRATION_ID_ATTR_NAME, clientRegistration.getRegistrationId());
				attributes.put(AUTHENTICATION_ATTR_NAME, authentication);
			};
		}
		return attributes -> {};
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
		LOGGER.debug(">>> principal = {}", principal);
		HttpServletRequest httpRequest = (HttpServletRequest) request.attribute(HTTP_SERVLET_REQUEST_ATTR_NAME).get();
		OAuth2AuthorizedClient oauth2AuthorizedClient = OAuth2ClientUtils.getOAuth2AuthorizedClient(clientRegistrationId, principal, httpRequest);
		if(oauth2AuthorizedClient != null) {
			OAuth2AccessToken oauth2AccessToken = oauth2AuthorizedClient.getAccessToken();
			LOGGER.debug("{} - Apply OAuth2AccessToken[value = {}, issuedAt = {}, expiresAt = {}, scopes = {}] for Client[registrationId = {}, principalName = {}] ", request.logPrefix(), oauth2AccessToken.getTokenValue(), oauth2AccessToken.getIssuedAt(), oauth2AccessToken.getExpiresAt(), oauth2AccessToken.getScopes(), clientRegistrationId, principal.getName());
		}
		return next.exchange(request);
	}
	
}
