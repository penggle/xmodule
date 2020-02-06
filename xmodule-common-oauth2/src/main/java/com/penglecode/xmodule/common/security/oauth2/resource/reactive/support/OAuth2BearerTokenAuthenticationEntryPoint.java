package com.penglecode.xmodule.common.security.oauth2.resource.reactive.support;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.server.resource.BearerTokenError;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

/**
 * 自定义的OAuth2资源服务器AuthenticationEntryPoint
 * 
 * @author 	pengpeng
 * @date 	2020年2月4日 下午3:38:58
 */
public class OAuth2BearerTokenAuthenticationEntryPoint extends AbstractOAuth2BearerTokenErrorSupport<AuthenticationException> implements ServerAuthenticationEntryPoint {

	@Override
	public Mono<Void> commence(ServerWebExchange exchange, AuthenticationException authException) {
		return handle(exchange, authException);
	}
	
	protected Mono<Map<String,String>> resolveOAuth2ErrorParameters(ServerWebExchange exchange, AuthenticationException exception) {
		Map<String, String> parameters = new LinkedHashMap<>();
		if (exception instanceof OAuth2AuthenticationException) {
			OAuth2Error error = ((OAuth2AuthenticationException) exception).getError();

			parameters.put("error", error.getErrorCode());

			if (StringUtils.hasText(error.getDescription())) {
				parameters.put("error_description", error.getDescription());
			}

			if (StringUtils.hasText(error.getUri())) {
				parameters.put("error_uri", error.getUri());
			}

			if (error instanceof BearerTokenError) {
				BearerTokenError bearerTokenError = (BearerTokenError) error;

				if (StringUtils.hasText(bearerTokenError.getScope())) {
					parameters.put("scope", bearerTokenError.getScope());
				}
			}
		}
		return Mono.just(parameters);
	}

	protected Mono<HttpStatus> resolveOAuth2ErrorHttpStatus(ServerWebExchange exchange, AuthenticationException exception) {
		HttpStatus status = HttpStatus.UNAUTHORIZED;
		if (exception instanceof OAuth2AuthenticationException) {
			OAuth2Error error = ((OAuth2AuthenticationException) exception).getError();
			if (error instanceof BearerTokenError) {
				status = ((BearerTokenError) error).getHttpStatus();
			}
		}
		return Mono.just(status);
	}
	
}
