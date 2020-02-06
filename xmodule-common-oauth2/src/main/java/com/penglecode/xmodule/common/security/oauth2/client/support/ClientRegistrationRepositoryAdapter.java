package com.penglecode.xmodule.common.security.oauth2.client.support;

import org.springframework.security.oauth2.client.registration.ClientRegistration;

/**
 * ClientRegistrationRepository的适配器
 * 
 * @author 	pengpeng
 * @date 	2020年2月5日 下午10:15:48
 */
public interface ClientRegistrationRepositoryAdapter {

	public ClientRegistration findByRegistrationId(String registrationId);
	
}
