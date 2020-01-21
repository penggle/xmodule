package com.penglecode.xmodule.common.security.jwt;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 基于JWT的AccessToken供前端使用
 * 
 * @author 	pengpeng
 * @date	2019年12月27日 下午2:39:32
 */
public class JwtAccessToken {

	/**
	 * JWT令牌
	 */
	@JsonProperty("access_token")
	private String accessToken;
	
	/**
	 * JWT令牌来源
	 */
	@JsonIgnore
	private TokenSource tokenSource;
	
	/**
	 * 有效期(秒)
	 */
	@JsonProperty("expires_in")
	private int expiresIn;
	
	public JwtAccessToken() {
		super();
	}

	public JwtAccessToken(String accessToken, TokenSource tokenSource, int expiresIn) {
		super();
		this.accessToken = accessToken;
		this.tokenSource = tokenSource;
		this.expiresIn = expiresIn;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public TokenSource getTokenSource() {
		return tokenSource;
	}

	public void setTokenSource(TokenSource tokenSource) {
		this.tokenSource = tokenSource;
	}

	public int getExpiresIn() {
		return expiresIn;
	}

	public void setExpiresIn(int expiresIn) {
		this.expiresIn = expiresIn;
	}

	@Override
	public String toString() {
		return "JwtAccessToken [accessToken=" + accessToken + ", tokenSource=" + tokenSource + ", expiresIn=" + expiresIn
				+ "]";
	}
	
}