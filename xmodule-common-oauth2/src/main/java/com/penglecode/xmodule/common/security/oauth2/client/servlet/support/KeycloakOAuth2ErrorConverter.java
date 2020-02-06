package com.penglecode.xmodule.common.security.oauth2.client.servlet.support;

import java.util.Map;

import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;

import com.penglecode.xmodule.common.consts.ApplicationConstants;

/**
 * Keycloak的OAuth2ErrorConverter
 * 
 * @author 	pengpeng
 * @date	2019年12月21日 下午4:26:09
 */
public class KeycloakOAuth2ErrorConverter implements Converter<Map<String, String>, OAuth2Error> {
	
	@Override
	public OAuth2Error convert(Map<String, String> parameters) {
		MessageSourceAccessor messageSourceAccessor = ApplicationConstants.DEFAULT_MESSAGE_SOURCE_ACCESSOR.get();
		String errorCode = parameters.get(OAuth2ParameterNames.ERROR);
		String errorDescription = parameters.get(OAuth2ParameterNames.ERROR_DESCRIPTION);
		String errorUri = parameters.get(OAuth2ParameterNames.ERROR_URI);
		
		if("invalid_grant".equals(errorCode) && errorDescription != null && errorDescription.contains("Invalid user credentials")) {
			errorDescription = messageSourceAccessor.getMessage("spring.security.oauth2.error.invalid_user_credentials", errorDescription);
		} else if ("Incorrect result size: expected 1, actual 0".equals(errorCode)) {
			errorCode = "invalid_grant";
			errorDescription = messageSourceAccessor.getMessage("spring.security.oauth2.error.invalid_user_credentials", errorDescription);
		} else if("unauthorized_client".equals(errorCode) && errorDescription != null && errorDescription.contains("Invalid client secret")) {
			errorDescription = messageSourceAccessor.getMessage("spring.security.oauth2.error.invalid_client_secret", errorDescription);
		}
		return new OAuth2Error(errorCode, errorDescription, errorUri);
	}
	
}