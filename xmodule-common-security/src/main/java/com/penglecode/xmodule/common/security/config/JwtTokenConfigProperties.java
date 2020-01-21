package com.penglecode.xmodule.common.security.config;

/**
 * JWT令牌配置
 * 
 * @author 	pengpeng
 * @date	2019年12月24日 下午8:16:22
 */
public class JwtTokenConfigProperties {

	/**
	 * JWT令牌在请求header/cookie中的name
	 */
	private String name = "Authorization";
	
	/**
	 * HS加密算法的秘钥
	 */
	private String secret;
	
	/**
	 * 过期时间(秒)
	 */
	private Long expires;
	
	/**
	 * 旧的JWT令牌的短暂过渡时间(秒)
	 */
	private Long transients = 60L;
	
	/**
	 * JWT令牌自动续约提前时间(比如在过期前提前5分钟续约)
	 */
	private Long renewalAheadTime = 5 * 60L;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public Long getExpires() {
		return expires;
	}

	public void setExpires(Long expires) {
		this.expires = expires;
	}

	public Long getTransients() {
		return transients;
	}

	public void setTransients(Long transients) {
		this.transients = transients;
	}

	public Long getRenewalAheadTime() {
		return renewalAheadTime;
	}

	public void setRenewalAheadTime(Long renewalAheadTime) {
		this.renewalAheadTime = renewalAheadTime;
	}
	
}