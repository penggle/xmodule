package com.penglecode.xmodule.common.security.oauth2.resource.support;

import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityCoreVersion;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.AbstractOAuth2TokenAuthenticationToken;

/**
 * 自定义的JWT认证Authentication
 * 代码基本copy自#JwtAuthenticationToken
 * 
 * @author 	pengpeng
 * @date 	2020年2月3日 上午8:40:31
 */
public class OAuth2JwtAuthenticationToken extends AbstractOAuth2TokenAuthenticationToken<Jwt> {

	private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

	private final String name;
	
	public OAuth2JwtAuthenticationToken(Jwt jwt) {
		super(jwt);
		this.name = jwt.getSubject();
	}

	public OAuth2JwtAuthenticationToken(Jwt jwt, Collection<? extends GrantedAuthority> authorities) {
		super(jwt, authorities);
		this.setAuthenticated(true);
		this.name = jwt.getSubject();
	}

	public OAuth2JwtAuthenticationToken(Jwt jwt, Object principal, Collection<? extends GrantedAuthority> authorities, String name) {
		super(jwt, principal, jwt, authorities);
		this.setAuthenticated(true);
		this.name = name;
	}

	@Override
	public Map<String, Object> getTokenAttributes() {
		return this.getToken().getClaims();
	}

	@Override
	public String getName() {
		return this.name;
	}
	
}
