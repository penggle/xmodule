package com.penglecode.xmodule.common.security.oauth2.client.util;

import java.util.Arrays;

import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.security.oauth2.core.http.converter.OAuth2AccessTokenResponseHttpMessageConverter;
import org.springframework.security.oauth2.core.http.converter.OAuth2ErrorHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import com.penglecode.xmodule.common.security.oauth2.client.support.DefaultOAuth2ErrorResponseErrorHandler;
import com.penglecode.xmodule.common.security.oauth2.client.support.KeycloakOAuth2ErrorConverter;

public class OAuth2AccessTokenResponseClientUtils {

	public static RestTemplate createDefaultRestTemplate() {
		RestTemplate restTemplate = new RestTemplate(Arrays.asList(
				new FormHttpMessageConverter(), new OAuth2AccessTokenResponseHttpMessageConverter()));
		restTemplate.setRequestFactory(new BufferingClientHttpRequestFactory(new OkHttp3ClientHttpRequestFactory())); //请求体/响应体可缓存
		
		OAuth2ErrorHttpMessageConverter oauth2ErrorHttpMessageConverter = new OAuth2ErrorHttpMessageConverter();
		oauth2ErrorHttpMessageConverter.setErrorConverter(new KeycloakOAuth2ErrorConverter()); //OAuth2授权服务器用的是Keycloak
		restTemplate.setErrorHandler(new DefaultOAuth2ErrorResponseErrorHandler(oauth2ErrorHttpMessageConverter));
		return restTemplate;
	}
	
}
