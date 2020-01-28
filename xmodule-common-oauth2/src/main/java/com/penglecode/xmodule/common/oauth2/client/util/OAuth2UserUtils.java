package com.penglecode.xmodule.common.oauth2.client.util;

import java.util.Collections;
import java.util.Map;

import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.util.Assert;

import com.penglecode.xmodule.common.security.servlet.util.SpringSecurityUtils;
import com.penglecode.xmodule.common.util.ObjectUtils;

/**
 * 有关OAuth2用户的工具类
 * 
 * @author 	pengpeng
 * @date 	2020年1月26日 下午10:08:47
 */
@SuppressWarnings("unchecked")
public class OAuth2UserUtils {

	/**
	 * 获取当前登录OAuth2用户的用户详情
	 * @return
	 */
	public static Map<String,Object> getCurrentOAuth2UserInfo() {
		return getOAuth2UserInfo(SpringSecurityUtils.getAuthentication());
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
	
}
