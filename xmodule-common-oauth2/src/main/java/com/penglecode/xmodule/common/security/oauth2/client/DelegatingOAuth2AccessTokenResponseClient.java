package com.penglecode.xmodule.common.security.oauth2.client;

import java.lang.reflect.Field;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import org.springframework.security.oauth2.client.endpoint.AbstractOAuth2AuthorizationGrantRequest;
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.util.ReflectionUtils;

/**
 * 代理的OAuth2AccessTokenResponseClient，用来对OAuth2AccessTokenResponse进行进一步的处理
 * 
 * @param <T>
 * @author 	pengpeng
 * @date	2019年12月18日 下午1:48:22
 */
public class DelegatingOAuth2AccessTokenResponseClient<T extends AbstractOAuth2AuthorizationGrantRequest> implements OAuth2AccessTokenResponseClient<T> {

	private final OAuth2AccessTokenResponseClient<T> delegate;
	
	private final long secondsDiff;
	
	private final Field accessTokenField;
	
	private final Field refreshTokenField;
	
	public DelegatingOAuth2AccessTokenResponseClient(OAuth2AccessTokenResponseClient<T> delegate) {
		super();
		this.delegate = delegate;
		this.secondsDiff = computeSecondsDiff();
		this.accessTokenField = ReflectionUtils.findField(OAuth2AccessTokenResponse.class, "accessToken");
		this.accessTokenField.setAccessible(true);
		this.refreshTokenField = ReflectionUtils.findField(OAuth2AccessTokenResponse.class, "refreshToken");
		this.refreshTokenField.setAccessible(true);
	}

	@Override
	public OAuth2AccessTokenResponse getTokenResponse(T authorizationGrantRequest) {
		OAuth2AccessTokenResponse tokenResponse = delegate.getTokenResponse(authorizationGrantRequest);
		handleTokenResponse(tokenResponse);
		return tokenResponse;
	}

	protected void handleTokenResponse(OAuth2AccessTokenResponse tokenResponse) {
		OAuth2AccessToken oldAccessToken = tokenResponse.getAccessToken();
		if(oldAccessToken != null) {
			OAuth2AccessToken newAccessToken = new OAuth2AccessToken(oldAccessToken.getTokenType(), oldAccessToken.getTokenValue(), adjustInstant(oldAccessToken.getIssuedAt()), adjustInstant(oldAccessToken.getExpiresAt()), oldAccessToken.getScopes());
			ReflectionUtils.setField(accessTokenField, tokenResponse, newAccessToken);
		}
		OAuth2RefreshToken oldRefreshToken = tokenResponse.getRefreshToken();
		if(oldRefreshToken != null) {
			OAuth2RefreshToken newRefreshToken = new OAuth2RefreshToken(oldRefreshToken.getTokenValue(), adjustInstant(oldRefreshToken.getIssuedAt()));
			ReflectionUtils.setField(refreshTokenField, tokenResponse, newRefreshToken);
		}
	}
	
	private Instant adjustInstant(Instant instant) {
		if(instant != null) {
			return instant.plusSeconds(secondsDiff);
		}
		return instant;
	}
	
	/**
	 * 计算时间差(秒): UTC时间 - 系统默认时间
	 * @return
	 */
	protected long computeSecondsDiff() {
		ZoneId defaultZone = ZoneId.systemDefault();
		ZoneId utcZone = ZoneId.of("Z");
		LocalDateTime now = LocalDateTime.now(); //本地时间
		long localTimestamp = now.atZone(defaultZone).toInstant().getEpochSecond();
		long utcTimestamp = now.atZone(utcZone).toInstant().getEpochSecond();
		return utcTimestamp - localTimestamp;
	}

}
