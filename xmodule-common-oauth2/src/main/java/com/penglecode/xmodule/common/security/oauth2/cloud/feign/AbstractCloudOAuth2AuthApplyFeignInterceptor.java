package com.penglecode.xmodule.common.security.oauth2.cloud.feign;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.Assert;

import com.penglecode.xmodule.common.exception.ApplicationServiceApiException;
import com.penglecode.xmodule.common.security.oauth2.client.servlet.util.OAuth2ClientUtils;
import com.penglecode.xmodule.common.security.oauth2.client.support.OAuth2ClientConfigProperties;
import com.penglecode.xmodule.common.security.oauth2.consts.OAuth2ApplicationConstants;
import com.penglecode.xmodule.common.util.CollectionUtils;

import feign.RequestInterceptor;
import feign.RequestTemplate;

/**
 * 微服务之间接口调用鉴权之基于OAuth2认证的AccessToken拦截器
 * 
 * 即在需要API鉴权的接口请求之前设置header[Authorization]
 * 
 * @author 	pengpeng
 * @date 	2020年2月12日 下午1:36:17
 */
public abstract class AbstractCloudOAuth2AuthApplyFeignInterceptor implements RequestInterceptor {

	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractCloudOAuth2AuthApplyFeignInterceptor.class);
	
	private AntPathMatcher apiRequestPathMatcher = new AntPathMatcher();
	
	@Autowired
	private OAuth2ClientConfigProperties oauth2ClientConfig;
	
	@Override
	public void apply(RequestTemplate template) {
		try {
			String path = template.path();
			boolean applyAuthorization = true;
			List<String> excludePatterns = oauth2ClientConfig.getAppAuthExcludesUrl();
			if(!CollectionUtils.isEmpty(excludePatterns)) {
				for(String excludePattern : excludePatterns) {
					if(apiRequestPathMatcher.match(excludePattern, path)) {
						applyAuthorization = false;
						break;
					}
				}
			}
			if(applyAuthorization) {
				OAuth2AuthorizeRequest authorizeRequest = buildAuthorizeRequest(template, createAuthorizeRequestBuilder());
				OAuth2AuthorizedClient authorizedClient = authorize(authorizeRequest);
				Assert.notNull(authorizedClient, "Can not obtain a OAuth2AuthorizedClient!");
				String authorization = authorizedClient.getAccessToken().getTokenValue();
				LOGGER.debug(">>> Apply OAuth2 'Authorization' [{}] with ClientRegistration({}) for request: {}", authorization, authorizeRequest.getClientRegistrationId(), path);
				FeignUtils.setHeader(template, "Authorization", "bearer " + authorization);
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			throw new ApplicationServiceApiException(e.getMessage(), e);
		}
	}
	
	protected abstract OAuth2AuthorizedClient authorize(OAuth2AuthorizeRequest authorizeRequest);
	
	protected abstract OAuth2AuthorizeRequest buildAuthorizeRequest(RequestTemplate template, OAuth2AuthorizeRequest.Builder builder);
	
	protected OAuth2AuthorizeRequest.Builder createAuthorizeRequestBuilder() {
		//默认使用当前应用的APP客户端(client_credentials模式)来进行Feign请求OAuth2认证
		String clientRegistrationId = OAuth2ApplicationConstants.DEFAULT_OAUTH2_CLIENT_CONFIG.getAppRegistrationId();
		ClientRegistration clientRegistration = OAuth2ClientUtils.getClientRegistrationById(clientRegistrationId);
		Assert.notNull(clientRegistration, "No ClientRegistration found for clientRegistrationId: " + clientRegistrationId);
		String principalName = clientRegistration.getClientId(); //此处是约定好的
		Authentication authentication = OAuth2ClientUtils.prepareClientCredentialsOAuth2Authentication(clientRegistration, null, principalName);
		
		return OAuth2AuthorizeRequest.withClientRegistrationId(clientRegistration.getRegistrationId()).principal(authentication);
	}
	
	protected OAuth2ClientConfigProperties getOauth2ClientConfig() {
		return oauth2ClientConfig;
	}

	protected AntPathMatcher getApiRequestPathMatcher() {
		return apiRequestPathMatcher;
	}

	public void setApiRequestPathMatcher(AntPathMatcher apiRequestPathMatcher) {
		this.apiRequestPathMatcher = apiRequestPathMatcher;
	}

}