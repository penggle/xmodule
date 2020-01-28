package com.penglecode.xmodule.common.oauth2.client.util;

import java.lang.reflect.Method;
import java.util.Map;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.client.OAuth2LoginConfigurer;

import com.penglecode.xmodule.common.security.servlet.util.SpringSecurityUtils;
import com.penglecode.xmodule.common.util.ReflectionUtils;

/**
 * OAuth2登录工具类
 * 
 * @author 	pengpeng
 * @date 	2020年1月28日 下午3:56:04
 */
@SuppressWarnings("unchecked")
public class OAuth2LoginUtils {

	/**
	 * 获取已配置的OAuth2登录连接
	 * @return 例如：{/oauth2/authorization/github=GitHub, /oauth2/authorization/google=Google}
	 */
	public static Map<String,String> getConfiguredOAuth2LoginLinks() {
		HttpSecurity http = SpringSecurityUtils.getPrimaryHttpSecurity();
		OAuth2LoginConfigurer<?> oauth2Login = http.getConfigurer(OAuth2LoginConfigurer.class);
		Method method = ReflectionUtils.findMethod(OAuth2LoginConfigurer.class, "getLoginLinks");
		method.setAccessible(true);
		return (Map<String, String>) ReflectionUtils.invokeMethod(method, oauth2Login);
	}
	
}
