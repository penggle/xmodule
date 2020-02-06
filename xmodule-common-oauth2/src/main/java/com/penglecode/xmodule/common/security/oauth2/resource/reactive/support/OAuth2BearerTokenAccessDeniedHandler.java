package com.penglecode.xmodule.common.security.oauth2.resource.reactive.support;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.oauth2.server.resource.BearerTokenErrorCodes;
import org.springframework.security.oauth2.server.resource.authentication.AbstractOAuth2TokenAuthenticationToken;
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

/**
 * 自定义的OAuth2资源服务器AccessDeniedHandler
 * 
 * @author 	pengpeng
 * @date 	2020年2月4日 下午4:44:31
 */
public class OAuth2BearerTokenAccessDeniedHandler extends AbstractOAuth2BearerTokenErrorSupport<AccessDeniedException> implements ServerAccessDeniedHandler {

	@Override
	protected Mono<HttpStatus> resolveOAuth2ErrorHttpStatus(ServerWebExchange exchange, AccessDeniedException exception) {
		return Mono.just(HttpStatus.FORBIDDEN);
	}

	@Override
	protected Mono<Map<String,String>> resolveOAuth2ErrorParameters(ServerWebExchange exchange, AccessDeniedException exception) {
		return exchange.getPrincipal().filter(AbstractOAuth2TokenAuthenticationToken.class::isInstance).map(token -> {
			Map<String, String> parameters = new LinkedHashMap<>();
			parameters.put("error", BearerTokenErrorCodes.INSUFFICIENT_SCOPE);
			parameters.put("error_description", "The request requires higher privileges than provided by the access token.");
			parameters.put("error_uri", "https://tools.ietf.org/html/rfc6750#section-3.1");
			return parameters;
		}).switchIfEmpty(Mono.just(new LinkedHashMap<>()));
	}

}