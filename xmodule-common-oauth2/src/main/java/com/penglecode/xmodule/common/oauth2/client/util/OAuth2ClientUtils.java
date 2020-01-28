package com.penglecode.xmodule.common.oauth2.client.util;

import java.util.LinkedHashSet;
import java.util.Set;

import org.springframework.core.ResolvableType;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.OAuth2AccessToken;

import com.penglecode.xmodule.common.security.servlet.util.SpringSecurityUtils;
import com.penglecode.xmodule.common.util.SpringUtils;

/**
 * 有关OAuth2客户端的工具类
 * 
 * @author 	pengpeng
 * @date 	2020年1月27日 上午11:41:21
 */
@SuppressWarnings("unchecked")
public class OAuth2ClientUtils {

	/**
	 * 获取所有OAuth2客户端注册
	 * @return
	 */
	public static Set<ClientRegistration> getAllClientRegistrations() {
		Set<ClientRegistration> clientRegistrations = new LinkedHashSet<ClientRegistration>();
		ClientRegistrationRepository clientRegistrationRepository = SpringUtils.getBean(ClientRegistrationRepository.class);
		ResolvableType type = ResolvableType.forInstance(clientRegistrationRepository).as(Iterable.class);
		if (type != ResolvableType.NONE && ClientRegistration.class.isAssignableFrom(type.resolveGenerics()[0])) {
			Iterable<ClientRegistration> registrations = (Iterable<ClientRegistration>) clientRegistrationRepository;
			if(registrations != null) {
				registrations.forEach(clientRegistrations::add);
			}
		}
		return clientRegistrations;
	}
	
	/**
	 * 获取当前已授权客户端
	 * @return
	 */
	public static OAuth2AuthorizedClient getCurrentOAuth2Client() {
		OAuth2AuthenticationToken authentication = SpringSecurityUtils.getAuthentication();
		OAuth2AuthorizedClientService authorizedClientService = SpringUtils.getBean(OAuth2AuthorizedClientService.class);
		return authorizedClientService.loadAuthorizedClient(authentication.getAuthorizedClientRegistrationId(), authentication.getName());
	}
	
	/**
	 * 获取当前已授权的OAuth2客户端的AccessToken
	 * @return
	 */
	public static OAuth2AccessToken getCurrentOAuth2AccessToken() {
		OAuth2AuthorizedClient client = getCurrentOAuth2Client();
		if(client != null) {
			return client.getAccessToken();
		}
		return null;
	}
	
}
