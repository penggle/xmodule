package com.penglecode.xmodule.common.security.jwt;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * JWT令牌
 * 
 * @author 	pengpeng
 * @date	2019年12月25日 上午10:27:34
 */
public class JwtToken {

	/**
	 * JWT令牌值
	 */
	@JsonIgnore
	private String value;
	
	/**
	 * JWT令牌的来源(请求header/cookie)
	 */
	@JsonProperty("source")
	private TokenSource source;
	
	/**
	 * 用户名
	 */
	@JsonProperty("username")
	private String username;
	
	/**
	 * Token下发时间
	 */
	@JsonProperty("iat")
	private Instant issuedAt;
	
	/**
	 * Token过期时间
	 */
	@JsonProperty("exp")
	private Instant expiresAt;
	
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public TokenSource getSource() {
		return source;
	}

	public void setSource(TokenSource source) {
		this.source = source;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Instant getIssuedAt() {
		return issuedAt;
	}

	public void setIssuedAt(Instant issuedAt) {
		this.issuedAt = issuedAt;
	}

	public Instant getExpiresAt() {
		return expiresAt;
	}

	public void setExpiresAt(Instant expiresAt) {
		this.expiresAt = expiresAt;
	}

	/**
	 * 判断JWT令牌是否过期
	 * @return
	 */
	public boolean isValid() {
		return expiresAt != null && expiresAt.isAfter(Instant.now());
	}
	
	@Override
	public String toString() {
		return "JwtToken [source=" + source + ", username=" + username + ", issuedAt=" + issuedAt + ", expiresAt="
				+ expiresAt + "]";
	}

}
