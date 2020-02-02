package com.penglecode.xmodule.common.security.oauth2.client.service;

import java.util.Arrays;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

/**
 * 基于Keycloak OAuth2认证服务器的CustomOAuth2AuthorizedClientService
 * 
 * @author 	pengpeng
 * @date 	2020年2月1日 下午6:03:18
 */
public class KeycloakCustomOAuth2AuthorizedClientService extends AbstractCustomOAuth2AuthorizedClientService {

	public KeycloakCustomOAuth2AuthorizedClientService(
			OAuth2AuthorizedClientService delegateOAuth2AuthorizedClientService) {
		super(delegateOAuth2AuthorizedClientService);
	}

	@Override
	protected void revokeAccessToken(OAuth2AuthorizedClient authorizedClient, String revokeEndpointUrl) {
		MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
		body.add("client_id", authorizedClient.getClientRegistration().getClientId());
		body.add("client_secret", authorizedClient.getClientRegistration().getClientSecret());
		body.add("refresh_token", authorizedClient.getRefreshToken().getTokenValue());
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.set("Authorizatio", "Bearer " + authorizedClient.getAccessToken().getTokenValue());
		HttpEntity<Object> requestEntity = new HttpEntity<>(body, headers);
		getRestTemplate().exchange(revokeEndpointUrl, HttpMethod.POST, requestEntity, String.class);
	}
	
}
