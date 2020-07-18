package com.penglecode.xmodule.common.security.oauth2.client.reactive;

import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientManager;
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
import org.springframework.web.server.ServerWebExchange;

import com.penglecode.xmodule.common.security.oauth2.client.reactive.util.OAuth2ClientUtils;
import com.penglecode.xmodule.common.security.reactive.util.SpringSecurityUtils;
import com.penglecode.xmodule.common.util.SpringWebFluxUtils;

import reactor.core.publisher.Mono;

/**
 * 默认的自定义的OAuth2客户端WebClient请求过滤器
 * 
 * @author 	pengpeng
 * @date	2019年12月16日 上午11:17:30
 */
public class ServerOAuth2AuthorizedClientExchangeFilter implements ExchangeFilterFunction {

	private static final Logger LOGGER = LoggerFactory.getLogger(ServerOAuth2AuthorizedClientExchangeFilter.class);
	
	/**
	 * The request attribute name used to locate the {@link OAuth2AuthorizedClient}.
	 */
	public static final String OAUTH2_AUTHORIZED_CLIENT_ATTR_NAME = OAuth2AuthorizedClient.class.getName();
	public static final String CLIENT_REGISTRATION_ID_ATTR_NAME = OAuth2AuthorizedClient.class.getName().concat(".CLIENT_REGISTRATION_ID");
	private static final String SERVER_WEB_EXCHANGE_ATTR_NAME = ServerWebExchange.class.getName();
	
	private final String defaultClientRegistrationId;
	
	private final ReactiveOAuth2AuthorizedClientManager authorizedClientManager;
	
	public ServerOAuth2AuthorizedClientExchangeFilter(String defaultClientRegistrationId,
			ReactiveOAuth2AuthorizedClientManager authorizedClientManager) {
		super();
		Assert.notNull(authorizedClientManager, "Parameter 'authorizedClientManager' must be required!");
		this.defaultClientRegistrationId = defaultClientRegistrationId;
		this.authorizedClientManager = authorizedClientManager;
	}
	
	public Consumer<WebClient.Builder> oauth2Configuration() {
		return builder -> builder.defaultRequest(defaultRequest()) //WebClient的全局默认请求设置
			   .filter(this);
	}
	
	/**
	 * 默认的全局默认请求设置
	 * @return
	 */
	public Consumer<WebClient.RequestHeadersSpec<?>> defaultRequest() {
		return spec -> spec.attributes(this::applyDefaultClientRegistration);
	}
	
	/**
	 * 应用默认的ClientRegistration
	 * @param attrs
	 */
	protected void applyDefaultClientRegistration(Map<String, Object> attrs) {
		if(!attrs.containsKey(CLIENT_REGISTRATION_ID_ATTR_NAME)) {
			Assert.hasText(defaultClientRegistrationId, "Parameter 'defaultClientRegistrationId' must be required!");
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
	 * Modifies the {@link ClientRequest#attributes()} to include the {@link ServerWebExchange} to be used for
	 * providing the Bearer Token. Example usage:
	 *
	 * <pre>
	 * WebClient webClient = WebClient.builder()
	 *    .filter(new ServerOAuth2AuthorizedClientExchangeFilterFunction(authorizedClientManager))
	 *    .build();
	 * Mono<String> response = webClient
	 *    .get()
	 *    .uri(uri)
	 *    .attributes(serverWebExchange(serverWebExchange))
	 *    // ...
	 *    .retrieve()
	 *    .bodyToMono(String.class);
	 * </pre>
	 * @param serverWebExchange the {@link ServerWebExchange} to use
	 * @return the {@link Consumer} to populate the client request attributes
	 */
	public static Consumer<Map<String, Object>> serverWebExchange(ServerWebExchange serverWebExchange) {
		return attributes -> attributes.put(SERVER_WEB_EXCHANGE_ATTR_NAME, serverWebExchange);
	}

	@Override
	public Mono<ClientResponse> filter(ClientRequest request, ExchangeFunction next) {
		return authorizedClient(request)
				.map(authorizedClient -> bearer(request, authorizedClient))
				.flatMap(next::exchange)
				.switchIfEmpty(Mono.defer(() -> next.exchange(request)));
	}

	protected Mono<OAuth2AuthorizedClient> authorizedClient(ClientRequest request) {
		OAuth2AuthorizedClient authorizedClientFromAttrs = getOAuth2AuthorizedClient(request); //1、从请求属性中直接获取指定的authorizedClient
		return Mono.justOrEmpty(authorizedClientFromAttrs)
				.switchIfEmpty(Mono.defer(() ->
						authorizeRequest(request).flatMap(authorizedClientManager::authorize))) //2、如果1步骤中未能获得到authorizedClient，则根据ClientRegistration + Authentication构造OAuth2AuthorizeRequest获取authorizedClient
				.flatMap(authorizedClient ->
						reauthorizeRequest(request, authorizedClient).flatMap(authorizedClientManager::authorize)); //3、如果1步骤中获取到了authorizedClient，此步骤对获取到的authorizedClient进行重新授权(检测令牌有效性,无效则重新获取令牌)
	}

	protected Mono<OAuth2AuthorizeRequest> authorizeRequest(ClientRequest request) {
		Mono<Authentication> authentication = resolveAgreedAuthentication(request);

		Mono<String> clientRegistrationId = Mono.just(getClientRegistrationId(request));

		Mono<Optional<ServerWebExchange>> serverWebExchange = Mono.justOrEmpty(getServerWebExchange(request))
				.switchIfEmpty(SpringWebFluxUtils.getCurrentServerWebExchange())
				.map(Optional::of)
				.defaultIfEmpty(Optional.empty());

		return Mono.zip(clientRegistrationId, authentication, serverWebExchange)
				.map(t3 -> {
					OAuth2AuthorizeRequest.Builder builder = OAuth2AuthorizeRequest.withClientRegistrationId(t3.getT1()).principal(t3.getT2());
					if (t3.getT3().isPresent()) {
						builder.attribute(ServerWebExchange.class.getName(), t3.getT3().get());
					}
					return builder.build();
				});
	}

	/**
	 * 重新授权请求(底层根据authorizedClient所持令牌是否有效而进一步决定是否重新获取新的令牌)
	 * @param request
	 * @param authorizedClient
	 * @return
	 */
	protected Mono<OAuth2AuthorizeRequest> reauthorizeRequest(ClientRequest request, OAuth2AuthorizedClient authorizedClient) {
		Mono<Authentication> authentication = resolveAgreedAuthentication(request);

		Mono<Optional<ServerWebExchange>> serverWebExchange = Mono.justOrEmpty(getServerWebExchange(request))
				.switchIfEmpty(SpringWebFluxUtils.getCurrentServerWebExchange())
				.map(Optional::of)
				.defaultIfEmpty(Optional.empty());

		return Mono.zip(authentication, serverWebExchange)
				.map(t2 -> {
					OAuth2AuthorizeRequest.Builder builder = OAuth2AuthorizeRequest.withAuthorizedClient(authorizedClient).principal(t2.getT1());
					if (t2.getT2().isPresent()) {
						builder.attribute(ServerWebExchange.class.getName(), t2.getT2().get());
					}
					return builder.build();
				});
	}
	
	/**
	 * 根据约定获取Authentication
	 * @param clientRegistrationId
	 * @return
	 */
	protected Mono<Authentication> resolveAgreedAuthentication(ClientRequest request) {
		String clientRegistrationId = getClientRegistrationId(request);
		Mono<ClientRegistration> clientRegistration = OAuth2ClientUtils.getClientRegistrationById(clientRegistrationId);
		return clientRegistration.flatMap(registration -> {
			if (AuthorizationGrantType.PASSWORD.equals(registration.getAuthorizationGrantType())) { //OAuth2 password认证授权模式
				Mono<Authentication> authentication = SpringSecurityUtils.getCurrentAuthentication();
				return authentication.ofType(JwtAuthenticationToken.class); //当前已登录用户(此语句强制约束了：用户已登录且登录身份是JwtAuthenticationToken，否则报错)
			} else if (AuthorizationGrantType.AUTHORIZATION_CODE.equals(registration.getAuthorizationGrantType()) 
					|| AuthorizationGrantType.IMPLICIT.equals(registration.getAuthorizationGrantType())) { //OAuth2 authorization_code|implicit认证授权模式
				Mono<Authentication> authentication = SpringSecurityUtils.getCurrentAuthentication();
				return authentication.ofType(OAuth2AuthenticationToken.class); //当前已登录用户(此语句强制约束了：用户已登录且登录身份是OAuth2AuthenticationToken，否则报错)
			} else { //OAuth2 client_credentials认证授权模式 (如果当前处于未登录状态只能走此分支)
				/**
				 * AccessToken中的sub字段值与#OAuth2PrincipalNameAuthentication.name及#JwtAuthenticationToken.name的值保持一致(至于为什么要保持一致，究其原因是#JwtAuthenticationToken.name硬编码取自令牌的sub字段),
				 * 这样才能保证#OAuth2AuthorizedClientRepository.loadAuthorizedClient()方法在查找OAuth2AuthorizedClient时才能获取正确的结果
				 * (因为OAuth2AuthorizedClientRepository.loadAuthorizedClient()方法查找逻辑是根据key(clientRegistrationId + principal.name)作为唯一键来查找的)
				 */
				String principalName = registration.getClientId(); //此处不能轻易改动，必须约定好了
				Authentication authentication = OAuth2ClientUtils.prepareClientCredentialsOAuth2Authentication(registration, null, principalName);
				return Mono.just(authentication);
			}
		}).switchIfEmpty(Mono.error(new IllegalStateException("No ClientRegistration found for clientRegistrationId: " + clientRegistrationId)));
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

	static ServerWebExchange getServerWebExchange(ClientRequest request) {
		return (ServerWebExchange) request.attributes().get(SERVER_WEB_EXCHANGE_ATTR_NAME);
	}
	
	public String getDefaultClientRegistrationId() {
		return defaultClientRegistrationId;
	}

	public ReactiveOAuth2AuthorizedClientManager getAuthorizedClientManager() {
		return authorizedClientManager;
	}
	
}
