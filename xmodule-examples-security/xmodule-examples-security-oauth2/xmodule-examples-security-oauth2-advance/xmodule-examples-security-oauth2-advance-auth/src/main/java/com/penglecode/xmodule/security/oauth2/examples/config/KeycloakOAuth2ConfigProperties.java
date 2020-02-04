package com.penglecode.xmodule.security.oauth2.examples.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;

import com.penglecode.xmodule.common.util.DateTimeUtils;
import com.penglecode.xmodule.security.oauth2.common.model.OAuth2LoginUser;

public class KeycloakOAuth2ConfigProperties {

	private final String realm = "springcloud-security-oauth2";
	
	private final List<OAuth2LoginUser> users = new ArrayList<>();
	
	@Value("http://127.0.0.1:${server.port:8080}")
	private String baseUrl;
	
	private final Map<String, KeycloakOAuth2ClientProperties> registration = new HashMap<>();
	
	@PostConstruct
	public void init() {
		OAuth2LoginUser user = null;
		user = new OAuth2LoginUser();
		user.setUserId(1L);
		user.setUsername("tiger");
		user.setPassword("123456");
		user.setUserType(0);
		user.setEmail("tiger@qq.com");
		user.setMobilePhone("13812345678");
		user.setEnabled(true);
		user.setLoginTimes(0);
		user.setLastLoginTime(null);
		user.setCreateTime(DateTimeUtils.formatNow());
		users.add(user);
		
		user = new OAuth2LoginUser();
		user.setUserId(2L);
		user.setUsername("monkey");
		user.setPassword("123456");
		user.setUserType(0);
		user.setEmail("monkey@qq.com");
		user.setMobilePhone("13912345678");
		user.setEnabled(true);
		user.setLoginTimes(0);
		user.setLastLoginTime(null);
		user.setCreateTime(DateTimeUtils.formatNow());
		users.add(user);
	}
	
	public String getRealm() {
		return realm;
	}

	public String getBaseUrl() {
		return baseUrl;
	}
	
	public List<OAuth2LoginUser> getUsers() {
		return users;
	}

	public Map<String, KeycloakOAuth2ClientProperties> getRegistration() {
		return registration;
	}

}
