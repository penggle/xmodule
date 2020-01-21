package com.penglecode.xmodule.common.security.consts;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Spring-Security应用程序常量
 * 
 * @author 	pengpeng
 * @date	2018年10月26日 下午1:02:36
 */
public class SecurityApplicationConstants {
	
	/**
	 * 系统默认的PasswordEncoder
	 */
	public static final PasswordEncoder DEFAULT_PASSWORD_ENCODER = new BCryptPasswordEncoder();
	
	/**
	 * 默认的ROLE投票前缀
	 */
	public static final String DEFAULT_ROLE_VOTE_PREFIX = "ROLE_";
	
}
