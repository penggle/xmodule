package com.penglecode.xmodule.common.security.oauth2.client.support;

import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;

/**
 * 自定义的扩展OAuth2已授权客户端服务
 * 
 * @author 	pengpeng
 * @date 	2020年2月1日 下午5:43:04
 */
public interface CustomOAuth2AuthorizedClientService extends OAuth2AuthorizedClientService {

	/**
	 * 回收与该OAuth2AuthorizedClient相关连的令牌
	 * @param authorizedClient
	 */
	public void revokeAuthorizedClient(OAuth2AuthorizedClient authorizedClient);
	
}
