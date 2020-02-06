package com.penglecode.xmodule.common.security.reactive.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;

import reactor.core.publisher.Mono;

/**
 * Reactive Web环境下的spring-security工具类
 * 
 * @author 	pengpeng
 * @date 	2020年2月4日 下午10:18:05
 */
@SuppressWarnings("unchecked")
public class SpringSecurityUtils {

	/**
	 * 获取当前登录身份证明(Authentication)
	 * @param <T>
	 * @return
	 */
	public static <T extends Authentication> Mono<T> getCurrentAuthentication() {
		return (Mono<T>) ReactiveSecurityContextHolder.getContext().map(SecurityContext::getAuthentication);
	}
	
}
