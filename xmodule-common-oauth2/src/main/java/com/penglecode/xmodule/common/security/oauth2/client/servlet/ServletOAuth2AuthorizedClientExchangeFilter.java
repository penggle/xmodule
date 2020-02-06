package com.penglecode.xmodule.common.security.oauth2.client.servlet;

import java.util.Map;
import java.util.function.Consumer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.util.Assert;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import org.springframework.web.reactive.function.client.WebClient;

import com.penglecode.xmodule.common.security.oauth2.client.servlet.util.OAuth2ClientUtils;
import com.penglecode.xmodule.common.security.oauth2.client.support.OAuth2PrincipalNameAuthentication;
import com.penglecode.xmodule.common.security.servlet.util.SpringSecurityUtils;
import com.penglecode.xmodule.common.util.SpringWebMvcUtils;

import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

/**
 * 默认的自定义的OAuth2客户端WebClient请求过滤器
 * 
 * @author 	pengpeng
 * @date	2019年12月16日 上午11:17:30
 */
public class ServletOAuth2AuthorizedClientExchangeFilter implements ExchangeFilterFunction {

	private static final Logger LOGGER = LoggerFactory.getLogger(ServletOAuth2AuthorizedClientExchangeFilter.class);
	
	/**
	 * The request attribute name used to locate the {@link OAuth2AuthorizedClient}.
	 */
	public static final String OAUTH2_AUTHORIZED_CLIENT_ATTR_NAME = OAuth2AuthorizedClient.class.getName();
	public static final String CLIENT_REGISTRATION_ID_ATTR_NAME = OAuth2AuthorizedClient.class.getName().concat(".CLIENT_REGISTRATION_ID");
	public static final String HTTP_SERVLET_REQUEST_ATTR_NAME = HttpServletRequest.class.getName();
	public static final String HTTP_SERVLET_RESPONSE_ATTR_NAME = HttpServletResponse.class.getName();
	
	private final String defaultClientRegistrationId;
	
	private final OAuth2AuthorizedClientManager authorizedClientManager;
	
	public ServletOAuth2AuthorizedClientExchangeFilter(String defaultClientRegistrationId,
			OAuth2AuthorizedClientManager authorizedClientManager) {
		super();
		Assert.hasText(defaultClientRegistrationId, "Parameter 'defaultClientRegistrationId' must be required!");
		Assert.notNull(authorizedClientManager, "Parameter 'authorizedClientManager' must be required!");
		this.defaultClientRegistrationId = defaultClientRegistrationId;
		this.authorizedClientManager = authorizedClientManager;
	}
	
	public Consumer<WebClient.Builder> oauth2Configuration() {
		return builder -> {
			builder.defaultRequest(defaultRequest()) //WebClient的全局默认请求设置
				   .filter(this);
		};
	}
	
	/**
	 * 默认的全局默认请求设置
	 * @return
	 */
	public Consumer<WebClient.RequestHeadersSpec<?>> defaultRequest() {
		return spec -> spec.attributes(attrs -> {
			applyDefaultRequestResponse(attrs);
			applyDefaultClientRegistration(attrs);
		});
	}
	
	/**
	 * 应用默认的Request/Response
	 * @param attrs
	 */
	protected void applyDefaultRequestResponse(Map<String, Object> attrs) {
		if (!attrs.containsKey(HTTP_SERVLET_REQUEST_ATTR_NAME) || !attrs.containsKey(HTTP_SERVLET_RESPONSE_ATTR_NAME)) { //如果已经指定了则不用默认的覆盖
			attrs.put(HTTP_SERVLET_REQUEST_ATTR_NAME, SpringWebMvcUtils.getCurrentHttpServletRequest());
			attrs.put(HTTP_SERVLET_RESPONSE_ATTR_NAME, SpringWebMvcUtils.getCurrentHttpServletResponse());
		}
	}
	
	/**
	 * 应用默认的ClientRegistration
	 * @param attrs
	 */
	protected void applyDefaultClientRegistration(Map<String, Object> attrs) {
		if(!attrs.containsKey(CLIENT_REGISTRATION_ID_ATTR_NAME)) {
			attrs.put(CLIENT_REGISTRATION_ID_ATTR_NAME, defaultClientRegistrationId);
		}
	}
	
	/**
	 * 直接指定WebClient请求鉴权所使用OAuth2AuthorizedClient(使用哪个AccessToken进行接口鉴权?)
	 * @param authorizedClient
	 * @return
	 */
	public static Consumer<Map<String, Object>> withAuthorizedClient(OAuth2AuthorizedClient authorizedClient) {
		return attributes -> {
			if (authorizedClient == null) {
				attributes.remove(OAUTH2_AUTHORIZED_CLIENT_ATTR_NAME);
			} else {
				attributes.put(OAUTH2_AUTHORIZED_CLIENT_ATTR_NAME, authorizedClient);
			}
		};
	}

	/**
	 * 通过指定的注册OAuth2客户端来确定WebClient请求鉴权所使用OAuth2AuthorizedClient(使用哪个AccessToken进行接口鉴权?)
	 * 如果clientRegistrationId代表的ClientRegistration是(OAuth2 password模式)，则从上下文获取当前Authentication(必须是JwtAuthenticationToken)，来确定OAuth2AuthorizedClient
	 * 如果clientRegistrationId代表的ClientRegistration是(OAuth2 authorization_code|implicit模式)，则从上下文获取当前Authentication(必须是OAuth2AuthenticationToken)，来确定OAuth2AuthorizedClient
	 * 否则clientRegistrationId代表的ClientRegistration是(OAuth2 client_credentials模式)，则从使用client_id作为principalName构造一个OAuth2PrincipalNameAuthentication，来确定OAuth2AuthorizedClient
	 * (构造OAuth2PrincipalNameAuthentication而不是使用匿名的AnonymousAuthenticationToken，是因为这样会导致获取到的OAuth2AuthorizedClient存储的位置变了,具体见#AuthenticatedPrincipalOAuth2AuthorizedClientRepository.saveAuthorizedClient(..))
	 * @param clientRegistrationId
	 * @return
	 */
	public static Consumer<Map<String, Object>> withClientRegistration(String clientRegistrationId) {
		return attributes -> attributes.put(CLIENT_REGISTRATION_ID_ATTR_NAME, clientRegistrationId);
	}
	
	/**
	 * 指定HttpServletRequest&HttpServletResponse
	 * @param request
	 * @param response
	 * @return
	 */
	public static Consumer<Map<String, Object>> withHttpServletRequestResponse(HttpServletRequest request, HttpServletResponse response) {
		return attributes -> {
			attributes.put(HTTP_SERVLET_REQUEST_ATTR_NAME, request);
			attributes.put(HTTP_SERVLET_RESPONSE_ATTR_NAME, response);
		};
	}

	@Override
	public Mono<ClientResponse> filter(ClientRequest request, ExchangeFunction next) {
		return prepareRequest(request)
				.filter(req -> req.attribute(OAUTH2_AUTHORIZED_CLIENT_ATTR_NAME).isPresent())
				.flatMap(req -> preferAuthorizedClient(getOAuth2AuthorizedClient(req), req))
				.switchIfEmpty(Mono.defer(() -> prepareRequest(request).flatMap(req -> defaultAuthorizedClient(getClientRegistrationId(req), req))))
				.map(authorizedClient -> bearer(request, authorizedClient))
				.flatMap(next::exchange)
				.switchIfEmpty(Mono.defer(() -> next.exchange(request)));
	}
	
	protected Mono<ClientRequest> prepareRequest(ClientRequest request) {
		return Mono.just(request);
	}

	protected Mono<OAuth2AuthorizedClient> preferAuthorizedClient(OAuth2AuthorizedClient authorizedClient, ClientRequest request) {
		if (this.authorizedClientManager == null) {
			return Mono.just(authorizedClient);
		}
		Authentication authentication = new OAuth2PrincipalNameAuthentication(authorizedClient.getPrincipalName(), AuthorityUtils.createAuthorityList("ROLE_ANONYMOUS"));
		HttpServletRequest servletRequest = getRequest(request);
		HttpServletResponse servletResponse = getResponse(request);

		OAuth2AuthorizeRequest.Builder builder = OAuth2AuthorizeRequest.withAuthorizedClient(authorizedClient).principal(authentication);
		builder.attributes(attributes -> {
			if (servletRequest != null) {
				attributes.put(HttpServletRequest.class.getName(), servletRequest);
			}
			if (servletResponse != null) {
				attributes.put(HttpServletResponse.class.getName(), servletResponse);
			}
		});
		OAuth2AuthorizeRequest reauthorizeRequest = builder.build();

		// NOTE: 'authorizedClientManager.authorize()' needs to be executed on a dedicated thread via subscribeOn(Schedulers.boundedElastic())
		// since it performs a blocking I/O operation using RestTemplate internally
		return Mono.fromSupplier(() -> this.authorizedClientManager.authorize(reauthorizeRequest)).subscribeOn(Schedulers.boundedElastic());
	}
	
	protected Mono<OAuth2AuthorizedClient> defaultAuthorizedClient(String clientRegistrationId, ClientRequest request) {
		Authentication authentication = resolveAgreedAuthentication(request);
		HttpServletRequest servletRequest = getRequest(request);
		HttpServletResponse servletResponse = getResponse(request);

		OAuth2AuthorizeRequest.Builder builder = OAuth2AuthorizeRequest.withClientRegistrationId(clientRegistrationId).principal(authentication);
		builder.attributes(attributes -> {
			if (servletRequest != null) {
				attributes.put(HttpServletRequest.class.getName(), servletRequest);
			}
			if (servletResponse != null) {
				attributes.put(HttpServletResponse.class.getName(), servletResponse);
			}
		});
		OAuth2AuthorizeRequest authorizeRequest = builder.build();

		// NOTE: 'authorizedClientManager.authorize()' needs to be executed on a dedicated thread via subscribeOn(Schedulers.boundedElastic())
		// NOTE: 'authorizedClientManager.authorize()' needs to be executed on a dedicated thread via subscribeOn(Schedulers.boundedElastic())
		// since it performs a blocking I/O operation using RestTemplate internally
		return Mono.fromSupplier(() -> this.authorizedClientManager.authorize(authorizeRequest)).subscribeOn(Schedulers.boundedElastic());
	}
	
	/**
	 * 根据约定获取Authentication
	 * @param request
	 * @return
	 */
	protected Authentication resolveAgreedAuthentication(ClientRequest request) {
		String clientRegistrationId = getClientRegistrationId(request);
		ClientRegistration clientRegistration = OAuth2ClientUtils.getClientRegistrationById(clientRegistrationId);
		Assert.notNull(clientRegistration, "No ClientRegistration found for clientRegistrationId: " + clientRegistrationId);
		if (AuthorizationGrantType.PASSWORD.equals(clientRegistration.getAuthorizationGrantType())) { //OAuth2 password认证授权模式
			JwtAuthenticationToken authentication = SpringSecurityUtils.getCurrentAuthentication(); //当前已登录用户(此语句强制约束了：用户已登录且登录身份是JwtAuthenticationToken，否则报错)
			return authentication;
		} else if (AuthorizationGrantType.AUTHORIZATION_CODE.equals(clientRegistration.getAuthorizationGrantType()) 
				|| AuthorizationGrantType.IMPLICIT.equals(clientRegistration.getAuthorizationGrantType())) { //OAuth2 authorization_code|implicit认证授权模式
			OAuth2AuthenticationToken authentication = SpringSecurityUtils.getCurrentAuthentication(); //当前已登录用户(此语句强制约束了：用户已登录且登录身份是OAuth2AuthenticationToken，否则报错)
			return authentication;
		} else { //OAuth2 client_credentials认证授权模式 (如果当前处于未登录状态只能走此分支)
			/**
			 * AccessToken中的sub字段值与#OAuth2PrincipalNameAuthentication.name及#JwtAuthenticationToken.name的值保持一致(至于为什么要保持一致，究其原因是#JwtAuthenticationToken.name硬编码取自令牌的sub字段),
			 * 这样才能保证#OAuth2AuthorizedClientRepository.loadAuthorizedClient()方法在查找OAuth2AuthorizedClient时才能获取正确的结果
			 * (因为OAuth2AuthorizedClientRepository.loadAuthorizedClient()方法查找逻辑是根据key(clientRegistrationId + principal.name)作为唯一键来查找的)
			 */
			String principalName = clientRegistration.getClientId(); //此处不能轻易改动，必须约定好了
			Authentication authentication = OAuth2ClientUtils.prepareClientCredentialsOAuth2Authentication(clientRegistration, null, principalName);
			return authentication;
		}
	}
	
	protected ClientRequest bearer(ClientRequest request, OAuth2AuthorizedClient authorizedClient) {
		request = ClientRequest.from(request)
					.headers(headers -> headers.setBearerAuth(authorizedClient.getAccessToken().getTokenValue()))
					.build();
		log(request, authorizedClient);
		return request;
	}
	
	protected void log(ClientRequest request, OAuth2AuthorizedClient authorizedClient) {
		String clientRegistrationId = authorizedClient.getClientRegistration().getRegistrationId();
		String principalName = authorizedClient.getPrincipalName();
		OAuth2AccessToken oauth2AccessToken = authorizedClient.getAccessToken();
		LOGGER.debug("{} - Apply OAuth2AccessToken[value = {}, issuedAt = {}, expiresAt = {}, scopes = {}] for Client[clientRegistrationId = {}, principalName = {}] ", request.logPrefix(), oauth2AccessToken.getTokenValue(), oauth2AccessToken.getIssuedAt(), oauth2AccessToken.getExpiresAt(), oauth2AccessToken.getScopes(), clientRegistrationId, principalName);
	}
	
	static OAuth2AuthorizedClient getOAuth2AuthorizedClient(ClientRequest request) {
		return (OAuth2AuthorizedClient) request.attributes().get(OAUTH2_AUTHORIZED_CLIENT_ATTR_NAME);
	}

	static String getClientRegistrationId(ClientRequest request) {
		return (String) request.attributes().get(CLIENT_REGISTRATION_ID_ATTR_NAME);
	}

	static HttpServletRequest getRequest(ClientRequest request) {
		return (HttpServletRequest) request.attributes().get(HTTP_SERVLET_REQUEST_ATTR_NAME);
	}

	static HttpServletResponse getResponse(ClientRequest request) {
		return (HttpServletResponse) request.attributes().get(HTTP_SERVLET_RESPONSE_ATTR_NAME);
	}

	public String getDefaultClientRegistrationId() {
		return defaultClientRegistrationId;
	}

	public OAuth2AuthorizedClientManager getAuthorizedClientManager() {
		return authorizedClientManager;
	}
	
}
