package com.penglecode.xmodule.security.oauth2.examples.config;

public class KeycloakUserStorageConfigProperties {
		
	private String name;
	
	private String providerId;
	
	private String jndiDataSourceName;

	public KeycloakUserStorageConfigProperties() {
		super();
	}

	public KeycloakUserStorageConfigProperties(String name, String providerId, String jndiDataSourceName) {
		super();
		this.name = name;
		this.providerId = providerId;
		this.jndiDataSourceName = jndiDataSourceName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getProviderId() {
		return providerId;
	}

	public void setProviderId(String providerId) {
		this.providerId = providerId;
	}

	public String getJndiDataSourceName() {
		return jndiDataSourceName;
	}

	public void setJndiDataSourceName(String jndiDataSourceName) {
		this.jndiDataSourceName = jndiDataSourceName;
	}
	
}