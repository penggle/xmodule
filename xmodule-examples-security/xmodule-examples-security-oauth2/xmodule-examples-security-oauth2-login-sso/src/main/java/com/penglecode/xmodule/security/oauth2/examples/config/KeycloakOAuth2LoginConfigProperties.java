package com.penglecode.xmodule.security.oauth2.examples.config;

import org.springframework.beans.factory.annotation.Value;

public class KeycloakOAuth2LoginConfigProperties {

	private String realm = "spring-security-oauth2";
	
	private String clientId;
	
	private String clientName;
	
	private String clientSecret;
	
	private String redirectUri;
	
	private String authorizationGrantType;
	
	private String[] scope;
	
	@Value("http://127.0.0.1:${server.port:8080}")
	private String baseUrl;
	
	/**
	 * 登录用户名
	 */
	private String loginUsername = "pengsan";
	
	/**
	 * 登录密码
	 */
	private String loginPassword = "123456";
	
	public String getRealm() {
		return realm;
	}

	public void setRealm(String realm) {
		this.realm = realm;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public String getClientSecret() {
		return clientSecret;
	}

	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}

	public String getRedirectUri() {
		return redirectUri;
	}

	public void setRedirectUri(String redirectUri) {
		this.redirectUri = redirectUri;
	}

	public String getAuthorizationGrantType() {
		return authorizationGrantType;
	}

	public void setAuthorizationGrantType(String authorizationGrantType) {
		this.authorizationGrantType = authorizationGrantType;
	}

	public String[] getScope() {
		return scope;
	}

	public void setScope(String[] scope) {
		this.scope = scope;
	}

	public String getBaseUrl() {
		return baseUrl;
	}

	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	public String getLoginUsername() {
		return loginUsername;
	}

	public void setLoginUsername(String loginUsername) {
		this.loginUsername = loginUsername;
	}

	public String getLoginPassword() {
		return loginPassword;
	}

	public void setLoginPassword(String loginPassword) {
		this.loginPassword = loginPassword;
	}
	
}
