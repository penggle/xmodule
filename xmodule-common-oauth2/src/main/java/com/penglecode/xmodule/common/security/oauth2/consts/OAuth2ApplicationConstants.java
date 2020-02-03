package com.penglecode.xmodule.common.security.oauth2.consts;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.security.oauth2.core.AuthorizationGrantType;

import com.penglecode.xmodule.common.security.oauth2.client.support.OAuth2ClientConfigProperties;

/**
 * OAuth2应用常量
 * 
 * @author 	pengpeng
 * @date 	2020年2月1日 上午10:29:01
 */
public class OAuth2ApplicationConstants {

	/**
	 * OAuth2客户端的默认Scope
	 */
	public static final Map<AuthorizationGrantType,Set<String>> DEFAULT_OAUTH2_CLIENT_SCOPES = new HashMap<>();
	
	static {
		DEFAULT_OAUTH2_CLIENT_SCOPES.put(AuthorizationGrantType.AUTHORIZATION_CODE, new HashSet<String>(Arrays.asList("user")));
		DEFAULT_OAUTH2_CLIENT_SCOPES.put(AuthorizationGrantType.PASSWORD, new HashSet<String>(Arrays.asList("user")));
		DEFAULT_OAUTH2_CLIENT_SCOPES.put(AuthorizationGrantType.IMPLICIT, new HashSet<String>(Arrays.asList("user")));
		DEFAULT_OAUTH2_CLIENT_SCOPES.put(AuthorizationGrantType.CLIENT_CREDENTIALS, new HashSet<String>(Arrays.asList("app")));
		DEFAULT_OAUTH2_CLIENT_SCOPES.put(AuthorizationGrantType.REFRESH_TOKEN, new HashSet<String>(Arrays.asList("user", "app")));
	}
	
	/**
	 * OAuth2客户端的默认配置
	 */
	public static final OAuth2ClientConfigProperties DEFAULT_OAUTH2_CLIENT_CONFIG = new OAuth2ClientConfigProperties();
	
}
