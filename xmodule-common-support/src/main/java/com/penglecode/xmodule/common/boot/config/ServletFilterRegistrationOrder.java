package com.penglecode.xmodule.common.boot.config;

import org.springframework.boot.web.servlet.filter.OrderedFilter;
import org.springframework.core.Ordered;

public class ServletFilterRegistrationOrder {

	/**
	 * SpringSecurity过滤器链的注册顺序(见application.yml中配置的spring.security.filter.order)
	 */
	public static final int ORDER_SPRING_SECURITY_FILTER_CHAIN = Ordered.HIGHEST_PRECEDENCE + 20;
	
	/**
	 * RequestContextFilter过滤器的注册顺序(必须在ORDER_HTTP_ACCESS_LOGGING_FILTER之前)
	 */
	public static final int ORDER_REQUEST_CONTEXT_FILTER = OrderedFilter.HIGHEST_PRECEDENCE + 21;
	
	/**
	 * Http访问日志记录过滤器的注册顺序
	 */
	public static final int ORDER_HTTP_ACCESS_LOGGING_FILTER = Ordered.HIGHEST_PRECEDENCE + 30;
	
}
