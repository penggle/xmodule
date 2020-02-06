package com.penglecode.xmodule.common.security.oauth2.resource.reactive.support;

import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;

import com.nimbusds.oauth2.sdk.ParseException;
import com.penglecode.xmodule.common.security.oauth2.client.util.OAuth2ErrorUtils;
import com.penglecode.xmodule.common.support.Result;
import com.penglecode.xmodule.common.util.SpringWebFluxUtils;

import reactor.core.publisher.Mono;

public abstract class AbstractOAuth2BearerTokenErrorSupport<E extends Exception> {

	private String realmName;

	public Mono<Void> handle(ServerWebExchange exchange, E exception) {
		return Mono.defer(() -> {
			Mono<HttpStatus> status = resolveOAuth2ErrorHttpStatus(exchange, exception);
			Mono<Map<String,String>> parameters = resolveOAuth2ErrorParameters(exchange, exception).map(params -> {
				if(StringUtils.hasText(realmName)) {
					params.put("realm", realmName);
				}
				return params;
			});
			
			return Mono.zip(status, parameters).flatMap(tuple -> {
				String wwwAuthenticate = OAuth2ErrorUtils.computeWWWAuthenticateHeaderValue(tuple.getT2());
				ServerHttpResponse response = exchange.getResponse();
				response.getHeaders().set(HttpHeaders.WWW_AUTHENTICATE, wwwAuthenticate);
				response.setStatusCode(tuple.getT1());
				return postHandle(exchange, exception);
			});
		});
	}
	
	public Mono<Void> postHandle(ServerWebExchange exchange, E exception) {
		ServerHttpResponse response = exchange.getResponse();
		String wwwAuthenticate = response.getHeaders().getFirst(HttpHeaders.WWW_AUTHENTICATE);
		if(!StringUtils.isEmpty(wwwAuthenticate)) {
			HttpStatus status = response.getStatusCode();
			int code = status.value();
			String message = status.getReasonPhrase();
			if(!StringUtils.isEmpty(wwwAuthenticate)) {
				try {
					com.nimbusds.oauth2.sdk.token.BearerTokenError error = OAuth2ErrorUtils.parseBearerTokenError(wwwAuthenticate);
					if(!StringUtils.isEmpty(error.getCode()) && !StringUtils.isEmpty(error.getDescription())) {
						message = String.format("%s: %s", error.getCode(), error.getDescription());
					}
				} catch (ParseException ex) {
				}
			}
			Result<Object> result = Result.failure().code(code).message(message).build();
			return SpringWebFluxUtils.writeHttpMessage(exchange, result, MediaType.APPLICATION_JSON);
		}
		return Mono.empty();
	}
	
	protected abstract Mono<HttpStatus> resolveOAuth2ErrorHttpStatus(ServerWebExchange exchange, E exception);
	
	protected abstract Mono<Map<String,String>> resolveOAuth2ErrorParameters(ServerWebExchange exchange, E exception);
	
	protected String getRealmName() {
		return realmName;
	}

	public void setRealmName(String realmName) {
		this.realmName = realmName;
	}

}
