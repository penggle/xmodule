package com.penglecode.xmodule.security.oauth2.examples.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.penglecode.xmodule.common.util.DateTimeUtils;
import com.penglecode.xmodule.security.oauth2.common.model.OAuth2LoginUser;

@Configuration
public class KeycloakOAuth2ConfigProperties implements InitializingBean {

	private final String realm = "spring-security-oauth2";
	
	private final List<OAuth2LoginUser> users = new ArrayList<>();
	
	@Autowired
	@Qualifier("refreshClient")
	private KeycloakOAuth2ClientProperties refreshClient;
	
	@Autowired
	@Qualifier("passwordClient")
	private KeycloakOAuth2ClientProperties passwordClient;
	
	@Autowired
	@Qualifier("clientcredsClient")
	private KeycloakOAuth2ClientProperties clientcredsClient;

	@Value("http://127.0.0.1:${server.port:8080}")
	private String baseUrl;
	
	@Bean
	@ConfigurationProperties(prefix="spring.security.oauth2.client.registration.refresh-client")
	public KeycloakOAuth2ClientProperties refreshClient() {
		return new KeycloakOAuth2ClientProperties();
	}
	
	@Bean
	@ConfigurationProperties(prefix="spring.security.oauth2.client.registration.password-client")
	public KeycloakOAuth2ClientProperties passwordClient() {
		return new KeycloakOAuth2ClientProperties();
	}
	
	@Bean
	@ConfigurationProperties(prefix="spring.security.oauth2.client.registration.clientcreds-client")
	public KeycloakOAuth2ClientProperties clientcredsClient() {
		return new KeycloakOAuth2ClientProperties();
	}
	
	public KeycloakOAuth2ClientProperties getRefreshClient() {
		return refreshClient;
	}

	public KeycloakOAuth2ClientProperties getPasswordClient() {
		return passwordClient;
	}

	public KeycloakOAuth2ClientProperties getClientcredsClient() {
		return clientcredsClient;
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

	@Override
	public void afterPropertiesSet() throws Exception {
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
	
}
