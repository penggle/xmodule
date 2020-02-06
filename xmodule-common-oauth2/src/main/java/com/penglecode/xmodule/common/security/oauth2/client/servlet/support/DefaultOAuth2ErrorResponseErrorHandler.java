/*
 * Copyright 2002-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.penglecode.xmodule.common.security.oauth2.client.servlet.support;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.oauth2.core.OAuth2AuthorizationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.http.converter.OAuth2ErrorHttpMessageConverter;
import org.springframework.util.StringUtils;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.ResponseErrorHandler;

import com.nimbusds.oauth2.sdk.token.BearerTokenError;

/**
 * 自定义的默认OAuth2Error ResponseErrorHandler
 * 本类实现copy from #OAuth2ErrorResponseErrorHandler
 * 
 * @author 	pengpeng
 * @date	2019年12月21日 下午2:36:01
 */
public class DefaultOAuth2ErrorResponseErrorHandler implements ResponseErrorHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultOAuth2ErrorResponseErrorHandler.class);
	private final OAuth2ErrorHttpMessageConverter oauth2ErrorConverter;
	private final ResponseErrorHandler defaultErrorHandler = new DefaultResponseErrorHandler();

	public DefaultOAuth2ErrorResponseErrorHandler() {
		this(new OAuth2ErrorHttpMessageConverter());
	}

	public DefaultOAuth2ErrorResponseErrorHandler(OAuth2ErrorHttpMessageConverter oauth2ErrorConverter) {
		super();
		this.oauth2ErrorConverter = oauth2ErrorConverter;
	}

	@Override
	public boolean hasError(ClientHttpResponse response) throws IOException {
		return this.defaultErrorHandler.hasError(response);
	}

	@Override
	public void handleError(ClientHttpResponse response) throws IOException {
		// A Bearer Token Error may be in the WWW-Authenticate response header
		// See https://tools.ietf.org/html/rfc6750#section-3
		OAuth2Error oauth2Error = this.readErrorFromWwwAuthenticate(response.getHeaders());
		if (oauth2Error == null) {
			oauth2Error = this.oauth2ErrorConverter.read(OAuth2Error.class, response);
		}
		if(oauth2Error != null) {
			LOGGER.error(">>> oauth2Error = {}", oauth2Error);
			throw new OAuth2AuthorizationException(oauth2Error);
		}
		this.defaultErrorHandler.handleError(response);
	}

	protected OAuth2Error readErrorFromWwwAuthenticate(HttpHeaders headers) {
		String wwwAuthenticateHeader = headers.getFirst(HttpHeaders.WWW_AUTHENTICATE);
		if (!StringUtils.hasText(wwwAuthenticateHeader)) {
			return null;
		}

		BearerTokenError bearerTokenError;
		try {
			bearerTokenError = BearerTokenError.parse(wwwAuthenticateHeader);
		} catch (Exception ex) {
			return null;
		}

		String errorCode = bearerTokenError.getCode() != null ?
				bearerTokenError.getCode() : OAuth2ErrorCodes.SERVER_ERROR;
		String errorDescription = bearerTokenError.getDescription();
		String errorUri = bearerTokenError.getURI() != null ?
				bearerTokenError.getURI().toString() : null;

		return new OAuth2Error(errorCode, errorDescription, errorUri);
	}
}
