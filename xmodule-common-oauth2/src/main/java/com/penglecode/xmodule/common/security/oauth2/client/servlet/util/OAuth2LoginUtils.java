package com.penglecode.xmodule.common.security.oauth2.client.servlet.util;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Map;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.client.OAuth2LoginConfigurer;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.util.Assert;

import com.penglecode.xmodule.common.security.servlet.util.SpringSecurityUtils;
import com.penglecode.xmodule.common.util.ObjectUtils;
import com.penglecode.xmodule.common.util.ReflectionUtils;

/**
 * 基于OAuth2 authorization_code|implicit模式登录工具类
 * 
 * @author 	pengpeng
 * @date 	2020年1月28日 下午3:56:04
 */
@SuppressWarnings("unchecked")
public class OAuth2LoginUtils {

	/**
	 * 获取当前登录OAuth2用户的用户详情
	 * @return
	 */
	public static Map<String,Object> getCurrentOAuth2UserInfo() {
		return getOAuth2UserInfo(SpringSecurityUtils.getCurrentAuthentication());
	}
	
	/**
	 * 获取OAuth2用户的用户详情
	 * @param authentication
	 * @return
	 */
	public static Map<String,Object> getOAuth2UserInfo(OAuth2AuthenticationToken authentication) {
		Assert.notNull(authentication, "Parameter 'authentication' can not be null!");
		Map<String,Object> userInfo = null;
		OAuth2User oauth2User = authentication.getPrincipal();
		userInfo = oauth2User.getAttributes();
		return ObjectUtils.defaultIfNull(userInfo, Collections.EMPTY_MAP);
	}
	
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
