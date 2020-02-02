package com.penglecode.xmodule.common.security.oauth2.client.support;

import java.util.Collection;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.Assert;

/**
 * 执行OAuth2 Password模式登录的Authentication
 * 
 * @author 	pengpeng
 * @date 	2020年1月31日 下午10:40:41
 */
public final class OAuth2LoginAuthentication implements Authentication {
	
	private static final long serialVersionUID = 1L;
	
	private final String principalName;
	
	private final Collection<GrantedAuthority> authorities;

	public OAuth2LoginAuthentication(String principalName, Collection<GrantedAuthority> authorities) {
		super();
		Assert.hasText(principalName, "principalName cannot be empty");
		this.principalName = principalName;
		this.authorities = authorities;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public Object getCredentials() {
		return "";
	}

	@Override
	public Object getDetails() {
		return null;
	}

	@Override
	public Object getPrincipal() {
		return getName();
	}

	@Override
	public boolean isAuthenticated() {
		return true;
	}

	@Override
	public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
	}

	@Override
	public String getName() {
		return principalName;
	}
	
}
