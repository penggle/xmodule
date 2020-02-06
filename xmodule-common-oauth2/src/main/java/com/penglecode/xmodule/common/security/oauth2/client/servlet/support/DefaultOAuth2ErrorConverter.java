package com.penglecode.xmodule.common.security.oauth2.client.servlet.support;

import java.util.Map;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;

/**
 * 默认的标准的OAuth2ErrorConverter
 * 
 * @author 	pengpeng
 * @date	2019年12月21日 下午4:26:09
 */
public class DefaultOAuth2ErrorConverter implements Converter<Map<String, String>, OAuth2Error> {
	
	@Override
	public OAuth2Error convert(Map<String, String> parameters) {
		String errorCode = parameters.get(OAuth2ParameterNames.ERROR);
		String errorDescription = parameters.get(OAuth2ParameterNames.ERROR_DESCRIPTION);
		String errorUri = parameters.get(OAuth2ParameterNames.ERROR_URI);

		return new OAuth2Error(errorCode, errorDescription, errorUri);
	}
	
}