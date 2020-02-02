package com.penglecode.xmodule.common.security.oauth2.consts;

import java.time.Clock;
import java.time.Duration;

import com.penglecode.xmodule.common.consts.Constant;
import com.penglecode.xmodule.common.consts.SpringEnvConstant;

/**
 * OAuth2应用常量
 * 
 * @author 	pengpeng
 * @date 	2020年2月1日 上午10:29:01
 */
public class OAuth2ApplicationConstants {

	/**
	 * OAuth2令牌有效期校准时间(即客户端认为令牌提前多久认为是失效的)，默认5分钟
	 */
	public static final Constant<Duration> DEFAULT_OAUTH2_CLIENT_CLOCK_SKEW = new SpringEnvConstant<Duration>("spring.security.oauth2.client.clock-skew", Duration.ofSeconds(300)) {};
	
	/**
	 * OAuth2客户端的时钟
	 */
	public static final Clock DEFAULT_OAUTH2_CLIENT_CLOCK = Clock.systemUTC();
}
