package com.penglecode.xmodule.common.security.oauth2.client.servlet.support;

import java.time.Clock;
import java.time.Duration;

import org.springframework.beans.factory.InitializingBean;

import com.penglecode.xmodule.common.util.SpringUtils;
import com.penglecode.xmodule.common.util.StringUtils;

/**
 * OAuth2客户端的相关配置
 * 
 * @author 	pengpeng
 * @date 	2020年2月3日 下午3:27:17
 */
public class OAuth2ClientConfigProperties implements InitializingBean {

	/**
	 * OAuth2令牌有效期校准时间(即客户端认为令牌提前多久认为是失效的)，默认5分钟
	 */
	private Duration clockSkew = Duration.ofSeconds(600);
	
	/**
	 * OAuth2令牌有效期校准的时钟
	 */
	private Clock clock = Clock.systemUTC();
	
	/**
	 * 用户登录令牌的失效时间(包括AUTHORIZATION_CODE,IMPLICIT,PASSWORD三者模式的)
	 * (注意在KC中timeout最大值不能超过Realm.ssoSessionMaxLifespan)
	 */
	private Duration userTokenTimeout;
	
	/**
	 * API应用之间相互调用鉴权令牌失效时间(包括CLIENT_CREDENTIALS)
	 * (注意在KC中timeout最大值不能超过Realm.ssoSessionMaxLifespan)
	 */
	private Duration appTokenTimeout;
	
	/**
	 * 用于用户登录的注册客户端的ID
	 */
	private String userRegistrationId;
	
	/**
	 * 用于API应用之间相互调用鉴权的注册客户端的ID
	 */
	private String appRegistrationId;

	public Duration getClockSkew() {
		return clockSkew;
	}

	public void setClockSkew(Duration clockSkew) {
		this.clockSkew = clockSkew;
	}

	public Clock getClock() {
		return clock;
	}

	public void setClock(Clock clock) {
		this.clock = clock;
	}

	public Duration getUserTokenTimeout() {
		return userTokenTimeout;
	}

	public void setUserTokenTimeout(String userTokenTimeout) {
		if(!StringUtils.isEmpty(userTokenTimeout)) {
			Long timeoutSeconds = null;
			try {
				timeoutSeconds = Long.parseLong(userTokenTimeout);
			} catch (NumberFormatException e) {
			}
			if(timeoutSeconds != null) {
				this.userTokenTimeout = Duration.ofSeconds(timeoutSeconds);
			} else {
				this.userTokenTimeout = Duration.parse(userTokenTimeout);
			}
		}
	}

	public Duration getAppTokenTimeout() {
		return appTokenTimeout;
	}

	public void setAppTokenTimeout(String appTokenTimeout) {
		if(!StringUtils.isEmpty(appTokenTimeout)) {
			Long timeoutSeconds = null;
			try {
				timeoutSeconds = Long.parseLong(appTokenTimeout);
			} catch (NumberFormatException e) {
			}
			if(timeoutSeconds != null) {
				this.appTokenTimeout = Duration.ofSeconds(timeoutSeconds);
			} else {
				this.appTokenTimeout = Duration.parse(appTokenTimeout);
			}
		}
	}

	public String getUserRegistrationId() {
		return userRegistrationId;
	}

	public void setUserRegistrationId(String userRegistrationId) {
		this.userRegistrationId = userRegistrationId;
	}

	public String getAppRegistrationId() {
		return appRegistrationId;
	}

	public void setAppRegistrationId(String appRegistrationId) {
		this.appRegistrationId = appRegistrationId;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		if(this.appTokenTimeout == null) {
			this.appTokenTimeout = Duration.ofSeconds(3600 * 24);
		}
		if(this.userTokenTimeout == null) {
			this.userTokenTimeout = Duration.ofSeconds(SpringUtils.getEnvironment().getProperty("server.servlet.session.timeout", Long.class, 7200L));
		}
	}
	
}
