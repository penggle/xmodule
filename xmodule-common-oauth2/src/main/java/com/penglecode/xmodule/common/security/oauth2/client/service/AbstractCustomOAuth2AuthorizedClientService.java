package com.penglecode.xmodule.common.security.oauth2.client.service;

import java.net.URI;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

/**
 * CustomOAuth2AuthorizedClientService基类
 * 
 * @author 	pengpeng
 * @date 	2020年2月1日 下午6:03:18
 */
public abstract class AbstractCustomOAuth2AuthorizedClientService implements CustomOAuth2AuthorizedClientService, InitializingBean {

	private final OAuth2AuthorizedClientService delegateOAuth2AuthorizedClientService;
	
	@Autowired(required=false)
	@Qualifier("defaultRestTemplate")
	private RestTemplate restTemplate;
	
	/**
	 * 回收令牌的endpoint在OAuth2认证服务器提供的Provider自动发现接口响应JSON中的名字
	 */
	private String revocationEndpointName = "end_session_endpoint";
	
	public AbstractCustomOAuth2AuthorizedClientService(
			OAuth2AuthorizedClientService delegateOAuth2AuthorizedClientService) {
		super();
		Assert.notNull(delegateOAuth2AuthorizedClientService, "Parameter 'delegateOAuth2AuthorizedClientService' must be required!");
		this.delegateOAuth2AuthorizedClientService = delegateOAuth2AuthorizedClientService;
	}

	@Override
	public <T extends OAuth2AuthorizedClient> T loadAuthorizedClient(String clientRegistrationId,
			String principalName) {
		return delegateOAuth2AuthorizedClientService.loadAuthorizedClient(clientRegistrationId, principalName);
	}

	@Override
	public void saveAuthorizedClient(OAuth2AuthorizedClient authorizedClient, Authentication principal) {
		delegateOAuth2AuthorizedClientService.saveAuthorizedClient(authorizedClient, principal);
	}

	@Override
	public void removeAuthorizedClient(String clientRegistrationId, String principalName) {
		delegateOAuth2AuthorizedClientService.removeAuthorizedClient(clientRegistrationId, principalName);
	}

	@Override
	public void revokeAuthorizedClient(OAuth2AuthorizedClient authorizedClient) {
		if(authorizedClient != null) {
			removeAuthorizedClient(authorizedClient.getClientRegistration().getRegistrationId(), authorizedClient.getPrincipalName());
			String revokeEndpointUrl = getAccessTokenRevokeEndpointUrl(authorizedClient);
			revokeAccessToken(authorizedClient, revokeEndpointUrl);
		}
	}
	
	protected abstract void revokeAccessToken(OAuth2AuthorizedClient authorizedClient, String revokeEndpointUrl);
	
	protected String getAccessTokenRevokeEndpointUrl(OAuth2AuthorizedClient authorizedClient) {
		URI result = null;
		Object endSessionEndpoint = authorizedClient.getClientRegistration().getProviderDetails().getConfigurationMetadata().get(getRevocationEndpointName());
		if (endSessionEndpoint != null) {
			result = URI.create(endSessionEndpoint.toString());
		}
		return result.toString();
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		if(this.restTemplate == null) {
			this.restTemplate = new RestTemplate();
		}
	}

	protected OAuth2AuthorizedClientService getDelegateOAuth2AuthorizedClientService() {
		return delegateOAuth2AuthorizedClientService;
	}

	protected String getRevocationEndpointName() {
		return revocationEndpointName;
	}

	public void setRevocationEndpointName(String revocationEndpointName) {
		this.revocationEndpointName = revocationEndpointName;
	}

	protected RestTemplate getRestTemplate() {
		return restTemplate;
	}

	public void setRestTemplate(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

}
