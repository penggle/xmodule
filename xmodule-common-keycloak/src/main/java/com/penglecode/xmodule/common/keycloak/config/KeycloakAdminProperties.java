package com.penglecode.xmodule.common.keycloak.config;

public class KeycloakAdminProperties {

	public static final String DEFAULT_PROPERTIES_CONFIG_PREFIX = "keycloak-admin";
	
	private String serverUrl;
	
	private String realm = "master";
	
	private String username;
	
	private String password;
	
	private String clientId = "admin-cli";

	public String getServerUrl() {
		return serverUrl;
	}

	public void setServerUrl(String serverUrl) {
		this.serverUrl = serverUrl;
	}

	public String getRealm() {
		return realm;
	}

	public void setRealm(String realm) {
		this.realm = realm;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	
}
